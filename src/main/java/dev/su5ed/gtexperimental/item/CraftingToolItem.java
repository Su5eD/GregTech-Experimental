package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CraftingToolItem extends ToolItem {
    protected final int craftingDamage;
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
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.hurt(this.craftingDamage, GtUtil.RANDOM, null) ? getEmptyItem() : copy;
    }
}
