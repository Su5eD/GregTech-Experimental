package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.MachineController;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

public class LiquidMeterCover extends MeterCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("liquid_meter");

    public LiquidMeterCover(CoverType<MachineController> type, MachineController be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public int getRedstoneStrength() {
        return ((BlockEntity) this.be).getCapability(ForgeCapabilities.FLUID_HANDLER, this.side).map(handler -> {
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
