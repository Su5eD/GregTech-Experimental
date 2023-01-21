package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface BaseRecipe<T extends RecipeType<?>, I, C extends BaseRecipe<T, I, C>> extends Recipe<Container> {
    void toNetwork(FriendlyByteBuf buffer);

    boolean matches(I input);

    int compareInputCount(C other);
}
