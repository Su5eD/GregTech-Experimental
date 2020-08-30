package mods.gregtechmod.common.objects.fluids;

import mods.gregtechmod.common.init.FluidLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidGas extends Fluid {
    public FluidGas(String name, ResourceLocation still, ResourceLocation flowing) {
        super(name, still, flowing);
        setUnlocalizedName(name);
        FluidLoader.FLUIDS.add(this);
        setGaseous(true);
    }
}