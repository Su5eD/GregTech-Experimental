package mods.gregtechmod.common.cover;

import net.minecraft.util.ResourceLocation;

/**
 * Overrides the standard {@link ICover#getIcon() item} that's dropped on cover removal
 *
 * @author Su5eD
 */
public interface ICoverSpecialItem {
    boolean overridesDefault();
    ResourceLocation getIconOverride();
}
