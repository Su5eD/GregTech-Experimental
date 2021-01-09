package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.objects.items.base.ItemToolCrafting;

public class ItemFile extends ItemToolCrafting {

    public ItemFile(String material, int durability, float attackDamage) {
        super("file_"+material, "For sharpening or rounding Edges", durability, attackDamage, 1);
    }
}
