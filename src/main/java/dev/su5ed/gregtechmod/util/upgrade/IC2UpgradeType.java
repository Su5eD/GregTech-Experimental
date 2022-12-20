package dev.su5ed.gregtechmod.util.upgrade;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class IC2UpgradeType implements Upgrade {
    private final ItemStack stack;
    private final UpgradeCategory category;
    private final Function<UpgradableBlockEntity, Integer> maxCount;

    public IC2UpgradeType(ItemStack stack, UpgradeCategory category, Function<UpgradableBlockEntity, Integer> maxCount) {
        this.stack = stack;
        this.category = category;
        this.maxCount = maxCount;
    }

    @Override
    public UpgradeCategory getCategory() {
        return this.category;
    }

    @Override
    public InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        int count = stack.getCount();
        int maxCount = this.maxCount.apply(machine);
        return count < maxCount ? InjectResult.PASS : InjectResult.REJECT;
    }

    @Override
    public void update(UpgradableBlockEntity machine, @Nullable Player player, ItemStack stack) {

    }

    @Override
    public int getExtraEUCapacity() {
        return this.category == UpgradeCategory.BATTERY ? this.stack.getCount() * 10000 : 0;
    }
}
