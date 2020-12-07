package mods.gregtechmod.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class RecipeType {

    public abstract static class Default extends RecipeType {
        @JsonIgnore
        public abstract double getEnergyCost();

        @JsonIgnore
        public abstract int getTime();
    }

    public abstract static class Time extends RecipeType {
        @JsonIgnore
        public abstract double getEnergyCost();
    }

    public abstract static class Energy extends RecipeType {
        @JsonIgnore
        public abstract int getTime();
    }
}
