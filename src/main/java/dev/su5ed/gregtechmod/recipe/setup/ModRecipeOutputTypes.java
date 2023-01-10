package dev.su5ed.gregtechmod.recipe.setup;

import dev.su5ed.gregtechmod.recipe.type.ItemRecipeOutputType;
import dev.su5ed.gregtechmod.recipe.type.RecipeOutputType;
import net.minecraft.world.item.ItemStack;

public final class ModRecipeOutputTypes {
    public static final RecipeOutputType<ItemStack> ITEM = new ItemRecipeOutputType();

    private ModRecipeOutputTypes() {}
}
