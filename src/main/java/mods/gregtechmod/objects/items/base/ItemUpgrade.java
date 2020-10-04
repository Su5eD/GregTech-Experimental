package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.TriFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiPredicate;

public class ItemUpgrade extends ItemBase implements IGtUpgradeItem {
    private final GtUpgradeType type;
    private final int requiredTier;
    private final BiPredicate<ItemStack, IUpgradableMachine> condition;
    private final TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert;
    private final TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate;

    public ItemUpgrade(String name, String description, GtUpgradeType type, int maxCount, int requiredTier, BiPredicate<ItemStack, IUpgradableMachine> condition, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
        super(name, description);
        setMaxStackSize(maxCount);
        this.type = type;
        this.requiredTier = requiredTier;
        this.condition = condition;
        this.onInsert = onInsert;
        this.onUpdate = onUpdate;
    }

    @Override
    public GtUpgradeType getType() {
        return this.type;
    }

    @Override
    public int getRequiredTier() {
        return this.requiredTier;
    }

    @Override
    public boolean canBeInserted(ItemStack stack, IUpgradableMachine machine) {
        return this.maxStackSize > stack.getCount() && requiredTier <= machine.getTier() && this.condition.test(stack, machine);
    }

    @Override
    public boolean onInsert(ItemStack stack, IUpgradableMachine machine, EntityPlayer player) {
        return this.onInsert.apply(stack, machine, player);
    }

    @Override
    public void onUpdate(ItemStack stack, IUpgradableMachine machine, EntityPlayer player) {
        this.onUpdate.accept(stack, machine, player);
    }
}
