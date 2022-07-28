package mods.gregtechmod.api.upgrade;

import mods.gregtechmod.api.machine.IUpgradableMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IGtUpgradeItem {

    GtUpgradeType getType();

    /**
     * Whether the upgrade can be inserted
     */
    boolean canBeInserted(ItemStack stack, IUpgradableMachine machine);

    /**
     * Called right before the upgrade is inserted into the machine. Return true to cancel the insertion
     */
    boolean beforeInsert(IUpgradableMachine machine, EntityPlayer player);

    /**
     * Called after the upgrade is inserted into the machine
     */
    void afterInsert(IUpgradableMachine machine, EntityPlayer player);
}
