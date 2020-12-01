package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechTEBlocks;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictRegistrar {

    public static void registerItems() {
        GregTechAPI.logger.debug("Adding GregTech items to the Ore Dictionary");

        for (BlockItems.Blocks block : BlockItems.Blocks.values()) registerOre("block", block.name(), block.getInstance());
        OreDictionary.registerOre("craftingRawMachineTier04", BlockItems.Blocks.highly_advanced_machine.getInstance());

        for (BlockItems.ConnectedBlocks block : BlockItems.ConnectedBlocks.values()) registerOre("block", block.name(), block.getInstance());

        for (BlockItems.Ores ore : BlockItems.Ores.values()) registerOre("ore", ore.name(), ore.getInstance());

        for (BlockItems.Ingots ingot : BlockItems.Ingots.values()) registerOre("ingot", ingot.name(), ingot.getInstance());

        for (BlockItems.Nuggets nugget : BlockItems.Nuggets.values()) registerOre("nugget", nugget.name(), nugget.getInstance());

        for (BlockItems.Plates plate : BlockItems.Plates.values()) registerOre("plate", plate.name(), plate.getInstance());
        OreDictionary.registerOre("plankWood", BlockItems.Plates.wood.getInstance());

        for (BlockItems.Rods rod : BlockItems.Rods.values()) registerOre("stick", rod.name(), rod.getInstance());

        for (BlockItems.Dusts dust : BlockItems.Dusts.values()) registerOre("dust", dust.name(), dust.getInstance());
        OreDictionary.registerOre("pulpWood", BlockItems.Dusts.wood.getInstance());
        OreDictionary.registerOre("dyeCyan", BlockItems.Dusts.lazurite.getInstance());
        OreDictionary.registerOre("dyeBlue", BlockItems.Dusts.sodalite.getInstance());

        for (BlockItems.Smalldusts smallDust : BlockItems.Smalldusts.values()) registerOre("dustTiny", smallDust.name(), smallDust.getInstance());

        for (BlockItems.Upgrades upgrade : BlockItems.Upgrades.values()) {
            if (upgrade.oreDict != null) OreDictionary.registerOre(upgrade.oreDict, upgrade.getInstance());
        }

        for (BlockItems.Covers cover : BlockItems.Covers.values()) {
            if (cover.oreDict != null) OreDictionary.registerOre(cover.oreDict, cover.getInstance());
        }

        for (BlockItems.Components component : BlockItems.Components.values()) {
            if (component.oreDict != null) OreDictionary.registerOre(component.oreDict, component.getInstance());
        }
        OreDictionary.registerOre("crafting100kEUStore", BlockItems.Components.lithium_battery.getInstance());
        OreDictionary.registerOre("craftingGearTier01", BlockItems.Components.gear_iron.getInstance());
        OreDictionary.registerOre("craftingGearTier01", BlockItems.Components.gear_bronze.getInstance());
        OreDictionary.registerOre("craftingGearTier02", BlockItems.Components.gear_steel.getInstance());
        OreDictionary.registerOre("craftingGearTier03", BlockItems.Components.gear_titanium.getInstance());
        OreDictionary.registerOre("craftingGearTier03", BlockItems.Components.gear_tungsten_steel.getInstance());
        OreDictionary.registerOre("craftingGearTier04", BlockItems.Components.gear_iridium.getInstance());

        for (BlockItems.Tools tool : BlockItems.Tools.values()) {
            if (tool.oreDict != null) OreDictionary.registerOre(tool.oreDict, tool.getInstance());
        }

        for (BlockItems.Files file : BlockItems.Files.values()) OreDictionary.registerOre("craftingToolFile", file.getInstance());

        for (BlockItems.Hammers hammer : BlockItems.Hammers.values()) OreDictionary.registerOre("craftingToolHardHammer", hammer.getInstance());

        for (BlockItems.Saws saw : BlockItems.Saws.values()) OreDictionary.registerOre("craftingToolSaw", saw.getInstance());

        for (BlockItems.SolderingMetals solderingMetal : BlockItems.SolderingMetals.values()) OreDictionary.registerOre("craftingToolSolderingMetal", solderingMetal.getInstance());

        for (BlockItems.ColorSprays spray : BlockItems.ColorSprays.values()) registerOre("dye", spray.name(), spray.getInstance());

        for (BlockItems.Wrenches wrench : BlockItems.Wrenches.values()) OreDictionary.registerOre("craftingToolWrench", wrench.getInstance());

        for (BlockItems.Cells cell : BlockItems.Cells.values()) registerOre("cell", cell.name(), cell.getInstance());

        for (BlockItems.NuclearCoolantPacks pack : BlockItems.NuclearCoolantPacks.values()) {
            if (pack.oreDict != null) OreDictionary.registerOre(pack.oreDict, pack.getInstance());
        }

        for (BlockItems.Armor armor : BlockItems.Armor.values()) {
            if (armor.oreDict != null) OreDictionary.registerOre(armor.oreDict, armor.getInstance());
        }

        for (BlockItems.Miscellaneous misc : BlockItems.Miscellaneous.values()) {
            if (misc.oreDict != null) OreDictionary.registerOre(misc.oreDict, misc.getInstance());
        }

        GregTechAPI.logger.debug("Adding Vanilla items to the Ore Dictionary");

        OreDictionary.registerOre("chunkLazurite", Blocks.LAPIS_BLOCK);
        OreDictionary.registerOre("stoneObsidian", Blocks.OBSIDIAN);
        OreDictionary.registerOre("stoneMossy", Blocks.MOSSY_COBBLESTONE);
        OreDictionary.registerOre("stoneMossy", Blocks.STONEBRICK);
        OreDictionary.registerOre("stoneCobble", Blocks.MOSSY_COBBLESTONE);
        OreDictionary.registerOre("stoneCobble", Blocks.COBBLESTONE);
        OreDictionary.registerOre("stoneSmooth", Blocks.STONE);
        OreDictionary.registerOre("stoneBricks", Blocks.STONEBRICK);
        OreDictionary.registerOre("stoneCracked", Blocks.STONEBRICK);
        OreDictionary.registerOre("stoneChiseled", Blocks.STONEBRICK);
        OreDictionary.registerOre("stoneSand", Blocks.SANDSTONE);
        OreDictionary.registerOre("stoneNetherrack", Blocks.NETHERRACK);
        OreDictionary.registerOre("stoneNetherBrick", Blocks.NETHER_BRICK);
        OreDictionary.registerOre("stoneEnd", Blocks.END_STONE);
        OreDictionary.registerOre("craftingRedstoneTorch", Blocks.REDSTONE_TORCH);
        OreDictionary.registerOre("craftingRedstoneTorch", Blocks.UNLIT_REDSTONE_TORCH);
        OreDictionary.registerOre("craftingCircuitTier00", Blocks.REDSTONE_TORCH);
        OreDictionary.registerOre("craftingCircuitTier00", Blocks.UNLIT_REDSTONE_TORCH);
        OreDictionary.registerOre("craftingCircuitTier00", Blocks.LEVER);
        OreDictionary.registerOre("craftingWorkBench", Blocks.CRAFTING_TABLE);
        OreDictionary.registerOre("craftingPiston", Blocks.PISTON);
        OreDictionary.registerOre("craftingPiston", Blocks.STICKY_PISTON);
        OreDictionary.registerOre("craftingChest", Blocks.CHEST);
        OreDictionary.registerOre("craftingChest", Blocks.TRAPPED_CHEST);
        OreDictionary.registerOre("paperMap", Items.MAP);
        OreDictionary.registerOre("paperMap", Items.FILLED_MAP);
        OreDictionary.registerOre("bookEmpty", Items.BOOK);
        OreDictionary.registerOre("bookWritable", Items.WRITABLE_BOOK);
        OreDictionary.registerOre("bookWritten", Items.WRITTEN_BOOK);
        OreDictionary.registerOre("bookEnchanted", Items.ENCHANTED_BOOK);
        OreDictionary.registerOre("dustSugar", Items.SUGAR);

        GregTechAPI.logger.debug("Registering IC2 items to the Ore Dictionary");

        OreDictionary.registerOre("gemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        OreDictionary.registerOre("craftingIndustrialDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        OreDictionary.registerOre("itemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        OreDictionary.registerOre("craftingUUMatter", IC2Items.getItem("misc_resource", "matter"));
        OreDictionary.registerOre("itemIridium", IC2Items.getItem("misc_resource", "iridium_ore"));
        OreDictionary.registerOre("plateAlloyIridium", IC2Items.getItem("crafting", "iridium"));
        OreDictionary.registerOre("glassReinforced", IC2Items.getItem("glass", "reinforced"));
        OreDictionary.registerOre("crafting10kEUStore", IC2Items.getItem("re_battery"));
        OreDictionary.registerOre("crafting60kEUPack", IC2Items.getItem("te", "batbox"));
        OreDictionary.registerOre("crafting300kEUPack", IC2Items.getItem("lappack"));
        OreDictionary.registerOre("crafting100kEUStore", IC2Items.getItem("energy_crystal"));
        OreDictionary.registerOre("crafting100kEUStore", IC2Items.getItem("advanced_re_battery"));
        OreDictionary.registerOre("crafting1kkEUStore", IC2Items.getItem("lapotron_crystal"));
        OreDictionary.registerOre("crafting10kCoolantStore", IC2Items.getItem("heat_storage"));
        OreDictionary.registerOre("crafting30kCoolantStore", IC2Items.getItem("tri_heat_storage"));
        OreDictionary.registerOre("crafting60kCoolantStore", IC2Items.getItem("hex_heat_storage"));
        OreDictionary.registerOre("craftingWireCopper", IC2Items.getItem("cable", "type:copper,insulation:1"));
        OreDictionary.registerOre("craftingWireGold", IC2Items.getItem("cable", "type:gold,insulation:2"));
        OreDictionary.registerOre("craftingWireIron", IC2Items.getItem("cable", "type:iron,insulation:3"));
        OreDictionary.registerOre("craftingCircuitTier02", IC2Items.getItem("crafting", "circuit"));
        OreDictionary.registerOre("craftingCircuitTier04", IC2Items.getItem("crafting", "advanced_circuit"));
        OreDictionary.registerOre("craftingRawMachineTier01", IC2Items.getItem("resource", "machine"));
        OreDictionary.registerOre("craftingRawMachineTier02", IC2Items.getItem("resource", "advanced_machine"));
        OreDictionary.registerOre("craftingChest", IC2Items.getItem("te", "personal_chest"));
        OreDictionary.registerOre("craftingPump", IC2Items.getItem("te", "pump"));
        OreDictionary.registerOre("craftingElectromagnet", IC2Items.getItem("te", "magnetizer"));
        OreDictionary.registerOre("craftingTeleporter", IC2Items.getItem("te", "teleporter"));
        OreDictionary.registerOre("craftingMacerator", IC2Items.getItem("te", "macerator"));
        OreDictionary.registerOre("craftingExtractor", IC2Items.getItem("te", "extractor"));
        OreDictionary.registerOre("craftingCompressor", IC2Items.getItem("te", "compressor"));
        OreDictionary.registerOre("craftingRecycler", IC2Items.getItem("te", "recycler"));
        OreDictionary.registerOre("craftingIronFurnace", IC2Items.getItem("te", "iron_furnace"));
        OreDictionary.registerOre("craftingInductionFurnace", IC2Items.getItem("te", "induction_furnace"));
        OreDictionary.registerOre("craftingElectricFurnace", IC2Items.getItem("te", "electric_furnace"));
        OreDictionary.registerOre("craftingGenerator", IC2Items.getItem("te", "generator"));
        OreDictionary.registerOre("craftingSolarPanel", IC2Items.getItem("te", "solar_generator"));
        OreDictionary.registerOre("craftingMVTUpgrade", IC2Items.getItem("upgrade", "transformer"));
        OreDictionary.registerOre("molecule_2o", IC2Items.getItem("fluid_cell", "ic2air"));
        //OreDictionary.registerOre("craftingRawMachineTier01", GregTechTEBlock.machine_box); TODO add machine box to oredict when added
        //OreDictionary.registerOre("craftingCircuitTier03", GregTechTEBlock.restone_circuit_block); TODO add redstone circuit block to oredict when added
        //OreDictionary.registerOre("craftingCircuitTier10", GregTechTEBlock.computer_cube); TODO add computer cube to oredict when added
        //OreDictionary.registerOre("craftingWorkBench", GregTechTEBlock.electric_workbench); TODO add electric crafting table to oredict when added
        //OreDictionary.registerOre("craftingChest", GregTechTEBlock.advanced_safe); TODO add advanced safe to oredict when added
        //OreDictionary.registerOre("craftingMacerator", GregTechTEBlock.automatic_macerator); TODO add automatic macerator to oredict when added
        //OreDictionary.registerOre("craftingRecycler", GregTechTEBlock.automatic_recycler); TODO add automatic recycler to oredict when added
        //OreDictionary.registerOre("craftingCompressor", GregTechTEBlock.automatic_compressor); TODO add automatic compressor to oredict when added
        //OreDictionary.registerOre("craftingExtractor", GregTechTEBlock.automatic_extractor); TODO add automatic extractor to oredict when added
        //OreDictionary.registerOre("craftingElectricFurnace", GregTechTEBlock.automatic_electric_furnace); TODO add automatic electric furnace to oredict when added

        if (Loader.isModLoaded("thermalexpansion")) {
            GregTechAPI.logger.debug("Registering Thermal Series items to the Ore Dictionary");

            OreDictionary.registerOre("craftingMacerator", ModHandler.getTEBlock("machine", 1)); //pulverizer
            OreDictionary.registerOre("craftingInductionFurnace", ModHandler.getTEBlock("machine", 3)); //induction smelter
            OreDictionary.registerOre("pulpWood", ModHandler.getTEItem("material", 800)); //sawdust
            OreDictionary.registerOre("glassReinforced", ModHandler.getTEBlock("glass", OreDictionary.WILDCARD_VALUE)); //hardened glass
            OreDictionary.registerOre("glassReinforced", ModHandler.getTEBlock("glass_alloy", OreDictionary.WILDCARD_VALUE)); //alloy hardened glass
            OreDictionary.registerOre("craftingSaltpeterToGunpowder", ModHandler.getTEItem("material", 772));
            OreDictionary.registerOre("craftingSulfurToGunpowder", ModHandler.getTEItem("material", 771));
        }

        if (Loader.isModLoaded("railcraft")) {
            GregTechAPI.logger.debug("Registering Railcraft items to the Ore Dictionary");

            OreDictionary.registerOre("stoneAbyssal", ModHandler.getRCBlock("generic", 8)); //abyssal stone
            OreDictionary.registerOre("stoneQuarried", ModHandler.getRCBlock("generic", 9)); //quarried stone
        }

        GregTechAPI.logger.debug("Registering GregTech machines to the Ore Dictionary");

        OreDictionary.registerOre("craftingCentrifuge", GregTechTEBlocks.getTileEntity("industrial_centrifuge"));
    }

    public static void registerOre(String base, String name, Item instance) {
        OreDictionary.registerOre(base+ CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), instance);
    }

    public static void registerOre(String base, String name, Block instance) {
        OreDictionary.registerOre(base+CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), instance);
    }
}