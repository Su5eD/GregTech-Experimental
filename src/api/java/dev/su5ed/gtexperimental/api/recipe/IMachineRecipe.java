package dev.su5ed.gtexperimental.api.recipe;

public interface IMachineRecipe<I, O> {
    O getOutput();

    I getInput();

    double getEnergyCost();

    int getDuration();

    boolean isInvalid();
}
