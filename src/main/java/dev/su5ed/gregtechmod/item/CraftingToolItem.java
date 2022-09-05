package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.util.JavaUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CraftingToolItem extends ToolItem {
    private final int craftingDamage;
    @Nullable
    private final Item emptyItem;

    public CraftingToolItem(ToolItemProperties<?> properties, int craftingDamage, @Nullable Item emptyItem) {
        super(properties);

        this.craftingDamage = craftingDamage;
        this.emptyItem = emptyItem;
    }

    public ItemStack getEmptyItem() {
        return this.emptyItem != null ? new ItemStack(this.emptyItem) : ItemStack.EMPTY;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.hurt(this.craftingDamage, JavaUtil.RANDOM, null) ? getEmptyItem() : copy;
    }
}
