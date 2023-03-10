package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.util.FluidProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public class VanillaRecipeIngredientType implements RecipeIngredientType<RecipeIngredient<ItemStack>, ItemStack> {
    public VanillaRecipeIngredient of(ItemLike... items) {
        return new VanillaRecipeIngredient(Ingredient.of(items));
    }

    public VanillaRecipeIngredient of(int count, ItemLike... items) {
        return new VanillaRecipeIngredient(Ingredient.of(items), count);
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

    public VanillaRecipeIngredient ofDamaged(ItemLike item) {
        return new VanillaRecipeIngredient(VanillaDamagedIngredient.of(item));
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

    public VanillaRecipeIngredient ofFluid(FluidProvider provider) {
        return ofFluidBuckets(provider, 1);
    }

    public VanillaRecipeIngredient ofFluidBuckets(FluidProvider provider, int count) {
        return new VanillaRecipeIngredient(ModRecipeIngredientTypes.FLUID.of(provider, buckets(count)));
    }

    public VanillaRecipeIngredient ofFluidBuckets(TagKey<Fluid> tag, int count) {
        return new VanillaRecipeIngredient(ModRecipeIngredientTypes.FLUID.of(tag, buckets(count)));
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

    @Override
    public ListRecipeIngredientType<List<RecipeIngredient<ItemStack>>, ItemStack> listOf(int count) {
        return new ListRecipeIngredientTypeImpl<>(this, count);
    }
}
