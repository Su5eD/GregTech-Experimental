package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperSecondaryFluid;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialSawmill;
import mods.gregtechmod.recipe.RecipeSawmill;
import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.List;

public class CategoryIndustrialSawmill extends CategoryBase<RecipeSawmill, WrapperSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>>> {
    private final IDrawable gauge;
    
    public CategoryIndustrialSawmill(IGuiHelper guiHelper) {
        super("industrial_sawmill", RecipeSawmill.class, recipe -> new WrapperSecondaryFluid<>(recipe, 3), guiHelper);
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SAWING);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(GuiIndustrialSawmill.TEXTURE, 33, 15, 106, 46)
                .addPadding(10, 33, 26, 43)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return RecipeWrapperFactory.getMultiRecipes(GtRecipes.industrialSawmill, recipe -> new WrapperSecondaryFluid<>(recipe, 3));
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiIndustrialSawmill.class, 58, 27, 20, 11, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 26, 10);
        guiItemStacks.init(1, true, 26, 28);
        guiItemStacks.init(2, false, 78, 19);
        guiItemStacks.init(3, false, 96, 19);
        guiItemStacks.init(4, false, 114, 19);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 51, 23);
    }
}
