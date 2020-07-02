package mods.gregtechmod.common.init;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.objects.fluids.FluidLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class FluidInit {
    public static List<Fluid> FLUIDS = new ArrayList<>();

    public static final Fluid SEED_OIL = new FluidLiquid("seed_oil", new ResourceLocation(GregtechMod.MODID+":blocks/fluids/seed_oil_still"), new ResourceLocation(GregtechMod.MODID+":blocks/fluids/seed_oil_flow"), 500);
}
