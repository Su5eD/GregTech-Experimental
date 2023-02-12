package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.recipe.type.ToolShapedRecipeBuilder;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ConditionalShapedRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final int count;
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final Map<String, CriterionTriggerInstance> advancement = new LinkedHashMap<>();
    @Nullable
    private String group;

    private final BiFunction<ItemLike, Integer, ShapedRecipeBuilder> builderFactory;
    private final List<ICondition> conditions = new ArrayList<>();
    private final Map<ICondition, Map<Character, Ingredient>> conditionalKeys = new HashMap<>();
    private final Map<ICondition, Map<String, CriterionTriggerInstance>> conditionalAdvancement = new LinkedHashMap<>();

    public ConditionalShapedRecipeBuilder(ItemLike result, int count, BiFunction<ItemLike, Integer, ShapedRecipeBuilder> builderFactory) {
        this.result = result.asItem();
        this.count = count;
        this.builderFactory = builderFactory;
    }

    public static ConditionalShapedRecipeBuilder shaped(ItemLike result) {
        return shaped(result, 1);
    }

    public static ConditionalShapedRecipeBuilder shaped(ItemLike result, int count) {
        return new ConditionalShapedRecipeBuilder(result, count, ShapedRecipeBuilder::new);
    }
    
    public static ConditionalShapedRecipeBuilder toolShaped(ItemLike result) {
        return toolShaped(result, 1);
    }
    
    public static ConditionalShapedRecipeBuilder toolShaped(ItemLike result, int count) {
        return new ConditionalShapedRecipeBuilder(result, count, ToolShapedRecipeBuilder::new);
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

    public ConditionalShapedRecipeBuilder addCondition(ICondition... conditions) {
        this.conditions.addAll(List.of(conditions));
        return this;
    }

    public ConditionalShapedRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        this.advancement.put(criterionName, criterionTrigger);
        return this;
    }

    public ConditionalShapedRecipeBuilder unlockedBy(ICondition condition, String criterionName, CriterionTriggerInstance criterionTrigger) {
        Map<String, CriterionTriggerInstance> conditionKeyMap = this.conditionalAdvancement.computeIfAbsent(condition, c -> new HashMap<>());
        if (conditionKeyMap.containsKey(criterionName)) {
            throw new IllegalArgumentException("Criterion '" + criterionName + "' is already defined!");
        }
        conditionKeyMap.put(criterionName, criterionTrigger);
        return this;
    }

    public ConditionalShapedRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        ConditionalRecipe.Builder conditionalBuilder = ConditionalRecipe.builder();
        StreamEx.of(this.conditionalKeys.keySet())
            .append(this.conditionalAdvancement.keySet())
            .distinct()
            .ifEmpty((ICondition) null)
            .forEach(condition -> {
                ShapedRecipeBuilder builder = this.builderFactory.apply(this.result, this.count);
                this.key.forEach(builder::define);
                Map<Character, Ingredient> conditionalKeyMap = this.conditionalKeys.get(condition);
                if (conditionalKeyMap != null) {
                    conditionalKeyMap.forEach(builder::define);
                }
                this.rows.forEach(builder::pattern);
                this.advancement.forEach(builder::unlockedBy);
                Map<String, CriterionTriggerInstance> conditionalAdvancementMap = this.conditionalAdvancement.get(condition);
                if (conditionalAdvancementMap != null) {
                    conditionalAdvancementMap.forEach(builder::unlockedBy);
                }
                builder.group(this.group);
                if (condition != null) {
                    conditionalBuilder.addCondition(condition);
                }
                this.conditions.forEach(conditionalBuilder::addCondition);
                conditionalBuilder.addRecipe(cons -> builder.save(cons, recipeId));
            });
        conditionalBuilder.build(finishedRecipeConsumer, recipeId);
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
