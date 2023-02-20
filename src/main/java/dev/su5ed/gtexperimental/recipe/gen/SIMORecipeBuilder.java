package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;

public class SIMORecipeBuilder<IN, OUT> extends ModRecipeBuilder<SIMORecipe<IN, OUT>> {

    public SIMORecipeBuilder(SIMORecipe<IN, OUT> recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("input", this.recipe.getInput().toJson());
        json.add("output", ModRecipeOutputTypes.toJson(this.recipe.getType().getOutputType(), this.recipe.getType().getOutputCount(), this.recipe.getOutput()));
        this.recipe.getProperties().toJson(json);
    }
}
