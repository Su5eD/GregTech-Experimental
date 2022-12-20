package dev.su5ed.gregtechmod.item.upgrade;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RSEnergyCellUpgrade extends UpgradeItemBase {

    public RSEnergyCellUpgrade() {
        super(UpgradeCategory.MJ, 16, 1);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        return super.beforeInsert(machine, player, stack); // TODO find a replacement for BC
    }
}
