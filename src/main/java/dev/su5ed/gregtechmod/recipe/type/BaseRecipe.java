package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class BaseRecipe<T extends RecipeType<?>, I, C extends BaseRecipe<T, I, C>> implements Recipe<Container> {
    protected final T type;
    protected final RecipeSerializer<?> serializer;
    protected final ResourceLocation id;

    public BaseRecipe(T type, RecipeSerializer<?> serializer, ResourceLocation id) {
        this.type = type;
        this.serializer = serializer;
        this.id = id;
        
        validate();
    }
    
    public void validate() {}

    public abstract void toNetwork(FriendlyByteBuf buffer);
    
    public abstract boolean matches(I input);
    
    public abstract int compareInputCount(C other);

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
}
