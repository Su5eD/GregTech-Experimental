package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.IAction;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;

import java.util.Locale;

public class AddFuelAction<R extends IFuel<?>, RM extends IFuelManager<R, ?>> implements IAction {
    private final RM manager;
    private final R fuel;

    public AddFuelAction(RM manager, R fuel) {
        this.manager = manager;
        this.fuel = fuel;
    }

    @Override
    public void apply() {
        this.manager.addFuel(this.fuel);
    }

    @Override
    public String describe() {
        return String.format(Locale.ENGLISH, "Add Fuel[%s] to %s", this.fuel, this.manager);
    }
}