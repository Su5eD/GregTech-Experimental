package dev.su5ed.gregtechmod.api.upgrade;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface GtUpgradeItem {

    GtUpgradeType getType();

    /**
     * Whether the upgrade can be inserted
     */
    boolean canBeInserted(ItemStack stack, UpgradableBlockEntity machine);

    /**
     * Called right before the upgrade is inserted into the machine. Return true to cancel the insertion
     */
    boolean beforeInsert(UpgradableBlockEntity machine, Player player);

    /**
     * Called after the upgrade is inserted into the machine
     */
    void afterInsert(UpgradableBlockEntity machine, Player player);
}
