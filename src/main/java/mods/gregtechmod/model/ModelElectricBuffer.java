package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState;
import ic2.core.util.Util;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Map;

public class ModelElectricBuffer extends ModelTeBlock {
    private final Map<EnumFacing, ResourceLocation> redstoneTextures;
    private final ResourceLocation textureDown;
    private final ResourceLocation textureDownRedstone;

    public ModelElectricBuffer(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> redstoneTextures, 
                               ResourceLocation textureDown, ResourceLocation textureDownRedstone) {
        super(particle, Arrays.asList(textures, redstoneTextures));
        this.redstoneTextures = redstoneTextures;
        this.textureDown = textureDown;
        this.textureDownRedstone = textureDownRedstone;
        
        this.sprites.put(this.textureDown, null);
        this.sprites.put(this.textureDownRedstone, null);
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        boolean horizontal = Util.horizontalFacings.contains(side);
        
        if (state.getValue(TileEntityElectricBuffer.REDSTONE_TEXTURE_PROPERTY)) {
            return this.sprites.get(face == EnumFacing.UP && horizontal ? this.textureDownRedstone : this.redstoneTextures.get(rotatedSide));
        }
        else if (horizontal) {
            if (face == EnumFacing.UP) {
                return this.sprites.get(this.textureDown);
            }
            else if (face == EnumFacing.DOWN) {
                return super.getSprite(face, side, EnumFacing.DOWN, state);
            }
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
