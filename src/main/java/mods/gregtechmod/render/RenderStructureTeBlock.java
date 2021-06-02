package mods.gregtechmod.render;

import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityStructureBase;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RenderStructureTeBlock extends RenderTeBlock {
    private final Map<EnumFacing, ResourceLocation> validTextures;
    private final Map<ResourceLocation, TextureAtlasSprite> validSprites;

    public RenderStructureTeBlock(Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> validTextures, ResourceLocation particle) {
        super(textures, particle);
        this.validTextures = validTextures;
        
        this.validSprites = new HashMap<>();
        for (ResourceLocation loc : validTextures.values()) validSprites.put(loc, null);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.validSprites.entrySet()
                .forEach(entry -> entry.setValue(bakedTextureGetter.apply(entry.getKey())));
        return super.bake(state, format, bakedTextureGetter);
    }

    @Override
    protected TextureAtlasSprite getSpriteFromDirection(EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state, Map<EnumFacing, ResourceLocation> covers) {
        TextureAtlasSprite ret = super.getSpriteFromDirection(side, rotatedSide, state, covers);
        if (state.hasValue(TileEntityStructureBase.PROPERTY_VALID) && state.getValue(TileEntityStructureBase.PROPERTY_VALID) && validTextures.containsKey(side)) {
            return this.validSprites.get(this.validTextures.get(rotatedSide));
        }
        return ret;
    }
}
