package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WrapperFuel<T extends IFuel<IRecipeIngredient>> implements IRecipeWrapper {
    private final T fuel;
    
    public WrapperFuel(T fuel) {
        this.fuel = fuel;
    }
 
    @Override
    public void getIngredients(IIngredients ingredients) {
        IRecipeIngredient input = this.fuel.getInput();
        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(input.getMatchingInputs()));
        if (input instanceof IRecipeIngredientFluid) {
            List<FluidStack> fluids = ((IRecipeIngredientFluid) input).getMatchingFluids().stream()
                    .map(fluid -> new FluidStack(fluid, Fluid.BUCKET_VOLUME))
                    .collect(Collectors.toList());
            ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(fluids));
        }
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString("EU: " + GtUtil.formatNumber(this.fuel.getEnergy() * 1000) + "EU", 2, 50, -16777216);
    }
}
