package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiVacuumFreezer;
import mods.gregtechmod.recipe.RecipeVacuumFreezer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class CategoryVacuumFreezer extends CategoryBase<RecipeVacuumFreezer, WrapperBasicMachineSingle<RecipeVacuumFreezer>> {
    private final IDrawable gauge;

    public CategoryVacuumFreezer(IGuiHelper guiHelper) {
        super("vacuum_freezer", RecipeVacuumFreezer.class, recipe -> new WrapperBasicMachineSingle<>(recipe, -10), guiHelper);
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.FREEZING);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Reference.MODID, "textures/gui/vacuum_freezer.png"), 33, 24, 70, 18)
                .addPadding(20, 51, 52, 52)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return RecipeWrapperFactory.getBasicMachineSingleRecipes(GtRecipes.vacuumFreezer);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);    
        registry.addRecipeClickArea(GuiVacuumFreezer.class, 58, 28, 20, 11, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 52, 20);
        guiItemStacks.init(1, false, 104, 20);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 77, 24);
    }
}
