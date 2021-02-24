package mods.gregtechmod.inventory.slot;

import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class GtUpgradeSlot extends InvSlot {

    public <T extends IInventorySlotHolder<?> & IUpgradableMachine> GtUpgradeSlot(T base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IGtUpgradeItem) {
            return ((IUpgradableMachine)base).getCompatibleGtUpgrades().contains(((IGtUpgradeItem) item).getType());
        }
        else if (item instanceof IUpgradeItem) {
            Set<UpgradableProperty> properties = ((IUpgradableMachine) base).getCompatibleIC2Upgrades().stream()
                    .map(type -> UpgradableProperty.valueOf(type.property))
                    .collect(Collectors.toSet());
            return ((IUpgradeItem)item).isSuitableFor(stack, properties);
        }
        return false;
    }
}
