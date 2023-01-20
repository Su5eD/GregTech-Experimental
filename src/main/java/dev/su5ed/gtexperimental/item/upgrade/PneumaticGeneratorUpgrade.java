package dev.su5ed.gtexperimental.item.upgrade;

import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.upgrade.Upgrade;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.util.GtLocale;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PneumaticGeneratorUpgrade extends UpgradeItemBase {

    public PneumaticGeneratorUpgrade() {
        super(UpgradeCategory.MJ, 1, 1);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        if (!ModHandler.buildcraftLoaded) {
            player.displayClientMessage(GtLocale.key("info", "buildcraft_absent").toComponent(), false);
            return Upgrade.InjectResult.SUCCESS;
        }
        return super.beforeInsert(machine, player, stack);
    }
}
