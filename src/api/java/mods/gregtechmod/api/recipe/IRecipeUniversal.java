package mods.gregtechmod.api.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRecipeUniversal<RI> extends IMachineRecipe<RI, List<ItemStack>> {
    boolean isUniversal();
}
