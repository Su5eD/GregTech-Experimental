package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.GtUpgradeItem;
import dev.su5ed.gregtechmod.api.upgrade.GtUpgradeType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class UpgradeItem extends ResourceItem implements GtUpgradeItem {
    private final GtUpgradeType type;
    private final int maxCount;
    private final int requiredTier;
    private final Predicate<UpgradableBlockEntity> condition;
    private final BiPredicate<UpgradableBlockEntity, Player> beforeInsert;
    private final BiConsumer<UpgradableBlockEntity, Player> afterInsert;

    public UpgradeItem(ExtendedItemProperties<?> properties, GtUpgradeType type, int maxCount, int requiredTier,
                       Predicate<UpgradableBlockEntity> condition, BiPredicate<UpgradableBlockEntity, Player> beforeInsert,
                       BiConsumer<UpgradableBlockEntity, Player> afterInsert) {
        super(properties);
        
        this.type = type;
        this.maxCount = maxCount;
        this.requiredTier = requiredTier;
        this.condition = condition;
        this.beforeInsert = beforeInsert;
        this.afterInsert = afterInsert;
    }

    @Override
    public GtUpgradeType getType() {
        return this.type;
    }

    @Override
    public boolean canBeInserted(ItemStack stack, UpgradableBlockEntity machine) {
        return this.maxCount > stack.getCount() && this.requiredTier <= Math.max(machine.getSinkTier(), machine.getSourceTier()) && this.condition.test(machine);
    }

    @Override
    public boolean beforeInsert(UpgradableBlockEntity machine, Player player) {
        // TODO Inverse: return true if can be inserted
        return this.beforeInsert.test(machine, player);
    }

    @Override
    public void afterInsert(UpgradableBlockEntity machine, Player player) {
        this.afterInsert.accept(machine, player);
    }
}
