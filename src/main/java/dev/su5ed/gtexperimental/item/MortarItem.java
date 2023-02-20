package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class MortarItem extends ResourceItem {
    private final int craftingDamage;
    private final ItemLike emptyItem;

    public MortarItem(int durability, int craftingDamage, ItemLike emptyItem) {
        super(new ExtendedItemProperties<>()
            .durability(durability)
            .setNoRepair()
            .autoDescription());
        this.craftingDamage = craftingDamage;
        this.emptyItem = emptyItem;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.hurt(this.craftingDamage, GtUtil.RANDOM, null) ? new ItemStack(this.emptyItem) : copy;
    }
}
