package mods.gregtechmod.core;

import mods.gregtechmod.util.IProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {

    @Override
    public void init() {}

    @Override
    public void playSound(SoundEvent sound, float pitch) {}

    @Override
    public void doSonictronSound(ItemStack stack, World world, BlockPos pos) {}
}
