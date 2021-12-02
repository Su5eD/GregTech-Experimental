package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ItemToolCrafting extends ItemToolBase {
    protected final int craftingDamage;

    public ItemToolCrafting(String name, int durability, float attackDamage, int craftingDamage, int damageOnHit) {
        this(name, () -> GtLocale.translateItemDescription(name), durability, attackDamage, ToolMaterial.WOOD, craftingDamage, damageOnHit);
    }

    public ItemToolCrafting(String name, String genericDescriptionKey, int durability, float attackDamage, int craftingDamage) {
        this(name, () -> GtLocale.translateGenericDescription(genericDescriptionKey), durability, attackDamage, ToolMaterial.WOOD, craftingDamage, 3);
    }

    public ItemToolCrafting(String name, String genericDescriptionKey, int durability, float attackDamage, ToolMaterial material, int craftingDamage, int damageOnHit) {
        this(name, () -> GtLocale.translateGenericDescription(genericDescriptionKey), durability, attackDamage, material, craftingDamage, damageOnHit);
    }

    public ItemToolCrafting(String name, Supplier<String> description, int durability, float attackDamage, ToolMaterial material, int craftingDamage, int damageOnHit) {
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
        if (stack.attemptDamageItem(this.craftingDamage, JavaUtil.RANDOM, null)) return getEmptyItem();
        return stack;
    }
}
