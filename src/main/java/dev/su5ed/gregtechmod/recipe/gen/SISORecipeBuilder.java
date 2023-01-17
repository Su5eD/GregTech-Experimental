package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.SISORecipe;

public class SISORecipeBuilder extends ModRecipeBuilder<SISORecipe> {

    public SISORecipeBuilder(SISORecipe recipe) {
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
