package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;

public class IFMORecipeBuilder extends ModRecipeBuilder<IFMORecipe> {

    public IFMORecipeBuilder(IFMORecipe recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("input", this.recipe.getInput().toJson());
        json.add("fluid", this.recipe.getFluid().toJson());
        json.add("output", this.recipe.getType().getOutputType().toJson(this.recipe.getOutput()));
        this.recipe.getProperties().toJson(json);
    }
}
