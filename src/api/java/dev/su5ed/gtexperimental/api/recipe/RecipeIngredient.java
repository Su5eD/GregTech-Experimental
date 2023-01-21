package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Predicate;

public interface RecipeIngredient<T> extends Predicate<T> {
    RecipeIngredientType<?> getType();
    
    int getCount();
    
    boolean isEmpty();
    
    Ingredient asIngredient();

    void toNetwork(FriendlyByteBuf buffer);
    
    JsonElement toJson();
}
