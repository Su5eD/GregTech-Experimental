package mods.gregtechmod.init;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.*;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSawmill;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.compat.ModCompat;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.recipe.RecipeAlloySmelter;
import mods.gregtechmod.recipe.RecipePulverizer;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

class DynamicRecipes {
    static boolean addPulverizerRecipes;
    static boolean addAlloySmelterRecipes;
    static boolean addCannerRecipes;
    static boolean addLatheRecipes;
    static boolean addAssemblerRecipes;
    static boolean addBenderRecipes;
    static boolean addSawmillRecipes;
    static boolean addCompressorRecipes;
    static boolean addExtractorRecipes;
    static boolean addCentrifugeRecipes;

    static final IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipePulverizer> PULVERIZER = new RecipeManagerPulverizer();
    static final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeAlloySmelter> ALLOY_SMELTER = new RecipeManagerAlloySmelter();
    static final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> CANNER = new RecipeManagerMultiInput<>();
    static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> LATHE = new RecipeManagerBasic<>();
    static final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, ItemStack>> ASSEMBLER = new RecipeManagerMultiInput<>();
    static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, ItemStack>> BENDER = new RecipeManagerBasic<>();
    static final IGtRecipeManagerSawmill SAWMILL = new RecipeManagerSawmill();
    static final IGtRecipeManagerCellular INDUSTRIAL_CENTRIFUGE = new RecipeManagerCellular();
    static final GtBasicMachineRecipeManager COMPRESSOR = new GtBasicMachineRecipeManager();
    static final GtBasicMachineRecipeManager EXTRACTOR = new GtBasicMachineRecipeManager();

    private static final Set<MaterialUsage> MATERIAL_USAGES = new HashSet<>();

    static void addPulverizerRecipe(ItemStack input, ItemStack output, boolean overwrite) {
        addPulverizerRecipe(input, output, 10, overwrite);
    }

    static void addPulverizerRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        addPulverizerRecipe(input, primaryOutput, secondaryOutput, chance, false);
    }

    static void addPulverizerRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        addPulverizerRecipe(RecipePulverizer.create(RecipeIngredientItemStack.create(input), Arrays.asList(primaryOutput, secondaryOutput), chance, overwrite));
    }

    static void addPulverizerRecipe(ItemStack input, ItemStack output, int chance, boolean overwrite) {
        addPulverizerRecipe(RecipePulverizer.create(RecipeIngredientItemStack.create(input), Collections.singletonList(output), chance, overwrite));
    }

    static void addPulverizerRecipe(IRecipePulverizer recipe) {
        if (addPulverizerRecipes) PULVERIZER.addRecipe(recipe);
    }

    static void addAlloySmelterRecipe(IRecipeAlloySmelter recipe) {
        if (addAlloySmelterRecipes) ALLOY_SMELTER.addRecipe(recipe);
    }

    static void addCannerRecipe(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe) {
        if (addCannerRecipes) CANNER.addRecipe(recipe);
    }

    static void addLatheRecipe(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        if (addLatheRecipes) LATHE.addRecipe(recipe);
    }

    static void addAssemblerRecipe(IMachineRecipe<List<IRecipeIngredient>, ItemStack> recipe) {
        if (addAssemblerRecipes) ASSEMBLER.addRecipe(recipe);
    }

    static void addBenderRecipe(IMachineRecipe<IRecipeIngredient, ItemStack> recipe) {
        if (addBenderRecipes) BENDER.addRecipe(recipe);
    }

    static void addSawmillRecipe(IRecipeSawmill recipe) {
        if (addSawmillRecipes) SAWMILL.addRecipe(recipe);
    }

    static void addCentrifugeRecipe(IRecipeCellular recipe) {
        if (addCentrifugeRecipes) INDUSTRIAL_CENTRIFUGE.addRecipe(recipe);
    }

    static void addCompressorRecipe(IRecipeInput input, ItemStack output) {
        if (addCompressorRecipes) COMPRESSOR.addRecipe(input, output);
    }

    static void addExtractorRecipe(IRecipeInput input, ItemStack output) {
        if (addExtractorRecipes) EXTRACTOR.addRecipe(input, output);
    }

    static void addInductionSmelterRecipe(String name, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        if (GregTechAPI.getDynamicConfig("induction_smelter", name, true)) ModHandler.addInductionSmelterRecipe(primaryInput, secondaryInput, primaryOutput, secondaryOutput, energy, chance);
    }

    static void addSmeltingAndAlloySmeltingRecipe(ItemStack input, ItemStack output) {
        addSmeltingAndAlloySmeltingRecipe(input, output, false);
    }

    static void addSmeltingAndAlloySmeltingRecipe(ItemStack input, ItemStack output, boolean overwrite) {
        addSmeltingAndAlloySmeltingRecipe(null, input, output, overwrite);
    }

    static void addSmeltingAndAlloySmeltingRecipe(@Nullable String oredict, ItemStack input, ItemStack output) {
        addSmeltingAndAlloySmeltingRecipe(oredict, input, output, false);
    }

    static void addSmeltingAndAlloySmeltingRecipe(@Nullable String oredict, ItemStack input, ItemStack output, boolean overwrite) {
        if (!input.isEmpty() && !output.isEmpty()) {
            if (overwrite) ModHandler.removeInductionSelterRecipe(input);
            IRecipeIngredient ingredient = oredict != null ? RecipeIngredientOre.create(oredict) : RecipeIngredientItemStack.create(input);
            addAlloySmelterRecipe(RecipeAlloySmelter.create(Collections.singletonList(ingredient), output, 130, 3, true));
        }
    }

    static void addDustToIngotSmeltingRecipe(String name, ItemStack input, Item output) {
        addDustToIngotSmeltingRecipe(name, input, new ItemStack(output));
    }

    static void addDustToIngotSmeltingRecipe(String name, ItemStack input, ItemStack output) {
        ItemStack dust = StackUtil.copyWithSize(input, 2);
        ItemStack ingots = StackUtil.copyWithSize(output, 2);
        ModHandler.addInductionSmelterRecipe(dust, new ItemStack(Blocks.SAND), ingots, ModHandler.slag, 800, 25);
        addSmeltingRecipe(name, input, output);
    }

    static boolean addSmeltingRecipe(String name, ItemStack input, ItemStack output) {
        if (!GregTechAPI.getDynamicConfig("smelting", name, true)) return false;

        FurnaceRecipes recipes = FurnaceRecipes.instance();
        Map<ItemStack, ItemStack> smeltingList = recipes.getSmeltingList();
        smeltingList.keySet()
                .stream()
                .filter(input::isItemEqual)
                .collect(Collectors.toList())
                .forEach(smeltingList::remove);
        recipes.addSmeltingRecipe(input, output, 0);
        return true;
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack input, Item output) {
        addSmelterOreToIngotsRecipe(input, new ItemStack(output));
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack input, ItemStack output) {
        ItemStack ore = StackUtil.copyWithSize(input, 1);
        ItemStack ingots2 = StackUtil.copyWithSize(output, 2);
        ItemStack ingots3 = StackUtil.copyWithSize(output, 3);

        ModHandler.addInductionSmelterRecipe(ore, new ItemStack(Blocks.SAND), ingots2, ModHandler.slagRich, 3200, 5);
        ModHandler.addInductionSmelterRecipe(ore, ModHandler.slagRich, ingots3, ModHandler.slag, 4000, 75);
        GameRegistry.addSmelting(input, output, 0);
    }

    public static void addIngotToBlockRecipe(String material, ItemStack input, ItemStack output, boolean crafting, boolean decrafting) {
        addCompressorRecipe(Recipes.inputFactory.forOreDict(material, 9), output);

        OreDictionary.getOres(material).forEach(stack -> ModHandler.removeFactorizerRecipe(stack, false));
        input = StackUtil.copyWithSize(input, 9);
        if (crafting) ModHandler.addFactorizerRecipe(input, output, false);

        if (!decrafting) ModHandler.removeFactorizerRecipe(output, true);
        else ModHandler.addFactorizerRecipe(output, input, true);
    }

    public static void processCraftingRecipes() {
        GregTechAPI.logger.info("Scanning for certain kinds of compatible machine blocks");
        ItemStack stack;

        ItemStack input = IC2Items.getItem("ingot", "bronze");
        ItemStack plateBronze = IC2Items.getItem("plate", "bronze");
        if (!(stack = ModHandler.getCraftingResult(input, input, input, input, ItemStack.EMPTY, input, input, input, input)).isEmpty() || !(stack = ModHandler.getCraftingResult(plateBronze, plateBronze, plateBronze, plateBronze, ItemStack.EMPTY, plateBronze, plateBronze, plateBronze, plateBronze)).isEmpty()) {
            OreDictUnificator.registerOre("craftingRawMachineTier00", stack);
            addPulverizerRecipe(RecipePulverizer.create(RecipeIngredientItemStack.create(stack), StackUtil.setSize(IC2Items.getItem("dust", "bronze"), 8)));
            addSmeltingRecipe(stack.getTranslationKey(), stack, StackUtil.setSize(IC2Items.getItem("ingot", "bronze"), 8));
        }

        ItemStack glass = new ItemStack(Blocks.GLASS);
        ItemStack gearTin = OreDictUnificator.get("gearTin");
        ItemStack dustTin = StackUtil.setSize(IC2Items.getItem("dust", "tin"), 4);
        ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
        if (!(stack = ModHandler.getCraftingResult(ingotIron, glass, ingotIron, glass, gearTin, glass, ingotIron, glass, ingotIron)).isEmpty()) {
            OreDictUnificator.registerOre("craftingRawMachineTier00", stack);
            addPulverizerRecipe(RecipePulverizer.create(RecipeIngredientItemStack.create(stack), StackUtil.setSize(IC2Items.getItem("dust", "iron"), 4), dustTin));
        }

        ItemStack ingotSteel = IC2Items.getItem("ingot", "steel");
        if (!(stack = ModHandler.getCraftingResult(ingotSteel, glass, ingotSteel, glass, gearTin, glass, ingotSteel, glass, ingotSteel)).isEmpty()) {
            OreDictUnificator.registerOre("craftingRawMachineTier01", stack);
            addPulverizerRecipe(RecipePulverizer.create(RecipeIngredientItemStack.create(stack), StackUtil.setSize(IC2Items.getItem("dust", "steel"), 4), dustTin));
        }

        if (IC2.version.isClassic()) {
            ItemStack ingotRefinedIron = OreDictUnificator.get("ingotRefinedIron");
            ItemStack plateRefinedIron = OreDictUnificator.get("plateRefinedIron");
            if (!(stack = ModHandler.getCraftingResult(ingotRefinedIron, ingotRefinedIron, ingotRefinedIron, ingotRefinedIron, ItemStack.EMPTY, ingotRefinedIron, ingotRefinedIron, ingotRefinedIron, ingotRefinedIron)).isEmpty() ||
                    !(stack = ModHandler.getCraftingResult(plateRefinedIron, plateRefinedIron, plateRefinedIron, plateRefinedIron, ItemStack.EMPTY, plateRefinedIron, plateRefinedIron, plateRefinedIron, plateRefinedIron)).isEmpty()) {
                OreDictUnificator.registerOre("craftingRawMachineTier01", stack);
                addPulverizerRecipe(RecipePulverizer.create(RecipeIngredientItemStack.create(stack), OreDictUnificator.get("dustRefinedIron", StackUtil.setSize(IC2Items.getItem("dust", "iron"), 8), 8)));
                addSmeltingRecipe(stack.getTranslationKey(), stack, StackUtil.copyWithSize(ingotRefinedIron, 8));
            }
        }

        ItemStack iridiumAlloy = IC2Items.getItem("crafting", "iridium");
        ModHandler.removeCraftingRecipe(iridiumAlloy);
        boolean harder = GregTechAPI.getDynamicConfig("harder_recipes", "iridium_plate", true);
        ModCompat.addRollingMachineRecipe(
                "plateAlloyIridium",
                harder ? new ItemStack(BlockItems.Ingot.IRIDIUM_ALLOY.getInstance()) : iridiumAlloy,
                "IAI", "ADA", "IAI", 'D', "dustDiamond", 'A', "plateAlloyAdvanced", 'I', "plateIridium"
        );
    }

    public static void applyMaterialUsages() {
        gatherMaterialUsage(BlockItems.Miscellaneous.RUBY.getInstance(), BlockItems.Dust.RUBY.getInstance(), false, true);
        gatherMaterialUsage(BlockItems.Miscellaneous.SAPPHIRE.getInstance(), BlockItems.Dust.SAPPHIRE.getInstance(), false, true);
        gatherMaterialUsage(BlockItems.Miscellaneous.GREEN_SAPPHIRE.getInstance(), BlockItems.Dust.GREEN_SAPPHIRE.getInstance(), false, true);
        gatherMaterialUsage(BlockItems.Ingot.BRASS.getInstance(), BlockItems.Dust.BRASS.getInstance(), true, true);
        gatherMaterialUsage(IC2Items.getItem("ingot", "silver"), IC2Items.getItem("dust", "silver"), true, true);
        gatherMaterialUsage(BlockItems.Miscellaneous.OLIVINE.getInstance(), BlockItems.Dust.OLIVINE.getInstance(), false, true);
        gatherMaterialUsage(BlockItems.Ingot.ALUMINIUM.getInstance(), BlockItems.Dust.ALUMINIUM.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.TITANIUM.getInstance(), BlockItems.Dust.TITANIUM.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.CHROME.getInstance(), BlockItems.Dust.CHROME.getInstance(), true, true);
        gatherMaterialUsage(IC2Items.getItem("ingot", "steel"), new ItemStack(BlockItems.Dust.STEEL.getInstance()), true, true);
        gatherMaterialUsage(BlockItems.Ingot.ELECTRUM.getInstance(), BlockItems.Dust.ELECTRUM.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.TUNGSTEN.getInstance(), BlockItems.Dust.TUNGSTEN.getInstance(), true, true);
        gatherMaterialUsage(IC2Items.getItem("ingot", "lead"), IC2Items.getItem("dust", "lead"), true, true);
        gatherMaterialUsage(BlockItems.Ingot.ZINC.getInstance(), BlockItems.Dust.ZINC.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.PLATINUM.getInstance(), BlockItems.Dust.PLATINUM.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.NICKEL.getInstance(), BlockItems.Dust.NICKEL.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.INVAR.getInstance(), BlockItems.Dust.INVAR.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.ANTIMONY.getInstance(), BlockItems.Dust.ANTIMONY.getInstance(), true, true);
        gatherMaterialUsage(BlockItems.Ingot.TUNGSTEN_STEEL.getInstance(), BlockItems.Ingot.TUNGSTEN_STEEL.getInstance(), true, false);
        ItemStack iridiumOre = IC2Items.getItem("misc_resource", "iridium_ore");
        gatherMaterialUsage(new ItemStack(BlockItems.Ingot.IRIDIUM.getInstance()), iridiumOre, true, true);
        gatherMaterialUsage(iridiumOre, iridiumOre, false, true);
        gatherMaterialUsage(BlockItems.Ingot.OSMIUM.getInstance(), BlockItems.Dust.OSMIUM.getInstance(), false, true);
        ItemStack dustIron = IC2Items.getItem("dust", "iron");
        if (IC2.version.isClassic()) gatherMaterialUsage(IC2Items.getItem("ingot", "refined_iron"), dustIron, true, true);
        gatherMaterialUsage(IC2Items.getItem("ingot", "bronze"), IC2Items.getItem("dust", "bronze"), true, true);
        gatherMaterialUsage(IC2Items.getItem("ingot", "copper"), IC2Items.getItem("dust", "copper"), true, true);
        gatherMaterialUsage(IC2Items.getItem("ingot", "tin"), IC2Items.getItem("dust", "tin"), true, true);
        gatherMaterialUsage(new ItemStack(Items.IRON_INGOT), dustIron, true, true);
        gatherMaterialUsage(new ItemStack(Items.GOLD_INGOT), IC2Items.getItem("dust", "gold"), true, true);
        gatherMaterialUsage(new ItemStack(Items.DIAMOND), IC2Items.getItem("dust", "diamond"), false, true);
        gatherMaterialUsage(new ItemStack(Blocks.PLANKS), new ItemStack(BlockItems.Smalldust.WOOD.getInstance()), false, true);
        if (ModHandler.thaumcraft) {
            ItemStack ingotThaumium = ModHandler.getTCItem("ingot", 0);
            gatherMaterialUsage(ingotThaumium, ingotThaumium, true, false);
        }
    }

    public static void gatherMaterialUsage(Item input, Item output, boolean backSmelting, boolean backMacerating) {
        gatherMaterialUsage(new ItemStack(input), new ItemStack(output), backSmelting, backMacerating);
    }

    private static void gatherMaterialUsage(ItemStack input, ItemStack output, boolean backSmelting, boolean backMacerating) {
        if (input.isEmpty() || output.isEmpty()) return;
        input = input.copy();
        output = output.copy();
        ItemStack stick = new ItemStack(Items.STICK);
        if (output.getCount() < 1) output.setCount(1);

        boolean isSawdust = OreDictUnificator.isItemInstanceOf(output, "dustSmallWood", false);
        IRecipe recipe;
        if (!input.isItemEqual(new ItemStack(Items.IRON_INGOT))) {
            ItemStack bucket = new ItemStack(Items.BUCKET);
            ItemStack minecart = new ItemStack(Items.MINECART);
            if ((recipe = ModHandler.getCraftingRecipe(input, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)) != null &&
                    recipe.getRecipeOutput().isItemEqual(bucket))
                ModHandler.removeCraftingRecipe(recipe);
            if ((recipe = ModHandler.getCraftingRecipe(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY)) != null &&
                    recipe.getRecipeOutput().isItemEqual(bucket))
                ModHandler.removeCraftingRecipe(recipe);
            if ((recipe = ModHandler.getCraftingRecipe(input, ItemStack.EMPTY, input, input, input, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)) != null &&
                    recipe.getRecipeOutput().isItemEqual(minecart))
                ModHandler.removeCraftingRecipe(recipe);
            if ((recipe = ModHandler.getCraftingRecipe(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, input, input, input, input)) != null &&
                    recipe.getRecipeOutput().isItemEqual(minecart))
                ModHandler.removeCraftingRecipe(recipe);
        }

        findMaterialUsage(input, output, 6, backMacerating, backSmelting, input, ItemStack.EMPTY, input, input, input, input, ItemStack.EMPTY, input, ItemStack.EMPTY);
        findMaterialUsage(input, output, 6, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, input, input, input, input, ItemStack.EMPTY, input);
        findMaterialUsage(input, output, 5, backMacerating, backSmelting, input, input, input, input, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        findMaterialUsage(input, output, 8, backMacerating, backSmelting, input, ItemStack.EMPTY, input, input, input, input, input, input, input);
        findMaterialUsage(input, output, 7, backMacerating, backSmelting, input, input, input, input, ItemStack.EMPTY, input, input, ItemStack.EMPTY, input);
        findMaterialUsage(input, output, 4, backMacerating, backSmelting, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, input, input, ItemStack.EMPTY, input);
        findMaterialUsage(input, output, 2, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, input, input, ItemStack.EMPTY, input);
        findMaterialUsage(input, output, 2, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 5, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, input, input, input, input, stick, input, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, input, input, input, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 1, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, input, input, ItemStack.EMPTY, input, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, input, input, ItemStack.EMPTY, stick, input, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 2, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, input, input, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 2, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, input, input, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, input, stick);
        findMaterialUsage(input, output, 3, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, input, stick, input, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, stick);
        findMaterialUsage(input, output, 3, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, input, ItemStack.EMPTY, input, stick, ItemStack.EMPTY, ItemStack.EMPTY);
        findMaterialUsage(input, output, 2, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, input, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, input, input, input);
        findMaterialUsage(input, output, 1, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, input, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, input, input, ItemStack.EMPTY);
        findMaterialUsage(input, output, 3, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, stick, input, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, input, input);
        findMaterialUsage(input, output, 2, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, input, input, ItemStack.EMPTY);
        findMaterialUsage(input, output, 1, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, stick);
        findMaterialUsage(input, output, 1, isSawdust ? 4 : 0, 100, backMacerating, backSmelting, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, stick, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY);
        findMaterialUsage(input, output, 1, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, input, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        findMaterialUsage(input, output, 1, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, ItemStack.EMPTY, ItemStack.EMPTY, input, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        findMaterialUsage(input, output, 1, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, input, stick, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        findMaterialUsage(input, output, 1, isSawdust ? 2 : 0, 50, backMacerating, backSmelting, input, ItemStack.EMPTY, ItemStack.EMPTY, stick, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    private static void findMaterialUsage(ItemStack input, ItemStack output, int count, boolean backMacerating, boolean backSmelting, ItemStack... pattern) {
        findMaterialUsage(input, output, count, 0, 0, backMacerating, backSmelting, pattern);
    }

    private static void findMaterialUsage(ItemStack input, ItemStack output, int count, int bonus, int chance, boolean backMacerating, boolean backSmelting, ItemStack... pattern) {
        IRecipe recipe = ModHandler.getCraftingRecipe(pattern);
        if (recipe != null) MATERIAL_USAGES.add(new MaterialUsage(recipe.getRecipeOutput(), input, output, count, bonus, chance, backMacerating, backSmelting));
    }

    public static void processMaterialUsages() {
        ItemStack sawdust = new ItemStack(BlockItems.Dust.WOOD.getInstance());
        MATERIAL_USAGES.forEach(material -> {
            if (material.backMacerating) {
                if (material.bonus > 0) addPulverizerRecipe(material.recipeOutput, StackUtil.copyWithSize(material.output, material.count * material.output.getCount() + material.bonus), material.chance, true);
                else addPulverizerRecipe(material.recipeOutput, StackUtil.copyWithSize(material.output, material.count * material.output.getCount()), sawdust, material.chance, true);
            }
            if (material.backSmelting && material.recipeOutput.getCount() == 1 && material.input.getCount() == 1) addSmeltingAndAlloySmeltingRecipe(material.recipeOutput, StackUtil.copyWithSize(material.input, material.count), true);
        });
    }

    private static class MaterialUsage {
        public final ItemStack recipeOutput;
        public final ItemStack input;
        public final ItemStack output;
        public final int count;
        public final int bonus;
        public final int chance;
        public final boolean backMacerating;
        public final boolean backSmelting;

        private MaterialUsage(ItemStack recipeOutput, ItemStack input, ItemStack output, int count, int bonus, int chance, boolean backMacerating, boolean backSmelting) {
            this.recipeOutput = recipeOutput;
            this.input = input;
            this.output = output;
            this.count = count;
            this.bonus = bonus;
            this.chance = chance;
            this.backMacerating = backMacerating;
            this.backSmelting = backSmelting;
        }
    }
}
