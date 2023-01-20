package dev.su5ed.gtexperimental.item.upgrade;

import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.upgrade.Upgrade;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.blockentity.DigitalChestBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class QuantumChestUpgrade extends UpgradeItemBase {

    public QuantumChestUpgrade() {
        super(UpgradeCategory.OTHER, 1, 0);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        if (!(machine instanceof DigitalChestBlockEntity)) {
            return Upgrade.InjectResult.REJECT;
        }
        return super.beforeInsert(machine, player, stack);
    }

    @Override
    public void update(UpgradableBlockEntity machine, @Nullable Player player, ItemStack stack) {
        super.update(machine, player, stack);

        if (machine instanceof DigitalChestBlockEntity chest) {
            chest.upgradeToQuantumChest();
        }
    }
}
