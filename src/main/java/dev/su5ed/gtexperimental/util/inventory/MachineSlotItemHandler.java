package dev.su5ed.gtexperimental.util.inventory;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.cover.Cover;
import dev.su5ed.gtexperimental.api.cover.CoverHandler;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public class MachineSlotItemHandler extends SlotAwareItemHandler {
    private final BaseBlockEntity parent;
    private final Lazy<CoverHandler> coverHandler;
    private final Lazy<MachineController> controller;

    public MachineSlotItemHandler(BaseBlockEntity parent, Runnable onChanged) {
        super(onChanged);

        this.parent = parent;

        this.coverHandler = Lazy.of(() -> GtUtil.getRequiredCapability(parent, Capabilities.COVER_HANDLER));
        this.controller = Lazy.of(() -> GtUtil.getRequiredCapability(parent, Capabilities.MACHINE_CONTROLLER));
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack, @Nullable Direction side) {
        if (allowTransferOnSide(side)) {
            super.setStackInSlot(slot, stack, normalizeSide(side));
        }
    }

    @Override
    public int getSlots(@Nullable Direction side) {
        return allowTransferOnSide(side) ? super.getSlots(normalizeSide(side)) : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot, @Nullable Direction side) {
        return allowTransferOnSide(side) ? super.getStackInSlot(slot, normalizeSide(side)) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, @Nullable Direction side, boolean simulate) {
        return allowInputOnSide(side) ? super.insertItem(slot, stack, normalizeSide(side), simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction side, boolean simulate) {
        return allowOutputOnSide(side) ? super.extractItem(slot, amount, normalizeSide(side), simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot, @Nullable Direction side) {
        return allowTransferOnSide(side) ? super.getSlotLimit(slot, normalizeSide(side)) : 0;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack, @Nullable Direction side) {
        return allowTransferOnSide(side) && super.isItemValid(slot, stack, normalizeSide(side));
    }

    private Direction normalizeSide(@Nullable Direction side) {
        return side != null && this.controller.get().isStrictInputSides() ? GtUtil.getRelativeSide(side, this.parent.getFacing().getOpposite()) : null;
    }

    private boolean allowInputOnSide(@Nullable Direction side) {
        return this.controller.get().isInputEnabled() && (side == null || this.coverHandler.get().getCoverAtSide(side).map(Cover::letsItemsIn).orElse(true));
    }

    private boolean allowOutputOnSide(@Nullable Direction side) {
        return this.controller.get().isOutputEnabled() && (side == null || this.coverHandler.get().getCoverAtSide(side).map(Cover::letsItemsOut).orElse(true));
    }

    private boolean allowTransferOnSide(@Nullable Direction side) {
        return (this.controller.get().isInputEnabled() || this.controller.get().isOutputEnabled()) && (side == null || this.coverHandler.get().getCoverAtSide(side).map(cover -> cover.letsItemsIn() || cover.letsItemsOut()).orElse(true));
    }
}
