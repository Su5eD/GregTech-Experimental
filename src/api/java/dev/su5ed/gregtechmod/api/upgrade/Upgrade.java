package dev.su5ed.gregtechmod.api.upgrade;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Upgrade {
    UpgradeCategory getCategory();

    /**
     * Whether the upgrade can be inserted
     */
    InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack);

    /**
     * Called after the upgrade is inserted into the machine
     */
    void update(UpgradableBlockEntity machine, @Nullable Player player, ItemStack stack);

    int getExtraEUCapacity();
    
    enum InjectResult {
        SUCCESS,
        PASS,
        REJECT
    }
}
