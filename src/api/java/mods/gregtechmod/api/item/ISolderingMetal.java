package mods.gregtechmod.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISolderingMetal {
    boolean canUse(ItemStack stack);

    void onUsed(EntityPlayer player, ItemStack stack);
}
