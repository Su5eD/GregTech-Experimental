package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.File;
import dev.su5ed.gtexperimental.object.Hammer;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.JackHammer;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.NuclearFuelRod;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.Saw;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.TurbineRotor;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.object.Wrench;
import dev.su5ed.gtexperimental.recipe.crafting.ToolCraftingIngredient;
import dev.su5ed.gtexperimental.recipe.type.VanillaDamagedIngredient;
import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
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

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapedRecipeBuilder.conditionalShaped;
import static dev.su5ed.gtexperimental.recipe.crafting.WrappedShapedRecipeBuilder.fluidShaped;
import static dev.su5ed.gtexperimental.recipe.crafting.WrappedShapedRecipeBuilder.toolShaped;
import static dev.su5ed.gtexperimental.recipe.crafting.WrappedShapelessRecipeBuilder.toolShapeless;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.*;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;

public final class CraftingRecipesGen implements ModRecipeProvider {
    public static final CraftingRecipesGen INSTANCE = new CraftingRecipesGen();

    private CraftingRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Armor
        shaped(Armor.CLOAKING_DEVICE).define('L', GregTechTags.LARGE_EU_STORE).define('I', GregTechTags.IRIDIUM_ALLOY).define('C', Plate.CHROME.getTag()).pattern("CIC").pattern("ILI").pattern("CIC").unlockedBy("has_large_eu_store", hasTags(GregTechTags.LARGE_EU_STORE)).save(finishedRecipeConsumer, shapedId("armor/cloaking_device"));
        shaped(Armor.LAPOTRONPACK).define('L', GregTechTags.LARGE_EU_STORE).define('C', GregTechTags.CIRCUIT_TIER_7).define('I', GregTechTags.IRIDIUM_ALLOY).define('P', GregTechTags.LAPPACK).define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR).pattern("CLC").pattern("SPS").pattern("CIC").unlockedBy("has_large_eu_store", hasTags(GregTechTags.LARGE_EU_STORE)).save(finishedRecipeConsumer, shapedId("armor/lapotronpack"));
        shapeless(Armor.LIGHT_HELMET).requires(GregTechTags.SOLAR_HELMET).requires(GregTechTags.ILLUMINATOR_FLAT).unlockedBy("has_solar_helmet", hasTags(GregTechTags.SOLAR_HELMET)).save(finishedRecipeConsumer, shapelessId("armor/light_helmet"));
        shaped(Armor.LITHIUM_BATPACK).define('L', GregTechTags.CRAFTING_LI_BATTERY).define('C', GregTechTags.ADVANCED_CIRCUIT).define('A', Plate.ALUMINIUM.getTag()).pattern("LCL").pattern("LAL").pattern("L L").unlockedBy("has_crafting_li_battery", hasTags(GregTechTags.CRAFTING_LI_BATTERY)).save(finishedRecipeConsumer, shapedId("armor/lithium_batpack"));

        // Blocks
        shaped(ModBlock.STANDARD_MACHINE_CASING, 4).define('C', GregTechTags.CIRCUIT).define('A', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag())).define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_1).pattern("AAA").pattern("CMC").pattern("AAA").unlockedBy("has_crafting_raw_machine_tier_1", hasTags(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1)).save(finishedRecipeConsumer, shapedId("block/standard_machine_casing"));
        shaped(Blocks.PISTON).define('W', ItemTags.PLANKS).define('C', Tags.Items.COBBLESTONE).define('R', Tags.Items.DUSTS_REDSTONE).define('B', tagsIngredient(GregTechTags.ANY_IRON_INGOT, GregTechTags.ingot("bronze"), Ingot.ALUMINIUM.getTag(), Ingot.STEEL.getTag(), Ingot.TITANIUM.getTag())).pattern("WWW").pattern("CBC").pattern("CRC").unlockedBy("has_redstone", has(Items.REDSTONE)).save(finishedRecipeConsumer, new ResourceLocation("piston"));
        shaped(ModBlock.ADVANCED_MACHINE_CASING, 4).define('C', GregTechTags.CIRCUIT_BOARD_TIER_6).define('T', Plate.CHROME.getTag()).define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4).pattern("TTT").pattern("CMC").pattern("TTT").unlockedBy("has_crafting_raw_machine_tier_4", hasTags(GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)).save(finishedRecipeConsumer, shapedId("block/advanced_machine_casing"));
        shaped(ModBlock.FUSION_COIL).define('I', GregTechTags.IRIDIUM_NEUTRON_REFLECTOR).define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR).define('E', GregTechTags.CIRCUIT_TIER_7).define('C', GregTechTags.CRAFTING_HEATING_COIL_TIER_2).define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4).pattern("ESE").pattern("CMC").pattern("EIE").unlockedBy("has_crafting_raw_machine_tier_4", hasTags(GregTechTags.CRAFTING_RAW_MACHINE_TIER_4)).save(finishedRecipeConsumer, shapedId("block/fusion_coil"));
        shaped(ModBlock.HIGHLY_ADVANCED_MACHINE).define('C', Plate.CHROME).define('T', Plate.TITANIUM).define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_2).pattern("CTC").pattern("TMT").pattern("CTC").unlockedBy("has_crafting_raw_machine_tier_2", hasTags(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)).save(finishedRecipeConsumer, shapedId("block/highly_advanced_machine"));
        shaped(ModBlock.LESUBLOCK).define('M', GregTechTags.CIRCUIT).define('L', GregTechTags.LAZURITE_CHUNK).pattern("LLL").pattern("LML").pattern("LLL").unlockedBy("has_crafting_raw_machine_tier_2", hasTags(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)).save(finishedRecipeConsumer, shapedId("block/lesublock"));
        shaped(ModBlock.REINFORCED_MACHINE_CASING, 4).define('C', GregTechTags.ADVANCED_CIRCUIT).define('S', Plate.STEEL.getTag()).define('M', GregTechTags.CRAFTING_RAW_MACHINE_TIER_2).pattern("SSS").pattern("CMC").pattern("SSS").unlockedBy("has_crafting_raw_machine_tier_2", hasTags(GregTechTags.CRAFTING_RAW_MACHINE_TIER_2)).save(finishedRecipeConsumer, shapedId("block/reinforced_machine_casing"));

        // Components
        shaped(ModCoverItem.CONVEYOR).define('A', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag())).define('C', GregTechTags.CIRCUIT).define('B', GregTechTags.RE_BATTERY).define('G', Tags.Items.GLASS_COLORLESS).pattern("GGG").pattern("AAA").pattern("CBC").unlockedBy("has_circuit", hasTags(GregTechTags.CIRCUIT)).save(finishedRecipeConsumer, shapedId("component/conveyor"));
        shaped(ModCoverItem.ENERGY_FLOW_CIRCUIT, 4).define('L', GregTechTags.MEDIUM_EU_STORE).define('I', GregTechTags.IRIDIUM_ALLOY).define('A', GregTechTags.ADVANCED_CIRCUIT).define('W', Plate.TUNGSTEN.getTag()).pattern("AWA").pattern("LIL").pattern("AWA").unlockedBy("has_medium_eu_store", hasTags(GregTechTags.MEDIUM_EU_STORE)).save(finishedRecipeConsumer, shapedId("component/energy_flow_circuit"));
        toolShaped(Component.IRON_GEAR).define('P', GregTechTags.UNIVERSAL_IRON_PLATE).define('S', GregTechTags.UNIVERSAL_IRON_ROD).define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8)).pattern("SPS").pattern("PWP").pattern("SPS").unlockedBy("has_wrench", hasTags(GregTechTags.WRENCH)).save(finishedRecipeConsumer, shapedId("component/iron_gear"));
        toolShaped(Miscellaneous.IRON_MORTAR).define('S', ItemTags.STONE_BRICKS).define('R', GregTechTags.UNIVERSAL_IRON_INGOT).pattern(" R ").pattern("SRS").pattern("SSS").unlockedBy("has_stone_bricks", hasTags(ItemTags.STONE_BRICKS)).save(finishedRecipeConsumer, shapedId("component/iron_mortar"));
        shaped(ModCoverItem.DATA_CONTROL_CIRCUIT, 4).define('E', GregTechTags.CIRCUIT_TIER_5).define('I', GregTechTags.IRIDIUM_ALLOY).define('A', GregTechTags.ADVANCED_CIRCUIT).pattern("AEA").pattern("EIE").pattern("AEA").unlockedBy("has_iridium_alloy", hasTags(GregTechTags.IRIDIUM_ALLOY)).save(finishedRecipeConsumer, shapedId("component/data_control_circuit"));
        shaped(Component.DIAMOND_GRINDER, 2).define('G', Tags.Items.GEMS_DIAMOND).define('D', Dust.DIAMOND.getTag()).define('S', Plate.STEEL.getTag()).pattern("DSD").pattern("SGS").pattern("DSD").unlockedBy("has_gems_diamond", hasTags(Tags.Items.GEMS_DIAMOND)).save(finishedRecipeConsumer, shapedId("component/diamond_grinder"));
        shaped(Component.DIAMOND_SAWBLADE, 4).define('D', Dust.DIAMOND.getTag()).define('S', Plate.STEEL.getTag()).pattern("DSD").pattern("S S").pattern("DSD").unlockedBy("has_gems_diamond", hasTags(Tags.Items.GEMS_DIAMOND)).save(finishedRecipeConsumer, shapedId("component/diamond_sawblade"));
        shaped(Component.DUCT_TAPE, 4).define('C', GregTechTags.CARBON_MESH).define('R', GregTechTags.RUBBER).define('S', GregTechTags.RESIN).pattern("CCC").pattern("RRR").pattern("SSS").unlockedBy("has_rubber", hasTags(GregTechTags.RUBBER)).save(finishedRecipeConsumer, shapedId("component/duct_tape"));
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
        // TODO Lithium cell
        fluidShaped(Component.LITHIUM_RE_BATTERY).define('C', GregTechTags.DOUBLE_INSULATED_GOLD_CABLE).define('L', VanillaFluidIngredient.of(ModFluid.LITHIUM.getTag(), buckets(1))).define('A', tagsIngredient(Plate.ALUMINIUM.getTag(), Plate.BATTERY_ALLOY.getTag())).pattern(" C ").pattern("ALA").pattern("ALA").unlockedBy("has_aluminium_plate", hasTags(Plate.ALUMINIUM.getTag(), Plate.BATTERY_ALLOY.getTag())).save(finishedRecipeConsumer, shapedId("component/lithium_re_battery"));
        shapeless(Miscellaneous.FLINT_MORTAR).requires(Items.BOWL).requires(Items.FLINT).unlockedBy("has_bowl", has(Items.BOWL)).save(finishedRecipeConsumer, shapelessId("component/flint_mortar"));
        shaped(Upgrade.QUANTUM_CHEST).define('A', GregTechTags.CIRCUIT_TIER_8).define('S', GregTechTags.CRAFTING_MONITOR_TIER_2).define('D', Plate.ALUMINIUM.getTag()).define('T', GregTechTags.TELEPORTER).define('B', GregTechTags.CRAFTING_RAW_MACHINE_TIER_4).pattern("ASA").pattern("BTB").pattern("ADA").unlockedBy("has_circuit_tier_8", hasTags(GregTechTags.CIRCUIT_TIER_8)).save(finishedRecipeConsumer, shapedId("component/quantum_chest_upgrade"));
        shaped(ModCoverItem.SCREEN).define('A', Plate.ALUMINIUM.getTag()).define('L', Tags.Items.DUSTS_GLOWSTONE).define('R', Tags.Items.DYES_RED).define('G', Tags.Items.DYES_LIME).define('B', Tags.Items.DYES_BLUE).define('P', Tags.Items.GLASS_PANES_COLORLESS).pattern("AGA").pattern("RPB").pattern("ALA").unlockedBy("has_dusts_glowstone", hasTags(Tags.Items.DUSTS_GLOWSTONE)).save(finishedRecipeConsumer, shapedId("component/screen"));
        shaped(ModCoverItem.SOLAR_PANEL).define('C', GregTechTags.ADVANCED_CIRCUIT).define('G', Tags.Items.GLASS_PANES_COLORLESS).define('P', GregTechTags.CARBON_PLATE).define('S', Plate.SILICON.getTag()).pattern("SGS").pattern("PCP").unlockedBy("has_advanced_circuit", hasTags(GregTechTags.ADVANCED_CIRCUIT)).save(finishedRecipeConsumer, shapedId("component/solar_panel"));
        // TODO New tiered solar panels
//        shaped(ModCoverItem.SOLAR_PANEL_HV).define('S', ModCoverItem.SOLAR_PANEL_MV.getTag()).define('T', Upgrade.HV_TRANSFORMER_UPGRADE.getTag()).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_solar_panel_mv", has(ModCoverItem.SOLAR_PANEL_MV.getTag())).save(finishedRecipeConsumer, id("component/solar_panel_hv"));
//        shaped(ModCoverItem.SOLAR_PANEL_MV).define('S', ModCoverItem.SOLAR_PANEL_LV.getTag()).define('T', GregTechTags.TRANSFORMER_UPGRADE).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_solar_panel_lv", has(ModCoverItem.SOLAR_PANEL_LV.getTag())).save(finishedRecipeConsumer, id("component/solar_panel_mv"));
//        shaped(ModCoverItem.SOLAR_PANEL_LV).define('S', GregTechTags.CRAFTING_SOLAR_PANEL).define('T', GregTechTags.ADVANCED_CIRCUIT).pattern("SSS").pattern("STS").pattern("SSS").unlockedBy("has_solar_panel", has(GregTechTags.CRAFTING_SOLAR_PANEL)).save(finishedRecipeConsumer, id("component/solar_panel_lv"));
        shaped(Component.SUPERCONDUCTOR, 4).define('L', GregTechTags.CIRCUIT_TIER_7).define('W', Plate.TUNGSTEN.getTag()).define('I', GregTechTags.IRIDIUM_ALLOY).define('C', tagsIngredient(NuclearCoolantPack.HELIUM_60K.getTag(), NuclearCoolantPack.NAK_60K.getTag())).pattern("CCC").pattern("WIW").pattern("LLL").unlockedBy("has_circuit_tier_7", hasTags(GregTechTags.CIRCUIT_TIER_7)).save(finishedRecipeConsumer, shapedId("component/superconductor"));
        shaped(Component.SUPERCONDUCTOR, 4).define('L', GregTechTags.CIRCUIT_TIER_7).define('W', Plate.TUNGSTEN.getTag()).define('I', GregTechTags.IRIDIUM_ALLOY).define('C', GregTechTags.SEXTUPLE_REACTOR_COOLANT_CELL).pattern("CCC").pattern("WIW").pattern("LLL").unlockedBy("has_circuit_tier_7", hasTags(GregTechTags.CIRCUIT_TIER_7)).save(finishedRecipeConsumer, shapedId("component/superconductor_2"));
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
        shaped(Component.WOLFRAMIUM_GRINDER, 2).define('B', ModBlock.STEEL.getTag()).define('T', Plate.TUNGSTEN.getTag()).define('S', Plate.STEEL.getTag()).pattern("TST").pattern("SBS").pattern("TST").unlockedBy("has_tungsten_plate", hasTags(Plate.TUNGSTEN.getTag())).save(finishedRecipeConsumer, shapedId("component/wolframium_grinder"));

        // Materials
        shapeless(Dust.INVAR, 3).requires(Ingredient.of(GregTechTags.ANY_IRON_DUST), 2).requires(Dust.NICKEL).unlockedBy("has_nickel_dust", has(Dust.NICKEL)).save(finishedRecipeConsumer, shapelessId("material/invar_dust"));
        shapeless(Dust.IRON).requires(Miscellaneous.IRON_MORTAR).requires(GregTechTags.ANY_IRON_INGOT).unlockedBy("has_iron_mortar", has(Miscellaneous.IRON_MORTAR)).save(finishedRecipeConsumer, shapelessId("material/iron_dust"));
        shapeless(Smalldust.LEAD, 3).requires(GregTechTags.ANY_IRON_SMALLDUST).requires(Smalldust.NICKEL.getTag()).unlockedBy("has_small_dusts_nickel", hasTags(Smalldust.NICKEL.getTag())).save(finishedRecipeConsumer, shapelessId("material/lead_smalldust"));
//        conditionalShapeless(Dust.STEEL).requires(SelectedProfileCondition.REFINED_IRON, tagsIngredient(Dust.IRON.getTag(), GregTechTags.dust("refined_iron"))).requires(SelectedProfileCondition.REGULAR_IRON, Smalldust.IRON.getTag()).requires(Dust.COAL.getTag(), 2).unlockedBy("has_coal_dust", has(Dust.COAL.getTag())).save(finishedRecipeConsumer, id("material/steel_dust"));
        shapeless(Dust.BRASS).requires(Miscellaneous.IRON_MORTAR).requires(Ingot.BRASS.getTag()).unlockedBy("has_iron_mortar", has(Miscellaneous.IRON_MORTAR)).save(finishedRecipeConsumer, shapelessId("material/brass_dust"));
        shapeless(Dust.BRASS, 4).requires(Ingredient.of(Dust.COPPER.getTag()), 3).requires(Dust.ZINC.getTag()).unlockedBy("has_copper_dust", hasTags(Dust.COPPER.getTag())).save(finishedRecipeConsumer, shapelessId("material/brass_dust_combining"));
        shapeless(Dust.BRASS).requires(Ingredient.of(Smalldust.COPPER.getTag()), 3).requires(Smalldust.ZINC.getTag()).unlockedBy("has_copper_smalldust", hasTags(Smalldust.COPPER.getTag())).save(finishedRecipeConsumer, shapelessId("material/brass_dust_combining_small"));
        shapeless(Dust.BRONZE).requires(Miscellaneous.IRON_MORTAR).requires(GregTechTags.ingot("bronze")).unlockedBy("has_iron_mortar", has(Miscellaneous.IRON_MORTAR)).save(finishedRecipeConsumer, shapelessId("material/bronze_dust"));
        shapeless(Dust.BRONZE, 2).requires(Ingredient.of(Dust.COPPER.getTag()), 3).requires(Dust.TIN.getTag()).unlockedBy("has_copper_dust", hasTags(Dust.COPPER.getTag())).save(finishedRecipeConsumer, shapelessId("material/bronze_dust_combining"));
        shapeless(Dust.BRONZE).requires(Ingredient.of(Smalldust.COPPER.getTag()), 3).requires(Smalldust.TIN.getTag()).unlockedBy("has_copper_smalldust", hasTags(Smalldust.COPPER.getTag())).save(finishedRecipeConsumer, shapelessId("material/bronze_dust_combining_small"));
        shapeless(Dust.CLAY).requires(GregTechTags.MORTAR).requires(Items.CLAY_BALL).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("material/clay_dust"));
        shapeless(Dust.COPPER).requires(GregTechTags.MORTAR).requires(Tags.Items.INGOTS_COPPER).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("material/copper_dust"));
        shapeless(Dust.ELECTRUM).requires(GregTechTags.MORTAR).requires(Ingot.ELECTRUM.getTag()).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("material/electrum_dust"));
        shapeless(Dust.ELECTRUM, 2).requires(Dust.SILVER.getTag()).requires(Dust.GOLD.getTag()).unlockedBy("has_silver_dust", hasTags(Dust.SILVER.getTag())).save(finishedRecipeConsumer, shapelessId("material/electrum_dust_combining"));
        shapeless(Dust.ELECTRUM).requires(Ingredient.of(Smalldust.SILVER.getTag()), 2).requires(Ingredient.of(Smalldust.GOLD.getTag()), 2).unlockedBy("has_silver_smalldust", hasTags(Smalldust.SILVER.getTag())).save(finishedRecipeConsumer, shapelessId("material/electrum_dust_combining_small"));
        shapeless(Dust.GOLD).requires(GregTechTags.MORTAR).requires(Tags.Items.INGOTS_GOLD).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("material/gold_dust"));
        shapeless(Dust.SALTPETER, 10).requires(VanillaFluidIngredient.of(ModFluid.POTASSIUM.getTag(), buckets(1)), 2).requires(VanillaFluidIngredient.of(ModFluid.NITROGEN.getTag(), buckets(1)), 2).requires(VanillaFluidIngredient.of(GregTechTags.AIR, buckets(1)), 3).unlockedBy("has_potassium_fluid", hasFluid(ModFluid.POTASSIUM.getTag())).save(finishedRecipeConsumer, shapelessId("material/saltpeter_dust"));
        shapeless(Dust.SILVER).requires(GregTechTags.MORTAR).requires(Ingot.SILVER.getTag()).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("material/silver_dust"));
        shapeless(Dust.TIN).requires(GregTechTags.MORTAR).requires(GregTechTags.ingot("tin")).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("material/tin_dust"));
        shapeless(Dust.FLINT).requires(Miscellaneous.IRON_MORTAR).requires(Tags.Items.GRAVEL).unlockedBy("has_iron_mortar", has(Miscellaneous.IRON_MORTAR)).save(finishedRecipeConsumer, shapelessId("material/flint_dust"));

        // Nuclear
        shaped(NuclearCoolantPack.HELIUM_60K).define('H', VanillaFluidIngredient.of(ModFluid.HELIUM.getTag(), buckets(1))).define('T', Plate.TIN.getTag()).pattern(" T ").pattern("THT").pattern(" T ").unlockedBy("has_tin_plate", hasTags(Plate.TIN.getTag())).save(finishedRecipeConsumer, shapedId("nuclear/helium_60k_coolant"));
        shaped(NuclearCoolantPack.HELIUM_180K).define('H', NuclearCoolantPack.HELIUM_60K.getTag()).define('T', Plate.TIN.getTag()).pattern("TTT").pattern("HHH").pattern("TTT").unlockedBy("has_helium_60k_coolant", hasTags(NuclearCoolantPack.HELIUM_60K.getTag())).save(finishedRecipeConsumer, shapedId("nuclear/helium_180k_coolant"));
        shaped(NuclearCoolantPack.HELIUM_360K).define('H', NuclearCoolantPack.HELIUM_180K.getTag()).define('T', Plate.TIN.getTag()).define('C', GregTechTags.DENSE_COPPER_PLATE).pattern("THT").pattern("TCT").pattern("THT").unlockedBy("has_helium_180k_coolant", hasTags(NuclearCoolantPack.HELIUM_180K.getTag())).save(finishedRecipeConsumer, shapedId("nuclear/helium_360k_coolant"));
        shaped(NuclearCoolantPack.NAK_60K).define('N', VanillaFluidIngredient.of(ModFluid.SODIUM.getTag(), buckets(1))).define('K', VanillaFluidIngredient.of(ModFluid.POTASSIUM.getTag(), buckets(1))).define('T', Plate.TIN.getTag()).define('C', GregTechTags.REACTOR_COOLANT_CELL).pattern("TNT").pattern("KCK").pattern("TNT").unlockedBy("has_reactor_coolant_cell", hasTags(GregTechTags.REACTOR_COOLANT_CELL)).save(finishedRecipeConsumer, shapedId("nuclear/nak_60k_coolant"));
        shaped(NuclearCoolantPack.NAK_60K).define('N', VanillaFluidIngredient.of(ModFluid.SODIUM.getTag(), buckets(1))).define('K', VanillaFluidIngredient.of(ModFluid.POTASSIUM.getTag(), buckets(1))).define('T', Plate.TIN.getTag()).define('C', GregTechTags.REACTOR_COOLANT_CELL).pattern("TKT").pattern("NCN").pattern("TKT").unlockedBy("has_reactor_coolant_cell", hasTags(GregTechTags.REACTOR_COOLANT_CELL)).save(finishedRecipeConsumer, shapedId("nuclear/nak_60k_coolant_vertical"));
        shaped(NuclearCoolantPack.NAK_180K).define('H', NuclearCoolantPack.NAK_60K.getTag()).define('T', Plate.TIN.getTag()).pattern("TTT").pattern("HHH").pattern("TTT").unlockedBy("has_nak_60k_coolant", hasTags(NuclearCoolantPack.NAK_60K.getTag())).save(finishedRecipeConsumer, shapedId("nuclear/nak_180k_coolant"));
        shaped(NuclearCoolantPack.NAK_360K).define('H', NuclearCoolantPack.NAK_180K.getTag()).define('T', Plate.TIN.getTag()).define('C', GregTechTags.DENSE_COPPER_PLATE).pattern("THT").pattern("TCT").pattern("THT").unlockedBy("has_nak_180k_coolant", hasTags(NuclearCoolantPack.NAK_180K.getTag())).save(finishedRecipeConsumer, shapedId("nuclear/nak_360k_coolant"));
        shaped(NuclearFuelRod.DUAL_PLUTONIUM).define('C', NuclearFuelRod.PLUTONIUM).define('P', tagsIngredient(Plate.COPPER.getTag(), Plate.LEAD.getTag())).pattern("CPC").unlockedBy("has_plutonium_fuel_rod", has(NuclearFuelRod.PLUTONIUM)).save(finishedRecipeConsumer, shapedId("nuclear/dual_plutonium_fuel_rod"));
        shaped(NuclearFuelRod.QUAD_PLUTONIUM).define('C', NuclearFuelRod.DUAL_PLUTONIUM).define('P', tagsIngredient(Plate.COPPER.getTag(), Plate.LEAD.getTag())).pattern(" C ").pattern("PPP").pattern(" C ").unlockedBy("has_dual_plutonium_fuel_rod", has(NuclearFuelRod.DUAL_PLUTONIUM)).save(finishedRecipeConsumer, shapedId("nuclear/quad_plutonium_fuel_rod"));
        shaped(NuclearFuelRod.DUAL_THORIUM).define('C', NuclearFuelRod.THORIUM).define('P', tagsIngredient(Plate.COPPER.getTag(), Plate.LEAD.getTag())).pattern("CPC").unlockedBy("has_thorium_fuel_rod", has(NuclearFuelRod.THORIUM)).save(finishedRecipeConsumer, shapedId("nuclear/dual_thorium_fuel_rod"));
        shaped(NuclearFuelRod.QUAD_THORIUM).define('C', NuclearFuelRod.DUAL_THORIUM).define('P', tagsIngredient(Plate.COPPER.getTag(), Plate.LEAD.getTag())).pattern(" C ").pattern("PPP").pattern(" C ").unlockedBy("has_dual_thorium_fuel_rod", has(NuclearFuelRod.DUAL_THORIUM)).save(finishedRecipeConsumer, shapedId("nuclear/quad_thorium_fuel_rod"));

        // Tools
        shaped(File.IRON).define('S', Tags.Items.RODS_WOODEN).define('P', GregTechTags.UNIVERSAL_IRON_PLATE).pattern("P").pattern("P").pattern("S").unlockedBy("has_rods_wooden", hasTags(Tags.Items.RODS_WOODEN)).save(finishedRecipeConsumer, shapedId("tool/iron_file"));
        shaped(File.STEEL).define('S', GregTechTags.UNIVERSAL_IRON_ROD).define('P', Plate.STEEL.getTag()).pattern("P").pattern("P").pattern("S").unlockedBy("has_steel_plate", hasTags(Plate.STEEL.getTag())).save(finishedRecipeConsumer, shapedId("tool/steel_file"));
        shaped(File.BRONZE).define('S', Tags.Items.RODS_WOODEN).define('P', Plate.BRONZE.getTag()).pattern("P").pattern("P").pattern("S").unlockedBy("has_bronze_plate", hasTags(Plate.BRONZE.getTag())).save(finishedRecipeConsumer, shapedId("tool/bronze_file"));
        shaped(File.TUNGSTEN_STEEL).define('S', Rod.STEEL.getTag()).define('P', Plate.TUNGSTEN_STEEL.getTag()).pattern("P").pattern("P").pattern("S").unlockedBy("has_tungsten_steel_plate", hasTags(Plate.TUNGSTEN_STEEL.getTag())).save(finishedRecipeConsumer, shapedId("tool/tungsten_steel_file"));
        shaped(Hammer.IRON).define('S', Tags.Items.RODS_WOODEN).define('R', GregTechTags.UNIVERSAL_IRON_INGOT).pattern("RRR").pattern("RRR").pattern(" S ").unlockedBy("has_iron_ingot", hasTags(GregTechTags.UNIVERSAL_IRON_INGOT)).save(finishedRecipeConsumer, shapedId("tool/iron_hammer"));
        shaped(Hammer.BRONZE).define('S', Tags.Items.RODS_WOODEN).define('R', GregTechTags.ingot("bronze")).pattern("RRR").pattern("RRR").pattern(" S ").unlockedBy("has_bronze_ingot", hasTags(GregTechTags.ingot("bronze"))).save(finishedRecipeConsumer, shapedId("tool/bronze_hammer"));
        shaped(Hammer.STEEL).define('S', GregTechTags.UNIVERSAL_IRON_ROD).define('R', Ingot.STEEL.getTag()).pattern("RRR").pattern("RRR").pattern(" S ").unlockedBy("has_steel_ingot", hasTags(Ingot.STEEL.getTag())).save(finishedRecipeConsumer, shapedId("tool/steel_hammer"));
        shaped(Hammer.TUNGSTEN_STEEL).define('S', Rod.STEEL.getTag()).define('R', Ingot.TUNGSTEN_STEEL.getTag()).pattern("RRR").pattern("RRR").pattern(" S ").unlockedBy("has_tungsten_steel_ingot", hasTags(Ingot.TUNGSTEN_STEEL.getTag())).save(finishedRecipeConsumer, shapedId("tool/tungsten_steel_hammer"));
        shaped(Saw.IRON).define('S', Tags.Items.RODS_WOODEN).define('P', GregTechTags.UNIVERSAL_IRON_PLATE).define('F', GregTechTags.FILE).define('H', GregTechTags.HARD_HAMMER).pattern("SSS").pattern("PPS").pattern("FH ").unlockedBy("has_hard_hammer", hasTags(GregTechTags.HARD_HAMMER)).save(finishedRecipeConsumer, shapedId("tool/iron_saw"));
        shaped(Saw.STEEL).define('S', GregTechTags.UNIVERSAL_IRON_ROD).define('P', Plate.STEEL.getTag()).define('F', GregTechTags.FILE).define('H', GregTechTags.HARD_HAMMER).pattern("SSS").pattern("PPS").pattern("FH ").unlockedBy("has_hard_hammer", hasTags(GregTechTags.HARD_HAMMER)).save(finishedRecipeConsumer, shapedId("tool/steel_saw"));
        shaped(Saw.BRONZE).define('S', Tags.Items.RODS_WOODEN).define('P', Plate.BRONZE.getTag()).define('F', GregTechTags.FILE).define('H', GregTechTags.HARD_HAMMER).pattern("SSS").pattern("PPS").pattern("FH ").unlockedBy("has_hard_hammer", hasTags(GregTechTags.HARD_HAMMER)).save(finishedRecipeConsumer, shapedId("tool/bronze_saw"));
        shaped(Saw.TUNGSTEN_STEEL).define('S', Rod.STEEL.getTag()).define('P', Plate.TUNGSTEN_STEEL.getTag()).define('F', GregTechTags.FILE).define('H', GregTechTags.HARD_HAMMER).pattern("SSS").pattern("PPS").pattern("FH ").unlockedBy("has_hard_hammer", hasTags(GregTechTags.HARD_HAMMER)).save(finishedRecipeConsumer, shapedId("tool/tunsten_steel_saw"));
        shaped(Wrench.IRON).define('I', GregTechTags.UNIVERSAL_IRON_INGOT).pattern("I I").pattern("III").pattern(" I ").unlockedBy("has_iron_ingot", hasTags(GregTechTags.UNIVERSAL_IRON_INGOT)).save(finishedRecipeConsumer, shapedId("tool/iron_wrench"));
        conditionalShaped(Wrench.BRONZE).define('B', GregTechTags.ingot("bronze")).pattern("B B").pattern("BBB").pattern(" B ").unlockedBy("has_bronze_ingot", hasTags(GregTechTags.ingot("bronze"))).addCondition(NOT_IC2_LOADED).save(finishedRecipeConsumer, shapedId("tool/bronze_wrench"));
        shaped(Wrench.STEEL).define('S', Ingot.STEEL.getTag()).pattern("S S").pattern("SSS").pattern(" S ").unlockedBy("has_steel_ingot", hasTags(Ingot.STEEL.getTag())).save(finishedRecipeConsumer, shapedId("tool/steel_wrench"));
        shaped(Wrench.TUNGSTEN_STEEL).define('T', Ingot.TUNGSTEN_STEEL.getTag()).pattern("T T").pattern("TTT").pattern(" T ").unlockedBy("has_tungsten_steel", hasTags(Ingot.TUNGSTEN_STEEL.getTag())).save(finishedRecipeConsumer, shapedId("tool/tungsten_steel_wrench"));
        shaped(JackHammer.BRONZE).define('B', GregTechTags.RE_BATTERY).define('C', GregTechTags.CIRCUIT).define('S', Rod.BRONZE.getTag()).define('I', GregTechTags.ingot("bronze")).pattern("SBS").pattern(" C ").pattern(" I ").unlockedBy("has_re_battery", hasTags(GregTechTags.RE_BATTERY)).save(finishedRecipeConsumer, shapedId("tool/bronze_jack_hammer"));
        shaped(JackHammer.STEEL).define('B', GregTechTags.RE_BATTERY).define('C', GregTechTags.ADVANCED_CIRCUIT).define('S', Rod.STEEL.getTag()).define('I', Ingot.STEEL.getTag()).pattern("SBS").pattern(" C ").pattern(" I ").unlockedBy("has_re_battery", hasTags(GregTechTags.RE_BATTERY)).save(finishedRecipeConsumer, shapedId("tool/steel_jack_hammer"));
        shaped(JackHammer.DIAMOND).define('B', GregTechTags.CRAFTING_100K_EU_STORE).define('C', GregTechTags.ADVANCED_CIRCUIT).define('S', tagsIngredient(Rod.TITANIUM.getTag(), Rod.TUNGSTEN_STEEL.getTag())).define('D', Dust.DIAMOND.getTag()).pattern("SBS").pattern(" C ").pattern(" D ").unlockedBy("has_100k_eu_store", hasTags(GregTechTags.CRAFTING_100K_EU_STORE)).save(finishedRecipeConsumer, shapedId("tool/diamond_jack_hammer"));
        shaped(Tool.ADVANCED_DRILL).define('C', GregTechTags.CIRCUIT_TIER_6).define('B', GregTechTags.CRAFTING_LI_BATTERY).define('S', Plate.TUNGSTEN_STEEL.getTag()).define('D', Tags.Items.GEMS_DIAMOND).pattern("DDD").pattern("SCS").pattern("SBS").unlockedBy("has_circuit_tier_6", hasTags(GregTechTags.CIRCUIT_TIER_6)).save(finishedRecipeConsumer, shapedId("tool/advanced_drill"));
        shaped(Tool.ADVANCED_SAW).define('C', GregTechTags.ADVANCED_CIRCUIT).define('B', GregTechTags.CRAFTING_LI_BATTERY).define('S', Plate.TUNGSTEN_STEEL.getTag()).pattern(" SS").pattern("SCS").pattern("BS ").unlockedBy("has_advanced_circuit", hasTags(GregTechTags.ADVANCED_CIRCUIT)).save(finishedRecipeConsumer, shapedId("tool/advanced_saw"));
        shaped(Tool.ADVANCED_WRENCH).define('C', GregTechTags.ADVANCED_CIRCUIT).define('B', GregTechTags.CRAFTING_LI_BATTERY).define('T', Plate.TUNGSTEN_STEEL.getTag()).pattern("T T").pattern("TCT").pattern(" B ").unlockedBy("has_advanced_circuit", hasTags(GregTechTags.ADVANCED_CIRCUIT)).save(finishedRecipeConsumer, shapedId("tool/advanced_wrench"));
        shaped(Tool.CROWBAR).define('I', GregTechTags.UNIVERSAL_IRON_INGOT).define('B', Tags.Items.DYES_BLUE).pattern(" BI").pattern("BIB").pattern("IB ").unlockedBy("has_iron_ingot", hasTags(GregTechTags.UNIVERSAL_IRON_INGOT)).save(finishedRecipeConsumer, shapedId("tool/crowbar"));
        shaped(Tool.DESTRUCTORPACK).define('A', GregTechTags.ADVANCED_CIRCUIT).define('R', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag())).define('L', Items.LAVA_BUCKET).pattern("ARA").pattern("RLR").pattern("ARA").unlockedBy("has_advanced_circuit", hasTags(GregTechTags.ADVANCED_CIRCUIT)).save(finishedRecipeConsumer, shapedId("tool/destructorpack"));
        shaped(Tool.RUBBER_HAMMER).define('S', Tags.Items.RODS_WOODEN).define('R', GregTechTags.RUBBER).pattern("RRR").pattern("RRR").pattern(" S ").unlockedBy("has_rubber", hasTags(GregTechTags.RUBBER)).save(finishedRecipeConsumer, shapedId("tool/rubber_hammer"));
        shaped(Tool.LAPOTRONIC_ENERGY_ORB).define('L', GregTechTags.MEDIUM_EU_STORE).define('I', GregTechTags.IRIDIUM_ALLOY).pattern("LLL").pattern("LIL").pattern("LLL").unlockedBy("has_medium_eu_store", hasTags(GregTechTags.MEDIUM_EU_STORE)).save(finishedRecipeConsumer, shapedId("tool/lapotronic_energy_orb"));
        shaped(Tool.SCREWDRIVER).define('I', GregTechTags.UNIVERSAL_IRON_ROD).define('S', Tags.Items.RODS_WOODEN).pattern("I  ").pattern(" I ").pattern("  S").unlockedBy("has_iron_rod", hasTags(GregTechTags.UNIVERSAL_IRON_ROD)).save(finishedRecipeConsumer, shapedId("tool/screwdriver"));
        shaped(Tool.SOLDERING_TOOL).define('C', GregTechTags.CIRCUIT).define('B', GregTechTags.RE_BATTERY).define('R', GregTechTags.UNIVERSAL_IRON_INGOT).pattern("R").pattern("C").pattern("B").unlockedBy("has_circuit", hasTags(GregTechTags.CIRCUIT)).save(finishedRecipeConsumer, shapedId("tool/soldering_tool"));
        shaped(Tool.TESLA_STAFF).define('L', GregTechTags.LARGE_EU_STORE).define('S', GregTechTags.CRAFTING_SUPERCONDUCTOR).define('I', GregTechTags.IRIDIUM_ALLOY).pattern("LS ").pattern("SI ").pattern("  I").unlockedBy("has_iridium_alloy", hasTags(GregTechTags.IRIDIUM_ALLOY)).save(finishedRecipeConsumer, shapedId("tool/tesla_staff"));
        shaped(Tool.ROCK_CUTTER).define('B', GregTechTags.RE_BATTERY).define('C', GregTechTags.CIRCUIT).define('P', Plate.TITANIUM.getTag()).define('S', Rod.TITANIUM.getTag()).define('D', Dust.DIAMOND.getTag()).pattern("DS ").pattern("DP ").pattern("DCB").unlockedBy("has_re_battery", hasTags(GregTechTags.RE_BATTERY)).save(finishedRecipeConsumer, shapedId("tool/rock_cutter"));
        shaped(Tool.ROCK_CUTTER).define('B', GregTechTags.RE_BATTERY).define('C', GregTechTags.CIRCUIT).define('P', Plate.TUNGSTEN_STEEL.getTag()).define('S', Rod.TUNGSTEN_STEEL.getTag()).define('D', Dust.DIAMOND.getTag()).pattern("DS ").pattern("DP ").pattern("DCB").unlockedBy("has_re_battery", hasTags(GregTechTags.RE_BATTERY)).save(finishedRecipeConsumer, shapedId("tool/rock_cutter_tungsten_steel"));
        shaped(Items.COMPARATOR).define('T', Items.REDSTONE_TORCH).define('Q', Tags.Items.GEMS_QUARTZ).define('S', Items.STONE).pattern(" T ").pattern("TQT").pattern("SSS").unlockedBy("has_gems_quartz", hasTags(Tags.Items.GEMS_QUARTZ)).save(finishedRecipeConsumer, shapedId("comparator"));
        shapeless(Miscellaneous.DIAMOND_CREDIT).requires(Miscellaneous.GOLD_CREDIT, 8).unlockedBy("has_gold_credit", has(Miscellaneous.GOLD_CREDIT)).save(finishedRecipeConsumer, shapelessId("diamond_credit_from_gold"));
        shapeless(Miscellaneous.GOLD_CREDIT, 8).requires(Miscellaneous.DIAMOND_CREDIT).unlockedBy("has_diamond_credit", has(Miscellaneous.DIAMOND_CREDIT)).save(finishedRecipeConsumer, shapelessId("gold_credit_from_diamond"));
        shapeless(Miscellaneous.GOLD_CREDIT).requires(Miscellaneous.SILVER_CREDIT, 8).unlockedBy("has_silver_credit", has(Miscellaneous.SILVER_CREDIT)).save(finishedRecipeConsumer, shapelessId("gold_credit_from_silver"));
        shapeless(Miscellaneous.SILVER_CREDIT, 8).requires(Miscellaneous.GOLD_CREDIT).unlockedBy("has_gold_credit", has(Miscellaneous.GOLD_CREDIT)).save(finishedRecipeConsumer, shapelessId("silver_credit"));
        shapeless(Miscellaneous.FLOUR).requires(GregTechTags.MORTAR).requires(Tags.Items.CROPS_WHEAT).unlockedBy("has_mortar", hasTags(GregTechTags.MORTAR)).save(finishedRecipeConsumer, shapelessId("flour"));
        shapeless(Items.STICK, 2).requires(Items.DEAD_BUSH).unlockedBy("has_dead_bush", has(Items.DEAD_BUSH)).save(finishedRecipeConsumer, shapelessId("stick"));
    }

    private static void gear(String name, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        toolShaped(result)
            .define('P', GregTechTags.material("plates", name))
            .define('S', GregTechTags.material("rods", name))
            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .pattern("SPS")
            .pattern("PWP")
            .pattern("SPS")
            .unlockedBy("has_" + name + "_plate", hasTags(GregTechTags.material("plates", name)))
            .save(finishedRecipeConsumer, shapedId("component/" + name + "_gear"));
    }

    private static void hull(String name, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        toolShaped(result)
            .define('R', GregTechTags.material("plates", name))
            .define('W', ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .pattern("RRR")
            .pattern("RWR")
            .pattern("RRR")
            .unlockedBy("has_" + name + "_plate", hasTags(GregTechTags.material("plates", name)))
            .save(finishedRecipeConsumer, shapedId("component/" + name + "_hull"));
    }

    private static void turbineBlade(String name, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        shaped(result)
            .define('P', GregTechTags.material("plates", name))
            .define('H', GregTechTags.HARD_HAMMER)
            .define('F', GregTechTags.FILE)
            .pattern(" H ")
            .pattern("PPP")
            .pattern(" F ")
            .unlockedBy("has_" + name + "_plate", hasTags(GregTechTags.material("plates", name)))
            .save(finishedRecipeConsumer, shapedId("component/" + name + "_turbine_blade"));
    }

    private static void turbineRotor(String name, TagKey<Item> blade, TagKey<Item> block, ItemLike result, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        shaped(result)
            .define('T', blade)
            .define('B', block)
            .pattern("TTT")
            .pattern("TBT")
            .pattern("TTT")
            .unlockedBy("has_" + name + "_blade", hasTags(blade))
            .save(finishedRecipeConsumer, shapedId("component/" + name + "_turbine_rotor"));

        toolShapeless(result)
            .requires(VanillaDamagedIngredient.of(result))
            .requires(GregTechTags.HARD_HAMMER)
            .requires(ToolCraftingIngredient.of(GregTechTags.WRENCH, 8))
            .requires(GregTechTags.FILE)
            .unlockedBy("has_" + name + "_blade", hasTags(blade))
            .save(finishedRecipeConsumer, shapedId("component/" + name + "_turbine_rotor_repair"));
    }

    private static ResourceLocation shapedId(String name) {
        return location("shaped/" + name);
    }

    private static ResourceLocation shapelessId(String name) {
        return location("shapeless/" + name);
    }

    private static InventoryChangeTrigger.TriggerInstance has(ItemLike item) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(item).build());
    }
}
