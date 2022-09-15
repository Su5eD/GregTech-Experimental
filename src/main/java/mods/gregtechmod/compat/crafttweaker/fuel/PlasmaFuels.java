package mods.gregtechmod.compat.crafttweaker.fuel;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.CraftTweakerFuels;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.fuel.Plasma")
@ZenRegister
public class PlasmaFuels {

    @ZenMethod
    public static void addFuel(IIngredient input, double energy, @Optional IItemStack output) {
        CraftTweakerFuels.addSimpleFuel(GtFuels.plasma, input, energy, output);
    }

    @ZenMethod
    public static void removeFuel(ILiquidStack fluid) {
        GtFuels.plasma.removeFuel(CraftTweakerMC.getFluid(fluid.getDefinition()));
    }

    @ZenMethod
    public static void removeFuel(IItemStack stack) {
        GtFuels.plasma.removeFuel(CraftTweakerMC.getItemStack(stack));
    }
}
