package mods.gregtechmod.common.objects.fluids;

import mods.gregtechmod.common.init.FluidInit;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidLiquid extends Fluid {

    public FluidLiquid(String name, ResourceLocation still, ResourceLocation flow, int density) {
        super(name, still, flow);
        setUnlocalizedName(name);
        FluidInit.FLUIDS.add(this);
        setDensity(density);
    }
}
