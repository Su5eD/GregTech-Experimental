package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipeType;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import dev.su5ed.gregtechmod.recipe.type.RecipeOutputType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class MIMORecipeBuilder extends ModRecipeBuilder {
    protected final MIMORecipeType<?> recipeType;
    protected final RecipeSerializer<?> recipeSerializer;
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final List<ItemStack> outputs;

    public MIMORecipeBuilder(MIMORecipeType<?> recipeType, RecipeSerializer<?> recipeSerializer, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs) {
        this.recipeType = recipeType;
        this.recipeSerializer = recipeSerializer;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    protected ModFinishedRecipe getFinishedRecipe(ResourceLocation recipeId) {
        return new MIMOFinishedRecipe(recipeId);
    }
    
    private class MIMOFinishedRecipe extends ModFinishedRecipe {
        
        public MIMOFinishedRecipe(ResourceLocation recipeId) {
            super(recipeId, MIMORecipeBuilder.this.recipeSerializer);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray inputs = new JsonArray(MIMORecipeBuilder.this.inputs.size());
            for (RecipeIngredient<ItemStack> input : MIMORecipeBuilder.this.inputs) {
                inputs.add(input.toJson());
            }
            json.add("input", inputs);
            
            JsonArray outputs = new JsonArray(MIMORecipeBuilder.this.outputs.size());
            for (int i = 0; i < MIMORecipeBuilder.this.recipeType.outputTypes.size(); i++) {
                outputs.add(MIMORecipeBuilder.this.recipeType.outputTypes.get(i).toJson(MIMORecipeBuilder.this.outputs.get(i)));
            }
            json.add("output", outputs);
        }
    }
}
