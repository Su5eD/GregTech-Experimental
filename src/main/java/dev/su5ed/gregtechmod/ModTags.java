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
    
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_TIER_5 = itemTag("circuit_tier_5");
    public static final Tags.IOptionalNamedTag<Item> CIRCUIT_TIER_8 = itemTag("circuit_tier_8");
    
    private ModTags() {}
    
    private static Tags.IOptionalNamedTag<Item> forgeItemTag(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
    
    private static Tags.IOptionalNamedTag<Item> itemTag(String name) {
        return ItemTags.createOptional(location(name));
    }
}
