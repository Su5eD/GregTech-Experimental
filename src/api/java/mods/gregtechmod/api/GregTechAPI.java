package mods.gregtechmod.api;

import com.google.common.collect.Sets;
import mods.gregtechmod.api.recipe.IRecipeFactory;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.util.SonictronSound;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;

public class GregTechAPI {
    public static Logger logger;
    public static final ArrayList<SonictronSound> sonictronSounds = new ArrayList<>();
    public static IRecipeFactory recipeFactory;
    public static IRecipeIngredientFactory ingredientFactory;
    public static final Set<Block> jackHammerMinableBlocks = Sets.newHashSet(Blocks.COBBLESTONE, Blocks.STONE, Blocks.SANDSTONE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.GLOWSTONE, Blocks.NETHER_BRICK, Blocks.END_STONE);
}