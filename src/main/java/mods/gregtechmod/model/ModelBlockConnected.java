package mods.gregtechmod.model;

import com.google.common.collect.ImmutableMap;
import ic2.core.util.Util;
import mods.gregtechmod.objects.blocks.BlockConnected;
import mods.gregtechmod.objects.blocks.BlockConnectedTurbine;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.Rotor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Function;

public class ModelBlockConnected extends ModelCached {
    public static final List<String> TEXTURE_PARTS = Arrays.asList(
        "en", "ens", "ensw", "enw", "es",
        "esw", "ew", "ns", "nsw", "nw", "sw"
    );
    private static final Map<String, String> TEXTURE_ALIASES = ImmutableMap.of(
        "w", "ew",
        "e", "ew",
        "n", "ns",
        "s", "ns"
    );

    private final Map<String, ResourceLocation> textures;

    @SafeVarargs
    public ModelBlockConnected(Block block, Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        super(block, mainTextures.get(""), getTextures(mainTextures, extras).values().toImmutableList());
        this.textures = getTextures(mainTextures, extras).toImmutableMap();
    }

    @SafeVarargs
    private static EntryStream<String, ResourceLocation> getTextures(Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        return EntryStream.of(mainTextures)
            .append(StreamEx.of(extras)
                .flatMapToEntry(Function.identity()));
    }

    @Override
    protected Map<IBlockState, Map<EnumFacing, List<BakedQuad>>> populateCache(List<IBlockState> states) {
        return StreamEx.of(states)
            .mapToEntry(state -> StreamEx.of(EnumFacing.VALUES)
                .toMap(facing -> getQuads(state, facing)))
            .toImmutableMap();
    }

    @Override
    protected List<BakedQuad> getQuads(IBlockState state, EnumFacing side) {
        String texture = getTexture(state, side);
        TextureAtlasSprite sprite = this.sprites.get(this.textures.get(texture));
        BakedQuad quad = BAKERY.makeBakedQuad(ModelBase.ZERO, side == EnumFacing.DOWN ? MAX_DOWN : MAX, new BlockPartFace(side.getOpposite(), 0, this.textures.get(texture).toString(), FACE_UV), sprite, side, ModelRotation.X0_Y0, null, true, true);
        return Collections.singletonList(quad);
    }

    private String getTexture(IBlockState state, EnumFacing side) {
        Rotor rotor = GtUtil.getStateValueSafely(state, BlockConnectedTurbine.TURBINE_ROTOR);
        if (rotor != null && rotor != Rotor.DISABLED && rotor.side == side) {
            return rotor.getTexture();
        }

        Map<EnumFacing, Boolean> connections = getConnections(state);
        String connectionStringRaw = StreamEx.of(Util.horizontalFacings)
            .filter(facing -> connections.get(getRelativeSide(side, facing)))
            .map(facing -> {
                EnumFacing actualFacing = side == EnumFacing.DOWN && facing.getAxis() == EnumFacing.Axis.Z || Util.horizontalFacings.contains(side) && facing.getAxis() == EnumFacing.Axis.X
                    ? facing.getOpposite()
                    : facing;
                return actualFacing.getName().toLowerCase(Locale.ROOT).substring(0, 1);
            })
            .sorted()
            .joining();
        String connectionString = TEXTURE_ALIASES.getOrDefault(connectionStringRaw, connectionStringRaw);

        return this.textures.containsKey(connectionString) ? connectionString : "";
    }

    private EnumFacing getRelativeSide(EnumFacing facing, EnumFacing relative) {
        boolean verticalFacing = Util.verticalFacings.contains(facing);

        if (!verticalFacing && relative.getAxis() == EnumFacing.Axis.Z) {
            return relative == EnumFacing.NORTH ? EnumFacing.UP : EnumFacing.DOWN;
        }
        else if (facing == EnumFacing.NORTH || verticalFacing) {
            return relative;
        }
        else if (!Util.verticalFacings.contains(relative)) {
            return relative.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? facing.rotateY() : facing.rotateYCCW();
        }
        return facing;
    }

    protected Map<EnumFacing, Boolean> getConnections(IBlockState state) {
        return EntryStream.of(
            EnumFacing.DOWN, state.getValue(BlockConnected.CONNECTED_DOWN),
            EnumFacing.UP, state.getValue(BlockConnected.CONNECTED_UP),
            EnumFacing.NORTH, state.getValue(BlockConnected.CONNECTED_NORTH),
            EnumFacing.SOUTH, state.getValue(BlockConnected.CONNECTED_SOUTH),
            EnumFacing.WEST, state.getValue(BlockConnected.CONNECTED_WEST),
            EnumFacing.EAST, state.getValue(BlockConnected.CONNECTED_EAST)
        )
            .toImmutableMap();
    }
}
