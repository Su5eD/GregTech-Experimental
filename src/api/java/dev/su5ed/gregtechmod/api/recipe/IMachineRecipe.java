package dev.su5ed.gregtechmod.api.recipe;

public interface IMachineRecipe<I, O> {
    O getOutput();

    I getInput();

    double getEnergyCost();

    int getDuration();

    boolean isInvalid();
}
