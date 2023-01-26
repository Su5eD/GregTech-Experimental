package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface RecipeProperty<T> {
    String getName();
    
    void toNetwork(FriendlyByteBuf buffer, T value);

    JsonElement toJson(T value);

    T fromNetwork(FriendlyByteBuf buffer);

    T fromJson(JsonElement element);

    void validate(ResourceLocation id, T value);
}
