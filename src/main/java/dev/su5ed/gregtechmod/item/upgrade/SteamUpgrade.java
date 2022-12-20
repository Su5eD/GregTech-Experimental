package dev.su5ed.gregtechmod.item.upgrade;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import dev.su5ed.gregtechmod.util.power.SteamPowerProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SteamUpgrade extends UpgradeItemBase {
    
    public SteamUpgrade() {
        super(UpgradeCategory.STEAM, 1, 1);
    }

    @Override
    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        if (machine.getPowerProvider(SteamPowerProvider.class).isPresent()) {
            return Upgrade.InjectResult.REJECT;
        }
        return super.beforeInsert(machine, player, stack);
    }

    @Override
    public void update(UpgradableBlockEntity machine, @Nullable Player player) {
        super.update(machine, player);
        
        CoverHandler coverHandler = machine.getCapability(Capabilities.COVER_HANDLER).orElse(null);
        machine.addPowerProvider(new SteamPowerProvider(machine, coverHandler));
    }
}
