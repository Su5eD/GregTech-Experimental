package mods.gregtechmod.common.util;

import ic2.core.block.state.UnlistedBooleanProperty;
import ic2.core.block.state.UnlistedProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Map;
import java.util.Set;

public class GtProperties {

    public static final IUnlistedProperty<AnimationSpeed> ANIMATION_SPEED_PROPERTY = new UnlistedProperty<>("animationSpeed", AnimationSpeed.class);
    public static final IUnlistedProperty<TextureOverride> TEXTURE_OVERRIDE_PROPERTY = new UnlistedProperty<>("textureOverride", TextureOverride.class);
    public static final IUnlistedProperty<Boolean> UV_LOCK_PROPERTY = new UnlistedBooleanProperty("uvLock");

    public static class AnimationSpeed {
        private final Set<EnumFacing> sides;
        private final int value;

        public AnimationSpeed(Set<EnumFacing> sides, int value) {
            this.sides = sides;
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public Set<EnumFacing> getSides() {
            return this.sides;
        }
    }

    public static class TextureOverride {
        private final Map<EnumFacing, ResourceLocation> overrides;

        public TextureOverride(Map<EnumFacing, ResourceLocation> overrides) {
            this.overrides = overrides;
        }

        public boolean hasOverride(EnumFacing side) {
            return this.overrides.containsKey(side);
        }

        public ResourceLocation getTextureOverride(EnumFacing side) {
            return this.overrides.get(side);
        }
    }
}
