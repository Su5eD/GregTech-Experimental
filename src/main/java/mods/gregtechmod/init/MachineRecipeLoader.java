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
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModCompat;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityPrinter;
import mods.gregtechmod.objects.blocks.teblocks.generator.TileEntityDieselGenerator;
import mods.gregtechmod.recipe.*;
import mods.gregtechmod.recipe.compat.BasicMachineRecipe;
import mods.gregtechmod.recipe.fuel.FluidFuelManager;
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
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MachineRecipeLoader {
    private static final Map<String, Predicate<JsonNode>> CONDITIONS = new HashMap<>();
    private static Path recipesPath = null;
    private static Path classicRecipesPath = null;
    private static Path experimentalRecipesPath = null;
    private static Path fuelsPath = null;
    private static Path classicFuelsPath = null;
    private static Path dynamicRecipesDir = null;
    private static final ObjectMapper mapper = new ObjectMapper(
            new YAMLFactory()
                    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES))
            .registerModule(new SimpleModule()
                    .addDeserializer(ItemStack.class, ItemStackDeserializer.INSTANCE)
                    .addDeserializer(FluidStack.class, FluidStackDeserializer.INSTANCE)
                    .addDeserializer(IRecipeIngredient.class, RecipeIngredientDeserializer.INSTANCE)
                    .addDeserializer(IRecipeIngredientFluid.class, RecipeIngredientFluidDeserializer.INSTANCE)
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

    public static void loadRecipes() {
        GregTechMod.logger.info("Loading machine recipes");

        GregTechAPI.recipeFactory = new RecipeFactory();
        GregTechAPI.ingredientFactory = new RecipeIngredientFactory();

        Path recipesPath = GtUtil.getAssetPath("assets/" + Reference.MODID + "/machine_recipes");
        Path gtConfig = relocateConfig(recipesPath, "machine_recipes");
        if (gtConfig == null) {
            GregTechMod.logger.error("Couldn't find the recipes config directory. Loading default recipes...");
            MachineRecipeLoader.recipesPath = recipesPath;
        } else MachineRecipeLoader.recipesPath = gtConfig;
        classicRecipesPath = MachineRecipeLoader.recipesPath.resolve("classic");
        experimentalRecipesPath = MachineRecipeLoader.recipesPath.resolve("experimental");

        registerCondition("mod_loaded", node -> Loader.isModLoaded(node.get("modid").asText()));
        registerCondition("ore_exists", node -> !OreDictUnificator.getFirstOre(node.get("ore").asText()).isEmpty());

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
                    List<IRecipePulverizer> filtered = recipes.stream()
                            .filter(recipe -> !recipe.getInput().apply(gravel))
                            .collect(Collectors.toList());
                    registerRecipes("pulverizer", filtered, GtRecipes.pulverizer);
                });

        GtRecipes.industrialGrinder = new RecipeManagerMultiInput<>();
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

        GtRecipes.sawmill = new RecipeManagerSawmill();
        parseRecipes("sawmill", RecipeSawmill.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("sawmill", recipes, GtRecipes.sawmill));

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

        registerMatterAmplifiers();
        addScrapboxDrops();
        loadRecyclerBlackList();
    }

    public static void loadFuels() {
        GregTechMod.logger.info("Loading fuels");

        Path recipesPath = GtUtil.getAssetPath("assets/" + Reference.MODID + "/fuels");
        Path gtConfig = relocateConfig(recipesPath, "fuels");
        if (gtConfig == null) {
            GregTechMod.logger.error("Couldn't find the fuels config directory. Loading default fuels...");
            MachineRecipeLoader.fuelsPath = recipesPath;
        } else MachineRecipeLoader.fuelsPath = gtConfig;
        classicFuelsPath = fuelsPath.resolve("classic");

        GtFuels.plasma = new FluidFuelManager<>();
        parseFuels("plasma", FuelSimple.class, null)
                .ifPresent(fuels -> registerFuels("plasma", fuels, GtFuels.plasma));

        GtFuels.magic = new FluidFuelManager<>();
        parseFuels("magic", FuelSimple.class, null)
                .ifPresent(fuels -> registerFuels("magic", fuels, GtFuels.magic));

        GtFuels.diesel = new FluidFuelManager<>();
        parseFuels("diesel", FuelSimple.class, null)
                .ifPresent(fuels -> registerFuels("diesel", fuels, GtFuels.diesel));
        
        GtFuels.gas = new FluidFuelManager<>();
        parseFuels("gas", FuelSimple.class, null)
                .ifPresent(fuels -> registerFuels("gas", fuels, GtFuels.gas));

        GtFuels.hot = new FluidFuelManager<>();
        parseFuels("hot", FuelMulti.class, null)
                .ifPresent(fuels -> registerFuels("hot", fuels, GtFuels.hot));

        GtFuels.denseLiquid = new FluidFuelManager<>();
        parseFuels("dense_liquid", FuelSimple.class, null)
                .ifPresent(fuels -> registerFuels("dense liquid", fuels, GtFuels.denseLiquid));

        ModCompat.registerBoilerFuels();
    }

    public static void loadDynamicRecipes() {
        GregTechMod.logger.info("Loading dynamic recipes");
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
        DynamicRecipes.addCompressorRecipes = parseDynamicRecipes("compressor", BasicMachineRecipe.class, null, DynamicRecipes.COMPRESSOR);
        DynamicRecipes.addExtractorRecipes = parseDynamicRecipes("extractor", BasicMachineRecipe.class, null, DynamicRecipes.EXTRACTOR);

        DynamicRecipes.processCraftingRecipes();
        DynamicRecipes.applyMaterialUsages();
        ModCompat.addRollingMachineRecipes();
        ModCompat.registerTools();

        ItemStack ingotCopper = IC2Items.getItem("ingot", "copper");
        ItemStack bronze = ModHandler.getCraftingResult(ingotCopper, ingotCopper, ItemStack.EMPTY, ingotCopper, IC2Items.getItem("ingot", "tin"));
        if (!bronze.isEmpty()) {
            int count = bronze.getCount();
            GtRecipes.industrialCentrifuge.addRecipe(
                    RecipeCentrifuge.create(RecipeIngredientOre.create("dustBronze", count < 3 ? 1 : count / 2),
                            Arrays.asList(new ItemStack(BlockItems.Smalldust.COPPER.getInstance(), 6),
                                    new ItemStack(BlockItems.Smalldust.TIN.getInstance(), 2)),
                            0,
                            1500,
                            CellType.CELL));
        }

        ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
        ItemStack stick = new ItemStack(Items.STICK);
        ItemStack rail = ModHandler.getCraftingResult(ingotIron, ItemStack.EMPTY, ingotIron, ingotIron, stick, ingotIron, ingotIron, ItemStack.EMPTY, ingotIron);
        if (!rail.isEmpty()) DynamicRecipes.addPulverizerRecipe(rail, StackUtil.setSize(IC2Items.getItem("dust", "iron"), 6), new ItemStack(BlockItems.Smalldust.WOOD.getInstance(), 2), 95);
        ItemStack ingotGold = new ItemStack(Items.GOLD_INGOT);
        ItemStack redstone = new ItemStack(Items.REDSTONE);
        ItemStack poweredRail = ModHandler.getCraftingResult(ingotGold, ItemStack.EMPTY, ingotGold, ingotGold, stick, ingotGold, ingotGold, redstone, ingotGold);
        if (!poweredRail.isEmpty()) DynamicRecipes.addPulverizerRecipe(poweredRail, StackUtil.setSize(IC2Items.getItem("dust", "gold"), 6), redstone, 95);

        ItemStack ingotTin = IC2Items.getItem("ingot", "tin");
        ItemStack tinCan = ModHandler.getCraftingResult(ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ingotTin, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        if (!tinCan.isEmpty()) {
            int tinNuggetCount = 27 / tinCan.getCount();
            if (tinNuggetCount > 0) {
                tinCan.setCount(1);
                if (tinNuggetCount % 9 == 0) {
                    DynamicRecipes.addSmeltingAndAlloySmeltingRecipe(tinCan, StackUtil.setSize(ingotTin, tinNuggetCount / 9));
                }
                else DynamicRecipes.addSmeltingAndAlloySmeltingRecipe(tinCan, new ItemStack(BlockItems.Nugget.TIN.getInstance(), tinNuggetCount));
            }
        }

        DynamicRecipes.addPulverizerRecipe(ProfileDelegate.getCell(null), new ItemStack(BlockItems.Smalldust.TIN.getInstance(), 9), true);
        ModHandler.addLiquidTransposerEmptyRecipe(IC2Items.getItem("dust", "coal_fuel"), new FluidStack(FluidRegistry.WATER, 100), IC2Items.getItem("dust", "coal"), 1250);
        if (GregTechMod.classic) {
            DynamicRecipes.addSmeltingRecipe("machineCasing", IC2Items.getItem("resource", "machine"), StackUtil.setSize(IC2Items.getItem("ingot", "refined_iron"), 8));
        } else {
            DynamicRecipes.addSmeltingRecipe("machineCasing", IC2Items.getItem("resource", "machine"), new ItemStack(Items.IRON_INGOT, 8));
        }
        DynamicRecipes.addSmeltingRecipe("resin", new ItemStack(Items.SLIME_BALL), IC2Items.getItem("misc_resource", "resin"));
    }

    public static void registerDynamicRecipes() {
        DynamicRecipes.processMaterialUsages();

        serializeRecipes("Pulverizer", DynamicRecipes.PULVERIZER.getRecipes(), GtRecipes.pulverizer, DynamicRecipes.addPulverizerRecipes);
        serializeRecipes("Alloy Smelter", DynamicRecipes.ALLOY_SMELTER.getRecipes(), GtRecipes.alloySmelter, DynamicRecipes.addAlloySmelterRecipes);
        serializeRecipes("Canner", DynamicRecipes.CANNER.getRecipes(), GtRecipes.canner, DynamicRecipes.addCannerRecipes);
        serializeRecipes("Lathe", DynamicRecipes.LATHE.getRecipes(), GtRecipes.lathe, DynamicRecipes.addLatheRecipes);
        serializeRecipes("Assembler", DynamicRecipes.ASSEMBLER.getRecipes(), GtRecipes.assembler, DynamicRecipes.addAssemblerRecipes);
        serializeRecipes("Bender", DynamicRecipes.BENDER.getRecipes(), GtRecipes.bender, DynamicRecipes.addBenderRecipes);
        serializeRecipes("Sawmill", DynamicRecipes.SAWMILL.getRecipes(), GtRecipes.sawmill, DynamicRecipes.addSawmillRecipes);
        serializeRecipes("Industrial Centrifuge", DynamicRecipes.INDUSTRIAL_CENTRIFUGE.getRecipes(), GtRecipes.industrialCentrifuge, DynamicRecipes.addCentrifugeRecipes);

        List<MachineRecipe<IRecipeInput, Collection<ItemStack>>> compressorRecipes = StreamSupport.stream(DynamicRecipes.COMPRESSOR.getRecipes().spliterator(), false)
                .collect(Collectors.toList());
        serializeRecipes("Compressor", compressorRecipes, (BasicMachineRecipeManager) Recipes.compressor, DynamicRecipes.addCompressorRecipes);

        List<MachineRecipe<IRecipeInput, Collection<ItemStack>>> extractorRecipes = StreamSupport.stream(DynamicRecipes.EXTRACTOR.getRecipes().spliterator(), false)
                .collect(Collectors.toList());
        serializeRecipes("Extractor", extractorRecipes, (BasicMachineRecipeManager) Recipes.extractor, DynamicRecipes.addExtractorRecipes);
    }

    public static void registerCondition(String name, Predicate<JsonNode> predicate) {
        CONDITIONS.put(name, predicate);
    }

    public static <R> Optional<Collection<R>> parseRecipes(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter) {
        Optional<Collection<R>> normalRecipes = parseConfig(name, recipeClass, filter, recipesPath);
        Optional<Collection<R>> profileRecipes = GregTechMod.classic ? parseConfig(name, recipeClass, filter, classicRecipesPath, true) : parseConfig(name, recipeClass, filter, experimentalRecipesPath, true);
        return normalRecipes.flatMap(recipes -> Optional.of(GtUtil.mergeCollection(recipes, profileRecipes.orElseGet(Collections::emptyList))));
    }

    public static <R> Optional<Collection<R>> parseFuels(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter) {
        Optional<Collection<R>> normalFuels = parseConfig(name, recipeClass, filter, fuelsPath, false);
        Optional<Collection<R>> classicFuels = GregTechMod.classic ? parseConfig(name, recipeClass, filter, classicFuelsPath, true) : Optional.empty();
        return normalFuels.flatMap(recipes -> Optional.of(GtUtil.mergeCollection(recipes, classicFuels.orElseGet(Collections::emptyList))));
    }

    public static <R> Optional<Collection<R>> parseConfig(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path) {
        return parseConfig(name, recipeClass, filter, path, false);
    }

    public static <R> Optional<Collection<R>> parseConfig(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path, boolean silent) {
        try {
            return parseConfig(recipeClass, filter, Files.newBufferedReader(path.resolve(name + ".yml")));
        } catch (IOException e) {
            if (!silent) {
                GregTechMod.logger.error("Failed to parse " + name + " recipes");
                e.printStackTrace();
            }
            return Optional.empty();
        }
    }

    public static <R> Optional<Collection<R>> parseConfig(Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Reader reader) throws IOException {
        ObjectMapper recipeMapper = mapper.copy();
        if (filter != null) recipeMapper.addMixIn(IMachineRecipe.class, filter);

        JsonNode node = recipeMapper.readTree(reader);
        Iterator<JsonNode> recipeIterator = node.elements();
        while (recipeIterator.hasNext()) {
            JsonNode recipe = recipeIterator.next();
            if (recipe.has("conditions")) {
                JsonNode conditions = recipe.get("conditions");
                Iterator<JsonNode> conditionIterator = conditions.elements();
                while (conditionIterator.hasNext()) {
                    JsonNode condition = conditionIterator.next();
                    String type = condition.get("type").asText();
                    if (CONDITIONS.containsKey(type)) {
                        if (!CONDITIONS.get(type).test(condition))
                            recipeIterator.remove();
                    } else throw new IllegalArgumentException("Unknown condition type: "+type);
                }
            }
            ((ObjectNode) recipe).remove("conditions");
        }
        return Optional.ofNullable(recipeMapper.convertValue(node, mapper.getTypeFactory().constructCollectionType(List.class, recipeClass)));
    }

    private static <T extends IMachineRecipe<?, ?>> boolean parseDynamicRecipes(String name, Class<? extends T> recipeClass, @Nullable Class<? extends RecipeFilter> filter, IGtRecipeManager<?, ?, T> manager) {
        if(!shouldAddNewDynamicRecipes(name)) {
            parseConfig(name, recipeClass, filter, dynamicRecipesDir)
                    .ifPresent(recipes -> registerRecipes("dynamic "+name.replace('_', ' '), recipes, manager));
            return false;
        }
        return true;
    }

    private static <T extends IBasicMachineRecipe> boolean parseDynamicRecipes(String name, Class<? extends T> recipeClass, @Nullable Class<? extends RecipeFilter> filter, IBasicMachineRecipeManager manager) {
        if (!shouldAddNewDynamicRecipes(name)) {
            parseConfig(name, recipeClass, filter, dynamicRecipesDir)
                    .ifPresent(recipes -> registerRecipes("dynamic "+name.replace('_', ' '), recipes, (BasicMachineRecipeManager) manager));
            return false;
        }
        return true;
    }

    private static <T extends IMachineRecipe<?, ?>> void registerRecipes(String name, Collection<? extends T> recipes, IGtRecipeManager<?, ?, T> manager) {
        int total = recipes.size();
        long successful = recipes.stream()
                .map(manager::addRecipe)
                .filter(Boolean::booleanValue)
                .count();
        GregTechMod.logger.info("Loaded " + successful + " out of " + total + " " + name + " recipes");
    }

    private static <T extends IBasicMachineRecipe> void registerRecipes(String name, Collection<? extends T> recipes, BasicMachineRecipeManager manager) {
        int total = recipes.size();
        long successful = recipes.stream()
                .map(recipe -> {
                    IRecipeInput input = recipe.getInput();
                    if (recipe.shouldOverwrite()) input.getInputs().forEach(stack -> ModHandler.removeIC2Recipe(stack, manager));
                    return manager.addRecipe(input, null, false, recipe.getOutput().toArray(new ItemStack[0]));
                })
                .filter(Boolean::booleanValue)
                .count();
        GregTechMod.logger.info("Loaded " + successful + " out of " + total + " " + name + " recipes");
    }

    private static <T extends IFuel<?>, I> void registerFuels(String name, Collection<? extends T> fuels, IFuelManager<T, I> manager) {
        int total = fuels.size();
        long successful = fuels.stream()
                .map(manager::addFuel)
                .filter(Boolean::booleanValue)
                .count();
        GregTechMod.logger.info("Loaded " + successful + " out of " + total + " " + name + " fuels");
    }

    private static <T extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> void serializeRecipes(String name, Collection<? extends T> recipes, BasicMachineRecipeManager manager, boolean write) {
        recipes.forEach(recipe -> {
            IRecipeInput input = recipe.getInput();
            input.getInputs().forEach(stack -> ModHandler.removeIC2Recipe(stack, manager));
            manager.addRecipe(input, null, false, recipe.getOutput().toArray(new ItemStack[0]));
        });
        if (write) serializeRecipes(name, recipes);
    }

    public static <R extends IMachineRecipe<?, ?>, M extends IGtRecipeManager<?, ?, R>> void serializeRecipes(String name, Collection<R> recipes, M recipeManager, boolean write) {
        recipes.forEach(recipeManager::addRecipe);
        if (write) serializeRecipes(name, recipes);
    }

    public static <R extends IMachineRecipe<?, ?>> void serializeRecipes(String name, Collection<?> recipes) {
        try {
            File file = dynamicRecipesDir.resolve(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name.replace(" ", "")) +".yml").toFile();
            file.createNewFile();

            OutputStream output = Files.newOutputStream(file.toPath());
            output.write(("# Dynamic " + name + " recipes\n").getBytes(StandardCharsets.UTF_8));

            mapper.writeValue(output, recipes);
        } catch (IOException e) {
            GregTechMod.logger.error("Failed to serialize " + name + " recipes");
            e.printStackTrace();
        }
    }

    private static boolean shouldAddNewDynamicRecipes(String name) {
        Path dest = dynamicRecipesDir.resolve(name+".yml");
        return !dest.toFile().exists();
    }

    private static Path relocateConfig(Path recipesPath, String target) {
        File configDir = new File(GregTechMod.configDir.toURI().getPath() + "/GregTech/"+target);
        configDir.mkdirs();
        return copyDir(recipesPath, configDir);
    }

    private static Path copyDir(Path source, File target) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(source);
            for (Path path : stream) {
                File dest = new File(Paths.get(target.getPath(), path.getFileName().toString()).toUri());
                if (!dest.exists()) {
                    if (path.toString().endsWith("/")) {
                        dest.mkdirs();
                        copyDir(path, dest);
                        continue;
                    }

                    GregTechMod.logger.debug("Copying file " + path + " to " + dest.toPath());
                    BufferedReader in = Files.newBufferedReader(path);
                    FileOutputStream out = new FileOutputStream(dest);
                    for (int i; (i = in.read()) != -1; ) out.write(i);
                    in.close();
                    out.close();
                }
            }
            return target.toPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void registerProviders() {
        GtRecipes.printer.registerProvider(new TileEntityPrinter.PrinterRecipeProvider());
        
        GtFuels.diesel.registerProvider(new TileEntityDieselGenerator.FuelCanRecipeProvider());
    }

    private static void registerMatterAmplifiers() {
        GregTechMod.logger.info("Adding matter amplifiers");
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustElectrotine"), 5000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustTungsten"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustManganese"), 5000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustRedstone"), 5000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustGlowstone"), 25000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustPlatinum"), 100000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustIridium"), 100000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustEnderPearl"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustEnderEye"), 75000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustOlivine"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustEmerald"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustDiamond"), 125000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustRuby"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustSapphire"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustGreenSapphire"), 50000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustUranium"), 1000000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustOsmium"), 200000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustPlutonium"), 2000000, null, true);
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict("dustThorium"), 500000, null, true);
    }

    private static void addScrapboxDrops() {
        GregTechMod.logger.info("Adding Scrapbox drops");
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("crafting", "scrap"), 200);
        addScrapboxDrop(Items.WOODEN_AXE, 2);
        addScrapboxDrop(Items.WOODEN_SWORD, 2);
        addScrapboxDrop(Items.WOODEN_SHOVEL, 2);
        addScrapboxDrop(Items.WOODEN_PICKAXE, 2);
        addScrapboxDrop(Items.SIGN, 2);
        addScrapboxDrop(Items.STICK, 9.5F);
        Recipes.scrapboxDrops.addDrop(new ItemStack(Blocks.PUMPKIN), (float) 0.5);
        addScrapboxDrop(Items.ROTTEN_FLESH, 9);
        addScrapboxDrop(Items.COOKED_PORKCHOP, 0.4F);
        addScrapboxDrop(Items.COOKED_BEEF, 0.4F);
        addScrapboxDrop(Items.COOKED_CHICKEN, 0.4F);
        addScrapboxDrop(Items.APPLE, 0.5F);
        addScrapboxDrop(Items.BREAD, 0.5F);
        addScrapboxDrop(Items.CAKE, 0.1F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("filled_tin_can"), 1);
        Recipes.scrapboxDrops.addDrop(ProfileDelegate.getCell("silicon"), 0.2F);
        Recipes.scrapboxDrops.addDrop(ProfileDelegate.getCell("water"), 1);
        Recipes.scrapboxDrops.addDrop(ProfileDelegate.getCell(null), 2);
        addScrapboxDrop(Items.PAPER, 5);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("crafting", "plant_ball"), 0.7F);
        addScrapboxDrop(BlockItems.Dust.WOOD.getInstance(), 3.8F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("single_use_battery"), 2.7F);
        addScrapboxDrop(BlockItems.Component.MACHINE_PARTS.getInstance(), 0.8F);
        addScrapboxDrop(BlockItems.Component.ADVANCED_CIRCUIT_PARTS.getInstance(), 1.2F);
        addScrapboxDrop(BlockItems.Component.CIRCUIT_BOARD_BASIC.getInstance(), 1.8F);
        addScrapboxDrop(BlockItems.Component.CIRCUIT_BOARD_ADVANCED.getInstance(), 0.4F);
        addScrapboxDrop(BlockItems.Component.CIRCUIT_BOARD_PROCESSOR.getInstance(), 0.2F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("cable", "type:copper,insulation:1"), 2);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("cable", "type:gold,insulation:2"), 0.4F);
        addScrapboxDrop(BlockItems.Dust.CHARCOAL.getInstance(), 2.5F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("dust", "iron"), 1);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("dust", "gold"), 1);
        addScrapboxDrop(BlockItems.Dust.SILVER.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.ELECTRUM.getInstance(), 0.5F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("dust", "tin"), 1.2F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("dust", "copper"), 1.2F);
        addScrapboxDrop(BlockItems.Dust.BAUXITE.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.ALUMINIUM.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.LEAD.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.NICKEL.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.ZINC.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.BRASS.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.STEEL.getInstance(), 0.5F);
        addScrapboxDrop(BlockItems.Dust.OBSIDIAN.getInstance(), 1.5F);
        Recipes.scrapboxDrops.addDrop(IC2Items.getItem("dust", "sulfur"), 1.5F);
        addScrapboxDrop(BlockItems.Dust.SALTPETER.getInstance(), 2);
        addScrapboxDrop(BlockItems.Dust.LAZURITE.getInstance(), 2);
    }

    private static void addScrapboxDrop(Item item, float value) {
        Recipes.scrapboxDrops.addDrop(new ItemStack(item), value);
    }

    public static void loadRecyclerBlackList() {
        GregTechMod.logger.info("Adding stuff to the Recycler blacklist");

        if (GregTechConfig.DISABLED_RECIPES.easyMobGrinderRecycling) {
            addToRecyclerBlacklist(Items.ARROW);
            addToRecyclerBlacklist(Items.BONE);
            addToRecyclerBlacklist(new ItemStack(Items.DYE, 1, 15));
            addToRecyclerBlacklist(Items.ROTTEN_FLESH);
            addToRecyclerBlacklist(Items.STRING);
            addToRecyclerBlacklist(Items.EGG);
        }

        if (GregTechConfig.DISABLED_RECIPES.easyStoneRecycling) {
            addToRecyclerBlacklist(Blocks.SAND);
            addToRecyclerBlacklist(new ItemStack(Blocks.SANDSTONE, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(new ItemStack(Blocks.RED_SANDSTONE, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(Blocks.GLASS);
            addToRecyclerBlacklist(Items.GLASS_BOTTLE);
            addToRecyclerBlacklist(Items.POTIONITEM);
            addToRecyclerBlacklist(Items.SPLASH_POTION);
            addToRecyclerBlacklist(Items.LINGERING_POTION);
            ItemStack stone = new ItemStack(Blocks.STONE);
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(ModHandler.getSmeltingOutput(stone));
            addToRecyclerBlacklistOreDict("rodStone");
            addToRecyclerBlacklist(ModHandler.getCraftingResult(stone, ItemStack.EMPTY, stone, ItemStack.EMPTY, stone));
            if (ModHandler.buildcraftTransport) {
                addToRecyclerBlacklist(ModHandler.getModItem("buildcrafttransport", "pipe_stone_item"));
                addToRecyclerBlacklist(ModHandler.getModItem("buildcrafttransport", "pipe_cobble_item"));
                addToRecyclerBlacklist(ModHandler.getModItem("buildcrafttransport", "pipe_sandstone_item"));
            }
            addToRecyclerBlacklist(Blocks.GLASS_PANE);
            addToRecyclerBlacklist(Blocks.STAINED_GLASS);
            addToRecyclerBlacklist(Blocks.STAINED_GLASS_PANE);
            addToRecyclerBlacklist(Blocks.COBBLESTONE);
            addToRecyclerBlacklist(Blocks.COBBLESTONE_WALL);
            addToRecyclerBlacklist(Blocks.SANDSTONE_STAIRS);
            addToRecyclerBlacklist(Blocks.RED_SANDSTONE_STAIRS);
            addToRecyclerBlacklist(Blocks.STONE_STAIRS);
            addToRecyclerBlacklist(Blocks.STONE_BRICK_STAIRS);
            addToRecyclerBlacklist(Blocks.FURNACE);
            addToRecyclerBlacklist(new ItemStack(Blocks.WOODEN_SLAB, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_SLAB, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_SLAB2));
            addToRecyclerBlacklist(new ItemStack(Blocks.PURPUR_SLAB));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_PRESSURE_PLATE));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_BUTTON));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONEBRICK, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(Blocks.LEVER);
        }
        addToRecyclerBlacklist(Items.SNOWBALL);
        addToRecyclerBlacklist(Blocks.ICE);
        addToRecyclerBlacklist(Blocks.SNOW);
        addToRecyclerBlacklist(Blocks.SNOW_LAYER);
    }

    private static void addToRecyclerBlacklist(Block block) {
        addToRecyclerBlacklist(new ItemStack(block));
    }

    private static void addToRecyclerBlacklist(Item item) {
        addToRecyclerBlacklist(new ItemStack(item));
    }

    private static void addToRecyclerBlacklist(ItemStack stack) {
        if (!stack.isEmpty()) Recipes.recyclerBlacklist.add(Recipes.inputFactory.forStack(stack));
    }

    private static void addToRecyclerBlacklistOreDict(String ore) {
        Recipes.recyclerBlacklist.add(Recipes.inputFactory.forOreDict(ore));
    }
}
