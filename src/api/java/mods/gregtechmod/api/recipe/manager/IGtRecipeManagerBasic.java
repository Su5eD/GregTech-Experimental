package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;

public interface IGtRecipeManagerBasic<RI, I, R extends IMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R>, IRecipeProvider<RI, I, R> {
    
}
