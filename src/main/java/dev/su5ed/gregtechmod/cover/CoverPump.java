package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.core.util.LiquidUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CoverPump extends CoverInventory {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("pump");

    public CoverPump(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (canWork() && LiquidUtil.isFluidTile((BlockEntity) this.be, side)) {
            LiquidUtil.AdjacentFluidHandler target = LiquidUtil.getAdjacentHandler((BlockEntity) this.be, side);
            if (target != null) {
                FluidStack stack = GtUtil.drainBlock(this.mode.isImport ? target.handler : (BlockEntity) this.be, this.mode.isImport ? side.getOpposite() : side, 1000, FluidAction.EXECUTE);
                if (stack != null) {
                    double energy = Math.min(1, stack.getAmount() / 100D);

                    if (!shouldUseEnergy(energy) || ((IElectricMachine) this.be).canUseEnergy(energy)) {
                        // TODO GtUtil method
                        LiquidUtil.transfer(this.mode.isImport ? target.handler : (BlockEntity) this.be, this.mode.isImport ? side.getOpposite() : side, this.mode.isImport ? (BlockEntity) this.be : target.handler, FluidAttributes.BUCKET_VOLUME);
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean letsLiquidsIn() {
        return canWork() && mode.allowsInput();
    }

    @Override
    public boolean letsLiquidsOut() {
        return canWork() && mode.allowsOutput();
    }

    @Override
    public boolean letsItemsIn() {
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public int getTickRate() {
        return 1;
    }
}
