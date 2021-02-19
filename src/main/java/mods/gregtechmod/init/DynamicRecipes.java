package mods.gregtechmod.init;

import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSawmill;
import mods.gregtechmod.recipe.RecipeDualInput;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.*;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    static final IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipePulverizer> PULVERIZER = new RecipeManagerPulverizer();
    static final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> ALLOY_SMELTER = new RecipeManagerMultiInput<>();
    static final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> CANNER = new RecipeManagerMultiInput<>();
    static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IGtMachineRecipe<IRecipeIngredient, List<ItemStack>>> LATHE = new RecipeManagerBasic<>();
    static final IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> ASSEMBLER = new RecipeManagerMultiInput<>();
    static final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IGtMachineRecipe<IRecipeIngredient, ItemStack>> BENDER = new RecipeManagerBasic<>();
    static final IGtRecipeManagerSawmill SAWMILL = new RecipeManagerSawmill();

    static final GtBasicMachineRecipeManager COMPRESSOR = new GtBasicMachineRecipeManager();

    static void addPulverizerRecipe(IRecipePulverizer recipe) {
        if (addPulverizerRecipes) PULVERIZER.addRecipe(recipe);
    }

    static void addAlloySmelterRecipe(IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> recipe) {
        if (addAlloySmelterRecipes) ALLOY_SMELTER.addRecipe(recipe);
    }

    static void addCannerRecipe(IGtMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe) {
        if (addCannerRecipes) CANNER.addRecipe(recipe);
    }

    static void addLatheRecipe(IGtMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        if (addLatheRecipes) LATHE.addRecipe(recipe);
    }

    static void addAssemblerRecipe(IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> recipe) {
        if (addAssemblerRecipes) ASSEMBLER.addRecipe(recipe);
    }

    static void addBenderRecipe(IGtMachineRecipe<IRecipeIngredient, ItemStack> recipe) {
        if (addBenderRecipes) BENDER.addRecipe(recipe);
    }

    static void addSawmillRecipe(IRecipeSawmill recipe) {
        if (addSawmillRecipes) {
            SAWMILL.addRecipe(recipe);
            List<ItemStack> outputs = recipe.getOutput();
            ModHandler.addTESawmillRecipe(1600, recipe.getInput().getMatchingInputs().get(0), outputs.get(0), outputs.size() > 1 ? outputs.get(1) : ItemStack.EMPTY, 100, false);
        }
    }

    static void addCompressorRecipe(IRecipeInput input, ItemStack output) {
        if (addCompressorRecipes) COMPRESSOR.addRecipe(input, output);
    }

    static void addInductionSmelterRecipe(String name, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        if (GregTechAPI.dynamicConfig.get("inductionSmelter", name, true).getBoolean()) ModHandler.addInductionSmelterRecipe(primaryInput, secondaryInput, primaryOutput, secondaryOutput, energy, chance);
    }

    static void addSmeltingAndAlloySmeltingRecipe(String inputName, ItemStack input, ItemStack output) {
        if (input.isEmpty() || output.isEmpty()) return;

        addSmeltingRecipe(inputName, input, output);
        addAlloySmelterRecipe(RecipeDualInput.create(Collections.singletonList(RecipeIngredientOre.create(inputName)), output, 130, 3));
        ModHandler.addInductionSmelterRecipe(input, new ItemStack(Blocks.SAND), output, ItemStack.EMPTY, output.getCount() * 1000, 0);
    }

    static void addDustToIngotSmeltingRecipe(String name, ItemStack input, Item output) {
        addDustToIngotSmeltingRecipe(name, input, new ItemStack(output));
    }

    static void addDustToIngotSmeltingRecipe(String name, ItemStack input, ItemStack output) {
        ItemStack dust = StackUtil.copyWithSize(input, 2);
        ItemStack ingots = StackUtil.copyWithSize(output, 2);
        SmelterManager.addRecipe(800, dust, new ItemStack(Blocks.SAND), ingots, ModHandler.SLAG, 25);
        addSmeltingRecipe(name, input, output);
    }

    static boolean addSmeltingRecipe(String name, ItemStack input, ItemStack output) {
        if (!GregTechAPI.dynamicConfig.get("smelting", name, true).getBoolean()) return false;

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

        ModHandler.addInductionSmelterRecipe(ore, new ItemStack(Blocks.SAND), ingots2, ModHandler.SLAG_RICH, 3200, 5);
        ModHandler.addInductionSmelterRecipe(ore, ModHandler.SLAG_RICH, ingots3, ModHandler.SLAG, 4000, 75);
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
}
