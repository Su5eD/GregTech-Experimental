package mods.gregtechmod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IProxy {
    void init();

    void playSound(SoundEvent sound, float pitch);

    void doSonictronSound(ItemStack stack, World world, BlockPos pos);
}
