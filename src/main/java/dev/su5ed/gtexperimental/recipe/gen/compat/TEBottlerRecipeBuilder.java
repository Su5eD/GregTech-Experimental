package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.gen.BaseRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;

public class TEBottlerRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation TYPE = new ResourceLocation(ModHandler.THERMAL_MODID, "bottler");

    private final Ingredient ingredient;
    private final TagKey<Fluid> fluid;
    private final int fluidAmount;
    private final ItemStack result;
    private final int energy;

    public TEBottlerRecipeBuilder(Ingredient ingredient, TagKey<Fluid> fluid, int fluidAmount, ItemStack result, int energy) {
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
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
        
        JsonArray ingredients = new JsonArray();
        ingredients.add(this.ingredient.toJson());
        JsonObject fluidIngredient = new JsonObject();
        fluidIngredient.addProperty("fluid_tag", this.fluid.location().toString());
        fluidIngredient.addProperty("amount", this.fluidAmount);
        ingredients.add(fluidIngredient);
        JsonArray results = new JsonArray();
        results.add(ModRecipeOutputTypes.ITEM.toJson(this.result));
        json.add("result", results);
        if (this.energy > 0) {
            json.addProperty("energy", this.energy);
        }
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
}
