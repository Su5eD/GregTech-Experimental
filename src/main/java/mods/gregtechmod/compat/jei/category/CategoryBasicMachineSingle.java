package mods.gregtechmod.compat.jei.category;

import ic2.core.gui.Gauge;
import mezz.jei.api.IGuiHelper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import mods.gregtechmod.gui.GuiBasicMachine;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CategoryBasicMachineSingle<R extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> extends CategoryBasicMachine<WrapperBasicMachine<R>, R> {

    public CategoryBasicMachineSingle(String name, Class<R> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeManager, boolean customTexture, Gauge.IGaugeStyle gauge, IGuiHelper guiHelper) {
        super(name, recipeClass, guiClass, WrapperBasicMachineSingle::new, () -> RecipeWrapperFactory.getBasicMachineSingleRecipes(recipeManager), customTexture, gauge, guiHelper);
    }
}
