package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;

public class SISORecipeBuilder<T> extends ModRecipeBuilder<SISORecipe<T>> {

    public SISORecipeBuilder(SISORecipe<T> recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        
        json.add("input", this.recipe.getInput().toJson());
        json.add("output", this.recipe.getType().outputType.toJson(this.recipe.getOutput()));
        json.addProperty("duration", this.recipe.getDuration());
        json.addProperty("energyCost", this.recipe.getEnergyCost());
    }
}
