package dev.su5ed.gtexperimental.recipe.gen;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class ConditionRecipeBuilder<T extends ConditionRecipeBuilder<T, B>, B extends RecipeBuilder> implements RecipeBuilder {
    private final ItemLike result;
    private final int count;
    private final Map<String, CriterionTriggerInstance> advancement = new LinkedHashMap<>();
    @Nullable
    private String group;

    private final BiFunction<ItemLike, Integer, B> builderFactory;
    private final List<ICondition> conditions = new ArrayList<>();

    public ConditionRecipeBuilder(ItemLike result, int count, BiFunction<ItemLike, Integer, B> builderFactory) {
        this.result = result;
        this.count = count;
        this.builderFactory = builderFactory;
    }

    public Item getResult() {
        return this.result.asItem();
    }

    public T unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        this.advancement.put(criterionName, criterionTrigger);
        return (T) this;
    }

    public T group(@Nullable String groupName) {
        this.group = groupName;
        return (T) this;
    }

    public T addCondition(ICondition... conditions) {
        this.conditions.addAll(List.of(conditions));
        return (T) this;
    }

    protected abstract Collection<ICondition> getKeys();

    protected abstract void buildRecipe(ICondition condition, B builder);

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        ConditionalRecipe.Builder conditionalBuilder = ConditionalRecipe.builder();
        StreamEx.of(getKeys())
            .distinct()
            .ifEmpty((ICondition) null)
            .forEach(condition -> {
                B builder = this.builderFactory.apply(this.result, this.count);
                buildRecipe(condition, builder);
                this.advancement.forEach(builder::unlockedBy);
                builder.group(this.group);
                if (condition != null) {
                    conditionalBuilder.addCondition(condition);
                }
                this.conditions.forEach(conditionalBuilder::addCondition);
                conditionalBuilder.addRecipe(cons -> builder.save(cons, recipeId));
            });
        conditionalBuilder.build(finishedRecipeConsumer, recipeId);
    }
}
