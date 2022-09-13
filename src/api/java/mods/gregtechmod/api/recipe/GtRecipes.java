package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFusion;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtRecipes {
    public static IGtRecipeManagerCellular industrialCentrifuge;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> assembler;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IRecipePulverizer> pulverizer;
    public static IGtRecipeManagerSecondaryFluid<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> industrialGrinder;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeBlastFurnace> industrialBlastFurnace;
    public static IGtRecipeManagerCellular industrialElectrolyzer;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> canner;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeUniversal<List<IRecipeIngredient>>> alloySmelter;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> implosion;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> wiremill;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> bender;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> lathe;
    public static IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> vacuumFreezer;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> chemical;
    public static IGtRecipeManagerFusion fusion;
    public static IGtRecipeManagerSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>> industrialSawmill;
    public static IGtRecipeManagerCellular distillation;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipePrinter> printer;
}
