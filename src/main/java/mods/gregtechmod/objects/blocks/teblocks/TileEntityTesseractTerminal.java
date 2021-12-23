package mods.gregtechmod.objects.blocks.teblocks;

import com.google.common.collect.Sets;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Collections;
import java.util.Set;

public class TileEntityTesseractTerminal extends TileEntityTesseract {
    private static final Set<Capability<?>> ALLOWED_CAPABILITIES = Sets.newHashSet(
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
    );
    
    public boolean isConnected;

    public TileEntityTesseractTerminal() {
        setPrivate(true);
    }

    private TileEntityTesseractGenerator getTesseractGen(boolean workIrrevelant) {
        TileEntityTesseractGenerator gen = TileEntityTesseractGenerator.getTesseract(this.frequency, this.world, getOwner(), workIrrevelant);
        return gen != null && gen.ensureConnection(this) ? gen : null;
    }

    public TileEntity getTargetGeneratorTE() {
        TileEntityTesseractGenerator gen = getTesseractGen(false);
        return gen != null ? gen.getTargetTileEntity() : null;
    }
    
    private void disconnect() {
        TileEntityTesseractGenerator gen = getTesseractGen(false);
        if (gen != null) gen.disconnectTerminal(this);
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        disconnect();
    }

    @Override
    protected void beforeFrequencyChanged() {
        disconnect();
    }

    @Override
    protected boolean isTesseractActive() {
        return getTesseractGen(false) != null;
    }

    @Override
    protected String getExistingTesseractMessage() {
        if (getTesseractGen(false) == null) return "frequency_underpowered";
        return "frequency_connected";
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing side) {
        if (ALLOWED_CAPABILITIES.contains(capability)) {
            TileEntity target = getTargetGeneratorTE();
            if (target != null) return target.hasCapability(capability, side);
        }
        
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (ALLOWED_CAPABILITIES.contains(capability)) {
            TileEntity target = getTargetGeneratorTE();
            if (target != null) return target.getCapability(capability, side);
        }
        
        return null;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork()) {
            TileEntityTesseractGenerator gen = getTesseractGen(true);
            if (gen != null && gen.ensureConnection(this)) {
                if (!this.isConnected && getTesseractGen(false) != null) {
                    this.isConnected = true;
                    updateConnections();
                }
            } else if (this.isConnected) {
                this.isConnected = false;
                updateConnections();
            }
        }
    }
    
    private void updateConnections() {
        rerender();
        this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, false);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 0;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.emptySet();
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }
}
