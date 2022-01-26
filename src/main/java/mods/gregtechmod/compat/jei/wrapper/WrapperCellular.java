package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.recipe.RecipeCellular;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        IRecipeIngredient input = this.recipe.getInput();
        int cells = this.recipe.getCells();
        int count = input.getCount();

        if (input instanceof IRecipeIngredientFluid) {
            int amount = count * Fluid.BUCKET_VOLUME;
            List<FluidStack> fluids = StreamEx.of(((IRecipeIngredientFluid) input).getMatchingFluids())
                .map(fluid -> new FluidStack(fluid, amount))
                .toList();
            ingredients.setInputs(VanillaTypes.FLUID, fluids);

            ItemStack cell = cells > 0 ? ItemHandlerHelper.copyStackWithSize(ModHandler.emptyCell, cells) : ItemStack.EMPTY;
            ingredients.setInput(VanillaTypes.ITEM, cell);
        } else {
            List<List<ItemStack>> inputs = StreamEx.of(input.getMatchingInputs())
                .map(stack -> ItemHandlerHelper.copyStackWithSize(stack, count))
                .toListAndThen(list -> {
                    ItemStack cell = cells > 0 ? ItemHandlerHelper.copyStackWithSize(ModHandler.emptyCell, cells) : ItemStack.EMPTY;
                    return Arrays.asList(Collections.singletonList(cell), list);
                });
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        }

        ingredients.setOutputs(VanillaTypes.ITEM, this.recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, this.recipe, this.showEnergyCost);
    }
}
