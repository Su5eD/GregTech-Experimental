package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.jei.JEIUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WrapperBasicMachineMulti<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends WrapperBasicMachine<R> {

    public WrapperBasicMachineMulti(R recipe) {
        super(recipe, -5);
    }

    @Override
    protected void setInputs(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, JEIUtils.getMultiInputs(recipe));
    }
}
