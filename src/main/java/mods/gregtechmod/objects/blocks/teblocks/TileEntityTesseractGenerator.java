package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.PropertyHelper.VerticalRotation;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.*;
import java.util.function.Function;

public class TileEntityTesseractGenerator extends TileEntityUpgradable implements IPanelInfoProvider {
    public static final Map<Integer, TileEntityTesseractGenerator> tesseractGenerators = new HashMap<>(); // TODO World Saved Data
    
    public static int neededEnergy;
    
    public int isWorking = 0;
    @NBTPersistent
    public int frequency = 0;
    @NBTPersistent
    public int oldFrequency = 0;

    public TileEntityTesseractGenerator() {
        super("tesseract_generator");
        
        setPrivate(true);
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
    protected void onUnloaded() {
        super.onUnloaded();
        tesseractGenerators.remove(this.frequency);
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side == getFacing()) {
            int screw = getUpdatedFrequency(side, hitX, hitY, hitZ);
            
            if (screw == 1) ++this.frequency;
            else if (screw == 0) --this.frequency;
            
            sendFrequencyMessage(player);
            return true;
        }
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (side == getFacing()) {
            int screw = getUpdatedFrequency(side, hitX, hitY, hitZ);

            switch (screw) {
                case 0:
                    this.frequency -= 64;
                    break;
                case 1:
                    this.frequency += 64;
                    break;
                case 2:
                    this.frequency -= 512;
                    break;
                case 3:
                    this.frequency += 512;
                    break;
            }

            sendFrequencyMessage(player);
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }
    
    public int getUpdatedFrequency(EnumFacing side, float x, float y, float z) {
        int screw = 0;
        switch (side) {
            case DOWN:
            case UP:
                screw = (int) (x * 2 + 2 * (int) (z * 2));
                break;
            case NORTH:
                screw = (int) (2 - x * 2 + 2 * (int) (2 - y * 2));
                break;
            case SOUTH:
                screw = (int) (x * 2 + 2 * (int) (2 - y * 2));
                break;
            case WEST:
                screw = (int) (z * 2 + 2 * (int) (2 - y * 2));
                break;
            case EAST:
                screw = (int) (2 - z * 2 + 2 * (int) (2 - y * 2));
                break;
        }
        return screw;
    }
    
    public void sendFrequencyMessage(EntityPlayer player) {
        TileEntityTesseractGenerator gen = tesseractGenerators.get(this.frequency);
        String msg = GtUtil.translateTeBlock("tesseract", "frequency", this.frequency);
        if (gen != null && gen != this) msg += GtUtil.translateTeBlock("tesseract", "frequency_occupied");
        
        GtUtil.sendMessage(player, msg);
    }
    
    public boolean isValidTesseractGenerator(EntityPlayer player, boolean workIrrelevant) {
        return !isInvalid() && isAllowedToWork() && (player == null || checkAccess(player)) && (workIrrelevant || this.isWorking >= 20);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.frequency != this.oldFrequency) {
            if (tesseractGenerators.get(this.oldFrequency) == this) {
                tesseractGenerators.remove(this.oldFrequency);
                rerender();
            }
            this.oldFrequency = this.frequency;
        }
        
        TileEntityTesseractGenerator gen = tesseractGenerators.get(this.frequency);
        if (isAllowedToWork() && tryUseEnergy(neededEnergy)) {
            if (gen == null || !gen.isValidTesseractGenerator(null, true)) {
                tesseractGenerators.put(this.frequency, this);
            }
        } else if (gen == this) {
            tesseractGenerators.remove(this.frequency);
            rerender();
        }
        
        if (tesseractGenerators.get(this.frequency) == this) {
            if (this.isWorking < 20)
                this.isWorking = (byte) (this.isWorking + 1);
            if (this.isWorking == 20) {
                rerender();
                this.isWorking = (byte) (this.isWorking + 1);
            }
        } else {
            this.isWorking = 0;
        }
        
        neededEnergy = 0;
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
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

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return delegatePanelInfo(GtUtil.translateTeBlockName(this.teBlock), IPanelInfoProvider::getMainInfo);
    }

    @Override
    public String getSecondaryInfo() {
        return delegatePanelInfo(GtUtil.translateTeBlock(this.teBlock, "frequency_short", this.frequency), IPanelInfoProvider::getSecondaryInfo);
    }

    @Override
    public String getTertiaryInfo() {
        String msg = GtUtil.translateInfo(tesseractGenerators.get(this.frequency) == this ? "active" : "inactive");
        return delegatePanelInfo(msg, IPanelInfoProvider::getTertiaryInfo);
    }

    private String delegatePanelInfo(String def, Function<IPanelInfoProvider, String> func) {
        TileEntity te = this.world.getTileEntity(this.pos.offset(getFacing().getOpposite()));
        if (te instanceof IPanelInfoProvider && ((IPanelInfoProvider) te).isGivingInformation() && isAllowedToWork()) {
            return func.apply((IPanelInfoProvider) te);
        }
        return def;
    }
}
