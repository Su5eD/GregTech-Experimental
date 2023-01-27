package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class HybridRecipeIngredient implements RecipeIngredient<Either<ItemStack, FluidStack>> {
    private final Either<RecipeIngredient<ItemStack>, RecipeIngredient<FluidStack>> ingredient;

    public HybridRecipeIngredient(Either<RecipeIngredient<ItemStack>, RecipeIngredient<FluidStack>> ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public RecipeIngredientType<?> getType() {
        return ModRecipeIngredientTypes.HYBRID;
    }

    @Override
    public int getCount() {
        return this.ingredient.map(RecipeIngredient::getCount, RecipeIngredient::getCount);
    }

    @Override
    public boolean isEmpty() {
        return this.ingredient.map(RecipeIngredient::isEmpty, RecipeIngredient::isEmpty);
    }

    @Override
    public Ingredient asIngredient() {
        return this.ingredient.map(RecipeIngredient::asIngredient, RecipeIngredient::asIngredient);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeEither(this.ingredient, (buf, val) -> val.toNetwork(buffer), (buf, val) -> val.toNetwork(buffer));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = this.ingredient.map(RecipeIngredient::toJson, RecipeIngredient::toJson);
        json.addProperty("type", this.ingredient.<String>map(i -> "item", i -> "fluid"));
        return json;
    }

    @Override
    public boolean test(Either<ItemStack, FluidStack> either) {
        return this.ingredient.map(i -> either.left().map(i::test), i -> either.right().map(i::test)).orElse(false);
    }
}
