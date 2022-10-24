package mods.gregtechmod.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ClientProxy {
    
    public void openWrittenBook(ItemStack stack) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.displayGuiScreen(new GuiScreenBook(minecraft.player, stack, false));
    }

    public void playSound(SoundEvent sound, float pitch) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world != null && minecraft.player != null) {
            minecraft.world.playSound(minecraft.player.posX, minecraft.player.posY, minecraft.player.posZ, sound, SoundCategory.BLOCKS, 1, pitch, false);
        }
    }
}
