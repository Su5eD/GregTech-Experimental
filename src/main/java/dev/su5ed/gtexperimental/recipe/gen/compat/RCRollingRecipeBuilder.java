package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.recipe.gen.BaseRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RCRollingRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation TYPE = new ResourceLocation(ModHandler.RAILCRAFT_MODID, "rolling");

    private final ItemStack result;
    private final int tickCost;
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    
    public RCRollingRecipeBuilder(ItemStack result) {
        this(result, 100);
    }

    public RCRollingRecipeBuilder(ItemStack result, int tickCost) {
        this.result = result;
        this.tickCost = tickCost;
    }

    public RCRollingRecipeBuilder define(Character key, TagKey<Item> itemTagValue) {
        return define(key, Ingredient.of(itemTagValue));
    }

    public RCRollingRecipeBuilder define(Character key, ItemLike itemValue) {
        return define(key, Ingredient.of(itemValue));
    }

    public RCRollingRecipeBuilder define(Character key, Ingredient itemValue) {
        if (this.key.containsKey(key)) {
            throw new IllegalArgumentException("Symbol '" + key + "' is already defined!");
        }
        else if (key == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.key.put(key, itemValue);
        return this;
    }

    public RCRollingRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.rows.add(pattern);
        return this;
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", TYPE.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        JsonObject keyJson = new JsonObject();
        this.key.forEach((ch, ingredient) -> keyJson.add(String.valueOf(ch), ingredient.toJson()));
        json.add("key", keyJson);
        JsonArray pattern = new JsonArray();
        this.rows.forEach(pattern::add);
        json.add("pattern", pattern);
        json.add("result", ModRecipeOutputTypes.ITEM.toJson(this.result));
        json.addProperty("tickCost", this.tickCost);
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }
}
