package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.IAction;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;

import java.util.Locale;

public class RemoveRecipeAction<R extends IMachineRecipe<?, ?>, RM extends IGtRecipeManager<?, ?, R>> implements IAction {
    private final RM manager;
    private final R recipe;

    public RemoveRecipeAction(RM manager, R recipe) {
        this.manager = manager;
        this.recipe = recipe;
    }

    @Override
    public void apply() {
        this.manager.removeRecipe(this.recipe);
    }

    @Override
    public String describe() {
        return String.format(Locale.ENGLISH, "Remove Recipe[%s] from %s", this.recipe, this.manager);
    }
}
