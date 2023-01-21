package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public interface RecipeOutputType<T> {
    void toNetwork(FriendlyByteBuf buffer, T output);

    JsonObject toJson(T output);

    T fromNetwork(FriendlyByteBuf buffer);

    T fromJson(JsonElement element);

    static List<ItemStack> parseOutputs(List<? extends RecipeOutputType<ItemStack>> outputTypes, JsonArray json) {
        if (!json.isEmpty() && outputTypes.size() >= json.size()) {
            return StreamEx.of(outputTypes)
                .zipWith(StreamEx.of(json.getAsJsonArray().iterator()))
                .mapKeyValue(RecipeOutputType::fromJson)
                .toList();
        }
        throw new IllegalArgumentException("Output type and json sizes differ");
    }
}
