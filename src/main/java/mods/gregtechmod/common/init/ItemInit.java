package mods.gregtechmod.common.init;

import mods.gregtechmod.common.objects.items.GtUpgradeItem;
import mods.gregtechmod.common.objects.items.base.ItemBase;
import mods.gregtechmod.common.objects.items.base.ItemCover;
import mods.gregtechmod.common.objects.items.tools.ItemChainsawAdvanced;
import mods.gregtechmod.common.objects.items.tools.ItemCrowbar;
import mods.gregtechmod.common.objects.items.tools.ItemDrillAdvanced;
import mods.gregtechmod.common.objects.items.tools.ItemScrewdriver;
import net.minecraft.item.Item;

import java.util.SortedMap;
import java.util.TreeMap;

public class ItemInit {
    public static final SortedMap<String,Item> ITEMS = new TreeMap<>();
    //INGOTS
    public static final Item INGOT_COPPER = new ItemBase("ingot_copper");
    //TOOLS
    public static final ItemDrillAdvanced DRILL_ADVANCED = new ItemDrillAdvanced();
    public static final ItemChainsawAdvanced CHAINSAW_ADVANCED = new ItemChainsawAdvanced();
    public static final ItemCrowbar CROWBAR = new ItemCrowbar();
    public static final ItemScrewdriver SCREWDRIVER = new ItemScrewdriver();
    //COMPONENTS and COVERS
    //TODO: Localize descriptions
    public static final Item GT_UPGRADE = new GtUpgradeItem<>();
    public static final ItemCover DRAIN = new ItemCover("drain", "Collects liquids and rain");
    public static final ItemCover ACTIVE_DETECTOR = new ItemCover("active_detector", "Emits redstone if the machine has work");
    public static final ItemCover EU_METER = new ItemCover("eu_meter", "Outputs redstone depending on stored energy");
    public static final ItemCover ITEM_METER = new ItemCover("item_meter", "Outputs redstone depending on stored items");
    public static final ItemCover LIQUID_METER = new ItemCover("liquid_meter", "Outputs redstone depending on stored liquids");
    public static final ItemCover MACHINE_CONTROLLER = new ItemCover("machine_controller", "This can control mahcines with redstone");
    public static final ItemCover SCREEN = new ItemCover("screen", "Displays things");
    public static final ItemCover SOLAR_PANEL = new ItemCover("solar_panel", "Makes energy from the Sun");
    public static final ItemCover SOLAR_PANEL_LV = new ItemCover("solar_panel_lv", "solar_panel", "Makes energy from the Sun at 8EU/t");
    public static final ItemCover SOLAR_PANEL_MV = new ItemCover("solar_panel_mv", "solar_panel", "Makes energy from the Sun at 64EU/t");
    public static final ItemCover SOLAR_PANEL_HV = new ItemCover("solar_panel_hv", "solar_panel", "Makes energy from the Sun at 512EU/t");
    public static final ItemCover CRAFTING = new ItemCover("crafting", "A workbench on a cover");
    public static final ItemCover CONVEYOR = new ItemCover("conveyor", "Moves items around");
    public static final ItemCover PUMP_MODULE = new ItemCover("pump_module", "Moves liquids around");
    public static final ItemCover ITEM_VALVE = new ItemCover("item_valve", "Moves items and liquids at once!");

    //LOGO a.k.a greg coin (very rare)
    public static final Item GREG_COIN = new ItemBase("greg_coin", "very rare");
}
