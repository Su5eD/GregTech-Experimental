package dev.su5ed.gregtechmod.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface SolderingTool {
    /**
     * Uses the soldering tool, draining its durability and the durability of the soldering metal
     * @param player The player soldering
     * @param simulate Whether the solder should be simulated or not. Such simulation can be used to determine if the solder can be performed.
     * @return <code>true</code> if the solder was performed successfully
     */
    boolean solder(Player player, boolean simulate);
}
