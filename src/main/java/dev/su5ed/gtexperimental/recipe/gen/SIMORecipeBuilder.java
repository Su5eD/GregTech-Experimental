package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;

public class SIMORecipeBuilder<T> extends ModRecipeBuilder<SIMORecipe<T>> {

    public SIMORecipeBuilder(SIMORecipe<T> recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("input", this.recipe.getInput().toJson());
        json.add("output", ModRecipeOutputTypes.toJson(this.recipe.getType().getOutputTypes(), this.recipe.getOutput()));
        json.addProperty("duration", this.recipe.getDuration());
        json.addProperty("energyCost", this.recipe.getEnergyCost());
    }
}
