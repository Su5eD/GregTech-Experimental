package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.objects.items.base.ItemCrafting;
import net.minecraft.item.ItemStack;

public class ItemMortar extends ItemCrafting {
    private final ItemStack emptyItem;

    public ItemMortar(String material, int durability, ItemStack emptyItem) {
        super("mortar_" + material, "mortar", durability, 1);
        this.emptyItem = emptyItem;
    }

    @Override
    public ItemStack getEmptyItem() {
        return this.emptyItem;
    }
}
