package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeFactory;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MIMORecipeType<R extends MIMORecipe> extends ModRecipeType<R, ListRecipeIngredientType<List<RecipeIngredient<ItemStack>>, ItemStack>, List<RecipeIngredient<ItemStack>>, List<ItemStack>> {
    public MIMORecipeType(ResourceLocation name, int inputCount, int outputCount, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, List<RecipeIngredient<ItemStack>>, List<ItemStack>> factory) {
        super(name, ModRecipeIngredientTypes.ITEM.listOf(inputCount), ModRecipeOutputTypes.ITEM.listOf(outputCount), properties, factory);
    }
}
