package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import dev.su5ed.gregtechmod.recipe.type.RecipeOutputType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MIMORecipeBuilder extends ModRecipeBuilder<MIMORecipe> {

    public MIMORecipeBuilder(MIMORecipe recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        
        JsonArray inputs = new JsonArray(this.recipe.getInputs().size());
        for (RecipeIngredient<ItemStack> input : this.recipe.getInputs()) {
            inputs.add(input.toJson());
        }
        json.add("input", inputs);

        List<? extends RecipeOutputType<ItemStack>> outputTypes = this.recipe.getType().getOutputTypes();
        List<ItemStack> outputs = this.recipe.getOutputs();
        JsonArray outputsJson = new JsonArray(this.recipe.getOutputs().size());
        for (int i = 0; i < outputTypes.size(); i++) {
            outputsJson.add(outputTypes.get(i).toJson(outputs.get(i)));
        }
        json.add("output", outputsJson);
    }
}
