package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.apache.commons.lang3.tuple.Pair;

public class CoverLiquidMeter extends CoverMeter {

    public CoverLiquidMeter(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    protected Pair<Integer, Integer> getItemStorageAndCapacity() {
        IFluidHandler handler = ((TileEntity)te).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
        int amount = 0;
        int capacity = 0;
        if (handler != null) {
            for (IFluidTankProperties properties : handler.getTankProperties()) {
                capacity += properties.getCapacity();
                FluidStack fluid = properties.getContents();
                if (fluid != null) amount += fluid.amount;
            }
        }

        return Pair.of(amount, capacity);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/liquid_meter");
    }
}
