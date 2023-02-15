package dev.su5ed.gtexperimental.recipe.crafting;

import dev.su5ed.gtexperimental.recipe.gen.ConditionRecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ConditionalShapedRecipeBuilder extends ConditionRecipeBuilder<ConditionalShapedRecipeBuilder, ShapedRecipeBuilder> {
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final Map<ICondition, Map<Character, Ingredient>> conditionalKeys = new HashMap<>();

    public ConditionalShapedRecipeBuilder(ItemLike result, int count, BiFunction<ItemLike, Integer, ShapedRecipeBuilder> builderFactory) {
        super(result, count, builderFactory);
    }

    public static ConditionalShapedRecipeBuilder conditionalShaped(ItemLike result) {
        return conditionalShaped(result, 1);
    }

    public static ConditionalShapedRecipeBuilder conditionalShaped(ItemLike result, int count) {
        return new ConditionalShapedRecipeBuilder(result, count, ShapedRecipeBuilder::new);
    }

    public static ConditionalShapedRecipeBuilder conditionalToolShaped(ItemLike result) {
        return conditionalToolShaped(result, 1);
    }

    public static ConditionalShapedRecipeBuilder conditionalToolShaped(ItemLike result, int count) {
        return new ConditionalShapedRecipeBuilder(result, count, WrappedShapedRecipeBuilder::toolShaped);
    }

    public ConditionalShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return define(symbol, Ingredient.of(tag));
    }

    public ConditionalShapedRecipeBuilder define(Character symbol, ItemLike item) {
        return define(symbol, Ingredient.of(item));
    }

    public ConditionalShapedRecipeBuilder define(ICondition condition, Character symbol, Item item) {
        return define(condition, symbol, Ingredient.of(item));
    }

    public ConditionalShapedRecipeBuilder define(ICondition condition, Character symbol, TagKey<Item> tag) {
        return define(condition, symbol, Ingredient.of(tag));
    }

    public ConditionalShapedRecipeBuilder define(ICondition condition, Character symbol, Ingredient ingredient) {
        Map<Character, Ingredient> conditionKeyMap = this.conditionalKeys.computeIfAbsent(condition, c -> new HashMap<>());
        checkSymbol(conditionKeyMap, symbol);
        conditionKeyMap.put(symbol, ingredient);
        return this;
    }

    public ConditionalShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
        checkSymbol(this.key, symbol);
        this.conditionalKeys.values().forEach(map -> checkSymbol(map, symbol));
        this.key.put(symbol, ingredient);
        return this;
    }

    public ConditionalShapedRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        else {
            this.rows.add(pattern);
            return this;
        }
    }

    @Override
    protected Collection<ICondition> getKeys() {
        return this.conditionalKeys.keySet();
    }

    @Override
    protected void buildRecipe(ICondition condition, ShapedRecipeBuilder builder) {
        this.key.forEach(builder::define);
        Map<Character, Ingredient> conditionalKeyMap = this.conditionalKeys.get(condition);
        if (conditionalKeyMap != null) {
            conditionalKeyMap.forEach(builder::define);
        }
        this.rows.forEach(builder::pattern);
    }

    private static void checkSymbol(Map<Character, ?> map, Character symbol) {
        if (map.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        }
        else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
    }
}
