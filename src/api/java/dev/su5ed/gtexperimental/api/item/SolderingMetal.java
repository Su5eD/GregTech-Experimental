package dev.su5ed.gtexperimental.api.item;

import net.minecraft.world.entity.player.Player;

public interface SolderingMetal {
    boolean canUse();

    void onUsed(Player player);
}
