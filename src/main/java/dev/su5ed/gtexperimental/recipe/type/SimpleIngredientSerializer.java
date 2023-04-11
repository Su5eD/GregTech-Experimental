package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleIngredientSerializer<T extends Ingredient> implements IIngredientSerializer<T> {
    private final Function<Stream<? extends Ingredient.Value>, T> factory;

    public SimpleIngredientSerializer(Function<Stream<? extends Ingredient.Value>, T> factory) {
        this.factory = factory;
    }

    @Override
    public T parse(FriendlyByteBuf buffer) {
        return this.factory.apply(RecipeUtil.ingredientValuesFromNetwork(buffer).stream());
    }

    @Override
    public T parse(JsonObject json) {
        return this.factory.apply(RecipeUtil.ingredientFromJson(json));
    }

    @Override
    public void write(FriendlyByteBuf buffer, T ingredient) {
        buffer.writeCollection(List.of(ingredient.getItems()), FriendlyByteBuf::writeItem);
    }
}
