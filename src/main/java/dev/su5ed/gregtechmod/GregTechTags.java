package dev.su5ed.gregtechmod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class GregTechTags {
    public static final TagKey<Block> MINEABLE_WITH_SHEARS = forgeBlockTag("mineable/shears");

    public static final TagKey<Item> PLATES = forgeItemTag("plates");
    public static final TagKey<Item> IRIDIUM_ALLOY = itemTag("plates/iridium_alloy");

    public static final TagKey<Item> CRAFTING_SUPERCONDUCTOR = itemTag("crafting/superconductor");
    public static final TagKey<Item> CRAFTING_LI_BATTERY = itemTag("crafting/li_battery");
    public static final TagKey<Item> CRAFTING_DIAMOND_BLADE = itemTag("crafting/diamond_blade");
    public static final TagKey<Item> CRAFTING_GRINDER = itemTag("crafting/grinder");
    public static final TagKey<Item> CRAFTING_MACHINE_PARTS = itemTag("crafting/machine_parts");
    public static final TagKey<Item> CRAFTING_CIRCUIT_PARTS_TIER_4 = itemTag("crafting/circuit_parts_tier_4");
    public static final TagKey<Item> CRAFTING_DUCT_TAPE = itemTag("crafting/duct_tape");

    public static final TagKey<Item> CRAFTING_HEATING_COIL_TIER_0 = itemTag("crafting/heating_coil_tier_0");
    public static final TagKey<Item> CRAFTING_HEATING_COIL_TIER_1 = itemTag("crafting/heating_coil_tier_1");
    public static final TagKey<Item> CRAFTING_HEATING_COIL_TIER_2 = itemTag("crafting/heating_coil_tier_2");

    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_0 = itemTag("crafting/raw_machine_tier_0");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_1 = itemTag("crafting/raw_machine_tier_1");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_2 = itemTag("crafting/raw_machine_tier_2");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_3 = itemTag("crafting/raw_machine_tier_3");

    public static final TagKey<Item> CRAFTING_BRONZE_TURBINE_BLADE = itemTag("crafting/bronze_turbine_blade");
    public static final TagKey<Item> CRAFTING_CARBON_TURBINE_BLADE = itemTag("crafting/carbon_turbine_blade");
    public static final TagKey<Item> CRAFTING_MAGNALIUM_TURBINE_BLADE = itemTag("crafting/magnalium_turbine_blade");
    public static final TagKey<Item> CRAFTING_STEEL_TURBINE_BLADE = itemTag("crafting/steel_turbine_blade");
    public static final TagKey<Item> CRAFTING_TUNGSTEN_STEEL_TURBINE_BLADE = itemTag("crafting/tungsten_steel_turbine_blade");

    public static final TagKey<Item> IRON_GEAR = forgeItemTag("gears/iron");
    public static final TagKey<Item> BRONZE_GEAR = forgeItemTag("gears/bronze");
    public static final TagKey<Item> STEEL_GEAR = forgeItemTag("gears/steel");
    public static final TagKey<Item> TITANIUM_GEAR = forgeItemTag("gears/titanium");
    public static final TagKey<Item> TUNGSTEN_STEEL_GEAR = forgeItemTag("gears/tungsten_steel");
    public static final TagKey<Item> IRIDIUM_GEAR = forgeItemTag("gears/iridium");

    public static final TagKey<Item> CIRCUIT_BOARD_TIER_2 = itemTag("circuit_board_tier_2");
    public static final TagKey<Item> CIRCUIT_BOARD_TIER_4 = itemTag("circuit_board_tier_4");
    public static final TagKey<Item> CIRCUIT_BOARD_TIER_6 = itemTag("circuit_board_tier_6");

    public static final TagKey<Item> CIRCUIT_TIER_0 = itemTag("circuit_tier_0");
    public static final TagKey<Item> CIRCUIT_TIER_5 = itemTag("circuit_tier_5");
    public static final TagKey<Item> CIRCUIT_TIER_6 = itemTag("circuit_tier_6");
    public static final TagKey<Item> CIRCUIT_TIER_8 = itemTag("circuit_tier_8");

    public static final TagKey<Item> CRAFTING_WORK_DETECTOR = itemTag("crafting/work_detector");
    public static final TagKey<Item> CRAFTING_CONVEYOR = itemTag("crafting/conveyor");
    public static final TagKey<Item> CRAFTING_WORKBENCH = itemTag("crafting/workbench");
    public static final TagKey<Item> CRAFTING_DRAIN = itemTag("crafting/drain");
    public static final TagKey<Item> CRAFTING_ENERGY_METER = itemTag("crafting/energy_meter");
    public static final TagKey<Item> CRAFTING_ITEM_METER = itemTag("crafting/item_meter");
    public static final TagKey<Item> CRAFTING_ITEM_VALVE = itemTag("crafting/item_valve");
    public static final TagKey<Item> CRAFTING_LIQUID_METER = itemTag("crafting/liquid_meter");
    public static final TagKey<Item> CRAFTING_WORK_CONTROLLER = itemTag("crafting/work_controller");
    public static final TagKey<Item> CRAFTING_PUMP = itemTag("crafting/pump");
    public static final TagKey<Item> CRAFTING_REDSTONE_CONDUCTOR = itemTag("crafting/redstone_conductor");
    public static final TagKey<Item> CRAFTING_REDSTONE_SIGNALIZER = itemTag("crafting/redstone_signalizer");
    public static final TagKey<Item> CRAFTING_MONITOR_TIER_2 = itemTag("crafting/monitor_tier_2");
    public static final TagKey<Item> CRAFTING_SOLAR_PANEL = itemTag("crafting/solar_panel");
    public static final TagKey<Item> CRAFTING_SOLAR_PANEL_LV = itemTag("crafting/solar_panel_lv");
    public static final TagKey<Item> CRAFTING_SOLAR_PANEL_MV = itemTag("crafting/solar_panel_mv");
    public static final TagKey<Item> CRAFTING_SOLAR_PANEL_HV = itemTag("crafting/solar_panel_hv");

    public static final TagKey<Item> CRAFTING_HV_TRANSFORMER_UPGRADE = itemTag("crafting/hv_transformer_upgrade");
    public static final TagKey<Item> CRAFTING_100K_EU_STORE = itemTag("crafting/100k_eu_store");
    public static final TagKey<Item> CRAFTING_1KK_EU_STORE = itemTag("crafting/1kk_eu_store");
    public static final TagKey<Item> CRAFTING_10KK_EU_STORE = itemTag("crafting/10kk_eu_store");
    public static final TagKey<Item> CRAFTING_100KK_EU_STORE = itemTag("crafting/100kk_eu_store");
    public static final TagKey<Item> CRAFTING_LOCK = itemTag("crafting/lock");
    public static final TagKey<Item> CRAFTING_QUANTUM_CHEST_UPGRADE = itemTag("crafting/quantum_chest_upgrade");
    public static final TagKey<Item> CRAFTING_STEAM_UPGRADE = itemTag("crafting/steam_upgrade");
    public static final TagKey<Item> CRAFTING_STEAM_TANK = itemTag("crafting/steam_tank");
    public static final TagKey<Item> CRAFTING_PNEUMATIC_GENERATOR = itemTag("crafting/pneumatic_generator");
    public static final TagKey<Item> CRAFTING_ENERGY_CELL_UPGRADE = itemTag("crafting/energy_cell_upgrade");

//    public static final TagKey<Block> MINABLE_WITH_JACK_HAMMER = BlockTags.bind("minable_with_jack_hammer");

    public static final TagKey<Item> WRENCH = forgeItemTag("tools/wrench");
    public static final TagKey<Item> CROWBAR = forgeItemTag("tools/crowbar");
    public static final TagKey<Item> SCREWDRIVER = forgeItemTag("tools/screwdriver");
    public static final TagKey<Item> LARGE_DRILL = forgeItemTag("tools/large_drill");
    public static final TagKey<Item> SOFT_HAMMER = forgeItemTag("tools/soft_hammer");
    public static final TagKey<Item> HARD_HAMMER = forgeItemTag("tools/hard_hammer");
    public static final TagKey<Item> SOLDERING_IRON = forgeItemTag("tools/soldering_iron");

    public static final TagKey<Item> HEAT_VENT = itemTag("heat_vent");
    public static final TagKey<Item> COMPONENT_HEAT_VENT = itemTag("component_heat_vent");
    public static final TagKey<Item> ADVANCED_HEAT_VENT = itemTag("advanced_heat_vent");
    public static final TagKey<Item> OVERCLOCKED_HEAT_VENT = itemTag("overclocked_heat_vent");
    public static final TagKey<Item> EMPTY_FLUID_CELL = itemTag("empty_fluid_cell");
    public static final TagKey<Item> EMPTY_FUEL_CAN = itemTag("empty_fuel_can");

    private static final Map<String, TagKey<Item>> PLATES_CACHE = new HashMap<>();

    public static TagKey<Item> plate(String type) {
        return PLATES_CACHE.computeIfAbsent(type, s -> forgeItemTag("plates/" + s));
    }

    private static TagKey<Item> forgeItemTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }

    private static TagKey<Item> itemTag(String name) {
        return ItemTags.create(location(name));
    }

    private static TagKey<Block> forgeBlockTag(String name) {
        return BlockTags.create(new ResourceLocation("forge", name));
    }

    private GregTechTags() {}
}
