package dev.su5ed.gregtechmod.model;

import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConnectedModel extends BaseModel {
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

    private final Map<Set<Direction>, Map<Direction, List<BakedQuad>>> quads;

    public ConnectedModel(TextureAtlasSprite particle, Map<String, Material> materials, Map<Material, TextureAtlasSprite> sprites, ItemOverrides overrides, ItemTransforms transforms, ResourceLocation modelLocation) {
        super(particle, overrides, transforms);
        this.quads = pregenQuads(materials, sprites, modelLocation);
    }

    private Map<Set<Direction>, Map<Direction, List<BakedQuad>>> pregenQuads(Map<String, Material> materials, Map<Material, TextureAtlasSprite> sprites, ResourceLocation modelLocation) {
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
                String texture = getTexture(sides, facing, materials);
                Material material = materials.get(texture);
                TextureAtlasSprite sprite = sprites.get(material);
                BlockElementFace face = new BlockElementFace(facing, 0, material.texture().toString(), FACE_UV);
                return bakeSingleQuad(face, sprite, facing, modelLocation);
            });
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        return state.getBlock() instanceof ConnectedBlock block
            ? new ModelDataMap.Builder()
                .withInitial(ConnectedBlock.DIRECTIONS, block.getConnections(world, pos))
                .build()
            : tileData;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
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

    private static Set<Direction> getConnections(IModelData data) {
        Set<Direction> directions = data.getData(ConnectedBlock.DIRECTIONS);
        return directions != null ? directions : Set.of();
    }
}
