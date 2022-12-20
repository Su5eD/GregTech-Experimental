package dev.su5ed.gregtechmod.item.upgrade;

import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import net.minecraft.world.item.ItemStack;

public class BatteryUpgrade extends UpgradeItemBase {
    private final int extraEUCapacity;
    
    public BatteryUpgrade(int maxCount, int requiredTier, int extraEUCapacity) {
        super(UpgradeCategory.BATTERY, maxCount, requiredTier);
        
        this.extraEUCapacity = extraEUCapacity;
    }

    @Override
    public int getExtraEUCapacity(ItemStack stack) {
        return this.extraEUCapacity * stack.getCount();
    }
}
