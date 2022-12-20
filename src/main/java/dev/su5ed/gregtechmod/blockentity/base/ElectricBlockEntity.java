package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ElectricBlockEntity extends UpgradableBlockEntityImpl {

    public ElectricBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }

    @Override
    public boolean addEnergy(double amount) {
        return false;
    }

    @Override
    public boolean canUseEnergy(double amount) {
        return false;
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        return 0;
    }

    @Override
    public boolean tryUseEnergy(double amount, boolean simulate) {
        return false;
    }

    @Override
    public int getSinkTier() {
        return 0;
    }

    @Override
    public int getSourceTier() {
        return 0;
    }

    @Override
    public double getMaxInputEUp() {
        return 0;
    }

    @Override
    public double getMaxOutputEUt() {
        return 0;
    }

    @Override
    public double getStoredEnergy() {
        return 0;
    }

    @Override
    public double getEnergyCapacity() {
        return getBaseEUCapacity() + getExtraEUCapacity();
    }

    @Override
    public double getAverageEUInput() {
        return 0;
    }

    @Override
    public double getAverageEUOutput() {
        return 0;
    }

    @Override
    public void markForExplosion() {

    }

    @Override
    public void markForExplosion(float power) {

    }
    
    // IGregTech Machine TODO

    @Override
    public void setRedstoneOutput(Direction side, int strength) {
        
    }

    @Override
    public void setAllowedToWork(boolean value) {

    }

    @Override
    public boolean workJustHasBeenEnabled() {
        return false;
    }

    @Override
    public boolean isAllowedToWork() {
        return false;
    }

    @Override
    public boolean isInputEnabled() {
        return false;
    }

    @Override
    public void setInputEnabled(boolean value) {

    }

    @Override
    public boolean isOutputEnabled() {
        return false;
    }

    @Override
    public void setOutputEnabled(boolean value) {

    }
}
