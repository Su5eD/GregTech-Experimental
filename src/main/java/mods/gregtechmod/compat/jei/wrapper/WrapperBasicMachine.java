package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class WrapperBasicMachine<R extends IMachineRecipe<?, List<ItemStack>>> implements IRecipeWrapper {
    protected final R recipe;

    public WrapperBasicMachine(R recipe) {
        this.recipe = recipe;
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
        int duration = recipe.getDuration();
        double energyCost = this.recipe.getEnergyCost();

        minecraft.fontRenderer.drawString(GtUtil.translate("jei.energy", GtUtil.formatNumber(duration * energyCost)), 1, 55, -16777216, false);
        minecraft.fontRenderer.drawString(GtUtil.translate("jei.time", GtUtil.formatNumber(duration / 20)), 1,65, -16777216, false);
        minecraft.fontRenderer.drawString(GtUtil.translate("jei.max_energy", GtUtil.formatNumber(energyCost)), 1,75, -16777216, false);
    }
}
