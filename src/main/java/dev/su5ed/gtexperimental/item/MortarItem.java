package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class MortarItem extends ResourceItem {
    private final int craftingDamage;
    private final Lazy<ItemStack> emptyItem;

    public MortarItem(int durability, int craftingDamage, Supplier<ItemStack> emptyItem) {
        super(new ExtendedItemProperties<>()
            .durability(durability)
            .setNoRepair()
            .autoDescription());
        this.craftingDamage = craftingDamage;
        this.emptyItem = Lazy.of(emptyItem);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.hurt(this.craftingDamage, GtUtil.RANDOM, null) ? this.emptyItem.get().copy() : copy;
    }
}
