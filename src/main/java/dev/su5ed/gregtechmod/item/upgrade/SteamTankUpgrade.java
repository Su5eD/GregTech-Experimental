package dev.su5ed.gregtechmod.item.upgrade;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import dev.su5ed.gregtechmod.api.util.GtFluidTank;
import dev.su5ed.gregtechmod.util.power.SteamPowerProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SteamTankUpgrade extends UpgradeItemBase {

    public SteamTankUpgrade() {
        super(UpgradeCategory.STEAM, 16, 1);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        if (machine.getPowerProvider(SteamPowerProvider.class).isEmpty()) {
            return Upgrade.InjectResult.REJECT;
        }
        return super.beforeInsert(machine, player, stack);
    }

    @Override
    public void update(UpgradableBlockEntity machine, @Nullable Player player) {
        super.update(machine, player);
        
        machine.getPowerProvider(SteamPowerProvider.class)
            .ifPresent(provider -> {
                GtFluidTank steamTank = provider.getSteamTank();
                steamTank.setCapacity(steamTank.getCapacity() + 64000);
            });
    }
}
