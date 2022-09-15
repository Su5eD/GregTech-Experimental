package mods.gregtechmod.init;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import ic2.api.item.IC2Items;
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
import mods.gregtechmod.recipe.fuel.FuelFactory;
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
import java.io.IOException;
import java.io.Reader;
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
                .addDeserializer(IRecipeIngredientFluid.class, RecipeIngredientFluidDeserializer.INSTANCE));

        FUEL_MAPPER = mapperBase.copy()
            .registerModule(new SimpleModule()
                .addDeserializer(IRecipeIngredientFluid.class, FuelIngredientFluidDeserializer.INSTANCE));
    }

    public static void setupRecipes() {
        GregTechMod.LOGGER.info("Setting up machine recipes");

        GtRecipes.industrialCentrifuge = new RecipeManagerCellular();
        GtRecipes.assembler = new RecipeManagerMultiInput<>();
        GtRecipes.pulverizer = new RecipeManagerPulverizer();
        GtRecipes.industrialGrinder = new RecipeManagerSecondaryFluid<>();
        GtRecipes.industrialBlastFurnace = new RecipeManagerBlastFurnace();
        GtRecipes.industrialElectrolyzer = new RecipeManagerCellular();
        GtRecipes.canner = new RecipeManagerMultiInput<>();
        GtRecipes.alloySmelter = new RecipeManagerAlloySmelter();
        GtRecipes.implosion = new RecipeManagerMultiInput<>();
        GtRecipes.wiremill = new RecipeManagerBasic<>();
        GtRecipes.bender = new RecipeManagerBasic<>();
        GtRecipes.lathe = new RecipeManagerBasic<>();
        GtRecipes.vacuumFreezer = new RecipeManagerBasic<>();
        GtRecipes.chemical = new RecipeManagerMultiInput<>();
        GtRecipes.fusion = new RecipeManagerFusion();
        GtRecipes.industrialSawmill = new RecipeManagerSawmill();
        GtRecipes.distillation = new RecipeManagerCellular();
        GtRecipes.printer = new RecipeManagerPrinter();

        JavaUtil.setStaticValue(GregTechAPI.class, "recipeFactory", new RecipeFactory());
        JavaUtil.setStaticValue(GregTechAPI.class, "ingredientFactory", new RecipeIngredientFactory());

        recipesPath = GtUtil.getAssetPath("machine_recipes");
        classicRecipesPath = recipesPath.resolve("classic");
        experimentalRecipesPath = recipesPath.resolve("experimental");

        GregTechAPI.instance().registerCondition("mod_loaded", node -> Loader.isModLoaded(node.get("modid").asText()));
        GregTechAPI.instance().registerCondition("ore_exists", node -> OreDictUnificator.oreExists(node.get("ore").asText()));
    }

    public static void setupFuels() {
        GregTechMod.LOGGER.info("Setting up fuels");

        GtFuels.plasma = new FuelManagerFluid<>();
        GtFuels.magic = new FuelManagerFluid<>();
        GtFuels.diesel = new FuelManagerFluid<>();
        GtFuels.gas = new FuelManagerFluid<>();
        GtFuels.hot = new FuelManagerFluid<>();
        GtFuels.denseLiquid = new FuelManagerFluid<>();
        GtFuels.steam = new FuelManagerFluid<>();
        
        JavaUtil.setStaticValue(GregTechAPI.class, "fuelFactory", new FuelFactory());

        fuelsPath = GtUtil.getAssetPath("fuels");
        classicFuelsPath = fuelsPath.resolve("classic");
    }

    public static void loadRecipes() {
        progressBar = ProgressManager.push("Parsing Recipes", 21);

        GregTechMod.LOGGER.info("Parsing Machine Recipes");

        ItemStack gravel = new ItemStack(Blocks.GRAVEL);
        parseAndRegisterRecipes("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class, GtRecipes.industrialCentrifuge);
        parseAndRegisterRecipes("assembler", RecipeDualInput.class, GtRecipes.assembler);
        parseAndRegisterRecipes("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class, recipe -> !recipe.getInput().apply(gravel), GtRecipes.pulverizer);
        parseAndRegisterRecipes("industrial_grinder", RecipeGrinder.class, RecipeFilter.Default.class, GtRecipes.industrialGrinder);
        parseAndRegisterRecipes("industrial_blast_furnace", RecipeBlastFurnace.class, RecipeFilter.Energy.class, GtRecipes.industrialBlastFurnace);
        parseAndRegisterRecipes("industrial_electrolyzer", RecipeElectrolyzer.class, GtRecipes.industrialElectrolyzer);
        parseAndRegisterRecipes("canner", RecipeCanner.class, GtRecipes.canner);
        parseAndRegisterRecipes("alloy_smelter", RecipeAlloySmelter.class, GtRecipes.alloySmelter);
        parseAndRegisterRecipes("implosion", RecipeImplosion.class, RecipeFilter.Default.class, GtRecipes.implosion);
        parseAndRegisterRecipes("wiremill", RecipeSimple.class, GtRecipes.wiremill);
        parseAndRegisterRecipes("bender", RecipeSimple.class, GtRecipes.bender);
        parseAndRegisterRecipes("lathe", RecipeLathe.class, GtRecipes.lathe);
        parseAndRegisterRecipes("vacuum_freezer", RecipeVacuumFreezer.class, RecipeFilter.Energy.class, GtRecipes.vacuumFreezer);
        parseAndRegisterRecipes("chemical", RecipeChemical.class, RecipeFilter.Energy.class, GtRecipes.chemical);
        parseAndRegisterRecipes("fusion_fluid", RecipeFusionFluid.class, GtRecipes.fusion);
        parseAndRegisterRecipes("fusion_solid", RecipeFusionSolid.class, GtRecipes.fusion);
        parseAndRegisterRecipes("sawmill", RecipeSawmill.class, RecipeFilter.Default.class, GtRecipes.industrialSawmill);
        parseAndRegisterRecipes("distillation", RecipeDistillation.class, RecipeFilter.Energy.class, GtRecipes.distillation);
        parseAndRegisterRecipes("printer", RecipePrinter.class, GtRecipes.printer);

        // IC2 Recipes
        parseAndRegisterRecipes("compressor", (BasicMachineRecipeManager) Recipes.compressor);
        parseAndRegisterRecipes("extractor", (BasicMachineRecipeManager) Recipes.extractor);
        
        GtRecipes.printer.registerProvider(new TileEntityPrinter.PrinterRecipeProvider());
        GtFuels.diesel.registerProvider(new TileEntityDieselGenerator.FuelCanRecipeProvider());

        ProgressManager.pop(progressBar);
    }

    public static void loadFuels() {
        progressBar = ProgressManager.push("Parsing Fuels", 7);

        GregTechMod.LOGGER.info("Parsing fuels");

        parseAndRegisterFuels("plasma", FuelSimple.class, GtFuels.plasma);
        parseAndRegisterFuels("magic", FuelSimple.class, GtFuels.magic);
        parseAndRegisterFuels("diesel", FuelSimple.class, GtFuels.diesel);
        parseAndRegisterFuels("gas", FuelSimple.class, GtFuels.gas);
        parseAndRegisterFuels("hot", FuelMulti.class, GtFuels.hot);
        parseAndRegisterFuels("dense_liquid", FuelSimple.class, GtFuels.denseLiquid);
        parseAndRegisterFuels("steam", FuelSimple.class, GtFuels.steam);

        ProgressManager.pop(progressBar);
        ModCompat.registerBoilerFuels();
    }

    public static void loadGeneratedRecipes() {
        progressBar = ProgressManager.push("Loading Generated Recipes", 3);
        GregTechMod.LOGGER.info("Loading Dynamic Recipes");

        progressBar.step("Crafting Recipes");
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

        registerDynamicRecipes(DynamicRecipes.PULVERIZER.getRecipes(), GtRecipes.pulverizer);
        registerDynamicRecipes(DynamicRecipes.ALLOY_SMELTER.getRecipes(), GtRecipes.alloySmelter);
        registerDynamicRecipes(DynamicRecipes.CANNER.getRecipes(), GtRecipes.canner);
        registerDynamicRecipes(DynamicRecipes.LATHE.getRecipes(), GtRecipes.lathe);
        registerDynamicRecipes(DynamicRecipes.ASSEMBLER.getRecipes(), GtRecipes.assembler);
        registerDynamicRecipes(DynamicRecipes.BENDER.getRecipes(), GtRecipes.bender);
        registerDynamicRecipes(DynamicRecipes.SAWMILL.getRecipes(), GtRecipes.industrialSawmill);
        registerDynamicRecipes(DynamicRecipes.INDUSTRIAL_CENTRIFUGE.getRecipes(), GtRecipes.industrialCentrifuge);

        registerDynamicRecipes(JavaUtil.toList(DynamicRecipes.COMPRESSOR.getRecipes()), (BasicMachineRecipeManager) Recipes.compressor);
        registerDynamicRecipes(JavaUtil.toList(DynamicRecipes.EXTRACTOR.getRecipes()), (BasicMachineRecipeManager) Recipes.extractor);
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

    public static <R extends IFuel<?>> void parseAndRegisterFuels(String name, Class<? extends R> fuelClass, IFuelManager<R, ?> manager) {
        progressBar.step(formatDisplayName(name));

        Optional<Collection<R>> normalFuels = parseConfig(FUEL_MAPPER, name, fuelClass, null, fuelsPath, false);
        Optional<Collection<R>> classicFuels = GregTechMod.classic ? parseConfig(FUEL_MAPPER, name, fuelClass, null, classicFuelsPath, true) : Optional.empty();
        normalFuels
            .flatMap(recipes -> Optional.of(JavaUtil.mergeCollection(recipes, classicFuels.orElseGet(Collections::emptyList))))
            .ifPresent(fuels -> registerFuels(name.replace("_", ""), fuels, manager));
    }

    private static <R> Optional<Collection<R>> parseRecipes(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter) {
        progressBar.step(formatDisplayName(name));

        Optional<Collection<R>> normalRecipes = parseConfig(MachineRecipeParser.RECIPE_MAPPER, name, recipeClass, filter, recipesPath, false);
        Optional<Collection<R>> profileRecipes = parseConfig(RECIPE_MAPPER, name, recipeClass, filter, GregTechMod.classic ? classicRecipesPath : experimentalRecipesPath, true);
        return normalRecipes.flatMap(recipes -> Optional.of(JavaUtil.mergeCollection(recipes, profileRecipes.orElseGet(Collections::emptyList))));
    }

    private static <R> Optional<Collection<R>> parseConfig(ObjectMapper mapper, String name, Class<? extends R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path, boolean silent) {
        try {
            ObjectMapper objectMapper = mapper.copy();
            if (filter != null) objectMapper.addMixIn(IMachineRecipe.class, filter);

            Reader reader = Files.newBufferedReader(path.resolve(name + ".yml"));
            JsonNode node = objectMapper.readTree(reader);
            Iterator<JsonNode> recipeIterator = node.elements();
            while (recipeIterator.hasNext()) {
                JsonNode recipe = recipeIterator.next();
                if (recipe.has("conditions")) {
                    JsonNode conditions = recipe.get("conditions");
                    conditions.forEach(condition -> {
                        String type = condition.get("type").asText();
                        if (!GregTechAPI.instance().testCondition(type, condition)) {
                            recipeIterator.remove();
                        }
                    });
                }
                ((ObjectNode) recipe).remove("conditions");
            }
            return Optional.ofNullable(objectMapper.convertValue(node, objectMapper.getTypeFactory().constructCollectionType(List.class, recipeClass)));
        } catch (IOException e) {
            if (!silent || !(e instanceof NoSuchFileException)) {
                GregTechMod.LOGGER.error("Failed to parse " + name + " recipes", e);
            }
            return Optional.empty();
        }
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

    private static <R extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> void registerDynamicRecipes(Collection<? extends R> recipes, BasicMachineRecipeManager manager) {
        recipes.forEach(recipe -> ModHandler.addIC2Recipe(manager, recipe.getInput(), null, true, recipe.getOutput().toArray(new ItemStack[0])));
    }

    private static <R extends IMachineRecipe<?, ?>, M extends IGtRecipeManager<?, ?, R>> void registerDynamicRecipes(Collection<R> recipes, M recipeManager) {
        recipes.forEach(recipeManager::addRecipe);
    }
    
    private static String formatDisplayName(String str) {
        return StreamEx.of(str.split("_"))
            .map(JavaUtil::capitalizeString)
            .joining(" ");
    }
}
