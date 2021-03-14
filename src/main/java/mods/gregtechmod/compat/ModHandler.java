package mods.gregtechmod.compat;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import appeng.api.features.IGrinderRegistry;
import cofh.thermalexpansion.util.managers.device.FactorizerManager;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalexpansion.util.managers.machine.TransposerManager;
import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.IC2;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import ic2.core.recipe.BasicMachineRecipeManager;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechTEBlock;
import mods.gregtechmod.util.DummyContainer;
import mods.gregtechmod.util.DummyWorld;
import mods.gregtechmod.util.GtUtil;
import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.api.crafting.IRockCrusherCrafter;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class ModHandler {
    public static boolean thermalfoundation;
    public static boolean thermalExpansion;
    public static boolean appliedEnergistics;
    public static boolean forestry;
    public static boolean railcraft;
    public static boolean projectredCore;
    public static boolean projectredExploration;
    public static boolean thaumcraft;
    public static boolean quark;
    public static boolean traverse;
    public static boolean projectVibrantJourneys;
    public static boolean buildcraftCore;
    public static boolean buildcraftFactory;
    public static boolean buildcraftTransport;
    public static boolean twilightForest;
    public static boolean enderStorage;
    public static boolean agricraft;

    public static ItemStack emptyCell = ItemStack.EMPTY;
    public static ItemStack slag = ItemStack.EMPTY;
    public static ItemStack slagRich = ItemStack.EMPTY;
    public static ItemStack hardenedGlass = ItemStack.EMPTY;
    public static ItemStack bcStoneGear = ItemStack.EMPTY;
    public static ItemStack woodenTie = ItemStack.EMPTY;
    public static ItemStack essentiaPhial = ItemStack.EMPTY;

    public static void checkLoadedMods() {
        thermalfoundation = Loader.isModLoaded("thermalfoundation");
        thermalExpansion = Loader.isModLoaded("thermalexpansion");
        appliedEnergistics = Loader.isModLoaded("appliedenergistics2");
        forestry = Loader.isModLoaded("forestry");
        railcraft = Loader.isModLoaded("railcraft");
        projectredCore = Loader.isModLoaded("projectred-core");
        projectredExploration = Loader.isModLoaded("projectred-exploration");
        thaumcraft = Loader.isModLoaded("thaumcraft");
        quark = Loader.isModLoaded("quark");
        traverse = Loader.isModLoaded("traverse");
        projectVibrantJourneys = Loader.isModLoaded("pvj");
        buildcraftCore = Loader.isModLoaded("buildcraftcore");
        buildcraftFactory = Loader.isModLoaded("buildcraftfactory");
        buildcraftTransport = Loader.isModLoaded("buildcrafttransport");
        twilightForest = Loader.isModLoaded("twilightforest");
        enderStorage = Loader.isModLoaded("enderstorage");
        agricraft = Loader.isModLoaded("agricraft");
    }

    public static void gatherModItems() {
        if (IC2.version.isClassic()) emptyCell = IC2Items.getItem("cell", "empty");
        else emptyCell = IC2Items.getItem("fluid_cell");

        Item material = getItem("thermalfoundation", "material");
        if (material != null) {
            slag = new ItemStack(material, 1, 864);
            slagRich = new ItemStack(material, 1, 865);
        }

        Item glass = getItem("thermalfoundation", "glass");
        if (glass != null) hardenedGlass = new ItemStack(glass, 1, 3);

        bcStoneGear = getModItem("buildcraftcore", "gear_stone");

        Item tie = getItem("railcraft", "tie");
        if (tie != null) woodenTie = new ItemStack(tie);

        essentiaPhial = getModItem("thaumcraft", "phial");
    }

    public static Item getItem(String modid, String itemName) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, itemName));
    }

    public static ItemStack getModItem(String modid, String itemName) {
        return getModItem(modid, itemName, 0);
    }

    public static ItemStack getModItem(String modid, String itemName, int meta) {
        Item item = getItem(modid, itemName);
        if (item == null) return ItemStack.EMPTY;
        return new ItemStack(item, 1, meta);
    }

    public static ItemStack getTEItem(String baseItem, int meta) {
        return getModItem("thermalexpansion", baseItem, meta);
    }

    public static ItemStack getTFItem(String baseItem, int meta) {
        return getModItem("thermalfoundation", baseItem, meta);
    }

    public static ItemStack getRCItem(String baseItem) {
        return getRCItem(baseItem, 0);
    }

    public static ItemStack getRCItem(String baseItem, int meta) {
        return getModItem("railcraft", baseItem, meta);
    }

    public static ItemStack getPRItem(String baseItem, int meta) {
        return getModItem("projectred-core", baseItem, meta);
    }

    public static ItemStack getFRItem(String baseItem) {
        return getModItem("forestry", baseItem);
    }

    public static ItemStack getTCItem(String baseItem, int meta) {
        return getModItem("thaumcraft", baseItem, meta);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack output, boolean overwrite) {
        addTESawmillRecipe(energy, input, output, ItemStack.EMPTY, 0, overwrite);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (thermalExpansion) registerSawmillRecipe(energy, input, primaryOutput, secondaryOutput, chance, overwrite);
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
        if (thermalExpansion) registerPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, chance, overwrite);
    }

    public static void removeTEPulverizerRecipe(ItemStack input) {
        if (thermalExpansion) _removeTEPulverizerRecipe(input);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void _removeTEPulverizerRecipe(ItemStack input) {
        PulverizerManager.removeRecipe(input);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (overwrite && PulverizerManager.recipeExists(input)) PulverizerManager.removeRecipe(input);
        PulverizerManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    public static void addInductionSmelterRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        if (thermalExpansion) registerInductionSmelterRecipe(primaryInput, secondaryInput, primaryOutput, secondaryOutput, energy, chance);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerInductionSmelterRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        SmelterManager.addRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, chance);
    }

    public static void removeInductionSelterRecipe(ItemStack input) {
        if (thermalExpansion) _removeInductionSelterRecipe(input);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void _removeInductionSelterRecipe(ItemStack input) {
        Arrays.stream(SmelterManager.getRecipeList())
                .filter(recipe -> GtUtil.stackEquals(recipe.getPrimaryInput(), input) || GtUtil.stackEquals(recipe.getSecondaryInput(), input))
                .collect(Collectors.toList())
                .forEach(recipe -> SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput()));
    }

    public static void addLiquidTransposerFillRecipe(ItemStack emptyContainer, FluidStack fluid, ItemStack fullContainer, int energy) {
        if (thermalExpansion) registerLiquidTransposerFillRecipe(emptyContainer, fluid, fullContainer, energy);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerLiquidTransposerFillRecipe(ItemStack emptyContainer, FluidStack fluid, ItemStack fullContainer, int energy) {
        TransposerManager.addFillRecipe(energy, emptyContainer, fullContainer, fluid, false);
    }

    public static void addLiquidTransposerEmptyRecipe(ItemStack fullContainer, FluidStack fluid, ItemStack emptyContainer, int energy) {
        if (thermalExpansion) registerLiquidTransposerEmptyRecipe(emptyContainer, fluid, fullContainer, energy);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerLiquidTransposerEmptyRecipe(ItemStack fullContainer, FluidStack fluid, ItemStack emptyContainer, int energy) {
        TransposerManager.addExtractRecipe(energy, fullContainer, emptyContainer, fluid, 100, false);
    }

    public static void addFactorizerRecipe(ItemStack input, ItemStack output, boolean reverse) {
        if (thermalExpansion) registerFactorizerRecipe(input, output, reverse);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerFactorizerRecipe(ItemStack input, ItemStack output, boolean reverse) {
        FactorizerManager.addRecipe(input, output, reverse);
    }

    public static void removeFactorizerRecipe(ItemStack input, boolean reverse) {
        if (thermalExpansion) _removeFactorizerRecipe(input, reverse);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void _removeFactorizerRecipe(ItemStack input, boolean reverse) {
        FactorizerManager.removeRecipe(input, reverse);
    }

    public static void addAEGrinderRecipe(ItemStack input, ItemStack output, int turns) {
        if (appliedEnergistics) registerAEGrinderRecipe(input, output, turns);
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

    public static void addRockCrusherRecipe(ItemStack input, Map<ItemStack, Float> outputs) {
        if (railcraft) registerRockCrusherRecipe(input, outputs);
    }

    @Optional.Method(modid = "railcraft")
    private static void registerRockCrusherRecipe(ItemStack input, Map<ItemStack, Float> outputs) {
        IRockCrusherCrafter.IRockCrusherRecipeBuilder builder = Crafters.rockCrusher()
                .makeRecipe(input);
        for (Map.Entry<ItemStack, Float> output : outputs.entrySet()) {
            builder.addOutput(output.getKey(), output.getValue());
        }
        builder.register();
    }

    public static void addRollingMachineRecipe(String name, ItemStack output, Object... pattern) {
        if (railcraft) registerRollingMachineRecipe(name, output, pattern);
    }

    @Optional.Method(modid = "railcraft")
    private static void registerRollingMachineRecipe(String name, ItemStack output, Object... pattern) {
        Crafters.rollingMachine()
                .newRecipe(output)
                .name(name)
                .shaped(pattern);
    }

    public static void addShapedRecipe(String name, ResourceLocation group, @Nonnull ItemStack output, Object... params) {
        GameRegistry.addShapedRecipe(
                new ResourceLocation(Reference.MODID, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)),
                group,
                output,
                params
        );
    }

    public static void addShapelessRecipe(String name, ResourceLocation group, ItemStack output, Ingredient... inputs) {
        GameRegistry.addShapelessRecipe(new ResourceLocation(Reference.MODID, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)),
                group,
                output,
                inputs);
    }

    public static IRecipe getCraftingRecipe(ItemStack... stacks) {
        return getCraftingRecipe(ForgeRegistries.RECIPES.getValuesCollection(), stacks);
    }

    public static IRecipe getCraftingRecipe(Collection<IRecipe> recipes, ItemStack... stacks) {
        InventoryCrafting crafting = new InventoryCrafting(new DummyContainer(),  3, 3);
        for (int i = 0; i < 9 && i < stacks.length; i++) {
            crafting.setInventorySlotContents(i, stacks[i]);
        }
        for (IRecipe recipe : recipes) {
            try {
                if (recipe.matches(crafting, DummyWorld.INSTANCE)) {
                    return recipe;
                }
            } catch (Throwable ignored) {}
        }

        return null;
    }

    public static ItemStack getCraftingResult(ItemStack... stacks) {
        IRecipe recipe = getCraftingRecipe(stacks);

        return recipe != null ? recipe.getRecipeOutput().copy() : ItemStack.EMPTY;
    }

    public static ItemStack removeCraftingRecipeFromInputs(ItemStack... stacks) {
        IRecipe recipe = getCraftingRecipe(stacks);
        if (recipe != null) {
            ((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(recipe.getRegistryName());
            return recipe.getRecipeOutput();
        }

        return ItemStack.EMPTY;
    }

    public static void removeCraftingRecipe(IRecipe recipe) {
        ((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(recipe.getRegistryName());
    }

    public static void removeCraftingRecipe(ItemStack output) {
        new ArrayList<>(ForgeRegistries.RECIPES.getValuesCollection()).stream()
                .filter(recipe -> ItemHandlerHelper.canItemStacksStack(recipe.getRecipeOutput(), output))
                .map(IRecipe::getRegistryName)
                .filter(name -> !name.getNamespace().equals(Reference.MODID))
                .forEach(((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES)::remove);
    }

    public static void removeIC2Recipe(ItemStack input, BasicMachineRecipeManager manager) {
        Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iterator = manager.getRecipes().iterator();
        while (iterator.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = iterator.next();
            if (recipe.getInput().matches(input)) {
                iterator.remove();
                return;
            }
        }
    }

    public static void removeSmeltingRecipe(ItemStack input) {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
        recipes.keySet()
                .stream()
                .filter(input::isItemEqual)
                .collect(Collectors.toList())
                .forEach(recipes::remove);
    }

    public static ItemStack getSmeltingOutput(ItemStack input) {
        return FurnaceRecipes.instance().getSmeltingList()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().isItemEqual(input))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    public static ItemStack getIC2ItemSafely(String name, String variant) {
        ItemStack stack = ItemStack.EMPTY;
        try {
            ItemStack item = IC2Items.getItem(name, variant);
            if (item != null) stack = item;
        } catch (Throwable ignored) {}

        return stack;
    }

    public static ItemStack getTEBlockSafely(String variant) {
        try {
            BlockTileEntity blockTE = TeBlockRegistry.get(GregTechTEBlock.LOCATION);
            ItemStack stack = blockTE.getItemStack(variant);
            if (stack != null) return stack;
        } catch (Throwable ignored) {}

        return ItemStack.EMPTY;
    }
}