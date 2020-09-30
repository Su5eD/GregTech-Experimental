package mods.gregtechmod.init;

import com.zuxelus.energycontrol.api.EnergyContolRegister;
import com.zuxelus.energycontrol.api.IItemCard;
import com.zuxelus.energycontrol.api.IItemKit;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.objects.blocks.BlockBase;
import mods.gregtechmod.objects.blocks.BlockLightSource;
import mods.gregtechmod.objects.blocks.ConnectedBlock;
import mods.gregtechmod.objects.items.*;
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
        initArmor();
        initMiscellaneous();
        initUpgrades();
    }

    private static void initBlocks() {
        BlockItems.lightSource = registerBlock(new BlockLightSource()
                .setRegistryName("light_source")
                .setTranslationKey("light_source"));

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
                .setFolder("component")
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
                .setFolder("tool")
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

        BlockItems.Components.turbine_rotor_bronze.setInstance(registerItem(new ItemBase(BlockItems.Components.turbine_rotor_bronze.name(), BlockItems.Components.turbine_rotor_bronze.description, false)
                .setFolder("component")
                .setEnchantable(false)
                .setRegistryName(BlockItems.Components.turbine_rotor_bronze.name())
                .setTranslationKey(BlockItems.Components.turbine_rotor_bronze.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)
                .setMaxDamage(15000)
                .setMaxStackSize(1)
                .setNoRepair()));
        BlockItems.Components.turbine_rotor_steel.setInstance(registerItem(new ItemBase(BlockItems.Components.turbine_rotor_steel.name(), BlockItems.Components.turbine_rotor_steel.description, false)
                .setFolder("component")
                .setEnchantable(false)
                .setRegistryName(BlockItems.Components.turbine_rotor_steel.name())
                .setTranslationKey(BlockItems.Components.turbine_rotor_steel.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)
                .setMaxDamage(10000)
                .setMaxStackSize(1)
                .setNoRepair()));
        BlockItems.Components.turbine_rotor_magnalium.setInstance(registerItem(new ItemBase(BlockItems.Components.turbine_rotor_magnalium.name(), BlockItems.Components.turbine_rotor_magnalium.description, false)
                .setFolder("component")
                .setEnchantable(false)
                .setRegistryName(BlockItems.Components.turbine_rotor_magnalium.name())
                .setTranslationKey(BlockItems.Components.turbine_rotor_magnalium.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)
                .setMaxDamage(10000)
                .setMaxStackSize(1)
                .setNoRepair()));
        BlockItems.Components.turbine_rotor_tungstensteel.setInstance(registerItem(new ItemBase(BlockItems.Components.turbine_rotor_tungstensteel.name(), BlockItems.Components.turbine_rotor_tungstensteel.description, false)
                .setFolder("component")
                .setEnchantable(false)
                .setRegistryName(BlockItems.Components.turbine_rotor_tungstensteel.name())
                .setTranslationKey(BlockItems.Components.turbine_rotor_tungstensteel.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)
                .setMaxDamage(30000)
                .setMaxStackSize(1)
                .setNoRepair()));
        BlockItems.Components.turbine_rotor_carbon.setInstance(registerItem(new ItemBase(BlockItems.Components.turbine_rotor_carbon.name(), BlockItems.Components.turbine_rotor_carbon.description, false)
                .setFolder("component")
                .setEnchantable(false)
                .setRegistryName(BlockItems.Components.turbine_rotor_carbon.name())
                .setTranslationKey(BlockItems.Components.turbine_rotor_carbon.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)
                .setMaxDamage(2500)
                .setMaxStackSize(1)
                .setNoRepair()));
        BlockItems.Components.lava_filter.setInstance(registerItem(new ItemBase(BlockItems.Components.lava_filter.name(), BlockItems.Components.lava_filter.description, false)
                .setFolder("component")
                .setEnchantable(false)
                .setRegistryName(BlockItems.Components.lava_filter.name())
                .setTranslationKey(BlockItems.Components.lava_filter.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)
                .setMaxDamage(100)
                .setMaxStackSize(1)
                .setNoRepair()));

        for (BlockItems.Components type : BlockItems.Components.values()) {
            if (type.autoInit) {
                type.setInstance(registerItem(new ItemBase(type.name(), type.description)
                        .setFolder("component")
                        .setRegistryName(type.name())
                        .setTranslationKey(type.name())
                        .setCreativeTab(GregtechMod.GREGTECH_TAB)));
            }
        }
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

    private static void initArmor() {
        for (BlockItems.Armor type : BlockItems.Armor.values()) {
            type.setInstance(registerItem(new ItemArmorElectricBase(type.name(), type.slot, type.maxCharge, type.transferLimit, type.tier, type.damageEnergyCost, type.absorbtionDamage, type.chargeProvider, type.perks)
                    .setFolder("armor")
                    .setRegistryName(type.name())
                    .setTranslationKey(type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }
    }

    private static void initMiscellaneous() {
        BlockItems.Miscellaneous.destructorpack.setInstance(registerItem(new ItemDestructorPack(BlockItems.Miscellaneous.destructorpack.name(), BlockItems.Miscellaneous.destructorpack.description)
                .setRegistryName(BlockItems.Miscellaneous.destructorpack.name())
                .setTranslationKey(BlockItems.Miscellaneous.destructorpack.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        BlockItems.Miscellaneous.lapotronic_energy_orb.setInstance(registerItem(new ItemElectricBase(BlockItems.Miscellaneous.lapotronic_energy_orb.name(), null, 100000000, 8192, 5)
                .setRegistryName(BlockItems.Miscellaneous.lapotronic_energy_orb.name())
                .setCreativeTab(GregtechMod.GREGTECH_TAB)));

        for (BlockItems.Miscellaneous type : BlockItems.Miscellaneous.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description)
                    .setRegistryName(type.name())
                    .setTranslationKey(type.name())
                    .setCreativeTab(GregtechMod.GREGTECH_TAB)));
        }

        if (Loader.isModLoaded("energycontrol")) {
            BlockItems.sensor_kit = new ItemSensorKit();
            BlockItems.sensor_card = new ItemSensorCard();
            EnergyContolRegister.registerCard((IItemCard) BlockItems.sensor_card);
            EnergyContolRegister.registerKit((IItemKit) BlockItems.sensor_kit);
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
