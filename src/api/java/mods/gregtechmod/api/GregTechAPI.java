package mods.gregtechmod.api;

import mods.gregtechmod.api.recipe.IRecipeFactory;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.util.SonictronSound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class GregTechAPI {
    public static final ArrayList<SonictronSound> SONICTRON_SOUNDS = new ArrayList<>();
    public static final Set<ItemStack> JACK_HAMMER_MINABLE_BLOCKS = new HashSet<>();
    
    private static final Set<ItemStack> WRENCHES = new HashSet<>();
    private static final Set<ItemStack> SCREWDRIVERS = new HashSet<>();
    private static final Set<ItemStack> SOFT_HAMMERS = new HashSet<>();
    private static final Set<ItemStack> HARD_HAMMERS = new HashSet<>();
    private static final Set<ItemStack> CROWBARS = new HashSet<>();

    public static Configuration dynamicConfig;
    public static IRecipeFactory recipeFactory;
    public static IRecipeIngredientFactory ingredientFactory;

    public static boolean isClassic;
    
    public static void registerWrench(Item item) {
        registerTool(item, WRENCHES);
    }
    
    public static void registerWrench(ItemStack stack) {
        registerTool(stack, WRENCHES);
    }
    
    public static Collection<ItemStack> getWrenches() {
        return Collections.unmodifiableSet(WRENCHES);
    }
    
    public static void registerScrewdriver(Item item) {
        registerTool(item, SCREWDRIVERS);
    }

    public static void registerScrewdriver(ItemStack stack) {
        registerTool(stack, SCREWDRIVERS);
    }

    public static Collection<ItemStack> getScrewdrivers() {
        return Collections.unmodifiableSet(SCREWDRIVERS);
    }
    
    public static void registerSoftHammer(Item item) {
        registerTool(item, SOFT_HAMMERS);
    }
    
    public static void registerSoftHammer(ItemStack stack) {
        registerTool(stack, SOFT_HAMMERS);
    }
        
    public static Collection<ItemStack> getSoftHammers() {
        return Collections.unmodifiableSet(SOFT_HAMMERS);
    }
    
    public static void registerHardHammer(Item item) {
        registerTool(item, HARD_HAMMERS);
    }
    
    public static void registerHardHammer(ItemStack stack) {
        registerTool(stack, HARD_HAMMERS);
    }
            
    public static Collection<ItemStack> getHardHammers() {
        return Collections.unmodifiableSet(HARD_HAMMERS);
    }
    
    public static void registerCrowbar(Item item) {
        registerTool(item, CROWBARS);
    }

    public static void registerCrowbar(ItemStack stack) {
        registerTool(stack, CROWBARS);
    }

    public static Collection<ItemStack> getCrowbars() {
        return Collections.unmodifiableSet(CROWBARS);
    }
    
    private static void registerTool(Item item, Set<ItemStack> registry) {
        registerTool(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), registry);
    }
    
    private static void registerTool(ItemStack stack, Set<ItemStack> registry) {
        if (!stack.isEmpty()) registry.add(stack);
    }

    public static boolean getDynamicConfig(String category, String name, boolean value) {
        boolean ret = dynamicConfig.get(category, name, value).getBoolean();
        if (dynamicConfig.hasChanged()) dynamicConfig.save();
        return ret;
    }
}
