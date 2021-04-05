package mods.gregtechmod.render;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.model.AbstractModel;
import ic2.core.model.ModelUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.cover.CoverHandler;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class RenderTeBlock extends AbstractModel {
    private final HashMap<EnumFacing, ResourceLocation> textures;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites;
    private final ResourceLocation particle;
    private final FaceBakery bakery = new FaceBakery();
    public LoadingCache<IBlockState, IBakedModel> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(7, TimeUnit.MINUTES)
            .build(new CacheLoader<IBlockState, IBakedModel>() {
                public IBakedModel load(@Nonnull IBlockState key) {
                    return generateModel(key);
                }
            });
    //Block face UVs in DUNSWE order
    public static final float[][] blockFaceUVs = new float[][] {
            { 0, 16, 16, 0},
            { 0, 0, 16, 16},
            { 0, 0, 16, 16},
            { 0, 0, 16, 16},
            { 0, 0, 16, 16},
            { 0, 0, 16, 16}
    };

    public RenderTeBlock(HashMap<EnumFacing, ResourceLocation> map, ResourceLocation particle) {
        this.textures = map;
        this.particle = particle;
        this.sprites = new HashMap<>();
        for (ResourceLocation loc : textures.values()) sprites.put(loc, null);
        sprites.put(this.particle, null);
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.sprites.keySet();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.sprites.entrySet().forEach(entry -> entry.setValue(bakedTextureGetter.apply(entry.getKey())));
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        try {
            return cache.get(state).getQuads(state, side, rand);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ModelUtil.getMissingModel().getQuads(state, side, rand);
    }

    private IBakedModel generateModel(IBlockState rawState) {
        HashMap<EnumFacing, ResourceLocation> covers = new HashMap<>();
        EnumFacing face = rawState.getValue(BlockTileEntity.facingProperty);
        Ic2BlockState.Ic2BlockStateInstance state = (Ic2BlockState.Ic2BlockStateInstance)rawState;
        HashMap<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<>();

        if (state.hasValue(CoverHandler.COVER_HANDLER_PROPERTY)) {
            CoverHandler handler = state.getValue(CoverHandler.COVER_HANDLER_PROPERTY);
            for (Map.Entry<EnumFacing, ICover> entry : handler.covers.entrySet()) {
                EnumFacing side = entry.getKey();
                covers.put(side, handler.covers.get(side).getIcon());
            }
        }

        for (EnumFacing side : EnumFacing.VALUES) {
            TextureAtlasSprite sprite = getSpriteFromDirection(face, side, state, covers);
            faceQuads.put(side, Collections.singletonList(getQuad(new Vector3f(0,0,0), new Vector3f(16, side == EnumFacing.DOWN ? 0 : 16,16), side, sprite)));
        }

        return new SimpleBakedModel(Collections.emptyList(), faceQuads, true, true, null, ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE);
    }

    private BakedQuad getQuad(Vector3f from, Vector3f to, EnumFacing direction, TextureAtlasSprite sprite) {
        return bakery.makeBakedQuad(from, to, new BlockPartFace(direction, 0, this.textures.get(direction).toString(), new BlockFaceUV(blockFaceUVs[direction.getIndex()], 0)), sprite, direction, ModelRotation.X0_Y0, null, true, true);
    }

    /**
     * @param face the block's facing
     * @param side the side to get the texture for
     * @return the side's texture depending on the block's facing
     */
    private TextureAtlasSprite getSpriteFromDirection(EnumFacing face, EnumFacing side, Ic2BlockState.Ic2BlockStateInstance state, Map<EnumFacing, ResourceLocation> covers) {
        EnumFacing currentFacing = side;
        if (!covers.containsKey(side) && face != EnumFacing.NORTH) {
            if (face == side) currentFacing = EnumFacing.NORTH;
            else if (Util.verticalFacings.contains(face)) {
                if (side == EnumFacing.NORTH) currentFacing = EnumFacing.SOUTH;
            }
            else if (!Util.verticalFacings.contains(side)) {
                if (face == EnumFacing.SOUTH) currentFacing = side.getOpposite();
                else currentFacing = side.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? side.rotateY() : side.rotateY().getOpposite();
            }
        }

        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        PropertyHelper.AnimationSpeed prop;
        PropertyHelper.TextureOverride overrides;

        if (covers.containsKey(side)) return map.getAtlasSprite(covers.get(currentFacing).toString());
        else if ((prop = state.getValue(PropertyHelper.ANIMATION_SPEED_PROPERTY)) != null && prop.getSides().contains(currentFacing) && prop.getValue() > 1) {
            return map.getAtlasSprite(textures.get(currentFacing).toString() + prop.getValue());
        }
        else if ((overrides = state.getValue(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY)) != null && overrides.hasOverride(currentFacing)) {
            return map.getAtlasSprite(overrides.getTextureOverride(currentFacing).toString());
        }
        else if (state.hasValue(PropertyHelper.OUTPUT_SIDE_PROPERTY) && side == state.getValue(PropertyHelper.OUTPUT_SIDE_PROPERTY)) {
            String textureName = side == EnumFacing.DOWN ? "bottom" : side == EnumFacing.UP ? "top" : "side";
            return map.getAtlasSprite(String.format("%s:blocks/machines/machine_%s_pipe", Reference.MODID, textureName));
        }
        else return sprites.get(textures.get(currentFacing));
    }

    @Override
    public void onReload() {}

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.sprites.get(particle);
    }
}
