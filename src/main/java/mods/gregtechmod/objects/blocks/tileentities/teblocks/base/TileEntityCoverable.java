package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.cover.CoverRegistry;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.cover.CoverHandler;
import mods.gregtechmod.cover.type.CoverGeneric;
import mods.gregtechmod.cover.type.CoverVent;
import mods.gregtechmod.util.GtUtil;
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
    protected final CoverHandler coverHandler;
    public Set<EnumFacing> sinkDirections = new HashSet<>(Arrays.asList(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH));
    public boolean needsCoverBehaviorUpdate;
    protected Set<String> allowedCovers = new HashSet<>();

    public TileEntityCoverable() {
        this.coverHandler = addComponent(new CoverHandler(this, () -> {
            IC2.network.get(true).updateTileEntityField(TileEntityCoverable.this, "coverHandler");
            markForCoverBehaviorUpdate();
            markForRenderUpdate();
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
        } else if (attemptUseCrowbar(stack, side, player) || attemptUseScrewdriver(stack, side, player)) return true;

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public boolean attemptUseScrewdriver(ItemStack stack, EnumFacing side, EntityPlayer player) {
        if (isScrewdriver(stack)) {
            ICover cover = getCoverAtSide(side);
            if (cover != null) {
                if (cover.onScrewdriverClick(player)) {
                    markForCoverBehaviorUpdate();
                    stack.damageItem(1, player);
                }
            } else placeCoverAtSide(CoverRegistry.constructCover("normal", side, this, null), side, false);
        }
        return false;
    }

    public boolean attemptUseCrowbar(ItemStack stack, EnumFacing side, EntityPlayer player) {
        if (isCrowbar(stack)) {
            if (removeCover(side, false)) {
                stack.damageItem(1, player);
                return true;
            }
        }
        return false;
    }

    private boolean isCrowbar(ItemStack stack) {
        return GregTechAPI.getCrowbars().stream()
                .anyMatch(crowbar -> GtUtil.stackEquals(crowbar, stack));
    }

    private boolean isScrewdriver(ItemStack stack) {
        return GregTechAPI.getScrewdrivers().stream()
                .anyMatch(screwdriver -> GtUtil.stackEquals(screwdriver, stack));
    }

    private void placeCover(EntityPlayer player, EnumFacing side, ItemStack stack, String name) { //For generic covers and vents
        ItemStack coverStack = StackUtil.copyWithSize(stack, 1);
        if (placeCoverAtSide(CoverRegistry.constructCover(name, side, this, coverStack), side, false) && !player.capabilities.isCreativeMode) stack.shrink(1);
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
    protected boolean canSetFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (this.coverHandler.covers.containsKey(facing)) return false;
        return super.canSetFacingWrench(facing, player);
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("coverHandler");
        return ret;
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("coverHandler")) rerender();
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        needsCoverBehaviorUpdate = true;
    }

    @Override
    public Collection<? extends ICover> getCovers() {
        return coverHandler.covers.values();
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EnumFacing side, boolean simulate) {
        if (!allowedCovers.isEmpty() && !allowedCovers.contains(CoverRegistry.getCoverName(cover))) return false;
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
        for (ICover cover : coverHandler.covers.values()) ret.add(cover.getItem());
        return ret;
    }

    @Override
    public void markForRenderUpdate() {
        rerender();
    }

    @Override
    public void markForCoverBehaviorUpdate() {
        this.needsCoverBehaviorUpdate = true;
    }
}
