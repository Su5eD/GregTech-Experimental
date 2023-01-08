package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.client.resources.I18n;

import java.util.Collection;

public abstract class CategoryBase<R, W extends IRecipeWrapper> implements IRecipeCategory<W> {
    protected final String name;
    protected final String uid;
    private final LazyValue<IDrawable> background;
    protected final Class<R> recipeClass;
    protected final IRecipeWrapperFactory<R> recipeWrapperFactory;

    public CategoryBase(String name, Class<R> recipeClass, IRecipeWrapperFactory<R> recipeWrapperFactory, IGuiHelper guiHelper) {
        this.name = name;
        this.uid = Reference.MODID + "." + this.name;
        this.background = new LazyValue<>(() -> drawBackground(guiHelper));
        this.recipeClass = recipeClass;
        this.recipeWrapperFactory = recipeWrapperFactory;
    }

    protected abstract IDrawable drawBackground(IGuiHelper guiHelper);

    protected abstract Collection<?> getRecipes();

    public void init(IModRegistry registry) {
        registry.handleRecipes(this.recipeClass, this.recipeWrapperFactory, this.uid);
        registry.addRecipes(getRecipes(), this.uid);
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity(this.name), this.uid);
        addRecipeClickArea(registry);
    }

    protected void addRecipeClickArea(IModRegistry registry) {}

    @Override
    public final void setRecipe(IRecipeLayout recipeLayout, W recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        initSlots(guiItemStacks);
        guiItemStacks.set(ingredients);

        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
        initFluidsSlots(guiFluidStacks);
        guiFluidStacks.set(ingredients);
    }

    protected void initSlots(IGuiItemStackGroup guiItemStacks) {}

    protected void initFluidsSlots(IGuiFluidStackGroup guiFluidStacks) {}

    @Override
    public String getUid() {
        return this.uid;
    }

    @Override
    public String getTitle() {
        return I18n.format(GtLocale.buildKey("teblock", Reference.MODID + "_" + this.name));
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background.get();
    }
}
