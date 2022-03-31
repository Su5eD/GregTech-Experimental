package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityStructureBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModelStructureTeBlock extends ModelTeBlock {
    private final Map<EnumFacing, ResourceLocation> validTextures;

    public ModelStructureTeBlock(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> validTextures) {
        super(particle, textures, validTextures);
        this.validTextures = validTextures;
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockStateInstance state) {
        if (state.hasValue(TileEntityStructureBase.PROPERTY_VALID) && state.getValue(TileEntityStructureBase.PROPERTY_VALID) && this.validTextures.containsKey(rotatedSide)) {
            return this.sprites.get(this.validTextures.get(rotatedSide));
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
