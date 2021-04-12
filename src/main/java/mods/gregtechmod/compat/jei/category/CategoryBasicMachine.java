package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.gui.GuiBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class CategoryBasicMachine<W extends WrapperBasicMachine<R>, R extends IMachineRecipe<?, List<ItemStack>>> implements IRecipeCategory<W> {
    private final String name;
    private final Class<R> recipeClass;
    private final Class<? extends GuiBasicMachine<?>> guiClass;
    protected final String uid;
    private final IDrawable background;
    private final IDrawable gauge;

    public CategoryBasicMachine(String name, Class<R> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass, boolean customTexture, IGuiHelper guiHelper) {
        this.name = name;
        this.recipeClass = recipeClass;
        this.guiClass = guiClass;
        this.uid = Reference.MODID+"."+name;
        ResourceLocation guiTexture = new ResourceLocation(Reference.MODID, String.format("textures/gui%s/%s.png", customTexture ? "/jei" : "", name));
        this.background = guiHelper.drawableBuilder(guiTexture, 34, 24, 108, 18)
                .addPadding(24, 44, 34, 34)
                .build();
        IDrawableStatic gaugeStatic = guiHelper.createDrawable(guiTexture, 176, 0, 20, 18);
        this.gauge = guiHelper.createAnimatedDrawable(gaugeStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    public void init(IModRegistry registry, IRecipeWrapperFactory<R> recipeWrapperFactory) {
        registry.handleRecipes(this.recipeClass, recipeWrapperFactory, this.uid);

        addRecipes(registry);

        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity(this.name), this.uid);

        registry.addRecipeClickArea(this.guiClass, 78, 24, 18, 18, this.uid);
    }

    protected abstract void addRecipes(IModRegistry registry);

    @Override
    public String getUid() {
        return this.uid;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock."+this.name);
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, W recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        initSlots(guiItemStacks);

        guiItemStacks.set(ingredients);
    }

    protected void initSlots(IGuiItemStackGroup guiItemStacks) {
        guiItemStacks.init(0, true, 52, 24);
        guiItemStacks.init(1, false, 106, 24);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.gauge.draw(minecraft, 78, 24);
    }
}
