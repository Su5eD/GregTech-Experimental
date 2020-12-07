package mods.gregtechmod.api.recipe;

public interface IGtMachineRecipe<I, O> {
    O getOutput();

    I getInput();

    double getEnergyCost();

    int getDuration();
}