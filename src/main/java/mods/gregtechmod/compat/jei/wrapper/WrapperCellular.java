package mods.gregtechmod.compat.jei.wrapper;

import ic2.core.util.StackUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.recipe.RecipeCellular;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WrapperCellular implements IRecipeWrapper {
    private final RecipeCellular recipe;
    private final boolean showEnergyCost;

    public WrapperCellular(RecipeCellular recipe) {
        this(recipe, false);
    }

    public WrapperCellular(RecipeCellular recipe, boolean showEnergyCost) {
        this.recipe = recipe;
        this.showEnergyCost = showEnergyCost;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IRecipeIngredient input = recipe.getInput();
        int cells = recipe.getCells();
        int count = input.getCount();

        if (input instanceof IRecipeIngredientFluid) {
            int amount = count * Fluid.BUCKET_VOLUME;
            List<FluidStack> fluids = ((IRecipeIngredientFluid) input).getMatchingFluids().stream()
                    .map(fluid -> new FluidStack(fluid, amount))
                    .collect(Collectors.toList());
            ingredients.setInputs(VanillaTypes.FLUID, fluids);
            ItemStack cell = cells > 0 ? StackUtil.copyWithSize(ModHandler.emptyCell, cells) : ItemStack.EMPTY;
            ingredients.setInput(VanillaTypes.ITEM, cell);
        } else {
            List<ItemStack> stacks = input.getMatchingInputs()
                    .stream()
                    .map(stack -> StackUtil.copyWithSize(stack, count))
                    .collect(Collectors.toList());
            List<List<ItemStack>> inputs = new ArrayList<>(Collections.singletonList(stacks));
            ItemStack cell = cells > 0 ? StackUtil.copyWithSize(ModHandler.emptyCell, cells) : ItemStack.EMPTY;
            inputs.add(0, Collections.singletonList(cell));
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        }

        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int duration = recipe.getDuration();
        double energyCost = recipe.getEnergyCost();
        minecraft.fontRenderer.drawString(GtUtil.translate("jei.energy", GtUtil.formatNumber(duration * energyCost)), 2, 60, -16777216, false);
        minecraft.fontRenderer.drawString(GtUtil.translate("jei.time", GtUtil.formatNumber(duration / 20)), 2,70, -16777216, false);
        if (showEnergyCost) minecraft.fontRenderer.drawString(GtUtil.translate("jei.max_energy", GtUtil.formatNumber(energyCost)), 2,80, -16777216, false);
    }
}
