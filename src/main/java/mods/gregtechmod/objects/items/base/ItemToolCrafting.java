package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.util.GtUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemToolCrafting extends ItemToolBase {
    protected final int craftingDamage;

    public ItemToolCrafting(String name, @Nullable String description, int durability, float attackDamage, int craftingDamage, int damageOnHit) {
        this(name, description, durability, attackDamage, ToolMaterial.WOOD, craftingDamage, damageOnHit);
    }

    public ItemToolCrafting(String name, @Nullable String description, int durability, float attackDamage, int craftingDamage) {
        this(name, description, durability, attackDamage, ToolMaterial.WOOD, craftingDamage, 3);
    }

    public ItemToolCrafting(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, int craftingDamage, int damageOnHit) {
        super(name, description, durability, attackDamage, material, damageOnHit);
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
        if (stack.attemptDamageItem(this.craftingDamage, GtUtil.RANDOM, null)) return getEmptyItem();
        return stack;
    }
}
