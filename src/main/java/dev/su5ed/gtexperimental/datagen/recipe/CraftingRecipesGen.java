package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.recipe.gen.ConditionalShapedRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import dev.su5ed.gtexperimental.recipe.type.ToolCraftingIngredient;
import dev.su5ed.gtexperimental.recipe.type.ToolShapedRecipeBuilder;
import ic2.core.ref.Ic2Items;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public final class CraftingRecipesGen implements ModRecipeProvider {
    public static final CraftingRecipesGen INSTANCE = new CraftingRecipesGen();

    private CraftingRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Armor
        ConditionalShapedRecipeBuilder.shaped(Armor.CLOAKING_DEVICE)
            .define(SelectedProfileCondition.CLASSIC, 'L', GregTechTags.CRAFTING_10KK_EU_STORE)
            .define(SelectedProfileCondition.EXPERIMENTAL, 'L', GregTechTags.CRAFTING_100KK_EU_STORE)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('C', Plate.CHROME.getTag())
            .pattern("CIC")
            .pattern("ILI")
            .pattern("CIC")
            .unlockedBy(SelectedProfileCondition.CLASSIC, "has_10kk_eu_store", has(GregTechTags.CRAFTING_10KK_EU_STORE))
            .unlockedBy(SelectedProfileCondition.EXPERIMENTAL, "has_100kk_eu_store", has(GregTechTags.CRAFTING_100KK_EU_STORE))
            .save(finishedRecipeConsumer, id("armor/cloaking_device"));
        ConditionalShapedRecipeBuilder.shaped(Armor.LAPOTRONPACK)
            .define(SelectedProfileCondition.CLASSIC, 'L', GregTechTags.CRAFTING_10KK_EU_STORE)
            .define(SelectedProfileCondition.EXPERIMENTAL, 'L', GregTechTags.CRAFTING_100KK_EU_STORE)
            .define('C', GregTechTags.CIRCUIT_TIER_7)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('P', GregTechTags.LAPPACK)
            .define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR)
            .pattern("CLC")
            .pattern("SPS")
            .pattern("CIC")
            .unlockedBy(SelectedProfileCondition.CLASSIC, "has_10kk_eu_store", has(GregTechTags.CRAFTING_10KK_EU_STORE))
            .unlockedBy(SelectedProfileCondition.EXPERIMENTAL, "has_100kk_eu_store", has(GregTechTags.CRAFTING_100KK_EU_STORE))
            .save(finishedRecipeConsumer, id("armor/lapotronpack"));
        shapeless(Armor.LIGHT_HELMET)
            .requires(GregTechTags.SOLAR_HELMET)
            .requires(GregTechTags.ILLUMINATOR_FLAT)
            .unlockedBy("has_solar_helmet", has(GregTechTags.SOLAR_HELMET))
            .save(finishedRecipeConsumer, id("armor/light_helmet"));
        shaped(Armor.LITHIUM_BATPACK)
            .define('L', GregTechTags.CRAFTING_LI_BATTERY)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .define('A', Plate.ALUMINIUM.getTag())
            .pattern("LCL")
            .pattern("LAL")
            .pattern("L L")
            .unlockedBy("has_crafting_li_battery", has(GregTechTags.CRAFTING_LI_BATTERY))
            .save(finishedRecipeConsumer, id("armor/lithium_batpack"));

        // Blocks
        ConditionalShapedRecipeBuilder.shaped(ModBlock.STANDARD_MACHINE_CASING, 4)
            .define('C', GregTechTags.CIRCUIT)
            .define(SelectedProfileCondition.REFINED_IRON, 'A', tagsIngredient(Plate.REFINED_IRON.getTag(), Plate.ALUMINIUM.getTag()))
            .define(SelectedProfileCondition.REGULAR_IRON, 'A', tagsIngredient(Plate.IRON.getTag(), Plate.ALUMINIUM.getTag()))
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_1)
            .pattern("AAA")
            .pattern("CMC")
            .pattern("AAA")
            .unlockedBy("has_crafting_raw_machine_tier_1", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1))
            .save(finishedRecipeConsumer, id("block/standard_machine_casing"));
        ConditionalShapedRecipeBuilder.shaped(Blocks.PISTON)
            .define('W', ItemTags.PLANKS)
            .define('C', Tags.Items.COBBLESTONE)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define(SelectedProfileCondition.REFINED_IRON, 'B', tagsIngredient(Tags.Items.INGOTS_IRON, GregTechTags.ingot("bronze"), Ingot.ALUMINIUM.getTag(), Ingot.STEEL.getTag(), Ingot.TITANIUM.getTag(), GregTechTags.ingot("refined_iron")))
            .define(SelectedProfileCondition.REGULAR_IRON, 'B', tagsIngredient(Tags.Items.INGOTS_IRON, GregTechTags.ingot("bronze"), Ingot.ALUMINIUM.getTag(), Ingot.STEEL.getTag(), Ingot.TITANIUM.getTag()))
            .pattern("WWW")
            .pattern("CBC")
            .pattern("CRC")
            .unlockedBy("has_redstone", has(Items.REDSTONE))
            .save(finishedRecipeConsumer, new ResourceLocation("piston"));
        shaped(ModBlock.ADVANCED_MACHINE_CASING, 4)
            .define('C', GregTechTags.CIRCUIT_BOARD_TIER_6)
            .define('T', Plate.CHROME.getTag())
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)
            .pattern("TTT")
            .pattern("CMC")
            .pattern("TTT")
            .unlockedBy("has_crafting_raw_machine_tier_4", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_4))
            .save(finishedRecipeConsumer, id("block/advanced_machine_casing"));
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
            .save(finishedRecipeConsumer, id("block/fusion_coil"));
        shaped(ModBlock.HIGHLY_ADVANCED_MACHINE)
            .define('C', Plate.CHROME)
            .define('T', Plate.TITANIUM)
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)
            .pattern("CTC")
            .pattern("TMT")
            .pattern("CTC")
            .unlockedBy("has_crafting_raw_machine_tier_2", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2))
            .save(finishedRecipeConsumer, id("block/highly_advanced_machine"));
        shaped(ModBlock.LESUBLOCK)
            .define('M', GregTechTags.CIRCUIT)
            .define('L', GregTechTags.LAZURITE_CHUNK)
            .pattern("LLL")
            .pattern("LML")
            .pattern("LLL")
            .unlockedBy("has_crafting_raw_machine_tier_2", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2))
            .save(finishedRecipeConsumer, id("block/lesublock"));
        shaped(ModBlock.REINFORCED_MACHINE_CASING, 4)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .define('S', Plate.STEEL.getTag())
            .define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)
            .pattern("SSS")
            .pattern("CMC")
            .pattern("SSS")
            .unlockedBy("has_crafting_raw_machine_tier_2", has(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2))
            .save(finishedRecipeConsumer, id("block/reinforced_machine_casing"));

        // Components
        ConditionalShapedRecipeBuilder.shaped(ModCoverItem.CONVEYOR)
            .define(SelectedProfileCondition.CLASSIC, 'A', tagsIngredient(Plate.REFINED_IRON.getTag(), Plate.ALUMINIUM.getTag()))
            .define(SelectedProfileCondition.EXPERIMENTAL, 'A', tagsIngredient(Plate.IRON.getTag(), Plate.ALUMINIUM.getTag()))
            .define('C', GregTechTags.CIRCUIT)
            .define('B', GregTechTags.RE_BATTERY)
            .define('G', Tags.Items.GLASS_COLORLESS)
            .pattern("GGG")
            .pattern("AAA")
            .pattern("CBC")
            .unlockedBy("has_circuit", has(GregTechTags.CIRCUIT))
            .save(finishedRecipeConsumer, id("component/conveyor"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.CIRCUIT)
            .define('C', Ic2Items.INSULATED_COPPER_CABLE)
            .define(SelectedProfileCondition.REFINED_IRON, 'I', Plate.REFINED_IRON.getTag())
            .define(SelectedProfileCondition.REGULAR_IRON, 'I', Plate.IRON.getTag())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .pattern("CCC")
            .pattern("RIR")
            .pattern("CCC")
            .unlockedBy("has_insulated_copper_cable", has(Ic2Items.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/circuit"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.CIRCUIT)
            .define(SelectedProfileCondition.REFINED_IRON, 'I', Plate.REFINED_IRON.getTag())
            .define(SelectedProfileCondition.REGULAR_IRON, 'I', Plate.IRON.getTag())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('C', Ic2Items.INSULATED_COPPER_CABLE)
            .pattern("CRC")
            .pattern("CIC")
            .pattern("CRC")
            .unlockedBy("has_insulated_copper_cable", has(Ic2Items.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/circuit_vertical"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.ENERGY_CRYSTAL)
            .define('D', Tags.Items.DUSTS_REDSTONE)
            .define('R', Miscellaneous.RUBY.getTag())
            .pattern("DDD")
            .pattern("DRD")
            .pattern("DDD")
            .unlockedBy("has_gems_ruby", has(Miscellaneous.RUBY.getTag()))
            .addCondition(SelectedProfileCondition.CLASSIC)
            .save(finishedRecipeConsumer, id("component/energy_crystal"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.ENERGIUM_DUST, 9)
            .define('D', Dust.RUBY.getTag())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .pattern("RDR")
            .pattern("DRD")
            .pattern("RDR")
            .unlockedBy("has_gems_ruby", has(Miscellaneous.RUBY.getTag()))
            .addCondition(SelectedProfileCondition.EXPERIMENTAL)
            .save(finishedRecipeConsumer, id("component/energium_dust"));
        ConditionalShapedRecipeBuilder.shaped(ModCoverItem.ENERGY_FLOW_CIRCUIT, 4)
            .define(SelectedProfileCondition.CLASSIC, 'L', GregTechTags.CRAFTING_1KK_EU_STORE)
            .define(SelectedProfileCondition.EXPERIMENTAL, 'L', GregTechTags.CRAFTING_10KK_EU_STORE)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('A', GregTechTags.ADVANCED_CIRCUIT)
            .define('W', Plate.TUNGSTEN.getTag())
            .pattern("AWA")
            .pattern("LIL")
            .pattern("AWA")
            .unlockedBy("has_1kk_eu_store", has(GregTechTags.CRAFTING_1KK_EU_STORE))
            .save(finishedRecipeConsumer, id("component/energy_flow_circuit"));
//        ConditionalShapedRecipeBuilder.toolShaped(Component.IRON_GEAR)
//            .define(SelectedProfileCondition.REFINED_IRON, 'P', Plate.REFINED_IRON.getTag())
//            .define(SelectedProfileCondition.REGULAR_IRON, 'P', Plate.IRON.getTag())
//            .define(SelectedProfileCondition.REFINED_IRON, 'S', Rod.REFINED_IRON.getTag())
//            .define(SelectedProfileCondition.REGULAR_IRON, 'S', Rod.IRON.getTag())
//            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
//            .pattern("SPS")
//            .pattern("PWP")
//            .pattern("SPS")
//            .unlockedBy("has_1kk_eu_store", has(GregTechTags.CRAFTING_1KK_EU_STORE))
//            .save(finishedRecipeConsumer, id("component/iron_gear"));
        ToolShapedRecipeBuilder.toolShaped(Component.IRON_GEAR)
            .define('P', Plate.IRON.getTag())
            .define('S', Rod.IRON.getTag())
            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .pattern("SPS")
            .pattern("PWP")
            .pattern("SPS")
            .unlockedBy("has_1kk_eu_store", has(GregTechTags.CRAFTING_1KK_EU_STORE))
            .save(finishedRecipeConsumer, id("component/iron_gear"));

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

    private static InventoryChangeTrigger.TriggerInstance has(ItemLike item) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(item).build());
    }

    private static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tag).build());
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
    }
}
