package dev.su5ed.gtexperimental.compat.jei;

import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.ItemProvider;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.Lazy;

public abstract class BaseRecipeCategory<T> implements IRecipeCategory<T> {
    private final Component title;
    private final Lazy<IDrawable> background;
    private final IDrawable icon;
    private final RecipeType<T> recipeType;

    public <U extends BlockEntityProvider & ItemProvider> BaseRecipeCategory(U provider, IGuiHelper guiHelper, RecipeType<T> recipeType) {
        this.title = provider.getDummyInstance().getDisplayName();
        this.background = Lazy.of(() -> drawBackground(guiHelper));
        this.icon = guiHelper.createDrawableItemStack(provider.getItemStack());
        this.recipeType = recipeType;
    }

    protected abstract IDrawable drawBackground(IGuiHelper guiHelper);

    @Override
    public RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background.get();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
}
