package mods.gregtechmod.common.inventory;

import ic2.api.upgrade.IUpgradeItem;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GtUpgradeSlot extends InvSlot {

    public <T extends IInventorySlotHolder<?> & IUpgradableMachine> GtUpgradeSlot(T base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        if (item instanceof IGtUpgradeItem) {
            return ((IUpgradableMachine)base).getCompatibleGtUpgrades().contains(((IGtUpgradeItem) item).getType());
        }
        else if (item instanceof IUpgradeItem) {
            ItemUpgradeModule.UpgradeType type = ItemUpgradeModule.UpgradeType.values()[meta];
            return ((IUpgradableMachine)base).getCompatibleIC2Upgrades().contains(type);
        }
        return false;
    }
}
