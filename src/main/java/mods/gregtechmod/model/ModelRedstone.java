package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferSmall;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Map;

public class ModelRedstone extends ModelTeBlock {
    private final Map<EnumFacing, ResourceLocation> redstoneTextures;

    public ModelRedstone(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> redstoneTextures) {
        super(particle, Arrays.asList(textures, redstoneTextures));
        this.redstoneTextures = redstoneTextures;
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        if (state.getValue(TileEntityElectricBufferSmall.REDSTONE_TEXTURE_PROPERTY)) {
            return this.sprites.get(this.redstoneTextures.get(side));
        }
        return super.getSprite(side, rotatedSide, state);
    }
}
