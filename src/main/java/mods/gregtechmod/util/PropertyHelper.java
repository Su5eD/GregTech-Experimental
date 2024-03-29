package mods.gregtechmod.util;

import ic2.core.block.state.UnlistedEnumProperty;
import ic2.core.block.state.UnlistedProperty;
import ic2.core.util.Util;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.property.IUnlistedProperty;
import one.util.streamex.StreamEx;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class PropertyHelper {
    public static final IUnlistedProperty<AnimationSpeed> ANIMATION_SPEED_PROPERTY = new UnlistedProperty<>("animationSpeed", AnimationSpeed.class);
    public static final IUnlistedProperty<TextureOverride> TEXTURE_OVERRIDE_PROPERTY = new UnlistedProperty<>("textureOverride", TextureOverride.class);
    public static final IUnlistedProperty<DimensionalTextureInfo> TEXTURE_INDEX_PROPERTY = new UnlistedProperty<>("textureInfo", DimensionalTextureInfo.class);
    public static final IUnlistedProperty<EnumFacing> OUTPUT_SIDE_PROPERTY = new UnlistedEnumProperty<>("outputSide", EnumFacing.class);
    public static final IUnlistedProperty<VerticalRotation> VERTICAL_ROTATION_PROPERTY = new UnlistedEnumProperty<>("rotationBehavior", VerticalRotation.class);
    public static final IUnlistedProperty<TextureModeInfo> TEXTURE_MODE_INFO_PROPERTY = new UnlistedProperty<>("textureModeInfo", TextureModeInfo.class);

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
        private final boolean absolute;

        public TextureOverride(ResourceLocation texture) {
            this(StreamEx.of(Util.allFacings).toMap(f -> texture), false);
        }

        public TextureOverride(EnumFacing facing, ResourceLocation texture) {
            this(facing, texture, false);
        }

        public TextureOverride(EnumFacing facing, ResourceLocation texture, boolean absolute) {
            this(Collections.singletonMap(facing, texture), absolute);
        }

        public TextureOverride(Map<EnumFacing, ResourceLocation> overrides, boolean absolute) {
            this.overrides = overrides;
            this.absolute = absolute;
        }

        public boolean hasOverride(EnumFacing side) {
            return this.overrides.containsKey(side);
        }

        public boolean isAbsolute() {
            return this.absolute;
        }

        public ResourceLocation getTextureOverride(EnumFacing side) {
            return this.overrides.get(side);
        }
    }

    public static class DimensionalTextureInfo {
        public final Map<EnumFacing, EnumFacing> sideOverrides;
        public final DimensionType dimension;

        public DimensionalTextureInfo(Map<EnumFacing, EnumFacing> sideOverrides, DimensionType dimension) {
            this.sideOverrides = sideOverrides;
            this.dimension = dimension;
        }
    }

    public enum VerticalRotation {
        MIRROR_BACK((face, side) -> side == EnumFacing.NORTH ? EnumFacing.SOUTH : side),
        ROTATE_X((face, side) -> {
            EnumFacing target = face == EnumFacing.UP ? EnumFacing.NORTH : EnumFacing.SOUTH;

            if (side == target) return face.getOpposite();
            else if (side == target.getOpposite()) return face;
            else if (side == face.getOpposite()) return EnumFacing.SOUTH;

            return side;
        });

        public final BinaryOperator<EnumFacing> rotation;

        VerticalRotation(BinaryOperator<EnumFacing> rotation) {
            this.rotation = rotation;
        }
    }
    
    public static class TextureModeInfo {
        public final ITextureMode mode;
        public final int index;

        public TextureModeInfo(ITextureMode mode, int index) {
            this.mode = mode;
            this.index = index;
        }
    }
}
