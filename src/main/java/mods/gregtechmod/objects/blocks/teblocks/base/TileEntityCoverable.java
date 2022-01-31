package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.objects.Cover;
import mods.gregtechmod.objects.blocks.teblocks.component.CoverHandler;
import mods.gregtechmod.objects.covers.CoverGeneric;
import mods.gregtechmod.objects.covers.CoverVent;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.PropertyHelper.VerticalRotation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.*;

/**
 * Lets you add/remove covers on a tile entity. <b>Isn't responsible</b> for cover behavior.
 */
public abstract class TileEntityCoverable extends TileEntityAutoNBT implements ICoverable {
    protected final CoverHandler coverHandler;
    protected Set<CoverType> coverBlacklist = new HashSet<>();

    public TileEntityCoverable() {
        this.coverHandler = addComponent(new CoverHandler(this, this::updateRender));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return beforeActivated(player.inventory.getCurrentItem(), player, side, hitX, hitY, hitZ) 
            || super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }
    
    protected boolean beforeActivated(ItemStack stack, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (CoverGeneric.isGenericCover(stack)) {
            placeCover(Cover.GENERIC, player, side, stack);
            return true;
        } else if (CoverVent.isVent(stack)) {
            placeCover(Cover.VENT, player, side, stack);
            return true;
        } else if (GtUtil.isScrewdriver(stack)) {
            return onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
        }
        return attemptUseCrowbar(stack, side, player);
    }
    
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        ICover existing = getCoverAtSide(side);
        if (existing != null) {
            if (existing.onScrewdriverClick(player)) {
                stack.damageItem(1, player);
                return true;
            }
        }
        
        ICover cover = Cover.NORMAL.instance.get().constructCover(side, this, ItemStack.EMPTY);
        return placeCoverAtSide(cover, player, side, false);
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

    // For generic covers and vents
    private void placeCover(Cover type, EntityPlayer player, EnumFacing side, ItemStack stack) {
        ItemStack coverStack = ItemHandlerHelper.copyStackWithSize(stack, 1);
        ICover cover = type.instance.get().constructCover(side, this, coverStack);
        if (placeCoverAtSide(cover, player, side, false) && !player.capabilities.isCreativeMode) stack.shrink(1);
    }

    @Override
    protected ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return Optional.ofNullable(target)
            .map(trace -> getCoverAtSide(trace.sideHit))
            .map(ICover::getItem)
            .orElseGet(() -> super.getPickBlock(player, target));
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance ret = state.withProperty(PropertyHelper.VERTICAL_ROTATION_PROPERTY, getVerticalRotation());
        return this.coverHandler != null ? ret.withProperty(CoverHandler.COVER_HANDLER_PROPERTY, this.coverHandler) : ret;
    }
    
    protected VerticalRotation getVerticalRotation() {
        return VerticalRotation.MIRROR_BACK;
    }

    @Override
    public ICover getCoverAtSide(EnumFacing side) {
        return this.coverHandler.covers.get(side);
    }
    
    @Override
    public void getNetworkedFields(List<? super String> list) {
        list.add("coverHandler");
    }

    @Override
    public Collection<? extends ICover> getCovers() {
        return this.coverHandler.covers.values();
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return !this.coverBlacklist.contains(cover.getType()) && this.coverHandler.placeCoverAtSide(cover, side, simulate);
    }

    @Override
    public boolean removeCover(EnumFacing side, boolean simulate) {
        ICover cover = this.coverHandler.covers.get(side);
        if (cover != null) {
            ItemStack coverItem = cover.getItem();
            if (this.coverHandler.removeCover(side, false)) {
                if (coverItem != null && !this.world.isRemote) {
                    EntityItem entity = new EntityItem(this.world, pos.getX() + side.getXOffset() + 0.5, pos.getY() + side.getYOffset() + 0.5, pos.getZ() + side.getZOffset() + 0.5, coverItem);
                    entity.motionX = entity.motionY = entity.motionZ = 0;
                    this.world.spawnEntity(entity);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        return StreamEx.of(super.getWrenchDrops(player, fortune))
            .append(StreamEx.of(this.coverHandler.covers.values())
                .map(ICover::getItem))
            .toList();
    }
}
