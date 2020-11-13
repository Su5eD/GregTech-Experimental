package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.util.GtUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemCraftingTool extends ItemToolBase {
    protected final int craftingDamage;

    public ItemCraftingTool(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, int craftingDamage) {
        this(name, description, durability, attackDamage, material, craftingDamage, 3);
    }

    public ItemCraftingTool(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, int craftingDamage, int damageOnHit) {
        super(name, description, durability, attackDamage, material, damageOnHit);
        this.craftingDamage = craftingDamage;
    }

    public ItemStack getEmptyItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        stack = stack.copy();
        if (stack.attemptDamageItem(this.craftingDamage, GtUtil.RANDOM, null)) return getEmptyItem();
        return stack;
    }
}
