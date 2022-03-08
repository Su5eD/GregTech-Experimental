package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityShelf;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModelShelf extends ModelTeBlock {
    private final Map<TileEntityShelf.Type, ResourceLocation> typeTextures;

    public ModelShelf(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures, Map<TileEntityShelf.Type, ResourceLocation> typeTextures) {
        super(particle, textures, typeTextures);
        
        this.typeTextures = typeTextures;
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        if (face == side) {
            TileEntityShelf.Type type = state.getValue(TileEntityShelf.SHELF_TYPE_PROPERTY);
            if (type != null) return this.sprites.get(this.typeTextures.get(type));
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
