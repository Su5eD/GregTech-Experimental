package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityMachineBox;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.StreamEx;

import java.util.Map;

public class ModelMachineBox extends ModelTeBlock {
    private final Map<Integer, Map<EnumFacing, ResourceLocation>> tierTextures;

    public ModelMachineBox(ResourceLocation particle, Map<EnumFacing, ResourceLocation> mainTextures, Map<Integer, Map<EnumFacing, ResourceLocation>> tierTextures) {
        super(particle, mainTextures, StreamEx.ofValues(tierTextures).flatMap(m -> m.values().stream()).toSet());
        
        this.tierTextures = tierTextures;
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockStateInstance state) {
        Integer tier = state.getValue(TileEntityMachineBox.MACHINE_TIER_PROPERTY);
        if (tier != null && tier > 1) {
            ResourceLocation texture = this.tierTextures.get(tier).get(rotatedSide);
            return this.sprites.get(texture);
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
