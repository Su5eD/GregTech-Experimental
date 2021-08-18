package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.cover.CoverGeneric;
import mods.gregtechmod.cover.CoverVent;
import mods.gregtechmod.objects.blocks.teblocks.component.CoverHandler;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Let's you add/remove covers on a tile entity. <b>Isn't responsible</b> for cover behavior.
 */
public abstract class TileEntityCoverable extends TileEntityInventory implements ICoverable {
    protected final CoverHandler coverHandler;
    protected Set<CoverType> coverBlacklist = new HashSet<>();

    public TileEntityCoverable() {
        this.coverHandler = addComponent(new CoverHandler(this, this::rerender));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (beforeActivated(player.inventory.getCurrentItem(), player, side)) return true;

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }
    
    protected boolean beforeActivated(ItemStack stack, EntityPlayer player, EnumFacing side) {
        if (CoverGeneric.isGenericCover(stack)) {
            placeCover(player, side, stack, "generic");
            return true;
        } else if (CoverVent.isVent(stack)) {
            placeCover(player, side, stack, "vent");
            return true;
        } else if (GtUtil.isScrewdriver(stack)) {
            return onScrewdriverActivated(stack, side, player);
        } else return attemptUseCrowbar(stack, side, player);
    }
    
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player) {
        ICover cover = getCoverAtSide(side);
        if (cover != null) {
            if (cover.onScrewdriverClick(player)) {
                stack.damageItem(1, player);
                return true;
            }
        } else return placeCoverAtSide(GregTechAPI.getCoverRegistry().constructCover("normal", side, this, null), player, side, false);
        
        return false;
    }

    public boolean attemptUseCrowbar(ItemStack stack, EnumFacing side, EntityPlayer player) {
        if (GtUtil.isCrowbar(stack)) {
            if (removeCover(side, false)) {
                stack.damageItem(1, player);
                return true;
            }
        }
        return false;
    }

    private void placeCover(EntityPlayer player, EnumFacing side, ItemStack stack, String name) { //For generic covers and vents
        ItemStack coverStack = StackUtil.copyWithSize(stack, 1);
        if (placeCoverAtSide(GregTechAPI.getCoverRegistry().constructCover(name, side, this, coverStack), player, side, false) && !player.capabilities.isCreativeMode) stack.shrink(1);
    }

    @Override
    protected ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        if (target != null) {
            ICover cover = getCoverAtSide(target.sideHit);
            if (cover != null) return cover.getItem();
        }
        return super.getPickBlock(player, target);
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        if (this.coverHandler != null) state = state.withProperties(CoverHandler.COVER_HANDLER_PROPERTY, this.coverHandler);
        return state;
    }

    @Override
    public ICover getCoverAtSide(EnumFacing side) {
        return this.coverHandler.covers.get(side);
    }

    @Override
    public final List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        getNetworkedFields(ret);
        return ret;
    }
    
    public void getNetworkedFields(List<? super String> list) {
        list.add("coverHandler");
    }

    @Override
    public Collection<? extends ICover> getCovers() {
        return coverHandler.covers.values();
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        if (coverBlacklist.contains(cover.getType())) return false;
        return this.coverHandler.placeCoverAtSide(cover, side, simulate);
    }

    @Override
    public boolean removeCover(EnumFacing side, boolean simulate) {
        if (!this.coverHandler.covers.containsKey(side)) return false;

        ICover cover = this.coverHandler.covers.get(side);
        ItemStack coverItem = cover.getItem();
        cover.onCoverRemoval();
        if (this.coverHandler.removeCover(side, false)) {
            if (coverItem != null) {
                EntityItem entity = new EntityItem(this.world, pos.getX() + side.getXOffset() + 0.5, pos.getY() + side.getYOffset() + 0.5, pos.getZ()+side.getZOffset() + 0.5, coverItem);
                entity.motionX = 0;
                entity.motionY = 0;
                entity.motionZ = 0;
                if (!this.world.isRemote) world.spawnEntity(entity);
            }
            return true;
        }
        return false;
    }

    @Override
    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        for (ICover cover : coverHandler.covers.values()) ret.add(cover.getItem());
        return ret;
    }

    @Override
    public void updateRender() {
        rerender();
    }
}
