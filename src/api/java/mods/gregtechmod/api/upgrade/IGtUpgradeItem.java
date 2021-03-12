package mods.gregtechmod.api.upgrade;

import mods.gregtechmod.api.machine.IUpgradableMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IGtUpgradeItem {

    GtUpgradeType getType();

    String getName();

    /**
     * Whether or not can the upgrade be inserted
     */
    boolean canBeInserted(ItemStack stack, IUpgradableMachine machine);

    /**
     * Called right before the upgrade is inserted into the machine. Return true to cancel the insertion
     * @param stack ItemStack in the upgrade slot
     */
    boolean beforeInsert(ItemStack stack, IUpgradableMachine machine, EntityPlayer player);

    /**
     * Called after the upgrade is inserted into the machine
     * @param stack ItemStack in the upgrade slot
     */
    void afterInsert(ItemStack stack, IUpgradableMachine machine, EntityPlayer player);
}
