package dev.su5ed.gtexperimental.item.upgrade;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.upgrade.Upgrade;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.util.power.SteamPowerProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SteamUpgrade extends UpgradeItemBase {

    public SteamUpgrade() {
        super(UpgradeCategory.STEAM, 1, 1);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        boolean hasSteam = machine.be().getCapability(Capabilities.ENERGY_HANDLER).resolve()
            .flatMap(power -> power.getPowerProvider(SteamPowerProvider.class))
            .isPresent();
        return hasSteam ? Upgrade.InjectResult.REJECT : super.beforeInsert(machine, player, stack);
    }

    @Override
    public void update(UpgradableBlockEntity machine, @Nullable Player player, ItemStack stack) {
        super.update(machine, player, stack);
        
        machine.be().getCapability(Capabilities.ENERGY_HANDLER)
            .ifPresent(power -> power.addPowerProvider(new SteamPowerProvider(machine)));
    }
}
