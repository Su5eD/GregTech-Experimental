package mods.gregtechmod.core;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ClientProxy {
    
    public void playSound(SoundEvent sound, float pitch) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.world.playSound(minecraft.player.posX, minecraft.player.posY, minecraft.player.posZ, sound, SoundCategory.BLOCKS, 1, pitch, false);
    }
}
