package mods.gregtechmod.model;

import mods.gregtechmod.core.GregTechMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class ModelCached<T> extends ModelBase {
    private final Block block;
    private Map<T, Map<EnumFacing, List<BakedQuad>>> cache;

    protected ModelCached(Block block, ResourceLocation particle, Collection<ResourceLocation> textures) {
        super(particle, textures);
        this.block = block;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        super.bake(state, format, bakedTextureGetter);
        if (this.block != null) {
            this.cache = StreamEx.of(this.block.getBlockState().getValidStates())
                .map(this::getModelKey)
                .toListAndThen(this::populateCache);
        }
        return this;
    }

    protected abstract Map<T, Map<EnumFacing, List<BakedQuad>>> populateCache(List<T> keys);
    
    protected abstract T getModelKey(IBlockState state);
    
    @Override
    public final List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (side != null) {
            if (this.cache != null) {
                T key = getModelKey(state);
                Map<EnumFacing, List<BakedQuad>> quads = this.cache.get(key);
                if (quads == null) {
                    GregTechMod.LOGGER.error("No cached model for blockstate {}, bug?", state);
                    throw new IllegalStateException("Can't find cached model for blockstate, see log for details");
                }
                return quads.get(side);
            }
            return getQuads(state, side);
        }
        return Collections.emptyList();
    }
}
