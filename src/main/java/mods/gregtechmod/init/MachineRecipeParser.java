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
import mods.gregtechmod.api.recipe.IRecipePulverizer;
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
import mods.gregtechmod.recipe.fuel.FuelIngredientFluidDeserializer;
import mods.gregtechmod.recipe.fuel.FuelManagerFluid;
import mods.gregtechmod.recipe.fuel.FuelMulti;
import mods.gregtechmod.recipe.fuel.FuelSimple;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.*;
import mods.gregtechmod.recipe.util.IBasicMachineRecipe;
import mods.gregtechmod.recipe.util.RecipeFilter;
import mods.gregtechmod.recipe.util.deserializer.*;
import mods.gregtechmod.recipe.util.serializer.*;
import mods.gregtechmod.util.GtUtil;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MachineRecipeParser {
    private static Path recipesPath = null;
    private static Path classicRecipesPath = null;
    private static Path experimentalRecipesPath = null;
    private static Path fuelsPath = null;
    private static Path classicFuelsPath = null;
    private static Path dynamicRecipesDir = null;
    private static ProgressBar progressBar;
    private static final ObjectMapper MAPPER_BASE = new ObjectMapper(
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
                    .addSerializer(RecipeSimple.class,  new RecipeSerializerSingleOutput<>())
                    .addSerializer(RecipeCanner.class, new RecipeSerializer<>())
                    .addSerializer(RecipeCentrifuge.class, new RecipeSerializerCentrifuge())
                    .addSerializer(RecipeLathe.class, new RecipeSerializer<>())
                    .addSerializer(RecipePulverizer.class, new RecipeSerializerPulverizer())
                    .addSerializer(RecipeSawmill.class, new RecipeSerializerSawmill())
                    .addSerializer(IRecipeInput.class, new RecipeInputSerializer())
                    .addSerializer(MachineRecipe.class, new MachineRecipeSerializer()));
    private static final ObjectMapper RECIPE_MAPPER = MAPPER_BASE.copy()
            .registerModule(new SimpleModule()
                    .addDeserializer(IRecipeIngredientFluid.class, RecipeIngredientFluidDeserializer.INSTANCE)
            );
    private static final ObjectMapper FUEL_MAPPER = MAPPER_BASE.copy()
            .registerModule(new SimpleModule()
                    .addDeserializer(IRecipeIngredientFluid.class, FuelIngredientFluidDeserializer.INSTANCE)
            );
    
    private static void setup() {
        GregTechMod.LOGGER.info("Setting up machine recipe parser");
        
        GtUtil.setPrivateStaticValue(GregTechAPI.class, "recipeFactory", new RecipeFactory());
        GtUtil.setPrivateStaticValue(GregTechAPI.class, "ingredientFactory", new RecipeIngredientFactory());
        
        Path recipesPath = GtUtil.getAssetPath("machine_recipes");
        Path gtConfig = relocateConfig(recipesPath, "machine_recipes");
        if (gtConfig != null) MachineRecipeParser.recipesPath = gtConfig;
        else  {
            GregTechMod.LOGGER.error("Couldn't find the recipes config directory. Loading default recipes...");
            MachineRecipeParser.recipesPath = recipesPath;
        }
        classicRecipesPath = MachineRecipeParser.recipesPath.resolve("classic");
        experimentalRecipesPath = MachineRecipeParser.recipesPath.resolve("experimental");
        
        GregTechAPI.instance().registerCondition("mod_loaded", node -> Loader.isModLoaded(node.get("modid").asText()));
        GregTechAPI.instance().registerCondition("ore_exists", node -> OreDictUnificator.oreExists(node.get("ore").asText()));
    }

    public static void loadRecipes() {
        setup();
        progressBar = ProgressManager.push("Parsing Recipes", 21);
        
        GregTechMod.LOGGER.info("Parsing Machine Recipes");

        GtRecipes.industrialCentrifuge = new RecipeManagerCellular();
        parseRecipes("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("industrial centrifuge", recipes, GtRecipes.industrialCentrifuge));
        GtRecipes.assembler = new RecipeManagerMultiInput<>();
        parseRecipes("assembler", RecipeDualInput.class, null)
                .ifPresent(recipes -> registerRecipes("assembler", recipes, GtRecipes.assembler));

        GtRecipes.pulverizer = new RecipeManagerPulverizer();
        ItemStack gravel = new ItemStack(Blocks.GRAVEL);
        parseRecipes("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> {
                    Collection<IRecipePulverizer> filtered = recipes.stream()
                            .filter(recipe -> !recipe.getInput().apply(gravel))
                            .collect(Collectors.toList());
                    registerRecipes("pulverizer", filtered, GtRecipes.pulverizer);
                });

        GtRecipes.industrialGrinder = new RecipeManagerSecondaryFluid<>();
        parseRecipes("industrial_grinder", RecipeGrinder.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("industrial grinder", recipes, GtRecipes.industrialGrinder));

        GtRecipes.industrialBlastFurnace = new RecipeManagerBlastFurnace();
        parseRecipes("industrial_blast_furnace", RecipeBlastFurnace.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("blast furnace", recipes, GtRecipes.industrialBlastFurnace));

        GtRecipes.industrialElectrolyzer = new RecipeManagerCellular();
        parseRecipes("industrial_electrolyzer", RecipeElectrolyzer.class, null)
                .ifPresent(recipes -> registerRecipes("industrial electrolyzer", recipes, GtRecipes.industrialElectrolyzer));

        GtRecipes.canner = new RecipeManagerMultiInput<>();
        parseRecipes("canner", RecipeCanner.class, null)
                .ifPresent(recipes -> registerRecipes("canner", recipes, GtRecipes.canner));

        GtRecipes.alloySmelter = new RecipeManagerAlloySmelter();
        parseRecipes("alloy_smelter", RecipeAlloySmelter.class, null)
                .ifPresent(recipes -> registerRecipes("alloy smelter", recipes, GtRecipes.alloySmelter));

        GtRecipes.implosion = new RecipeManagerMultiInput<>();
        parseRecipes("implosion", RecipeImplosion.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("implosion", recipes, GtRecipes.implosion));

        GtRecipes.wiremill = new RecipeManagerBasic<>();
        parseRecipes("wiremill", RecipeSimple.class, null)
                .ifPresent(recipes -> registerRecipes("wiremill", recipes, GtRecipes.wiremill));

        GtRecipes.bender = new RecipeManagerBasic<>();
        parseRecipes("bender", RecipeSimple.class, null)
                .ifPresent(recipes -> registerRecipes("bender", recipes, GtRecipes.bender));

        GtRecipes.lathe = new RecipeManagerBasic<>();
        parseRecipes("lathe", RecipeLathe.class, null)
                .ifPresent(recipes -> registerRecipes("lathe", recipes, GtRecipes.lathe));

        GtRecipes.vacuumFreezer = new RecipeManagerBasic<>();
        parseRecipes("vacuum_freezer", RecipeVacuumFreezer.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("vacuum freezer", recipes, GtRecipes.vacuumFreezer));

        GtRecipes.chemical = new RecipeManagerMultiInput<>();
        parseRecipes("chemical", RecipeChemical.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("chemical", recipes, GtRecipes.chemical));

        GtRecipes.fusionFluid = new RecipeManagerFusionFluid();
        parseRecipes("fusion_fluid", RecipeFusionFluid.class, null)
                .ifPresent(recipes -> registerRecipes("fluid fusion", recipes, GtRecipes.fusionFluid));

        GtRecipes.fusionSolid = new RecipeManagerMultiInput<>();
        parseRecipes("fusion_solid", RecipeFusionSolid.class, null)
                .ifPresent(recipes -> registerRecipes("solid fusion", recipes, GtRecipes.fusionSolid));

        GtRecipes.industrialSawmill = new RecipeManagerSawmill();
        parseRecipes("sawmill", RecipeSawmill.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("sawmill", recipes, GtRecipes.industrialSawmill));

        GtRecipes.distillation = new RecipeManagerCellular();
        parseRecipes("distillation", RecipeDistillation.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("distillation", recipes, GtRecipes.distillation));

        GtRecipes.printer = new RecipeManagerPrinter();
        parseRecipes("printer", RecipePrinter.class, null)
                .ifPresent(recipes -> registerRecipes("printer", recipes, GtRecipes.printer));

        // IC2 Recipes
        parseRecipes("compressor", BasicMachineRecipe.class, null)
                .ifPresent(recipes -> registerRecipes("compressor", recipes, (BasicMachineRecipeManager) Recipes.compressor));

        parseRecipes("extractor", BasicMachineRecipe.class, null)
                .ifPresent(recipes -> registerRecipes("extractor", recipes, (BasicMachineRecipeManager) Recipes.extractor));
        
        ProgressManager.pop(progressBar);
    }

    public static void loadFuels() {
        progressBar = ProgressManager.push("Parsing Fuels", 7);
        GregTechMod.LOGGER.info("Parsing fuels");

        Path recipesPath = GtUtil.getAssetPath("fuels");
        Path gtConfig = relocateConfig(recipesPath, "fuels");
        if (gtConfig == null) {
            GregTechMod.LOGGER.error("Couldn't find the fuels config directory. Loading default fuels...");
            MachineRecipeParser.fuelsPath = recipesPath;
        } else MachineRecipeParser.fuelsPath = gtConfig;
        classicFuelsPath = fuelsPath.resolve("classic");

        GtFuels.plasma = new FuelManagerFluid<>();
        parseFuels("plasma", FuelSimple.class)
                .ifPresent(fuels -> registerFuels("plasma", fuels, GtFuels.plasma));

        GtFuels.magic = new FuelManagerFluid<>();
        parseFuels("magic", FuelSimple.class)
                .ifPresent(fuels -> registerFuels("magic", fuels, GtFuels.magic));

        GtFuels.diesel = new FuelManagerFluid<>();
        parseFuels("diesel", FuelSimple.class)
                .ifPresent(fuels -> registerFuels("diesel", fuels, GtFuels.diesel));
        
        GtFuels.gas = new FuelManagerFluid<>();
        parseFuels("gas", FuelSimple.class)
                .ifPresent(fuels -> registerFuels("gas", fuels, GtFuels.gas));

        GtFuels.hot = new FuelManagerFluid<>();
        parseFuels("hot", FuelMulti.class)
                .ifPresent(fuels -> registerFuels("hot", fuels, GtFuels.hot));

        GtFuels.denseLiquid = new FuelManagerFluid<>();
        parseFuels("dense_liquid", FuelSimple.class)
                .ifPresent(fuels -> registerFuels("dense liquid", fuels, GtFuels.denseLiquid));
        
        GtFuels.steam = new FuelManagerFluid<>();
        parseFuels("steam", FuelSimple.class)
                .ifPresent(fuels -> registerFuels("steam", fuels, GtFuels.steam));
        
        ProgressManager.pop(progressBar);

        ModCompat.registerBoilerFuels();
    }

    public static void loadDynamicRecipes() {
        progressBar = ProgressManager.push("Loading Dynamic Recipes", 13);
        GregTechMod.LOGGER.info("Loading Dynamic Recipes");
        dynamicRecipesDir = GregTechMod.configDir.toPath().resolve("GregTech/machine_recipes/dynamic");
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
        ModHandler.getCraftingResult(ingotCopper, ingotCopper, ItemStack.EMPTY, ingotCopper, IC2Items.getItem("ingot", "tin"))
                .ifPresent(bronze -> {
                    int count = bronze.getCount();
                    GtRecipes.industrialCentrifuge.addRecipe(
                            RecipeCentrifuge.create(RecipeIngredientOre.create("dustBronze", count < 3 ? 1 : count / 2), 
                                    Arrays.asList(new ItemStack(BlockItems.Smalldust.COPPER.getInstance(), 6), new ItemStack(BlockItems.Smalldust.TIN.getInstance(), 2)), 
                                    0, 
                                    1500, 
                                    CellType.CELL
                            )
                    );
                });

        ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
        ItemStack stick = new ItemStack(Items.STICK);
        ModHandler.getCraftingResult(ingotIron, ItemStack.EMPTY, ingotIron, ingotIron, stick, ingotIron, ingotIron, ItemStack.EMPTY, ingotIron)
                .ifPresent(rail -> DynamicRecipes.addPulverizerRecipe(rail, StackUtil.setSize(IC2Items.getItem("dust", "iron"), 6), new ItemStack(BlockItems.Smalldust.WOOD.getInstance(), 2), 95));
        ItemStack ingotGold = new ItemStack(Items.GOLD_INGOT);
        ItemStack redstone = new ItemStack(Items.REDSTONE);
        ModHandler.getCraftingResult(ingotGold, ItemStack.EMPTY, ingotGold, ingotGold, stick, ingotGold, ingotGold, redstone, ingotGold)
                .ifPresent(poweredRail -> DynamicRecipes.addPulverizerRecipe(poweredRail, StackUtil.setSize(IC2Items.getItem("dust", "gold"), 6), redstone, 95));

        ItemStack ingotTin = IC2Items.getItem("ingot", "tin");
        ModHandler.getCraftingResult(ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)
                .ifPresent(tinCan -> {
                    int tinNuggetCount = 27 / tinCan.getCount();
                    if (tinNuggetCount > 0) {
                        tinCan.setCount(1);
                        if (tinNuggetCount % 9 == 0) {
                            DynamicRecipes.addSmeltingAndAlloySmeltingRecipe(tinCan, StackUtil.copyWithSize(ingotTin, tinNuggetCount / 9));
                        }
                        else DynamicRecipes.addSmeltingAndAlloySmeltingRecipe(tinCan, new ItemStack(BlockItems.Nugget.TIN.getInstance(), tinNuggetCount));
                    }
                });

        DynamicRecipes.addPulverizerRecipe(ProfileDelegate.getEmptyCell(), new ItemStack(BlockItems.Smalldust.TIN.getInstance(), 9), true);
        ModHandler.addLiquidTransposerEmptyRecipe(IC2Items.getItem("dust", "coal_fuel"), new FluidStack(FluidRegistry.WATER, 100), IC2Items.getItem("dust", "coal"), 1250);
        if (GregTechMod.classic) {
            DynamicRecipes.addSmeltingRecipe("machineCasing", IC2Items.getItem("resource", "machine"), StackUtil.setSize(IC2Items.getItem("ingot", "refined_iron"), 8));
        } else {
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

        Collection<MachineRecipe<IRecipeInput, Collection<ItemStack>>> compressorRecipes = StreamSupport.stream(DynamicRecipes.COMPRESSOR.getRecipes().spliterator(), false)
                .collect(Collectors.toList());
        registerDynamicRecipes("Compressor", compressorRecipes, (BasicMachineRecipeManager) Recipes.compressor, DynamicRecipes.addCompressorRecipes);

        Collection<MachineRecipe<IRecipeInput, Collection<ItemStack>>> extractorRecipes = StreamSupport.stream(DynamicRecipes.EXTRACTOR.getRecipes().spliterator(), false)
                .collect(Collectors.toList());
        registerDynamicRecipes("Extractor", extractorRecipes, (BasicMachineRecipeManager) Recipes.extractor, DynamicRecipes.addExtractorRecipes);
    }
    
    private static String formatDisplayName(String str) {
        return Arrays.stream(str.split("_"))
                .map(GtUtil::capitalizeString)
                .collect(Collectors.joining(" "));
    }

    public static <R> Optional<Collection<R>> parseRecipes(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter) {
        progressBar.step(formatDisplayName(name));
        
        Optional<Collection<R>> normalRecipes = parseConfig(RECIPE_MAPPER, name, recipeClass, filter, recipesPath);
        Optional<Collection<R>> profileRecipes = GregTechMod.classic ? parseConfig(RECIPE_MAPPER, name, recipeClass, filter, classicRecipesPath, true) : parseConfig(RECIPE_MAPPER, name, recipeClass, filter, experimentalRecipesPath, true);
        return normalRecipes.flatMap(recipes -> Optional.of(GtUtil.mergeCollection(recipes, profileRecipes.orElseGet(Collections::emptyList))));
    }

    public static <R> Optional<Collection<R>> parseFuels(String name, Class<R> recipeClass) {
        progressBar.step(formatDisplayName(name));
        
        Optional<Collection<R>> normalFuels = parseConfig(FUEL_MAPPER, name, recipeClass, null, fuelsPath, false);
        Optional<Collection<R>> classicFuels = GregTechMod.classic ? parseConfig(FUEL_MAPPER, name, recipeClass, null, classicFuelsPath, true) : Optional.empty();
        return normalFuels.flatMap(recipes -> Optional.of(GtUtil.mergeCollection(recipes, classicFuels.orElseGet(Collections::emptyList))));
    }

    public static <R> Optional<Collection<R>> parseConfig(ObjectMapper mapper, String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path) {
        return parseConfig(mapper, name, recipeClass, filter, path, false);
    }

    public static <R> Optional<Collection<R>> parseConfig(ObjectMapper mapper, String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path, boolean silent) {
        try {
            return parseConfig(mapper, recipeClass, filter, Files.newBufferedReader(path.resolve(name + ".yml")));
        } catch (IOException e) {
            if (!silent || !(e instanceof NoSuchFileException)) {
                GregTechMod.LOGGER.error("Failed to parse " + name + " recipes", e);
            }
            return Optional.empty();
        }
    }

    public static <R> Optional<Collection<R>> parseConfig(ObjectMapper mapper, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Reader reader) throws IOException {
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

    private static <T extends IMachineRecipe<?, ?>> boolean parseDynamicRecipes(String name, Class<? extends T> recipeClass, @Nullable Class<? extends RecipeFilter> filter, IGtRecipeManager<?, ?, T> manager) {
        progressBar.step(formatDisplayName(name));
        
        if(parseDynamicRecipes(name)) {
            parseConfig(RECIPE_MAPPER, name, recipeClass, filter, dynamicRecipesDir)
                    .ifPresent(recipes -> registerRecipes("dynamic " + name.replace('_', ' '), recipes, manager));
            return false;
        }
        return true;
    }

    private static boolean parseIC2DynamicRecipes(String name, IBasicMachineRecipeManager manager) {
        progressBar.step(formatDisplayName(name));
        
        if (parseDynamicRecipes(name)) {
            parseConfig(RECIPE_MAPPER, name, BasicMachineRecipe.class, null, dynamicRecipesDir)
                    .ifPresent(recipes -> registerRecipes("dynamic " + name.replace('_', ' '), recipes, (BasicMachineRecipeManager) manager));
            return false;
        }
        return true;
    }

    private static <T extends IMachineRecipe<?, ?>> void registerRecipes(String name, Collection<? extends T> recipes, IGtRecipeManager<?, ?, T> manager) {
        long successful = recipes.stream()
                .map(manager::addRecipe)
                .filter(Boolean::booleanValue)
                .count();
        GregTechMod.LOGGER.info("Loaded " + successful + " out of " + recipes.size() + " " + name + " recipes");
    }

    private static <T extends IBasicMachineRecipe> void registerRecipes(String name, Collection<? extends T> recipes, BasicMachineRecipeManager manager) {
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

    private static <T extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> void registerDynamicRecipes(String name, Collection<? extends T> recipes, BasicMachineRecipeManager manager, boolean serialize) {
        recipes.forEach(recipe -> ModHandler.addIC2Recipe(manager, recipe.getInput(), null, true, recipe.getOutput().toArray(new ItemStack[0])));
        if (serialize) serializeRecipes(name, recipes);
    }

    public static <R extends IMachineRecipe<?, ?>, M extends IGtRecipeManager<?, ?, R>> void registerDynamicRecipes(String name, Collection<R> recipes, M recipeManager, boolean serialize) {
        recipes.forEach(recipeManager::addRecipe);
        if (serialize) serializeRecipes(name, recipes);
    }

    public static void serializeRecipes(String name, Collection<?> recipes) {
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

    private static boolean parseDynamicRecipes(String name) {
        Path dest = dynamicRecipesDir.resolve(name + ".yml");
        return dest.toFile().exists();
    }

    private static Path relocateConfig(Path recipesPath, String target) {
        File configDir = new File(GregTechMod.configDir.toPath().resolve("GregTech").resolve(target).toString());
        configDir.mkdirs();
        return GtUtil.copyDir(recipesPath, configDir);
    }
    
    public static void registerProviders() {
        GtRecipes.printer.registerProvider(new TileEntityPrinter.PrinterRecipeProvider());
        
        GtFuels.diesel.registerProvider(new TileEntityDieselGenerator.FuelCanRecipeProvider());
    }
}
