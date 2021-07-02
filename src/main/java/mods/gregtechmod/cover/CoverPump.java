package mods.gregtechmod.cover;

import ic2.core.util.LiquidUtil;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricalMachine;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class CoverPump extends CoverInventory {

    public CoverPump(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!canWork()) return;
        if (LiquidUtil.isFluidTile((TileEntity)te, side)) {
            LiquidUtil.AdjacentFluidHandler target = LiquidUtil.getAdjacentHandler((TileEntity)te, side);
            if (target == null) return;

            FluidStack stack = LiquidUtil.drainTile(mode.isImport ? target.handler : (TileEntity)te, mode.isImport ? side.getOpposite() : side, 1000, true);
            if (stack == null) return;

            double energy = Math.min(1, stack.amount / 100D);
            if (te instanceof IElectricalMachine && mode.consumesEnergy(side) && ((IElectricalMachine)te).getUniversalEnergyCapacity() >= energy) {
                if (((IElectricalMachine)te).useEnergy(energy, false) < energy) return;
            }

            LiquidUtil.transfer(mode.isImport ? target.handler : (TileEntity)te, side, mode.isImport ? (TileEntity)te : target.handler, 1000);
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/pump");
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
