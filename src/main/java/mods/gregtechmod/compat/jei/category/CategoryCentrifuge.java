package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.CentrifugeRecipeFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.recipe.RecipeCentrifuge;
import net.minecraft.client.Minecraft;

import java.util.Collection;

public class CategoryCentrifuge extends CategoryBase<RecipeCentrifuge, WrapperCellular> {
    private final IDrawable gaugeUp;
    private final IDrawable gaugeDown;
    private final IDrawable gaugeRight;
    private final IDrawable gaugeLeft;
    private final IDrawable tank;

    public CategoryCentrifuge(IGuiHelper guiHelper) {
        super("industrial_centrifuge", RecipeCentrifuge.class, WrapperCellular::new, guiHelper);
        
        gaugeUp = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_UP);
        gaugeDown = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_DOWN);
        gaugeRight = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_RIGHT);
        gaugeLeft = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_LEFT);
        
        tank = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 40, 0, 18, 18);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper
                .drawableBuilder(GuiIndustrialCentrifuge.TEXTURE, 48, 4, 79, 79)
                .addPadding(0, 0, 48, 48)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return CentrifugeRecipeFactory.INSTANCE.getCellularRecipes(GtRecipes.industrialCentrifuge, false);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 98, 38, 10, 10, this.uid);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 83, 23, 10, 10, this.uid);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 68, 38, 10, 10, this.uid);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 83, 53, 10, 10, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 49, 0);
        guiItemStacks.init(1, true, 79, 30);
        guiItemStacks.init(2, false, 79, 0);
        guiItemStacks.init(3, false, 109, 30);
        guiItemStacks.init(4, false, 79, 60);
        guiItemStacks.init(5, false, 49, 30);
    }

    @Override
    protected void initFluidsSlots(IGuiFluidStackGroup guiFluidStacks) {
        guiFluidStacks.init(0, true, 110, 60, 16, 16, 1, false, null);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gaugeUp.draw(minecraft, 83, 19);
        gaugeDown.draw(minecraft, 83, 49);
        gaugeRight.draw(minecraft, 98, 34);
        gaugeLeft.draw(minecraft, 68, 34);

        tank.draw(minecraft, 109, 59);
    }
}
