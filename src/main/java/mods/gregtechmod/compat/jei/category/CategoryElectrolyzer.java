package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.ElectrolyzerRecipeFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialElectrolyzer;
import mods.gregtechmod.recipe.RecipeElectrolyzer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class CategoryElectrolyzer extends CategoryBase<RecipeElectrolyzer, WrapperCellular> {
    private final IDrawable gauge;
    private final IDrawable tank;

    public CategoryElectrolyzer(IGuiHelper guiHelper) {
        super("industrial_electrolyzer", RecipeElectrolyzer.class, WrapperCellular::new, guiHelper);
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_UP);
        tank = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 40, 0, 18, 18);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Reference.MODID, "textures/gui/industrial_electrolyzer.png"), 48, 9, 79, 74)
                .addPadding(0, 15, 48, 48)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return ElectrolyzerRecipeFactory.INSTANCE.getCellularRecipes(GtRecipes.industrialElectrolyzer, true);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiIndustrialElectrolyzer.class, 73, 30, 30, 10, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 49, 36);
        guiItemStacks.init(1, true, 79, 36);
        guiItemStacks.init(2, false, 49, 6);
        guiItemStacks.init(3, false, 69, 6);
        guiItemStacks.init(4, false, 89, 6);
        guiItemStacks.init(5, false, 109, 6);
    }

    @Override
    protected void initFluidsSlots(IGuiFluidStackGroup guiFluidStacks) {
        guiFluidStacks.init(6, true, 110, 37, 16, 16, 1, false, null);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 73, 25);
        gauge.draw(minecraft, 83, 25);
        gauge.draw(minecraft, 93, 25);

        tank.draw(minecraft, 109, 36);
    }
}
