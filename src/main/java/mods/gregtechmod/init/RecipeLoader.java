package mods.gregtechmod.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.*;
import mods.gregtechmod.recipe.fuel.FluidFuelManager;
import mods.gregtechmod.recipe.fuel.FuelFluid;
import mods.gregtechmod.recipe.fuel.FuelSolid;
import mods.gregtechmod.recipe.fuel.SolidFuelManager;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.*;
import mods.gregtechmod.recipe.util.IBasicMachineRecipe;
import mods.gregtechmod.recipe.util.RecipeFilter;
import mods.gregtechmod.recipe.util.deserializer.*;
import mods.gregtechmod.recipe.util.serializer.*;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RecipeLoader {
    private static Path recipesPath = null;
    private static Path fuelsPath = null;
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
                    .addSerializer(IRecipeIngredient.class, RecipeIngredientSerializer.INSTANCE)
                    .addSerializer(IRecipeInput.class, RecipeInputSerializer.INSTANCE)
                    .addSerializer(RecipeCanner.class, RecipeCannerSerializer.INSTANCE)
                    .addSerializer(RecipeDualInput.class, RecipeDualInputSerializer.INSTANCE)
                    .addSerializer(RecipeLathe.class, RecipeLatheSerializer.INSTANCE)
                    .addSerializer(RecipePulverizer.class, RecipePulverizerSerializer.INSTANCE)
                    .addSerializer(RecipeSimple.class, RecipeSimpleSerializer.INSTANCE)
                    .addSerializer(RecipeSawmill.class, RecipeSawmillSerializer.INSTANCE)
                    .addSerializer(MachineRecipe.class, MachineRecipeSerializer.INSTANCE));
    private static FileSystem modFile;

    private static FileSystem getModFile() {
        if (modFile == null) {
            try {
                File file = Loader.instance().activeModContainer().getSource();
                modFile = FileSystems.newFileSystem(file.toPath(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return modFile;
    }

    public static void loadRecipes() {
        GregTechAPI.logger.info("Loading machine recipes");

        GregTechAPI.recipeFactory = new RecipeFactory();
        GregTechAPI.ingredientFactory = new RecipeIngredientFactory();

        Path recipesPath = getModFile().getPath("assets", Reference.MODID, "machine_recipes");
        Path gtConfig = relocateConfig(recipesPath, "machine_recipes");
        if (gtConfig == null) {
            GregTechAPI.logger.error("Couldn't find the recipes config directory. Loading default recipes...");
            RecipeLoader.recipesPath = recipesPath;
        } else RecipeLoader.recipesPath = gtConfig;

        GtRecipes.industrialCentrifuge = new RecipeManagerCellular();
        RecipeLoader.parseConfig("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("industrial centrifuge", recipes, GtRecipes.industrialCentrifuge));
        GtRecipes.assembler = new RecipeManagerMultiInput<>();
        RecipeLoader.parseConfig("assembler", RecipeDualInput.class, null)
                .ifPresent(recipes -> registerRecipes("assembler", recipes, GtRecipes.assembler));

        GtRecipes.pulverizer = new RecipeManagerPulverizer();
        RecipeLoader.parseConfig("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("pulverizer", recipes, GtRecipes.pulverizer));

        GtRecipes.grinder = new RecipeManagerGrinder();
        RecipeLoader.parseConfig("grinder", RecipeGrinder.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("grinder", recipes, GtRecipes.grinder));

        GtRecipes.blastFurnace = new RecipeManagerBlastFurnace();
        RecipeLoader.parseConfig("blast_furnace", RecipeBlastFurnace.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("blast furnace", recipes, GtRecipes.blastFurnace));

        GtRecipes.electrolyzer = new RecipeManagerCellular();
        parseConfig("electrolyzer", RecipeElectrolyzer.class, null)
                .ifPresent(recipes -> registerRecipes("electrolyzer", recipes, GtRecipes.electrolyzer));

        GtRecipes.canner = new RecipeManagerMultiInput<>();
        parseConfig("canner", RecipeCanner.class, null)
                .ifPresent(recipes -> registerRecipes("canner", recipes, GtRecipes.canner));

        GtRecipes.alloySmelter = new RecipeManagerMultiInput<>();
        parseConfig("alloy_smelter", RecipeDualInput.class, null)
                .ifPresent(recipes -> registerRecipes("alloy smelter", recipes, GtRecipes.alloySmelter));

        GtRecipes.implosion = new RecipeManagerMultiInput<>();
        parseConfig("implosion", RecipeImplosion.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("implosion", recipes, GtRecipes.implosion));

        GtRecipes.wiremill = new RecipeManagerBasic<>();
        parseConfig("wiremill", RecipeSimple.class, null)
                .ifPresent(recipes -> registerRecipes("wiremill", recipes, GtRecipes.wiremill));

        GtRecipes.bender = new RecipeManagerBasic<>();
        parseConfig("bender", RecipeSimple.class, null)
                .ifPresent(recipes -> registerRecipes("bender", recipes, GtRecipes.bender));

        GtRecipes.lathe = new RecipeManagerBasic<>();
        parseConfig("lathe", RecipeLathe.class, null)
                .ifPresent(recipes -> registerRecipes("lathe", recipes, GtRecipes.lathe));

        GtRecipes.vacuumFreezer = new RecipeManagerBasic<>();
        parseConfig("vacuum_freezer", RecipeVacuumFreezer.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("vacuum freezer", recipes, GtRecipes.vacuumFreezer));

        GtRecipes.chemical = new RecipeManagerMultiInput<>();
        parseConfig("chemical", RecipeChemical.class, RecipeFilter.Energy.class)
                .ifPresent(recipes -> registerRecipes("chemical", recipes, GtRecipes.chemical));

        GtRecipes.fusionFluid = new RecipeManagerFusionFluid();
        parseConfig("fusion_fluid", RecipeFusionFluid.class, null)
                .ifPresent(recipes -> registerRecipes("fluid fusion", recipes, GtRecipes.fusionFluid));

        GtRecipes.fusionSolid = new RecipeManagerMultiInput<>();
        parseConfig("fusion_solid", RecipeFusionSolid.class, null)
                .ifPresent(recipes -> registerRecipes("solid fusion", recipes, GtRecipes.fusionSolid));

        GtRecipes.sawmill = new RecipeManagerSawmill();
        parseConfig("sawmill", RecipeSawmill.class, RecipeFilter.Default.class)
                .ifPresent(recipes -> registerRecipes("sawmill", recipes, GtRecipes.sawmill));

        // IC2 Recipes
        parseConfig("compressor", BasicMachineRecipe.class, null)
                .ifPresent(recipes -> registerRecipes("compressor", recipes, Recipes.compressor));

        parseConfig("extractor", BasicMachineRecipe.class, null)
                .ifPresent(recipes -> registerRecipes("extractor", recipes, Recipes.extractor));

        registerMatterAmplifiers();
    }

    public static void loadFuels() {
        GregTechAPI.logger.info("Loading fuels");

        Path recipesPath = getModFile().getPath("assets", Reference.MODID, "fuels");
        Path gtConfig = relocateConfig(recipesPath, "fuels");
        if (gtConfig == null) {
            GregTechAPI.logger.error("Couldn't find the fuels config directory. Loading default fuels...");
            RecipeLoader.fuelsPath = recipesPath;
        } else RecipeLoader.fuelsPath = gtConfig;

        GtFuels.plasmaFuels = new FluidFuelManager();
        parseConfig("plasma", FuelFluid.class, null, fuelsPath)
                .ifPresent(fuels -> registerFuels("plasma", fuels, GtFuels.plasmaFuels));

        GtFuels.magicFuels = new SolidFuelManager();
        parseConfig("magic", FuelSolid.class, null, fuelsPath)
                .ifPresent(fuels -> registerFuels("magic", fuels, GtFuels.magicFuels));
    }

    public static void loadDynamicRecipes() {
        GregTechAPI.logger.info("Loading dynamic recipes");
        dynamicRecipesDir = GregTechMod.configDir.toPath().resolve("GregTech/machine_recipes/dynamic");
        dynamicRecipesDir.toFile().mkdirs();

        DynamicRecipes.addPulverizerRecipes = parseDynamicRecipes("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class, DynamicRecipes.PULVERIZER);
        DynamicRecipes.addAlloySmelterRecipes = parseDynamicRecipes("alloy_smelter", RecipeDualInput.class, null, DynamicRecipes.ALLOY_SMELTER);
        DynamicRecipes.addCannerRecipes = parseDynamicRecipes("canner", RecipeCanner.class, null, DynamicRecipes.CANNER);
        DynamicRecipes.addLatheRecipes = parseDynamicRecipes("lathe", RecipeLathe.class, null, DynamicRecipes.LATHE);
        DynamicRecipes.addAssemblerRecipes = parseDynamicRecipes("assembler", RecipeDualInput.class, null, DynamicRecipes.ASSEMBLER);
        DynamicRecipes.addBenderRecipes = parseDynamicRecipes("bender", RecipeSimple.class, null, DynamicRecipes.BENDER);
        DynamicRecipes.addSawmillRecipes = parseDynamicRecipes("sawmill", RecipeSawmill.class, RecipeFilter.Default.class, DynamicRecipes.SAWMILL);
        DynamicRecipes.addCompressorRecipes = parseDynamicRecipes("compressor", BasicMachineRecipe.class, null, DynamicRecipes.COMPRESSOR);

        DynamicRecipes.processCraftingRecipes();
        ItemStack copper = IC2Items.getItem("ingot", "copper");
        ItemStack bronze = ModHandler.getCraftingResult(copper, copper, ItemStack.EMPTY, copper, IC2Items.getItem("ingot", "tin"));
        if (!bronze.isEmpty()) {
            int count = bronze.getCount();
            GtRecipes.industrialCentrifuge.addRecipe(
                    RecipeCentrifuge.create(RecipeIngredientOre.create("dustBronze", count < 3 ? 1 : count / 2),
                            Arrays.asList(StackUtil.copyWithSize(IC2Items.getItem("dust", "small_copper"), 6),
                                    StackUtil.copyWithSize(IC2Items.getItem("dust", "small_tin"), 2)),
                            0,
                            1500));
        }
    }

    public static void registerDynamicRecipes() {
        if (DynamicRecipes.addPulverizerRecipes) serializeRecipes("Pulverizer", DynamicRecipes.PULVERIZER.getRecipes(), GtRecipes.pulverizer);
        if (DynamicRecipes.addAlloySmelterRecipes) serializeRecipes("Alloy Smelter", DynamicRecipes.ALLOY_SMELTER.getRecipes(), GtRecipes.alloySmelter);
        if (DynamicRecipes.addCannerRecipes) serializeRecipes("Canner", DynamicRecipes.CANNER.getRecipes(), GtRecipes.canner);
        if (DynamicRecipes.addLatheRecipes) serializeRecipes("Lathe", DynamicRecipes.LATHE.getRecipes(), GtRecipes.lathe);
        if (DynamicRecipes.addAssemblerRecipes) serializeRecipes("Assembler", DynamicRecipes.ASSEMBLER.getRecipes(), GtRecipes.assembler);
        if (DynamicRecipes.addBenderRecipes) serializeRecipes("Bender", DynamicRecipes.BENDER.getRecipes(), GtRecipes.bender);
        if (DynamicRecipes.addSawmillRecipes) serializeRecipes("Sawmill", DynamicRecipes.SAWMILL.getRecipes(), GtRecipes.sawmill);

        List<?> compressorRecipes = StreamSupport.stream(DynamicRecipes.COMPRESSOR.getRecipes().spliterator(), false)
                .peek(recipe -> {
                    IRecipeInput input = recipe.getInput();
                    input.getInputs().forEach(stack -> ModHandler.removeIC2Recipe(stack, (BasicMachineRecipeManager) Recipes.compressor));
                    Recipes.compressor.addRecipe(input, null, false, recipe.getOutput().toArray(new ItemStack[0]));
                })
                .collect(Collectors.toList());
        if (DynamicRecipes.addCompressorRecipes) serializeRecipes("Compressor", compressorRecipes);
    }

    public static <R> Optional<Collection<R>> parseConfig(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter) {
        return parseConfig(name, recipeClass, filter, recipesPath);
    }

    public static <R> Optional<Collection<R>> parseConfig(String name, Class<R> recipeClass, @Nullable Class<? extends RecipeFilter> filter, Path path) {
        try {
            ObjectMapper recipeMapper = mapper.copy();
            if (filter != null) recipeMapper.addMixIn(IGtMachineRecipe.class, filter);

            return Optional.ofNullable(recipeMapper.readValue(Files.newBufferedReader(path.resolve(name + ".yml")), mapper.getTypeFactory().constructCollectionType(List.class, recipeClass)));
        } catch (IOException e) {
            GregTechAPI.logger.error("Failed to parse " + name + " recipes: " + e.getMessage());
            return Optional.empty();
        }
    }

    private static <T extends IGtMachineRecipe<?, ?>> boolean parseDynamicRecipes(String name, Class<? extends T> recipeClass, @Nullable Class<? extends RecipeFilter> filter, IGtRecipeManager<?, ?, T> manager) {
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
                    .ifPresent(recipes -> registerRecipes("dynamic "+name.replace('_', ' '), recipes, manager));
            return false;
        }
        return true;
    }

    public static <R extends IGtMachineRecipe<?, ?>, M extends IGtRecipeManager<?, ?, R>> void serializeRecipes(String name, Collection<R> recipes, M recipeManager) {
        recipes.forEach(recipeManager::addRecipe);
        serializeRecipes(name, recipes);
    }

    public static <R extends IGtMachineRecipe<?, ?>, M extends IGtRecipeManager<?, ?, R>> void serializeRecipes(String name, Collection<?> recipes) {
        try {
            File file = dynamicRecipesDir.resolve(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name) +".yml").toFile();
            file.createNewFile();

            OutputStream output = Files.newOutputStream(file.toPath());
            output.write(("# Dynamic " + name + " recipes\n").getBytes(StandardCharsets.UTF_8));

            mapper.writeValue(output, recipes);
        } catch (IOException e) {
            GregTechAPI.logger.error("Failed to serialize " + name + " recipes: " + e.getMessage());
        }
    }

    private static <T extends IGtMachineRecipe<?, ?>> void registerRecipes(String name, Collection<? extends T> recipes, IGtRecipeManager<?, ?, T> manager) {
        int total = recipes.size();
        long successful = recipes.stream()
                .map(manager::addRecipe)
                .filter(Boolean::booleanValue)
                .count();
        GregTechAPI.logger.info("Loaded " + successful + " out of " + total + " " + name + " recipes");
    }

    private static <T extends IBasicMachineRecipe> void registerRecipes(String name, Collection<? extends T> recipes, IBasicMachineRecipeManager manager) {
        int total = recipes.size();
        long successful = recipes.stream()
                .map(recipe -> {
                    IRecipeInput input = recipe.getInput();
                    if (recipe.shouldOverwrite()) input.getInputs().forEach(stack -> ModHandler.removeIC2Recipe(stack, (BasicMachineRecipeManager) manager));
                    return manager.addRecipe(input, null, false, recipe.getOutput().toArray(new ItemStack[0]));
                })
                .filter(Boolean::booleanValue)
                .count();
        GregTechAPI.logger.info("Loaded " + successful + " out of " + total + " " + name + " recipes");
    }

    private static <T extends IFuel<?, ?>> void registerFuels(String name, Collection<? extends T> fuels, IFuelManager<T, ?, ?> manager) {
        int total = fuels.size();
        long successful = fuels.stream()
                .map(manager::addFuel)
                .filter(Boolean::booleanValue)
                .count();
        GregTechAPI.logger.info("Loaded " + successful + " out of " + total + " " + name + " fuels");
    }

    private static boolean shouldAddNewDynamicRecipes(String name) {
        Path dest = dynamicRecipesDir.resolve(name+".yml");
        return !dest.toFile().exists();
    }

    private static Path relocateConfig(Path source, String target) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(source);
            File configDir = new File(GregTechMod.configDir.toURI().getPath() + "/GregTech/"+target);
            configDir.mkdirs();
            for (Path path : stream) {
                GregTechAPI.logger.debug("Copying config: " + path.getFileName());
                File dest = new File(Paths.get(configDir.getPath(), path.getFileName().toString()).toUri());
                if (!dest.exists()) {
                    BufferedReader in = Files.newBufferedReader(path);
                    FileOutputStream out = new FileOutputStream(dest);
                    for (int i; (i = in.read()) != -1; ) {
                        out.write(i);
                    }

                    in.close();
                    out.close();
                }
            }
            return configDir.toPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void registerMatterAmplifiers() {
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
}