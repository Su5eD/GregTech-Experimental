package mods.gregtechmod.compat.jei.wrapper;

import ic2.core.util.StackUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WrapperBasicMachineSingle extends WrapperBasicMachine<IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {

    public WrapperBasicMachineSingle(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        super(recipe);
    }

    @Override
    protected void setInputs(IIngredients ingredients) {
        IRecipeIngredient ingredient = this.recipe.getInput();
        int count = ingredient.getCount();
        List<ItemStack> inputs = ingredient.getMatchingInputs().stream()
                .map(stack -> StackUtil.copyWithSize(stack, count))
                .collect(Collectors.toList());

        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(inputs));
    }
}
