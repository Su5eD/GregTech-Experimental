package mods.gregtechmod.objects.items;

import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

public class ItemUpgrade extends ItemBase implements IGtUpgradeItem {
    private final GtUpgradeType type;
    private final int requiredTier;
    private final int maxCount;
    private final BiPredicate<ItemStack, IUpgradableMachine> condition;
    private final TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> beforeInsert;
    private final TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert;

    public ItemUpgrade(String name, String descriptionKey, GtUpgradeType type, int maxCount, int requiredTier, BiPredicate<ItemStack, IUpgradableMachine> condition, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> beforeInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert) {
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
    public String getName() {
        return this.name;
    }

    @Override
    public boolean canBeInserted(ItemStack stack, IUpgradableMachine machine) {
        return this.maxCount > stack.getCount() && requiredTier <= Math.max(machine.getSinkTier(), machine.getSourceTier()) && this.condition.test(stack, machine);
    }

    @Override
    public boolean beforeInsert(ItemStack stack, IUpgradableMachine machine, EntityPlayer player) {
        return this.beforeInsert.apply(stack, machine, player);
    }

    @Override
    public void afterInsert(ItemStack stack, IUpgradableMachine machine, EntityPlayer player) {
        this.afterInsert.accept(stack, machine, player);
    }
}
