package mods.gregtechmod.objects.covers;

import ic2.core.util.LiquidUtil;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class CoverPump extends CoverInventory {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("pump");

    public CoverPump(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (canWork() && LiquidUtil.isFluidTile((TileEntity)te, side)) {
            LiquidUtil.AdjacentFluidHandler target = LiquidUtil.getAdjacentHandler((TileEntity)te, side);
            if (target != null) {
                FluidStack stack = LiquidUtil.drainTile(this.mode.isImport ? target.handler : (TileEntity)te, this.mode.isImport ? side.getOpposite() : side, 1000, true);
                if (stack != null) {
                    double energy = Math.min(1, stack.amount / 100D);
                    
                    if (shouldUseEnergy(energy) && !((IElectricMachine) te).canUseEnergy(energy)) return;
                    
                    LiquidUtil.transfer(this.mode.isImport ? target.handler : (TileEntity)te, this.mode.isImport ? side.getOpposite() : side, this.mode.isImport ? (TileEntity)te : target.handler, Fluid.BUCKET_VOLUME);
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
