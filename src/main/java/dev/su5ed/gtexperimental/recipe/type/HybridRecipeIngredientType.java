package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.util.ItemProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class HybridRecipeIngredientType implements RecipeIngredientType<HybridRecipeIngredient> {
    public HybridRecipeIngredient of(ItemLike... items) {
        return new HybridRecipeIngredient(Either.left(ModRecipeIngredientTypes.ITEM.of(items)));
    }

    public HybridRecipeIngredient of(int count, ItemLike... items) {
        return new HybridRecipeIngredient(Either.left(ModRecipeIngredientTypes.ITEM.of(count, items)));
    }

    public HybridRecipeIngredient of(ItemProvider provider) {
        return new HybridRecipeIngredient(Either.left(ModRecipeIngredientTypes.ITEM.of(provider)));
    }

    public HybridRecipeIngredient of(ItemLike item) {
        return new HybridRecipeIngredient(Either.left(ModRecipeIngredientTypes.ITEM.of(item)));
    }

    public HybridRecipeIngredient of(ItemLike item, int count) {
        return new HybridRecipeIngredient(Either.left(ModRecipeIngredientTypes.ITEM.of(item, count)));
    }

    public HybridRecipeIngredient of(TagKey<Item> tag) {
        return of(tag, 1);
    }

    public HybridRecipeIngredient of(TagKey<Item> tag, int count) {
        return new HybridRecipeIngredient(Either.left(ModRecipeIngredientTypes.ITEM.of(tag, count)));
    }

    public HybridRecipeIngredient ofFluid(TagKey<Fluid> tag, int amount) {
        return new HybridRecipeIngredient(Either.right(ModRecipeIngredientTypes.FLUID.of(tag, amount)));
    }

    @Override
    public HybridRecipeIngredient create(JsonObject json) {
        String type = GsonHelper.getAsString(json, "type");
        Either<RecipeIngredient<ItemStack>, RecipeIngredient<FluidStack>> either = switch (type) {
            case "item" -> Either.left(ModRecipeIngredientTypes.ITEM.create(json));
            case "fluid" -> Either.right(ModRecipeIngredientTypes.FLUID.create(json));
            default -> throw new IllegalArgumentException("Invalid ingredient type " + type);
        };
        return new HybridRecipeIngredient(either);
    }

    @Override
    public HybridRecipeIngredient create(FriendlyByteBuf buffer) {
        Either<RecipeIngredient<ItemStack>, RecipeIngredient<FluidStack>> either = buffer.readEither(ModRecipeIngredientTypes.ITEM::create, ModRecipeIngredientTypes.FLUID::create);
        return new HybridRecipeIngredient(either);
    }
}
