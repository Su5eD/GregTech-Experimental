package mods.gregtechmod.api.cover;

import java.util.Arrays;
import java.util.Collection;

public enum CoverType {
    GENERIC,
    IO,
    ENERGY,
    METER,
    CONTROLLER,
    UTIL,
    OTHER;
    
    public static final Collection<CoverType> VALUES = Arrays.asList(values());
}
