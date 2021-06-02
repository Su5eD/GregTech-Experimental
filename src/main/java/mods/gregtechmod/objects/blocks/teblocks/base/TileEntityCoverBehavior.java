package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.objects.blocks.teblocks.component.SidedRedstone;
import mods.gregtechmod.objects.blocks.teblocks.component.SidedRedstoneEmitter;
import mods.gregtechmod.util.InvUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityCoverBehavior extends TileEntityCoverable implements IGregTechMachine {
    public final SidedRedstoneEmitter rsEmitter;
    public final SidedRedstone redstone;
    protected boolean enableWorking = true;
    private boolean enableWorkingOld = true;
    protected int tickCounter;
    protected boolean enableInput = true;
    protected boolean enableOutput = true;

    public TileEntityCoverBehavior() {
        this.rsEmitter = addComponent(new SidedRedstoneEmitter(this));
        this.redstone = addComponent(new SidedRedstone(this));
    }

    @Override
    protected void updateEntityServer() {
        for (ICover cover : coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && tickCounter % tickRate == 0) cover.doCoverThings();
        }

        if (enableWorking != enableWorkingOld) {
            enableWorkingOld = enableWorking;
            updateEnet();
        }
        tickCounter++;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.enableInput = nbt.getBoolean("enableInput");
        this.enableOutput = nbt.getBoolean("enableOutput");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("enableInput", this.enableInput);
        nbt.setBoolean("enableOutput", this.enableOutput);
        return super.writeToNBT(nbt);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        if (!enableInput) return false;
        else if (coverHandler.covers.containsKey(side)) return coverHandler.covers.get(side).letsItemsIn() && super.canInsertItem(index, stack, side);
        return tryInsert(index, stack, side);
    }

    protected boolean tryInsert(int index, ItemStack stack, EnumFacing side) {
        if (!stack.isEmpty()) {
            InvSlot targetSlot = InvUtil.getInventorySlot(this, index);
            if (targetSlot != null && targetSlot.canInput() && targetSlot.accepts(stack)) {
                if (targetSlot.preferredSide != InvSlot.InvSide.ANY && !strictInputSides() || targetSlot.preferredSide.matches(side)) {
                    return true;
                } else {
                    return InvUtil.getInvSlots(this).stream()
                            .allMatch(invSlot -> invSlot == targetSlot || invSlot.preferredSide == InvSlot.InvSide.ANY || !invSlot.preferredSide.matches(side) || !invSlot.canInput() || !invSlot.accepts(stack));
                }
            }
        }

        return false;
    }

    protected boolean strictInputSides() {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        if (!enableOutput) return false;
        else if (coverHandler.covers.containsKey(side)) return coverHandler.covers.get(side).letsItemsOut() && super.canExtractItem(index, stack, side);
        return super.canExtractItem(index, stack, side);
    }

    @Override
    public void setRedstoneOutput(EnumFacing side, byte strength) {
        this.rsEmitter.setLevel(side, strength);
    }

    @Override
    protected int getWeakPower(EnumFacing side) {
        return this.rsEmitter.getLevel(side.getOpposite());
    }

    @Override
    protected boolean canConnectRedstone(EnumFacing side) {
        if (side != null) {
            EnumFacing oppositeSide = side.getOpposite();
            if (coverHandler.covers.containsKey(oppositeSide)) {
                ICover cover = coverHandler.covers.get(oppositeSide);
                return cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone() || cover.overrideRedstoneOut();
            }
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return getActive();
    }

    @Override
    public void disableInput() {
        this.enableInput = false;
    }

    @Override
    public void enableInput() {
        this.enableInput = true;
    }

    @Override
    public boolean isInputEnabled() {
        return this.enableInput;
    }

    @Override
    public void disableOutput() {
        this.enableOutput = false;
    }

    @Override
    public void enableOutput() {
        this.enableOutput = true;
    }

    @Override
    public boolean isOutputEnabled() {
        return this.enableOutput;
    }

    @Override
    public void disableWorking() {
        this.enableWorking = false;
    }

    @Override
    public void enableWorking() {
        this.enableWorking = true;
    }

    @Override
    public boolean isAllowedToWork() {
        return this.enableWorking;
    }
}
