package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Locale;

public class OreDictRegistrar {

    public static void registerItems() {
        GregTechAPI.logger.debug("Adding certain items to the OreDict unification blacklist");

        OreDictUnificator.addToBlacklist(IC2Items.getItem("crafting", "industrial_diamond"));

        GregTechAPI.logger.debug("Registering GregTech items to the Ore Dictionary");

        for (BlockItems.Block block : BlockItems.Block.values()) registerOre("block", block.name().toLowerCase(Locale.ROOT), block.getInstance());
        registerOre("craftingRawMachineTier04", BlockItems.Block.HIGHLY_ADVANCED_MACHINE.getInstance());

        for (BlockItems.Ore ore : BlockItems.Ore.values()) registerOre("ore", ore.name().toLowerCase(Locale.ROOT), ore.getInstance());

        for (BlockItems.Ingot ingot : BlockItems.Ingot.values()) registerOre("ingot", ingot.name().toLowerCase(Locale.ROOT), ingot.getInstance());

        for (BlockItems.Nugget nugget : BlockItems.Nugget.values()) registerOre("nugget", nugget.name().toLowerCase(Locale.ROOT), nugget.getInstance());

        for (BlockItems.Plate plate : BlockItems.Plate.values()) registerOre("plate", plate.name().toLowerCase(Locale.ROOT), plate.getInstance());
        OreDictUnificator.add("pulpWood", BlockItems.Dust.WOOD.getInstance());
        registerOre("plankWood", BlockItems.Plate.WOOD.getInstance());

        for (BlockItems.Rod rod : BlockItems.Rod.values()) registerOre("stick", rod.name().toLowerCase(Locale.ROOT), rod.getInstance());

        for (BlockItems.Dust dust : BlockItems.Dust.values()) registerOre("dust", dust.name().toLowerCase(Locale.ROOT), dust.getInstance());
        registerOre("dyeCyan", BlockItems.Dust.LAZURITE.getInstance());
        registerOre("dyeBlue", BlockItems.Dust.SODALITE.getInstance());

        for (BlockItems.Smalldust smallDust : BlockItems.Smalldust.values()) registerOre("dustTiny", smallDust.name().toLowerCase(Locale.ROOT), smallDust.getInstance());

        for (BlockItems.Upgrade upgrade : BlockItems.Upgrade.values()) {
            if (upgrade.oreDict != null) registerOre(upgrade.oreDict, upgrade.getInstance());
        }

        for (BlockItems.Cover cover : BlockItems.Cover.values()) {
            if (cover.oreDict != null) registerOre(cover.oreDict, cover.getInstance());
        }

        for (BlockItems.Component component : BlockItems.Component.values()) {
            if (component.oreDict != null) registerOre(component.oreDict, component.getInstance());
        }
        registerOre("crafting100kEUStore", BlockItems.Component.LITHIUM_BATTERY.getInstance());
        registerOre("craftingGearTier01", BlockItems.Component.GEAR_IRON.getInstance());
        registerOre("craftingGearTier01", BlockItems.Component.GEAR_BRONZE.getInstance());
        registerOre("craftingGearTier02", BlockItems.Component.GEAR_STEEL.getInstance());
        registerOre("craftingGearTier03", BlockItems.Component.GEAR_TITANIUM.getInstance());
        registerOre("craftingGearTier03", BlockItems.Component.GEAR_TUNGSTEN_STEEL.getInstance());
        registerOre("craftingGearTier04", BlockItems.Component.GEAR_IRIDIUM.getInstance());

        for (BlockItems.Tool tool : BlockItems.Tool.values()) {
            if (tool.oreDict != null) registerOre(tool.oreDict, tool.getInstance());
        }

        for (BlockItems.File file : BlockItems.File.values()) registerOre("craftingToolFile", file.getInstance());

        for (BlockItems.Hammer hammer : BlockItems.Hammer.values()) registerOre("craftingToolHardHammer", hammer.getInstance());

        for (BlockItems.Saw saw : BlockItems.Saw.values()) registerOre("craftingToolSaw", saw.getInstance());

        for (BlockItems.SolderingMetal solderingMetal : BlockItems.SolderingMetal.values()) registerOre("craftingToolSolderingMetal", solderingMetal.getInstance());

        for (BlockItems.ColorSpray spray : BlockItems.ColorSpray.values()) registerOre("dye", spray.name().toLowerCase(Locale.ROOT), spray.getInstance());

        for (BlockItems.Wrenche wrench : BlockItems.Wrenche.values()) registerOre("craftingToolWrench", wrench.getInstance());

        for (BlockItems.Cell cell : BlockItems.Cell.values()) registerOre("cell", cell.name().toLowerCase(Locale.ROOT), cell.getInstance());

        for (BlockItems.NuclearCoolantPack pack : BlockItems.NuclearCoolantPack.values()) {
            if (pack.oreDict != null) registerOre(pack.oreDict, pack.getInstance());
        }

        for (BlockItems.Armor armor : BlockItems.Armor.values()) {
            if (armor.oreDict != null) registerOre(armor.oreDict, armor.getInstance());
        }

        for (BlockItems.Miscellaneous misc : BlockItems.Miscellaneous.values()) {
            if (misc.oreDict != null) registerOre(misc.oreDict, misc.getInstance());
        }

        GregTechAPI.logger.debug("Adding vanilla items to the Ore Dictionary");

        registerOre("chunkLazurite", Blocks.LAPIS_BLOCK);
        registerOre("stoneObsidian", Blocks.OBSIDIAN);
        registerOre("stoneMossy", Blocks.MOSSY_COBBLESTONE);
        registerOre("stoneMossy", Blocks.STONEBRICK);
        registerOre("stoneCobble", Blocks.MOSSY_COBBLESTONE);
        registerOre("stoneCobble", Blocks.COBBLESTONE);
        registerOre("stoneSmooth", Blocks.STONE);
        registerOre("stoneBricks", Blocks.STONEBRICK);
        registerOre("stoneCracked", Blocks.STONEBRICK);
        registerOre("stoneChiseled", Blocks.STONEBRICK);
        registerOre("stoneNetherrack", Blocks.NETHERRACK);
        registerOre("stoneNetherBrick", Blocks.NETHER_BRICK);
        registerOre("stoneRedrock", Blocks.RED_SANDSTONE);
        registerOre("stoneEnd", Blocks.END_STONE);
        registerOre("craftingRedstoneTorch", Blocks.REDSTONE_TORCH);
        registerOre("craftingRedstoneTorch", Blocks.UNLIT_REDSTONE_TORCH);
        registerOre("craftingCircuitTier00", Blocks.REDSTONE_TORCH);
        registerOre("craftingCircuitTier00", Blocks.UNLIT_REDSTONE_TORCH);
        registerOre("craftingCircuitTier00", Blocks.LEVER);
        registerOre("craftingWorkBench", Blocks.CRAFTING_TABLE);
        registerOre("craftingPiston", Blocks.PISTON);
        registerOre("craftingPiston", Blocks.STICKY_PISTON);
        registerOre("craftingChest", Blocks.CHEST);
        registerOre("craftingChest", Blocks.TRAPPED_CHEST);
        registerOre("paperMap", Items.MAP);
        registerOre("paperMap", Items.FILLED_MAP);
        registerOre("bookEmpty", Items.BOOK);
        registerOre("bookWritable", Items.WRITABLE_BOOK);
        registerOre("bookWritten", Items.WRITTEN_BOOK);
        registerOre("bookEnchanted", Items.ENCHANTED_BOOK);

        GregTechAPI.logger.debug("Registering unification entries");

        OreDictUnificator.add("oreCoal", Blocks.COAL_ORE);
        OreDictUnificator.add("oreIron", Blocks.IRON_ORE);
        OreDictUnificator.add("oreLapis", Blocks.LAPIS_ORE);
        OreDictUnificator.add("oreRedstone", Blocks.REDSTONE_ORE);
        OreDictUnificator.add("oreRedstone", Blocks.LIT_REDSTONE_ORE);
        OreDictUnificator.add("oreGold", Blocks.GOLD_ORE);
        OreDictUnificator.add("oreDiamond", Blocks.DIAMOND_ORE);
        OreDictUnificator.add("oreEmerald", Blocks.EMERALD_ORE);
        OreDictUnificator.add("oreNetherQuartz", Blocks.QUARTZ_ORE);
        OreDictUnificator.add("gemDiamond", Items.DIAMOND);
        OreDictUnificator.add("gemEmerald", Items.EMERALD);
        OreDictUnificator.add("nuggetGold", Items.GOLD_NUGGET);
        OreDictUnificator.add("ingotGold", Items.GOLD_INGOT);
        OreDictUnificator.add("ingotIron", Items.IRON_INGOT);
        OreDictUnificator.add("ingotTin", IC2Items.getItem("ingot", "tin"));
        OreDictUnificator.add("ingotCopper", IC2Items.getItem("ingot", "copper"));
        OreDictUnificator.add("ingotBronze", IC2Items.getItem("ingot", "bronze"));
        OreDictUnificator.add("ingotSteel", IC2Items.getItem("ingot", "steel"));
        OreDictUnificator.add("plateAlloyIridium", IC2Items.getItem("crafting", "iridium"));
        OreDictUnificator.add("plateAlloyAdvanced", IC2Items.getItem("crafting", "alloy"));
        OreDictUnificator.add("plateAlloyCarbon", IC2Items.getItem("crafting", "carbon_plate"));
        OreDictUnificator.add("plateDenseCopper", IC2Items.getItem("plate", "dense_copper"));
        OreDictUnificator.add("dustLapis", new ItemStack(Items.DYE, 1, 4));
        OreDictUnificator.add("dustRedstone", Items.REDSTONE);
        OreDictUnificator.add("dustGunpowder", Items.GUNPOWDER);
        OreDictUnificator.add("dustGlowstone", Items.GLOWSTONE_DUST);
        OreDictUnificator.add("blockIron", Blocks.IRON_BLOCK);
        OreDictUnificator.add("blockGold", Blocks.GOLD_BLOCK);
        OreDictUnificator.add("blockDiamond", Blocks.GOLD_BLOCK);
        OreDictUnificator.add("blockEmerald", Blocks.EMERALD_BLOCK);
        OreDictUnificator.add("blockLapis", Blocks.LAPIS_BLOCK);
        OreDictUnificator.add("blockRedstone", Blocks.REDSTONE_BLOCK);
        OreDictUnificator.add("blockCopper", IC2Items.getItem("resource", "copper_block"));
        OreDictUnificator.add("blockTin", IC2Items.getItem("resource", "tin_block"));
        OreDictUnificator.add("blockBronze", IC2Items.getItem("resource", "bronze_block"));
        OreDictUnificator.add("blockUranium", IC2Items.getItem("resource", "uranium_block"));
        OreDictUnificator.add("blockSilver", IC2Items.getItem("resource", "silver_block"));
        OreDictUnificator.add("blockSteel", IC2Items.getItem("resource", "steel_block"));
        OreDictUnificator.add("blockLead", IC2Items.getItem("resource", "lead_block"));
        OreDictUnificator.add("itemRubber", IC2Items.getItem("crafting", "rubber"));
        OreDictUnificator.add("dustSugar", Items.SUGAR);
        OreDictUnificator.add("stickWood", Items.STICK);
        OreDictUnificator.add("crystalNetherQuartz", Items.QUARTZ);

        GregTechAPI.logger.debug("Registering other mods' unification targets");

        if (GregTechConfig.UNIFICATION.forestry && Loader.isModLoaded("forestry")) {
            OreDictUnificator.override("ingotCopper", ModHandler.getFRItem("ingot_copper"));
            OreDictUnificator.override("ingotTin", ModHandler.getFRItem("ingot_tin"));
            OreDictUnificator.override("ingotBronze", ModHandler.getFRItem("ingot_bronze"));
            OreDictUnificator.override("dustAsh", ModHandler.getFRItem("ash"));
            OreDictUnificator.override("dustWood", ModHandler.getFRItem("wood_pulp"));
            OreDictUnificator.override("pulpWood", ModHandler.getFRItem("wood_pulp"));
        }

        if (Loader.isModLoaded("railcraft")) {
            registerOre("stoneAbyssal", ModHandler.getRCItem("generic", 8));
            registerOre("stoneQuarried", ModHandler.getRCItem("generic", 9));

            OreDictUnificator.override("plateIron", ModHandler.getRCItem("plate", 0));
            OreDictUnificator.override("plateSteel", ModHandler.getRCItem("plate", 1));

            if (GregTechConfig.UNIFICATION.railcraft) {
                OreDictUnificator.override("nuggetSteel", ModHandler.getRCItem("nugget", 0));
                OreDictUnificator.override("ingotSteel", ModHandler.getRCItem("ingot", 0));
                OreDictUnificator.override("ingotBrass", ModHandler.getRCItem("ingot", 9));
                OreDictUnificator.override("dustCharcoal", ModHandler.getRCItem("dust", 3));
                OreDictUnificator.override("dustObsidian", ModHandler.getRCItem("dust", 0));
                OreDictUnificator.override("dustSaltpeter", ModHandler.getRCItem("dust", 2));
                OreDictUnificator.override("dustSulfur", ModHandler.getRCItem("dust", 1));
            }
        }

        if (Loader.isModLoaded("thermalfoundation")) {
            OreDictUnificator.add("craftingSaltpeterToGunpowder", ModHandler.getTFItem("material", 772));
            OreDictUnificator.add("craftingSulfurToGunpowder", ModHandler.getTFItem("material", 771));

            if (GregTechConfig.UNIFICATION.thermalfoundation) {
                OreDictUnificator.override("pulpWood", ModHandler.getTFItem("material", 800));
                OreDictUnificator.override("dustWood", ModHandler.getTFItem("material", 800));
                OreDictUnificator.override("dustGold", ModHandler.getTFItem("material", 1));
                OreDictUnificator.override("dustBronze", ModHandler.getTFItem("material", 99));
                OreDictUnificator.override("dustCopper", ModHandler.getTFItem("material", 64));
                OreDictUnificator.override("dustElectrum", ModHandler.getTFItem("material", 97));
                OreDictUnificator.override("dustInvar", ModHandler.getTFItem("material", 98));
                OreDictUnificator.override("dustIron", ModHandler.getTFItem("material", 0));
                OreDictUnificator.override("dustLead", ModHandler.getTFItem("material", 67));
                OreDictUnificator.override("dustNickel", ModHandler.getTFItem("material", 69));
                OreDictUnificator.override("dustObsidian", ModHandler.getTFItem("material", 770));
                OreDictUnificator.override("dustPlatinum", ModHandler.getTFItem("material", 70));
                OreDictUnificator.override("dustSilver", ModHandler.getTFItem("material", 66));
                OreDictUnificator.override("dustTin", ModHandler.getTFItem("material", 65));

                OreDictUnificator.override("ingotCopper", ModHandler.getTFItem("material", 128));
                OreDictUnificator.override("ingotElectrum", ModHandler.getTFItem("material", 161));
                OreDictUnificator.override("ingotInvar", ModHandler.getTFItem("material", 162));
                OreDictUnificator.override("ingotLead", ModHandler.getTFItem("material", 131));
                OreDictUnificator.override("ingotNickel", ModHandler.getTFItem("material", 133));
                OreDictUnificator.override("ingotPlatinum", ModHandler.getTFItem("material", 134));
                OreDictUnificator.override("ingotSilver", ModHandler.getTFItem("material", 130));
                OreDictUnificator.override("ingotTin", ModHandler.getTFItem("material", 129));

                OreDictUnificator.override("nuggetCopper", ModHandler.getTFItem("material", 192));
                OreDictUnificator.override("nuggetElectrum", ModHandler.getTFItem("material", 225));
                OreDictUnificator.override("nuggetInvar", ModHandler.getTFItem("material", 226));
                OreDictUnificator.override("nuggetLead", ModHandler.getTFItem("material", 195));
                OreDictUnificator.override("nuggetNickel", ModHandler.getTFItem("material", 197));
                OreDictUnificator.override("nuggetPlatinum", ModHandler.getTFItem("material", 198));
                OreDictUnificator.override("nuggetSilver", ModHandler.getTFItem("material", 194));
                OreDictUnificator.override("nuggetTin", ModHandler.getTFItem("material", 193));

                OreDictUnificator.override("blockCopper", ModHandler.getTFItem("storage", 0));
                OreDictUnificator.override("blockElectrum", ModHandler.getTFItem("storage_alloy", 1));
                OreDictUnificator.override("blockInvar", ModHandler.getTFItem("storage_alloy", 2));
                OreDictUnificator.override("blockLead", ModHandler.getTFItem("storage", 3));
                OreDictUnificator.override("blockNickel", ModHandler.getTFItem("storage", 5));
                OreDictUnificator.override("blockPlatinum", ModHandler.getTFItem("storage", 6));
                OreDictUnificator.override("blockSilver", ModHandler.getTFItem("storage", 2));
                OreDictUnificator.override("blockTin", ModHandler.getTFItem("storage", 1));
            }
        }

        if (GregTechConfig.UNIFICATION.projectred && Loader.isModLoaded("projectred-core")) {
            OreDictUnificator.override("gemRuby", ModHandler.getPRItem("resource_item", 200));
            OreDictUnificator.override("gemSapphire", ModHandler.getPRItem("resource_item", 201));
            OreDictUnificator.override("gemGreenSapphire", ModHandler.getPRItem("resource_item", 202));
            OreDictUnificator.override("ingotSilver", ModHandler.getPRItem("resource_item", 102));
            OreDictUnificator.override("ingotCopper", ModHandler.getPRItem("resource_item", 100));
            OreDictUnificator.override("ingotTin", ModHandler.getPRItem("resource_item", 101));
        }

        if (GregTechConfig.UNIFICATION.thaumcraft && Loader.isModLoaded("thaumcraft")) {
            OreDictUnificator.override("nuggetIron", ModHandler.getTCItem("nugget", 0));
            OreDictUnificator.override("nuggetSilver", ModHandler.getTCItem("nugget", 3));
            OreDictUnificator.override("nuggetTin", ModHandler.getTCItem("nugget", 2));
            OreDictUnificator.override("nuggetCopper", ModHandler.getTCItem("nugget", 1));
            OreDictUnificator.override("nuggetLead", ModHandler.getTCItem("nugget", 4));
        }

        GregTechAPI.logger.debug("Registering GT/IC2 circuitry and similar to the Ore Dictionary");

        registerOre("itemIridium", IC2Items.getItem("misc_resource", "iridium_ore"));
        registerOre("glassReinforced", IC2Items.getItem("glass", "reinforced"));
        //registerOre("molecule_2o", IC2Items.getItem("fluid_cell", "ic2air"));
        registerOre("gemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        registerOre("craftingIndustrialDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        registerOre("itemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        registerOre("craftingUUMatter", IC2Items.getItem("misc_resource", "matter"));
        registerOre("crafting10kEUStore", GtUtil.setWildcard(IC2Items.getItem("re_battery")));
        registerOre("crafting60kEUPack", IC2Items.getItem("te", "batbox"));
        registerOre("crafting300kEUPack", GtUtil.setWildcard(IC2Items.getItem("lappack")));
        registerOre("crafting100kEUStore", GtUtil.setWildcard(IC2Items.getItem("energy_crystal")));
        registerOre("crafting100kEUStore", GtUtil.setWildcard(IC2Items.getItem("advanced_re_battery")));
        registerOre("crafting10kkEUStore", GtUtil.setWildcard(IC2Items.getItem("lapotron_crystal")));
        registerOre("crafting10kCoolantStore", GtUtil.setWildcard(IC2Items.getItem("heat_storage")));
        registerOre("crafting30kCoolantStore", GtUtil.setWildcard(IC2Items.getItem("tri_heat_storage")));
        registerOre("crafting60kCoolantStore", GtUtil.setWildcard(IC2Items.getItem("hex_heat_storage")));
        registerOre("craftingWireCopper", IC2Items.getItem("cable", "type:copper,insulation:1"));
        registerOre("craftingWireGold", IC2Items.getItem("cable", "type:gold,insulation:2"));
        registerOre("craftingWireIron", IC2Items.getItem("cable", "type:iron,insulation:3"));
        registerOre("craftingCircuitTier02", IC2Items.getItem("crafting", "circuit"));
        registerOre("craftingCircuitTier04", IC2Items.getItem("crafting", "advanced_circuit"));
        registerOre("craftingRawMachineTier01", IC2Items.getItem("resource", "machine"));
        registerOre("craftingRawMachineTier02", IC2Items.getItem("resource", "advanced_machine"));
        registerOre("craftingMVTUpgrade", IC2Items.getItem("upgrade", "transformer"));
        registerOre("craftingChest", IC2Items.getItem("te", "personal_chest"));
        registerOre("craftingPump", IC2Items.getItem("te", "pump"));
        registerOre("craftingElectromagnet", IC2Items.getItem("te", "magnetizer"));
        registerOre("craftingTeleporter", IC2Items.getItem("te", "teleporter"));
        registerOre("craftingMacerator", IC2Items.getItem("te", "macerator"));
        registerOre("craftingExtractor", IC2Items.getItem("te", "extractor"));
        registerOre("craftingCompressor", IC2Items.getItem("te", "compressor"));
        registerOre("craftingRecycler", IC2Items.getItem("te", "recycler"));
        registerOre("craftingIronFurnace", IC2Items.getItem("te", "iron_furnace"));
        registerOre("craftingInductionFurnace", IC2Items.getItem("te", "induction_furnace"));
        registerOre("craftingElectricFurnace", IC2Items.getItem("te", "electric_furnace"));
        registerOre("craftingGenerator", IC2Items.getItem("te", "generator"));
        registerOre("craftingSolarPanel", IC2Items.getItem("te", "solar_generator"));

        registerOre("craftingCentrifuge", GregTechObjectAPI.getTileEntity("industrial_centrifuge"));
        //registerOre("craftingRawMachineTier01", GregTechTEBlock.machine_box); TODO add machine box to oredict when added
        //registerOre("craftingCircuitTier03", GregTechTEBlock.restone_circuit_block); TODO add redstone circuit block to oredict when added
        //registerOre("craftingCircuitTier10", GregTechTEBlock.computer_cube); TODO add computer cube to oredict when added
        //registerOre("craftingWorkBench", GregTechTEBlock.electric_workbench); TODO add electric crafting table to oredict when added
        //registerOre("craftingChest", GregTechTEBlock.advanced_safe); TODO add advanced safe to oredict when added
        //registerOre("craftingMacerator", GregTechTEBlock.automatic_macerator); TODO add automatic macerator to oredict when added
        //registerOre("craftingRecycler", GregTechTEBlock.automatic_recycler); TODO add automatic recycler to oredict when added
        //registerOre("craftingCompressor", GregTechTEBlock.automatic_compressor); TODO add automatic compressor to oredict when added
        //registerOre("craftingExtractor", GregTechTEBlock.automatic_extractor); TODO add automatic extractor to oredict when added
        //registerOre("craftingElectricFurnace", GregTechTEBlock.automatic_electric_furnace); TODO add automatic electric furnace to oredict when added

        if (Loader.isModLoaded("thermalfoundation")) {
            registerOre("glassReinforced", ModHandler.getTFItem("glass", OreDictionary.WILDCARD_VALUE));
            registerOre("glassReinforced", ModHandler.getTFItem("glass_alloy", OreDictionary.WILDCARD_VALUE));
        }

        if (Loader.isModLoaded("thermalexpansion")) {
            registerOre("craftingMacerator", ModHandler.getTEItem("machine", 1));
            registerOre("craftingInductionFurnace", ModHandler.getTEItem("machine", 3));
        }

        if (Loader.isModLoaded("quark")) {
            registerOre("stoneRedrock", ModHandler.getModItem("quark", "jasper", OreDictionary.WILDCARD_VALUE));
        }

        if (Loader.isModLoaded("traverse")) {
            registerOre("stoneRedrock", ModHandler.getModItem("traverse", "red_rock", OreDictionary.WILDCARD_VALUE));
        }

        if (Loader.isModLoaded("pvj")) {
            registerOre("stoneMarble", ModHandler.getModItem("pvj", "marble"));
            registerOre("stoneBasalt", ModHandler.getModItem("pvj", "basalt"));
        }
    }

    public static void registerOre(String base, String name, Item instance) {
        registerOre(base+ CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), new ItemStack(instance, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOre(String base, String name, net.minecraft.block.Block instance) {
        registerOre(base+CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), new ItemStack(instance, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOre(String name, net.minecraft.block.Block block) {
        registerOre(name, new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOre(String name, Item item) {
        registerOre(name, new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOre(String name, ItemStack stack) {
        if (name.startsWith("ore") || name.startsWith("block") || name.startsWith("ingot") || name.startsWith("gem") || name.startsWith("dust") || name.startsWith("plate") || name.startsWith("nugget") || name.startsWith("dustTiny")) OreDictUnificator.add(name, stack);
        else OreDictUnificator.registerOre(name, stack);
    }
}