package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.objects.items.base.ItemToolCrafting;

public class ItemFile extends ItemToolCrafting {

    public ItemFile(String material, int durability, float attackDamage) {
        super("file_"+material, "file", durability, attackDamage, 1);
    }
}
