package mods.gregtechmod.recipe.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("unused")
public abstract class RecipeFilter {

    public abstract static class Default extends RecipeFilter {
        @JsonIgnore
        public abstract double getEnergyCost();

        @JsonIgnore
        public abstract int getDuration();
    }

    public abstract static class Energy extends RecipeFilter {
        @JsonIgnore
        public abstract double getEnergyCost();
    }

    public abstract static class Time extends RecipeFilter {
        @JsonIgnore
        public abstract int getDuration();
    }
}
