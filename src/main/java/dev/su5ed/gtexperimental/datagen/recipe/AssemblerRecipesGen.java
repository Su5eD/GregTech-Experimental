package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.*;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.assembler;

public final class AssemblerRecipesGen implements ModRecipeProvider {
    public static final AssemblerRecipesGen INSTANCE = new AssemblerRecipesGen();

    private AssemblerRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.GUNPOWDER, 4), ModRecipeIngredientTypes.ITEM.of(Tags.Items.SAND, 4), new ItemStack(Items.TNT), 400, 1)
            .build(finishedRecipeConsumer, id("tnt"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_REDSTONE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FLUID_CELL), Miscellaneous.EMPTY_SPRAY_CAN.getItemStack(), 800, 2)
            .build(finishedRecipeConsumer, id("empty_spray_can"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_GLOWSTONE, 4), ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_REDSTONE, 4), new ItemStack(Items.REDSTONE_LAMP), 400, 1)
            .build(finishedRecipeConsumer, id("redstone_lamp"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.RODS_WOODEN), ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_REDSTONE), new ItemStack(Items.REDSTONE_TORCH), 400, 1)
            .build(finishedRecipeConsumer, id("redstone_torch"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_IRON, 4), ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_REDSTONE), new ItemStack(Items.COMPASS), 400, 1)
            .build(finishedRecipeConsumer, id("compass"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD, 4), ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_REDSTONE), new ItemStack(Items.CLOCK), 400, 4)
            .build(finishedRecipeConsumer, id("clock"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(Tags.Items.GEMS_LAPIS, GregTechTags.LAZURITE_CHUNK), ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_GLOWSTONE), Component.ADVANCED_CIRCUIT_PARTS.getItemStack(2), 800, 2)
            .build(finishedRecipeConsumer, id("advanced_circuit_parts"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(8, Dust.OLIVINE.getTag(), Dust.EMERALD.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.ADVANCED_CIRCUIT), Component.DATA_STORAGE_CIRCUIT.getItemStack(4), 6400, 8)
            .build(finishedRecipeConsumer, id("data_storage_circuit_from_olivine_dust"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(Component.IRON_GEAR.getTag()), ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD, 4), new ItemStack(GEAR_GOLD), 800, 1)
//            .build(finishedRecipeConsumer, id("gold_gear"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("gears", "gold")), ModRecipeIngredientTypes.ITEM.of(Tags.Items.GEMS_DIAMOND, 4), new ItemStack(GEAR_DIAMOND), 1600, 2)
//            .build(finishedRecipeConsumer, id("diamond_gear"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ENDER_PEARLS), ModRecipeIngredientTypes.ITEM.of(Items.BLAZE_POWDER), new ItemStack(Items.ENDER_EYE), 400, 2)
            .build(finishedRecipeConsumer, id("ender_eye_from_powder"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ENDER_PEARLS, 6), ModRecipeIngredientTypes.ITEM.of(Tags.Items.RODS_BLAZE), new ItemStack(Items.ENDER_EYE, 6), 2400, 2)
            .build(finishedRecipeConsumer, id("ender_eye_from_rods"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.GEMS_EMERALD, 8), ModRecipeIngredientTypes.ITEM.of(GregTechTags.ADVANCED_CIRCUIT), Component.DATA_STORAGE_CIRCUIT.getItemStack(4), 3200, 4)
            .build(finishedRecipeConsumer, id("data_storage_circuit_from_emerald"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Miscellaneous.OLIVINE.getTag(), 8), ModRecipeIngredientTypes.ITEM.of(GregTechTags.ADVANCED_CIRCUIT), Component.DATA_STORAGE_CIRCUIT.getItemStack(4), 6400, 8)
            .build(finishedRecipeConsumer, id("data_storage_circuit_from_olivine_gem"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.LITHIUM_RE_BATTERY), ModRecipeIngredientTypes.ITEM.of(Plate.ALUMINIUM.getTag(), 4), Upgrade.LITHIUM_BATTERY_UPGRADE.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("lithium_battery_upgrade"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.IRON.getTag(), 3), new ItemStack(Items.BUCKET), 400, 1)
            .build(finishedRecipeConsumer, id("bucket"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.BRONZE.getTag(), 6), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), Component.BRONZE_HULL.getItemStack(), 400, 8)
            .build(finishedRecipeConsumer, id("bronze_hull"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Rod.BRONZE.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Plate.BRONZE.getTag(), 4), Component.BRONZE_GEAR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("bronze_gear"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.BRASS.getTag(), 6), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), Component.BRASS_HULL.getItemStack(), 400, 8)
            .build(finishedRecipeConsumer, id("brass_hull"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.STEEL.getTag(), 2), ModRecipeIngredientTypes.ITEM.of(GTBlockEntity.GAS_TURBINE), Upgrade.STEAM_UPGRADE.getItemStack(), 1600, 32)
//            .build(finishedRecipeConsumer, id("steam_upgrade"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.STEEL.getTag(), 2), ModRecipeIngredientTypes.ITEM.of(new ItemStack(Items.GLASS_PANE)), Upgrade.STEAM_TANK.getItemStack(), 1600, 32)
            .build(finishedRecipeConsumer, id("steam_tank"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.STEEL.getTag(), 6), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), Component.STEEL_HULL.getItemStack(), 400, 8)
            .build(finishedRecipeConsumer, id("steel_hull"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Rod.STEEL.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Plate.STEEL.getTag(), 4), Component.STEEL_GEAR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("steel_gear"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.TITANIUM.getTag(), 6), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), Component.TITANIUM_HULL.getItemStack(), 400, 8)
            .build(finishedRecipeConsumer, id("titanium_hull"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Rod.TITANIUM.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Plate.TITANIUM.getTag(), 4), Component.TITANIUM_GEAR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("titanium_gear"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.TUNGSTEN_STEEL.getTag(), 6), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), Component.TUNGSTEN_STEEL_HULL.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("tungsten_steel_hull"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.TUNGSTEN_STEEL.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.REINFORCED_STONE), ModBlock.TUNGSTEN_STEEL.getItemStack(), 400, 4)
            .build(finishedRecipeConsumer, id("tungsten_steel_block"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.TUNGSTEN_STEEL.getTag()), ModRecipeIngredientTypes.ITEM.of(ModBlock.IRIDIUM_REINFORCED_STONE), ModBlock.IRIDIUM_REINFORCED_TUNGSTEN_STEEL.getItemStack(), 400, 4)
            .build(finishedRecipeConsumer, id("iridium_reinforced_tungsten_steel_block"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Rod.TUNGSTEN_STEEL.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Plate.TUNGSTEN_STEEL.getTag(), 4), Component.TUNGSTEN_STEEL_GEAR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("tungsten_steel_gear"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.IRIDIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.REINFORCED_STONE), ModBlock.IRIDIUM_REINFORCED_STONE.getItemStack(), 400, 4)
            .build(finishedRecipeConsumer, id("iridium_reinforced_stone"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.IRIDIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(ModBlock.TUNGSTEN_STEEL), ModBlock.IRIDIUM_REINFORCED_TUNGSTEN_STEEL.getItemStack(), 400, 4)
            .build(finishedRecipeConsumer, id("iridium_reinforced_tungsten_steel_block_2"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Rod.IRIDIUM.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Plate.IRIDIUM.getTag(), 4), Component.IRIDIUM_GEAR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("iridium_gear"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.ALUMINIUM.getTag(), 2), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CIRCUIT), Component.MACHINE_PARTS.getItemStack(3), 800, 16)
            .build(finishedRecipeConsumer, id("machine_parts_from_aluminium"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.ALUMINIUM.getTag(), 6), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), Component.ALUMINIUM_HULL.getItemStack(), 400, 8)
            .build(finishedRecipeConsumer, id("aluminium_hull"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CIRCUIT), ModRecipeIngredientTypes.ITEM.of(Plate.ELECTRUM.getTag(), 2), Component.ADVANCED_CIRCUIT_BOARD.getItemStack(), 1600, 2)
            .build(finishedRecipeConsumer, id("advanced_circuit_board_from_circuit"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.SILICON.getTag()), ModRecipeIngredientTypes.ITEM.of(Plate.ELECTRUM.getTag(), 4), Component.ADVANCED_CIRCUIT_BOARD.getItemStack(), 1600, 2)
            .build(finishedRecipeConsumer, id("advanced_circuit_board_from_silicon"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.PLATINUM.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.ADVANCED_CIRCUIT), Component.PROCESSOR_CIRCUIT_BOARD.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("processor_circuit_board"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.RODS_WOODEN), ModRecipeIngredientTypes.ITEM.of(Tags.Items.COBBLESTONE), new ItemStack(Items.LEVER), 400, 1)
            .build(finishedRecipeConsumer, id("lever"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Tags.Items.RODS_WOODEN), ModRecipeIngredientTypes.ITEM.ofTags(ItemTags.COALS, GregTechTags.RESIN), new ItemStack(Items.TORCH), 400, 1)
            .build(finishedRecipeConsumer, id("torch"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Rod.IRON.getTag(), 3), new ItemStack(Items.IRON_BARS, 4), 400, 1)
            .build(finishedRecipeConsumer, id("iron_bars"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.PROCESSOR_CIRCUIT_BOARD), ModRecipeIngredientTypes.ITEM.of(Component.DATA_STORAGE_CIRCUIT), ModCoverItem.DATA_CONTROL_CIRCUIT.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("data_control_circuit"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.PROCESSOR_CIRCUIT_BOARD), ModRecipeIngredientTypes.ITEM.of(GregTechTags.LAPOTRON_CRYSTAL), ModCoverItem.ENERGY_FLOW_CIRCUIT.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("energy_flow_circuit"));
        assembler(ModRecipeIngredientTypes.ITEM.of(ModCoverItem.DATA_CONTROL_CIRCUIT), ModRecipeIngredientTypes.ITEM.of(Component.DATA_STORAGE_CIRCUIT, 8), Component.DATA_ORB.getItemStack(), 12800, 16)
            .build(finishedRecipeConsumer, id("data_orb"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GTBlockEntity.SONICTRON), ModRecipeIngredientTypes.ITEM.of(Component.DATA_STORAGE_CIRCUIT, 4), Tool.PORTABLE_SONICTRON, 6400, 8)
//            .build(finishedRecipeConsumer, id("portable_sonictron"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Armor.LITHIUM_BATPACK), Component.LITHIUM_RE_BATTERY.getItemStack(6), 3200, 4)
            .build(finishedRecipeConsumer, id("lithium_re_battery_from_batpack"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.HV_TRANSFORMER), ModRecipeIngredientTypes.ITEM.of(GregTechTags.TRANSFORMER_UPGRADE), Upgrade.HV_TRANSFORMER_UPGRADE.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("hv_transformer_upgrade"));
        assembler(ModRecipeIngredientTypes.ITEM.of(ModCoverItem.CONVEYOR), ModRecipeIngredientTypes.ITEM.of(ModCoverItem.PUMP_MODULE), ModCoverItem.ITEM_VALVE.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("item_valve"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CARBON_MESH, 16), Component.LAVA_FILTER.getItemStack(), 1600, 8)
            .build(finishedRecipeConsumer, id("lava_filter"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.STEEL_GEAR), ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), Upgrade.PNEUMATIC_GENERATOR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("pneumatic_generator"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1), ModRecipeIngredientTypes.ITEM.of(Items.NOTE_BLOCK, 4), GTBlockEntity.REDSTONE_NOTE_BLOCK, 800, 1)
//            .build(finishedRecipeConsumer, id("redstone_note_block"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1), ModRecipeIngredientTypes.ITEM.of(Items.STONE_BUTTON, 16), GTBlockEntity.BUTTON_PANEL, 800, 1)
//            .build(finishedRecipeConsumer, id("button_panel"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS.getTag()), GTBlockEntity.MACHINE_BOX, 1600, 2)
//            .build(finishedRecipeConsumer, id("machine_box"));

        assembler(ModRecipeIngredientTypes.ITEM.ofTags(5, GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.ofTags(Tags.Items.CHESTS_WOODEN, Tags.Items.CHESTS_TRAPPED), new ItemStack(Items.HOPPER), 800, 2)
            .build(finishedRecipeConsumer, id("hopper"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.PUMP), ModCoverItem.PUMP_MODULE.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("pump_module"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE), ModCoverItem.ITEM_METER.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("item_meter"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Items.LIGHT_WEIGHTED_PRESSURE_PLATE), ModCoverItem.LIQUID_METER.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("liquid_meter"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(2, GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Items.IRON_BARS, 2), ModCoverItem.DRAIN.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("drain"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE), ModRecipeIngredientTypes.ITEM.of(Items.COMPARATOR), ModCoverItem.ACTIVE_DETECTOR.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("active_detector"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Items.LEVER), ModCoverItem.MACHINE_CONTROLLER.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("machine_controller"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Items.CRAFTING_TABLE), ModCoverItem.CRAFTING.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("crafting"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.ENERGY_CRYSTAL), Upgrade.ENERGY_CRYSTAL_UPGRADE.getItemStack(), 1600, 16)
            .build(finishedRecipeConsumer, id("energy_crystal_upgrade"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.LAPOTRON_CRYSTAL), Upgrade.LAPOTRON_CRYSTAL_UPGRADE.getItemStack(), 3200, 16)
            .build(finishedRecipeConsumer, id("lapotron_crystal_upgrade"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Tool.LAPOTRONIC_ENERGY_ORB), Upgrade.ENERGY_ORB.getItemStack(), 6400, 16)
            .build(finishedRecipeConsumer, id("energy_orb"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_PLATE, 2), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CIRCUIT), Component.MACHINE_PARTS.getItemStack(4), 800, 16)
            .build(finishedRecipeConsumer, id("machine_parts_from_iron"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Plate.ELECTRUM.getTag(), 2), Component.BASIC_CIRCUIT_BOARD.getItemStack(2), 800, 1)
            .build(finishedRecipeConsumer, id("basic_circuit_board"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Plate.IRIDIUM.getTag()), Upgrade.MACHINE_LOCK.getItemStack(), 1600, 2)
            .build(finishedRecipeConsumer, id("machine_lock"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Tags.Items.DUSTS_REDSTONE), ModCoverItem.REDSTONE_CONDUCTOR.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("redstone_conductor"));
        assembler(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Items.REDSTONE_TORCH), ModCoverItem.REDSTONE_SIGNALIZER.getItemStack(), 800, 16)
            .build(finishedRecipeConsumer, id("redstone_signalizer"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_ROD, 4), ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_PLATE, 4), Component.IRON_GEAR.getItemStack(), 3200, 4)
            .build(finishedRecipeConsumer, id("iron_gear"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
//        ICondition railcraftLoaded = new ModLoadedCondition(ModHandler.RAILCRAFT_MODID);

        // TODO Base Mod output item recipe for each base mod
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_PLATE, 8), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), new ItemStack(ModHandler.getModItem("machine")), 400, 8)
            .build(finishedRecipeConsumer, id("machine"));

        // Base mods
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin"), 2), new ItemStack(ModHandler.getModItem("empty_cell")), 400, 1)
            .build(finishedRecipeConsumer, id("universal/empty_cell"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.TIN.getTag()), new ItemStack(ModHandler.getModItem("tin_can")), 400, 1)
            .build(finishedRecipeConsumer, id("universal/tin_can"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1), ModRecipeIngredientTypes.ITEM.of(ModCoverItem.SOLAR_PANEL.getTag()), new ItemStack(ModHandler.getModItem("solar_generator")), 1600, 2)
            .build(finishedRecipeConsumer, id("universal/solar_generator"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.BASIC_CIRCUIT_BOARD), ModRecipeIngredientTypes.ITEM.of(GregTechTags.INSULATED_COPPER_CABLE, 3), new ItemStack(ModHandler.getModItem("circuit")), 800, 1)
            .build(finishedRecipeConsumer, id("universal/circuit"));
        // FIXME cant use modhandler here
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.COMPRESSED_COAL_BALL, 8), ModRecipeIngredientTypes.ITEM.ofValues(new Ingredient.ItemValue(new ItemStack(Items.BRICKS)), new Ingredient.TagValue(Tags.Items.STORAGE_BLOCKS_IRON), new Ingredient.TagValue(Tags.Items.OBSIDIAN)), new ItemStack(ModHandler.getModItem("coal_chunk")), 400, 4)
            .build(finishedRecipeConsumer, id("universal/coal_chunk_from_bricks"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.dust("coal"), 8), ModRecipeIngredientTypes.ITEM.of(Items.FLINT), new ItemStack(ModHandler.getModItem("coal_block")), 400, 4)
            .build(finishedRecipeConsumer, id("universal/compressed_coal_ball"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.ADVANCED_CIRCUIT_BOARD), ModRecipeIngredientTypes.ITEM.of(Component.ADVANCED_CIRCUIT_PARTS, 2), new ItemStack(ModHandler.getModItem("advanced_circuit")), 1600, 2)
            .build(finishedRecipeConsumer, id("universal/advanced_circuit"));

        // IC2
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.COPPER_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER), new ItemStack(Ic2Items.INSULATED_COPPER_CABLE), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/insulated_copper_cable"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.GOLD_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER, 2), new ItemStack(Ic2Items.DOUBLE_INSULATED_GOLD_CABLE), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/double_insulated_gold_cable_from_raw"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER, 3), new ItemStack(Ic2Items.TRIPLE_INSULATED_IRON_CABLE), 300, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/triple_insulated_iron_cable_from_raw"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.INSULATED_GOLD_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER), new ItemStack(Ic2Items.DOUBLE_INSULATED_GOLD_CABLE), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/double_insulated_gold_cable_from_insulated"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.INSULATED_IRON_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER, 2), new ItemStack(Ic2Items.TRIPLE_INSULATED_IRON_CABLE), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/triple_insulated_iron_cable_from_insulated"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.DOUBLE_INSULATED_IRON_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER), new ItemStack(Ic2Items.TRIPLE_INSULATED_IRON_CABLE), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/triple_insulated_iron_cable_from_double_insulated"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Dust.FLINT.getTag(), 5), ModRecipeIngredientTypes.ITEM.of(Items.TNT, 3), new ItemStack(Ic2Items.ITNT), 800, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/itnt"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin")), new ItemStack(Ic2Items.TIN_CABLE, 4), 150, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/tin_cable"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.WATER_GENERATOR, 2), new ItemStack(Ic2Items.GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/generator"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), ModRecipeIngredientTypes.ITEM.of(Plate.ALUMINIUM.getTag(), 4), new ItemStack(Ic2Items.WATER_GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/water_generator"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CARBON_PLATE, 4), new ItemStack(Ic2Items.WIND_GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/wind_generator_from_carbon"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), ModRecipeIngredientTypes.ITEM.of(Plate.MAGNALIUM.getTag(), 2), new ItemStack(Ic2Items.WIND_GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/wind_generator_from_magnalium"));

        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.CARBON_FIBRE, 2), new ItemStack(Ic2Items.CARBON_MESH), 800, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/carbon_mesh"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.ALLOY, 2), ModRecipeIngredientTypes.ITEM.of(Items.GLASS, 7), new ItemStack(Ic2Items.REINFORCED_GLASS, 7), 400, 4)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/reinforced_glass"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_LI_BATTERY, 8), ModRecipeIngredientTypes.ITEM.of(Ic2Items.CROPNALYZER), Tool.SCANNER.getItemStack(), 12800, 16)
//            .addConditions(ic2Loaded)
//            .build(finishedRecipeConsumer, id("scanner"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.CIRCUIT), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_WIRE_COPPER), new ItemStack(Ic2Items.FREQUENCY_TRANSMITTER), 800, 1)
//            .addConditions(ic2Loaded)
//            .build(finishedRecipeConsumer, id("circuit"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.BATPACK), new ItemStack(Ic2Items.RE_BATTERY, 6), 1600, 2)
//            .addConditions(ic2Loaded)
//            .build(finishedRecipeConsumer, id("re_battery"));

        // Classic
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.ALLOY), ModRecipeIngredientTypes.ITEM.of(Tags.Items.STONE, 8), new ItemStack(Ic2Items.REINFORCED_STONE, 8), 400, 4)
            .addConditions(IC2_LOADED, SelectedProfileCondition.CLASSIC)
            .build(finishedRecipeConsumer, profileId("classic", "ic2/reinforced_stone"));
//        assembler(ModRecipeIngredientTypes.ITEM.ofTags(Plate.REFINED_IRON.getTag(), Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Ic2Items.METER), ModCoverItem.ENERGY_METER.getItemStack(), 800, 16)
//            .addConditions(ic2Loaded, SelectedProfileCondition.CLASSIC)
//            .build(finishedRecipeConsumer, id("classic/ic2/energy_meter"));

        // FTBIC
        assembler(ModRecipeIngredientTypes.ITEM.of(FTBICItems.CARBON_FIBERS.item.get(), 4), new ItemStack(FTBICItems.CARBON_FIBER_MESH.item.get()), 800, 2)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/carbon_mesh"));
        assembler(ModRecipeIngredientTypes.ITEM.of(FTBICItems.REINFORCED_STONE.get(), 4), ModRecipeIngredientTypes.ITEM.of(Items.GLASS, 4), new ItemStack(FTBICItems.REINFORCED_GLASS.get(), 4), 400, 4)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/reinforced_glass"));
        assembler(ModRecipeIngredientTypes.ITEM.of(FTBICItems.ADVANCED_ALLOY.item.get()), ModRecipeIngredientTypes.ITEM.of(Tags.Items.STONE, 8), new ItemStack(FTBICItems.REINFORCED_STONE.get(), 4), 400, 4)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/reinforced_stone"));

        // Twilight Forest
        assembler(ModRecipeIngredientTypes.ITEM.of(TFItems.CHARM_OF_LIFE_1.get(), 4), new ItemStack(TFItems.CHARM_OF_LIFE_2.get()), 800, 2)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, id("twilight_forest/charm_of_life_2"));
        assembler(ModRecipeIngredientTypes.ITEM.of(TFItems.CHARM_OF_KEEPING_1.get(), 4), new ItemStack(TFItems.CHARM_OF_KEEPING_2.get()), 800, 2)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, id("twilight_forest/charm_of_keeping_2"));
        assembler(ModRecipeIngredientTypes.ITEM.of(TFItems.CHARM_OF_KEEPING_2.get(), 4), new ItemStack(TFItems.CHARM_OF_KEEPING_3.get()), 800, 2)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, id("twilight_forest/charm_of_keeping_3"));

        // Railcraft
//        assembler(ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_TIE, 4), new ItemStack(RAILCRAFT_RAILBED), 800, 1)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("railbed"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_TIE_1, 4), new ItemStack(RAILCRAFT_RAILBED_1), 800, 1)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("railbed_1"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_TIE, RAILCRAFT_RAILBED), ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAIL, 6), new ItemStack(Items.RAIL, 32), 1600, 1)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAILBED_1, 4), ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAIL_2, 6), new ItemStack(RAILCRAFT_track_flex_strap_iron, 32), 1600, 1)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("track_flex_strap_iron"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAILBED_1, 4), ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAIL_3, 6), new ItemStack(RAILCRAFT_track_flex_high_speed, 32), 1600, 1)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("track_flex_high_speed"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAILBED_1), ModRecipeIngredientTypes.ITEM.of(RAILCRAFT_RAIL_4, 6), new ItemStack(RAILCRAFT_track_flex_reinforced, 32), 1600, 1)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("track_flex_reinforced"));

        // Energy Control
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CIRCUIT), ModRecipeIngredientTypes.ITEM.of(Ic2Items.FREQUENCY_TRANSMITTER), new ItemStack(energy_control, item_kit), 1600, 2)
//            .addConditions(energyControlLoaded)
//            .build(finishedRecipeConsumer, id("item_kit"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(ENERGY_CONTROL_ITEM_CARD), new ItemStack(ModHandler.getModItem("circuit")), 1600, 2)
//            .addConditions(energyControlLoaded)
//            .build(finishedRecipeConsumer, id("circuit_from_item_kit"));
    }

    private static RecipeName id(String name) {
        return profileId(null, name);
    }

    private static RecipeName profileId(String profile, String name) {
        return RecipeName.profile(Reference.MODID, "assembler", profile, name);
    }
}
