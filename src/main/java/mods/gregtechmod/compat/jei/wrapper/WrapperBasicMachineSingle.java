package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collections;
import java.util.List;

public class WrapperBasicMachineSingle<R extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> extends WrapperBasicMachine<R> {

    public WrapperBasicMachineSingle(R recipe) {
        super(recipe, -5);
    }

    public WrapperBasicMachineSingle(R recipe, int yOffset) {
        super(recipe, yOffset);
    }

    @Override
    protected void setInputs(IIngredients ingredients) {
        IRecipeIngredient ingredient = this.recipe.getInput();
        int count = ingredient.getCount();
        
        List<ItemStack> inputs = ingredient.stream()
            .map(stack -> ItemHandlerHelper.copyStackWithSize(stack, count))
            .toList();

        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(inputs));
    }
}
