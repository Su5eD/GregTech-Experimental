package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.List;

public class InductionSmelterRecipeBuilder extends BaseRecipeBuilder {
    private static final ResourceLocation TYPE = new ResourceLocation(ModHandler.THERMAL_MODID, "smelter");
    private static final ICondition CONDITION = new ModLoadedCondition(ModHandler.THERMAL_MODID);

    private final Ingredient ingredient;
    private final List<ItemStack> result;
    private final int energy;

    public InductionSmelterRecipeBuilder(Ingredient ingredient, List<ItemStack> result, int energy) {
        this.ingredient = ingredient;
        this.result = result;
        this.energy = energy;

        addConditions(CONDITION);
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
        for (ItemStack stack : this.result) {
            resultJson.add(ModRecipeOutputTypes.ITEM.toJson(stack));
        }
        json.add("result", resultJson);
        if (this.energy > 0) {
            json.addProperty("energy", this.energy);
        }
    }

    @Override
    public RecipeSerializer<?> getType() {
        return null;
    }
}
