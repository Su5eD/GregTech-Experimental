package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import one.util.streamex.StreamEx;

import java.util.stream.Stream;

public class VanillaRecipeIngredientType implements RecipeIngredientType<VanillaRecipeIngredient> {
    public VanillaRecipeIngredient of(ItemLike... items) {
        return new VanillaRecipeIngredient(Ingredient.of(items));
    }

    public VanillaRecipeIngredient of(ItemLike item, int count) {
        return new VanillaRecipeIngredient(Ingredient.of(item), count);
    }

    public VanillaRecipeIngredient of(ItemStack stack) {
        return new VanillaRecipeIngredient(Ingredient.of(stack), stack.getCount());
    }

    public VanillaRecipeIngredient of(TagKey<Item> tag) {
        return of(tag, 1);
    }

    public VanillaRecipeIngredient of(TagKey<Item> tag, int count) {
        return new VanillaRecipeIngredient(Ingredient.of(tag), count);
    }

    @SafeVarargs
    public final VanillaRecipeIngredient ofTags(TagKey<Item>... tags) {
        return ofTags(1, tags);
    }

    @SafeVarargs
    public final VanillaRecipeIngredient ofTags(int count, TagKey<Item>... tags) {
        return new VanillaRecipeIngredient(Ingredient.fromValues(StreamEx.of(tags).map(Ingredient.TagValue::new)), count);
    }

    public VanillaRecipeIngredient ofValues(Ingredient.Value... values) {
        return new VanillaRecipeIngredient(Ingredient.fromValues(Stream.of(values)));
    }

    @Override
    public VanillaRecipeIngredient create(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        JsonElement value = obj.get("value");
        int count = GsonHelper.getAsInt(obj, "count", 1);
        return new VanillaRecipeIngredient(Ingredient.fromJson(value), count);
    }

    @Override
    public VanillaRecipeIngredient create(FriendlyByteBuf buffer) {
        return new VanillaRecipeIngredient(Ingredient.fromNetwork(buffer));
    }
}
