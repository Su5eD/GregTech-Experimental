package mods.gregtechmod.api.cover;

import mods.gregtechmod.api.util.TriFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class CoverRegistry {
    private static final Map<String, TriFunction<ICoverable, EnumFacing, ItemStack, ICover>> COVER_MAP = new HashMap<>();
    private static final Map<Class<? extends ICover>, String> NAME_MAP = new HashMap<>();

    public static void registerCover(String name, TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor) {
        if (COVER_MAP.put(name, constructor) != null) throw new IllegalStateException("duplicate name: " + name);
        NAME_MAP.put(constructor.apply(null, null, null).getClass(), name);
    }

    public static ICover constructCover(String name, EnumFacing side, ICoverable te, ItemStack stack) {
        TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor = COVER_MAP.get(name);
        return constructor.apply(te, side, stack);
    }

    public static String getCoverName(ICover cover) {
        return NAME_MAP.get(cover.getClass());
    }
}
