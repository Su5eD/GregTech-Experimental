package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperIndustrialBlastFurnace;
import mods.gregtechmod.compat.jei.wrapper.WrapperMultiInput;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialBlastFurnace;
import mods.gregtechmod.recipe.RecipeBlastFurnace;
import net.minecraft.client.Minecraft;

import java.util.Collection;

public class CategoryIndustrialBlastFurnace extends CategoryBase<RecipeBlastFurnace, WrapperMultiInput<?>> {
    private final IDrawable gauge;

    public CategoryIndustrialBlastFurnace(IGuiHelper guiHelper) {
        super("industrial_blast_furnace", RecipeBlastFurnace.class, WrapperIndustrialBlastFurnace::new, guiHelper);
        
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.BLASTING);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(GuiIndustrialBlastFurnace.TEXTURE, 33, 15, 88, 39)
                .addPadding(7, 43, 43, 43)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return RecipeWrapperFactory.getMultiRecipes(GtRecipes.industrialBlastFurnace, WrapperIndustrialBlastFurnace::new);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiIndustrialBlastFurnace.class, 58, 28, 20, 11, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 43, 7);
        guiItemStacks.init(1, true, 43, 25);
        guiItemStacks.init(2, false, 95, 16);
        guiItemStacks.init(3, false, 113, 16);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 68, 20);
    }
}
