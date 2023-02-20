package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.gen.BaseRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class RCCrusherRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation TYPE = new ResourceLocation(ModHandler.RAILCRAFT_MODID, "crusher");

    private final Ingredient ingredient;
    private final List<Output> outputs;
    private final int tickCost;

    public RCCrusherRecipeBuilder(Ingredient ingredient, List<Output> outputs, int tickCost) {
        this.ingredient = ingredient;
        this.outputs = outputs;
        this.tickCost = tickCost;

        addConditions(RecipeGen.RAILCRAFT_LOADED);
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", TYPE.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        json.add("ingredient", this.ingredient.toJson());
        JsonArray output = new JsonArray();
        for (Output out : this.outputs) {
            output.add(out.toJson());
        }
        json.add("output", output);
        json.addProperty("tickCost", this.tickCost);
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
    
    public record Output(ItemStack stack, double probability) {
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.add("result", ModRecipeOutputTypes.ITEM.toJson(this.stack));
            json.addProperty("probability", this.probability);
            return json;
        }
    }
}
