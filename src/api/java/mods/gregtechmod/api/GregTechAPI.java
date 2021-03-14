package mods.gregtechmod.api;

import mods.gregtechmod.api.recipe.IRecipeFactory;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.util.SonictronSound;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GregTechAPI {

    public static final ArrayList<SonictronSound> SONICTRON_SOUNDS = new ArrayList<>();
    public static final Set<ItemStack> JACK_HAMMER_MINABLE_BLOCKS = new HashSet<>();
    private static final Set<ItemStack> SCREWDRIVERS = new HashSet<>();
    private static final Set<ItemStack> CROWBARS = new HashSet<>();

    public static Configuration dynamicConfig;
    public static IRecipeFactory recipeFactory;
    public static IRecipeIngredientFactory ingredientFactory;

    public static void registerScrewdriver(ItemStack stack) {
        if (!stack.isEmpty()) SCREWDRIVERS.add(stack);
    }

    public static Set<ItemStack> getScrewdrivers() {
        return Collections.unmodifiableSet(SCREWDRIVERS);
    }

    public static void registerCrowbar(ItemStack stack) {
        if (!stack.isEmpty()) CROWBARS.add(stack);
    }

    public static Set<ItemStack> getCrowbars() {
        return Collections.unmodifiableSet(CROWBARS);
    }

    public static boolean getDynamicConfig(String category, String name, boolean value) {
        return getDynamicConfig(category, name, value, null);
    }

    public static boolean getDynamicConfig(String category, String name, boolean value, String comment) {
        boolean ret = dynamicConfig.get(category, name, value).getBoolean();
        if (dynamicConfig.hasChanged()) dynamicConfig.save();
        return ret;
    }
}