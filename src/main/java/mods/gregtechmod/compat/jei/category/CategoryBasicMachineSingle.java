package mods.gregtechmod.compat.jei.category;

import ic2.core.gui.Gauge;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
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
    private final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeManager;

    public CategoryBasicMachineSingle(String name, Class<R> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass, boolean customTexture, IGuiHelper guiHelper,
                                      IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeManager, Gauge.IGaugeStyle gauge) {
        super(name, recipeClass, guiClass, customTexture, guiHelper, gauge);
        this.recipeManager = recipeManager;
    }

    @Override
    protected void addRecipes(IModRegistry registry) {
        registry.addRecipes(RecipeWrapperFactory.getBasicMachineSingleRecipes(this.recipeManager), this.uid);
    }

    public void init(IModRegistry registry) {
        init(registry, WrapperBasicMachineSingle::new);
    }
}