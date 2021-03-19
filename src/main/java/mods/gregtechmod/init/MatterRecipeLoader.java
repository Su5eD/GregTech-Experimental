package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import ic2.api.recipe.ICraftingRecipeManager;
import ic2.core.block.machine.tileentity.TileEntityAssemblyBench;
import ic2.core.recipe.AdvRecipe;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatterRecipeLoader {
    private static final ICraftingRecipeManager.AttributeContainer ATTRIBUTES = new ICraftingRecipeManager.AttributeContainer(true, false, true);

    public static void init() {
        addMatterRecipe("gemRuby", 2, " UU", "UUU", "UU ");
        addMatterRecipe("gemSapphire", 2, "UU ", "UUU", " UU");
        addMatterRecipe("gemGreenSapphire", 2, " UU", "UUU", " UU");
        addMatterRecipe("gemOlivine", 2, "UU ", "UUU", "UU ");
        addMatterRecipe("dustZinc", 10, "   ", "U U", " U ");
        addMatterRecipe("dustNickel", 10, " U ", "U U", "   ");
        addMatterRecipe("dustSilver", 14, " U ", "UUU", "UUU");
        addMatterRecipe("dustPlatinum", 1, "  U", "UUU", "UUU");
        addMatterRecipe("dustTungsten", 6, "U  ", "UUU", "UUU");
        addMatterRecipe("dustSmallOsmium", 1, "U U", "UUU", "U U");
        addMatterRecipe("dustTitanium", 2, "UUU", " U ", " U ");
        addMatterRecipe("dustAluminium", 16, " U ", " U ", "UUU");
        addMatterRecipe("dustElectrotine", 12, "UUU", " U ", "   ");
        addMatterRecipe("blazeRod", new ItemStack(Items.BLAZE_ROD, 4), "U U", "UU ", "U U");
        addMatterRecipe("leather", new ItemStack(Items.LEATHER, 32), "U U", " U ", "UUU");
        addMatterRecipe("string", new ItemStack(Items.STRING, 32), "U U", "   ", "U  ");
        addMatterRecipe("obsidian", new ItemStack(Blocks.OBSIDIAN, 12), "U U", "U U", "   ");
        addMatterRecipe("woodSpruce", new ItemStack(Blocks.LOG, 8, 1), "U  ", "   ", "   ");
        addMatterRecipe("woodBirch", new ItemStack(Blocks.LOG, 8, 2), "  U", "   ", "   ");
        addMatterRecipe("woodJungle", new ItemStack(Blocks.LOG, 8, 3), "   ", "U  ", "   ");
        addMatterRecipe("woodAcacia", new ItemStack(Blocks.LOG2, 8), "   ", "  U", "   ");
        addMatterRecipe("woodDarkOak", new ItemStack(Blocks.LOG2, 8, 1), "   ", "   ", "U  ");

        ItemStack matter = IC2Items.getItem("misc_resource", "matter");
        ItemStack[] input = new ItemStack[] { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, matter, matter, matter };
        ModHandler.removeCraftingRecipeFromInputs(input);
        IRecipe recipe = ModHandler.getCraftingRecipe(TileEntityAssemblyBench.RECIPES, input);
        if (recipe != null) TileEntityAssemblyBench.RECIPES.remove(recipe);

        ItemStack dustPlutonium = new ItemStack(BlockItems.Dust.PLUTONIUM.getInstance());
        List<Object> pattern;
        if (GregTechAPI.getDynamicConfig("gregtech_recipes", "matterfabricator", true) &&
                GregTechConfig.DISABLED_RECIPES.massFabricator &&
                GregTechConfig.MACHINES.matterFabricationRate >= 10000000) {
            pattern = Arrays.asList("U", "R", 'R', "dustUranium");
        } else pattern = Arrays.asList("UUU", "URU", "UUU", 'R', "dustUranium");

        List<Object> patternWithMatter = new ArrayList<>(pattern);
        patternWithMatter.add('U');
        patternWithMatter.add("craftingUUMatter");
        if (addMatterRecipe("dustPlutonium", new AdvRecipe(dustPlutonium, patternWithMatter.toArray()))) {
            addMatterCraftingRecipe("dustPlutonium", dustPlutonium, pattern.toArray());
        }
    }

    private static void addMatterRecipe(String name, int count, Object... pattern) {
        ItemStack output = OreDictUnificator.getFirstOre(name, count);
        addMatterRecipe(name, output, pattern);
    }

    private static void addMatterRecipe(String name, ItemStack output, Object... pattern) {
        if (output.isEmpty()) return;
        if (addMatterRecipe(name, TileEntityAssemblyBench.UuRecipe.create(output, pattern))) addMatterCraftingRecipe(name, output, pattern);
    }

    private static boolean addMatterRecipe(String name, IRecipe recipe) {
        if (!GregTechAPI.getDynamicConfig("uumrecipe", name, true)) return false;
        return TileEntityAssemblyBench.RECIPES.add(recipe);
    }

    private static void addMatterCraftingRecipe(String name, ItemStack output, Object... pattern) {
        List<Object> args = new ArrayList<>(Arrays.asList(pattern));
        args.add('U');
        args.add("craftingUUMatter");
        args.add(ATTRIBUTES);
        AdvRecipe recipe = new AdvRecipe(output, args.toArray());
        recipe.setRegistryName(new ResourceLocation(Reference.MODID, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name+"FromMatter")));
        if (recipe.masksMirrored != null) Arrays.fill(recipe.masksMirrored, -1);

        ForgeRegistries.RECIPES.register(recipe);
    }
}
