package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;

import java.util.Collection;

public abstract class CategoryStruct<R, T extends IRecipeWrapper> implements IRecipeCategory<T> {
    protected final String name;
    protected final String uid;
    private final IDrawable background;
    private final Class<R> recipeClass;
    private final IRecipeWrapperFactory<R> recipeWrapperFactory;
    
    public CategoryStruct(String name, Class<R> recipeClass, IRecipeWrapperFactory<R> recipeWrapperFactory, IGuiHelper guiHelper) {
        this.name = name;
        this.uid = Reference.MODID + "." + this.name;
        this.background = drawBackground(guiHelper);
        this.recipeClass = recipeClass;
        this.recipeWrapperFactory = recipeWrapperFactory;
    }
    
    protected abstract IDrawable drawBackground(IGuiHelper guiHelper);
    
    protected abstract Collection<? extends T> getRecipes();
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(this.recipeClass, this.recipeWrapperFactory, this.uid);
        registry.addRecipes(getRecipes(), this.uid);
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity(this.name), this.uid);
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, T recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        initSlots(guiItemStacks);
        guiItemStacks.set(ingredients);
    }
    
    protected abstract void initSlots(IGuiItemStackGroup guiItemStacks);

    @Override
    public String getUid() {
        return this.uid;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock." + this.name);
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }
}
