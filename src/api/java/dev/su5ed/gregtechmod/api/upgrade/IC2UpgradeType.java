package dev.su5ed.gregtechmod.api.upgrade;

import com.google.common.base.CaseFormat;
import ic2.api.upgrade.UpgradableProperty;

import java.util.EnumSet;
import java.util.Set;

public enum IC2UpgradeType { // TODO remove UpgradableProperty
    OVERCLOCKER("Processing", 4, "overclocker"),
    TRANSFORMER(2),
    BATTERY("EnergyStorage", 16);

    public static final Set<IC2UpgradeType> DEFAULT = EnumSet.of(OVERCLOCKER, TRANSFORMER, BATTERY);
    
    public final UpgradableProperty property;
    public final int maxCount;
    public final String itemType;

    IC2UpgradeType(int maxCount) {
        String propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name());
        this.property = UpgradableProperty.valueOf(propertyName);
        this.maxCount = maxCount;
        this.itemType = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName);
    }


    IC2UpgradeType(String name, int maxCount) {
        this.property = UpgradableProperty.valueOf(name);
        this.maxCount = maxCount;
        this.itemType = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    IC2UpgradeType(String name, int maxCount, String itemType) {
        this.property = UpgradableProperty.valueOf(name);
        this.maxCount = maxCount;
        this.itemType = itemType;
    }
    
    public UpgradableProperty getProperty() {
        return this.property;
    }
}
