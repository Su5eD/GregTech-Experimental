package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public final class CraftingRecipesGen implements ModRecipeProvider {
    public static final CraftingRecipesGen INSTANCE = new CraftingRecipesGen();

    private CraftingRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        Consumer<FinishedRecipe> classicConsumer = finishedRecipe -> ConditionalRecipe.builder()
            .addCondition(SelectedProfileCondition.CLASSIC)
            .addRecipe(finishedRecipe)
            .build(finishedRecipeConsumer, finishedRecipe.getId());
        Consumer<FinishedRecipe> experimentalConsumer = finishedRecipe -> ConditionalRecipe.builder()
            .addCondition(SelectedProfileCondition.EXPERIMENTAL)
            .addRecipe(finishedRecipe)
            .build(finishedRecipeConsumer, finishedRecipe.getId());
        Consumer<FinishedRecipe> refinedIronConsumer = finishedRecipe -> ConditionalRecipe.builder()
            .addCondition(SelectedProfileCondition.REFINED_IRON)
            .addRecipe(finishedRecipe)
            .build(finishedRecipeConsumer, finishedRecipe.getId());
        Consumer<FinishedRecipe> regularIronConsumer = finishedRecipe -> ConditionalRecipe.builder()
            .addCondition(SelectedProfileCondition.REGULAR_IRON)
            .addRecipe(finishedRecipe)
            .build(finishedRecipeConsumer, finishedRecipe.getId());

        // Armor
        shaped(Armor.CLOAKING_DEVICE)
            .define('L', GregTechTags.CRAFTING_10KK_EU_STORE)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('C', Plate.CHROME.getTag())
            .pattern("CIC")
            .pattern("ILI")
            .pattern("CIC")
            .unlockedBy("has_10kk_eu_store", has(GregTechTags.CRAFTING_10KK_EU_STORE))
            .save(classicConsumer, id("armor/classic/cloaking_device"));
        shaped(Armor.CLOAKING_DEVICE)
            .define('L', GregTechTags.CRAFTING_100KK_EU_STORE)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('C', Plate.CHROME.getTag())
            .pattern("CIC")
            .pattern("ILI")
            .pattern("CIC")
            .unlockedBy("has_100kk_eu_store", has(GregTechTags.CRAFTING_100KK_EU_STORE))
            .save(experimentalConsumer, id("armor/experimental/cloaking_device"));
        shaped(Armor.LAPOTRONPACK)
            .define('L', GregTechTags.CRAFTING_10KK_EU_STORE)
            .define('C', GregTechTags.CIRCUIT_TIER_7)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('P', GregTechTags.LAPPACK)
            .define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR)
            .pattern("CLC")
            .pattern("SPS")
            .pattern("CIC")
            .unlockedBy("has_10kk_eu_store", has(GregTechTags.CRAFTING_10KK_EU_STORE))
            .save(classicConsumer, id("armor/classic/lapotronpack"));
        shaped(Armor.LAPOTRONPACK)
            .define('L', GregTechTags.CRAFTING_100KK_EU_STORE)
            .define('C', GregTechTags.CIRCUIT_TIER_7)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('P', GregTechTags.LAPPACK)
            .define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR)
            .pattern("CLC")
            .pattern("SPS")
            .pattern("CIC")
            .unlockedBy("has_10kk_eu_store", has(GregTechTags.CRAFTING_100KK_EU_STORE))
            .save(experimentalConsumer, id("armor/experimental/lapotronpack"));
        shapeless(Armor.LIGHT_HELMET)
            .requires(GregTechTags.SOLAR_HELMET)
            .requires(GregTechTags.ILLUMINATOR_FLAT)
            .unlockedBy("has_solar_helmet", has(GregTechTags.SOLAR_HELMET))
            .save(finishedRecipeConsumer, id("armor/solar_helmet"));
        shaped(Armor.LIGHT_HELMET)
            .define('L', GregTechTags.CRAFTING_LI_BATTERY)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .define('A', Plate.ALUMINIUM.getTag())
            .pattern("LCL")
            .pattern("LAL")
            .pattern("L L")
            .unlockedBy("has_crafting_li_battery", has(GregTechTags.CRAFTING_LI_BATTERY))
            .save(finishedRecipeConsumer, id("armor/light_helmet"));

        // Blocks
        shaped(ModBlock.STANDARD_MACHINE_CASING, 4)
            .define('C', GregTechTags.CIRCUIT)
            .define('A', tagsIngredient(Plate.REFINED_IRON.getTag(), Plate.ALUMINIUM.getTag()))
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_1)
            .pattern("AAA")
            .pattern("CMC")
            .pattern("AAA")
            .unlockedBy("has_crafting_raw_machine_tier_1", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1))
            .save(refinedIronConsumer, id("blocks/refined_iron/standard_machine_casing"));
        shaped(ModBlock.STANDARD_MACHINE_CASING, 4)
            .define('C', GregTechTags.CIRCUIT)
            .define('A', tagsIngredient(Plate.IRON.getTag(), Plate.ALUMINIUM.getTag()))
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_1)
            .pattern("AAA")
            .pattern("CMC")
            .pattern("AAA")
            .unlockedBy("has_crafting_raw_machine_tier_1", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1))
            .save(regularIronConsumer, id("blocks/regular_iron/standard_machine_casing"));
        shaped(Blocks.PISTON)
            .define('W', ItemTags.PLANKS)
            .define('C', Tags.Items.COBBLESTONE)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('B', tagsIngredient(Tags.Items.INGOTS_IRON, GregTechTags.ingot("bronze"), Ingot.ALUMINIUM.getTag(), Ingot.STEEL.getTag(), Ingot.TITANIUM.getTag(), GregTechTags.ingot("refined_iron")))
            .pattern("WWW")
            .pattern("CBC")
            .pattern("CRC")
            .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
            .save(refinedIronConsumer, id("blocks/refined_iron/piston"));
        shaped(Blocks.PISTON)
            .define('W', ItemTags.PLANKS)
            .define('C', Tags.Items.COBBLESTONE)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('B', tagsIngredient(Tags.Items.INGOTS_IRON, GregTechTags.ingot("bronze"), Ingot.ALUMINIUM.getTag(), Ingot.STEEL.getTag(), Ingot.TITANIUM.getTag()))
            .pattern("WWW")
            .pattern("CBC")
            .pattern("CRC")
            .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
            .save(regularIronConsumer, id("blocks/regular_iron/piston"));
        shaped(ModBlock.ADVANCED_MACHINE_CASING, 4)
            .define('C', GregTechTags.CIRCUIT_BOARD_TIER_6)
            .define('T', Plate.CHROME.getTag())
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)
            .pattern("TTT")
            .pattern("CMC")
            .pattern("TTT")
            .unlockedBy("has_crafting_raw_machine_tier_4", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_4))
            .save(finishedRecipeConsumer, id("blocks/advanced_machine_casing"));
        shaped(ModBlock.FUSION_COIL)
            .define('I', GregTechTags.IRIDIUM_NEUTRON_REFLECTOR)
            .define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR)
            .define('E', GregTechTags.CIRCUIT_TIER_7)
            .define('C', GregTechTags.CRAFTING_HEATING_COIL_TIER_2)
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)
            .pattern("ESE")
            .pattern("CMC")
            .pattern("EIE")
            .unlockedBy("has_crafting_raw_machine_tier_4", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_4))
            .save(finishedRecipeConsumer, id("blocks/fusion_coil"));
        shaped(ModBlock.HIGHLY_ADVANCED_MACHINE)
            .define('C', Plate.CHROME)
            .define('T', Plate.TITANIUM)
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)
            .pattern("CTC")
            .pattern("TMT")
            .pattern("CTC")
            .unlockedBy("has_crafting_raw_machine_tier_2", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2))
            .save(finishedRecipeConsumer, id("blocks/highly_advanced_machine"));
        shaped(ModBlock.LESUBLOCK)
            .define('M', GregTechTags.CIRCUIT)
            .define('L', GregTechTags.LAZURITE_CHUNK)
            .pattern("LLL")
            .pattern("LML")
            .pattern("LLL")
            .unlockedBy("has_crafting_raw_machine_tier_2", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2))
            .save(finishedRecipeConsumer, id("blocks/lasublock"));
        shaped(ModBlock.REINFORCED_MACHINE_CASING, 4)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .define('S', Plate.STEEL.getTag())
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)
            .pattern("SSS")
            .pattern("CMC")
            .pattern("SSS")
            .unlockedBy("has_crafting_raw_machine_tier_2", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2))
            .save(finishedRecipeConsumer, id("blocks/reinforced_machine_casing"));
        // TODO config condition, heavy_weighted_pressure_plate

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
//        shaped(Ic2Items.LAPPACK)
//            .define('C', GregTechTags.CIRCUIT_TIER_4)
//            .define('B', GregTechTags.BATPACK)
//            .define('L', GregTechTags.LAZURITE_CHUNK)
//            .pattern("LCL")
//            .pattern("LBL")
//            .pattern("L L")
//            .unlockedBy("has_circuit_tier_4", has(GregTechTags.CIRCUIT_TIER_4))
//            .save(finishedRecipeConsumer, id("ic2/lappack"));
    }

    @SafeVarargs
    public final Ingredient tagsIngredient(TagKey<Item>... tags) {
        return Ingredient.fromValues(Stream.of(tags).map(Ingredient.TagValue::new));
    }

    private static ResourceLocation id(String name) {
        return location("shaped/" + name);
    }

    private static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints pCount, ItemLike pItem) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItem).withCount(pCount).build());
    }

    private static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    private static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> pTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pPredicates);
    }
}
