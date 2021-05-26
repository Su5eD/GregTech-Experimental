package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.jei.JEIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WrapperMultiInput<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> implements IRecipeWrapper {
    protected final R recipe;
    
    public WrapperMultiInput(R recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, JEIUtils.getMultiInputs(recipe));
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, recipe, true);
    }
}
