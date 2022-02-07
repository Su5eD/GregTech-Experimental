package dev.su5ed.gregtechmod.model;

import com.mojang.math.Vector3f;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class ConnectedModel implements IDynamicBakedModel {
    private static final FaceBakery BAKERY = new FaceBakery();
    private static final BlockFaceUV FACE_UV = new BlockFaceUV(new float[] { 0, 0, 16, 16 }, 0);

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
    private static final Vector3f MAX = new Vector3f(16, 16, 16);
    private static final Vector3f MAX_DOWN = new Vector3f(16, 0, 16);

    private final TextureAtlasSprite particle;
    private final ItemOverrides overrides;
    private final ItemTransforms transforms;
    private final Map<ModelKey, Map<Direction, List<BakedQuad>>> quads;

    public ConnectedModel(TextureAtlasSprite particle, Map<String, Material> materials, Function<Material, TextureAtlasSprite> spriteGetter, ItemOverrides overrides, ItemTransforms transforms, ResourceLocation modelLocation) {
        this.particle = particle;
        this.overrides = overrides;
        this.transforms = transforms;
        this.quads = pregenQuads(materials, spriteGetter, modelLocation);
    }

    private Map<ModelKey, Map<Direction, List<BakedQuad>>> pregenQuads(Map<String, Material> materials, Function<Material, TextureAtlasSprite> spriteGetter, ResourceLocation modelLocation) {
        Map<Material, TextureAtlasSprite> sprites = StreamEx.ofValues(materials)
            .mapToEntry(spriteGetter)
            .toImmutableMap();
        
        return IntStreamEx.range(0b111111)
            .mapToObj(i -> new ModelKey(
                (0b000001 & i) != 0,
                (0b000010 & i) != 0,
                (0b0000100 & i) != 0,
                (0b001000 & i) != 0,
                (0b010000 & i) != 0,
                (0b100000 & i) != 0
            ))
            .mapToEntry(key -> getQuadsForKey(key, materials, sprites, modelLocation))
            .toImmutableMap();
    }

    private Map<Direction, List<BakedQuad>> getQuadsForKey(ModelKey key, Map<String, Material> materials, Map<Material, TextureAtlasSprite> sprites, ResourceLocation modelLocation) {
        return StreamEx.of(Direction.values())
            .toMap(facing -> {
                Vector3f to = facing == Direction.DOWN ? MAX_DOWN : MAX;
                String texture = getTexture(key, facing, materials);
                Material material = materials.get(texture);
                TextureAtlasSprite sprite = sprites.get(material);
                BlockElementFace face = new BlockElementFace(facing.getOpposite(), 0, material.texture().toString(), FACE_UV);

                BakedQuad quad = BAKERY.bakeQuad(Vector3f.ZERO, to, face, sprite, facing, BlockModelRotation.X0_Y0, null, true, modelLocation);
                return List.of(quad);
            });
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (side != null) {
            ModelKey key = ModelKey.fromState(state);
            Map<Direction, List<BakedQuad>> faceQuads = this.quads.get(key);
            return faceQuads.get(side);
        }
        return List.of();
    }

    private String getTexture(ModelKey key, Direction side, Map<String, Material> materials) {
        Map<Direction, Boolean> connections = key.getConnections();

        String connectionStringRaw = StreamEx.ofKeys(connections)
            .remove(GtUtil::isVerticalFacing)
            .mapToEntry(facing -> connections.get(getRelativeSide(side, facing)))
            .filterValues(Boolean::booleanValue)
            .mapKeyValue((facing, connected) -> {
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

    private record ModelKey(boolean connectedDown, boolean connectedUp, boolean connectedNorth, boolean connectedSouth, boolean connectedWest, boolean connectedEast) {
        public static final ModelKey DEFAULT = new ModelKey(false, false, false, false, false, false);

        public static ModelKey fromState(@Nullable BlockState state) {
            return state != null ? new ModelKey(
                state.getValue(ConnectedBlock.CONNECTED_DOWN), 
                state.getValue(ConnectedBlock.CONNECTED_UP), 
                state.getValue(ConnectedBlock.CONNECTED_NORTH),
                state.getValue(ConnectedBlock.CONNECTED_SOUTH),
                state.getValue(ConnectedBlock.CONNECTED_WEST),
                state.getValue(ConnectedBlock.CONNECTED_EAST)
            )
                : DEFAULT;
        }

        public Map<Direction, Boolean> getConnections() {
            return Map.of(
                Direction.DOWN, this.connectedDown,
                Direction.UP, this.connectedUp,
                Direction.NORTH, this.connectedNorth,
                Direction.SOUTH, this.connectedSouth,
                Direction.WEST, this.connectedWest,
                Direction.EAST, this.connectedEast
            );
        }
    }
}
