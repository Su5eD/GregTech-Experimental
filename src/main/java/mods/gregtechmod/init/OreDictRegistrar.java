package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.IItemProvider;
import mods.gregtechmod.util.IOreDictItemProvider;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictRegistrar {

    public static void registerItems() {
        GregTechMod.LOGGER.debug("Adding certain items to the OreDict unification blacklist");
        OreDictUnificator.addToBlacklist(IC2Items.getItem("crafting", "industrial_diamond"));

        GregTechMod.LOGGER.debug("Registering GregTech items to the Ore Dictionary");

        registerOres("block", BlockItems.Block.values());
        registerOres("ore", BlockItems.Ore.values());
        registerOres("ingot", BlockItems.Ingot.values());
        registerOres("nugget", BlockItems.Nugget.values());
        registerOres("plate", BlockItems.Plate.values(), true);
        registerOres("stick", BlockItems.Rod.values(), true);
        registerOres("dust", BlockItems.Dust.values());
        registerOres("dustSmall", BlockItems.Smalldust.values());
        registerOresWildcard("craftingToolFile", BlockItems.File.values());
        registerOresWildcard("craftingToolHardHammer", BlockItems.Hammer.values());
        registerOresWildcard("craftingToolSaw", BlockItems.Saw.values());
        registerOresWildcard("craftingToolSolderingMetal", BlockItems.SolderingMetal.values());
        registerOresWildcard("craftingToolWrench", BlockItems.Wrench.values());
        registerOresWildcardPrefix("dye", BlockItems.ColorSpray.values());
        registerOresWildcardPrefix("cell", BlockItems.Cell.values());
        registerOres(BlockItems.Upgrade.values());
        registerOres(BlockItems.CoverItem.values());
        registerOres(BlockItems.Component.values());
        registerOres(BlockItems.Tool.values());
        registerOres(BlockItems.NuclearCoolantPack.values());
        registerOres(BlockItems.Armor.values());
        registerOres(BlockItems.Miscellaneous.values());
        
        registerOreWildcard("lithiumBattery", BlockItems.Component.LITHIUM_BATTERY);
        registerOreWildcard("crafting100kEUStore", BlockItems.Component.LITHIUM_BATTERY);
        registerOreWildcard("craftingToolSaw", BlockItems.Tool.SAW_ADVANCED);
        registerOreWildcard("craftingToolSaw", ModHandler.ic2ItemApi.getItem("chainsaw"));
        registerOre("craftingRawMachineTier04", BlockItems.Block.HIGHLY_ADVANCED_MACHINE);
        registerOre("plankWood", BlockItems.Plate.WOOD);
        registerOre("dyeCyan", BlockItems.Dust.LAZURITE);
        registerOre("dyeBlue", BlockItems.Dust.SODALITE);
        ItemStack gearIron = BlockItems.Component.GEAR_IRON.getItemStack();
        if (GregTechMod.classic) registerOre("gearRefinedIron", gearIron);
        registerOre("craftingGearTier01", gearIron);
        registerOre("craftingGearTier01", BlockItems.Component.GEAR_BRONZE);
        registerOre("craftingGearTier02", BlockItems.Component.GEAR_STEEL);
        registerOre("craftingGearTier03", BlockItems.Component.GEAR_TITANIUM);
        registerOre("craftingGearTier03", BlockItems.Component.GEAR_TUNGSTEN_STEEL);
        registerOre("craftingGearTier04", BlockItems.Component.GEAR_IRIDIUM);
        registerOreWildcard("craftingToolWrench", ModHandler.ic2ItemApi.getItem("wrench"));
        if (!GregTechMod.classic) registerOreWildcard("craftingToolWrench", ModHandler.ic2ItemApi.getItem("wrench_new"));
        registerOreWildcard("craftingToolWrench", ModHandler.ic2ItemApi.getItem("electric_wrench"));
        OreDictUnificator.add("pulpWood", BlockItems.Dust.WOOD.getItemStack());
        OreDictUnificator.override("plateAluminum", BlockItems.Plate.ALUMINIUM.getItemStack());

        GregTechMod.LOGGER.debug("Adding vanilla items to the Ore Dictionary");

        registerOre("soulsand", Blocks.SOUL_SAND);
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
        registerOre("craftingEnderChest", Blocks.ENDER_CHEST);
        registerOre("paperMap", Items.MAP);
        registerOre("paperMap", Items.FILLED_MAP);
        registerOre("bookEmpty", Items.BOOK);
        registerOre("bookWritable", Items.WRITABLE_BOOK);
        registerOre("bookWritten", Items.WRITTEN_BOOK);
        registerOre("bookEnchanted", Items.ENCHANTED_BOOK);
        registerOre("gemCoal", Items.COAL);
        registerOre("plantGrass", new ItemStack(Blocks.TALLGRASS, 1, 1));
        registerOre("plantFern", new ItemStack(Blocks.TALLGRASS, 1, 2));
        registerOre("plantFernLarge", new ItemStack(Blocks.DOUBLE_PLANT, 1, 3));
        registerOre("plantTallGrass", new ItemStack(Blocks.DOUBLE_PLANT, 1, 2));
        registerOre("seedWheat", Items.WHEAT_SEEDS);
        registerOre("seedMelon", Items.MELON_SEEDS);
        registerOre("seedPumpkin", Items.PUMPKIN_SEEDS);
        registerOre("seedBeet", Items.BEETROOT_SEEDS);
        registerOre("flowerRed", Blocks.RED_FLOWER);
        registerOre("flowerRed", new ItemStack(Blocks.RED_FLOWER, 1, 4));
        registerOre("flowerDoubleRed", new ItemStack(Blocks.DOUBLE_PLANT, 1, 4));
        registerOre("flowerYellow", Blocks.YELLOW_FLOWER);
        registerOre("flowerDoubleYellow", Blocks.DOUBLE_PLANT);
        registerOre("flowerLightBlue", new ItemStack(Blocks.RED_FLOWER, 1, 1));
        registerOre("flowerMagenta", new ItemStack(Blocks.RED_FLOWER, 1, 2));
        registerOre("flowerDoubleMagenta", new ItemStack(Blocks.DOUBLE_PLANT, 1, 1));
        registerOre("flowerLightGray", new ItemStack(Blocks.RED_FLOWER, 1, 3));
        registerOre("flowerLightGray", new ItemStack(Blocks.RED_FLOWER, 1, 6));
        registerOre("flowerLightGray", new ItemStack(Blocks.RED_FLOWER, 1, 8));
        registerOre("flowerOrange", new ItemStack(Blocks.RED_FLOWER, 1, 5));
        registerOre("flowerPink", new ItemStack(Blocks.RED_FLOWER, 1, 7));
        registerOre("flowerDoublePink", new ItemStack(Blocks.DOUBLE_PLANT, 1, 5));
        registerOre("foodRaw", Items.PORKCHOP);
        registerOre("foodRaw", Items.BEEF);
        registerOre("foodRaw", Items.CHICKEN);
        registerOre("foodRaw", Items.MUTTON);
        registerOre("foodRaw", Items.RABBIT);
        registerOre("foodRaw", new ItemStack(Items.FISH));
        registerOre("foodRaw", new ItemStack(Items.FISH, 1, 1));
        registerOre("foodCooked", Items.COOKED_PORKCHOP);
        registerOre("foodCooked", Items.COOKED_BEEF);
        registerOre("foodCooked", Items.COOKED_CHICKEN);
        registerOre("foodCooked", Items.COOKED_MUTTON);
        registerOre("foodCooked", Items.COOKED_RABBIT);
        registerOre("foodCooked", new ItemStack(Items.COOKED_FISH));
        registerOre("foodCooked", new ItemStack(Items.COOKED_FISH, 1, 1));
        for (int i = 1; i < 16; i++) {
            registerOre("woolColored", new ItemStack(Blocks.WOOL, 1, i));
        }
        registerOre("itemRecord", Items.RECORD_11);
        registerOre("itemRecord", Items.RECORD_13);
        registerOre("itemRecord", Items.RECORD_CAT);
        registerOre("itemRecord", Items.RECORD_BLOCKS);
        registerOre("itemRecord", Items.RECORD_FAR);
        registerOre("itemRecord", Items.RECORD_CHIRP);
        registerOre("itemRecord", Items.RECORD_MALL);
        registerOre("itemRecord", Items.RECORD_MELLOHI);
        registerOre("itemRecord", Items.RECORD_STAL);
        registerOre("itemRecord", Items.RECORD_STRAD);
        registerOre("itemRecord", Items.RECORD_WAIT);
        registerOre("itemRecord", Items.RECORD_WARD);

        GregTechMod.LOGGER.debug("Registering unification entries");

        OreDictUnificator.add("oreCoal", Blocks.COAL_ORE);
        OreDictUnificator.add("oreIron", Blocks.IRON_ORE);
        OreDictUnificator.add("oreLapis", Blocks.LAPIS_ORE);
        OreDictUnificator.add("oreRedstone", Blocks.REDSTONE_ORE);
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
        OreDictUnificator.add("ingotSteel", BlockItems.Ingot.STEEL.getItemStack());
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
        OreDictUnificator.add("blockDiamond", Blocks.DIAMOND_BLOCK);
        OreDictUnificator.add("blockEmerald", Blocks.EMERALD_BLOCK);
        OreDictUnificator.add("blockLapis", Blocks.LAPIS_BLOCK);
        OreDictUnificator.add("blockRedstone", Blocks.REDSTONE_BLOCK);
        OreDictUnificator.add("blockCopper", IC2Items.getItem("resource", "copper_block"));
        OreDictUnificator.add("blockTin", IC2Items.getItem("resource", "tin_block"));
        OreDictUnificator.add("blockBronze", IC2Items.getItem("resource", "bronze_block"));
        OreDictUnificator.add("blockUranium", IC2Items.getItem("resource", "uranium_block"));
        OreDictUnificator.add("itemRubber", IC2Items.getItem("crafting", "rubber"));
        OreDictUnificator.add("dustSugar", Items.SUGAR);
        OreDictUnificator.add("stickWood", Items.STICK);
        OreDictUnificator.add("crystalNetherQuartz", Items.QUARTZ);

        GregTechMod.LOGGER.debug("Registering other mods' unification targets");

        if (GregTechConfig.UNIFICATION.forestry && ModHandler.forestry) {
            OreDictUnificator.override("ingotCopper", ModHandler.getFRItem("ingot_copper"));
            OreDictUnificator.override("ingotTin", ModHandler.getFRItem("ingot_tin"));
            OreDictUnificator.override("ingotBronze", ModHandler.getFRItem("ingot_bronze"));
            OreDictUnificator.override("dustAshes", ModHandler.getFRItem("ash"));
            OreDictUnificator.override("dustWood", ModHandler.getFRItem("wood_pulp"));
            OreDictUnificator.override("pulpWood", ModHandler.getFRItem("wood_pulp"));
        }

        if (ModHandler.railcraft) {
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

        if (ModHandler.thermalfoundation) {
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

        if (GregTechConfig.UNIFICATION.projectred && ModHandler.projectredCore) {
            OreDictUnificator.override("gemRuby", ModHandler.getPRItem("resource_item", 200));
            OreDictUnificator.override("gemSapphire", ModHandler.getPRItem("resource_item", 201));
            OreDictUnificator.override("gemGreenSapphire", ModHandler.getPRItem("resource_item", 202));
            OreDictUnificator.override("ingotSilver", ModHandler.getPRItem("resource_item", 102));
            OreDictUnificator.override("ingotCopper", ModHandler.getPRItem("resource_item", 100));
            OreDictUnificator.override("ingotTin", ModHandler.getPRItem("resource_item", 101));
        }

        if (GregTechConfig.UNIFICATION.thaumcraft && ModHandler.thaumcraft) {
            OreDictUnificator.override("nuggetIron", ModHandler.getTCItem("nugget", 0));
            OreDictUnificator.override("nuggetSilver", ModHandler.getTCItem("nugget", 3));
            OreDictUnificator.override("nuggetTin", ModHandler.getTCItem("nugget", 2));
            OreDictUnificator.override("nuggetCopper", ModHandler.getTCItem("nugget", 1));
            OreDictUnificator.override("nuggetLead", ModHandler.getTCItem("nugget", 4));
        }

        GregTechMod.LOGGER.debug("Registering GT/IC2 circuitry and similar to the Ore Dictionary");

        registerOre("itemIridium", IC2Items.getItem("misc_resource", "iridium_ore"));
        registerOre("glassReinforced", IC2Items.getItem("glass", "reinforced"));
        registerOre("gemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        registerOre("itemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        if (GregTechMod.classic) {
            registerOre("craftingUUMatter", IC2Items.getItem("misc_resource", "matter"));
            registerOreWildcard("craftingLappack", ModHandler.ic2ItemApi.getItem("lappack"));
        } else {
            registerOreWildcard("craftingEnergyPack", ModHandler.ic2ItemApi.getItem("energy_pack"));
            registerOre("stoneBasalt", IC2Items.getItem("resource", "basalt"));
        }
        registerOreWildcard("crafting10kEUStore", ModHandler.ic2ItemApi.getItem("re_battery"));
        registerOre("crafting60kEUPack", IC2Items.getItem("batpack"));
        registerOreWildcard(GregTechMod.classic ? "crafting100kEUStore" : "crafting1kkEUStore", ModHandler.ic2ItemApi.getItem("energy_crystal"));
        registerOreWildcard(GregTechMod.classic ? "crafting1kkEUStore" : "crafting10kkEUStore", ModHandler.ic2ItemApi.getItem("lapotron_crystal"));
        registerOreWildcard("crafting10kCoolantStore", ModHandler.ic2ItemApi.getItem("heat_storage"));
        registerOreWildcard("crafting30kCoolantStore", ModHandler.ic2ItemApi.getItem("tri_heat_storage"));
        registerOreWildcard("crafting60kCoolantStore", ModHandler.ic2ItemApi.getItem("hex_heat_storage"));
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
        registerOre("craftingCircuitTier10", GregTechObjectAPI.getTileEntity("computer_cube"));
        //registerOre("craftingWorkBench", GregTechTEBlock.electric_workbench); TODO add electric crafting table to oredict when added
        registerOre("craftingChest", GregTechObjectAPI.getTileEntity("advanced_safe"));
        registerOre("craftingMacerator", GregTechObjectAPI.getTileEntity("auto_macerator"));
        registerOre("craftingRecycler", GregTechObjectAPI.getTileEntity("auto_recycler"));
        registerOre("craftingCompressor", GregTechObjectAPI.getTileEntity("auto_compressor"));
        registerOre("craftingExtractor", GregTechObjectAPI.getTileEntity("auto_extractor"));
        registerOre("craftingElectricFurnace", GregTechObjectAPI.getTileEntity("auto_electric_furnace"));

        if (ModHandler.thermalfoundation) {
            registerOre("glassReinforced", ModHandler.getTFItem("glass", OreDictionary.WILDCARD_VALUE));
            registerOre("glassReinforced", ModHandler.getTFItem("glass_alloy", OreDictionary.WILDCARD_VALUE));
        }
        if (ModHandler.thermalExpansion) {
            registerOre("craftingMacerator", ModHandler.getTEItem("machine", 1));
            registerOre("craftingInductionFurnace", ModHandler.getTEItem("machine", 3));
        }
        if (ModHandler.quark) {
            registerOre("stoneRedrock", ModHandler.getModItem("quark", "jasper", OreDictionary.WILDCARD_VALUE));
        }
        if (ModHandler.traverse) {
            registerOre("stoneRedrock", ModHandler.getModItem("traverse", "red_rock", OreDictionary.WILDCARD_VALUE));
        }
        if (ModHandler.projectVibrantJourneys) {
            registerOre("stoneMarble", ModHandler.getModItem("pvj", "marble"));
            registerOre("stoneBasalt", ModHandler.getModItem("pvj", "basalt"));
        }
        if (ModHandler.projectredExploration) {
            Item stone = ModHandler.getItem("projectred-exploration", "stone");
            registerOre("stoneMarble", new ItemStack(stone, 1, 1));
            registerOre("stoneBasalt", new ItemStack(stone, 1, 2));
            registerOre("stoneBasalt", new ItemStack(stone, 1, 4));
        }
        if (ModHandler.enderStorage) {
            registerOre("craftingEnderChest", ModHandler.getModItem("enderstorage", "ender-storage"));
        }
        if (ModHandler.buildcraftFactory) {
            registerOre("craftingWorkBench", ModHandler.getModItem("buildcraftfactory", "autoworkbench_item"));
            registerOre("craftingPump", ModHandler.getModItem("buildcraftfactory", "pump"));
            registerOre("craftingTank", ModHandler.getModItem("buildcraftfactory", "tank"));
        }
        if (ModHandler.agricraft) {
            registerOre("seedAgri", ModHandler.getModItem("agricraft", "agri_seed", OreDictionary.WILDCARD_VALUE));
        }
        if (ModHandler.twilightForest) {
            registerOre("plantForestGrass", ModHandler.getModItem("twilightforest", "twilight_plant", 5));
        }
    }
    
    public static <T extends Enum<?> & IItemProvider> void registerOres(String prefix, T[] providers) {
        registerOres(prefix, providers, false);
    }
    
    public static <T extends Enum<?> & IItemProvider> void registerOres(String prefix, T[] providers, boolean filterProfile) {
        registerOres(prefix, providers, filterProfile, false);
    }
    
    public static <T extends Enum<?> & IItemProvider> void registerOresWildcardPrefix(String prefix, T[] providers) {
        registerOres(prefix, providers, false, true);
    }
    
    public static <T extends Enum<?> & IItemProvider> void registerOres(String prefix, T[] providers, boolean filterProfile, boolean wildcard) {
        for (T provider : providers) {
            if (!filterProfile || ProfileDelegate.shouldEnable(provider)) {
                String name = prefix + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, provider.name());
                
                if (wildcard) registerOreWildcard(name, provider.getInstance());
                else registerOre(name, provider.getItemStack());
            }
        }
    }
    
    public static <T extends Enum<?> & IItemProvider> void registerOresWildcard(String name, T[] providers) {
        for (T provider : providers) {
            registerOreWildcard(name, provider);
        }
    }
    
    public static <T extends Enum<?> & IOreDictItemProvider> void registerOres(T[] providers) {
        for (T provider : providers) {
            String oreDict = provider.getOreDictName();
            if (oreDict != null) {
                if (provider.isWildcard()) registerOreWildcard(oreDict, provider);
                else registerOre(oreDict, provider);
            }
        }
    }
    
    public static void registerOreWildcard(String name, IItemProvider provider) {
        registerOreWildcard(name, provider.getInstance());
    }
    
    public static void registerOreWildcard(String name, Item item) {
        registerOre(name, new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }
    
    public static void registerOre(String name, net.minecraft.block.Block block) {
        registerOre(name, new ItemStack(block));
    }
    
    public static void registerOre(String name, IItemProvider provider) {
        registerOre(name, provider.getItemStack());
    }

    public static void registerOre(String name, Item item) {
        registerOre(name, new ItemStack(item));
    }

    public static void registerOre(String name, ItemStack stack) {
        if (name.startsWith("ore") || name.startsWith("block") || name.startsWith("ingot") || name.startsWith("gem") || name.startsWith("dust") || name.startsWith("plate") || name.startsWith("nugget") || name.startsWith("dustSmall")) OreDictUnificator.add(name, stack);
        else OreDictUnificator.registerOre(name, stack);
    }
}
