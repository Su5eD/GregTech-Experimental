package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public interface RecipeProperties {
    <T> T get(RecipeProperty<T> property);

    <T> Optional<T> getOptional(RecipeProperty<T> property);
    
    void toNetwork(FriendlyByteBuf buffer);
    
    void toJson(JsonObject json);
    
    CompoundTag toNBT();
    
    void validate(ResourceLocation id, List<RecipeProperty<?>> expected);
}
