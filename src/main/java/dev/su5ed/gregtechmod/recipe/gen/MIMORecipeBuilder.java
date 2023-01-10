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

public class MIMORecipeBuilder<O> extends ModRecipeBuilder {
    protected final MIMORecipeType<?, O> recipeType;
    protected final RecipeSerializer<?> recipeSerializer;
    protected final List<? extends RecipeIngredient<ItemStack>> inputs;
    protected final List<O> outputs;

    public MIMORecipeBuilder(MIMORecipeType<?, O> recipeType, RecipeSerializer<?> recipeSerializer, List<? extends RecipeIngredient<ItemStack>> inputs, List<O> outputs) {
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
            List<? extends RecipeOutputType<O>> outputTypes = MIMORecipeBuilder.this.recipeType.getOutputTypes();
            for (int i = 0; i < outputTypes.size(); i++) {
                outputs.add(outputTypes.get(i).toJson(MIMORecipeBuilder.this.outputs.get(i)));
            }
            json.add("output", outputs);
        }
    }
}
