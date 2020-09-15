package mods.gregtechmod.api.cover;

import mods.gregtechmod.api.util.TriFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

/**
 * The home of all <code>{@link ICover}</code>s
 */
public class CoverRegistry {
    private static final Map<String, TriFunction<ICoverable, EnumFacing, ItemStack, ICover>> COVER_MAP = new HashMap<>();
    private static final Map<Class<? extends ICover>, String> NAME_MAP = new HashMap<>();

    /**
     * Registers a gregtech cover
     * @param name The cover's unique registry name
     * @param factory Takes the <code>{@link ICoverable}</code> which is being covered, the <code>{@link EnumFacing}</code> at which it's being covered, an <code>{@link ItemStack}</code> containing the cover item and returns a new instance of the target <code>{@link ICover}</code>
     */
    public static void registerCover(String name, TriFunction<ICoverable, EnumFacing, ItemStack, ICover> factory) {
        if (COVER_MAP.put(name, factory) != null) throw new IllegalStateException("duplicate name: " + name);
        NAME_MAP.put(factory.apply(null, null, null).getClass(), name);
    }

    /**
     * Constructs a new instance of a cover
     * @param name Name of the cover
     * @param side The cover's side
     * @param parent The <code>{@link net.minecraft.tileentity.TileEntity}</code> being covered
     * @param stack <code>{@link ItemStack}</code> containing the cover item
     * @return A new instance of the target cover
     */
    public static ICover constructCover(String name, EnumFacing side, ICoverable parent, ItemStack stack) {
        TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor = COVER_MAP.get(name);
        return constructor.apply(parent, side, stack);
    }

    /**
     * @param cover The cover to look up
     * @return The name of the cover.
     */
    public static String getCoverName(ICover cover) {
        return NAME_MAP.get(cover.getClass());
    }
}
