package dev.su5ed.gtexperimental.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.world.item.ItemStack;

public class MISORecipeBuilder<T> extends ModRecipeBuilder<MISORecipe<T>> {

    public MISORecipeBuilder(MISORecipe<T> recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        
        JsonArray inputs = new JsonArray(this.recipe.getInputs().size());
        for (RecipeIngredient<T> input : this.recipe.getInputs()) {
            inputs.add(input.toJson());
        }
        json.add("input", inputs);
        json.add("output", this.recipe.getType().outputType.toJson(this.recipe.getOutput()));
        json.addProperty("duration", this.recipe.getDuration());
        json.addProperty("energyCost", this.recipe.getEnergyCost());
    }
}
