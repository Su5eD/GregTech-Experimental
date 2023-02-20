package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.recipe.AlloySmelterRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.AssemblerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.BenderRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.BlastFurnaceRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.CanningMachineRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.ChemicalRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.CompressorRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.CraftingRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.DistillationRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.ExtractorRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.FusionRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.ImplosionRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.IndustrialCentrifugeRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.IndustrialElectrolyzerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.IndustrialGrinderRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.IndustrialSawmillRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.LatheRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.ModRecipeProvider;
import dev.su5ed.gtexperimental.datagen.recipe.PrinterRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.PulverizerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.SmeltingRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.VacuumFreezerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.WiremillRecipesGen;
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
    public static final ICondition MYRTREES_LOADED = new ModLoadedCondition(ModHandler.MYRTREES_MODID);
    public static final ICondition TWILIGHT_FOREST_LOADED = new ModLoadedCondition(ModHandler.TWILIGHT_FOREST_MODID);
    public static final ICondition THERMAL_LOADED = new ModLoadedCondition(ModHandler.THERMAL_MODID);
    public static final ICondition RAILCRAFT_LOADED = new ModLoadedCondition(ModHandler.RAILCRAFT_MODID);
    public static final ICondition NOT_RAILCRAFT_LOADED = new NotCondition(RAILCRAFT_LOADED);

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        List<ModRecipeProvider> providers = List.of(
            AlloySmelterRecipesGen.INSTANCE,
            AssemblerRecipesGen.INSTANCE,
            BenderRecipesGen.INSTANCE,
            CanningMachineRecipesGen.INSTANCE,
            ChemicalRecipesGen.INSTANCE,
            CompressorRecipesGen.INSTANCE,
            DistillationRecipesGen.INSTANCE,
            ExtractorRecipesGen.INSTANCE,
            FusionRecipesGen.INSTANCE,
            ImplosionRecipesGen.INSTANCE,
            BlastFurnaceRecipesGen.INSTANCE,
            IndustrialCentrifugeRecipesGen.INSTANCE,
            IndustrialElectrolyzerRecipesGen.INSTANCE,
            IndustrialGrinderRecipesGen.INSTANCE,
            LatheRecipesGen.INSTANCE,
            PrinterRecipesGen.INSTANCE,
            PulverizerRecipesGen.INSTANCE,
            IndustrialSawmillRecipesGen.INSTANCE,
            VacuumFreezerRecipesGen.INSTANCE,
            WiremillRecipesGen.INSTANCE,
            CraftingRecipesGen.INSTANCE,
            SmeltingRecipesGen.INSTANCE
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
