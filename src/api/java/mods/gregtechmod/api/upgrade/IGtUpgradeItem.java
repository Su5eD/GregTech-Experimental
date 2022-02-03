package mods.gregtechmod.api.upgrade;

import mods.gregtechmod.api.machine.IUpgradableMachine;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IGtUpgradeItem {

    GtUpgradeType getType();

    String getName();

    /**
     * Whether the upgrade can be inserted
     */
    boolean canBeInserted(ItemStack stack, IUpgradableMachine machine);

    /**
     * Called right before the upgrade is inserted into the machine. Return true to cancel the insertion
     */
    boolean beforeInsert(IUpgradableMachine machine, Player player);

    /**
     * Called after the upgrade is inserted into the machine
     */
    void afterInsert(IUpgradableMachine machine, Player player);
}
