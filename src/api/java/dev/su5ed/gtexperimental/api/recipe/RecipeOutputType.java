package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface RecipeOutputType<T> {
    void toNetwork(FriendlyByteBuf buffer, T value);

    JsonElement toJson(T value);

    T fromNetwork(FriendlyByteBuf buffer);

    T fromJson(JsonElement json);

    Tag toNBT(T value);

    T fromNBT(Tag tag);
    
    T copy(T value);

    void validate(ResourceLocation id, String name, T value, boolean allowEmpty);

    RecipeOutputType<List<T>> listOf(int count);
}
