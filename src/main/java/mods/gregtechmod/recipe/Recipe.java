package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IMachineRecipe;

public class Recipe<I, O> implements IMachineRecipe<I, O> {
    protected final I input;
    protected final O output;
    protected final int duration;
    protected final double energyCost;
    protected boolean invalid;

    protected Recipe(@JsonProperty(value = "input", required = true) I input,
                     @JsonProperty(value = "output", required = true) O output,
                     @JsonProperty(value = "duration", required = true) int duration,
                     @JsonProperty(value = "energyCost", required = true) double energyCost) {
        this.input = input;
        this.output = output;
        this.duration = duration;
        this.energyCost = energyCost;
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
    public int getDuration() {
        return this.duration;
    }

    @Override
    public double getEnergyCost() {
        return this.energyCost;
    }

    @Override
    public boolean isInvalid() {
        return this.invalid;
    }

    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
            .add("input", input)
            .add("output", output)
            .add("duration", duration)
            .add("energyCost", energyCost);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }
}
