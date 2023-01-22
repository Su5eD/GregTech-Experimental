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

public class FTBICMachineRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation COMPRESSOR = new ResourceLocation(ModHandler.FTBIC_MODID, "compressor");

    private final ResourceLocation type;
    private final Ingredient ingredient;
    private final int count;
    private final ItemStack output;

    public FTBICMachineRecipeBuilder(ResourceLocation type, Ingredient ingredient, int count, ItemStack output) {
        this.type = type;
        this.ingredient = ingredient;
        this.count = count;
        this.output = output;

        addConditions(RecipeGen.FTBIC_LOADED);
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", this.type.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        JsonArray inputItems = new JsonArray();
        JsonObject ingredient = new JsonObject();
        ingredient.add("ingredient", this.ingredient.toJson());
        ingredient.addProperty("count", this.count);
        inputItems.add(ingredient);
        json.add("inputItems", inputItems);

        JsonArray outputItems = new JsonArray();
        outputItems.add(ModRecipeOutputTypes.ITEM.toJson(this.output));
        json.add("outputItems", outputItems);
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
}
