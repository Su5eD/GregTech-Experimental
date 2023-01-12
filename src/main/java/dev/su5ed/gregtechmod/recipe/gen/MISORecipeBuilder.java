package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.MISORecipeType;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class MISORecipeBuilder extends ModRecipeBuilder {
    protected final MISORecipeType<?> recipeType;
    protected final RecipeSerializer<?> recipeSerializer;
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final ItemStack output;

    public MISORecipeBuilder(MISORecipeType<?> recipeType, RecipeSerializer<?> recipeSerializer, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output) {
        this.recipeType = recipeType;
        this.recipeSerializer = recipeSerializer;
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    protected ModFinishedRecipe getFinishedRecipe(ResourceLocation recipeId) {
        return new MISOFinishedRecipe(recipeId);
    }
    
    private class MISOFinishedRecipe extends ModFinishedRecipe {
        
        public MISOFinishedRecipe(ResourceLocation recipeId) {
            super(recipeId, MISORecipeBuilder.this.recipeSerializer);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray inputs = new JsonArray(MISORecipeBuilder.this.inputs.size());
            for (RecipeIngredient<ItemStack> input : MISORecipeBuilder.this.inputs) {
                inputs.add(input.toJson());
            }
            json.add("input", inputs);
            json.add("output", MISORecipeBuilder.this.recipeType.outputType.toJson(MISORecipeBuilder.this.output));
        }
    }
}
