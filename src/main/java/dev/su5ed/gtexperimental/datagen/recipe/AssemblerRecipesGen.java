package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
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
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.TWILIGHT_FOREST_LOADED;
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
//        assembler(ModRecipeIngredientTypes.ITEM.of(ENERGY_CONTROL_ITEM_CARD), new ItemStack(CIRCUIT), 1600, 2)
//            .addConditions(energyControlLoaded)
//            .build(finishedRecipeConsumer, id("circuit_from_item_kit"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "assembler", name);
    }
}
