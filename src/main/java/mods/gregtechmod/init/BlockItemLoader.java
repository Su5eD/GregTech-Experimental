package mods.gregtechmod.init;

import com.zuxelus.energycontrol.api.EnergyContolRegister;
import com.zuxelus.energycontrol.api.IItemCard;
import com.zuxelus.energycontrol.api.IItemKit;
import mods.gregtechmod.api.BlockItems;
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
            Block block = new BlockBase("block_"+type.name(), Material.IRON, type.hardness, type.resistance);
            type.setInstance(registerBlock(block));
            registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
        for (BlockItems.ConnectedBlocks type : BlockItems.ConnectedBlocks.values()) {
            Block block = new ConnectedBlock("block_"+type.name(), Material.IRON, type.hardness, type.resistance);
            type.setInstance(registerBlock(block));
            registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    private static void initIngots() {
        for (BlockItems.Ingots type : BlockItems.Ingots.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "ingot", type.hasEffect)));
        }
    }

    private static void initNuggets() {
        for (BlockItems.Nuggets type : BlockItems.Nuggets.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "nugget")));
        }
    }

    private static void initPlates() {
        for (BlockItems.Plates type : BlockItems.Plates.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "plate")));
        }
    }

    private static void initRods() {
        for (BlockItems.Rods type : BlockItems.Rods.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "rod")));
        }
    }

    private static void initDusts() {
        for (BlockItems.Dusts type : BlockItems.Dusts.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "dust", type.hasEffect)));
        }
    }

    private static void initSmallDusts() {
        for (BlockItems.Smalldusts type : BlockItems.Smalldusts.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "smalldust", type.hasEffect)));
        }
    }

    private static void initUpgrades() {
        for (BlockItems.Upgrades upgrade : BlockItems.Upgrades.values()) {
            ItemUpgrade item = new ItemUpgrade(upgrade.name(), upgrade.description, upgrade.type, upgrade.maxCount, upgrade.requiredTier, upgrade.condition, upgrade.onInsert, upgrade.onUpdate);
            registerItem(item);
            upgrade.setInstance(item);
        }
    }

    private static void initCovers() {
        for (BlockItems.Covers type : BlockItems.Covers.values()) {
            ItemCover item = new ItemCover(type.name(), type.description);
            registerItem(item);
            type.setInstance(item);
        }
    }

    private static void initCraftingItems() {
        BlockItems.Crafting.mortar_iron.setInstance(registerItem(new ItemMortar("iron", 63)));
        BlockItems.Crafting.mortar_flint.setInstance(registerItem(new ItemBase("mortar_flint", "Used to turn ingots into dust").setFolder("crafting")));
    }

    private static void initTools() {
        BlockItems.Tools.drill_advanced.setInstance(registerItem(new ItemDrillAdvanced()));
        BlockItems.Tools.saw_advanced.setInstance(registerItem(new ItemSawAdvanced()));
        BlockItems.Tools.wrench_advanced.setInstance(registerItem(new ItemWrenchAdvanced()));
        BlockItems.Tools.crowbar.setInstance(registerItem(new ItemCrowbar()));
        BlockItems.Tools.screwdriver.setInstance(registerItem(new ItemScrewdriver()));
        BlockItems.Tools.rock_cutter.setInstance(registerItem(new ItemRockCutter()));
        BlockItems.Tools.rubber_hammer.setInstance(registerItem(new ItemRubberHammer()));
        BlockItems.Tools.soldering_tool.setInstance(registerItem(new ItemSolderingTool()));
        BlockItems.Tools.tesla_staff.setInstance(registerItem(new ItemTeslaStaff()));
        BlockItems.Tools.scanner.setInstance(registerItem(new ItemScanner()));
        BlockItems.Tools.debug_scanner.setInstance(registerItem(new ItemDebugScanner()));

        for (BlockItems.Wrenches type : BlockItems.Wrenches.values()) {
            ItemWrench item = new ItemWrench(type.name(), type.durability);
            registerItem(item);
            type.setInstance(item);
        }

        for (BlockItems.JackHammers type : BlockItems.JackHammers.values()) {
            ItemJackHammer item = new ItemJackHammer(type.name(), type.operationEnergyCost, type.maxCharge, type.tier, type.transferLimit, type.efficiency);
            registerItem(item);
            type.setInstance(item);
        }

        for (BlockItems.Hammers type : BlockItems.Hammers.values()) {
            ItemHammer item = new ItemHardHammer(type.name(), type.durability, type.entityDamage, type.toolMaterial);
            registerItem(item);
            type.setInstance(item);
        }

        for (BlockItems.Saws type : BlockItems.Saws.values()) {
            ItemSaw item = new ItemSaw(type.name(), type.durability, type.efficiency, type.entityDamage, type.toolMaterial);
            registerItem(item);
            type.setInstance(item);
        }

        for (BlockItems.SolderingMetals type : BlockItems.SolderingMetals.values()) {
            ItemSolderingMetal item = new ItemSolderingMetal(type.name(), type.durability);
            registerItem(item);
            type.setInstance(item);
        }

        for (BlockItems.Files type : BlockItems.Files.values()) {
            ItemFile item = new ItemFile(type.name(), type.durability, type.entityDamage, Item.ToolMaterial.IRON);
            registerItem(item);
            type.setInstance(item);
        }
    }

    private static void initComponents() {
        BlockItems.Components.data_control_circuit.setInstance(registerItem(new ItemCover(BlockItems.Components.data_control_circuit.name(), BlockItems.Components.data_control_circuit.coverName, BlockItems.Components.data_control_circuit.description, "component")));
        BlockItems.Components.energy_flow_circuit.setInstance(registerItem(new ItemCover(BlockItems.Components.energy_flow_circuit.name(), BlockItems.Components.energy_flow_circuit.coverName, BlockItems.Components.energy_flow_circuit.description, "component")));
        BlockItems.Components.lithium_battery.setInstance(registerItem(new ItemLithiumBattery()));
    }

    private static void initNuclearComponents() {
        for (BlockItems.NuclearCoolantPacks type : BlockItems.NuclearCoolantPacks.values()) {
            type.setInstance(registerItem(new ItemNuclearHeatStorage(type.name(), type.heatStorage)));
        }
        for (BlockItems.NuclearFuelRods type : BlockItems.NuclearFuelRods.values()) {
            type.setInstance(registerItem(new ItemNuclearFuelRod("cell_"+type.name(), type.cells, type.duration, type.energy, type.radiation, type.heat, type.depletedStack)));
        }
    }

    private static void initSpecials() {
        BlockItems.Specials.greg_coin.setInstance(registerItem(new ItemBase(BlockItems.Specials.greg_coin.name(), "A minimalist GregTech logo on a coin")));

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
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "cell")));
        }
    }
}
