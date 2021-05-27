package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiLathe;
import mods.gregtechmod.recipe.RecipeLathe;

public class CategoryLathe extends CategoryBasicMachineSingle<RecipeLathe> {

    public CategoryLathe(IGuiHelper guiHelper) {
        super("lathe", RecipeLathe.class, GuiLathe.class, true, guiHelper, GtRecipes.lathe, GregtechGauge.TURNING);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        super.initSlots(guiItemStacks);
        guiItemStacks.init(2, false, 124, 24);
    }
}
