package mods.gregtechmod.objects.fluids;

import mods.gregtechmod.api.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidGas extends Fluid {
    public FluidGas(String name, ResourceLocation still, ResourceLocation flowing) {
        super(name, still, flowing);
        setGaseous(true);
        setDensity(-200);
    }

    @Override
    public String getUnlocalizedName() {
        return Reference.MODID+"."+super.getUnlocalizedName();
    }
}