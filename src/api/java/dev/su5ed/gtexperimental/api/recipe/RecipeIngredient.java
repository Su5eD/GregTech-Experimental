package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

public interface RecipeIngredient<T> extends Predicate<T> {
    RecipeIngredientType<?, T> getType();
    
    int getCount();
    
    boolean isEmpty();
    
    boolean testPartial(T value);
    
    Ingredient asIngredient();

    void toNetwork(FriendlyByteBuf buffer);
    
    JsonElement toJson();
    
    void validate(ResourceLocation id, String name);

    default List<ItemStack> getItemStacks() {
        int count = getCount();
        return StreamEx.of(asIngredient().getItems())
            .map(stack -> ItemHandlerHelper.copyStackWithSize(stack, count))
            .toList();
    }
}
