package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;

public class MIMORecipeBuilder extends ModRecipeBuilder<MIMORecipe> {

    public MIMORecipeBuilder(MIMORecipe recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("input", ModRecipeIngredientTypes.toJson(this.recipe.getInputs()));
        json.add("output", ModRecipeOutputTypes.toJson(this.recipe.getType().getOutputType(), this.recipe.getType().getOutputCount(), this.recipe.getOutput()));
        json.addProperty("duration", this.recipe.getDuration());
        json.addProperty("energyCost", this.recipe.getEnergyCost());
    }
}
