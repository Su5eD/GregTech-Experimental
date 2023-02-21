package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface RecipeProperties {
    <T> T get(RecipeProperty<T> property);
    
    void toNetwork(FriendlyByteBuf buffer);
    
    void toJson(JsonObject json);
    
    void validate(ResourceLocation id, List<RecipeProperty<?>> expected);
}
