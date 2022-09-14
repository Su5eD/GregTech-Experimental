package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.api.item.ICustomDamageItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenExpansion("crafttweaker.item.IItemStack")
@ZenRegister
public class CustomDamageExtensions {
    
    @ZenMethod
    public static IItemStack anyCustomDamage(IItemStack stack) {
        return withCustomDamage(stack, OreDictionary.WILDCARD_VALUE);
    }

    @ZenMethod
    public static IItemStack withCustomDamage(IItemStack stack, int damage) {
        ItemStack result = CraftTweakerMC.getItemStack(stack).copy();
        Item item = result.getItem();
        if (item instanceof ICustomDamageItem) {
            ((ICustomDamageItem) item).setCustomDamage(result, damage);
        }
        else {
            throw new IllegalArgumentException(stack + " must be instance of ICustomDamageItem");
        }
        return CraftTweakerMC.getIItemStack(result);
    }
}
