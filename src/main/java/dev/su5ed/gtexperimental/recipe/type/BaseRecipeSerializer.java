package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class BaseRecipeSerializer<R extends BaseRecipeImpl<?, ?, ? super R>> implements RecipeSerializer<R> {
    private final BaseRecipeType<R> recipeType;

    public BaseRecipeSerializer(BaseRecipeType<R> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        return this.recipeType.fromJson(recipeId, serializedRecipe);
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return this.recipeType.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, R recipe) {
        recipe.toNetwork(buffer);
    }
}
