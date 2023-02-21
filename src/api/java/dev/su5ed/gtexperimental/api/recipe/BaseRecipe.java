package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface BaseRecipe<T extends RecipeType<?>, IN, OUT, C extends BaseRecipe<T, IN, OUT, C>> extends Recipe<Container> {
    void toNetwork(FriendlyByteBuf buffer);

    boolean matches(IN input);

    int compareInputCount(C other);

    OUT getOutput();

    RecipeProperties getProperties();

    default <U> U getProperty(RecipeProperty<U> property) {
        return getProperties().get(property);
    }
}
