package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface RecipeOutputType<T> {
    void toNetwork(FriendlyByteBuf buffer, T output);
    
    JsonObject toJson(T output);

    T fromNetwork(FriendlyByteBuf buffer);

    T fromJson(JsonElement element);
}
