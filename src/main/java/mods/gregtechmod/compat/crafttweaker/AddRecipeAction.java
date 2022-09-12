package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.IAction;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Locale;

public class AddRecipeAction<R extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>, RM extends IGtRecipeManager<IRecipeIngredient, ItemStack, R>> implements IAction {
    private final RM manager;
    private final R recipe;

    public AddRecipeAction(RM manager, R recipe) {
        this.manager = manager;
        this.recipe = recipe;
    }

    @Override
    public void apply() {
        this.manager.addRecipe(this.recipe);
    }

    @Override
    public String describe() {
        return String.format(Locale.ENGLISH, "Add Recipe[%s] to %s", this.recipe, this.manager);
    }
}
