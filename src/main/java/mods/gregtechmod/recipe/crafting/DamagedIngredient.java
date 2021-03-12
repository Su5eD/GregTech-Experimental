package mods.gregtechmod.recipe.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

public class DamagedIngredient extends Ingredient {

    protected DamagedIngredient(ItemStack... stacks) {
        super(stacks);
    }

    public static DamagedIngredient fromItem(Item item) {
        return new DamagedIngredient(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        return super.apply(stack) && stack.isItemDamaged();
    }
}
