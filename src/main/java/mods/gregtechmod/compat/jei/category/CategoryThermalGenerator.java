package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.compat.jei.wrapper.WrapperFuel;
import mods.gregtechmod.gui.GuiThermalGenerator;
import mods.gregtechmod.recipe.fuel.FuelSimple;

public class CategoryThermalGenerator extends CategoryGenerator<FuelSimple> {
    
    public CategoryThermalGenerator(IGuiHelper guiHelper) {
        super("thermal_generator", FuelSimple.class, WrapperFuel::new, GtFuels.hot, guiHelper);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiThermalGenerator.class, 81, 35, 14, 15, this.uid);
    }
}
