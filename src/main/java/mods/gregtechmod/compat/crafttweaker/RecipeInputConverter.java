package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import net.minecraftforge.fluids.Fluid;
import one.util.streamex.StreamEx;

import java.util.List;

public final class RecipeInputConverter {

    public static IRecipeIngredient of(IIngredient ingredient) {
        IRecipeIngredientFactory factory = GregTechAPI.getIngredientFactory();
        if (ingredient instanceof IItemStack) {
            return factory.fromStack(CraftTweakerMC.getItemStack((IItemStack) ingredient));
        }
        else if (ingredient instanceof ILiquidStack) {
            return of((ILiquidStack) ingredient);
        }
        else if (ingredient instanceof IOreDictEntry) {
            return factory.fromOre(((IOreDictEntry) ingredient).getName(), ingredient.getAmount());
        }
        return new CraftTweakerRecipeIngredient(ingredient);
    }

    public static IRecipeIngredientFluid of(ILiquidStack liquidStack) {
        return GregTechAPI.getIngredientFactory().fromFluidStack(CraftTweakerMC.getLiquidStack(liquidStack));
    }

    public static List<IRecipeIngredient> of(IIngredient[] ingredients) {
        return StreamEx.of(ingredients)
            .map(RecipeInputConverter::of)
            .toList();
    }

    public static List<IRecipeIngredientFluid> of(ILiquidStack[] ingredients) {
        return StreamEx.of(ingredients)
            .map(RecipeInputConverter::of)
            .toList();
    }

    public static List<IRecipeIngredientFluid> fluids(IIngredient[] ingredients) {
        return StreamEx.of(ingredients)
            .mapToEntry(ingredient -> StreamEx.of(ingredient.getLiquids())
                .map(ILiquidStack::getDefinition)
                .map(CraftTweakerMC::getFluid)
                .toList(), ingredient -> ingredient.getAmount() / Fluid.BUCKET_VOLUME)
            .<IRecipeIngredientFluid>mapKeyValue(RecipeIngredientFluid::fromFluids)
            .toList();
    }

    private RecipeInputConverter() {}
}
