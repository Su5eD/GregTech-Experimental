package mods.gregtechmod.common.objects.blocks.machines.tileentity.base;

import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.common.cover.CoverHandler;
import mods.gregtechmod.common.cover.CoverRegistry;
import mods.gregtechmod.common.cover.ICover;
import mods.gregtechmod.common.cover.ICoverable;
import mods.gregtechmod.common.cover.types.CoverGeneric;
import mods.gregtechmod.common.cover.types.CoverVent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.*;

/**
 * Let's you add/remove covers on a tile entity. <b>Isn't responsible</b>  for cover behavior.
 */
public abstract class TileEntityCoverable extends TileEntityInventory implements ICoverable {
    protected final CoverHandler handler;
    public Set<EnumFacing> sinkDirections = new HashSet<>(Arrays.asList(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH));
    public boolean needsCoverBehaviorUpdate;
    protected Set<String> allowedCovers = new HashSet<>();

    public TileEntityCoverable() {
        this.handler = addComponent(new CoverHandler(this, new Runnable() {
            public void run() {
                IC2.network.get(true).updateTileEntityField(TileEntityCoverable.this, "handler");
                needsCoverBehaviorUpdate = true;
                rerender();
            }
        }));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (CoverGeneric.isGenericCover(stack)) {
            placeCover(player, side, stack, "generic");
            return true;
        } else if (CoverVent.isVent(stack)) {
            placeCover(player, side, stack, "vent");
            return true;
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    private void placeCover(EntityPlayer player, EnumFacing side, ItemStack stack, String name) {
        ItemStack tStack = stack.copy();
        tStack.setCount(1);
        if (placeCoverAtSide(CoverRegistry.constructCover(name, side, this, tStack), side, false) && !player.capabilities.isCreativeMode) stack.splitStack(1);
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        CoverHandler value = this.handler;
        if (value != null) state = state.withProperties(CoverHandler.coverHandlerProperty, value);
        return state;
    }

    @Override
    public ICover getCoverAtSide(EnumFacing side) {
        if (handler.covers.containsKey(side)) return this.handler.covers.get(side);
        return null;
    }

    @Override
    protected boolean canSetFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (this.handler.covers.containsKey(facing)) return false;
        return super.canSetFacingWrench(facing, player);
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = new ArrayList<>();
        ret.add("handler");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        rerender();
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        needsCoverBehaviorUpdate = true;
    }

    @Override
    public Collection<? extends ICover> getCovers() {
        return handler.covers.values();
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate) {
        if (!allowedCovers.isEmpty() && !allowedCovers.contains(CoverRegistry.getCoverName(cover))) return false;
        return this.handler.placeCoverAtSide(cover, side, simulate);
    }

    @Override
    public boolean removeCover(EnumFacing side, boolean simulate) {
        if (!this.handler.covers.containsKey(side)) return false;

        ICover cover = this.handler.covers.get(side);
        ItemStack coverItem = cover.getItem();
        cover.onCoverRemoved();
        if (this.handler.removeCover(side, false)) {
            if (coverItem != null) {
                EntityItem tEntity = new EntityItem(world, pos.getX() + side.getXOffset()+0.5, pos.getY() + side.getYOffset() + 0.5, pos.getZ()+side.getZOffset()+0.5, coverItem);
                tEntity.motionX = 0;
                tEntity.motionY = 0;
                tEntity.motionZ = 0;
                if (!world.isRemote) world.spawnEntity(tEntity);
            }
            return true;
        }
        return false;
    }

    @Override
    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        for (ICover cover : handler.covers.values()) ret.add(cover.getItem());
        return ret;
    }

    @Override
    public void markForRenderUpdate() {
        rerender();
    }
}
