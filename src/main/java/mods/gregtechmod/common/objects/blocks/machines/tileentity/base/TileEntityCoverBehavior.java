package mods.gregtechmod.common.objects.blocks.machines.tileentity.base;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.common.util.SidedRedstone;
import mods.gregtechmod.common.util.SidedRedstoneEmitter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityCoverBehavior extends TileEntityCoverable implements IGregtechMachine {
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
        if (world.isRemote) return;

        for (ICover cover : coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && tickCounter%tickRate == 0) cover.doCoverThings();
        }


        if (enableWorking != enableWorkingOld) {
            enableWorkingOld = enableWorking;
            needsCoverBehaviorUpdate = true;
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
        return super.canInsertItem(index, stack, side);
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
        if (side == null) return false;
        EnumFacing aSide = side.getOpposite();
        if (coverHandler.covers.containsKey(aSide)) {
            ICover cover = coverHandler.covers.get(aSide);
            return cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone() || cover.overrideRedstoneOut();
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
