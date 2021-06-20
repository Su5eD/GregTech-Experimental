package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.compat.jei.wrapper.WrapperFuel;
import mods.gregtechmod.gui.GuiPlasmaGenerator;
import mods.gregtechmod.recipe.fuel.FuelSimple;

public class CategoryPlasmaGenerator extends CategoryGenerator<FuelSimple> {

    public CategoryPlasmaGenerator(IGuiHelper guiHelper) {
        super("plasma_generator", FuelSimple.class, GuiPlasmaGenerator.class, WrapperFuel::new, GtFuels.plasma, guiHelper);
    }

    @Override
    protected void addRecipeClickArea(IModRegistry registry) {
        registry.addRecipeClickArea(this.guiClass, 86, 35, 14, 15, this.uid);
    }
}
