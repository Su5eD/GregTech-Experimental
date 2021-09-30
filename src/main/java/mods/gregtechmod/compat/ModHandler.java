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
import ic2.api.item.IItemAPI;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import ic2.core.recipe.BasicMachineRecipeManager;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.GregTechTEBlock;
import mods.gregtechmod.recipe.RecipePulverizer;
import mods.gregtechmod.recipe.crafting.AdvancementRecipeFixer;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.util.*;
import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.api.crafting.IOutputEntry;
import mods.railcraft.api.crafting.IRockCrusherCrafter;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import thaumcraft.api.aura.AuraHelper;

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
    public static boolean buildcraftLib;
    public static boolean buildcraftSilicon;
    public static boolean twilightForest;
    public static boolean enderStorage;
    public static boolean agricraft;
    
    public static IItemAPI ic2ItemApi;

    public static ItemStack emptyCell = ItemStack.EMPTY;
    public static ItemStack emptyFuelCan = ItemStack.EMPTY;
    public static ItemStack slag = ItemStack.EMPTY;
    public static ItemStack slagRich = ItemStack.EMPTY;
    public static ItemStack woodenTie = ItemStack.EMPTY;
    public static ItemStack waxCapsule = ItemStack.EMPTY;
    public static ItemStack refractoryCapsule = ItemStack.EMPTY;
    public static ItemStack can = ItemStack.EMPTY;
    public static ItemStack scrap = ItemStack.EMPTY;
    public static ItemStack itnt = ItemStack.EMPTY;
    public static ItemStack filledFuelCan = ItemStack.EMPTY;
    public static ItemStack rcTurbineRotor = ItemStack.EMPTY;
    public static Item depletedIsotopeFuelRod = null;
    public static Item heatpack = null;
    public static Item lithiumFuelRod = null;
    public static Item cropSeedBag = null;

    public static void gatherLoadedMods() {
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
        buildcraftLib = Loader.isModLoaded("buildcraftlib");
        buildcraftSilicon = Loader.isModLoaded("buildcraftsilicon");
        twilightForest = Loader.isModLoaded("twilightforest");
        enderStorage = Loader.isModLoaded("enderstorage");
        agricraft = Loader.isModLoaded("agricraft");
    }

    public static void gatherModItems() {
        ic2ItemApi = IC2Items.getItemAPI();
        
        emptyCell = ProfileDelegate.getEmptyCell();
        emptyFuelCan = IC2Items.getItem("crafting", "empty_fuel_can");
        scrap = IC2Items.getItem("crafting", "scrap");
        itnt = IC2Items.getItem("te", "itnt");
        filledFuelCan = IC2Items.getItem("filled_fuel_can");
        depletedIsotopeFuelRod = ic2ItemApi.getItem("depleted_isotope_fuel_rod");
        heatpack = ic2ItemApi.getItem("heatpack");
        lithiumFuelRod = ic2ItemApi.getItem("lithium_fuel_rod");
        cropSeedBag = ic2ItemApi.getItem("crop_seed_bag");

        Item material = getItem("thermalfoundation", "material");
        if (material != null) {
            slag = new ItemStack(material, 1, 864);
            slagRich = new ItemStack(material, 1, 865);
        }

        Item tie = getItem("railcraft", "tie");
        if (tie != null) woodenTie = new ItemStack(tie);

        waxCapsule = getModItem("forestry", "capsule");
        refractoryCapsule = getModItem("forestry", "refractory");
        can = getModItem("forestry", "can");
        rcTurbineRotor = getRCItem("turbine_rotor");
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

    public static List<IRecipePulverizer> getTEPulverizerRecipes() {
        return thermalExpansion ? _getTEPulverizerRecipes() : Collections.emptyList();
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

    @Optional.Method(modid = "thermalexpansion")
    private static List<IRecipePulverizer> _getTEPulverizerRecipes() {
        PulverizerManager.PulverizerRecipe[] recipes = PulverizerManager.getRecipeList();
        return Arrays.stream(recipes)
                .map(recipe -> {
                    IRecipeIngredient input = RecipeIngredientItemStack.create(recipe.getInput());
                    if (!input.isEmpty()) return RecipePulverizer.create(input, GtUtil.nonEmptyList(recipe.getPrimaryOutput(), recipe.getSecondaryOutput()), 3, recipe.getSecondaryOutputChance(), false, false);
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    public static List<IRecipePulverizer> getRockCrusherRecipes() {
        return railcraft ? _getRockCrusherRecipes() : Collections.emptyList();
    }

    @Optional.Method(modid = "railcraft")
    private static List<IRecipePulverizer> _getRockCrusherRecipes() {
        return Crafters.rockCrusher()
                .getRecipes().stream()
                .map(recipe -> {
                    List<IOutputEntry> outputs = recipe.getOutputs();
                    IOutputEntry primaryOutput = outputs.get(0);

                    List<ItemStack> output = new ArrayList<>();
                    if (primaryOutput.getGenRule().test(GtUtil.RANDOM)) output.add(primaryOutput.getOutput());

                    IOutputEntry secondaryOutput = outputs.size() > 1 ? outputs.get(1) : null;
                    int secondaryChance;

                    if (secondaryOutput != null) {
                        secondaryChance = (int) RailcraftHelper.getRandomChance(secondaryOutput.getGenRule());
                        output.add(secondaryOutput.getOutput());
                    } else secondaryChance = 0;

                    return !output.isEmpty() ? RecipePulverizer.create(RecipeIngredientItemStack.create(Arrays.asList(recipe.getInput().getMatchingStacks()), 1), output, 4, secondaryChance, false, false) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static void addRollingMachineRecipe(String name, ItemStack output, Object... pattern) {
        if (railcraft) registerRollingMachineRecipe(name, output, pattern);
    }

    @Optional.Method(modid = "railcraft")
    private static void registerRollingMachineRecipe(String name, ItemStack output, Object... pattern) {
        Crafters.rollingMachine()
                .newRecipe(output)
                .name(Reference.MODID, name)
                .shaped(pattern);
    }

    public static void polluteAura(World world, BlockPos pos, int amount, boolean showEffect) {
        if (thaumcraft) reallyPolluteAura(world, pos, amount, showEffect);
    }

    @Optional.Method(modid = "thaumcraft")
    private static void reallyPolluteAura(World world, BlockPos pos, int amount, boolean showEffect) {
        AuraHelper.polluteAura(world, pos, amount, showEffect);
    }
    
    public static void addShapedRecipe(String name, ItemStack output, Object... params) {
        addShapedRecipe(name, null, output, params);
    }

    public static void addShapedRecipe(String name, ResourceLocation group, ItemStack output, Object... params) {
        GameRegistry.addShapedRecipe(
                new ResourceLocation(Reference.MODID, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)),
                group,
                output,
                params
        );
    }

    public static void addShapelessRecipe(String name, ItemStack output, Ingredient... inputs) {
        addShapelessRecipe(name, null, output, inputs);
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
        return recipes.stream()
                .filter(recipe -> recipe.matches(crafting, DummyWorld.INSTANCE))
                .findFirst()
                .orElse(null);
    }

    public static OptionalItemStack getCraftingResult(ItemStack... stacks) {
        IRecipe recipe = getCraftingRecipe(stacks);

        return recipe != null ? OptionalItemStack.of(recipe.getRecipeOutput()) : OptionalItemStack.EMPTY;
    }

    public static OptionalItemStack removeCraftingRecipeFromInputs(ItemStack... stacks) {
        IRecipe recipe = getCraftingRecipe(stacks);
        if (recipe != null) {
            ResourceLocation name = recipe.getRegistryName();
            AdvancementRecipeFixer.DUMMY_RECIPES.put(name.getPath(), recipe);
            ((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(name);
            return OptionalItemStack.of(recipe.getRecipeOutput());
        }

        return OptionalItemStack.EMPTY;
    }

    public static void removeCraftingRecipe(IRecipe recipe) {
        ((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(recipe.getRegistryName());
    }

    public static void removeCraftingRecipe(ItemStack output) {
        new ArrayList<>(ForgeRegistries.RECIPES.getValuesCollection()).stream()
                .filter(recipe -> GtUtil.stackEquals(recipe.getRecipeOutput(), output))
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
    
    public static boolean addIC2Recipe(BasicMachineRecipeManager manager, IRecipeInput input, NBTTagCompound metadata, boolean overwrite, ItemStack... outputs) {
        if (overwrite) input.getInputs().forEach(stack -> removeIC2Recipe(stack, manager));
        return manager.addRecipe(input, metadata, false, outputs);
    }

    public static void removeSmeltingRecipe(ItemStack input) {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
        recipes.keySet().stream()
                .filter(stack -> GtUtil.stackEquals(stack, input, false))
                .collect(Collectors.toList())
                .forEach(recipes::remove);
    }

    public static ItemStack getIC2ItemSafely(String name, String variant) {
        try {
            ItemStack stack = IC2Items.getItem(name, variant);
            if (stack != null) return stack;
        } catch (Throwable ignored) {}

        return ItemStack.EMPTY;
    }

    public static ItemStack getTEBlockSafely(String variant) {
        try {
            BlockTileEntity blockTE = TeBlockRegistry.get(GregTechTEBlock.LOCATION);
            ItemStack stack = blockTE.getItemStack(variant);
            if (stack != null) return stack;
        } catch (Throwable ignored) {}

        return ItemStack.EMPTY;
    }

    public static String getVariantSafely(ItemName item, ItemStack stack) {
        try {
            return item.getVariant(stack);
        } catch (Throwable t) {
            return "";
        }
    }
    
    public static int getFuelCanValue(ItemStack fuelCan) { 
        if (!fuelCan.isEmpty() && fuelCan.isItemEqual(ModHandler.filledFuelCan)) {
            NBTTagCompound nbt = fuelCan.getTagCompound();
            if (nbt != null) return nbt.getInteger("value") * 5;
        }
        return 0;
    }
    
    public static ItemStack adjustWoodSize(ItemStack stack) {
        return StackUtil.copyWithSize(stack, GregTechConfig.GENERAL.woodNeedsSawForCrafting ? stack.getCount() : stack.getCount() * 5 / 4);
    }
}
