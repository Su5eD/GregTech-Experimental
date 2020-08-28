package mods.gregtechmod.common.core;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import org.apache.commons.lang3.ArrayUtils;


public class CommonProxy {
    public void registerModel(Item item, int metadata) {
        registerModel(item, metadata, null, null, null);
    }

    public void registerModel(Item item, int metadata, String itemName, String prefix, String folder) {}

    public KeyBinding getModeKeyBinding() {
        KeyBinding[] keybinding = ArrayUtils.clone(Minecraft.getMinecraft().gameSettings.keyBindings);

        for (KeyBinding keybind : keybinding) {
             String category = keybind.getKeyCategory();
             String desc = keybind.getKeyDescription();
             if (category.equals("IC2") && desc.toLowerCase().contains("mode")) return keybind;
        }
        return null;
    }
}