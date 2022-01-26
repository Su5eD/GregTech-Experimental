package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class CoverLiquidMeter extends CoverMeter {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("liquid_meter");

    public CoverLiquidMeter(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public int getRedstoneStrength() {
        IFluidHandler handler = ((TileEntity) te).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.side);
        if (handler != null) {
            float amount = 0;
            float capacity = 0;
            int fluidsFound = 0;

            for (IFluidTankProperties properties : handler.getTankProperties()) {
                int propCapacity = properties.getCapacity();
                capacity += propCapacity;

                FluidStack fluid = properties.getContents();
                if (fluid != null) {
                    amount += fluid.amount;
                    ++fluidsFound;
                }
            }

            float ratio = amount / capacity;
            return MathHelper.floor(ratio * 14) + (fluidsFound > 0 ? 1 : 0);
        }
        return 0;
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }
}
