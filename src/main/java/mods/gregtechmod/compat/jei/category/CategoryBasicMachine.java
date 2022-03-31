package mods.gregtechmod.compat.jei.category;

import ic2.core.gui.Gauge;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.gui.GuiBasicMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class CategoryBasicMachine<W extends WrapperBasicMachine<R>, R extends IMachineRecipe<?, List<ItemStack>>> extends CategoryBase<R, W> {
    private final ResourceLocation backgroundTexture;
    private final Class<? extends GuiBasicMachine<?>> guiClass;
    private final Supplier<Collection<?>> recipeFactory;
    private final IDrawable gauge;

    public CategoryBasicMachine(String name, Class<R> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass, IRecipeWrapperFactory<R> recipeWrapperFactory, Supplier<Collection<?>> recipeFactory, boolean customTexture, Gauge.IGaugeStyle gauge, IGuiHelper guiHelper) {
        super(name, recipeClass, recipeWrapperFactory, guiHelper);

        this.guiClass = guiClass;
        this.recipeFactory = recipeFactory;
        this.backgroundTexture = new ResourceLocation(Reference.MODID, String.format("textures/gui%s/%s.png", customTexture ? "/jei" : "", name));
        this.gauge = JEIUtils.gaugeToDrawable(guiHelper, gauge);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(this.backgroundTexture, 34, 24, 108, 18)
            .addPadding(24, 44, 34, 34)
            .build();
    }

    @Override
    protected Collection<?> getRecipes() {
        return this.recipeFactory.get();
    }

    @Override
    public void init(IModRegistry registry) {
        super.init(registry);
        registry.addRecipeClickArea(this.guiClass, 78, 24, 18, 18, this.uid);
    }

    @Override
    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 52, 24);
        guiItemStacks.init(1, false, 106, 24);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.gauge.draw(minecraft, 78, 24);
    }
}
