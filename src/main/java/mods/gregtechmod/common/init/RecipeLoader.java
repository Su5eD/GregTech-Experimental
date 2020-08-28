package mods.gregtechmod.common.init;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.core.init.Rezepte;
import ic2.core.util.Config;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.ReflectionUtil;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

import static ic2.core.init.Rezepte.getConfigFile;

public class RecipeLoader {
    private static final Queue<Runnable> PENDING_RECIPES = new ArrayDeque<>();

    private static final Method WHITESPACE = ReflectionUtil.getMethod(Rezepte.class, new String[] { "splitWhitespace" }, String.class);

    private static final Marker RECIPE = MarkerManager.getMarker("recipe");

    static void loadRecipes() {
        //TODO: Replace with for loop + get an interface for detecting recipes

        Config gtcentrifuge = new Config("industrial centrifuge recipes");
        try {
            gtcentrifuge.load(getConfigFile("gtcentrifuge"));
        } catch (Exception e) {
            GregtechMod.LOGGER.warn(LogCategory.Recipe, e, "Failed to load recipe.");
        }
        loadMachineRecipes(gtcentrifuge, Recipes.gtcentrifuge, MachineType.TimeBased);
    }
    private static void loadMachineRecipes(Config config, IMachineRecipeManager<IRecipeInput, Collection<ItemStack>, ?> machine, MachineType type) {
        int amount = 0;
        int successful = 0;

        for (Iterator<Config.Value> it = config.valueIterator(); it.hasNext(); amount++) {
            Config.Value value = it.next();
            if (loadMachineRecipe(value, machine, type, false)) successful++;
        }

        GregtechMod.LOGGER.log(LogCategory.Recipe, Level.INFO, "Successfully loaded " + successful + " from " + amount + " recipes for " + config.name);
    }

    private static boolean loadMachineRecipe(Config.Value value, IMachineRecipeManager<IRecipeInput, Collection<ItemStack>, ?> machine, MachineType type, boolean lastAttempt) {
        IRecipeInput input;
        List<ItemStack> outputs = new ArrayList<>();
        NBTTagCompound metadata = new NBTTagCompound();
        try {
            input = ConfigUtil.asRecipeInputWithAmount(value.name);
        } catch (ParseException e) {
            throw new Config.ParseException("invalid key", value, e);
        }
        if (input == null) {
            if (lastAttempt) {
                GregtechMod.LOGGER.warn(LogCategory.Recipe, new Config.ParseException("invalid input: " + value.name, value), "Skipping recipe due to unresolvable input %s.", value.name);
            } else {
                PENDING_RECIPES.add(() -> loadMachineRecipe(value, machine, type, true));
            }
            return false;
        }
        try {
            for (String part : splitWhitespace(value.getString())) {
                if (part.startsWith("@")) {
                    if (part.startsWith("@duration:") && type == MachineType.TimeBased || type == MachineType.TimeAndEUt) {
                        metadata.setInteger("duration", Integer.parseInt(part.substring(10)));
                        continue;
                    }
                    /*if (part.startsWith("@MaxEU/t:") && type == MachineType.TimeAndEUt) {
                        metadata.setInteger("MaxEU/t", Integer.parseInt(part.substring(6)));
                        continue;
                    }*/
                    throw new Config.ParseException("invalid attribute: " + part, value);
                }
                ItemStack cOutput = ConfigUtil.asStackWithAmount(part);
                if (cOutput == null) {
                    if (lastAttempt) {
                        GregtechMod.LOGGER.warn(LogCategory.Recipe, new Config.ParseException("invalid output specified: " + part, value), "Skipping recipe using %s due to unresolvable output %s.", new Object[] { value.name, part });
                    } else {
                        PENDING_RECIPES.add(() -> loadMachineRecipe(value, machine, type, true));
                    }
                    return false;
                }
                outputs.add(cOutput);
            }
            if (!type.tagsRequired.isEmpty() && (metadata.isEmpty() || !type.hasRequiredTags(metadata))) {
                GregtechMod.LOGGER.warn(LogCategory.Recipe, "Could not add machine recipe: " + value.name + " missing tag.");
                return false;
            }
            if (metadata.isEmpty())
                metadata = null;
            if (machine.addRecipe(input, outputs, metadata, false))
                return true;
            throw new Exception("Conflicting recipe");
        } catch (ic2.core.util.Config.ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new Config.ParseException("generic parse error", value, e);
        }
    }

    private static List<String> splitWhitespace(String text) {
        try {
            return (List<String>)WHITESPACE.invoke(null, text);
        } catch (Exception e) {
            throw new RuntimeException("Error reflecting whitespace", e);
        }
    }

    private enum MachineType {
        Normal(),
        TimeBased("duration"),
        TimeAndEUt("duration", "MaxEU/t");

        private final Set<String> tagsRequired;

        MachineType(String... tagsRequired) {
            this.tagsRequired = new HashSet<>(Arrays.asList(ArrayUtils.nullToEmpty(tagsRequired)));
        }

        private boolean hasRequiredTags(NBTTagCompound metadata) {
            for (String key : this.tagsRequired) {
                if (!metadata.hasKey(key))
                    return false;
            }
            return true;
        }
    }
    public static void registerRecipes() {
        loadRecipes();
    }
}