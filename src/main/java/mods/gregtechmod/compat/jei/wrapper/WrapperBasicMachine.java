package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.compat.jei.JEIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class WrapperBasicMachine<R extends IMachineRecipe<?, List<ItemStack>>> implements IRecipeWrapper {
    protected final R recipe;
    private final int yOffset;

    public WrapperBasicMachine(R recipe) {
        this(recipe, 0);
    }
    
    public WrapperBasicMachine(R recipe, int yOffset) {
        this.recipe = recipe;
        this.yOffset = yOffset;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        setInputs(ingredients);
        List<List<ItemStack>> output = recipe.getOutput().stream()
                .map(Collections::singletonList)
                .collect(Collectors.toList());
        ingredients.setOutputLists(VanillaTypes.ITEM, output);
    }

    protected abstract void setInputs(IIngredients ingredients);

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, recipe, this.yOffset, true);
    }
}
