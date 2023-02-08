package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.gen.BaseRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RCBlastFurnaceRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation TYPE = new ResourceLocation(ModHandler.RAILCRAFT_MODID, "blast_furnace");

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;
    private final int experience;
    private final int slagOutput;

    public RCBlastFurnaceRecipeBuilder(Ingredient ingredient, ItemStack result, int cookingTime, int experience, int slagOutput) {
        this.ingredient = ingredient;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.slagOutput = slagOutput;

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
        json.add("result", ModRecipeOutputTypes.ITEM.toJson(this.result));
        json.addProperty("cookingTime", this.cookingTime);
        if (this.experience > 0) {
            json.addProperty("experience", this.experience);
        }
        json.addProperty("slagOutput", this.slagOutput);
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
}
