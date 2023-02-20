package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface RecipeOutputType<T> {
    void toNetwork(FriendlyByteBuf buffer, T value);

    JsonObject toJson(T value);

    T fromNetwork(FriendlyByteBuf buffer);

    T fromJson(JsonObject json);
    
    void validate(ResourceLocation id, String name, T value, boolean allowEmpty);
}
