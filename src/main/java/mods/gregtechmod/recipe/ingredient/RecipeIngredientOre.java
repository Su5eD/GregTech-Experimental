package mods.gregtechmod.recipe.ingredient;

import net.minecraftforge.oredict.OreIngredient;

public class RecipeIngredientOre extends RecipeIngredientBase<OreIngredient> {

    public RecipeIngredientOre(String ore, int count) {
        super(new OreIngredient(ore), count);
    }
}
