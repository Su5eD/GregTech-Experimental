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

public class ModRecipes {
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> MACERATOR = new RecipeManagerBasic<>();
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> EXTRACTOR = new RecipeManagerBasic<>();
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> COMPRESSOR = new RecipeManagerBasic<>();
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> FURNACE = new RecipeManagerBasic<>();

    public static void init() {
        convertRecipes(Recipes.macerator.getRecipes(), 300, 2, MACERATOR);
        convertRecipes(Recipes.extractor.getRecipes(), 300, 2, EXTRACTOR);
        convertRecipes(Recipes.compressor.getRecipes(), 300, 2, COMPRESSOR);

        FurnaceRecipes.instance().getSmeltingList()
                .forEach((key, value) -> {
                    IRecipeIngredient ingredient = RecipeIngredientItemStack.create(key);
                    FURNACE.addRecipe(RecipeFurnace.create(ingredient, value));
                });
    }

    private static void convertRecipes(Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes, int duration, double energyCost,
                                       IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        recipes.forEach(recipe -> {
            IRecipeInput input = recipe.getInput();
            int count = input.getAmount();
            IRecipeIngredient gtInput;
            if (input instanceof RecipeInputOreDict) gtInput = RecipeIngredientOre.create(((RecipeInputOreDict) input).input, count);
            else gtInput = RecipeIngredientItemStack.create(input.getInputs(), count);

            manager.addRecipe(new IC2MachineRecipe(gtInput, new ArrayList<>(recipe.getOutput()), duration, energyCost));
        });
    }
}
