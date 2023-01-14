package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import net.minecraft.world.item.ItemStack;

public class MISORecipeBuilder extends ModRecipeBuilder<MISORecipe> {

    public MISORecipeBuilder(MISORecipe recipe) {
        super(recipe);
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        JsonArray inputs = new JsonArray(this.recipe.getInputs().size());
        for (RecipeIngredient<ItemStack> input : this.recipe.getInputs()) {
            inputs.add(input.toJson());
        }
        json.add("input", inputs);
        json.add("output", this.recipe.getType().outputType.toJson(this.recipe.getOutput()));
        json.addProperty("duration", this.recipe.getDuration());
        json.addProperty("energyCost", this.recipe.getEnergyCost());
    }
}
