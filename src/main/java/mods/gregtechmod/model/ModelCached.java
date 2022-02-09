package mods.gregtechmod.model;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class ModelCached extends ModelBase {
    private final Block block;
    private Map<IBlockState, Map<EnumFacing, List<BakedQuad>>> cache;

    public ModelCached(Block block, ResourceLocation particle, Collection<ResourceLocation> textures) {
        super(particle, textures);
        this.block = block;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        super.bake(state, format, bakedTextureGetter);
        if (this.block != null) this.cache = populateCache(this.block.getBlockState().getValidStates());
        return this;
    }

    protected abstract Map<IBlockState, Map<EnumFacing, List<BakedQuad>>> populateCache(List<IBlockState> states);
    
    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (side != null) {
            return this.cache != null ? this.cache.get(state).get(side) : getQuads(state, side);
        }
        return Collections.emptyList();
    }
}
