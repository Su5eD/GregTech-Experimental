package mods.gregtechmod.api;

import mods.gregtechmod.api.recipe.IRecipeFactory;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.util.SonictronSound;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GregTechAPI {
    public static Logger logger;
    public static final ArrayList<SonictronSound> sonictronSounds = new ArrayList<>();
    public static IRecipeFactory recipeFactory;
    public static IRecipeIngredientFactory ingredientFactory;
    public static final Set<ItemStack> jackHammerMinableBlocks = new HashSet<>();
    public static Configuration dynamicConfig;
}