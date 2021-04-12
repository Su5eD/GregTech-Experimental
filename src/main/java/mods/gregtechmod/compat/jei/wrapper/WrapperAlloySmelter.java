package mods.gregtechmod.compat.jei.wrapper;

import ic2.core.util.StackUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.api.recipe.IRecipeAlloySmelter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class WrapperAlloySmelter extends WrapperBasicMachine<IRecipeAlloySmelter> {

    public WrapperAlloySmelter(IRecipeAlloySmelter recipe) {
        super(recipe);
    }

    @Override
    protected void setInputs(IIngredients ingredients) {
        List<IRecipeIngredient> inputs = this.recipe.getInput();
        List<List<ItemStack>> stacks = inputs.stream()
                .map(input -> {
                    int count = input.getCount();
                    return input.getMatchingInputs().stream()
                            .map(stack -> StackUtil.copyWithSize(stack, count))
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());

        ingredients.setInputLists(VanillaTypes.ITEM, stacks);
    }
}
