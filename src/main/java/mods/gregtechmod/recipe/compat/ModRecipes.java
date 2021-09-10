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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModRecipes {
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> MACERATOR = new RecipeManagerBasic<>();
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> EXTRACTOR = new RecipeManagerBasic<>();
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> COMPRESSOR = new RecipeManagerBasic<>();
    public static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> FURNACE = new RecipeManagerBasic<>();

    public static void init() {
        convertBasicRecipes(Recipes.macerator.getRecipes(), MACERATOR);
        convertBasicRecipes(Recipes.extractor.getRecipes(), EXTRACTOR);
        convertBasicRecipes(Recipes.compressor.getRecipes(), COMPRESSOR);

        FurnaceRecipes.instance().getSmeltingList()
                .forEach((key, value) -> {
                    IRecipeIngredient ingredient = RecipeIngredientItemStack.create(key);
                    FURNACE.addRecipe(RecipeFurnace.create(ingredient, value));
                });
    }

    private static void convertBasicRecipes(Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes,
                                            IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        recipes.forEach(recipe -> {
            IRecipeIngredient input = convertRecipeInput(recipe.getInput());

            manager.addRecipe(new IC2MachineRecipe(input, new ArrayList<>(recipe.getOutput()), 300, 2));
        });
    }
    
    public static List<IRecipeInput> convertRecipeIngredient(IRecipeIngredient ingredient) {
        int count = ingredient.getCount();
        if (ingredient instanceof RecipeIngredientOre) return ((RecipeIngredientOre) ingredient).asIngredient().getOres().stream()
                .map(ore -> Recipes.inputFactory.forOreDict(ore, count))
                .collect(Collectors.toList());
        else return ingredient.getMatchingInputs().stream()
                .map(Recipes.inputFactory::forStack)
                .collect(Collectors.toList());
    }

    public static IRecipeIngredient convertRecipeInput(IRecipeInput input) {
        if (input instanceof RecipeInputOreDict) return RecipeIngredientOre.create(((RecipeInputOreDict) input).input, input.getAmount());
        else return RecipeIngredientItemStack.create(input.getInputs(), input.getAmount());
    }
}
