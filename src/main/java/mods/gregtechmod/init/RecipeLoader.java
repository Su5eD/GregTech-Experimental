package mods.gregtechmod.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.*;
import mods.gregtechmod.recipe.manager.*;
import mods.gregtechmod.util.ItemStackDeserializer;
import mods.gregtechmod.util.RecipeFilter;
import mods.gregtechmod.util.RecipeIngredientDeserializer;
import mods.gregtechmod.util.RecipeIngredientFluidDeserializer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RecipeLoader {
    private static Path configPath = null;
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
            .registerModule(new SimpleModule()
                    .addDeserializer(ItemStack.class, ItemStackDeserializer.INSTANCE)
                    .addDeserializer(IRecipeIngredient.class, RecipeIngredientDeserializer.INSTANCE)
                    .addDeserializer(IRecipeIngredientFluid.class, RecipeIngredientFluidDeserializer.INSTANCE));

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

            GtRecipes.industrial_centrifuge = new RecipeManagerCentrifuge();
            RecipeLoader.parseRecipe("industrial_centrifuge", RecipeCentrifuge.class, RecipeFilter.Energy.class)
                    .ifPresent(recipes -> registerRecipes("industrial_centrifuge", recipes, GtRecipes.industrial_centrifuge));
            GtRecipes.assembler = new RecipeManagerAssembler();
            RecipeLoader.parseRecipe("assembler", RecipeAssembler.class, null)
                    .ifPresent(recipes -> registerRecipes("assembler", recipes, GtRecipes.assembler));

            GtRecipes.pulverizer = new RecipeManagerPulverizer();
            parseRecipe("pulverizer", RecipePulverizer.class, RecipeFilter.Default.class)
                    .ifPresent(recipes -> registerRecipes("pulverizer", recipes, GtRecipes.pulverizer));

            GtRecipes.grinder = new RecipeManagerGrinder();
            parseRecipe("grinder", RecipeGrinder.class, RecipeFilter.Energy.class)
                    .ifPresent(recipes -> registerRecipes("grinder", recipes, GtRecipes.grinder));

            GtRecipes.blastFurnace = new RecipeManagerBlastFurnace();
            parseRecipe("blast_furnace", RecipeBlastFurnace.class, RecipeFilter.Energy.class)
                    .ifPresent(recipes -> registerRecipes("blast_furnace", recipes, GtRecipes.blastFurnace));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <R extends IGtMachineRecipe<?, ?>, T extends RecipeFilter> Optional<Collection<R>> parseRecipe(String name, Class<R> recipeClass, @Nullable Class<T> recipeType) {
        try {
            ObjectMapper recipeMapper = mapper.copy();
            if (recipeType != null) recipeMapper.addMixIn(IGtMachineRecipe.class, recipeType);

            return Optional.ofNullable(recipeMapper.readValue(Files.newBufferedReader(configPath.resolve(name+".yml")), recipeMapper.getTypeFactory().constructCollectionType(List.class, recipeClass)));
        } catch (IOException e) {
            GregTechAPI.logger.error("Failed to parse recipes for "+name+": "+e.getMessage());
            return Optional.empty();
        }
    }

    private static Path relocateRecipeConfig(Path source) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(source);
            File configDir = new File(GregTechMod.configDir.toURI().getPath()+"/GregTech/machine recipes");
            configDir.mkdirs();
            for(Path path : stream) {
                GregTechAPI.logger.debug("Copying recipe config: "+path.getFileName());
                File dest = new File(Paths.get(configDir.getPath(), path.getFileName().toString()).toUri());
                if(!dest.exists()) {
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
        GregTechAPI.logger.info("Loaded "+successful+" out of "+total+" recipes for "+name);
    }
}