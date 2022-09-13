package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.gui.GuiColors;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class WrapperFusion implements IRecipeWrapper {
    private final IRecipeFusion recipe;

    public WrapperFusion(IRecipeFusion recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<FluidStack>> inputs = StreamEx.of(recipe.getInput())
            .map(IRecipeIngredientFluid::getFluidStacks)
            .toList();
        
        ingredients.setInputLists(VanillaTypes.FLUID, inputs);
        recipe.getOutput().when(
            itemStack -> ingredients.setOutput(VanillaTypes.ITEM, itemStack),
            fluidStack -> ingredients.setOutput(VanillaTypes.FLUID, fluidStack)
        );
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, recipe, 14, true);
        
        minecraft.fontRenderer.drawString(GtLocale.translateInfo("fusion_start", JavaUtil.formatNumber(recipe.getStartEnergy())), 2, 64, GuiColors.BLACK, false);
    }
}
