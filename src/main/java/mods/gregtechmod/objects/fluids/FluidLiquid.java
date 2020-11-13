package mods.gregtechmod.objects.fluids;

import mods.gregtechmod.api.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidLiquid extends Fluid {

    public FluidLiquid(String name, ResourceLocation still, ResourceLocation flow) {
        super(name, still, flow);
    }

    @Override
    public String getUnlocalizedName() {
        return Reference.MODID+"."+super.getUnlocalizedName();
    }
}
