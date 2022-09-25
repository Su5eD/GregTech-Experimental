package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.util.JavaUtil;
import net.minecraft.item.ItemStack;

public class ItemCrafting extends ItemBase {
    private final int craftingDamage;

    public ItemCrafting(String name, String descriptionKey, int durability, int craftingDamage) {
        super(name, descriptionKey, durability);
        setMaxStackSize(1);
        setNoRepair();
        this.craftingDamage = craftingDamage;
    }

    public ItemStack getEmptyItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        stack = stack.copy();
        if (stack.attemptDamageItem(this.craftingDamage, JavaUtil.RANDOM, null)) return this.getEmptyItem();
        return stack;
    }
}
