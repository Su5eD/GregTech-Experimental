package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.core.util.LiquidUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class PumpCover extends InventoryCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("pump");

    public PumpCover(CoverType type, ICoverable be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void doCoverThings() {
        if (canWork() && LiquidUtil.isFluidTile((BlockEntity) this.be, this.side)) {
            LiquidUtil.AdjacentFluidHandler target = LiquidUtil.getAdjacentHandler((BlockEntity) this.be, this.side);
            if (target != null) {
                FluidStack stack = GtUtil.drainBlock(this.mode.isImport ? target.handler : (BlockEntity) this.be, this.mode.isImport ? this.side.getOpposite() : this.side, 1000, FluidAction.EXECUTE);
                if (stack != null) {
                    double energy = Math.min(1, stack.getAmount() / 100D);

                    if (!shouldUseEnergy(energy) || ((IElectricMachine) this.be).canUseEnergy(energy)) {
                        // TODO GtUtil method
                        LiquidUtil.transfer(this.mode.isImport ? target.handler : (BlockEntity) this.be, this.mode.isImport ? this.side.getOpposite() : this.side, this.mode.isImport ? (BlockEntity) this.be : target.handler, FluidAttributes.BUCKET_VOLUME);
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
        return canWork() && this.mode.allowsInput();
    }

    @Override
    public boolean letsLiquidsOut() {
        return canWork() && this.mode.allowsOutput();
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
