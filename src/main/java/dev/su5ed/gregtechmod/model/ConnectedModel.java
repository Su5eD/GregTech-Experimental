package dev.su5ed.gregtechmod.model;

import com.mojang.math.Vector3f;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class ConnectedModel implements IDynamicBakedModel {
    private static final FaceBakery BAKERY = new FaceBakery();
    private static final BlockFaceUV FACE_UV = new BlockFaceUV(new float[] { 0, 0, 16, 16 }, 0);
    private static final Vector3f MAX = new Vector3f(16, 16, 16);
    private static final Vector3f MAX_DOWN = new Vector3f(16, 0, 16);

    public static final List<String> TEXTURE_PARTS = Arrays.asList(
        "en", "ens", "ensw", "enw", "es",
        "esw", "ew", "ns", "nsw", "nw", "sw"
    );
    private static final Map<String, String> TEXTURE_ALIASES = Map.of(
        "w", "ew",
        "e", "ew",
        "n", "ns",
        "s", "ns"
    );

    private final TextureAtlasSprite particle;
    private final ItemOverrides overrides;
    private final ItemTransforms transforms;
    private final Map<Set<Direction>, Map<Direction, List<BakedQuad>>> quads;

    public ConnectedModel(TextureAtlasSprite particle, Map<String, Material> materials, Function<Material, TextureAtlasSprite> spriteGetter, ItemOverrides overrides, ItemTransforms transforms, ResourceLocation modelLocation) {
        this.particle = particle;
        this.overrides = overrides;
        this.transforms = transforms;
        this.quads = pregenQuads(materials, spriteGetter, modelLocation);
    }

    private Map<Set<Direction>, Map<Direction, List<BakedQuad>>> pregenQuads(Map<String, Material> materials, Function<Material, TextureAtlasSprite> spriteGetter, ResourceLocation modelLocation) {
        Map<Material, TextureAtlasSprite> sprites = StreamEx.ofValues(materials)
            .mapToEntry(spriteGetter)
            .toImmutableMap();

        return IntStreamEx.rangeClosed(0, 0b111111)
            .mapToObj(i -> IntStreamEx.range(Direction.values().length)
                .filter(j -> (i & 1 << j) != 0)
                .mapToObj(Direction::from3DDataValue)
                .toImmutableSet())
            .mapToEntry(key -> getQuadsForSides(key, materials, sprites, modelLocation))
            .toImmutableMap();
    }

    private Map<Direction, List<BakedQuad>> getQuadsForSides(Set<Direction> sides, Map<String, Material> materials, Map<Material, TextureAtlasSprite> sprites, ResourceLocation modelLocation) {
        return StreamEx.of(Direction.values())
            .toMap(facing -> {
                Vector3f to = facing == Direction.DOWN ? MAX_DOWN : MAX;
                String texture = getTexture(sides, facing, materials);
                Material material = materials.get(texture);
                TextureAtlasSprite sprite = sprites.get(material);
                BlockElementFace face = new BlockElementFace(facing.getOpposite(), 0, material.texture().toString(), FACE_UV);

                BakedQuad quad = BAKERY.bakeQuad(Vector3f.ZERO, to, face, sprite, facing, BlockModelRotation.X0_Y0, null, true, modelLocation);
                return List.of(quad);
            });
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        if (state.getBlock() instanceof ConnectedBlock block) {
            return new ModelDataMap.Builder()
                .withInitial(ConnectedBlock.DIRECTIONS, block.getConnections(world, pos))
                .build();
        }
        return tileData;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            Set<Direction> key = getConnections(extraData);
            Map<Direction, List<BakedQuad>> faceQuads = this.quads.get(key);
            return faceQuads.get(side);
        }
        return List.of();
    }

    private String getTexture(Set<Direction> sides, Direction side, Map<String, Material> materials) {
        String connectionStringRaw = StreamEx.of(GtUtil.HORIZONTAL_FACINGS)
            .filter(facing -> sides.contains(getRelativeSide(side, facing)))
            .map(facing -> {
                Direction actualFacing = side == Direction.DOWN && facing.getAxis() == Direction.Axis.Z || GtUtil.isHorizontalFacing(side) && facing.getAxis() == Direction.Axis.X
                    ? facing.getOpposite()
                    : facing;
                return actualFacing.getName().toLowerCase(Locale.ROOT).substring(0, 1);
            })
            .sorted()
            .joining();
        String connectionString = TEXTURE_ALIASES.getOrDefault(connectionStringRaw, connectionStringRaw);

        return materials.containsKey(connectionString) ? connectionString : "";
    }

    private Direction getRelativeSide(Direction facing, Direction relative) {
        boolean verticalFacing = GtUtil.isVerticalFacing(facing);

        if (!verticalFacing && relative.getAxis() == Direction.Axis.Z) {
            return relative == Direction.NORTH ? Direction.UP : Direction.DOWN;
        }
        else if (facing == Direction.NORTH || verticalFacing) {
            return relative;
        }
        else if (GtUtil.isHorizontalFacing(relative)) {
            return relative.getAxisDirection() == Direction.AxisDirection.POSITIVE ? facing.getClockWise() : facing.getCounterClockWise();
        }
        return facing;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemTransforms getTransforms() {
        return this.transforms;
    }

    private static Set<Direction> getConnections(IModelData data) {
        Set<Direction> directions = data.getData(ConnectedBlock.DIRECTIONS);
        return directions != null ? directions : Set.of();
    }
}
