package mods.gregtechmod.objects.blocks.teblocks;

import com.mojang.authlib.GameProfile;
import ic2.core.util.Util;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.PropertyHelper.VerticalRotation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class TileEntityTesseractGenerator extends TileEntityTesseract {
    private static final Map<Integer, TileEntityTesseractGenerator> TESSERACTS = new HashMap<>();

    private final Set<TileEntityTesseractTerminal> connections = new HashSet<>();

    private int isWorking;
    private int energyConsumption;

    public TileEntityTesseractGenerator() {
        this.frequencyMessageTesseractGlobal = true;

        setPrivate(true);
    }

    public static TileEntityTesseractGenerator getTesseract(int frequency, World world, GameProfile owner) {
        return getTesseract(frequency, world, owner, false);
    }

    public static TileEntityTesseractGenerator getTesseract(int frequency, World world, GameProfile owner, boolean workIrrelevant) {
        TileEntityTesseractGenerator generator = TESSERACTS.get(frequency);
        if (generator == null || generator.isInvalid(owner, workIrrelevant) || !GregTechConfig.MACHINES.TESSERACT.interdimensional && generator.world != world) return null;
        return generator;
    }

    public static void onServerStopping() {
        TESSERACTS.clear();
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        TESSERACTS.remove(this.frequency);
    }

    @Override
    protected void beforeFrequencyChanged() {
        if (TESSERACTS.get(this.frequency) == this) unregister();
    }

    @Override
    protected void onFrequencyChanged() {
        disconnect();
    }

    @Override
    protected boolean isTesseractActive() {
        return TESSERACTS.get(this.frequency) == this;
    }

    private boolean isInvalid(GameProfile owner, boolean workIrrelevant) {
        return isInvalid() || !isAllowedToWork() || owner != null && !checkAccess(owner) || !workIrrelevant && this.isWorking < 20;
    }

    public boolean ensureConnection(TileEntityTesseractTerminal terminal) {
        if (isAllowedToWork()) {
            if (this.connections.contains(terminal)) {
                if (tryUseEnergy(this.energyConsumption, true)) return true;
                else disconnectTerminal(terminal);
            }
            else if (tryUseEnergy(this.energyConsumption + getEnergyConsumption(terminal), true)) {
                this.connections.add(terminal);
                updateEnergyConsumption();
                return true;
            }
        }
        return false;
    }

    private void disconnect() {
        this.connections.clear();
        this.energyConsumption = 0;
    }

    public void disconnectTerminal(TileEntityTesseractTerminal terminal) {
        this.connections.remove(terminal);
        updateEnergyConsumption();
    }

    private void unregister() {
        disconnect();
        TESSERACTS.remove(this.frequency);
    }

    private int getEnergyConsumption(TileEntityTesseractTerminal terminal) {
        return terminal.getWorld() == this.world
            ? GregTechConfig.MACHINES.TESSERACT.energyPerTick
            : GregTechConfig.MACHINES.TESSERACT.interDimensionalEnergyPerTick;
    }

    private void updateEnergyConsumption() {
        this.energyConsumption = this.connections.stream().mapToInt(this::getEnergyConsumption).sum();
    }

    public TileEntity getTargetTileEntity() {
        BlockPos pos = this.pos.offset(getFacing().getOpposite());
        return this.world.getTileEntity(pos);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        TileEntityTesseractGenerator gen = TESSERACTS.get(this.frequency);
        if (isAllowedToWork() && tryUseEnergy(this.energyConsumption)) {
            if (gen == null || gen.isInvalid(null, true)) {
                TESSERACTS.put(this.frequency, this);
            }
        }

        if (TESSERACTS.get(this.frequency) == this) {
            if (this.isWorking <= 20) ++this.isWorking;
            if (this.isWorking == 20) updateRender();
        }
        else {
            this.isWorking = 0;
        }
    }

    @Override
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public long getMjCapacity() {
        return getEUCapacity();
    }

    @Override
    public int getSteamCapacity() {
        return getEUCapacity();
    }

    @Override
    protected String getExistingTesseractMessage() {
        return "frequency_occupied";
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    protected VerticalRotation getVerticalRotation() {
        return VerticalRotation.ROTATE_X;
    }
}
