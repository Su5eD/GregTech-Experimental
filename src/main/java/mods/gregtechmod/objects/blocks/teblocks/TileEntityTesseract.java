package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.function.Function;

public abstract class TileEntityTesseract extends TileEntityUpgradable implements IPanelInfoProvider {
    @NBTPersistent
    protected int frequency = 0;
    protected boolean frequencyMessageTesseractGlobal;

    protected TileEntityTesseract(String descriptionKey) {
        super(descriptionKey);
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side == getFacing()) {
            int screw = getUpdatedFrequency(side, hitX, hitY, hitZ);
            
            if (screw < 2) {
                if (!this.world.isRemote) beforeFrequencyChanged();
                
                if (screw == 1) ++this.frequency;
                else if (screw == 0) --this.frequency;
                
                if (!this.world.isRemote) onFrequencyChanged();
            }
            
            if (!this.world.isRemote) sendFrequencyMessage(player);
            return true;
        }
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (side == getFacing()) {
            int screw = getUpdatedFrequency(side, hitX, hitY, hitZ);

            if (!this.world.isRemote) beforeFrequencyChanged();
            
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
            
            if (!this.world.isRemote) {
                onFrequencyChanged();
                sendFrequencyMessage(player);
            }
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
    
    protected abstract String getExistingTesseractMessage();
    protected void beforeFrequencyChanged() {}
    protected void onFrequencyChanged() {}

    private void sendFrequencyMessage(EntityPlayer player) {
        TileEntityTesseractGenerator gen = TileEntityTesseractGenerator.getTesseract(this.frequency, this.world, this.frequencyMessageTesseractGlobal ? null : getOwner());
        String msg = GtLocale.translateTeBlock("tesseract", "frequency", this.frequency);
        if (gen != null && gen != this) msg += GtLocale.translateTeBlock("tesseract", getExistingTesseractMessage());
        
        GtUtil.sendMessage(player, msg);
    }
    
    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }
    
    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return delegatePanelInfo(GtLocale.translateTeBlockName(this.teBlock), IPanelInfoProvider::getMainInfo);
    }

    @Override
    public String getSecondaryInfo() {
        return delegatePanelInfo(GtLocale.translateTeBlock("tesseract", "frequency_short", this.frequency), IPanelInfoProvider::getSecondaryInfo);
    }

    @Override
    public String getTertiaryInfo() {
        String msg = GtLocale.translateInfo(TileEntityTesseractGenerator.getTesseract(this.frequency, this.world, getOwner()) == this ? "active" : "inactive");
        return delegatePanelInfo(msg, IPanelInfoProvider::getTertiaryInfo);
    }
    
    protected abstract TileEntity getPanelInfoTE();

    private String delegatePanelInfo(String def, Function<IPanelInfoProvider, String> func) {
        TileEntity te = getPanelInfoTE();
        if (te instanceof IPanelInfoProvider && ((IPanelInfoProvider) te).isGivingInformation() && isAllowedToWork()) {
            return func.apply((IPanelInfoProvider) te);
        }
        return def;
    }
}
