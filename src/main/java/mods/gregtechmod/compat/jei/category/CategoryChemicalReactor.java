package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperMultiInput;
import mods.gregtechmod.gui.GregtechGauge;
import mods.gregtechmod.gui.GuiChemicalReactor;
import mods.gregtechmod.recipe.RecipeChemical;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class CategoryChemicalReactor extends CategoryBase<RecipeChemical, WrapperMultiInput<?>> {
    private final IDrawable gauge;

    public CategoryChemicalReactor(IGuiHelper guiHelper) {
        super("chemical_reactor", RecipeChemical.class, WrapperMultiInput::new, guiHelper);
        gauge = JEIUtils.gaugeToDrawable(guiHelper, GregtechGauge.SMALL_ARROW_DOWN);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Reference.MODID, "textures/gui/chemical_reactor.png"), 69, 15, 37, 47)
                .addPadding(10, 32, 69, 69)
                .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return RecipeWrapperFactory.getMultiRecipes(GtRecipes.chemical, WrapperMultiInput::new);
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(GuiChemicalReactor.class, 73, 34, 30, 10, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 69, 10);
        guiItemStacks.init(1, true, 89, 10);
        guiItemStacks.init(2, false, 79, 40);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 73, 29);
        gauge.draw(minecraft, 83, 29);
        gauge.draw(minecraft, 93, 29);
    }
}
