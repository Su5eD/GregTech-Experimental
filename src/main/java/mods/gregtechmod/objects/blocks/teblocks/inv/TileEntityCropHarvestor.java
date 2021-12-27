package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiCropHarvestor;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class TileEntityCropHarvestor extends TileEntityElectricBuffer {

    public TileEntityCropHarvestor() {
        super(1);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected int getMinimumStoredEU() {
        return 1000 + getOverclockerMultiplier() * 100;
    }

    @Override
    protected void work() {
        if (workJustHasBeenEnabled() || this.tickCounter % 20 == 0) {
            int overclockers = getUpgradeCount(IC2UpgradeType.OVERCLOCKER);
            int multiplier = getOverclockerMultiplier();
            int harvestEnergy = 100 * multiplier;
            if (canUseEnergy(harvestEnergy)) {
                int range = 1 << overclockers;
                for (int i = 0; i < range; i++) {
                    TileEntity te = this.world.getTileEntity(this.pos.offset(getFacing(), i + 1));
                    if (te instanceof ICropTile) {
                        ICropTile cropTile = (ICropTile) te;
                        CropCard crop = cropTile.getCrop();
                        if (!crop.canGrow(cropTile) && crop.canBeHarvested(cropTile) && cropTile.performManualHarvest()) {
                            useEnergy(harvestEnergy);
                            break;
                        }
                    }

                    int collectEnergy = 64 * multiplier;
                    if (this.buffer.isEmpty() && canUseEnergy(collectEnergy)) {
                        BlockPos offset = this.pos.offset(getFacing(), i + 1);
                        ItemStack stack = GtUtil.collectItemFromArea(this.world, offset, offset.add(1, 1, 1));
                        if (!stack.isEmpty()) {
                            this.buffer.put(stack);
                            useEnergy(collectEnergy);
                        }
                    }
                }
            }
        }
        super.work();
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return IC2UpgradeType.DEFAULT;
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public ContainerElectricBufferSmall<?> getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBufferSmall<>(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiCropHarvestor(getGuiContainer(player));
    }
}
