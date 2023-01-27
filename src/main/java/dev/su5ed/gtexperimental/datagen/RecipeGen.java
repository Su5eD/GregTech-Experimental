package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.recipe.AlloySmelterRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.AssemblerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.BenderRecipeProvider;
import dev.su5ed.gtexperimental.datagen.recipe.BlastFurnaceRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.CanningMachineRecipeProvider;
import dev.su5ed.gtexperimental.datagen.recipe.ChemicalRecipeProvider;
import dev.su5ed.gtexperimental.datagen.recipe.CompressorRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.DistillationRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.ExtractorRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.FusionRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.ImplosionRecipesGen;
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
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider {
    public static final ICondition IC2_LOADED = new ModLoadedCondition(ModHandler.IC2_MODID);
    public static final ICondition NOT_IC2_LOADED = new NotCondition(IC2_LOADED);
    public static final ICondition FTBIC_LOADED = new ModLoadedCondition(ModHandler.FTBIC_MODID);
    public static final ICondition TWILIGHT_FOREST_LOADED = new ModLoadedCondition(ModHandler.TWILIGHT_FOREST_MODID);
    public static final ICondition THERMAL_LOADED = new ModLoadedCondition(ModHandler.THERMAL_MODID);

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        List<ModRecipeProvider> providers = List.of(
            AlloySmelterRecipesGen.INSTANCE,
            AssemblerRecipesGen.INSTANCE,
            BenderRecipeProvider.INSTANCE,
            CanningMachineRecipeProvider.INSTANCE,
            ChemicalRecipeProvider.INSTANCE,
            CompressorRecipesGen.INSTANCE,
            DistillationRecipesGen.INSTANCE,
            ExtractorRecipesGen.INSTANCE,
            FusionRecipesGen.INSTANCE,
            ImplosionRecipesGen.INSTANCE,
            BlastFurnaceRecipesGen.INSTANCE
        );

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
        return Reference.NAME + " " + super.getName();
    }
}
