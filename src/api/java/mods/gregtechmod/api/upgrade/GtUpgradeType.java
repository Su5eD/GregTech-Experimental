package mods.gregtechmod.api.upgrade;

import java.util.EnumSet;
import java.util.Set;

public enum GtUpgradeType {
    transformer,
    battery,
    lock,
    steam,
    quantum_chest;

    public static final Set<GtUpgradeType> MACHINE_PRESET = EnumSet.of(transformer, battery, lock, steam);
}
