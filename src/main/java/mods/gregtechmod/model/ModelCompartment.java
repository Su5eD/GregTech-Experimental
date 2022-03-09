package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityCompartment;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityShelf;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.IntStreamEx;

import java.util.List;
import java.util.Map;

public class ModelCompartment extends ModelTeBlock {
    private static final List<ResourceLocation> FACE_TEXTURES = IntStreamEx.range(16)
        .mapToObj(i -> new ResourceLocation(Reference.MODID, "blocks/machines/compartment/compartment_" + i))
        .toImmutableList();

    public ModelCompartment(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures) {
        super(particle, textures, FACE_TEXTURES);
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        if (face == side) {
            Integer faceIndex = state.getValue(TileEntityCompartment.FACE_TEXTURE_INDEX_PROPERTY);
            if (faceIndex != null) return this.sprites.get(FACE_TEXTURES.get(faceIndex));
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
