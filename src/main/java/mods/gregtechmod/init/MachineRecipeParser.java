package mods.gregtechmod.init;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.BasicMachineRecipeManager;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.compat.ModCompat;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityPrinter;
import mods.gregtechmod.objects.blocks.teblocks.generator.TileEntityDieselGenerator;
import mods.gregtechmod.recipe.*;
import mods.gregtechmod.recipe.compat.BasicMachineRecipe;
import mods.gregtechmod.recipe.deserializer.*;
import mods.gregtechmod.recipe.fuel.FuelManagerFluid;
import mods.gregtechmod.recipe.fuel.FuelMulti;
import mods.gregtechmod.recipe.fuel.FuelSimple;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.*;
import mods.gregtechmod.recipe.serializer.*;
import mods.gregtechmod.recipe.util.IBasicMachineRecipe;
import mods.gregtechmod.recipe.util.RecipeFilter;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public final class MachineRecipeParser {
    private static final ObjectMapper RECIPE_MAPPER;
    private static final ObjectMapper FUEL_MAPPER;

    private static Path recipesPath;
    private static Path classicRecipesPath;
    private static Path experimentalRecipesPath;
    private static Path fuelsPath;
    private static Path classicFuelsPath;
    private static Path dynamicRecipesDir;
    private static ProgressBar progressBar;

    static {
        ObjectMapper mapperBase = new ObjectMapper(
            new YAMLFactory()
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES))
            .registerModule(new SimpleModule()
                .addDeserializer(ItemStack.class, ItemStackDeserializer.INSTANCE)
                .addDeserializer(FluidStack.class, FluidStackDeserializer.INSTANCE)
                .addDeserializer(IRecipeIngredient.class, RecipeIngredientDeserializer.INSTANCE)
                .addDeserializer(IRecipeInput.class, RecipeInputDeserializer.INSTANCE)
                .addSerializer(ItemStack.class, ItemStackSerializer.INSTANCE)
                .addSerializer(IRecipeIngredient.class, new RecipeIngredientSerializer())
                .addSerializer(RecipeDualInput.class, new RecipeSerializerSingleOutput<>())
                .addSerializer(RecipeSimple.class, new RecipeSerializerSingleOutput<>())
                .addSerializer(RecipeCanner.class, new RecipeSerializer<>())
                .addSerializer(RecipeCentrifuge.class, new RecipeSerializerCentrifuge())
                .addSerializer(RecipeLathe.class, new RecipeSerializer<>())
                .addSerializer(RecipePulverizer.class, new RecipeSerializerPulverizer())
                .addSerializer(RecipeSawmill.class, new RecipeSerializerSawmill())
                .addSerializer(IRecipeInput.class, new RecipeInputSerializer())
                .addSerializer(MachineRecipe.class, new MachineRecipeSerializer()));

        RECIPE_MAPPER = mapperBase.copy()
            .registerModule(new SimpleModule()
                .addDeserializer(IRecipeIngredientFluid.class, RecipeIngredientFluidDeserializer.INSTANCE)
            );

        FUEL_MAPPER = mapperBase.copy()
            .registerModule(new SimpleModule()
                .addDeserializer(IRecipeIngredientFluid.class, FuelIngredientFluidDeserializer.INSTANCE)
            );
    }

    private static void setupRecipes() {
        GregTechMod.LOGGER.info("Setting up machine recipe parser");

        JavaUtil.setStaticValue(GregTechAPI.class, "recipeFactory", new RecipeFactory());
        JavaUtil.setStaticValue(GregTechAPI.class, "ingredientFactory", new RecipeIngredientFactory());

        Path recipesPath = GtUtil.getAssetPath("machine_recipes");
        Path gtConfig = relocateConfig(recipesPath, "machine_recipes");
        if (gtConfig != null) MachineRecipeParser.recipesPath = gtConfig;
        else {
            GregTechMod.LOGGER.error("Couldn't find the recipes config directory. Loading default recipes...");
            MachineRecipeParser.recipesPath = recipesPath;
        }
        classicRecipesPath = MachineRecipeParser.recipesPath.resolve("classic");
        experimentalRecipesPath = MachineRecipeParser.recipesPath.resolve("experimental");

        GregTechAPI.instance().registerCondition("mod_loaded", node -> Loader.isModLoaded(node.get("modid").asText()));
        GregTechAPI.instance().registerCondition("ore_exists", node -> OreDictUnificator.oreExists(node.get("ore").asText()));
    }

    private static void setupFuels() {
        Path recipesPath = GtUtil.getAssetPath("fuels");
        Path gtConfig = relocateConfig(recipesPath, "fuels");
        if (gtConfig == null) {
            GregTechMod.LOGGER.error("Couldn't find the fuels config directory. Loading default fuels...");
            MachineRecipeParser.fuelsPath = recipesPath;
        }
        else MachineRecipeParser.fuelsPath = gtConfig;
        classicFuelsPath = fuelsPath.resolve("classic");
    }

    public static void loadRecipes() {
        setupRecipes();
        progressBar = ProgressManager.push("Parsing Recipes", 21);

        GregTechMod.LOGGER.info("Parsing Machine Recipes");

        ItemStack gravel = new ItemStack(Blocks.GRAVEL);
        parseAndRegisterRecipes("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class, GtRecipes.industrialCentrifuge = new RecipeManagerCellular());
        parseAndRegisterRecipes("assembler", RecipeDualInput.class, GtRecipes.assembler = new RecipeManagerMultiInput<>());
        parseAndRegisterRecipes("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class, recipe -> !recipe.getInput().apply(gravel), GtRecipes.pulverizer = new RecipeManagerPulverizer());
        parseAndRegisterRecipes("industrial_grinder", RecipeGrinder.class, RecipeFilter.Default.class, GtRecipes.industrialGrinder = new RecipeManagerSecondaryFluid<>());
        parseAndRegisterRecipes("industrial_blast_furnace", RecipeBlastFurnace.class, RecipeFilter.Energy.class, GtRecipes.industrialBlastFurnace = new RecipeManagerBlastFurnace());
        parseAndRegisterRecipes("industrial_electrolyzer", RecipeElectrolyzer.class, GtRecipes.industrialElectrolyzer = new RecipeManagerCellular());
        parseAndRegisterRecipes("canner", RecipeCanner.class, GtRecipes.canner = new RecipeManagerMultiInput<>());
        parseAndRegisterRecipes("alloy_smelter", RecipeAlloySmelter.class, GtRecipes.alloySmelter = new RecipeManagerAlloySmelter());
        parseAndRegisterRecipes("implosion", RecipeImplosion.class, RecipeFilter.Default.class, GtRecipes.implosion = new RecipeManagerMultiInput<>());
        parseAndRegisterRecipes("wiremill", RecipeSimple.class, GtRecipes.wiremill = new RecipeManagerBasic<>());
        parseAndRegisterRecipes("bender", RecipeSimple.class, GtRecipes.bender = new RecipeManagerBasic<>());
        parseAndRegisterRecipes("lathe", RecipeLathe.class, GtRecipes.lathe = new RecipeManagerBasic<>());
        parseAndRegisterRecipes("vacuum_freezer", RecipeVacuumFreezer.class, RecipeFilter.Energy.class, GtRecipes.vacuumFreezer = new RecipeManagerBasic<>());
        parseAndRegisterRecipes("chemical", RecipeChemical.class, RecipeFilter.Energy.class, GtRecipes.chemical = new RecipeManagerMultiInput<>());
        parseAndRegisterRecipes("fusion_fluid", RecipeFusionFluid.class, GtRecipes.fusionFluid = new RecipeManagerFusionFluid());
        parseAndRegisterRecipes("fusion_solid", RecipeFusionSolid.class, GtRecipes.fusionSolid = new RecipeManagerMultiInput<>());
        parseAndRegisterRecipes("sawmill", RecipeSawmill.class, RecipeFilter.Default.class, GtRecipes.industrialSawmill = new RecipeManagerSawmill());
        parseAndRegisterRecipes("distillation", RecipeDistillation.class, RecipeFilter.Energy.class, GtRecipes.distillation = new RecipeManagerCellular());
        parseAndRegisterRecipes("printer", RecipePrinter.class, GtRecipes.printer = new RecipeManagerPrinter());

        // IC2 Recipes
        parseAndRegisterRecipes("compressor", (BasicMachineRecipeManager) Recipes.compressor);
        parseAndRegisterRecipes("extractor", (BasicMachineRecipeManager) Recipes.extractor);

        ProgressManager.pop(progressBar);
    }

    public static void loadFuels() {
        setupFuels();
        progressBar = ProgressManager.push("Parsing Fuels", 7);

        GregTechMod.LOGGER.info("Parsing fuels");

        parseAndRegisterFuels("plasma", FuelSimple.class, GtFuels.plasma = new FuelManagerFluid<>());
        parseAndRegisterFuels("magic", FuelSimple.class, GtFuels.magic = new FuelManagerFluid<>());
        parseAndRegisterFuels("diesel", FuelSimple.class, GtFuels.diesel = new FuelManagerFluid<>());
        parseAndRegisterFuels("gas", FuelSimple.class, GtFuels.gas = new FuelManagerFluid<>());
        parseAndRegisterFuels("hot", FuelMulti.class, GtFuels.hot = new FuelManagerFluid<>());
        parseAndRegisterFuels("dense_liquid", FuelSimple.class, GtFuels.denseLiquid = new FuelManagerFluid<>());
        parseAndRegisterFuels("steam", FuelSimple.class, GtFuels.steam = new FuelManagerFluid<>());

        ProgressManager.pop(progressBar);
        ModCompat.registerBoilerFuels();
    }

    public static void loadDynamicRecipes() {
        progressBar = ProgressManager.push("Loading Dynamic Recipes", 13);
        GregTechMod.LOGGER.info("Loading Dynamic Recipes");
        dynamicRecipesDir = GregTechMod.configDir.resolve("GregTech/machine_recipes/dynamic");
        dynamicRecipesDir.toFile().mkdirs();

        DynamicRecipes.addPulverizerRecipes = parseDynamicRecipes("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class, DynamicRecipes.PULVERIZER);
        DynamicRecipes.addAlloySmelterRecipes = parseDynamicRecipes("alloy_smelter", RecipeAlloySmelter.class, null, DynamicRecipes.ALLOY_SMELTER);
        DynamicRecipes.addCannerRecipes = parseDynamicRecipes("canner", RecipeCanner.class, null, DynamicRecipes.CANNER);
        DynamicRecipes.addLatheRecipes = parseDynamicRecipes("lathe", RecipeLathe.class, null, DynamicRecipes.LATHE);
        DynamicRecipes.addAssemblerRecipes = parseDynamicRecipes("assembler", RecipeDualInput.class, null, DynamicRecipes.ASSEMBLER);
        DynamicRecipes.addBenderRecipes = parseDynamicRecipes("bender", RecipeSimple.class, null, DynamicRecipes.BENDER);
        DynamicRecipes.addSawmillRecipes = parseDynamicRecipes("sawmill", RecipeSawmill.class, RecipeFilter.Default.class, DynamicRecipes.SAWMILL);
        DynamicRecipes.addCentrifugeRecipes = parseDynamicRecipes("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class, DynamicRecipes.INDUSTRIAL_CENTRIFUGE);
        DynamicRecipes.addCompressorRecipes = parseIC2DynamicRecipes("compressor", DynamicRecipes.COMPRESSOR);
        DynamicRecipes.addExtractorRecipes = parseIC2DynamicRecipes("extractor", DynamicRecipes.EXTRACTOR);

        progressBar.step("Dynamic Crafting Recipes");
        DynamicRecipes.processCraftingRecipes();
        progressBar.step("Applying Material Usages");
        DynamicRecipes.applyMaterialUsages();
        progressBar.step("");
        ModCompat.addRollingMachineRecipes();
        ModCompat.registerTools();

        ItemStack ingotCopper = IC2Items.getItem("ingot", "copper");
        ModHandler.getRecipeOutput(ingotCopper, ingotCopper, ItemStack.EMPTY, ingotCopper, IC2Items.getItem("ingot", "tin"))
            .ifPresent(bronze -> {
                int count = bronze.getCount();
                GtRecipes.industrialCentrifuge.addRecipe(
                    RecipeCentrifuge.create(RecipeIngredientOre.create("dustBronze", count < 3 ? 1 : count / 2),
                        Arrays.asList(BlockItems.Smalldust.COPPER.getItemStack(6), BlockItems.Smalldust.TIN.getItemStack(2)),
                        0,
                        1500,
                        CellType.CELL
                    )
                );
            });

        ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
        ItemStack stick = new ItemStack(Items.STICK);
        ModHandler.getRecipeOutput(ingotIron, ItemStack.EMPTY, ingotIron, ingotIron, stick, ingotIron, ingotIron, ItemStack.EMPTY, ingotIron)
            .ifPresent(rail -> DynamicRecipes.addPulverizerRecipe(rail, StackUtil.setSize(IC2Items.getItem("dust", "iron"), 6), BlockItems.Smalldust.WOOD.getItemStack(2), 95));
        ItemStack ingotGold = new ItemStack(Items.GOLD_INGOT);
        ItemStack redstone = new ItemStack(Items.REDSTONE);
        ModHandler.getRecipeOutput(ingotGold, ItemStack.EMPTY, ingotGold, ingotGold, stick, ingotGold, ingotGold, redstone, ingotGold)
            .ifPresent(poweredRail -> DynamicRecipes.addPulverizerRecipe(poweredRail, StackUtil.setSize(IC2Items.getItem("dust", "gold"), 6), redstone, 95));

        ItemStack ingotTin = IC2Items.getItem("ingot", "tin");
        ModHandler.getRecipeOutput(ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)
            .ifPresent(tinCan -> {
                int tinNuggetCount = 27 / tinCan.getCount();
                if (tinNuggetCount > 0) {
                    tinCan.setCount(1);
                    if (tinNuggetCount % 9 == 0) {
                        DynamicRecipes.addSmeltingAndAlloySmeltingRecipe(tinCan, ItemHandlerHelper.copyStackWithSize(ingotTin, tinNuggetCount / 9));
                    }
                    else DynamicRecipes.addSmeltingAndAlloySmeltingRecipe(tinCan, new ItemStack(BlockItems.Nugget.TIN.getInstance(), tinNuggetCount));
                }
            });

        DynamicRecipes.addPulverizerRecipe(ProfileDelegate.getEmptyCell(), BlockItems.Smalldust.TIN.getItemStack(9), true);
        ModHandler.addLiquidTransposerEmptyRecipe(IC2Items.getItem("dust", "coal_fuel"), new FluidStack(FluidRegistry.WATER, 100), IC2Items.getItem("dust", "coal"), 1250);
        if (GregTechMod.classic) {
            DynamicRecipes.addSmeltingRecipe("machineCasing", IC2Items.getItem("resource", "machine"), StackUtil.setSize(IC2Items.getItem("ingot", "refined_iron"), 8));
        }
        else {
            DynamicRecipes.addSmeltingRecipe("machineCasing", IC2Items.getItem("resource", "machine"), new ItemStack(Items.IRON_INGOT, 8));
        }
        DynamicRecipes.addSmeltingRecipe("resin", new ItemStack(Items.SLIME_BALL), IC2Items.getItem("misc_resource", "resin"));

        ProgressManager.pop(progressBar);
    }

    public static void registerDynamicRecipes() {
        DynamicRecipes.processMaterialUsages();

        registerDynamicRecipes("Pulverizer", DynamicRecipes.PULVERIZER.getRecipes(), GtRecipes.pulverizer, DynamicRecipes.addPulverizerRecipes);
        registerDynamicRecipes("Alloy Smelter", DynamicRecipes.ALLOY_SMELTER.getRecipes(), GtRecipes.alloySmelter, DynamicRecipes.addAlloySmelterRecipes);
        registerDynamicRecipes("Canner", DynamicRecipes.CANNER.getRecipes(), GtRecipes.canner, DynamicRecipes.addCannerRecipes);
        registerDynamicRecipes("Lathe", DynamicRecipes.LATHE.getRecipes(), GtRecipes.lathe, DynamicRecipes.addLatheRecipes);
        registerDynamicRecipes("Assembler", DynamicRecipes.ASSEMBLER.getRecipes(), GtRecipes.assembler, DynamicRecipes.addAssemblerRecipes);
        registerDynamicRecipes("Bender", DynamicRecipes.BENDER.getRecipes(), GtRecipes.bender, DynamicRecipes.addBenderRecipes);
        registerDynamicRecipes("Sawmill", DynamicRecipes.SAWMILL.getRecipes(), GtRecipes.industrialSawmill, DynamicRecipes.addSawmillRecipes);
        registerDynamicRecipes("Industrial Centrifuge", DynamicRecipes.INDUSTRIAL_CENTRIFUGE.getRecipes(), GtRecipes.industrialCentrifuge, DynamicRecipes.addCentrifugeRecipes);

        registerDynamicRecipes("Compressor", JavaUtil.toList(DynamicRecipes.COMPRESSOR.getRecipes()), (BasicMachineRecipeManager) Recipes.compressor, DynamicRecipes.addCompressorRecipes);
        registerDynamicRecipes("Extractor", JavaUtil.toList(DynamicRecipes.EXTRACTOR.getRecipes()), (BasicMachineRecipeManager) Recipes.extractor, DynamicRecipes.addExtractorRecipes);
    }

    public static void registerProviders() {
        GtRecipes.printer.registerProvider(new TileEntityPrinter.PrinterRecipeProvider());
        GtFuels.diesel.registerProvider(new TileEntityDieselGenerator.FuelCanRecipeProvider());
    }

    private static String formatDisplayName(String str) {
        return StreamEx.of(str.split("_"))
            .map(JavaUtil::capitalizeString)
            .joining(" ");
    }

    private static <R extends IMachineRecipe<?, ?>> void parseAndRegisterRecipes(String name, Class<? extends R> recipeClass, IGtRecipeManager<?, ?, R> manager) {
        parseAndRegisterRecipes(name, recipeClass, null, manager);
    }

    private static <R extends IMachineRecipe<?, ?>> void parseAndRegisterRecipes(String name, Class<? extends R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, IGtRecipeManager<?, ?, R> manager) {
        parseRecipes(name, recipeClass, filter)
            .ifPresent(recipes -> registerRecipes(name.replace("_", " "), recipes, manager));
    }

    @SuppressWarnings("SameParameterValue")
    private static <R extends IMachineRecipe<?, ?>> void parseAndRegisterRecipes(String name, Class<? extends R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Predicate<R> recipeFilter, IGtRecipeManager<?, ?, R> manager) {
        parseRecipes(name, recipeClass, filter)
            .ifPresent(recipes -> {
                Collection<? extends R> filtered = StreamEx.of(recipes)
                    .filter(recipeFilter)
                    .toList();
                registerRecipes(name.replace("_", " "), filtered, manager);
            });
    }

    private static void parseAndRegisterRecipes(String name, BasicMachineRecipeManager manager) {
        parseRecipes(name, BasicMachineRecipe.class, null)
            .ifPresent(recipes -> registerRecipes(name.replace("_", " "), recipes, manager));
    }

    public static <T extends IFuel<?>> void parseAndRegisterFuels(String name, Class<? extends T> fuelClass, IFuelManager<T, ?> manager) {
        parseFuels(name, fuelClass)
            .ifPresent(fuels -> registerFuels(name.replace("_", ""), fuels, manager));
    }

    private static <R> Optional<Collection<R>> parseRecipes(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter) {
        progressBar.step(formatDisplayName(name));

        Optional<Collection<R>> normalRecipes = parseRecipes(name, recipeClass, filter, recipesPath);
        Optional<Collection<R>> profileRecipes = GregTechMod.classic ? parseConfig(RECIPE_MAPPER, name, recipeClass, filter, classicRecipesPath, true) : parseConfig(RECIPE_MAPPER, name, recipeClass, filter, experimentalRecipesPath, true);
        return normalRecipes.flatMap(recipes -> Optional.of(JavaUtil.mergeCollection(recipes, profileRecipes.orElseGet(Collections::emptyList))));
    }

    private static <R> Optional<Collection<R>> parseRecipes(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path) {
        return parseConfig(MachineRecipeParser.RECIPE_MAPPER, name, recipeClass, filter, path, false);
    }

    private static <R extends IFuel<?>> Optional<Collection<R>> parseFuels(String name, Class<R> fuelClass) {
        progressBar.step(formatDisplayName(name));

        Optional<Collection<R>> normalFuels = parseConfig(FUEL_MAPPER, name, fuelClass, null, fuelsPath, false);
        Optional<Collection<R>> classicFuels = GregTechMod.classic ? parseConfig(FUEL_MAPPER, name, fuelClass, null, classicFuelsPath, true) : Optional.empty();
        return normalFuels.flatMap(recipes -> Optional.of(JavaUtil.mergeCollection(recipes, classicFuels.orElseGet(Collections::emptyList))));
    }

    private static <R> Optional<Collection<R>> parseConfig(ObjectMapper mapper, String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path, boolean silent) {
        try {
            return parseConfig(mapper, recipeClass, filter, Files.newBufferedReader(path.resolve(name + ".yml")));
        } catch (IOException e) {
            if (!silent || !(e instanceof NoSuchFileException)) {
                GregTechMod.LOGGER.error("Failed to parse " + name + " recipes", e);
            }
            return Optional.empty();
        }
    }

    private static <R> Optional<Collection<R>> parseConfig(ObjectMapper mapper, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Reader reader) throws IOException {
        ObjectMapper objectMapper = mapper.copy();
        if (filter != null) objectMapper.addMixIn(IMachineRecipe.class, filter);

        JsonNode node = objectMapper.readTree(reader);
        Iterator<JsonNode> recipeIterator = node.elements();
        while (recipeIterator.hasNext()) {
            JsonNode recipe = recipeIterator.next();
            if (recipe.has("conditions")) {
                JsonNode conditions = recipe.get("conditions");
                Iterator<JsonNode> conditionIterator = conditions.elements();
                while (conditionIterator.hasNext()) {
                    JsonNode condition = conditionIterator.next();
                    String type = condition.get("type").asText();
                    if (!GregTechAPI.instance().testCondition(type, condition)) recipeIterator.remove();
                }
            }
            ((ObjectNode) recipe).remove("conditions");
        }
        return Optional.ofNullable(objectMapper.convertValue(node, objectMapper.getTypeFactory().constructCollectionType(List.class, recipeClass)));
    }

    private static <R extends IMachineRecipe<?, ?>> boolean parseDynamicRecipes(String name, Class<? extends R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, IGtRecipeManager<?, ?, R> manager) {
        progressBar.step(formatDisplayName(name));

        if (shouldParseDynamicRecipes(name)) {
            parseRecipes(name, recipeClass, filter, dynamicRecipesDir)
                .ifPresent(recipes -> registerRecipes("dynamic " + name.replace('_', ' '), recipes, manager));
            return false;
        }
        return true;
    }

    private static boolean parseIC2DynamicRecipes(String name, IBasicMachineRecipeManager manager) {
        progressBar.step(formatDisplayName(name));

        if (shouldParseDynamicRecipes(name)) {
            parseRecipes(name, BasicMachineRecipe.class, null, dynamicRecipesDir)
                .ifPresent(recipes -> registerRecipes("dynamic " + name.replace('_', ' '), recipes, (BasicMachineRecipeManager) manager));
            return false;
        }
        return true;
    }

    private static <R extends IMachineRecipe<?, ?>> void registerRecipes(String name, Collection<? extends R> recipes, IGtRecipeManager<?, ?, R> manager) {
        long successful = recipes.stream()
            .map(manager::addRecipe)
            .filter(Boolean::booleanValue)
            .count();
        GregTechMod.LOGGER.info("Loaded " + successful + " out of " + recipes.size() + " " + name + " recipes");
    }

    private static <R extends IBasicMachineRecipe> void registerRecipes(String name, Collection<? extends R> recipes, BasicMachineRecipeManager manager) {
        long successful = recipes.stream()
            .map(recipe -> ModHandler.addIC2Recipe(manager, recipe.getInput(), null, recipe.shouldOverwrite(), recipe.getOutput()))
            .filter(Boolean::booleanValue)
            .count();
        GregTechMod.LOGGER.info("Loaded " + successful + " out of " + recipes.size() + " " + name + " recipes");
    }

    private static <T extends IFuel<?>, I> void registerFuels(String name, Collection<? extends T> fuels, IFuelManager<T, I> manager) {
        long successful = fuels.stream()
            .map(manager::addFuel)
            .filter(Boolean::booleanValue)
            .count();
        GregTechMod.LOGGER.info("Loaded " + successful + " out of " + fuels.size() + " " + name + " fuels");
    }

    private static <R extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> void registerDynamicRecipes(String name, Collection<? extends R> recipes, BasicMachineRecipeManager manager, boolean serialize) {
        recipes.forEach(recipe -> ModHandler.addIC2Recipe(manager, recipe.getInput(), null, true, recipe.getOutput().toArray(new ItemStack[0])));
        if (serialize) serializeDynamicRecipes(name, recipes);
    }

    private static <R extends IMachineRecipe<?, ?>, M extends IGtRecipeManager<?, ?, R>> void registerDynamicRecipes(String name, Collection<R> recipes, M recipeManager, boolean serialize) {
        recipes.forEach(recipeManager::addRecipe);
        if (serialize) serializeDynamicRecipes(name, recipes);
    }

    private static void serializeDynamicRecipes(String name, Collection<?> recipes) {
        try {
            File file = dynamicRecipesDir.resolve(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name.replace(" ", "")) + ".yml").toFile();
            file.createNewFile();

            OutputStream output = Files.newOutputStream(file.toPath());
            output.write(("# Dynamic " + name + " recipes\n").getBytes(StandardCharsets.UTF_8));

            RECIPE_MAPPER.writeValue(output, recipes);
        } catch (IOException e) {
            GregTechMod.LOGGER.error("Failed to serialize " + name + " recipes", e);
        }
    }

    private static boolean shouldParseDynamicRecipes(String name) {
        Path dest = dynamicRecipesDir.resolve(name + ".yml");
        return dest.toFile().exists();
    }

    private static Path relocateConfig(Path recipesPath, String target) {
        try {
            Path configDir = GregTechMod.modConfigDir.resolve(target);
            Files.createDirectories(configDir);
            return JavaUtil.copyDir(recipesPath, configDir);
        } catch (IOException e) {
            GregTechMod.LOGGER.error("Couldn't create config directory", e);
            return null;
        }
    }
}
