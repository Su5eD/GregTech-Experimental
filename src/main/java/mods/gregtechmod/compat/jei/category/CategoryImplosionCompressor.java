package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperMultiInput;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiImplosionCompressor;
import mods.gregtechmod.recipe.RecipeImplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;

public class CategoryImplosionCompressor extends CategoryStruct<RecipeImplosion, WrapperMultiInput<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    private final IDrawable gauge;

    public CategoryImplosionCompressor(IGuiHelper guiHelper) {
        super("implosion_compressor", RecipeImplosion.class, WrapperMultiInput::new, guiHelper);
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.IMPLODING);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Reference.MODID, "textures/gui/implosion_compressor.png"), 33, 15, 123, 46)
                .addPadding(10, 33, 26, 26)
                .build();
    }

    @Override
    protected Collection<? extends WrapperMultiInput<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> getRecipes() {
        return RecipeWrapperFactory.getMultiRecipes(GtRecipes.implosion, WrapperMultiInput::new);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiImplosionCompressor.class, 58, 28, 20, 11, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 26, 10);
        guiItemStacks.init(1, true, 26, 28);
        guiItemStacks.init(2, false, 78, 19);
        guiItemStacks.init(3, false, 96, 19);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 51, 23);
    }
}