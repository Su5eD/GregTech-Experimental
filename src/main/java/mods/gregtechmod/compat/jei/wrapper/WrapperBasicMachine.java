package mods.gregtechmod.compat.jei.wrapper;

import ic2.core.util.StackUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class WrapperBasicMachine implements IRecipeWrapper {
    private final IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe;

    public WrapperBasicMachine(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IRecipeIngredient ingredient = this.recipe.getInput();
        int count = ingredient.getCount();
        List<ItemStack> inputs = ingredient.getMatchingInputs().stream()
                .map(stack -> StackUtil.copyWithSize(stack, count))
                .collect(Collectors.toList());
        ingredients.setInputs(VanillaTypes.ITEM, inputs);

        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int duration = recipe.getDuration();
        double energyCost = this.recipe.getEnergyCost();
        minecraft.fontRenderer.drawString("EU: " + GtUtil.formatNumber(duration * energyCost), 1, 55, -16777216, false);
        minecraft.fontRenderer.drawString("Time: " + GtUtil.formatNumber(duration / 20) + " s", 1,65, -16777216, false);
        minecraft.fontRenderer.drawString("MaxEnergy: " + GtUtil.formatNumber(energyCost) + " EU/t", 1,75, -16777216, false);
    }
}
