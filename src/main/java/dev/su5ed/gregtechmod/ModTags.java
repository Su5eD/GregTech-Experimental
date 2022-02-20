package dev.su5ed.gregtechmod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class ModTags {
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_SUPERCONDUCTOR = itemTag("crafting/superconductor");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_LI_BATTERY = itemTag("crafting/li_battery");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_DIAMOND_BLADE = itemTag("crafting/diamond_blade");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_GRINDER = itemTag("crafting/grinder");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_MACHINE_PARTS = itemTag("crafting/machine_parts");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_CIRCUIT_PARTS_TIER_4 = itemTag("crafting/circuit_parts_tier_4");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_DUCT_TAPE = itemTag("crafting/duct_tape");

    public static final Tags.IOptionalNamedTag<Item> CRAFTING_HEATING_COIL_TIER_0 = itemTag("crafting/heating_coil_tier_0");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_HEATING_COIL_TIER_1 = itemTag("crafting/heating_coil_tier_1");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_HEATING_COIL_TIER_2 = itemTag("crafting/heating_coil_tier_2");

    public static final Tags.IOptionalNamedTag<Item> CRAFTING_RAW_MACHINE_TIER_0 = itemTag("crafting/raw_machine_tier_0");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_RAW_MACHINE_TIER_1 = itemTag("crafting/raw_machine_tier_1");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_RAW_MACHINE_TIER_2 = itemTag("crafting/raw_machine_tier_2");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_RAW_MACHINE_TIER_3 = itemTag("crafting/raw_machine_tier_3");

    public static final Tags.IOptionalNamedTag<Item> CRAFTING_BRONZE_TURBINE_BLADE = itemTag("crafting/bronze_turbine_blade");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_CARBON_TURBINE_BLADE = itemTag("crafting/carbon_turbine_blade");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_MAGNALIUM_TURBINE_BLADE = itemTag("crafting/magnalium_turbine_blade");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_STEEL_TURBINE_BLADE = itemTag("crafting/steel_turbine_blade");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_TUNGSTEN_STEEL_TURBINE_BLADE = itemTag("crafting/tungsten_steel_turbine_blade");

    public static final Tags.IOptionalNamedTag<Item> IRON_GEAR = forgeItemTag("gears/iron");
    public static final Tags.IOptionalNamedTag<Item> BRONZE_GEAR = forgeItemTag("gears/bronze");
    public static final Tags.IOptionalNamedTag<Item> STEEL_GEAR = forgeItemTag("gears/steel");
    public static final Tags.IOptionalNamedTag<Item> TITANIUM_GEAR = forgeItemTag("gears/titanium");
    public static final Tags.IOptionalNamedTag<Item> TUNGSTEN_STEEL_GEAR = forgeItemTag("gears/tungsten_steel");
    public static final Tags.IOptionalNamedTag<Item> IRIDIUM_GEAR = forgeItemTag("gears/iridium");

    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_BOARD_TIER_2 = itemTag("circuit_board_tier_2");
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_BOARD_TIER_4 = itemTag("circuit_board_tier_4");
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_BOARD_TIER_6 = itemTag("circuit_board_tier_6");

    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_TIER_0 = itemTag("circuit_tier_0");
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_TIER_5 = itemTag("circuit_tier_5");
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_TIER_6 = itemTag("circuit_tier_6");
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_TIER_8 = itemTag("circuit_tier_8");

    public static final Tags.IOptionalNamedTag<Item> CRAFTING_WORK_DETECTOR = itemTag("crafting/work_detector");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_CONVEYOR = itemTag("crafting/conveyor");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_WORKBENCH = itemTag("crafting/workbench");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_DRAIN = itemTag("crafting/drain");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_ENERGY_METER = itemTag("crafting/energy_meter");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_ITEM_METER = itemTag("crafting/item_meter");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_ITEM_VALVE = itemTag("crafting/item_valve");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_LIQUID_METER = itemTag("crafting/liquid_meter");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_WORK_CONTROLLER = itemTag("crafting/work_controller");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_PUMP = itemTag("crafting/pump");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_REDSTONE_CONDUCTOR = itemTag("crafting/redstone_conductor");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_REDSTONE_SIGNALIZER = itemTag("crafting/redstone_signalizer");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_MONITOR_TIER_2 = itemTag("crafting/monitor_tier_2");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_SOLAR_PANEL = itemTag("crafting/solar_panel");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_SOLAR_PANEL_LV = itemTag("crafting/solar_panel_lv");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_SOLAR_PANEL_MV = itemTag("crafting/solar_panel_mv");
    public static final Tags.IOptionalNamedTag<Item> CRAFTING_SOLAR_PANEL_HV = itemTag("crafting/solar_panel_hv");

    private ModTags() {}

    private static Tags.IOptionalNamedTag<Item> forgeItemTag(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }

    private static Tags.IOptionalNamedTag<Item> itemTag(String name) {
        return ItemTags.createOptional(location(name));
    }
}
