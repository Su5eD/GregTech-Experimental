package mods.gregtechmod.objects.items;

import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ItemUpgrade extends ItemBase implements IGtUpgradeItem {
    private final GtUpgradeType type;
    private final int requiredTier;
    private final int maxCount;
    private final Predicate<IUpgradableMachine> condition;
    private final BiPredicate<IUpgradableMachine, EntityPlayer> beforeInsert;
    private final BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert;

    public ItemUpgrade(String name, String descriptionKey, GtUpgradeType type, int maxCount, int requiredTier, Predicate<IUpgradableMachine> condition, BiPredicate<IUpgradableMachine, EntityPlayer> beforeInsert, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
        super(name, () -> GtLocale.translateItem(descriptionKey));
        setMaxStackSize(GregTechConfig.FEATURES.upgradeStackSize);
        this.type = type;
        this.requiredTier = requiredTier;
        this.maxCount = maxCount;
        this.condition = condition;
        this.beforeInsert = beforeInsert;
        this.afterInsert = afterInsert;
    }

    @Override
    public GtUpgradeType getType() {
        return this.type;
    }

    @Override
    public boolean canBeInserted(ItemStack stack, IUpgradableMachine machine) {
        return this.maxCount > stack.getCount() && this.requiredTier <= Math.max(machine.getSinkTier(), machine.getSourceTier()) && this.condition.test(machine);
    }

    @Override
    public boolean beforeInsert(IUpgradableMachine machine, EntityPlayer player) {
        return this.beforeInsert.test(machine, player);
    }

    @Override
    public void afterInsert(IUpgradableMachine machine, EntityPlayer player) {
        this.afterInsert.accept(machine, player);
    }
}
