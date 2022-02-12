package mods.gregtechmod.model;

import ic2.core.model.AbstractModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import one.util.streamex.StreamEx;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;
import java.util.function.Function;

public abstract class ModelBase extends AbstractModel {
    protected static final FaceBakery BAKERY = new FaceBakery();
    protected static final Vector3f ZERO = new Vector3f(0, 0, 0);
    protected static final Vector3f MAX = new Vector3f(16, 16, 16);
    protected static final Vector3f MAX_DOWN = new Vector3f(16, 0, 16);
    protected static final BlockFaceUV FACE_UV = new BlockFaceUV(new float[] { 0, 0, 16, 16 }, 0);

    protected final Map<ResourceLocation, TextureAtlasSprite> sprites = new HashMap<>();
    protected final ResourceLocation particle;

    protected ModelBase(ResourceLocation particle, List<Map<EnumFacing, ResourceLocation>> textures) {
        this(particle, StreamEx.of(textures)
            .map(Map::values)
            .flatMap(Collection::stream)
            .toImmutableList());
    }

    protected ModelBase(ResourceLocation particle, Collection<ResourceLocation> textures) {
        this.particle = Objects.requireNonNull(particle);
        textures.forEach(loc -> this.sprites.put(loc, null));
        this.sprites.put(particle, null);
    }

    protected abstract List<BakedQuad> getQuads(IBlockState state, EnumFacing side);

    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.sprites.keySet();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.sprites.entrySet()
            .forEach(entry -> entry.setValue(bakedTextureGetter.apply(entry.getKey())));
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (side != null) {
            return getQuads(state, side);
        }
        return Collections.emptyList();
    }

    @Override
    public void onReload() {}

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.sprites.get(this.particle);
    }
}
