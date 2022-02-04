package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperSecondaryFluid;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialGrinder;
import mods.gregtechmod.recipe.RecipeGrinder;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class CategoryIndustrialGrinder extends CategoryBase<RecipeGrinder, WrapperSecondaryFluid<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    private final IDrawable gauge;

    public CategoryIndustrialGrinder(IGuiHelper guiHelper) {
        super("industrial_grinder", RecipeGrinder.class, recipe -> new WrapperSecondaryFluid<>(recipe, 4), guiHelper);
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.MACERATING);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(GuiIndustrialGrinder.TEXTURE, 33, 15, 123, 46)
            .addPadding(10, 33, 26, 26)
            .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return RecipeWrapperFactory.getMultiRecipes(GtRecipes.industrialGrinder, recipe -> new WrapperSecondaryFluid<>(recipe, 4));
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiIndustrialGrinder.class, 58, 28, 20, 10, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 26, 10);
        guiItemStacks.init(1, true, 26, 28);
        guiItemStacks.init(2, false, 78, 19);
        guiItemStacks.init(3, false, 96, 19);
        guiItemStacks.init(4, false, 114, 19);
        guiItemStacks.init(5, false, 132, 19);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 51, 19);
    }
}
