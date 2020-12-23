package mods.gregtechmod.api.upgrade;

import com.google.common.base.CaseFormat;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.item.upgrade.ItemUpgradeModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.Set;

public enum IC2UpgradeType {
    OVERCLOCKER("Processing", 4, "overclocker"),
    TRANSFORMER(2),
    BATTERY("EnergyStorage", 16);

    public final String property;
    public final int maxCount;
    public final String itemType;

    IC2UpgradeType(int maxCount) {
        this.property = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name());
        this.maxCount = maxCount;
        this.itemType = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.property);
    }


    IC2UpgradeType(String name, int maxCount) {
        this.property = name;
        this.maxCount = maxCount;
        this.itemType = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.property);
    }

    IC2UpgradeType(String name, int maxCount, String itemType) {
        this.property = name;
        this.maxCount = maxCount;
        this.itemType = itemType;
    }

    public static IC2UpgradeType fromStack(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IUpgradeItem) {
            ItemUpgradeModule.UpgradeType upgradeType = ItemUpgradeModule.UpgradeType.values()[stack.getMetadata()];
            for (IC2UpgradeType type : values()) {
                if (type.itemType.equals(upgradeType.name())) return type;
            }
        }
        return null;
    }

    public static final Set<IC2UpgradeType> DEFAULT = EnumSet.of(OVERCLOCKER, TRANSFORMER, BATTERY);
}
