package mods.gregtechmod.api.upgrade;

import java.util.EnumSet;
import java.util.Set;

public enum GtUpgradeType {
    TRANSFORMER,
    BATTERY,
    LOCK,
    STEAM,
    QUANTUM_CHEST;

    public static final Set<GtUpgradeType> MACHINE_PRESET = EnumSet.of(TRANSFORMER, BATTERY, LOCK, STEAM);
}
