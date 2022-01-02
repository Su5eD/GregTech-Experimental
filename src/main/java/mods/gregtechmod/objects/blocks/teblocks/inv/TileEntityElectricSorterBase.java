package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.PropertyHelper.TextureOverride;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class TileEntityElectricSorterBase extends TileEntityElectricBuffer {
    private static final ResourceLocation TARGET_FACING_TEXTURE = new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_pipe_blue");
    private static final ResourceLocation TARGET_FACING_TEXTURE_REDSTONE = new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_pipe_blue_redstone");
    
    @NBTPersistent
    private EnumFacing targetFacing = EnumFacing.DOWN;

    public TileEntityElectricSorterBase() {
        super(1);
    }
    
    public void switchTargetFacing() {
        this.targetFacing = GtUtil.getNextFacing(this.targetFacing);

        updateClientField("targetFacing");
    }

    public EnumFacing getTargetFacing() {
        return this.targetFacing;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected int getMinimumStoredEU() {
        return 2000;
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        ResourceLocation texture = this.emitter.emitsRedstone() ? TARGET_FACING_TEXTURE_REDSTONE : TARGET_FACING_TEXTURE;
        return super.getExtendedState(state)
                .withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new TextureOverride(this.targetFacing, texture, true));
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote && side == getTargetFacing()) {
            updateTargetStackSize(player);
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }

    @Override
    protected int moveItem() {
        ItemStack stack = this.buffer.get();
        
        if (applyFilter(stack)) {
            int cost = moveItemStack(getNeighborTE(this.targetFacing), this.targetFacing);
            if (cost > 0) return cost;
        }
        
        return super.moveItem();
    }
    
    protected abstract boolean applyFilter(ItemStack stack);

    @Override
    protected int getMoveCostMultiplier() {
        return 3;
    }

    @Override
    protected int getMaxSuccess() {
        return 30;
    }

    @Override
    protected boolean shouldUpdate(boolean hasItem) {
        return canUseEnergy(1000) && (
                workJustHasBeenEnabled()
                || this.tickCounter % 20 == 0
                || this.success > 0 && this.tickCounter % 5 == 0
            );
    }

    @Override
    public boolean isInputSide(EnumFacing side) {
        return side != this.targetFacing && super.isInputSide(side);
    }

    @Override
    public boolean isOutputSide(EnumFacing side) {
        return side == this.targetFacing || super.isOutputSide(side);
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != this.targetFacing && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("targetFacing");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("targetFacing")) updateRender();
    }
}
