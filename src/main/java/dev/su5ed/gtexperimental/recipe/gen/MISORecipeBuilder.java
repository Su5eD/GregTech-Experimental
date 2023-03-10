package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.MISORecipe;

public class MISORecipeBuilder<IN, OUT> extends ModRecipeBuilder<MISORecipe<IN, OUT>> {

    public MISORecipeBuilder(MISORecipe<IN, OUT> recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("input", this.recipe.getInput().toJson());
        json.add("output", this.recipe.getType().getOutputType().toJson(this.recipe.getOutput()));
        this.recipe.getProperties().toJson(json);
    }
}
