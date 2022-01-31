package mods.gregtechmod.model;

import ic2.core.util.Util;
import mods.gregtechmod.objects.blocks.BlockConnected;
import mods.gregtechmod.objects.blocks.BlockConnectedTurbine;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.Rotor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelBlockConnected extends ModelBase {
    public static final List<String> TEXTURE_PARTS = Arrays.asList(
            "", "en", "ens", "ensw", "enw", "es",
            "esw", "ew", "ns", "nsw", "nw", "sw"
    );
    private static final Map<String, String> TEXTURE_ALIASES = new HashMap<>();
    private final Map<String, ResourceLocation> textures;
    
    static {
        TEXTURE_ALIASES.put("w", "ew");
        TEXTURE_ALIASES.put("e", "ew");
        TEXTURE_ALIASES.put("n", "ns");
        TEXTURE_ALIASES.put("s", "ns");
    }
    
    @SafeVarargs
    public ModelBlockConnected(Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        this(true, mainTextures, extras);
    }

    @SafeVarargs
    protected ModelBlockConnected(boolean enableCache, Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        super(mainTextures.get(""), getTextures(mainTextures, extras).map(Map.Entry::getValue).collect(Collectors.toList()), enableCache);
        this.textures = getTextures(mainTextures, extras).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    @SafeVarargs
    private static Stream<Map.Entry<String, ResourceLocation>> getTextures(Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        return Stream.concat(mainTextures.entrySet().stream(), Arrays.stream(extras).map(Map::entrySet).flatMap(Collection::stream));
    }

    @Override
    protected IBakedModel generateModel(IBlockState state) {
        Map<EnumFacing, List<BakedQuad>> faceQuads = Arrays.stream(EnumFacing.VALUES)
                .collect(Collectors.toMap(Function.identity(), facing -> {
                    String texture = getTexture(state, facing);
                    TextureAtlasSprite sprite = this.sprites.get(this.textures.get(texture));
                    return Collections.singletonList(getQuad(new Vector3f(0, 0, 0), new Vector3f(16, facing == EnumFacing.DOWN ? 0 : 16, 16), texture, facing, sprite));
                }));
        
        return new SimpleBakedModel(Collections.emptyList(), faceQuads, true, true, getParticleTexture(), ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE);
    }
    
    private String getTexture(IBlockState state, EnumFacing side) {
        Rotor rotor = GtUtil.getStateValueSafely(state, BlockConnectedTurbine.TURBINE_ROTOR);
        if (rotor != null && rotor != Rotor.DISABLED && rotor.side == side) {
            return rotor.getTexture();
        }
        
        Map<EnumFacing, Boolean> connections = getConnections(state);
        Map<EnumFacing, Boolean> relativeConnections = connections.keySet().stream()
                .filter(facing -> !Util.verticalFacings.contains(facing))
                .collect(Collectors.toMap(Function.identity(), facing -> connections.get(getRelativeSide(side, facing))));
        
        String connectionStringRaw = relativeConnections.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> {
                    EnumFacing facing = entry.getKey();
                    if (side == EnumFacing.DOWN && facing.getAxis() == EnumFacing.Axis.Z || Util.horizontalFacings.contains(side) && facing.getAxis() == EnumFacing.Axis.X) facing = facing.getOpposite();
                    return facing.getName().toLowerCase(Locale.ROOT).substring(0, 1);
                })
                .sorted()
                .collect(Collectors.joining());
        String connectionString = TEXTURE_ALIASES.getOrDefault(connectionStringRaw, connectionStringRaw);
        
        return this.textures.containsKey(connectionString) ? connectionString : "";
    }
    
    private EnumFacing getRelativeSide(EnumFacing facing, EnumFacing relative) {
        boolean verticalFacing = Util.verticalFacings.contains(facing);
        
        if (!verticalFacing && (relative == EnumFacing.NORTH || relative == EnumFacing.SOUTH)) {
            return relative == EnumFacing.NORTH ? EnumFacing.UP : EnumFacing.DOWN;
        }
        else if (facing == EnumFacing.NORTH || verticalFacing) return relative;
        else if (!Util.verticalFacings.contains(relative)) {
            if (facing == EnumFacing.SOUTH) return relative.getOpposite();
            else return relative.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? facing.rotateY() : facing.rotateY().getOpposite();
        }
        return facing;
    }
    
    protected Map<EnumFacing, Boolean> getConnections(IBlockState state) {
        Map<EnumFacing, Boolean> connections = new HashMap<>();
        connections.put(EnumFacing.DOWN, state.getValue(BlockConnected.CONNECTED_DOWN));
        connections.put(EnumFacing.UP, state.getValue(BlockConnected.CONNECTED_UP));
        connections.put(EnumFacing.NORTH, state.getValue(BlockConnected.CONNECTED_NORTH));
        connections.put(EnumFacing.SOUTH, state.getValue(BlockConnected.CONNECTED_SOUTH));
        connections.put(EnumFacing.WEST, state.getValue(BlockConnected.CONNECTED_WEST));
        connections.put(EnumFacing.EAST, state.getValue(BlockConnected.CONNECTED_EAST));
        return connections;
    }
    
    private BakedQuad getQuad(Vector3f from, Vector3f to, String texture, EnumFacing direction, TextureAtlasSprite sprite) {
        return bakery.makeBakedQuad(from, to, new BlockPartFace(direction.getOpposite(), 0, this.textures.get(texture).toString(), new BlockFaceUV(new float[]{ 0, 0, 16, 16 }, 0)), sprite, direction, ModelRotation.X0_Y0, null, true, true);
    }
}
