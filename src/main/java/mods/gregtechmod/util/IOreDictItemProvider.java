package mods.gregtechmod.util;

import javax.annotation.Nullable;

public interface IOreDictItemProvider extends IItemProvider {

    @Nullable
    String getOreDictName();

    default boolean isWildcard() {
        return false;
    }
}
