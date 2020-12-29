package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;

public class Recipe<I, O> implements IGtMachineRecipe<I, O> {
    protected final I input;
    protected final O output;
    protected final double energyCost;
    protected final int duration;
    protected boolean invalid;

    public Recipe(@JsonProperty(value = "input", required = true) I input,
                  @JsonProperty(value = "output", required = true) O output,
                  @JsonProperty(value = "energyCost", required = true) double energyCost,
                  @JsonProperty(value = "duration", required = true) int duration) {
        this.input = input;
        this.output = output;
        this.energyCost = energyCost;
        this.duration = duration;
    }

    @Override
    public O getOutput() {
        return this.output;
    }

    @Override
    public I getInput() {
        return this.input;
    }

    @Override
    public double getEnergyCost() {
        return this.energyCost;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    public boolean isInvalid() {
        return this.invalid;
    }
}
