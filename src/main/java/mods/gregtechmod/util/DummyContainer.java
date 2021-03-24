package mods.gregtechmod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class DummyContainer extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
