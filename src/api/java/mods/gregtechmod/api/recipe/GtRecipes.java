package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.*;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtRecipes {
    public static IGtRecipeManagerCellular industrialCentrifuge;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> assembler;
    public static IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipePulverizer> pulverizer;
    public static IGtRecipeManagerGrinder grinder;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeBlastFurnace> blastFurnace;
    public static IGtRecipeManagerCellular electrolyzer;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> canner;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeAlloySmelter> alloySmelter;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> implosion;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> wiremill;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> bender;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> lathe;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, ItemStack>> vacuumFreezer;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, ItemStack>> chemical;
    public static IGtRecipeManagerFusionFluid fusionFluid;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeFusion<IRecipeIngredient, ItemStack>> fusionSolid;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IRecipeSawmill> sawmill;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IRecipeCellular> distillation;
}
