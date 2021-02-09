package mods.gregtechmod.util;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import appeng.api.features.IGrinderRegistry;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalexpansion.util.managers.machine.TransposerManager;
import com.google.common.base.CaseFormat;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.recipe.RecipeDualInput;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModHandler {
    public static ItemStack SLAG = ItemStack.EMPTY;
    public static ItemStack SLAG_RICH = ItemStack.EMPTY;
    public static ItemStack HARDENED_GLASS = ItemStack.EMPTY;
    public static ItemStack BC_STONE_GEAR = ItemStack.EMPTY;

    public static void gatherModItems() {
        Item material = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"));
        if (material != null) {
            SLAG = new ItemStack(material, 1, 864);
            SLAG_RICH = new ItemStack(material, 1, 865);
        }

        Item glass = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "glass"));
        if (glass != null) HARDENED_GLASS = new ItemStack(glass, 1, 3);

        Item stoneGear = ForgeRegistries.ITEMS.getValue(new ResourceLocation("buildcraftcore", "gear_stone"));
        if (stoneGear != null) BC_STONE_GEAR = new ItemStack(stoneGear);
    }

    public static ItemStack getModItem(String modid, String itemName) {
        return getModItem(modid, itemName, 0);
    }

    public static ItemStack getModItem(String modid, String itemName, int meta) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, itemName));
        if (item == null) return ItemStack.EMPTY;
        return new ItemStack(item, 1, meta);
    }

    public static ItemStack getTEItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalexpansion", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getTFItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getRCItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("railcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getPRItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("projectred-core", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getFRItem(String baseItem) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base);
    }

    public static ItemStack getTCItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack output, boolean overwrite) {
        addTESawmillRecipe(energy, input, output, ItemStack.EMPTY, 0, overwrite);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (Loader.isModLoaded("thermalexpansion")) registerSawmillRecipe(energy, input, primaryOutput, secondaryOutput, chance, overwrite);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (overwrite && SawmillManager.recipeExists(input)) SawmillManager.removeRecipe(input);
        SawmillManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    public static void addTEPulverizerRecipe(int energy, ItemStack input, ItemStack output, boolean overwrite) {
        addTEPulverizerRecipe(energy, input, output, ItemStack.EMPTY, 0, overwrite);
    }

    public static void addTEPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (Loader.isModLoaded("thermalexpansion")) registerPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, chance, overwrite);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (overwrite && PulverizerManager.recipeExists(input)) PulverizerManager.removeRecipe(input);
        PulverizerManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    public static void addInductionSmelterRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        if (Loader.isModLoaded("thermalexpansion")) registerInductionsmelterRecipe(primaryInput, secondaryInput, primaryOutput, secondaryOutput, energy, chance);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerInductionsmelterRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        SmelterManager.addRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, chance);
    }

    public static void addSmelterOreToIngotsRecipe(String ore, Item output) {
        addSmelterOreToIngotsRecipe(ore, new ItemStack(output));
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack input, Item output) {
        addSmelterOreToIngotsRecipe(input, new ItemStack(output));
    }

    public static void addSmelterOreToIngotsRecipe(List<String> ores, Item output) {
        ItemStack outputStack = new ItemStack(output);
        ores.forEach(ore -> addSmelterOreToIngotsRecipe(ore, outputStack));
    }

    public static void addSmelterOreToIngotsRecipe(String ore, ItemStack output) {
        OreDictionary.getOres(ore).forEach(stack -> addSmelterOreToIngotsRecipe(stack, output));
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack input, ItemStack output) {
        ItemStack ore = StackUtil.copyWithSize(input, 1);
        ItemStack ingots2 = StackUtil.copyWithSize(output, 2);
        ItemStack ingots3 = StackUtil.copyWithSize(output, 3);

        addInductionSmelterRecipe(ore, new ItemStack(Blocks.SAND), ingots2, SLAG_RICH, 3200, 5);
        addInductionSmelterRecipe(ore, SLAG_RICH, ingots3, SLAG, 4000, 75);
        GameRegistry.addSmelting(input, output, 0);
    }

    public static void addLiquidTransposerFillRecipe(ItemStack emptyContainer, FluidStack fluid, ItemStack fullContainer, int energy) {
        if (Loader.isModLoaded("thermalexpansion")) registerLiquidTransposerFillRecipe(emptyContainer, fluid, fullContainer, energy);
    }
    @Optional.Method(modid = "thermalexpansion")
    public static void registerLiquidTransposerFillRecipe(ItemStack emptyContainer, FluidStack fluid, ItemStack fullContainer, int energy) {
        TransposerManager.addFillRecipe(energy, emptyContainer, fullContainer, fluid, false);
    }

    public static void addRockCrusherRecipe(ItemStack input, boolean matchMeta, boolean matchNBT, Map<ItemStack, Float> outputs) {
        NBTTagCompound nbt = new NBTTagCompound();

        NBTTagCompound inputNBT = new NBTTagCompound();
        input.writeToNBT(inputNBT);
        nbt.setTag("input", inputNBT);

        nbt.setBoolean("matchMeta", matchMeta);
        nbt.setBoolean("matchNBT", matchNBT);

        int outCount = 0;
        for (Map.Entry<ItemStack, Float> output : outputs.entrySet()) {
            NBTTagCompound outputNBT = new NBTTagCompound();
            output.getKey().writeToNBT(outputNBT);
            outputNBT.setFloat("chance", output.getValue());
            nbt.setTag("output" + outCount++, outputNBT);
        }

        FMLInterModComms.sendMessage("Railcraft", "rock-crusher", nbt);
    }

    public static ItemStack getCraftingResult(ItemStack... stacks) {
        IRecipe recipe = getCraftingRecipe(stacks);

        return recipe != null ? recipe.getRecipeOutput() : ItemStack.EMPTY;
    }

    public static ItemStack removeCraftingRecipe(ItemStack... stacks) {
        IRecipe recipe = getCraftingRecipe(stacks);
        if (recipe != null) {
            ForgeRegistries.RECIPES.getValuesCollection().remove(recipe);
            return recipe.getRecipeOutput();
        }

        return ItemStack.EMPTY;
    }

    public static IRecipe getCraftingRecipe(ItemStack... stacks) {
        InventoryCrafting crafting = new InventoryCrafting(new DummyContainer(),  3, 3);
        for (int i = 0; i < 9 && i < stacks.length; i++) {
            crafting.setInventorySlotContents(i, stacks[i]);
        }
        for (IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection()) {
            try {
                if (recipe.matches(crafting, DummyWorld.INSTANCE)) {
                    return recipe;
                }
            } catch (Throwable ignored) {}
        }

        return null;
    }

    public static void addDustToIngotSmeltingRecipe(ItemStack input, Item output) {
        addDustToIngotSmeltingRecipe(input, new ItemStack(output));
    }

    public static void addDustToIngotSmeltingRecipe(ItemStack input, ItemStack output) {
        ItemStack dust = StackUtil.copyWithSize(input, 2);
        ItemStack ingots = StackUtil.copyWithSize(output, 2);
        SmelterManager.addRecipe(800, dust, new ItemStack(Blocks.SAND), ingots, SLAG, 25);
    }

    public static void addAEGrinderRecipe(ItemStack input, ItemStack output, int turns) {
        if (Loader.isModLoaded("appliedenergistics2")) registerAEGrinderRecipe(input, output, turns);
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void registerAEGrinderRecipe(ItemStack input, ItemStack output, int turns) {
        IGrinderRegistry registry = AEApi.instance().registries().grinder();
        IGrinderRecipe recipe = registry.builder()
                .withInput(input)
                .withOutput(output)
                .withTurns(turns)
                .build();
        registry.addRecipe(recipe);
    }

    public static void addSmeltingAndAlloySmeltingRecipe(ItemStack input, ItemStack output) {
        GameRegistry.addSmelting(input, output, 0);
        GtRecipes.alloy_smelter.addRecipe(RecipeDualInput.create(Collections.singletonList(RecipeIngredientItemStack.create(input)), output, 130, 3));
        addInductionSmelterRecipe(input, new ItemStack(Blocks.SAND), output, ItemStack.EMPTY, output.getCount() * 1000, 0);
    }

    public static void addShapelessRecipe(String ore, ResourceLocation group, ItemStack... inputs) {
        ItemStack output = OreDictUnificator.get(ore);
        if (!output.isEmpty()) {
            Ingredient[] ingredients = Arrays.stream(inputs)
                    .map(Ingredient::fromStacks)
                    .toArray(Ingredient[]::new);
            addShapelessRecipe(ore, group, output, ingredients);
        }
    }

    public static void addShapelessRecipe(String name, ResourceLocation group, ItemStack output, Ingredient... inputs) {
        GameRegistry.addShapelessRecipe(new ResourceLocation(Reference.MODID, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)),
                group,
                output,
                inputs);
    }
}