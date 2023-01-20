package dev.su5ed.gtexperimental.api.recipe;

import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.manager.IGtRecipeManagerBasic;
import dev.su5ed.gtexperimental.api.recipe.manager.IGtRecipeManagerCellular;
import dev.su5ed.gtexperimental.api.recipe.manager.IGtRecipeManagerFusionFluid;
import dev.su5ed.gtexperimental.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import net.minecraft.world.item.ItemStack;

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
    public static IGtRecipeManagerFusionFluid fusionFluid;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeFusion<IRecipeIngredient, ItemStack>> fusionSolid;
    public static IGtRecipeManagerSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>> industrialSawmill;
    public static IGtRecipeManagerCellular distillation;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipePrinter> printer;
}
