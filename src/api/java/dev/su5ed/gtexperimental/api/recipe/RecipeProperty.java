package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface RecipeProperty<T> {
    String getName();

    void toNetwork(FriendlyByteBuf buffer, T value);

    T fromNetwork(FriendlyByteBuf buffer);

    JsonElement toJson(T value);

    Tag toNBT(T value);

    T fromJson(JsonElement element);
    
    T fromNBT(Tag tag);

    void validate(ResourceLocation id, T value);
}
