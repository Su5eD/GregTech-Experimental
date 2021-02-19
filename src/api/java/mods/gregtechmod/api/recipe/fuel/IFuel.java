package mods.gregtechmod.api.recipe.fuel;

public interface IFuel<T, I> {
    T getFuel();

    double getEnergy();

    boolean apply(I fuel);
}
