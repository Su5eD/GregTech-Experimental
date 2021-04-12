package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.compat.jei.RecipeMaker;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.gui.GuiAlloySmelter;
import mods.gregtechmod.recipe.RecipeAlloySmelter;

public class CategoryAlloySmelter extends CategoryBasicMachine<WrapperBasicMachine<RecipeAlloySmelter>, RecipeAlloySmelter> {

    public CategoryAlloySmelter(IGuiHelper guiHelper) {
        super("alloy_smelter", RecipeAlloySmelter.class, GuiAlloySmelter.class, guiHelper);
    }

    @Override
    protected void addRecipes(IModRegistry registry) {
        registry.addRecipes(RecipeMaker.getAlloySmelterRecipes(), this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 34, 24);
        guiItemStacks.init(1, true, 52, 24);
        guiItemStacks.init(2, false, 106, 24);
    }
}
