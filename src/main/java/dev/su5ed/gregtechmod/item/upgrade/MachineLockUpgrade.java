package dev.su5ed.gregtechmod.item.upgrade;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MachineLockUpgrade extends UpgradeItemBase {

    public MachineLockUpgrade() {
        super(UpgradeCategory.LOCK, 1, 0);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        if (!machine.isOwnedBy(player)) {
            player.displayClientMessage(GtLocale.itemKey("machine_lock", "error").toComponent(), false);
            return Upgrade.InjectResult.SUCCESS;
        }
        return super.beforeInsert(machine, player, stack);
    }
}
