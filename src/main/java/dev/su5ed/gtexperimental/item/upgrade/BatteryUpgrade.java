package dev.su5ed.gtexperimental.item.upgrade;

import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.util.GtLocale;
import net.minecraft.world.item.ItemStack;

public class BatteryUpgrade extends UpgradeItemBase {
    private final int extraEUCapacity;

    public BatteryUpgrade(int maxCount, int requiredTier, int extraEUCapacity) {
        super(new ExtendedItemProperties<>().autoDescription(GtLocale.formatNumber(extraEUCapacity)), UpgradeCategory.BATTERY, maxCount, requiredTier);

        this.extraEUCapacity = extraEUCapacity;
    }

    @Override
    public int getExtraEUCapacity(ItemStack stack) {
        return this.extraEUCapacity * stack.getCount();
    }
}
