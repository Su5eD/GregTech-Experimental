package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import ic2.core.init.OreValues;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.recipe.RecipeDualInput;
import mods.gregtechmod.recipe.RecipeLathe;
import mods.gregtechmod.recipe.RecipePulverizer;
import mods.gregtechmod.recipe.RecipeSimple;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.*;

//Good luck reading this ;)
public class OreDictHandler {
    private Map<String, ItemStack> events = new HashMap<>();

    public static final Map<String, String> GTOreNames = new HashMap<>();
    public static final Map<String, Integer> valuableOres = new HashMap<>();

    private static final List<String> IGNORED_NAMES = Arrays.asList("naquadah", "brickXyEngineering", "breederUranium", "diamondNugget", "infiniteBattery", "superconductor", "itemCharcoalSugar", "aluminumWire", "aluminiumWire", "silverWire",
            "tinWire", "eliteBattery", "advancedBattery", "transformer", "coil", "wireMill", "multimeter", "itemMultimeter", "chunkLazurite", "itemRecord", "aluminumNatural", "aluminiumNatural", "naturalAluminum", "naturalAluminium",
            "antimatterMilligram", "antimatterGram", "strangeMatter", "HSLivingmetalIngot", "oilMoving", "oilStill", "oilBucket", "orePetroleum", "dieselFuel", "lava", "water", "obsidianRod", "motor", "wrench", "coalGenerator",
            "electricFurnace", "ironTube", "netherTube", "obbyTube", "valvePart", "aquaRegia", "leatherSeal", "leatherSlimeSeal", "enrichedUranium", "batteryInfinite", "itemSuperconductor", "camoPaste", "CAMO_PASTE");

    private boolean activated = false;

    private static final ResourceLocation RECIPE_GROUP_DUSTS = new ResourceLocation(Reference.MODID, "dusts");
    private static final ResourceLocation RECIPE_GROUP_ORES = new ResourceLocation(Reference.MODID, "ores");
    private static final ResourceLocation RECIPE_GROUP_ARMOR = new ResourceLocation(Reference.MODID, "armor");
    private static final ResourceLocation RECIPE_GROUP_TOOLS = new ResourceLocation(Reference.MODID, "tools");
    private static final ResourceLocation RECIPE_GROUP_STICKS = new ResourceLocation(Reference.MODID, "sticks");
    private static final ResourceLocation RECIPE_GROUP_BLOCKS = new ResourceLocation(Reference.MODID, "blocks");

    static {
        GTOreNames.put("battery", "crafting10kEUStore");
        GTOreNames.put("basicCircuit", "craftingCircuitTier02");
        GTOreNames.put("circuitBasic", "craftingCircuitTier02");
        GTOreNames.put("advancedCircuit", "craftingCircuitTier04");
        GTOreNames.put("circuitAdvanced", "craftingCircuitTier04");
        GTOreNames.put("eliteCircuit", "craftingCircuitTier06");
        GTOreNames.put("circuitElite", "craftingCircuitTier06");
        GTOreNames.put("basalt", "stoneBasalt");
        GTOreNames.put("marble", "stoneMarble");
        GTOreNames.put("mossystone", "stoneMossy");
        GTOreNames.put("MonazitOre", "oreMonazit");
        GTOreNames.put("blockQuickSilver", "blockQuicksilver");
        GTOreNames.put("ingotQuickSilver", "ingotQuicksilver");
        GTOreNames.put("ingotQuicksilver", "itemQuicksilver");
        GTOreNames.put("dustQuickSilver", "dustQuicksilver");
        GTOreNames.put("dustQuicksilver", "itemQuicksilver");
        GTOreNames.put("itemQuickSilver", "itemQuicksilver");
        GTOreNames.put("dustCharCoal", "dustCharcoal");
        GTOreNames.put("quartzCrystal", "crystalQuartz");
        GTOreNames.put("quartz", "crystalQuartz");
        GTOreNames.put("woodGas", "gasWood");
        GTOreNames.put("woodLog", "logWood");
        GTOreNames.put("pulpWood", "dustWood");
        GTOreNames.put("blockCobble", "stoneCobble");
        GTOreNames.put("gemPeridot", "gemOlivine");
        GTOreNames.put("dustPeridot", "dustOlivine");
        GTOreNames.put("dustDiamond", "itemDiamond");
        GTOreNames.put("gemDiamond", "itemDiamond");
        GTOreNames.put("dustLapis", "itemLazurite");
        GTOreNames.put("dustLapisLazuli", "itemLazurite");
        GTOreNames.put("dustLazurite", "itemLazurite");
        GTOreNames.put("craftingRawMachineTier01", "craftingRawMachineTier00");
        GTOreNames.put("dustSulfur", "craftingSulfurToGunpowder");
        GTOreNames.put("dustSaltpeter", "craftingSaltpeterToGunpowder");
        GTOreNames.put("crystalQuartz", "craftingQuartz");
        GTOreNames.put("crystalNetherQuartz", "craftingQuartz");
        GTOreNames.put("crystalCertusQuartz", "craftingQuartz");
        GTOreNames.put("dustQuartz", "craftingQuartz");
        GTOreNames.put("dustCertusQuartz", "craftingQuartz");
        GTOreNames.put("dustNetherQuartz", "craftingQuartz");
        GTOreNames.put("ingotQuartz", "craftingQuartz");
        GTOreNames.put("ingotNetherQuartz", "craftingQuartz");
        GTOreNames.put("ingotCertusQuartz", "craftingQuartz");

        valuableOres.put("soulsand", 1);
        valuableOres.put("glowstone", 1);
        valuableOres.put("oreLapis", 3);
        valuableOres.put("oreSodalite", 3);
        valuableOres.put("oreRedstone", 3);
        valuableOres.put("oreQuartz", 4);
        valuableOres.put("oreNikolite", 3);
        valuableOres.put("oreIron", 3);
        valuableOres.put("oreGold", 4);
        valuableOres.put("oreSilver", 3);
        valuableOres.put("oreLead", 3);
        valuableOres.put("oreSilverLead", 4);
        valuableOres.put("oreGalena", 4);
        valuableOres.put("oreDiamond", 5);
        valuableOres.put("oreEmerald", 5);
        valuableOres.put("oreRuby", 4);
        valuableOres.put("oreSapphire", 4);
        valuableOres.put("oreGreenSapphire", 4);
        valuableOres.put("oreOlivine", 4);
        valuableOres.put("oreCoal", 1);
        valuableOres.put("oreCopper", 3);
        valuableOres.put("oreTin", 3);
        valuableOres.put("oreZinc", 2);
        valuableOres.put("oreCassiterite", 3);
        valuableOres.put("oreTetrahedrite", 3);
        valuableOres.put("oreAntimony", 3);
        valuableOres.put("oreIridium", 10);
        valuableOres.put("oreCooperite", 10);
        valuableOres.put("oreSheldonite", 10);
        valuableOres.put("orePlatinum", 7);
        valuableOres.put("oreNickel", 4);
        valuableOres.put("orePyrite", 1);
        valuableOres.put("oreCinnabar", 3);
        valuableOres.put("oreSphalerite", 2);
        valuableOres.put("oreAluminium", 2);
        valuableOres.put("oreAluminum", 2);
        valuableOres.put("oreSteel", 4);
        valuableOres.put("oreTitan", 5);
        valuableOres.put("oreTitanium", 5);
        valuableOres.put("oreChrome", 10);
        valuableOres.put("oreChromium", 10);
        valuableOres.put("oreElectrum", 5);
        valuableOres.put("oreTungsten", 4);
        valuableOres.put("oreTungstate", 4);
        valuableOres.put("oreBauxite", 2);
        valuableOres.put("oreApatite", 1);
        valuableOres.put("oreSulfur", 1);
        valuableOres.put("oreSaltpeter", 2);
        valuableOres.put("orePhosphorite", 2);
        valuableOres.put("oreMagnesium", 2);
        valuableOres.put("oreManganese", 2);
        valuableOres.put("oreMonazit", 3);
        valuableOres.put("oreMonazite", 3);
        valuableOres.put("oreFortronite", 3);
        valuableOres.put("oreThorium", 5);
        valuableOres.put("orePlutonium", 15);
        valuableOres.put("oreOsmium", 20);
        valuableOres.put("oreEximite", 3);
        valuableOres.put("oreMeutoite", 3);
        valuableOres.put("orePrometheum", 3);
        valuableOres.put("oreDeepIron", 2);
        valuableOres.put("oreInfuscolium", 3);
        valuableOres.put("oreOureclase", 3);
        valuableOres.put("oreAredrite", 3);
        valuableOres.put("oreAstralSilver", 4);
        valuableOres.put("oreCarmot", 4);
        valuableOres.put("oreMithril", 4);
        valuableOres.put("oreRubracium", 3);
        valuableOres.put("oreOrichalcum", 3);
        valuableOres.put("oreAdamantine", 5);
        valuableOres.put("oreAtlarus", 3);
        valuableOres.put("oreIgnatius", 3);
        valuableOres.put("oreShadowIron", 4);
        valuableOres.put("oreMidasium", 3);
        valuableOres.put("oreVyroxeres", 3);
        valuableOres.put("oreCeruclase", 3);
        valuableOres.put("oreKalendrite", 3);
        valuableOres.put("oreVulcanite", 3);
        valuableOres.put("oreSanguinite", 3);
        valuableOres.put("oreLemurite", 3);
        valuableOres.put("oreAdluorite", 3);
        valuableOres.put("oreNaquadah", 8);
        valuableOres.put("oreBitumen", 2);
        valuableOres.put("oreForce", 3);
        valuableOres.put("oreCertusQuartz", 4);
        valuableOres.put("oreVinteum", 3);
        valuableOres.put("orePotash", 2);
        valuableOres.put("oreArdite", 2);
        valuableOres.put("oreCobalt", 2);
        valuableOres.put("oreUranium", 5);
    }

    public OreDictHandler() {
        /*registerOre(new OreDictionary.OreRegisterEvent("stickWood", new ItemStack(Item.stick, 1, 0)));
        registerOre(new OreDictionary.OreRegisterEvent("logWood", new ItemStack(Block.wood, 1, 32767)));
        registerOre(new OreDictionary.OreRegisterEvent("slabWood", new ItemStack((Block)Block.woodSingleSlab, 1, 32767)));
        registerOre(new OreDictionary.OreRegisterEvent("plankWood", new ItemStack(Block.planks, 1, 32767)));
        registerOre(new OreDictionary.OreRegisterEvent("treeLeaves", new ItemStack((Block)Block.leaves, 1, 32767)));
        registerOre(new OreDictionary.OreRegisterEvent("treeSapling", new ItemStack(Block.sapling, 1, 32767)));
        registerOre(new OreDictionary.OreRegisterEvent("oreCoal", new ItemStack(Block.oreCoal, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreIron", new ItemStack(Block.oreIron, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreLapis", new ItemStack(Block.oreLapis, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreRedstone", new ItemStack(Block.oreRedstone, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreRedstone", new ItemStack(Block.oreRedstoneGlowing, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreGold", new ItemStack(Block.oreGold, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreDiamond", new ItemStack(Block.oreDiamond, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreEmerald", new ItemStack(Block.oreEmerald, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("oreNetherQuartz", new ItemStack(Block.oreNetherQuartz, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("gemDiamond", new ItemStack(Item.diamond, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("gemEmerald", new ItemStack(Item.emerald, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("nuggetGold", new ItemStack(Item.goldNugget, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("ingotGold", new ItemStack(Item.ingotGold, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("ingotIron", new ItemStack(Item.ingotIron, 1)));
        registerOre(new OreDictionary.OreRegisterEvent("blockIron", new ItemStack(Block.blockIron, 1, 0)));
        registerOre(new OreDictionary.OreRegisterEvent("blockGold", new ItemStack(Block.blockGold, 1, 0)));
        registerOre(new OreDictionary.OreRegisterEvent("blockDiamond", new ItemStack(Block.blockDiamond, 1, 0)));
        registerOre(new OreDictionary.OreRegisterEvent("blockEmerald", new ItemStack(Block.blockEmerald, 1, 0)));
        registerOre(new OreDictionary.OreRegisterEvent("blockLapis", new ItemStack(Block.blockLapis, 1, 0)));
        registerOre(new OreDictionary.OreRegisterEvent("blockRedstone", new ItemStack(Block.blockRedstone, 1, 0)));
        String[] dyes = {
                "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink",
                "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };
        for (int i = 0; i < 16; i++)
            registerOre(new OreDictionary.OreRegisterEvent(dyes[i], new ItemStack(Item.dyePowder, 1, i)));*/
    }

    public static void registerValuableOres() {
        valuableOres.forEach((key, value) -> OreDictionary.getOres(key)
                .forEach(stack -> OreValues.add(stack, value)));
    }

    @SubscribeEvent
    public void registerOre(OreDictionary.OreRegisterEvent event) {
        String name;
        ItemStack ore;
        if (event == null || (ore = event.getOre()).isEmpty() || (name = event.getName()) == null || event.getName().isEmpty() || IGNORED_NAMES.contains(event.getName())) return;

        if (ore.getCount() != 1) GregTechAPI.logger.error("'" + name + "' is either being misused by another Mod or has been wrongly registered, as the stackSize of the Event-Stack is not 1");
        event.getOre().setCount(1);

        if (name.toLowerCase().contains("xych") || name.toLowerCase().contains("xyore") || name.toLowerCase().contains("aluminum")) return;

        String unifiedName = GTOreNames.get(name);
        if (unifiedName != null) OreDictUnificator.registerOre(unifiedName, ore);

        if (name.startsWith("denseOre")) {
            OreDictUnificator.registerOre(name.replaceFirst("denseOre", "oreDense"), ore);
            return;
        } else if (name.startsWith("netherOre")) {
            OreDictUnificator.registerOre(name.replaceFirst("netherOre", "oreNether"), ore);
            return;
        } else if (name.startsWith("endOre")) {
            OreDictUnificator.registerOre(name.replaceFirst("endOre", "oreEnd"), ore);
            return;
        } else if (name.startsWith("itemDrop")) {
            OreDictUnificator.registerOre(name.replaceFirst("itemDrop", "item"), ore);
            return;
        } else if (name.startsWith("stoneBlackGranite")) OreDictUnificator.registerOre("stoneGranite", ore);
        else if (name.startsWith("stoneRedGranite")) OreDictUnificator.registerOre("stoneGranite", ore);

        if (name.startsWith("plate") || name.startsWith("ore") || name.startsWith("dust") || name.startsWith("gem")
                || name.startsWith("ingot") || name.startsWith("nugget") || name.startsWith("block") || name.startsWith("stick")) OreDictUnificator.addAssociation(name, ore.copy());

        if (this.activated) registerRecipes(event.getName(), event.getOre());
        else this.events.put(event.getName(), event.getOre());
    }

    public void activateHandler() {
        this.activated = true;
        for (Map.Entry<String, ItemStack> entry : this.events.entrySet())
            registerRecipes(entry.getKey(), entry.getValue());
        this.events = null;
    }

    private void processStoneOre(ItemStack stack, String name) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) GregTechAPI.jackHammerMinableBlocks.add(stack);


        if (name.equals("stoneObsidian") && item instanceof ItemBlock) {
            ((ItemBlock) item).getBlock().setResistance(20.0F);
        }
    }

    public void registerRecipes(String name, ItemStack ore) {
        /*if (ore.isEmpty()) return;

        ore.setCount(1);
        int cellCount = 0;
        if (name.startsWith("plate") || name.startsWith("ore") || name.startsWith("dust") || name.startsWith("gem") || name.startsWith("ingot") || name.startsWith("nugget") || name.startsWith("block") || name.startsWith("stick")) OreDictUnificator.add(name, ore.copy());

        if (name.startsWith("drop")) name = name.replaceFirst("drop", "item");

        if (name.startsWith("stone")) {
            processStoneOre(ore, name);
        } else if (name.startsWith("ore")) {
            processOre(ore, name);
        } else if (name.startsWith("dust")) {
            processDust(ore, name);
        } else if (name.startsWith("ingot")) {
            processIngot(ore, name);
        } else if (name.startsWith("block")) {
            processBlock(ore, name);
        } else if (name.startsWith("gem")) {
            registerGemRecipes(item, meta, name, event);
        } else if (name.startsWith("crystal") && !name.startsWith("crystalline")) {
            if (!name.equals("crystalQuartz") && name.equals("crystalNetherQuartz")) ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre("dustNetherQuartz", 1), null, 0, true);
        } else if (name.startsWith("plasma_")) {
            GregTech_API.sRecipeAdder.addFuel(ore, (GtUtil.getCapsuleCellContainerCount(ore) == 1) ? GtUtil.emptyCell : null, 8192, 4);
            GregTech_API.sRecipeAdder.addVacuumFreezerRecipe(ore, (GtUtil.getCapsuleCellContainerCount(ore) == 1) ? OreDictUnificator.getFirstCapsulatedOre(name.replaceFirst("plasma_", "molecule_"), 1) : OreDictUnificator.getFirstUnCapsulatedOre(name.replaceFirst("plasma_", "molecule_"), 1), 100);
        } else if (name.startsWith("molecule_")) {
            if (name.equals("molecule_1h")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(8, 1), new ItemStack(item, 4, meta), GT_Metitem_Cell.instance.getStack(9, 5), 3500);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(new ItemStack(item, 4, meta), ModHandler.getIC2Item("airCell", 1), ModHandler.getIC2Item("waterCell", 5), 10);
                }
                cellCount = 4 * GtUtil.getCapsuleCellContainerCount(ore) - 1;
                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 4, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 1, 1), null, null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 3000);
            } else if (name.equals("molecule_1he")) {
                cellCount = 16 * GtUtil.getCapsuleCellContainerCount(ore) - 1;
                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 16, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 1, 6), null, null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 10000);
            } else if (name.equals("molecule_1d") || name.equals("molecule_1h2")) {
                ArrayList tList = OreDictUnificator.getOres("molecule_1t");
                tList.addAll(OreDictUnificator.getOres("molecule_1h3"));
                Iterator<ItemStack> tIterator = tList.iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Cell.instance.getStack(131, 1), 128, -4096, 40000000));
                tIterator = OreDictUnificator.getOres("molecule_1he3").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Cell.instance.getStack(131, 1), 128, -2048, 60000000));
                cellCount = 4 * GtUtil.getCapsuleCellContainerCount(ore) - 1;
                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 4, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 1, 2), null, null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 3000);
            } else if (name.equals("molecule_1t") || name.equals("molecule_1h3")) {
                ArrayList tList = OreDictUnificator.getOres("molecule_1d");
                tList.addAll(OreDictUnificator.getOres("molecule_1h2"));
                Iterator<ItemStack> tIterator = tList.iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Cell.instance.getStack(131, 1), 128, -4096, 40000000));
            } else if (name.equals("molecule_1he3")) {
                ArrayList tList = OreDictUnificator.getOres("molecule_1d");
                tList.addAll(OreDictUnificator.getOres("molecule_1h2"));
                Iterator<ItemStack> tIterator = tList.iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Cell.instance.getStack(131, 1), 128, -2048, 60000000));
            } else if (name.equals("molecule_1w")) {
                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("molecule_1li").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), ModHandler.getIC2Item("iridiumOre", 1, OreDictUnificator.getFirstOre("dustIridium", 1)), 512, -32768, 150000000));
                tIterator = OreDictUnificator.getOres("molecule_1be").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Dust.instance.getStack(27, 1), 512, -32768, 100000000));
            } else if (name.equals("molecule_1li")) {
                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("molecule_1w").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, ModHandler.getIC2Item("iridiumOre", 1, OreDictUnificator.getFirstOre("dustIridium", 1)), 512, -32768, 150000000));
            } else if (name.equals("molecule_1be")) {
                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("molecule_1w").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Dust.instance.getStack(27, 1), 512, -32768, 100000000));
            } else if (name.equals("molecule_1c_4h") || name.equals("molecule_1me")) {
                cellCount = 5 * GtUtil.getCapsuleCellContainerCount(ore) - 5;
                GregTech_API.sRecipeAdder.addElectrolyzerRecipe(new ItemStack(item, 5, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 4, 0), GregTech_API.getGregTechItem(2, 1, 8), null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 150, 50);
            } else if (name.equals("molecule_1si")) {
                cellCount = 2 * GtUtil.getCapsuleCellContainerCount(ore);
                GregTech_API.sRecipeAdder.addBlastRecipe(new ItemStack(item, 2, meta), null, GT_Metitem_Material.instance.getStack(36, 1), (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 1000, 128, 1500);
            } else if (name.equals("molecule_1c")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                    GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(0, 4), GT_Metitem_Cell.instance.getStack(9, 5), 3500);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(15, 1), ore, GT_Metitem_Cell.instance.getStack(39, 2), 1500);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(11, 1), ore, GT_Metitem_Cell.instance.getStack(33, 2), 250);
                }
            } else if (name.equals("molecule_1ca")) {
                GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(8, 1), GT_Metitem_Cell.instance.getStack(33, 2), 250);
            } else if (name.equals("molecule_1na")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(36, 1), ore, GT_Metitem_Cell.instance.getStack(37, 2), 100);
            } else if (name.equals("molecule_1s")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                    GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(12, 1), GT_Metitem_Cell.instance.getStack(37, 2), 100);
                    GregTech_API.sRecipeAdder.addCentrifugeRecipe(ore, 0, OreDictUnificator.get("dustSulfur", 1), null, null, GtUtil.emptyCell, 40);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(ore, ModHandler.getIC2Item("waterCell", 2), GT_Metitem_Cell.instance.getStack(40, 3), 1150);
                }
            } else if (name.equals("molecule_1na_1s")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                    GregTech_API.sRecipeAdder.addChemicalRecipe(new ItemStack(item, 2, meta), ModHandler.getIC2Item("airCell", 3), GT_Metitem_Cell.instance.getStack(32, 5), 4000);
            } else if (!name.equals("molecule_1cl")) {
                if (!name.equals("molecule_1k"))
                    if (name.equals("molecule_1n")) {
                        if (item.getItemStackLimit() >= 16)
                            for (ItemStack tStack : OreDictUnificator.getOres("craftingSprayCan"))
                                GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 16, meta), tStack.copy().splitStack(1), GregTech_API.getGregTechItem(91, 1, 0), ModHandler.getEmptyCell(GtUtil.getCapsuleCellContainerCount(ore) * 16), 1600, 2);
                        if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, ModHandler.getIC2Item("airCell", 1), GT_Metitem_Cell.instance.getStack(38, 2), 1250);
                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(8, 1), GT_Metitem_Cell.instance.getStack(39, 2), 1500);
                        }
                    } else if (name.equals("molecule_1n_1c")) {
                        if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                            GregTech_API.sRecipeAdder.addChemicalRecipe(new ItemStack(item, 3, meta), ModHandler.getIC2Item("waterCell", 3), GT_Metitem_Cell.instance.getStack(34, 6), 1750);
                    } else if (name.equals("molecule_2h_1s_4o")) {
                        GregTech_API.sRecipeAdder.addElectrolyzerRecipe(new ItemStack(item, 7, meta), 0, GT_Metitem_Cell.instance.getStack(0, 2), GT_Metitem_Cell.instance.getStack(36, 1), ModHandler.getIC2Item("airCell", 2), ModHandler.getEmptyCell(2), 40, 100);
                    } else if (!name.equals("molecule_1n_2o")) {
                        if (name.equals("molecule_1hg")) {
                            if (GtUtil.getCapsuleCellContainerCount(ore) == 1 &&
                                    ModHandler.mTCResource != null)
                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(ore, 0, new ItemStack(ModHandler.mTCResource.getItem(), 1, 3), null, null, GtUtil.emptyCell, 40);
                        } else if (name.equals("molecule_1ca_1c_3o")) {
                            if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(ore, 0, OreDictUnificator.get("dustCalcite", 1), null, null, GtUtil.emptyCell, 40);
                        } else if (!name.equals("molecule_2na_2s_8o")) {
                            if (!name.equals("molecule_2o"))
                                if (!name.equals("molecule_1o"))
                                    if (name.equals("molecule_3c_5h_3n_9o")) {
                                        if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, ModHandler.getIC2Item("coalfuelCell", 4), GT_Metitem_Cell.instance.getStack(35, 5), 250);
                                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(18, 4), GT_Metitem_Cell.instance.getStack(22, 5), 250);
                                        }
                                    } else {
                                        System.out.println("Molecule Name: " + name + " !!!Unknown Molecule detected!!! Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information.");
                                    }
                        }
                    }
            }
        } else if (name.startsWith("nugget")) {
            ModHandler.addToRecyclerBlackList(ore);
            if (name.equals("nuggetGold"))
                if (ModHandler.mBCStoneGear == null) {
                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("stoneCobble").iterator();
                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 4, meta), ModHandler.getRCItem("part.gear.gold.plate", 1), 800, 1));
                } else {
                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.mBCStoneGear, new ItemStack(item, 4, meta), ModHandler.getRCItem("part.gear.gold.plate", 1), 800, 1);
                }
            if (name.equals("nuggetIridium") || name.equals("nuggetOsmium") || name.equals("nuggetUranium") || name.equals("nuggetPlutonium") || name.equals("nuggetThorium")) {
                ModHandler.addCompressionRecipe(new ItemStack(item, 9, meta), OreDictUnificator.getFirstOre(name.replaceFirst("nugget", "ingot"), 1));
            } else {
                GregTech_API.sRecipeAdder.addAlloySmelterRecipe(new ItemStack(item, 9, meta), null, OreDictUnificator.getFirstOre(name.replaceFirst("nugget", "ingot"), 1), 200, 1);
            }
            ModHandler.addShapelessCraftingRecipe(OreDictUnificator.getFirstOre(name.replaceFirst("nugget", "ingot"), 1), new Object[] {name, name, name, name, name, name, name, name, name});
            ModHandler.addShapelessCraftingRecipe(OreDictUnificator.getFirstOre(name, 9), new Object[] { name.replaceFirst("nugget", "ingot") });
        } else if (!name.startsWith("shard")) {
            if (name.startsWith("wax")) {
                if (name.equals("waxMagical"))
                    GregTech_API.sRecipeAdder.addFuel(ore, null, 6, 5);
            } else if (name.startsWith("element_")) {
                System.err.println("Depricated Prefix 'element_' @ " + name + " please change to 'molecule_'");
            } else if (name.startsWith("cell_")) {
                System.err.println("Depricated Prefix 'cell_' @ " + name + " Cells are now detected automatically, if you register as 'molecule_', so you don't need to register with this prefix any longer");
            } else if (!name.startsWith("element")) {
                if (!name.startsWith("cell"))
                    if (name.startsWith("crafting")) {
                        if (name.equals("craftingLiBattery")) {
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("cropnalyzer", 1, 32767), GregTech_API.getGregTechItem(63, 1, GregTech_API.getGregTechItem(63, 1, 0).getMaxDamage() - 1), 12800, 16);
                            Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateAluminium").iterator();
                            for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(4), GT_Metitem_Component.instance.getStack(26, 1), 3200, 4));
                        } else if (name.equals("craftingRawMachineTier01")) {
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.music, 4, 32767), new ItemStack(GregTech_API.sBlockList[1], 1, 66), 800, 1);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.stoneButton, 16, 32767), new ItemStack(GregTech_API.sBlockList[1], 1, 67), 800, 1);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GT_Metitem_Component.instance.getStack(22, 1), new ItemStack(GregTech_API.sBlockList[1], 1, 79), 1600, 2);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GT_Metitem_Component.instance.getStack(7, 1), ModHandler.getIC2Item("solarPanel", 1), 1600, 2);
                        } else if (name.equals("craftingGearTier02")) {
                            for (ItemStack tStack : OreDictUnificator.getOres("craftingGenerator"))
                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, tStack.copy().splitStack(1), GT_Metitem_Component.instance.getStack(25, 1), 3200, 4);
                        } else if (name.equals("craftingGenerator")) {
                            for (ItemStack tStack : OreDictUnificator.getOres("craftingGearTier02"))
                                GregTech_API.sRecipeAdder.addAssemblerRecipe(tStack.copy().splitStack(1), ore, GT_Metitem_Component.instance.getStack(25, 1), 3200, 4);
                        } else if (name.equals("craftingWireCopper")) {
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(GT_Metitem_Component.instance.getStack(48, 1), new ItemStack(item, 3, meta), ModHandler.getIC2Item("electronicCircuit", 1), 800, 1);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("electronicCircuit", 1), ore, ModHandler.getIC2Item("frequencyTransmitter", 1), 800, 1);
                        } else if (name.equals("craftingSprayCan")) {
                            GregTech_API.sRecipeAdder.addCannerRecipe(ModHandler.getIC2Item("grinPowder", 1), ore, ModHandler.getIC2Item("weedEx", 1), null, 800, 1);
                            GregTech_API.sRecipeAdder.addCannerRecipe(ModHandler.getIC2Item("waterCell", 16), ore, GregTech_API.getGregTechItem(95, 1, 0), ModHandler.getEmptyCell(16), 1600, 2);
                            GregTech_API.sRecipeAdder.addCannerRecipe(ModHandler.getIC2Item("constructionFoamPellet", 16), ore, GregTech_API.getGregTechItem(93, 1, 0), null, 1600, 2);
                            GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(Block.sand, 16, 0), ore, GregTech_API.getGregTechItem(92, 1, 0), null, 1600, 2);
                            for (ItemStack tStack : OreDictUnificator.getOres("molecule_1n")) {
                                if (tStack.getMaxStackSize() >= 16)
                                    GregTech_API.sRecipeAdder.addCannerRecipe(tStack.copy().splitStack(16), ore, GregTech_API.getGregTechItem(91, 1, 0), ModHandler.getEmptyCell(GtUtil.getCapsuleCellContainerCount(tStack) * 16), 1600, 2);
                            }
                            for (String tDyeName : GT_Utility.sDyeToMetaMapping.keySet()) {
                                for (ItemStack tStack : OreDictUnificator.getOres(tDyeName)) {
                                    if (tStack.getMaxStackSize() >= 16 && !tStack.getItem().hasContainerItem())
                                        GregTech_API.sRecipeAdder.addCannerRecipe(tStack.copy().splitStack(16), ore, GregTech_API.getGregTechItem(96 + ((Byte)GT_Utility.sDyeToMetaMapping.get(tDyeName)).byteValue(), 1, 0), null, 800, 1);
                                }
                            }
                        }
                    } else if (name.startsWith("dye")) {
                        if (item.getItemStackLimit() >= 16 && !item.hasContainerItem()) {
                            Iterator<ItemStack> tIterator = OreDictUnificator.getOres("craftingSprayCan").iterator();
                            for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 16, meta), ((ItemStack)tIterator.next()).copy().splitStack(1), GregTech_API.getGregTechItem(96 + ((Byte)GT_Utility.sDyeToMetaMapping.get(name)).byteValue(), 1, 0), null, 800, 1));
                        }
                    } else if (!name.startsWith("clump")) {
                        if (!name.startsWith("glass"))
                            if (!name.startsWith("gear"))
                                if (!name.startsWith("material"))
                                    if (!name.startsWith("storage"))
                                        if (!name.startsWith("tool"))
                                            if (!name.startsWith("food"))
                                                if (!name.startsWith("flower"))
                                                    if (name.startsWith("plate")) {
                                                        boolean temp = true;
                                                        if (temp)
                                                            if (name.startsWith("plateAlloy")) {
                                                                temp = false;
                                                                if (name.equals("plateAlloyCarbon")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("generator", 1), new ItemStack(item, 4, meta), ModHandler.getIC2Item("windMill", 1), 6400, 8);
                                                                } else if (name.equals("plateAlloyAdvanced")) {
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("stoneSmooth").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(8), ModHandler.getIC2Item("reinforcedStone", 8), 400, 4));
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.glass, 7, 0), ModHandler.getIC2Item("reinforcedGlass", 7), 400, 4);
                                                                }
                                                            } else {
                                                                ModHandler.removeRecipe(ore);
                                                            }
                                                        if (temp)
                                                            if (name.startsWith("plateDense")) {
                                                                temp = false;
                                                                ModHandler.addSmeltingAndAlloySmeltingRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plateDense", "ingot"), 8));
                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plateDense", "dust"), 8), null, 0, false);
                                                            } else {
                                                                ModHandler.addCompressionRecipe(new ItemStack(item, 8, meta), OreDictUnificator.getFirstOre(name.replaceFirst("plate", "plateDense"), 1));
                                                                ModHandler.addSmeltingAndAlloySmeltingRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plate", "ingot"), 1));
                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plate", "dust"), 1), null, 0, false);
                                                            }
                                                        if (temp)
                                                            if (name.equals("plateCopper")) {
                                                                ModHandler.addCompressionRecipe(new ItemStack(item, 8, meta), ModHandler.getIC2Item("denseCopperPlate", 1));
                                                            } else if (name.equals("plateTin")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, null, ModHandler.getIC2Item("tinCan", 1), 400, 1);
                                                            } else if (name.equals("plateIron")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 3, meta), null, new ItemStack(Item.bucketEmpty, 1, 0), 400, 1);
                                                            } else if (name.equals("plateBronze")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(33, 1), 400, 8);
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickBronze"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(97, 1), 3200, 4);
                                                            } else if (name.equals("plateBrass")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(34, 1), 400, 8);
                                                            } else if (name.equals("plateRefinedIron")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 8, meta), GT_Metitem_Component.instance.getStack(22, 1), ModHandler.getIC2Item("machine", 1, GT_Metitem_Component.instance.getStack(37, 1)), 400, 8);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack((Block)Block.chest, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack(Block.chestTrapped, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("pump", 1), GT_Metitem_Component.instance.getStack(6, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateIron, 1, 32767), GT_Metitem_Component.instance.getStack(11, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateGold, 1, 32767), GT_Metitem_Component.instance.getStack(10, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("ecMeter", 1), GT_Metitem_Component.instance.getStack(15, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.fenceIron, 2), GT_Metitem_Component.instance.getStack(9, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneActive, 1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneIdle, 1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Item.comparator, 1), GT_Metitem_Component.instance.getStack(30, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.lever, 1), GT_Metitem_Component.instance.getStack(31, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.workbench, 1), GT_Metitem_Component.instance.getStack(64, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("energyCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(12, 1), 1600, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("lapotronCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(13, 1), 3200, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GregTech_API.getGregTechItem(37, 1, 32767), GT_Metitem_Component.instance.getStack(14, 1), 6400, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), ModHandler.getIC2Item("electronicCircuit", 1), GT_Metitem_Component.instance.getStack(22, 4), 800, 16);
                                                                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateElectrum").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(2), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                tIterator = OreDictUnificator.getOres("plateIridium").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(86, 1), 800, 16));
                                                                tIterator = OreDictUnificator.getOres("craftingRedstoneTorch").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16));
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickRefinedIron"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(96, 1), 3200, 4);
                                                            } else if (name.equals("plateSteel")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), ModHandler.getRCItem("machine.beta.engine.steam.high", 1, GregTech_API.getGregTechBlock(1, 1, 34)), GT_Metitem_Component.instance.getStack(80, 1), 1600, 32);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.thinGlass, 1, 32767), GT_Metitem_Component.instance.getStack(81, 1), 1600, 32);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(35, 1), 400, 8);
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickSteel"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(98, 1), 3200, 4);
                                                            } else if (name.equals("plateTitanium")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(36, 1), 400, 8);
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickTitanium"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(99, 1), 3200, 4);
                                                            } else if (!name.equals("plateTungsten")) {
                                                                if (name.equals("plateTungstenSteel")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(38, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("reinforcedStone", 1), new ItemStack(GregTech_API.sBlockList[4], 1, 8), 400, 4);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(GregTech_API.sBlockList[0], 1, 2), new ItemStack(GregTech_API.sBlockList[4], 1, 9), 400, 4);
                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickTungstenSteel"))
                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(100, 1), 3200, 4);
                                                                } else if (name.equals("plateIridium")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("reinforcedStone", 1), new ItemStack(GregTech_API.sBlockList[0], 1, 2), 400, 4);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(GregTech_API.sBlockList[4], 1, 8), new ItemStack(GregTech_API.sBlockList[4], 1, 9), 400, 4);
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateRefinedIron").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                    tIterator = OreDictUnificator.getOres("plateAluminium").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickIridium"))
                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(101, 1), 3200, 4);
                                                                } else if (name.equals("plateAluminium")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack((Block)Block.chest, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack(Block.chestTrapped, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("pump", 1), GT_Metitem_Component.instance.getStack(6, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateIron, 1, 32767), GT_Metitem_Component.instance.getStack(11, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateGold, 1, 32767), GT_Metitem_Component.instance.getStack(10, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("ecMeter", 1), GT_Metitem_Component.instance.getStack(15, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.fenceIron, 2), GT_Metitem_Component.instance.getStack(9, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneActive, 1), GT_Metitem_Component.instance.getStack(30, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneIdle, 1), GT_Metitem_Component.instance.getStack(30, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.lever, 1), GT_Metitem_Component.instance.getStack(31, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.workbench, 1), GT_Metitem_Component.instance.getStack(64, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("energyCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(12, 1), 1600, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("lapotronCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(13, 1), 3200, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GregTech_API.getGregTechItem(37, 1, 32767), GT_Metitem_Component.instance.getStack(14, 1), 6400, 16);
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("craftingLiBattery").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(26, 1), 3200, 4));
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), ModHandler.getIC2Item("electronicCircuit", 1), GT_Metitem_Component.instance.getStack(22, 3), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(32, 1), 400, 8);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("generator", 1), new ItemStack(item, 4, meta), ModHandler.getIC2Item("waterMill", 2), 6400, 8);
                                                                    tIterator = OreDictUnificator.getOres("plateElectrum").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(2), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                    tIterator = OreDictUnificator.getOres("plateIridium").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                    tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(86, 1), 800, 16));
                                                                    tIterator = OreDictUnificator.getOres("craftingRedstoneTorch").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16));
                                                                } else if (name.equals("plateElectrum")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("electronicCircuit", 1), new ItemStack(item, 2, meta), GT_Metitem_Component.instance.getStack(49, 1), 1600, 2);
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateRefinedIron").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 2, meta), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                    tIterator = OreDictUnificator.getOres("plateAluminium").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 2, meta), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                    tIterator = OreDictUnificator.getOres("plateSilicon").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(49, 2), 1600, 2));
                                                                } else if (name.equals("plateSilicon")) {
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateElectrum").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(4), GT_Metitem_Component.instance.getStack(49, 2), 1600, 2));
                                                                } else if (name.equals("platePlatinum")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("advancedCircuit", 1), GT_Metitem_Component.instance.getStack(50, 1), 3200, 4);
                                                                } else if (name.equals("plateMagnalium")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("generator", 1), new ItemStack(item, 2, meta), ModHandler.getIC2Item("windMill", 1), 6400, 8);
                                                                }
                                                            }
                                                        if (OreDictUnificator.getFirstOre(name.replaceFirst("plate", "ingot"), 1) != null)
                                                            ModHandler.addCraftingRecipe(ore, new Object[] { "H", "I", "I", Character.valueOf('H'), "craftingToolHardHammer", Character.valueOf('I'), name.replaceFirst("plate", "ingot") });
                                                    } else if (!name.startsWith("dirtyGravel")) {
                                                        if (!name.startsWith("cleanGravel"))
                                                            if (!name.startsWith("crystalline"))
                                                                if (!name.startsWith("reduced"))
                                                                    if (name.startsWith("paper")) {
                                                                        OreDictUnificator.addAssociation(name, ore);
                                                                    } else if (name.startsWith("book")) {
                                                                        OreDictUnificator.addAssociation(name, ore);
                                                                    } else if (!name.startsWith("FZ.")) {
                                                                        if (!name.startsWith("crop"))
                                                                            if (!name.startsWith("lumar"))
                                                                                if (!name.startsWith("list"))
                                                                                    if (!name.startsWith("seed"))
                                                                                        if (!name.startsWith("sheet"))
                                                                                            if (!name.startsWith("icbm:"))
                                                                                                if (!name.startsWith("calclavia:"))
                                                                                                    if (!name.startsWith("mffs"))
                                                                                                        if (!name.startsWith("MiscPeripherals$"))
                                                                                                            if (name.equals("gasWood")) {
                                                                                                                cellCount = 16 * GtUtil.getCapsuleCellContainerCount(ore) - 16;
                                                                                                                GregTech_API.sRecipeAdder.addElectrolyzerRecipe(new ItemStack(item, 16, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 4, 0), GregTech_API.getGregTechItem(2, 8, 8), GregTech_API.getGregTechItem(2, 4, 9), (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 200, 100);
                                                                                                            } else if (name.equals("woodRubber") || name.equals("logRubber")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (GT_Utility.areStacksEqual(ModHandler.getIC2Item("rubberWood", 1), ore))
                                                                                                                    meta = 32767;
                                                                                                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 16, meta), 5, ModHandler.getIC2Item("resin", 8), ModHandler.getIC2Item("plantBall", 6), GregTech_API.getGregTechItem(2, 1, 9), GregTech_API.getGregTechItem(2, 4, 8), 5000);
                                                                                                                ModHandler.addSawmillRecipe(ore, ModHandler.getIC2Item("resin", 1), GT_Metitem_Dust.instance.getStack(15, 16));
                                                                                                                ModHandler.addExtractionRecipe(ore, ModHandler.getIC2Item("rubber", 1));
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                        if (tStack != null) {
                                                                                                                            ModHandler.removeRecipe(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 4 / 3)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { new ItemStack(item, 1, i) });
                                                                                                                        }
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { ore });
                                                                                                                    if (tStack != null) {
                                                                                                                        ModHandler.removeRecipe(new ItemStack[] { ore });
                                                                                                                        ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 4 / 3)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), ore });
                                                                                                                        ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { ore });
                                                                                                                    }
                                                                                                                }
                                                                                                            } else if (name.startsWith("log")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                        if (tStack != null) {
                                                                                                                            ItemStack tPlanks = tStack.copy();
                                                                                                                            tPlanks.stackSize = tPlanks.stackSize * 3 / 2;
                                                                                                                            ModHandler.addSawmillRecipe(new ItemStack(item, 1, i), tPlanks, GT_Metitem_Dust.instance.getStack(15, 1));
                                                                                                                            ModHandler.removeRecipe(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 5 / 4)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { new ItemStack(item, 1, i) });
                                                                                                                        }
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { ore });
                                                                                                                    if (tStack != null) {
                                                                                                                        ItemStack tPlanks = tStack.copy();
                                                                                                                        tPlanks.stackSize = tPlanks.stackSize * 3 / 2;
                                                                                                                        ModHandler.addSawmillRecipe(ore, tPlanks, GT_Metitem_Dust.instance.getStack(15, 1));
                                                                                                                        ModHandler.removeRecipe(new ItemStack[] { ore });
                                                                                                                        ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 5 / 4)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), ore });
                                                                                                                        ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { ore });
                                                                                                                    }
                                                                                                                }
                                                                                                            } else if (name.startsWith("slabWood")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sPlankStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sPlankStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addPulverisationRecipe(new ItemStack(item, 1, i), OreDictUnificator.get("dustSmallWood", 2), null, 0, false);
                                                                                                                        GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, i), ModHandler.getRCItem("liquid.creosote.cell", 1), ModHandler.getRCItem("part.tie.wood", 1), GtUtil.emptyCell, 200, 4);
                                                                                                                        GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, i), ModHandler.getRCItem("liquid.creosote.bucket", 1), ModHandler.getRCItem("part.tie.wood", 1), new ItemStack(Item.bucketEmpty, 1), 200, 4);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addPulverisationRecipe(ore, OreDictUnificator.get("dustSmallWood", 2), null, 0, false);
                                                                                                                    GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("liquid.creosote.cell", 1), ModHandler.getRCItem("part.tie.wood", 1), GtUtil.emptyCell, 200, 4);
                                                                                                                    GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("liquid.creosote.bucket", 1), ModHandler.getRCItem("part.tie.wood", 1), new ItemStack(Item.bucketEmpty, 1), 200, 4);
                                                                                                                }
                                                                                                            } else if (name.startsWith("plankWood")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sPlankStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sPlankStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addPulverisationRecipe(new ItemStack(item, 1, i), GT_Metitem_Dust.instance.getStack(15, 1), null, 0, false);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(15, 1), null, 0, false);
                                                                                                                }
                                                                                                                GregTech_API.sRecipeAdder.addLatheRecipe(ore, OreDictUnificator.get("stickWood", 2), null, 25, 8);
                                                                                                                GregTech_API.sRecipeAdder.addCNCRecipe(new ItemStack(item, 2, meta), ModHandler.mBCWoodGear, 800, 1);
                                                                                                                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 8, meta), ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.music, 1), 800, 1));
                                                                                                                tIterator = OreDictUnificator.getOres("gemDiamond").iterator();
                                                                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 8, meta), ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.jukebox, 1), 1600, 1));
                                                                                                            } else if (name.startsWith("treeSapling")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addCompressionRecipe(new ItemStack(item, 4, i), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                        ModHandler.addPulverisationRecipe(new ItemStack(item, 1, i), GT_Metitem_SmallDust.instance.getStack(15, 2), null, 0, false);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addCompressionRecipe(new ItemStack(item, 4, meta), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                    ModHandler.addPulverisationRecipe(ore, GT_Metitem_SmallDust.instance.getStack(15, 2), null, 0, false);
                                                                                                                }
                                                                                                            } else if (name.equals("treeLeaves")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addCompressionRecipe(new ItemStack(item, 8, i), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addCompressionRecipe(new ItemStack(item, 8, meta), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                }
                                                                                                            } else if (name.startsWith("stick")) {
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++)
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                }
                                                                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("stick", "dustSmall"), 2), null, 0, false);
                                                                                                                if (name.equals("stickWood")) {
                                                                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("stoneCobble").iterator();
                                                                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.lever, 1), 400, 1));
                                                                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Item.coal, 1, 32767), new ItemStack(Block.torchWood, 4), 400, 1);
                                                                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("resin", 1), new ItemStack(Block.torchWood, 4), 400, 1);
                                                                                                                    tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.torchRedstoneActive, 1), 400, 1));
                                                                                                                } else if (name.equals("stickAluminium")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 1), 150, 10);
                                                                                                                } else if (name.equals("stickIron")) {
                                                                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 3, meta), null, new ItemStack(Block.fenceIron, 4), 400, 1);
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 2), 150, 10);
                                                                                                                } else if (name.equals("stickRefinedIron")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 6, meta), ModHandler.getRCItem("part.rail.standard", 5), 150, 20);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateRefinedIron"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(96, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickBronze")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 2, meta), ModHandler.getRCItem("part.rail.standard", 1), 50, 20);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateBronze"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(97, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickSteel")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 4), 150, 30);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateSteel"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(98, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickTitanium")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 8), 150, 30);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateTitanium"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(99, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickTungsten")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 8), 150, 30);
                                                                                                                } else if (name.equals("stickTungstenSteel")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(ore, ModHandler.getRCItem("part.rail.reinforced", 4), 100, 30);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateTungstenSteel"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(100, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickIridium")) {
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateIridium"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(101, 1), 3200, 4);
                                                                                                                }
                                                                                                            } else if (!name.startsWith("stair")) {
                                                                                                                if (!name.startsWith("slab"))
                                                                                                                    if (name.equals("itemLazurite") || name.equals("lazurite")) {
                                                                                                                        Iterator<ItemStack> tIterator = OreDictUnificator.getOres("dustGlowstone").iterator();
                                                                                                                        for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(24, 2), 800, 2));
                                                                                                                    } else if (name.equals("itemDiamond") || name.equals("diamond")) {
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.mBCGoldGear, new ItemStack(item, 4, meta), ModHandler.mBCDiamondGear, 1600, 2);
                                                                                                                    } else if (!name.equals("itemIridium") && !name.equals("iridium")) {
                                                                                                                        if (!name.equals("itemTear") && !name.equals("tear"))
                                                                                                                            if (!name.equals("itemClaw") && !name.equals("claw"))
                                                                                                                                if (!name.equals("itemTar") && !name.equals("tar"))
                                                                                                                                    if (!name.equals("itemSlimeball") && !name.equals("slimeball"))
                                                                                                                                        if (!name.equals("fuelCoke") && !name.equals("coke"))
                                                                                                                                            if (!name.equals("itemBeeswax") && !name.equals("beeswax"))
                                                                                                                                                if (name.equals("itemBeeComb") || name.equals("beeComb")) {
                                                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                                                } else if (!name.equals("itemForcicium") && !name.equals("ForciciumItem")) {
                                                                                                                                                    if (!name.equals("itemForcillium"))
                                                                                                                                                        if (!name.equals("brickPeat") && !name.equals("peat"))
                                                                                                                                                            if (!name.equals("itemRoyalJelly") && !name.equals("royalJelly"))
                                                                                                                                                                if (!name.equals("itemHoneydew") && !name.equals("honeydew"))
                                                                                                                                                                    if (!name.equals("itemHoney") && !name.equals("honey"))
                                                                                                                                                                        if (!name.equals("itemPollen") && !name.equals("pollen"))
                                                                                                                                                                            if (!name.equals("itemReedTypha") && !name.equals("reedTypha"))
                                                                                                                                                                                if (!name.equals("itemSulfuricAcid") && !name.equals("sulfuricAcid"))
                                                                                                                                                                                    if (name.equals("itemRubber") || name.equals("rubber")) {
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("copperCableItem", 1), ore, ModHandler.getIC2Item("insulatedCopperCableItem", 1), 100, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("goldCableItem", 1), new ItemStack(item, 2, meta), ModHandler.getIC2Item("doubleInsulatedGoldCableItem", 1), 200, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("ironCableItem", 1), new ItemStack(item, 3, meta), ModHandler.getIC2Item("trippleInsulatedIronCableItem", 1), 300, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("insulatedGoldCableItem", 1), ore, ModHandler.getIC2Item("doubleInsulatedGoldCableItem", 1), 100, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("insulatedIronCableItem", 1), new ItemStack(item, 2, meta), ModHandler.getIC2Item("trippleInsulatedIronCableItem", 1), 200, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("doubleInsulatedIronCableItem", 1), ore, ModHandler.getIC2Item("trippleInsulatedIronCableItem", 1), 100, 2);
                                                                                                                                                                                    } else if (!name.equals("itemPotash") && !name.equals("potash")) {
                                                                                                                                                                                        if (!name.equals("itemCompressedCarbon") && !name.equals("compressedCarbon"))
                                                                                                                                                                                            if (name.equals("itemManganese") || name.equals("manganese")) {
                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(12, 1), null, 0, false);
                                                                                                                                                                                            } else if (name.equals("itemMagnesium") || name.equals("magnesium")) {
                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(13, 1), null, 0, false);
                                                                                                                                                                                            } else if (name.equals("itemPhosphorite") || name.equals("phosphorite") || name.equals("itemPhosphorus") || name.equals("phosphorus")) {
                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.get("dustPhosphorus", 1), null, 0, false);
                                                                                                                                                                                            } else if (!name.equals("itemBitumen") && !name.equals("bitumen")) {
                                                                                                                                                                                                if (!name.equals("itemBioFuel"))
                                                                                                                                                                                                    if (!name.equals("itemEnrichedAlloy"))
                                                                                                                                                                                                        if (name.equals("itemQuicksilver") || name.equals("quicksilver")) {
                                                                                                                                                                                                            GregTech_API.sRecipeAdder.addCannerRecipe(ore, GtUtil.emptyCell, GT_Metitem_Cell.instance.getStack(16, 1), null, 100, 1);
                                                                                                                                                                                                        } else if (!name.equals("chunkOsmium") && !name.equals("itemOsmium") && !name.equals("osmium")) {
                                                                                                                                                                                                            if (name.equals("sandOil") || name.equals("oilsandsOre")) {
                                                                                                                                                                                                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 2, meta), 1, GT_Metitem_Cell.instance.getStack(17, 1), new ItemStack(Block.sand, 1, 0), null, null, 1000);
                                                                                                                                                                                                            } else if (name.equals("itemSulfur") || name.equals("sulfur")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(8, 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("itemAluminum") || name.equals("aluminum") || name.equals("itemAluminium") || name.equals("aluminium")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(18, 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("itemSaltpeter") || name.equals("saltpeter")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(9, 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("itemUranium") || name.equals("uranium")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.get("dustUranium", 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("sandCracked")) {
                                                                                                                                                                                                                if (item.itemID < 4096)
                                                                                                                                                                                                                    GregTech_API.sRecipeAdder.addJackHammerMinableBlock(Block.blocksList[item.itemID]);
                                                                                                                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && item.getItemStackLimit() > GT_Mod.sBlockStackSize)
                                                                                                                                                                                                                    item.setMaxStackSize(GT_Mod.sBlockStackSize);
                                                                                                                                                                                                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 16, meta), -1, ModHandler.getFuelCan(25000), GT_Metitem_Dust.instance.getStack(9, 8), null, new ItemStack(Block.sand, 10), 2500);
                                                                                                                                                                                                            } else if (name.startsWith("item")) {
                                                                                                                                                                                                                System.out.println("Item Name: " + name + " !!!Unknown Item detected!!! Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information.");
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                System.out.println("Thingy Name: " + name + " !!!Unknown 'Thingy' detected!!! This Object seems to probably not follow a valid OreDictionary Convention, or I missed a Convention. Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information.");
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                            }
                                                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                    }
                                                                                                            }
                                                                    }
                                                    }
                    }
            }
        }*/
    }

    private void processGem(ItemStack stack, String name) {
        ItemStack block = OreDictUnificator.get(name.replaceFirst("gem", "block"));
        if (!block.isEmpty()) Recipes.compressor.addRecipe(Recipes.inputFactory.forOreDict(name, 9), null, true, block);
    }

    private void processBlock(ItemStack stack, String name) {
        ItemStack ingot = OreDictUnificator.getFirstOre(name.replaceFirst("block", "ingot"));
        ItemStack gem = OreDictUnificator.getFirstOre(name.replaceFirst("block", "gem"));
        String dustName = name.replaceFirst("block", "dust");
        ItemStack dust = OreDictUnificator.getFirstOre(dustName);

        ModHandler.removeCraftingRecipe(stack);
        if (!GregTechAPI.dynamicConfig.get("storageBlockCrafting", name, false).getBoolean()) {
            if (ingot.isEmpty() && gem.isEmpty() && !dust.isEmpty()) ModHandler.removeCraftingRecipe(dust, dust, dust, dust, dust, dust, dust, dust, dust);
            if (!gem.isEmpty()) ModHandler.removeCraftingRecipe(gem, gem, gem, gem, gem, gem, gem, gem, gem);
            if (!ingot.isEmpty()) ModHandler.removeCraftingRecipe(ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot);
        }

        ingot.setCount(9);
        gem.setCount(9);
        dust.setCount(9);
        if (GregTechAPI.dynamicConfig.get("storageBlockDeCrafting", name, !gem.isEmpty()).getBoolean()) {
            ModHandler.addShapelessRecipe(name+"Dust", RECIPE_GROUP_BLOCKS, dust, new OreIngredient(name));
            ModHandler.addShapelessRecipe(name+"Ingot", RECIPE_GROUP_BLOCKS, ingot, new OreIngredient(name));
            ModHandler.addShapelessRecipe(name+"Gem", RECIPE_GROUP_BLOCKS, gem, new OreIngredient(name));
        }

        if (!ingot.isEmpty()) ModHandler.addSmeltingAndAlloySmeltingRecipe(stack, ingot);
        if (!dust.isEmpty()) {
            if (ingot.isEmpty() && gem.isEmpty()) Recipes.compressor.addRecipe(Recipes.inputFactory.forOreDict(dustName, 9), null, true, stack);
            GtRecipes.pulverizer.addRecipe(RecipePulverizer.create(RecipeIngredientOre.create(name), dust));
        }
    }

    private void processIngot(ItemStack stack, String name) {
        ItemStack block = OreDictUnificator.get(name.replaceFirst("ingot", "block"));
        if (!block.isEmpty()) Recipes.compressor.addRecipe(Recipes.inputFactory.forOreDict(name, 9), null, true, block);

        String material = name.replace("ingot", "");
        String materialName = material.toLowerCase(Locale.ROOT);
        String plateName = name.replaceFirst("ingot", "plate");
        ItemStack plate = OreDictUnificator.get(plateName);
        if (!plate.isEmpty()) {
            GtRecipes.bender.addRecipe(RecipeSimple.create(RecipeIngredientOre.create(name), plate, 50, 20));
            ItemStack result;

            if (GregTechAPI.dynamicConfig.get("recipes", "platesNeededForArmorMadeOf"+material, true).getBoolean()) {
                if (!(result = ModHandler.removeCraftingRecipe(stack, stack, stack, stack, ItemStack.EMPTY, stack)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "helmet_" + materialName),
                            RECIPE_GROUP_ARMOR,
                            result,
                            "XXX", "XTX", 'X', plateName, 'T', "craftingToolHardHammer"
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(stack, ItemStack.EMPTY, stack, stack, stack, stack, stack, stack, stack)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "chestplate_" + materialName),
                            RECIPE_GROUP_ARMOR,
                            result,
                            "XTX", "XXX", "XXX", 'X', plateName, 'T', "craftingToolHardHammer"
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(stack, stack, stack, stack, ItemStack.EMPTY, stack, stack, ItemStack.EMPTY, stack)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "leggings_" + materialName),
                            RECIPE_GROUP_ARMOR,
                            result,
                            "XXX", "XTX", "X X", 'X', plateName, 'T', "craftingToolHardHammer"
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(stack, ItemStack.EMPTY, stack, stack, ItemStack.EMPTY, stack)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "boots_" + materialName),
                            RECIPE_GROUP_ARMOR,
                            result,
                            "XTX", "X X", 'X', plateName, 'T', "craftingToolHardHammer"
                    );
                }
            }


            if (GregTechAPI.dynamicConfig.get("recipes", "platesNeededForToolsMadeOf"+name.replace("ingot", ""), true).getBoolean()) {
                ItemStack stick = new ItemStack(Items.STICK);
                if (!(result = ModHandler.removeCraftingRecipe(ItemStack.EMPTY, stack, ItemStack.EMPTY, ItemStack.EMPTY, stack, ItemStack.EMPTY, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "sword_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            " X ", "FXT", " S ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile"
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(stack, stack, stack, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "pickaxe_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            "XII", "FST", " S ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile", 'I', name
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(ItemStack.EMPTY, stack, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "shovel_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            "FXT", " S ", " S ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile"
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(stack, stack, ItemStack.EMPTY, stack, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "axe_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            "XIT", "XS ", "FS ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile", 'I', name
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(ItemStack.EMPTY, stack, stack, ItemStack.EMPTY, stick, stack, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "axe_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            "XIT", "XS ", "FS ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile", 'I', name
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(stack, stack, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "hoe_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            "XIT", "FS ", " S ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile", 'I', name
                    );
                }
                if (!(result = ModHandler.removeCraftingRecipe(ItemStack.EMPTY, stack, stack, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick)).isEmpty()) {
                    GameRegistry.addShapedRecipe(
                            new ResourceLocation(Reference.MODID, "hoe_" + materialName),
                            RECIPE_GROUP_TOOLS,
                            result,
                            "XIT", "FS ", " S ", 'X', plateName, 'T', "craftingToolHardHammer", 'S', "stickWood", 'F', "craftingToolFile", 'I', name
                    );
                }
            }
        }


        ItemStack result;
        if (OreDictionary.doesOreNameExist("gearStone") && !(result = ModHandler.getCraftingResult(ItemStack.EMPTY, stack, ItemStack.EMPTY, stack, OreDictUnificator.getFirstOre("gearStone"), stack, ItemStack.EMPTY, stack, ItemStack.EMPTY)).isEmpty()) {
            GtRecipes.assembler.addRecipe(RecipeDualInput.create(RecipeIngredientItemStack.create(ModHandler.BC_STONE_GEAR), RecipeIngredientItemStack.create(stack, 4), result, 1600, 2));
        } else {
            if (!(result = ModHandler.getCraftingResult(ItemStack.EMPTY, stack, ItemStack.EMPTY, stack, new ItemStack(Items.IRON_INGOT), stack, ItemStack.EMPTY, stack, ItemStack.EMPTY)).isEmpty() ||
                    !(result = ModHandler.getCraftingResult(ItemStack.EMPTY, stack, ItemStack.EMPTY, stack, new ItemStack(Blocks.COBBLESTONE), stack, ItemStack.EMPTY, stack, ItemStack.EMPTY)).isEmpty()) {
                GtRecipes.assembler.addRecipe(RecipeDualInput.create(RecipeIngredientOre.create("stoneCobble"), RecipeIngredientItemStack.create(stack, 4), result, 1600, 2));
            }
        }

        switch (name) {
            case "ingotQuicksilver":
                ModHandler.addDustToIngotSmeltingRecipe(new ItemStack(BlockItems.Dust.CINNABAR.getInstance()), stack);
                break;
            case "ingotManganese":
                ModHandler.addDustToIngotSmeltingRecipe(new ItemStack(BlockItems.Dust.MANGANESE.getInstance()), stack);
                break;
            case "ingotMagnesium":
                ModHandler.addDustToIngotSmeltingRecipe(new ItemStack(BlockItems.Dust.MAGNESIUM.getInstance()), stack);
                break;
            case "ingotAluminium":
            case "ingotTitanium":
            case "ingotChrome":
            case "ingotTungsten":
            case "ingotSteel":
                FurnaceRecipes.instance().getSmeltingList().remove(stack);
                break;
        }

        ItemStack stick = OreDictUnificator.getFirstOre(name.replaceFirst("ingot","stick"));
        if (!stick.isEmpty()) {
            GameRegistry.addShapedRecipe(
                    new ResourceLocation(Reference.MODID, "stick_"+materialName),
                    RECIPE_GROUP_STICKS,
                    stick,
                    "F", "I", 'F', "craftingToolFile", 'I', name
            );
            ItemStack smallDust = OreDictUnificator.getFirstOre(name.replaceFirst("ingot", "dustSmall"), 2);
            GtRecipes.lathe.addRecipe(RecipeLathe.create(RecipeIngredientOre.create(name), Arrays.asList(stick, !smallDust.isEmpty() ? smallDust : stick), !smallDust.isEmpty() ? 50 : 150, 16));
        }

        if (GregTechAPI.dynamicConfig.hasChanged()) GregTechAPI.dynamicConfig.save();
    }

    private void processDust(ItemStack stack, String eventName) {
        if (eventName.startsWith("dustSmall")) {
            Recipes.recyclerBlacklist.add(Recipes.inputFactory.forOreDict(eventName));
            ModHandler.addShapelessRecipe(eventName.replaceFirst("dustSmall", "dust"), RECIPE_GROUP_DUSTS, stack, stack, stack, stack);
            ModHandler.addShapelessRecipe(eventName, RECIPE_GROUP_DUSTS, StackUtil.copyWithSize(stack, 4), new OreIngredient(eventName.replaceFirst("dustSmall", "dust")));
        }

        switch (eventName) {
            case "dustSteel":
                if (GregTechConfig.BLAST_FURNACE_REQUIREMENTS.steel) FurnaceRecipes.instance().getSmeltingList().remove(stack);
                else ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "steel"));
                break;
            case "dustChrome":
                if (GregTechConfig.BLAST_FURNACE_REQUIREMENTS.chrome) FurnaceRecipes.instance().getSmeltingList().remove(stack);
                else ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.CHROME.getInstance());
                break;
            case "dustTitanium":
                if (GregTechConfig.BLAST_FURNACE_REQUIREMENTS.titanium) FurnaceRecipes.instance().getSmeltingList().remove(stack);
                else ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.TITANIUM.getInstance());
                break;
            case "dustAluminium":
            case "dustAluminum":
                if (GregTechConfig.BLAST_FURNACE_REQUIREMENTS.aluminium) FurnaceRecipes.instance().getSmeltingList().remove(stack);
                else ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.ALUMINIUM.getInstance());
                break;
            case "dustTungsten":
                if (GregTechConfig.BLAST_FURNACE_REQUIREMENTS.tungsten) FurnaceRecipes.instance().getSmeltingList().remove(stack);
                else ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.TUNGSTEN.getInstance());
                break;
            case "dustInvar":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.INVAR.getInstance());
                break;
            case "dustBronze":
                ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "bronze"));
                break;
            case "dustBrass":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.BRASS.getInstance());
                break;
            case "dustCopper":
                ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "copper"));
                break;
            case "dustGold":
                ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "gold"));
                break;
            case "dustNickel":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.NICKEL.getInstance());
                break;
            case "dustIron":
                ModHandler.addDustToIngotSmeltingRecipe(stack, Items.IRON_INGOT);
                break;
            case "dustAntimony":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.ANTIMONY.getInstance());
                break;
            case "dustTin":
                ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "tin"));
                break;
            case "dustZinc":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.ZINC.getInstance());
                break;
            case "dustPlatinum":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.PLATINUM.getInstance());
                break;
            case "dustLead":
                ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "lead"));
                break;
            case "dustSilver":
                ModHandler.addDustToIngotSmeltingRecipe(stack, IC2Items.getItem("ingot", "silver"));
                break;
            case "dustCoal":
                ModHandler.addLiquidTransposerFillRecipe(stack, new FluidStack(FluidRegistry.WATER, 125), IC2Items.getItem("dust", "coal_fuel"), 1250);
                break;
            case "dustElectrum":
                ModHandler.addDustToIngotSmeltingRecipe(stack, BlockItems.Ingot.ELECTRUM.getInstance());
                break;
            case "dustWheat":
            case "dustFlour":
                GameRegistry.addSmelting(stack, new ItemStack(Items.BREAD), 0);
                break;
        }
    }

    private void processOre(ItemStack stack, String name) {
        String dustName = name.replaceFirst("ore", "dust");
        ItemStack dust = OreDictUnificator.getFirstOre(dustName);
        if (!dust.isEmpty()) {
            GameRegistry.addShapedRecipe(
                    new ResourceLocation(Reference.MODID, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, dustName+"Crushing")),
                    RECIPE_GROUP_ORES,
                    dust,
                    "T", "O", 'T', "craftingToolHardHammer", 'O', name);
        }

        switch (name) {
            case "oreIron":
            case "orePyrite":
                ModHandler.addInductionSmelterRecipe(stack, new ItemStack(Blocks.SAND), new ItemStack(Items.IRON_INGOT), ModHandler.SLAG, 3000, 10);
                ModHandler.addInductionSmelterRecipe(stack, ModHandler.SLAG_RICH, new ItemStack(Items.IRON_INGOT, 2), ModHandler.SLAG, 3000, 95);
                ModHandler.addSmelterOreToIngotsRecipe(stack, Items.IRON_INGOT);
                break;
            case "oreGold":
                ModHandler.addSmelterOreToIngotsRecipe(stack, Items.GOLD_INGOT);
                break;
            case "oreSilver":
                ModHandler.addSmelterOreToIngotsRecipe(stack, IC2Items.getItem("ingot", "silver"));
                break;
            case "oreLead":
                ModHandler.addSmelterOreToIngotsRecipe(stack, IC2Items.getItem("ingot", "lead"));
                break;
            case "oreCopper":
                ModHandler.addSmelterOreToIngotsRecipe(stack, IC2Items.getItem("ingot", "copper"));
                break;
            case "oreTin":
                ModHandler.addSmelterOreToIngotsRecipe(stack, IC2Items.getItem("ingot", "tin"));
                break;
            case "oreZinc":
                ModHandler.addSmelterOreToIngotsRecipe(stack, BlockItems.Ingot.ZINC.getInstance());
                break;
            case "oreCassiterite":
                ModHandler.addSmelterOreToIngotsRecipe(stack, StackUtil.copyWithSize(IC2Items.getItem("ingot", "tin"), 2));
                break;
            case "oreAntimony":
                ModHandler.addSmelterOreToIngotsRecipe(stack, BlockItems.Ingot.ANTIMONY.getInstance());
                break;
            case "oreCooperite":
            case "oreSheldonite":
            case "orePlatinum":
                ModHandler.addSmelterOreToIngotsRecipe(stack, BlockItems.Ingot.PLATINUM.getInstance());
                break;
            case "oreNickel":
                ModHandler.addSmelterOreToIngotsRecipe(stack, BlockItems.Ingot.NICKEL.getInstance());
                break;
            case "oreAluminium":
            case "oreAluminum":
                ModHandler.addSmelterOreToIngotsRecipe(stack, GregTechConfig.BLAST_FURNACE_REQUIREMENTS.aluminium ? BlockItems.Dust.ALUMINIUM.getInstance() : BlockItems.Ingot.ALUMINIUM.getInstance());
                break;
            case "oreSteel":
                ModHandler.addSmelterOreToIngotsRecipe(stack, GregTechConfig.BLAST_FURNACE_REQUIREMENTS.steel ? new ItemStack(BlockItems.Dust.STEEL.getInstance()) : IC2Items.getItem("ingot", "steel"));
                break;
            case "oreTitan":
            case "oreTitanium":
                ModHandler.addSmelterOreToIngotsRecipe(stack, GregTechConfig.BLAST_FURNACE_REQUIREMENTS.titanium ? BlockItems.Dust.TITANIUM.getInstance() : BlockItems.Ingot.TITANIUM.getInstance());
                break;
            case "oreChrome":
            case "oreChromium":
                ModHandler.addSmelterOreToIngotsRecipe(stack, GregTechConfig.BLAST_FURNACE_REQUIREMENTS.chrome ? BlockItems.Dust.CHROME.getInstance() : BlockItems.Ingot.CHROME.getInstance());
                break;
            case "oreElectrum":
                ModHandler.addSmelterOreToIngotsRecipe(stack, BlockItems.Ingot.ELECTRUM.getInstance());
                break;
            case "oreTungsten":
            case "oreTungstate":
                ModHandler.addSmelterOreToIngotsRecipe(stack, GregTechConfig.BLAST_FURNACE_REQUIREMENTS.tungsten ? BlockItems.Dust.TUNGSTEN.getInstance() : BlockItems.Ingot.TUNGSTEN.getInstance());
                break;
            case "orePhosphorite":
                GameRegistry.addSmelting(stack, new ItemStack(BlockItems.Dust.PHOSPHORUS.getInstance(), 2), 0);
                break;
            case "oreSaltpeter":
                GameRegistry.addSmelting(stack, new ItemStack(BlockItems.Dust.SALTPETER.getInstance(), 3), 0);
                break;
            case "oreSulfur":
                GameRegistry.addSmelting(stack, StackUtil.copyWithSize(IC2Items.getItem("dust", "sulfur"), 3), 0);
                break;
        }
    }
}
