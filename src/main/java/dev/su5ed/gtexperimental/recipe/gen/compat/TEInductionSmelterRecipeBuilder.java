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

public class TEInductionSmelterRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation TYPE = new ResourceLocation(ModHandler.THERMAL_MODID, "smelter");

    private final Ingredient ingredient;
    private final List<Result> result;
    private final int energy;

    public TEInductionSmelterRecipeBuilder(Ingredient ingredient, List<Result> result, int energy) {
        this.ingredient = ingredient;
        this.result = result;
        this.energy = energy;

        addConditions(RecipeGen.THERMAL_LOADED);
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", TYPE.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        json.add("ingredient", this.ingredient.toJson());
        JsonArray resultJson = new JsonArray();
        for (Result result : this.result) {
            resultJson.add(result.toJson());
        }
        json.add("result", resultJson);
        if (this.energy > 0) {
            json.addProperty("energy", this.energy);
        }
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
    
    public record Result(ItemStack item, double chance) {
        public Result(ItemStack item) {
            this(item, 1);
        }
        
        public JsonObject toJson() {
            JsonObject json = ModRecipeOutputTypes.ITEM.toJson(this.item);
            if (this.chance != 1) {
                json.addProperty("chance", this.chance);
            }
            return json;
        } 
    }
}
