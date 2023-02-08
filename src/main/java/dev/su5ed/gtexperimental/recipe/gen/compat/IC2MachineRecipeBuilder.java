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

public class IC2MachineRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation COMPRESSOR = new ResourceLocation(ModHandler.IC2_MODID, "compressor");
    public static final ResourceLocation EXTRACTOR = new ResourceLocation(ModHandler.IC2_MODID, "extractor");
    public static final ResourceLocation MACERATOR = new ResourceLocation(ModHandler.IC2_MODID, "macerator");

    private final ResourceLocation type;
    private final Ingredient ingredient;
    private final int count;
    private final ItemStack result;

    public IC2MachineRecipeBuilder(ResourceLocation type, Ingredient ingredient, int count, ItemStack result) {
        this.type = type;
        this.ingredient = ingredient;
        this.count = count;
        this.result = result;

        addConditions(RecipeGen.IC2_LOADED);
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", this.type.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        JsonObject ingredient;
        if (this.ingredient.values.length == 1) {
            Ingredient.Value value = this.ingredient.values[0];
            if (value instanceof Ingredient.ItemValue || value instanceof Ingredient.TagValue) {
                ingredient = value.serialize();
            }
            else {
                throw new IllegalArgumentException("Invalid ingredient value " + value);
            }
        }
        else {
            ingredient = new JsonObject();
            JsonArray values = new JsonArray();
            for (Ingredient.Value value : this.ingredient.values) {
                if (value instanceof Ingredient.ItemValue || value instanceof Ingredient.TagValue) {
                    values.add(value.serialize());
                }
                else {
                    throw new IllegalArgumentException("Invalid ingredient value " + value);
                }
            }
            ingredient.add("any", values);
        }
        if (this.count > 1) {
            ingredient.addProperty("count", this.count);
        }
        json.add("ingredient", ingredient);
        json.add("result", ModRecipeOutputTypes.ITEM.toJson(this.result));
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
}
