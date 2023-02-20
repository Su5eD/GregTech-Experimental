package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class TEMachineRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation PULVERIZER = new ResourceLocation(ModHandler.THERMAL_MODID, "pulverizer");
    public static final ResourceLocation SAWMILL = new ResourceLocation(ModHandler.THERMAL_MODID, "sawmill");

    private final ResourceLocation type;
    private final Ingredient ingredient;
    private final int inputCount;
    private final List<ItemStack> result;
    private final int secondaryChance;
    private final int energy;

    public TEMachineRecipeBuilder(ResourceLocation type, Ingredient ingredient, int inputCount, List<ItemStack> result, int secondaryChance, int energy) {
        this.type = type;
        this.ingredient = ingredient;
        this.result = result;
        this.inputCount = inputCount;
        this.secondaryChance = secondaryChance;
        this.energy = energy;

        addConditions(RecipeGen.THERMAL_LOADED);
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", this.type.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        JsonElement input;
        if (this.inputCount == 1) {
            input = this.ingredient.toJson();
        }
        else {
            JsonObject inputJson = new JsonObject();
            inputJson.add("value", this.ingredient.toJson());
            inputJson.addProperty("count", this.inputCount);
            input = inputJson;
        }
        json.add("ingredient", input);

        JsonArray resultJson = new JsonArray();
        for (ItemStack stack : this.result) {
            resultJson.add(ModRecipeOutputTypes.ITEM.toJson(stack));
        }
        if (this.secondaryChance > 0) {
            ((JsonObject) resultJson.get(resultJson.size() - 1)).addProperty("chance", this.secondaryChance / 100.0);
        }
        json.add("result", resultJson);
        if (this.energy != 0) {
            json.addProperty("energy", this.energy);
        }
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
}
