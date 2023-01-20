package dev.su5ed.gtexperimental.recipe.setup;

import dev.su5ed.gtexperimental.recipe.type.ItemRecipeOutputType;
import dev.su5ed.gtexperimental.recipe.type.RecipeOutputType;
import net.minecraft.world.item.ItemStack;

public final class ModRecipeOutputTypes {
    public static final RecipeOutputType<ItemStack> ITEM = new ItemRecipeOutputType();

    private ModRecipeOutputTypes() {}
}
