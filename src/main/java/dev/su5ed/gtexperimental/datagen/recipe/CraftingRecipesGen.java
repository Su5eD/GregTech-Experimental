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
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.TurbineRotor;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.recipe.crafting.ToolCraftingIngredient;
import dev.su5ed.gtexperimental.recipe.crafting.WrappedShapedRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.gen.ConditionalShapedRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import dev.su5ed.gtexperimental.recipe.type.VanillaDamagedIngredient;
import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
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
import static dev.su5ed.gtexperimental.recipe.crafting.WrappedShapedRecipeBuilder.toolShaped;
import static dev.su5ed.gtexperimental.recipe.crafting.WrappedShapelessRecipeBuilder.toolShapeless;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;
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
            .addCondition(IC2_LOADED, SelectedProfileCondition.CLASSIC)
            .save(finishedRecipeConsumer, id("component/energy_crystal"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.ENERGIUM_DUST, 9)
            .define('D', Dust.RUBY.getTag())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .pattern("RDR")
            .pattern("DRD")
            .pattern("RDR")
            .unlockedBy("has_gems_ruby", has(Miscellaneous.RUBY.getTag()))
            .addCondition(IC2_LOADED, SelectedProfileCondition.EXPERIMENTAL)
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
        ConditionalShapedRecipeBuilder.toolShaped(Component.IRON_GEAR)
            .define(SelectedProfileCondition.REFINED_IRON, 'P', Plate.REFINED_IRON.getTag())
            .define(SelectedProfileCondition.REGULAR_IRON, 'P', Plate.IRON.getTag())
            .define(SelectedProfileCondition.REFINED_IRON, 'S', Rod.REFINED_IRON.getTag())
            .define(SelectedProfileCondition.REGULAR_IRON, 'S', Rod.IRON.getTag())
            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .pattern("SPS")
            .pattern("PWP")
            .pattern("SPS")
            .unlockedBy("has_1kk_eu_store", has(GregTechTags.CRAFTING_1KK_EU_STORE))
            .save(finishedRecipeConsumer, id("component/iron_gear"));
        ConditionalShapedRecipeBuilder.shaped(Miscellaneous.IRON_MORTAR)
            .define('S', ItemTags.STONE_BRICKS)
            .define(SelectedProfileCondition.REFINED_IRON, 'R', GregTechTags.ingot("refined_iron"))
            .define(SelectedProfileCondition.REGULAR_IRON, 'R', Tags.Items.INGOTS_IRON)
            .pattern(" R ")
            .pattern("SRS")
            .pattern("SSS")
            .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
            .save(finishedRecipeConsumer, id("component/iron_mortar"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.ADVANCED_CIRCUIT)
            .define('C', GregTechTags.CIRCUIT)
            .define('S', Tags.Items.DUSTS_REDSTONE)
            .define('G', Tags.Items.DUSTS_GLOWSTONE)
            .define('L', Dust.LAZURITE.getTag())
            .pattern("SGS")
            .pattern("LCL")
            .pattern("SGS")
            .unlockedBy("has_circuit", has(GregTechTags.CIRCUIT))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/advanced_circuit"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.ADVANCED_CIRCUIT)
            .define('C', GregTechTags.CIRCUIT)
            .define('S', Tags.Items.DUSTS_REDSTONE)
            .define('G', Tags.Items.DUSTS_GLOWSTONE)
            .define('L', Dust.LAZURITE.getTag())
            .pattern("SLS")
            .pattern("GCG")
            .pattern("SLS")
            .unlockedBy("has_circuit", has(GregTechTags.CIRCUIT))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/advanced_circuit_vertical"));
        shaped(ModCoverItem.DATA_CONTROL_CIRCUIT, 4)
            .define('E', GregTechTags.CIRCUIT_TIER_5)
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('A', GregTechTags.ADVANCED_CIRCUIT)
            .pattern("AEA")
            .pattern("EIE")
            .pattern("AEA")
            .unlockedBy("has_iridium_alloy", has(GregTechTags.IRIDIUM_ALLOY))
            .save(finishedRecipeConsumer, id("component/data_control_circuit"));
        // TODO Shift right click to clean orb
//        shapeless(Component.DATA_ORB)
//            .requires(Component.DATA_ORB)
//            .save(finishedRecipeConsumer, id("component/data_orb_clean"));
        shaped(Component.DIAMOND_GRINDER, 2)
            .define('G', Tags.Items.GEMS_DIAMOND)
            .define('D', Dust.DIAMOND.getTag())
            .define('S', Plate.STEEL.getTag())
            .pattern("DSD")
            .pattern("SGS")
            .pattern("DSD")
            .unlockedBy("has_gems_diamond", has(Tags.Items.GEMS_DIAMOND))
            .save(finishedRecipeConsumer, id("component/diamond_grinder"));
        shaped(Component.DIAMOND_SAWBLADE, 4)
            .define('D', Dust.DIAMOND.getTag())
            .define('S', Plate.STEEL.getTag())
            .pattern("DSD")
            .pattern("S S")
            .pattern("DSD")
            .unlockedBy("has_gems_diamond", has(Tags.Items.GEMS_DIAMOND))
            .save(finishedRecipeConsumer, id("component/diamond_sawblade"));
        shaped(Component.DUCT_TAPE, 4)
            .define('C', GregTechTags.CARBON_MESH)
            .define('R', GregTechTags.RUBBER)
            .define('S', GregTechTags.RESIN)
            .pattern("CCC")
            .pattern("RRR")
            .pattern("SSS")
            .unlockedBy("has_rubber", has(GregTechTags.RUBBER))
            .save(finishedRecipeConsumer, id("component/duct_tape"));
        gear("bronze", Component.BRONZE_GEAR, finishedRecipeConsumer);
        gear("iridium", Component.IRIDIUM_GEAR, finishedRecipeConsumer);
        gear("steel", Component.STEEL_GEAR, finishedRecipeConsumer);
        gear("titanium", Component.TITANIUM_GEAR, finishedRecipeConsumer);
        gear("tungsten_steel", Component.TUNGSTEN_STEEL_GEAR, finishedRecipeConsumer);
        hull("aluminium", Component.ALUMINIUM_HULL, finishedRecipeConsumer);
        hull("brass", Component.BRASS_HULL, finishedRecipeConsumer);
        hull("bronze", Component.BRONZE_HULL, finishedRecipeConsumer);
        hull("steel", Component.STEEL_HULL, finishedRecipeConsumer);
        hull("titanium", Component.TITANIUM_HULL, finishedRecipeConsumer);
        hull("tungsten_steel", Component.TUNGSTEN_STEEL_HULL, finishedRecipeConsumer);
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.LAPOTRON_CRYSTAL)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .define('S', Miscellaneous.SAPPHIRE.getTag())
            .define('L', Dust.LAZURITE.getTag())
            .pattern("LCL")
            .pattern("LSL")
            .pattern("LCL")
            .unlockedBy("has_advanced_circuit", has(GregTechTags.ADVANCED_CIRCUIT))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/lapotron_crystal_sapphire"));
        // TODO Lithium cell
        WrappedShapedRecipeBuilder.fluidShaped(Component.LITHIUM_RE_BATTERY)
            .define('C', GregTechTags.DOUBLE_INSULATED_GOLD_CABLE)
            .define('L', VanillaFluidIngredient.of(ModFluid.LITHIUM.getTag(), buckets(1)))
            .define('A', tagsIngredient(Plate.ALUMINIUM.getTag(), Plate.BATTERY_ALLOY.getTag()))
            .pattern(" C ")
            .pattern("ALA")
            .pattern("ALA")
            .unlockedBy("has_aluminium_plate", has(Plate.ALUMINIUM.getTag(), Plate.BATTERY_ALLOY.getTag()))
            .save(finishedRecipeConsumer, id("component/lithium_re_battery"));
        shapeless(Miscellaneous.FLINT_MORTAR)
            .requires(Items.BOWL)
            .requires(Items.FLINT)
            .unlockedBy("has_bowl", has(Items.BOWL))
            .save(finishedRecipeConsumer, id("component/flint_mortar"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.OVERCLOCKER_UPGRADE, 2)
            .define('H', Ingredient.of(NuclearCoolantPack.HELIUM_60K, NuclearCoolantPack.NAK_60K))
            .define('W', GregTechTags.INSULATED_COPPER_CABLE)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .pattern(" H ")
            .pattern("WCW")
            .unlockedBy("has_advanced_circuit", has(GregTechTags.ADVANCED_CIRCUIT))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/overclocker_upgrade"));
        shaped(Upgrade.QUANTUM_CHEST)
            .define('A', GregTechTags.CIRCUIT_TIER_8)
            .define('S', GregTechTags.CRAFTING_MONITOR_TIER_2)
            .define('D', Plate.ALUMINIUM.getTag())
            .define('T', GregTechTags.TELEPORTER)
            .define('B', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)
            .pattern("ASA")
            .pattern("BTB")
            .pattern("ADA")
            .unlockedBy("has_circuit_tier_8", has(GregTechTags.CIRCUIT_TIER_8))
            .save(finishedRecipeConsumer, id("component/quantum_chest_upgrade"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.RE_BATTERY, 2)
            .define('S', VanillaFluidIngredient.of(ModFluid.SULFURIC_ACID.getTag(), buckets(1)))
            .define('L', Dust.LEAD.getTag())
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .define('T', Plate.TIN.getTag())
            .pattern(" C ")
            .pattern("TLT")
            .pattern("TST")
            .unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/re_battery_sulfur"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.RE_BATTERY, 2)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .define('T', Plate.BATTERY_ALLOY.getTag())
            .pattern(" C ")
            .pattern("TRT")
            .pattern("TRT")
            .unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/re_battery_alloy"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.RE_BATTERY, 3)
            .define('S', VanillaFluidIngredient.of(ModFluid.SULFURIC_ACID.getTag(), buckets(1)))
            .define('L', Dust.LEAD.getTag())
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .define('T', Plate.BATTERY_ALLOY.getTag())
            .pattern(" C ")
            .pattern("TLT")
            .pattern("TST")
            .unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/re_battery_sulfur_alloy"));
        shaped(ModCoverItem.SCREEN)
            .define('A', Plate.ALUMINIUM.getTag())
            .define('L', Tags.Items.DUSTS_GLOWSTONE)
            .define('R', Tags.Items.DYES_RED)
            .define('G', Tags.Items.DYES_LIME)
            .define('B', Tags.Items.DYES_BLUE)
            .define('P', Tags.Items.GLASS_PANES_COLORLESS)
            .pattern("AGA")
            .pattern("RPB")
            .pattern("ALA")
            .unlockedBy("has_dusts_glowstone", has(Tags.Items.DUSTS_GLOWSTONE))
            .save(finishedRecipeConsumer, id("component/screen"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.SINGLE_USE_BATTERY, 32)
            .define('Q', VanillaFluidIngredient.of(ModFluid.MERCURY.getTag(), buckets(1)))
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .pattern("C")
            .pattern("Q")
            .pattern("R")
            .unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/single_use_battery"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.SINGLE_USE_BATTERY, 32)
            .define('S', VanillaFluidIngredient.of(ModFluid.SULFURIC_ACID.getTag(), buckets(1)))
            .define('R', Dust.LEAD.getTag())
            .define('C', GregTechTags.INSULATED_COPPER_CABLE)
            .pattern("C")
            .pattern("S")
            .pattern("R")
            .unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/single_use_battery_sulfur"));
        shaped(ModCoverItem.SOLAR_PANEL)
            .define('C', GregTechTags.ADVANCED_CIRCUIT)
            .define('G', Tags.Items.GLASS_PANES_COLORLESS)
            .define('P', GregTechTags.CARBON_PLATE)
            .define('S', Plate.SILICON.getTag())
            .pattern("SGS")
            .pattern("PCP")
            .unlockedBy("has_advanced_circuit", has(GregTechTags.ADVANCED_CIRCUIT))
            .save(finishedRecipeConsumer, id("component/solar_panel"));
        // TODO New tiered solar panels
//        shaped(ModCoverItem.SOLAR_PANEL_HV)
//            .define('S', ModCoverItem.SOLAR_PANEL_MV.getTag())
//            .define('T', Upgrade.HV_TRANSFORMER_UPGRADE.getTag())
//            .pattern("SSS")
//            .pattern("STS")
//            .pattern("SSS")
//            .unlockedBy("has_solar_panel_mv", has(ModCoverItem.SOLAR_PANEL_MV.getTag()))
//            .save(finishedRecipeConsumer, id("component/solar_panel_hv"));
//        shaped(ModCoverItem.SOLAR_PANEL_MV)
//            .define('S', ModCoverItem.SOLAR_PANEL_LV.getTag())
//            .define('T', GregTechTags.TRANSFORMER_UPGRADE)
//            .pattern("SSS")
//            .pattern("STS")
//            .pattern("SSS")
//            .unlockedBy("has_solar_panel_lv", has(ModCoverItem.SOLAR_PANEL_LV.getTag()))
//            .save(finishedRecipeConsumer, id("component/solar_panel_mv"));
//        shaped(ModCoverItem.SOLAR_PANEL_LV)
//            .define('S', GregTechTags.CRAFTING_SOLAR_PANEL)
//            .define('T', GregTechTags.ADVANCED_CIRCUIT)
//            .pattern("SSS")
//            .pattern("STS")
//            .pattern("SSS")
//            .unlockedBy("has_solar_panel", has(GregTechTags.CRAFTING_SOLAR_PANEL))
//            .save(finishedRecipeConsumer, id("component/solar_panel_lv"));
        shaped(Component.SUPERCONDUCTOR, 4)
            .define('L', GregTechTags.CIRCUIT_TIER_7)
            .define('W', Plate.TUNGSTEN.getTag())
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('C', tagsIngredient(NuclearCoolantPack.HELIUM_60K.getTag(), NuclearCoolantPack.NAK_60K.getTag()))
            .pattern("CCC")
            .pattern("WIW")
            .pattern("LLL")
            .unlockedBy("has_circuit_tier_7", has(GregTechTags.CIRCUIT_TIER_7))
            .save(finishedRecipeConsumer, id("component/superconductor"));
        ConditionalShapedRecipeBuilder.shaped(Component.SUPERCONDUCTOR, 4)
            .define('L', GregTechTags.CIRCUIT_TIER_7)
            .define('W', Plate.TUNGSTEN.getTag())
            .define('I', GregTechTags.IRIDIUM_ALLOY)
            .define('C', Ic2Items.SEXTUPLE_REACTOR_COOLANT_CELL)
            .pattern("CCC")
            .pattern("WIW")
            .pattern("LLL")
            .unlockedBy("has_circuit_tier_7", has(GregTechTags.CIRCUIT_TIER_7))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("component/superconductor_2"));
        turbineBlade("bronze", Component.BRONZE_TURBINE_BLADE, finishedRecipeConsumer);
        turbineBlade("carbon", Component.CARBON_TURBINE_BLADE, finishedRecipeConsumer);
        turbineBlade("magnalium", Component.MAGNALIUM_TURBINE_BLADE, finishedRecipeConsumer);
        turbineBlade("steel", Component.STEEL_TURBINE_BLADE, finishedRecipeConsumer);
        turbineBlade("tungsten_steel", Component.TUNGSTEN_STEEL_TURBINE_BLADE, finishedRecipeConsumer);
        turbineRotor("bronze", Component.BRONZE_TURBINE_BLADE.getTag(), GregTechTags.material("blocks", "bronze"), TurbineRotor.BRONZE, finishedRecipeConsumer);
        turbineRotor("carbon", Component.CARBON_TURBINE_BLADE.getTag(), Tags.Items.STORAGE_BLOCKS_IRON, TurbineRotor.CARBON, finishedRecipeConsumer);
        turbineRotor("magnalium", Component.MAGNALIUM_TURBINE_BLADE.getTag(), Tags.Items.STORAGE_BLOCKS_IRON, TurbineRotor.MAGNALIUM, finishedRecipeConsumer);
        turbineRotor("steel", Component.STEEL_TURBINE_BLADE.getTag(), ModBlock.STEEL.getTag(), TurbineRotor.STEEL, finishedRecipeConsumer);
        turbineRotor("tungsten_steel", Component.TUNGSTEN_STEEL_TURBINE_BLADE.getTag(), ModBlock.STEEL.getTag(), TurbineRotor.TUNGSTEN_STEEL, finishedRecipeConsumer);
        shaped(Component.WOLFRAMIUM_GRINDER, 2)
            .define('B', ModBlock.STEEL.getTag())
            .define('T', Plate.TUNGSTEN.getTag())
            .define('S', Plate.STEEL.getTag())
            .pattern("TST")
            .pattern("SBS")
            .pattern("TST")
            .unlockedBy("has_tungsten_plate", has(Plate.TUNGSTEN.getTag()))
            .save(finishedRecipeConsumer, id("component/wolframium_grinder"));

        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT, 6)
            .define('X', Plate.TUNGSTEN_STEEL.getTag())
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', Plate.ALUMINIUM.getTag())
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_tungsten_steel_plate", has(Plate.TUNGSTEN_STEEL.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/mixed_metal_ingot"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT, 5)
            .define('X', Plate.TUNGSTEN_STEEL.getTag())
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag()))
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_tungsten_steel_plate", has(Plate.TUNGSTEN_STEEL.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("mixed_metal_ingot_2"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT, 4)
            .define('X', tagsIngredient(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag()))
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', Plate.ALUMINIUM.getTag())
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_titanium_plate", has(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("mixed_metal_ingot_3"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT, 3)
            .define('X', tagsIngredient(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag()))
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag()))
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_titanium_plate", has(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("mixed_metal_ingot_4"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT, 3)
            .define('X', tagsIngredient(Plate.INVAR.getTag(), Plate.STEEL.getTag()))
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', Plate.ALUMINIUM.getTag())
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_invar_plate", has(Plate.INVAR.getTag(), Plate.STEEL.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("mixed_metal_ingot_5"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT, 2)
            .define('X', tagsIngredient(Plate.INVAR.getTag(), Plate.STEEL.getTag()))
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag()))
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_invar_plate", has(Plate.INVAR.getTag(), Plate.STEEL.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("mixed_metal_ingot_6"));
        ConditionalShapedRecipeBuilder.shaped(Ic2Items.MIXED_METAL_INGOT)
            .define(SelectedProfileCondition.REFINED_IRON, 'X', tagsIngredient(Plate.REFINED_IRON.getTag(), Plate.NICKEL.getTag()))
            .define(SelectedProfileCondition.REGULAR_IRON, 'X', tagsIngredient(Plate.IRON.getTag(), Plate.NICKEL.getTag()))
            .define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag(), Plate.ALUMINIUM.getTag()))
            .pattern("X")
            .pattern("Y")
            .pattern("Z")
            .unlockedBy("has_bronze_plate", has(Plate.BRONZE.getTag(), Plate.BRASS.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, id("mixed_metal_ingot_7"));

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

    private static void gear(String name, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        toolShaped(result)
            .define('P', GregTechTags.material("plates", name))
            .define('S', GregTechTags.material("rods", name))
            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .pattern("SPS")
            .pattern("PWP")
            .pattern("SPS")
            .unlockedBy("has_" + name + "_plate", has(GregTechTags.material("plates", name)))
            .save(finishedRecipeConsumer, id("component/" + name + "_gear"));
    }

    private static void hull(String name, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        toolShaped(result)
            .define('R', GregTechTags.material("plates", name))
            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .pattern("RRR")
            .pattern("RWR")
            .pattern("RRR")
            .unlockedBy("has_" + name + "_plate", has(GregTechTags.material("plates", name)))
            .save(finishedRecipeConsumer, id("component/" + name + "_hull"));
    }

    private static void turbineBlade(String name, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        shaped(result)
            .define('P', GregTechTags.material("plates", name))
            .define('H', GregTechTags.HARD_HAMMER)
            .define('F', GregTechTags.FILE)
            .pattern(" H ")
            .pattern("PPP")
            .pattern(" F ")
            .unlockedBy("has_" + name + "_plate", has(GregTechTags.material("plates", name)))
            .save(finishedRecipeConsumer, id("component/" + name + "_turbine_blade"));
    }

    private static void turbineRotor(String name, TagKey<Item> blade, TagKey<Item> block, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        shaped(result)
            .define('T', blade)
            .define('B', block)
            .pattern("TTT")
            .pattern("TBT")
            .pattern("TTT")
            .unlockedBy("has_" + name + "_blade", has(blade))
            .save(finishedRecipeConsumer, id("component/" + name + "_turbine_rotor"));

        toolShapeless(result)
            .requires(VanillaDamagedIngredient.of(result))
            .requires(GregTechTags.HARD_HAMMER)
            .requires(ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .requires(GregTechTags.FILE)
            .unlockedBy("has_" + name + "_blade", has(blade))
            .save(finishedRecipeConsumer, id("component/" + name + "_turbine_rotor_repair"));
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

    @SafeVarargs
    private static InventoryChangeTrigger.TriggerInstance has(TagKey<Item>... tags) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.item();
        for (TagKey<Item> tag : tags) {
            builder.of(tag);
        }
        return inventoryTrigger(builder.build());
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
    }
}
