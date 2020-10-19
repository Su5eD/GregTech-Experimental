package mods.gregtechmod.objects.blocks;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ic2.core.model.AbstractModel;
import ic2.core.model.BasicBakedBlockModel;
import ic2.core.model.ModelUtil;
import ic2.core.model.VdUtil;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static mods.gregtechmod.cover.RenderTeBlock.generateQuad;

@SideOnly(Side.CLIENT)
public class RenderBlockOre extends AbstractModel {
    private final Map<EnumFacing, ResourceLocation> textures;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites = new HashMap<>();
    private final ResourceLocation particle;
    public LoadingCache<IBlockState, IBakedModel> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .build(new CacheLoader<IBlockState, IBakedModel>() {
                public IBakedModel load(IBlockState key) {
                    return generateModel(key);
                }
            });

    public RenderBlockOre(HashMap<EnumFacing, ResourceLocation> map, ResourceLocation particle) {
        this.textures = map;
        this.particle = particle;
        for (ResourceLocation loc : this.textures.values()) {
            this.sprites.put(loc, null);
        }
        sprites.put(this.particle, null);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        for (Map.Entry<ResourceLocation, TextureAtlasSprite> entry : this.sprites.entrySet()) {
            entry.setValue(bakedTextureGetter.apply(entry.getKey()));
        }
        return this;
    }

    public List<BakedQuad> getQuads(IBlockState rawState, EnumFacing side, long rand) {
        try {
            if (rawState == null) return ModelUtil.getMissingModel().getQuads(rawState, side, rand);
            return cache.get(rawState).getQuads(rawState, side, rand);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ModelUtil.getBlockModel(rawState).getQuads(rawState, side, rand);
    }

    private IBakedModel generateModel(IBlockState rawState) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) rawState;
        Integer[] textureIndexes = extendedBlockState.getValue(PropertyHelper.TEXTURE_INDEX_PROPERTY);
        float th = 1;
        float sp = 0;
        List<BakedQuad>[] arrayOfList = new List[EnumFacing.VALUES.length];
        for (int i = 0; i < arrayOfList.length; i++) arrayOfList[i] = new ArrayList<>();
        List<BakedQuad> generalQuads = new ArrayList<>();
        for (EnumFacing side : EnumFacing.VALUES) {
            TextureAtlasSprite sprite;
            if (textureIndexes != null) {
                if (textureIndexes[side.getIndex()] > -1) sprite = this.sprites.get(this.textures.get(EnumFacing.byIndex(textureIndexes[side.getIndex()])));
                else sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
            } else sprite = this.sprites.get(this.textures.get(null));
            float zE = sp + th;
            IntBuffer buffer = VdUtil.getQuadBuffer();
            generateQuad(sp, sp, sp, zE, zE, zE, side, sprite, buffer);
            BakedQuad quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, side, sprite, false, VdUtil.vertexFormat);
            arrayOfList[side.ordinal()].add(quad);
            generalQuads.add(quad);
            buffer.rewind();
        }
        int used = 0;
        for (int j = 0; j < arrayOfList.length; j++) {
            if (arrayOfList[j].isEmpty()) {
                arrayOfList[j] = Collections.emptyList();
            } else {
                used++;
            }
        }
        if (used == 0) arrayOfList = null;
        if (generalQuads.isEmpty()) generalQuads = Collections.emptyList();
        return new BasicBakedBlockModel(arrayOfList, generalQuads, this.sprites.get(this.particle));
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
