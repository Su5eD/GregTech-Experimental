package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.util.GtUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemCraftingTool extends ItemToolBase {
    private final int craftingDamage;

    public ItemCraftingTool(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, int craftingDamage) {
        super(name, description, durability, attackDamage, material);
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
