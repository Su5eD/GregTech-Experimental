package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public abstract class BaseRecipeImpl<T extends BaseRecipeType<?, OUT>, IN, OUT, C extends BaseRecipeImpl<T, IN, OUT, C>> implements BaseRecipe<T, IN, OUT, C> {
    protected final T type;
    protected final RecipeSerializer<?> serializer;
    protected final ResourceLocation id;

    protected final OUT output;
    protected final RecipePropertyMap properties;

    public BaseRecipeImpl(T type, RecipeSerializer<?> serializer, ResourceLocation id, OUT output, RecipePropertyMap properties) {
        this.type = type;
        this.serializer = serializer;
        this.id = id;
        this.output = output;
        this.properties = properties;

        this.properties.validate(this.id, this.type.getProperties());
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public T getType() {
        return this.type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public OUT getOutput() {
        return this.output;
    }

    @Override
    public RecipeProperties getProperties() {
        return this.properties;
    }
}
