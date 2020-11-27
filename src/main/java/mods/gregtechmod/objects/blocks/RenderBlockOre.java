package mods.gregtechmod.objects.blocks;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ic2.core.model.AbstractModel;
import ic2.core.model.ModelUtil;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class RenderBlockOre extends AbstractModel {
    private final Map<EnumFacing, ResourceLocation> textures;
    private final Map<EnumFacing, ResourceLocation> texturesNether;
    private final Map<EnumFacing, ResourceLocation> texturesEnd;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites = new HashMap<>();
    private final ResourceLocation particle;
    private final FaceBakery bakery = new FaceBakery();
    public LoadingCache<IBlockState, IBakedModel> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .build(new CacheLoader<IBlockState, IBakedModel>() {
                public IBakedModel load(@Nonnull IBlockState key) {
                    return generateModel(key);
                }
            });

    public RenderBlockOre(Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> texturesNether, Map<EnumFacing, ResourceLocation> texturesEnd, ResourceLocation particle) {
        this.textures = textures;
        this.texturesNether = texturesNether;
        this.texturesEnd = texturesEnd;
        this.particle = particle;
        for (ResourceLocation loc : this.textures.values()) this.sprites.put(loc, null);
        for (ResourceLocation loc : this.texturesNether.values()) this.sprites.put(loc, null);
        for (ResourceLocation loc : this.texturesEnd.values()) this.sprites.put(loc, null);
        sprites.put(this.particle, null);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        for (Map.Entry<ResourceLocation, TextureAtlasSprite> entry : this.sprites.entrySet()) {
            entry.setValue(bakedTextureGetter.apply(entry.getKey()));
        }
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState rawState, EnumFacing side, long rand) {
        try {
            return this.cache.get(rawState).getQuads(rawState, side, rand);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ModelUtil.getMissingModel().getQuads(rawState, side, rand);
    }

    private IBakedModel generateModel(@Nullable IBlockState rawState) {
        PropertyHelper.DimensionalTextureInfo textureInfo = rawState != null ? ((IExtendedBlockState)rawState).getValue(PropertyHelper.TEXTURE_INDEX_PROPERTY) : null;
        HashMap<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<>();

        Arrays.stream(EnumFacing.VALUES).forEach(facing -> faceQuads.put(facing, Collections.singletonList(getQuad(new Vector3f(0,0,0), new Vector3f(16, facing == EnumFacing.DOWN ? 0 : 16,16), facing, getOreTexture(facing, textureInfo)))));

        return new SimpleBakedModel(Collections.emptyList(), faceQuads, true, true, null, ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE);
    }

    public TextureAtlasSprite getOreTexture(EnumFacing side, @Nullable PropertyHelper.DimensionalTextureInfo info) {
        if (info != null) {
            EnumFacing sideOverride = info.sideOverrides.get(side);
            if (sideOverride != null) {
                ResourceLocation texture;
                if (info.dimension == DimensionType.OVERWORLD) {
                    texture = this.textures.get(sideOverride);
                    if (texture == null) {
                        texture = this.texturesNether.get(sideOverride);
                        if (texture == null) {
                            texture = this.texturesEnd.get(sideOverride);
                        }
                    }
                } else if (info.dimension == DimensionType.NETHER) {
                    texture = this.texturesNether.get(sideOverride);
                    if (texture == null) {
                        texture = this.textures.get(sideOverride);
                        if (texture == null) {
                            texture = this.texturesEnd.get(sideOverride);
                        }
                    }
                } else {
                    texture = this.texturesEnd.get(sideOverride);
                    if (texture == null) {
                        texture = this.textures.get(sideOverride);
                        if (texture == null) {
                            texture = this.texturesNether.get(sideOverride);
                        }
                    }
                }
                return this.sprites.get(texture);
            } else return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/" + (info.dimension == DimensionType.OVERWORLD ? "stone" : info.dimension == DimensionType.NETHER ? "netherrack" : "end_stone"));
        } else return this.sprites.get(this.textures.get(null));
    }

    private BakedQuad getQuad(Vector3f from, Vector3f to, EnumFacing direction, TextureAtlasSprite sprite) {
        return bakery.makeBakedQuad(from, to, new BlockPartFace(direction.getOpposite(), 0, this.textures.get(direction).toString(), new BlockFaceUV(new float[]{0,0,16,16}, 0)), sprite, direction, ModelRotation.X0_Y0, null, true, true);
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.sprites.keySet();
    }

    @Override
    public void onReload() {}

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.sprites.get(this.particle);
    }
}
