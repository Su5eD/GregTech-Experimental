package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class LiquidMeterCover extends MeterCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("liquid_meter");

    public LiquidMeterCover(CoverType<IGregTechMachine> type, IGregTechMachine be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public int getRedstoneStrength() {
        return ((BlockEntity) this.be).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.side).map(handler -> {
            float amount = 0;
            float capacity = 0;
            int fluidsFound = 0;

            for (int i = 0; i < handler.getTanks(); i++) {
                int tankCapacity = handler.getTankCapacity(i);
                capacity += tankCapacity;

                FluidStack fluid = handler.getFluidInTank(i);
                if (!fluid.isEmpty()) {
                    amount += fluid.getAmount();
                    ++fluidsFound;
                }
            }

            float ratio = amount / capacity;
            return Mth.floor(ratio * 14) + (fluidsFound > 0 ? 1 : 0);
        })
            .orElse(0);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }
}
