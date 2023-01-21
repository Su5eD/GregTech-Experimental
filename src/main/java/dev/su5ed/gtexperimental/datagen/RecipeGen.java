package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.datagen.recipe.AlloySmelterRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.AssemblerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.BenderRecipeProvider;
import dev.su5ed.gtexperimental.datagen.recipe.CanningMachineRecipeProvider;
import dev.su5ed.gtexperimental.datagen.recipe.ModRecipeProvider;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        List<ModRecipeProvider> providers = List.of(AlloySmelterRecipesGen.INSTANCE, AssemblerRecipesGen.INSTANCE, BenderRecipeProvider.INSTANCE, CanningMachineRecipeProvider.INSTANCE);
        
        providers.forEach(provider -> provider.buildCraftingRecipes(finishedRecipeConsumer));
    }

    public static InventoryChangeTrigger.TriggerInstance hasIngredient(Ingredient ingredient) {
        ItemPredicate[] predicates = StreamEx.of(ingredient.values)
            .map(value -> {
                if (value instanceof Ingredient.TagValue tagValue) {
                    return ItemPredicate.Builder.item().of(tagValue.tag).build();
                }
                Set<Item> items = StreamEx.of(value.getItems())
                    .map(ItemStack::getItem)
                    .toSet();
                return new ItemPredicate(null, items, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY);
            })
            .toArray(ItemPredicate[]::new);
        return inventoryTrigger(predicates);
    }

    @Override
    public String getName() {
        return Reference.NAME + super.getName();
    }
}
