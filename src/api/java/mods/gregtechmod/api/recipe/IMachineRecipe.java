package mods.gregtechmod.api.recipe;

public interface IMachineRecipe<I, O> {
    O getOutput();

    I getInput();

    double getEnergyCost();

    int getDuration();

    boolean isInvalid();
}