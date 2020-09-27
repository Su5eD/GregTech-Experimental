package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.objects.items.base.ItemCrafting;

public class ItemMortar extends ItemCrafting {

    public ItemMortar(String material, int durability) {
        super("mortar_"+material, "Used to turn ingots into dust", durability, 1);
        setFolder("component");
    }
}
