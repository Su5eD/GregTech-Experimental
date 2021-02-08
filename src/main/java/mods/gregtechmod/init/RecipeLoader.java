package mods.gregtechmod.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.*;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.manager.*;
import mods.gregtechmod.recipe.util.*;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RecipeLoader {
    private static Path configPath = null;
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
            .registerModule(new SimpleModule()
                    .addDeserializer(ItemStack.class, ItemStackDeserializer.INSTANCE)
                    .addDeserializer(IRecipeIngredient.class, RecipeIngredientDeserializer.INSTANCE)
                    .addDeserializer(IRecipeIngredientFluid.class, RecipeIngredientFluidDeserializer.INSTANCE)
                    .addDeserializer(IRecipeInput.class, RecipeInputDeserializer.INSTANCE));

    public static void load() {
        GregTechAPI.logger.info("Loading machine recipes");

        GregTechAPI.recipeFactory = new RecipeFactory();
        GregTechAPI.ingredientFactory = new RecipeIngredientFactory();

        try {
            File modFile = Loader.instance().activeModContainer().getSource();
            FileSystem fs = FileSystems.newFileSystem(modFile.toPath(), null);

            Path recipesPath = fs.getPath("assets", Reference.MODID, "machine_recipes");
            Path gtConfig = relocateRecipeConfig(recipesPath);
            if (gtConfig == null) {
                GregTechAPI.logger.error("Couldn't find the recipes config directory. Loading default recipes...");
                configPath = recipesPath;
            } else configPath = gtConfig;

            GtRecipes.industrial_centrifuge = new RecipeManagerCellular();
            parseRecipes("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class)
                    .ifPresent(recipes -> registerRecipes("industrial_centrifuge", recipes, GtRecipes.industrial_centrifuge));
            GtRecipes.assembler = new RecipeManagerMultiInput<>();
            parseRecipes("assembler", RecipeDualInput.class, null)
                    .ifPresent(recipes -> registerRecipes("assembler", recipes, GtRecipes.assembler));

            GtRecipes.pulverizer = new RecipeManagerPulverizer();
            parseRecipes("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class)
                    .ifPresent(recipes -> registerRecipes("pulverizer", recipes, GtRecipes.pulverizer));

            GtRecipes.grinder = new RecipeManagerGrinder();
            parseRecipes("grinder", RecipeGrinder.class, RecipeFilter.Energy.class)
                    .ifPresent(recipes -> registerRecipes("grinder", recipes, GtRecipes.grinder));

            GtRecipes.blastFurnace = new RecipeManagerBlastFurnace();
            parseRecipes("blast_furnace", RecipeBlastFurnace.class, RecipeFilter.Energy.class)
                    .ifPresent(recipes -> registerRecipes("blast_furnace", recipes, GtRecipes.blastFurnace));

            GtRecipes.electrolyzer = new RecipeManagerCellular();
            parseRecipes("electrolyzer", RecipeElectrolyzer.class, null)
                    .ifPresent(recipes -> registerRecipes("electrolyzer", recipes, GtRecipes.electrolyzer));

            GtRecipes.canner = new RecipeManagerMultiInput<>();
            parseRecipes("canner", RecipeCanner.class, null)
                    .ifPresent(recipes -> registerRecipes("canner", recipes, GtRecipes.canner));

            GtRecipes.alloy_smelter = new RecipeManagerMultiInput<>();
            parseRecipes("alloy_smelter", RecipeDualInput.class, null)
                    .ifPresent(recipes -> registerRecipes("alloy_smelter", recipes, GtRecipes.alloy_smelter));

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

            // IC2 Recipes
            parseRecipes("compressor", BasicMachineRecipe.class, null)
                    .ifPresent(recipes -> registerRecipes("compresor", recipes, Recipes.compressor));

            registerDynamicRecipes();
            registerMatterAmplifiers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <R, T extends RecipeFilter> Optional<Collection<R>> parseRecipes(String name, Class<R> recipeClass, @Nullable Class<T> recipeType) {
        try {
            ObjectMapper recipeMapper = mapper.copy();
            if (recipeType != null) recipeMapper.addMixIn(IGtMachineRecipe.class, recipeType);

            return Optional.ofNullable(recipeMapper.readValue(Files.newBufferedReader(configPath.resolve(name + ".yml")), recipeMapper.getTypeFactory().constructCollectionType(List.class, recipeClass)));
        } catch (IOException e) {
            GregTechAPI.logger.error("Failed to parse recipes for " + name + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    private static Path relocateRecipeConfig(Path source) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(source);
            File configDir = new File(GregTechMod.configDir.toURI().getPath() + "/GregTech/machine recipes");
            configDir.mkdirs();
            for (Path path : stream) {
                GregTechAPI.logger.debug("Copying recipe config: " + path.getFileName());
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
                .map(recipe -> manager.addRecipe(recipe.getInput(), null, true, recipe.getOutput()))
                .filter(Boolean::booleanValue)
                .count();
        GregTechAPI.logger.info("Loaded " + successful + " out of " + total + " " + name + " recipes");
    }

    private static void registerDynamicRecipes() {
        ItemStack copper = IC2Items.getItem("ingot", "copper");
        int bronze = ModHandler.getCraftingResult(copper, copper, ItemStack.EMPTY, copper, IC2Items.getItem("ingot", "tin")).getCount();
        GtRecipes.industrial_centrifuge.addRecipe(
                RecipeCentrifuge.create(RecipeIngredientOre.create("dustBronze", bronze < 3 ? 1 : bronze / 2),
                        Arrays.asList(StackUtil.copyWithSize(IC2Items.getItem("dust", "small_copper"), 6),
                                StackUtil.copyWithSize(IC2Items.getItem("dust", "small_tin"), 2)),
                        0,
                        1500));
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