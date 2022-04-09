package mods.gregtechmod.recipe.compat;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.RecipeInputOreDict;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.RecipeManagerBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModRecipes {
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> MACERATOR = new RecipeManagerBasic<>();
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> EXTRACTOR = new RecipeManagerBasic<>();
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> COMPRESSOR = new RecipeManagerBasic<>();
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> FURNACE = new RecipeManagerBasic<>();

    public static void init() {
        convertBasicRecipes(Recipes.macerator.getRecipes(), MACERATOR);
        convertBasicRecipes(Recipes.extractor.getRecipes(), EXTRACTOR);
        convertBasicRecipes(Recipes.compressor.getRecipes(), COMPRESSOR);

        EntryStream.of(FurnaceRecipes.instance().getSmeltingList())
            .filterKeyValue((input, output) -> !input.isEmpty() && !output.isEmpty())
            .mapKeys(RecipeIngredientItemStack::create)
            .mapKeyValue(RecipeFurnace::create)
            .forEach(FURNACE::addRecipe);
    }

    private static void convertBasicRecipes(Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        recipes.forEach(recipe -> {
            IRecipeIngredient input = convertRecipeInput(recipe.getInput());

            manager.addRecipe(new IC2MachineRecipe(input, new ArrayList<>(recipe.getOutput()), 300, 2));
        });
    }

    public static List<IRecipeInput> convertRecipeIngredient(IRecipeIngredient ingredient) {
        int count = ingredient.getCount();
        if (ingredient instanceof RecipeIngredientOre) {
            return StreamEx.of(((RecipeIngredientOre) ingredient).asIngredient().getOres())
                .map(ore -> Recipes.inputFactory.forOreDict(ore, count))
                .toList();
        }
        else return ingredient.stream()
            .map(Recipes.inputFactory::forStack)
            .toList();
    }

    public static IRecipeIngredient convertRecipeInput(IRecipeInput input) {
        return input instanceof RecipeInputOreDict
            ? RecipeIngredientOre.create(((RecipeInputOreDict) input).input, input.getAmount())
            : RecipeIngredientItemStack.create(input.getInputs(), input.getAmount());
    }
}
