package dev.su5ed.gregtechmod.api.upgrade;

import java.util.EnumSet;
import java.util.Set;

public enum UpgradeCategory {
    OVERCLOCKER,
    TRANSFORMER,
    HV_TRANSFORMER(false),
    BATTERY,
    LOCK(false),
    STEAM,
    MJ,
    OTHER(false);
    
    public static final Set<UpgradeCategory> DEFAULT = EnumSet.of(OVERCLOCKER, TRANSFORMER, HV_TRANSFORMER, BATTERY, LOCK, MJ, STEAM);
    
    public final boolean display;

    UpgradeCategory() {
        this(true);
    }
    
    UpgradeCategory(boolean display) {
        this.display = display;
    }
    
    public boolean show() {
        return this.display;
    }
}
