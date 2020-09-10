package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.util.GtUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemCrafting extends ItemBase {
    private final int craftingDamage;

    public ItemCrafting(String name, @Nullable String description, int durability, int craftingDamage) {
        super(name, description);
        setMaxDamage(durability - 1);
        setMaxStackSize(1);
        setNoRepair();
        this.craftingDamage = craftingDamage;
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        stack = stack.copy();
        if (stack.attemptDamageItem(this.craftingDamage, GtUtil.RANDOM, null)) return ItemStack.EMPTY;
        return stack;
    }
}
