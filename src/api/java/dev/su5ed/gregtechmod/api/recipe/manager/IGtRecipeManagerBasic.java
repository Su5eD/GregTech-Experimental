package dev.su5ed.gregtechmod.api.recipe.manager;

import dev.su5ed.gregtechmod.api.recipe.IMachineRecipe;

public interface IGtRecipeManagerBasic<RI, I, R extends IMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R>, IRecipeProvider<RI, I, R> {
    
}
