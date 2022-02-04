package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.ElectrolyzerRecipeFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiDistillationTower;
import mods.gregtechmod.recipe.RecipeDistillation;
import net.minecraft.client.Minecraft;

import java.util.Collection;

public class CategoryDistillationTower extends CategoryBase<RecipeDistillation, WrapperCellular> {
    private final IDrawable gauge;

    public CategoryDistillationTower(IGuiHelper guiHelper) {
        super("distillation_tower", RecipeDistillation.class, recipe -> new WrapperCellular(recipe, true), guiHelper);
        this.gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.DISTILLING);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(GuiDistillationTower.TEXTURE, 61, 4, 54, 72)
            .addPadding(0, 17, 65, 55)
            .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return ElectrolyzerRecipeFactory.INSTANCE.getCellularRecipes(GtRecipes.distillation, true);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiDistillationTower.class, 80, 4, 16, 72, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(1, true, 65, 36);
        guiItemStacks.init(0, true, 65, 54);
        guiItemStacks.init(2, false, 101, 0);
        guiItemStacks.init(3, false, 101, 18);
        guiItemStacks.init(4, false, 101, 36);
        guiItemStacks.init(5, false, 101, 54);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 84, 0);
    }
}
