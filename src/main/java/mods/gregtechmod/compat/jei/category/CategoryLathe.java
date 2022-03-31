package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiLathe;
import mods.gregtechmod.recipe.RecipeLathe;

public class CategoryLathe extends CategoryBasicMachine<WrapperBasicMachineSingle<RecipeLathe>, RecipeLathe> {

    public CategoryLathe(IGuiHelper guiHelper) {
        super("lathe", RecipeLathe.class, GuiLathe.class, WrapperBasicMachineSingle::new, () -> RecipeWrapperFactory.getBasicMachineSingleRecipes(GtRecipes.lathe), true, GregtechGauge.TURNING, guiHelper);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        super.initSlots(guiItemStacks);
        guiItemStacks.init(2, false, 124, 24);
    }
}
