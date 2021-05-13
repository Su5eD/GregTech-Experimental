package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineMulti;
import mods.gregtechmod.gui.GuiBasicMachine;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CategoryBasicMachineMulti<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends CategoryBasicMachine<WrapperBasicMachine<R>, R> {
    private final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> recipeManager;
    private final boolean secondaryOutput;

    public CategoryBasicMachineMulti(String name, Class<R> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> recipeManager, boolean secondaryOutput, IGuiHelper guiHelper) {
        this(name, recipeClass, guiClass, recipeManager, false, secondaryOutput, guiHelper);
    }

    public CategoryBasicMachineMulti(String name, Class<R> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> recipeManager, boolean customTexture, boolean secondaryOutput, IGuiHelper guiHelper) {
        super(name, recipeClass, guiClass, customTexture, guiHelper);
        this.recipeManager = recipeManager;
        this.secondaryOutput = secondaryOutput;
    }

    @Override
    protected void addRecipes(IModRegistry registry) {
        registry.addRecipes(RecipeWrapperFactory.getBasicMachineMultiRecipes(this.recipeManager), this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 34, 24);
        guiItemStacks.init(1, true, 52, 24);

        guiItemStacks.init(2, false, 106, 24);
        if (this.secondaryOutput) guiItemStacks.init(3, false, 124, 24);
    }

    public void init(IModRegistry registry) {
        init(registry, WrapperBasicMachineMulti::new);
    }
}
