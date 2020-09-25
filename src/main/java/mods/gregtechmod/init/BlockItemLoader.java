package mods.gregtechmod.init;

import com.zuxelus.energycontrol.api.EnergyContolRegister;
import com.zuxelus.energycontrol.api.IItemCard;
import com.zuxelus.energycontrol.api.IItemKit;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.objects.blocks.BlockBase;
import mods.gregtechmod.objects.blocks.ConnectedBlock;
import mods.gregtechmod.objects.items.ItemSensorCard;
import mods.gregtechmod.objects.items.ItemSensorKit;
import mods.gregtechmod.objects.items.ItemSolderingMetal;
import mods.gregtechmod.objects.items.base.*;
import mods.gregtechmod.objects.items.components.ItemLithiumBattery;
import mods.gregtechmod.objects.items.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class BlockItemLoader {
    static final List<Block> BLOCKS = new LinkedList<>();
    static final List<Item> ITEMS = new LinkedList<>();

    private static Item registerItem(Item item) {
        ModContainer container;

        if ((container = Loader.instance().activeModContainer()) != null && !container.getModId().equals(GregtechMod.MODID)) throw new IllegalAccessError("only gregtech can call this");
        else if (ITEMS.contains(item)) throw new IllegalStateException("duplicate item: "+item);

        ITEMS.add(item);
        return item;
    }

    private static Block registerBlock(Block block) {
        ModContainer container;

        if ((container = Loader.instance().activeModContainer()) != null && !container.getModId().equals(GregtechMod.MODID)) throw new IllegalAccessError("only gregtech can call this");
        else if (BLOCKS.contains(block)) throw new IllegalStateException("duplicate block: "+block);

        BLOCKS.add(block);
        return block;
    }

    static void init() {
        initBlocks();
        initIngots();
        initNuggets();
        initPlates();
        initRods();
        initDusts();
        initSmallDusts();
        initCells();
        initCovers();
        initCraftingItems();
        initTools();
        initComponents();
        initNuclearComponents();
        initSpecials();
        initUpgrades();
    }

    private static void initBlocks() {
        for (BlockItems.Blocks type : BlockItems.Blocks.values()) {
            Block block = new BlockBase(Material.IRON)
                    .setRegistryName("block_"+type.name())
                    .setTranslationKey("block_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)
                    .setHardness(type.hardness)
                    .setResistance(type.resistance);
            type.setInstance(registerBlock(block));
            registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
        for (BlockItems.ConnectedBlocks type : BlockItems.ConnectedBlocks.values()) {
            Block block = new ConnectedBlock(Material.IRON)
                    .setRegistryName("block_"+type.name())
                    .setTranslationKey("block_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)
                    .setHardness(type.hardness)
                    .setResistance(type.resistance);
            type.setInstance(registerBlock(block));
            registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    private static void initIngots() {
        for (BlockItems.Ingots type : BlockItems.Ingots.values()) {
            String name = "ingot_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, type.hasEffect)
                .setFolder("ingot")
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initNuggets() {
        for (BlockItems.Nuggets type : BlockItems.Nuggets.values()) {
            String name = "nugget_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description)
                    .setFolder("nugget")
                    .setRegistryName(name)
                    .setTranslationKey(name)
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initPlates() {
        for (BlockItems.Plates type : BlockItems.Plates.values()) {
            String name = "plate_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description)
                    .setFolder("plate")
                    .setRegistryName(name)
                    .setTranslationKey(name)
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initRods() {
        for (BlockItems.Rods type : BlockItems.Rods.values()) {
            String name = "rod_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description)
                    .setFolder("rod")
                    .setRegistryName(name)
                    .setTranslationKey(name)
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initDusts() {
        for (BlockItems.Dusts type : BlockItems.Dusts.values()) {
            String name = "dust_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, type.hasEffect)
                    .setFolder("dust")
                    .setRegistryName(name)
                    .setTranslationKey(name)
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initSmallDusts() {
        for (BlockItems.Smalldusts type : BlockItems.Smalldusts.values()) {
            String name = "smalldust_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, type.hasEffect)
                    .setFolder("smalldust")
                    .setRegistryName(name)
                    .setTranslationKey(name)
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initUpgrades() {
        for (BlockItems.Upgrades type : BlockItems.Upgrades.values()) {
            type.setInstance((IGtUpgradeItem) registerItem(new ItemUpgrade(type.name(), type.description, type.type, type.maxCount, type.requiredTier, type.condition, type.onInsert, type.onUpdate)
                    .setFolder("upgrade")
                    .setRegistryName(type.name())
                    .setTranslationKey(type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initCovers() {
        for (BlockItems.Covers type : BlockItems.Covers.values()) {
            type.setInstance(registerItem(new ItemCover(type.name(), type.description)
                    .setFolder("coveritem")
                    .setRegistryName(type.name())
                    .setTranslationKey(type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initCraftingItems() {
        BlockItems.Crafting.mortar_iron.setInstance(registerItem(new ItemMortar("iron", 63))
                .setRegistryName("mortar_iron")
                .setTranslationKey("mortar_iron")
                .setCreativeTab(GregtechMod.GREGTECH_TAB));
        BlockItems.Crafting.mortar_flint.setInstance(registerItem(new ItemBase("mortar_flint", "Used to turn ingots into dust")
                .setFolder("crafting")
                .setRegistryName("mortar_flint")
                .setTranslationKey("mortar_flint")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
    }

    private static void initTools() {
        BlockItems.Tools.drill_advanced.setInstance(registerItem(new ItemDrillAdvanced()));
        BlockItems.Tools.saw_advanced.setInstance(registerItem(new ItemSawAdvanced()));
        BlockItems.Tools.wrench_advanced.setInstance(registerItem(new ItemWrenchAdvanced()));
        BlockItems.Tools.crowbar.setInstance(registerItem(new ItemCrowbar("crowbar", "To remove covers form machines", 256, 6)
                .setRegistryName("crowbar")
                .setTranslationKey("crowbar")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Tools.screwdriver.setInstance(registerItem(new ItemScrewdriver("screwdriver", "To screw covers on machines \nCan switch the design of certain blocks \nCan rotate repeaters and comparators", 256, 4)
                .setRegistryName("screwdriver")
                .setTranslationKey("screwdriver")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Tools.rock_cutter.setInstance(registerItem(new ItemRockCutter()
                .setRegistryName("rock_cutter")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Tools.rubber_hammer.setInstance(registerItem(new ItemRubberHammer("rubber", "To give a machine a soft whack", 128, 4)
                .setRegistryName("hammer_rubber")
                .setTranslationKey("hammer_rubber")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Tools.soldering_tool.setInstance(registerItem(new ItemSolderingTool("soldering_tool", "To repair and construct circuitry", 10)
                .setRegistryName("soldering_tool")
                .setTranslationKey("soldering_tool")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Tools.tesla_staff.setInstance(registerItem(new ItemTeslaStaff()));
        BlockItems.Tools.scanner.setInstance(registerItem(new ItemScanner("scanner", "Tricorder", 100000, 100, 1)
                .setRegistryName("scanner")
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Tools.debug_scanner.setInstance(registerItem(new ItemDebugScanner()));

        for (BlockItems.Wrenches type : BlockItems.Wrenches.values()) {
            type.setInstance(registerItem(new ItemWrench("wrench_"+type.name(), type.durability)
                    .setRegistryName("wrench_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }

        for (BlockItems.JackHammers type : BlockItems.JackHammers.values()) {
            type.setInstance(registerItem(new ItemJackHammer("jack_hammer_"+type.name(), type.operationEnergyCost, type.maxCharge, type.tier, type.transferLimit, type.efficiency)
                .setRegistryName("jack_hammer_"+type.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }

        for (BlockItems.Hammers type : BlockItems.Hammers.values()) {
            type.setInstance(registerItem(new ItemHardHammer(type.name(), "To give a machine a hard whack", type.durability, type.entityDamage, type.toolMaterial)
                    .setRegistryName("hammer_"+type.name())
                    .setTranslationKey("hammer_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }

        for (BlockItems.Saws type : BlockItems.Saws.values()) {
            type.setInstance(registerItem(new ItemSaw(type.name(), type.durability, type.efficiency, type.entityDamage, type.toolMaterial)
                    .setRegistryName("saw_"+type.name())
                    .setTranslationKey("saw_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }

        for (BlockItems.SolderingMetals type : BlockItems.SolderingMetals.values()) {
            type.setInstance(registerItem(new ItemSolderingMetal(type.name(), type.durability)
                    .setRegistryName("soldering_"+type.name())
                    .setTranslationKey("soldering_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }

        for (BlockItems.Files type : BlockItems.Files.values()) {
            type.setInstance(registerItem(new ItemFile(type.name(), type.durability, type.entityDamage, Item.ToolMaterial.IRON)
                    .setRegistryName("file_"+type.name())
                    .setTranslationKey("file_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initComponents() {
        BlockItems.Components.data_control_circuit.setInstance(registerItem(new ItemCover(BlockItems.Components.data_control_circuit.name(), BlockItems.Components.data_control_circuit.coverName, BlockItems.Components.data_control_circuit.description)
            .setFolder("component")
            .setRegistryName(BlockItems.Components.data_control_circuit.name())
            .setTranslationKey(BlockItems.Components.data_control_circuit.name())
            .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Components.energy_flow_circuit.setInstance(registerItem(new ItemCover(BlockItems.Components.energy_flow_circuit.name(), BlockItems.Components.energy_flow_circuit.coverName, BlockItems.Components.energy_flow_circuit.description)
                .setFolder("component")
                .setRegistryName(BlockItems.Components.energy_flow_circuit.name())
                .setTranslationKey(BlockItems.Components.energy_flow_circuit.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Components.lithium_battery.setInstance(registerItem(new ItemLithiumBattery()));
    }

    private static void initNuclearComponents() {
        for (BlockItems.NuclearCoolantPacks type : BlockItems.NuclearCoolantPacks.values()) {
            type.setInstance(registerItem(new ItemNuclearHeatStorage(type.name(), type.heatStorage)
                    .setRegistryName(type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
        for (BlockItems.NuclearFuelRods type : BlockItems.NuclearFuelRods.values()) {
            type.setInstance(registerItem(new ItemNuclearFuelRod("cell_"+type.name(), type.cells, type.duration, type.energy, type.radiation, type.heat, type.depletedStack)
                    .setRegistryName("cell_"+type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initSpecials() {
        BlockItems.Specials.greg_coin.setInstance(registerItem(new ItemBase(BlockItems.Specials.greg_coin.name(), "A minimalist GregTech logo on a coin")
            .setRegistryName(BlockItems.Specials.greg_coin.name())
            .setTranslationKey(BlockItems.Specials.greg_coin.name())
            .setCreativeTab(GregtechMod.GREGTECH_TAB)));

        if (Loader.isModLoaded("energycontrol")) {
            Item sensorCard = new ItemSensorCard();
            Item sensorKit = new ItemSensorKit();
            BlockItems.Specials.sensor_kit.setInstance(sensorCard);
            BlockItems.Specials.sensor_card.setInstance(sensorKit);
            EnergyContolRegister.registerCard((IItemCard) sensorCard);
            EnergyContolRegister.registerKit((IItemKit) sensorKit);
        }
    }

    private static void initCells() {
        for (BlockItems.Cells type : BlockItems.Cells.values()) {
            String aName = "cell_"+type.name();
            type.setInstance(registerItem(new ItemBase(type.name(), type.description)
                .setFolder("cell")
                .setRegistryName(aName)
                .setTranslationKey(aName)
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }
}
