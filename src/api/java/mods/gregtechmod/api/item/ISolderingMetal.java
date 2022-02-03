package mods.gregtechmod.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ISolderingMetal {
    boolean canUse(ItemStack stack);

    void onUsed(Player player, ItemStack stack);
}
