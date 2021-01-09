package mods.gregtechmod.api.upgrade;

import java.util.EnumSet;
import java.util.Set;

public enum GtUpgradeType {
    TRANSFORMER,
    BATTERY,
    LOCK(false),
    STEAM,
    OTHER(false);

    public final boolean display;

    GtUpgradeType() {
        this(true);
    }

    GtUpgradeType(boolean display) {
        this.display = display;
    }

    public static final Set<GtUpgradeType> DEFAULT = EnumSet.of(TRANSFORMER, BATTERY, LOCK, STEAM);
}
