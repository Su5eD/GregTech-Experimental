package dev.su5ed.gtexperimental.recipe.crafting;

import dev.su5ed.gtexperimental.recipe.gen.ConditionRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ConditionalShapelessRecipeBuilder extends ConditionRecipeBuilder<ConditionalShapelessRecipeBuilder, ShapelessRecipeBuilder> {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final Map<ICondition, List<Ingredient>> conditionalIngredients = new HashMap<>();

    public ConditionalShapelessRecipeBuilder(ItemLike result, int count, BiFunction<ItemLike, Integer, ShapelessRecipeBuilder> builderFactory) {
        super(result, count, builderFactory);
    }

    public static ConditionalShapelessRecipeBuilder conditionalShapeless(ItemLike result) {
        return conditionalShapeless(result, 1);
    }

    public static ConditionalShapelessRecipeBuilder conditionalShapeless(ItemLike result, int count) {
        return new ConditionalShapelessRecipeBuilder(result, count, ShapelessRecipeBuilder::new);
    }

    public ConditionalShapelessRecipeBuilder requires(TagKey<Item> tag) {
        return requires(Ingredient.of(tag));
    }

    public ConditionalShapelessRecipeBuilder requires(TagKey<Item> tag, int quantity) {
        return requires(Ingredient.of(tag), quantity);
    }

    public ConditionalShapelessRecipeBuilder requires(ItemLike item) {
        return requires(item, 1);
    }

    public ConditionalShapelessRecipeBuilder requires(ItemLike item, int quantity) {
        return requires(null, item, quantity);
    }

    public ConditionalShapelessRecipeBuilder requires(Ingredient ingredient) {
        return requires(null, ingredient);
    }

    public ConditionalShapelessRecipeBuilder requires(Ingredient ingredient, int quantity) {
        return requires(null, ingredient, quantity);
    }

    public ConditionalShapelessRecipeBuilder requires(ICondition condition, TagKey<Item> tag) {
        return requires(condition, Ingredient.of(tag));
    }

    public ConditionalShapelessRecipeBuilder requires(ICondition condition, TagKey<Item> tag, int quantity) {
        return requires(condition, Ingredient.of(tag), quantity);
    }

    public ConditionalShapelessRecipeBuilder requires(ICondition condition, ItemLike item) {
        return requires(condition, item, 1);
    }

    public ConditionalShapelessRecipeBuilder requires(ICondition condition, ItemLike item, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            requires(condition, Ingredient.of(item));
        }
        return this;
    }

    public ConditionalShapelessRecipeBuilder requires(ICondition condition, Ingredient ingredient) {
        return requires(condition, ingredient, 1);
    }

    public ConditionalShapelessRecipeBuilder requires(@Nullable ICondition condition, Ingredient ingredient, int quantity) {
        List<Ingredient> conditionalIngredientList = condition != null ? this.conditionalIngredients.computeIfAbsent(condition, c -> new ArrayList<>()) : this.ingredients;
        for (int i = 0; i < quantity; ++i) {
            conditionalIngredientList.add(ingredient);
        }
        return this;
    }

    @Override
    protected Collection<ICondition> getKeys() {
        return this.conditionalIngredients.keySet();
    }

    @Override
    protected void buildRecipe(ICondition condition, ShapelessRecipeBuilder builder) {
        this.ingredients.forEach(builder::requires);
        List<Ingredient> conditionalIngredientList = this.conditionalIngredients.get(condition);
        if (conditionalIngredientList != null) {
            conditionalIngredientList.forEach(builder::requires);
        }
    }
}
