package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Locale;

public class OreDictRegistrar {

    public static void registerItems() {
        GregTechMod.logger.debug("Adding certain items to the OreDict unification blacklist");
        OreDictUnificator.addToBlacklist(IC2Items.getItem("crafting", "industrial_diamond"));

        GregTechMod.logger.debug("Registering GregTech items to the Ore Dictionary");

        Arrays.stream(BlockItems.Block.values())
                .forEach(block -> registerOre("block", block.name().toLowerCase(Locale.ROOT), block.getInstance()));
        registerOre("craftingRawMachineTier04", BlockItems.Block.HIGHLY_ADVANCED_MACHINE.getInstance());

        Arrays.stream(BlockItems.Ore.values())
                .forEach(ore -> registerOre("ore", ore.name().toLowerCase(Locale.ROOT), ore.getInstance()));

        Arrays.stream(BlockItems.Ingot.values())
                .forEach(ingot -> registerOre("ingot", ingot.name().toLowerCase(Locale.ROOT), ingot.getInstance()));
        registerOre("ingotAlloyIridium", BlockItems.Ingot.IRIDIUM_ALLOY.getInstance());

        Arrays.stream(BlockItems.Nugget.values())
                .forEach(nugget -> registerOre("nugget", nugget.name().toLowerCase(Locale.ROOT), nugget.getInstance()));

        Arrays.stream(BlockItems.Plate.values())
                .filter(ProfileDelegate::shouldEnable)
                .forEach(plate -> registerOre("plate", plate.name().toLowerCase(Locale.ROOT), plate.getInstance()));
        OreDictUnificator.add("pulpWood", BlockItems.Dust.WOOD.getInstance());
        registerOre("plankWood", BlockItems.Plate.WOOD.getInstance());
        OreDictUnificator.override("plateAluminum", new ItemStack(BlockItems.Plate.ALUMINIUM.getInstance()));

        Arrays.stream(BlockItems.Rod.values())
                .filter(ProfileDelegate::shouldEnable)
                .forEach(rod -> registerOre("stick", rod.name().toLowerCase(Locale.ROOT), rod.getInstance()));

        Arrays.stream(BlockItems.Dust.values())
                .forEach(dust -> registerOre("dust", dust.name().toLowerCase(Locale.ROOT), dust.getInstance()));
        registerOre("dyeCyan", BlockItems.Dust.LAZURITE.getInstance());
        registerOre("dyeBlue", BlockItems.Dust.SODALITE.getInstance());

        Arrays.stream(BlockItems.Smalldust.values())
                .forEach(smallDust -> registerOre("dustSmall", smallDust.name().toLowerCase(Locale.ROOT), smallDust.getInstance()));

        Arrays.stream(BlockItems.Upgrade.values())
                .filter(upgrade -> upgrade.oreDict != null)
                .forEach(upgrade -> registerOre(upgrade.oreDict, upgrade.getInstance()));

        Arrays.stream(BlockItems.Cover.values())
                .filter(cover -> cover.oreDict != null)
                .forEach(cover -> registerOre(cover.oreDict, cover.getInstance()));

        Arrays.stream(BlockItems.Component.values())
                .filter(component -> component.oreDict != null)
                .forEach(component -> registerOreWildcard(component.oreDict, component.getInstance()));
        registerOreWildcard("lithiumBattery", BlockItems.Component.LITHIUM_BATTERY.getInstance());
        registerOreWildcard("crafting100kEUStore", BlockItems.Component.LITHIUM_BATTERY.getInstance());
        Item gearIron = BlockItems.Component.GEAR_IRON.getInstance();
        if (GregTechMod.classic) registerOre("gearRefinedIron", gearIron);
        registerOre("craftingGearTier01", gearIron);
        registerOre("craftingGearTier01", BlockItems.Component.GEAR_BRONZE.getInstance());
        registerOre("craftingGearTier02", BlockItems.Component.GEAR_STEEL.getInstance());
        registerOre("craftingGearTier03", BlockItems.Component.GEAR_TITANIUM.getInstance());
        registerOre("craftingGearTier03", BlockItems.Component.GEAR_TUNGSTEN_STEEL.getInstance());
        registerOre("craftingGearTier04", BlockItems.Component.GEAR_IRIDIUM.getInstance());

        Arrays.stream(BlockItems.Tool.values())
                .filter(tool -> tool.oreDict != null)
                .forEach(tool -> registerOreWildcard(tool.oreDict, tool.getInstance()));
        registerOreWildcard("craftingToolSaw", BlockItems.Tool.SAW_ADVANCED.getInstance());

        Arrays.stream(BlockItems.File.values())
                .forEach(file -> registerOreWildcard("craftingToolFile", file.getInstance()));

        Arrays.stream(BlockItems.Hammer.values())
                .forEach(hammer -> registerOreWildcard("craftingToolHardHammer", hammer.getInstance()));

        Arrays.stream(BlockItems.Saw.values())
                .forEach(saw -> registerOreWildcard("craftingToolSaw", saw.getInstance()));
        registerOre("craftingToolSaw", StackUtil.copyWithWildCard(IC2Items.getItem("chainsaw")));

        Arrays.stream(BlockItems.SolderingMetal.values())
                .forEach(solderingMetal -> registerOreWildcard("craftingToolSolderingMetal", solderingMetal.getInstance()));

        Arrays.stream(BlockItems.ColorSpray.values())
                .forEach(spray -> registerOreWildcard("dye", spray.name().toLowerCase(Locale.ROOT), spray.getInstance()));

        Arrays.stream(BlockItems.Wrench.values())
                .forEach(wrench -> registerOreWildcard("craftingToolWrench", wrench.getInstance()));
        registerOre("craftingToolWrench", StackUtil.copyWithWildCard(IC2Items.getItem("wrench")));
        if (!IC2.version.isClassic()) registerOre("craftingToolWrench", StackUtil.copyWithWildCard(IC2Items.getItem("wrench_new")));
        registerOre("craftingToolWrench", StackUtil.copyWithWildCard(IC2Items.getItem("electric_wrench")));

        Arrays.stream(BlockItems.Cell.values())
                .forEach(cell -> registerOreWildcard("cell", cell.name().toLowerCase(Locale.ROOT), cell.getInstance()));

        Arrays.stream(BlockItems.NuclearCoolantPack.values())
                .filter(pack -> pack.oreDict != null)
                .forEach(pack -> registerOreWildcard(pack.oreDict, pack.getInstance()));

        Arrays.stream(BlockItems.Armor.values())
                .filter(armor -> armor.oreDict != null)
                .forEach(armor -> registerOreWildcard(armor.oreDict, armor.getInstance()));

        Arrays.stream(BlockItems.Miscellaneous.values())
                .filter(misc -> misc.oreDict != null)
                .forEach(misc -> registerOre(misc.oreDict, misc.getInstance()));

        GregTechMod.logger.debug("Adding vanilla items to the Ore Dictionary");

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

        GregTechMod.logger.debug("Registering unification entries");

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
        OreDictUnificator.add("ingotSteel", new ItemStack(BlockItems.Ingot.STEEL.getInstance()));
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

        GregTechMod.logger.debug("Registering other mods' unification targets");

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

        GregTechMod.logger.debug("Registering GT/IC2 circuitry and similar to the Ore Dictionary");

        registerOre("itemIridium", IC2Items.getItem("misc_resource", "iridium_ore"));
        registerOre("glassReinforced", IC2Items.getItem("glass", "reinforced"));
        registerOre("gemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        registerOre("itemDiamond", IC2Items.getItem("crafting", "industrial_diamond"));
        if (GregTechMod.classic) {
            registerOre("craftingUUMatter", IC2Items.getItem("misc_resource", "matter"));
            registerOre("craftingLappack", StackUtil.copyWithWildCard(IC2Items.getItem("lappack")));
        } else {
            registerOre("craftingEnergyPack", StackUtil.copyWithWildCard(IC2Items.getItem("energy_pack")));
            registerOre("stoneBasalt", IC2Items.getItem("resource", "basalt"));
        }
        registerOre("crafting10kEUStore", StackUtil.copyWithWildCard(IC2Items.getItem("re_battery")));
        registerOre("crafting60kEUPack", IC2Items.getItem("batpack"));
        registerOre(GregTechMod.classic ? "crafting100kEUStore" : "crafting1kkEUStore", StackUtil.copyWithWildCard(IC2Items.getItem("energy_crystal")));
        registerOre(GregTechMod.classic ? "crafting1kkEUStore" : "crafting10kkEUStore", StackUtil.copyWithWildCard(IC2Items.getItem("lapotron_crystal")));
        registerOre("crafting10kCoolantStore", StackUtil.copyWithWildCard(IC2Items.getItem("heat_storage")));
        registerOre("crafting30kCoolantStore", StackUtil.copyWithWildCard(IC2Items.getItem("tri_heat_storage")));
        registerOre("crafting60kCoolantStore", StackUtil.copyWithWildCard(IC2Items.getItem("hex_heat_storage")));
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

    public static void registerOreWildcard(String base, String name, Item item) {
        registerOre(base, name, new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOre(String base, String name, Item item) {
        registerOre(base, name, new ItemStack(item));
    }

    public static void registerOre(String base, String name, net.minecraft.block.Block block) {
        registerOre(base, name, new ItemStack(block));
    }

    public static void registerOre(String base, String name, ItemStack stack) {
        registerOre(base+CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), stack);
    }

    public static void registerOre(String name, net.minecraft.block.Block block) {
        registerOre(name, new ItemStack(block));
    }

    public static void registerOreWildcard(String name, Item item) {
        registerOre(name, new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOre(String name, Item item) {
        registerOre(name, new ItemStack(item));
    }

    public static void registerOre(String name, ItemStack stack) {
        if (name.startsWith("ore") || name.startsWith("block") || name.startsWith("ingot") || name.startsWith("gem") || name.startsWith("dust") || name.startsWith("plate") || name.startsWith("nugget") || name.startsWith("dustSmall")) OreDictUnificator.add(name, stack);
        else OreDictUnificator.registerOre(name, stack);
    }
}