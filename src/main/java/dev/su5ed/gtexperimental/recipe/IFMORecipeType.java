package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.CompositeRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeFactory;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeTypeImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IFMORecipeType<R extends IFMORecipe> extends BaseRecipeTypeImpl<R, CompositeRecipeIngredientType<IFMORecipe.Input>, CompositeRecipeIngredient<IFMORecipe.Input>, List<ItemStack>> {
    public IFMORecipeType(ResourceLocation name, int outputCount, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, CompositeRecipeIngredient<IFMORecipe.Input>, List<ItemStack>> factory) {
        super(name, ModRecipeIngredientTypes.ITEM_FLUID, ModRecipeOutputTypes.ITEM.listOf(outputCount), properties, factory);
    }
}
