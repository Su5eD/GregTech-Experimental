package mods.gregtechmod.common.core;


import net.minecraft.item.Item;


public class CommonProxy {
    public void preInit() {}

    public void registerModel(Item item, int metadata) {
        registerModel(item, metadata, null, null, null);
    }

    public void registerModel(Item item, int metadata, String itemName, String prefix, String folder) {}
}