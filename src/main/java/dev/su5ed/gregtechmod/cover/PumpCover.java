package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class PumpCover extends InventoryCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("pump");

    public PumpCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void tick() {
        BlockEntity target = GtUtil.getNeighborFluidBlockEntity(this.be, this.side);
        if (target != null) {
            FluidStack stack = GtUtil.drainBlock(this.mode.isImport ? target : this.be, this.mode.isImport ? this.side.getOpposite() : this.side, 1000, FluidAction.EXECUTE);
            if (!stack.isEmpty()) {
                double energy = Math.min(1, stack.getAmount() / 100D);

                if (!shouldUseEnergy(energy) || ((IElectricMachine) this.be).canUseEnergy(energy)) {
                    GtUtil.transportFluid(this.mode.isImport ? target : this.be, this.mode.isImport ? this.side.getOpposite() : this.side, this.mode.isImport ? this.be : target, FluidType.BUCKET_VOLUME);
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
        return shouldTick() && this.mode.allowsInput();
    }

    @Override
    public boolean letsLiquidsOut() {
        return shouldTick() && this.mode.allowsOutput();
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
