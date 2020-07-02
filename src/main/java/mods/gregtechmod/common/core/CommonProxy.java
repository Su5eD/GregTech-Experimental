package mods.gregtechmod.common.core;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import org.apache.commons.lang3.ArrayUtils;


public class CommonProxy {
    public void registerModel(Item item, int metadata) {}

    public void registerModel(Item item, int metadata, String prefix, String itemName) {}

    public KeyBinding getModeKeyBinding() {
        KeyBinding[] keybinding = (KeyBinding[]) ArrayUtils.clone(Minecraft.getMinecraft().gameSettings.keyBindings);

        for (KeyBinding keybind : keybinding) {
             String category = keybind.getKeyCategory();
             String desc = keybind.getKeyDescription();
             if (category == "IC2" && desc.toLowerCase().contains("mode")) return keybind;
        }
        return null;
    }
}