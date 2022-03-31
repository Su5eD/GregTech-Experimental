package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.util.ITextureMode;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityMultiMode extends TileEntityCoverBehavior {

    protected abstract ITextureMode getMode();
    
    protected abstract void updateMode();

    protected abstract int getTextureIndex();

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        return super.getExtendedState(state)
            .withProperty(PropertyHelper.TEXTURE_MODE_INFO_PROPERTY, new PropertyHelper.TextureModeInfo(getMode(), getTextureIndex()));
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (side == getFacing()) {
            if (!this.world.isRemote) {
                updateMode();
                rerender();
            }
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }
}
