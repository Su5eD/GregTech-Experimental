package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;

public class MISORecipeBuilder<T> extends ModRecipeBuilder<MISORecipe<T>> {

    public MISORecipeBuilder(MISORecipe<T> recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("input", ModRecipeIngredientTypes.toJson(this.recipe.getInputs()));
        json.add("output", this.recipe.getType().outputType.toJson(this.recipe.getOutput()));
        json.addProperty("duration", this.recipe.getDuration());
        json.addProperty("energyCost", this.recipe.getEnergyCost());
    }
}
