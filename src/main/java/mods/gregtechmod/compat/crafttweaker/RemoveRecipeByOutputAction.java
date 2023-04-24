package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.IAction;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class RemoveRecipeByOutputAction<R extends IMachineRecipe<?, O>, RM extends IGtRecipeManager<?, ?, R>, O> implements IAction {
    private final RM manager;
    private final Predicate<O> predicate;

    public RemoveRecipeByOutputAction(RM manager, Predicate<O> predicate) {
        this.manager = manager;
        this.predicate = predicate;
    }

    @Override
    public void apply() {
        List<R> toRemove = StreamEx.of(this.manager.getRecipes())
            .filter(recipe -> this.predicate.test(recipe.getOutput()))
            .toList();
        for (R recipe : toRemove) {
            this.manager.removeRecipe(recipe);
        }
    }

    @Override
    public String describe() {
        return String.format(Locale.ENGLISH, "Remove Recipe by output[%s] from %s", this.predicate, this.manager);
    }
}
