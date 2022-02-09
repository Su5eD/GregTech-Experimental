package mods.gregtechmod.model;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.component.CoverHandler;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.PropertyHelper.VerticalRotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.EntryStream;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModelTeBlock extends ModelBase {
    private final Map<EnumFacing, ResourceLocation> textures;

    //Block face UVs in DUNSWE order
    public static final float[][] BLOCK_FACE_UVS = new float[][] {
        { 0, 16, 16, 0 },
        { 0, 0, 16, 16 },
        { 0, 0, 16, 16 },
        { 0, 0, 16, 16 },
        { 0, 0, 16, 16 },
        { 0, 0, 16, 16 }
    };

    public ModelTeBlock(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures) {
        this(particle, Collections.singletonList(textures));
    }

    public ModelTeBlock(ResourceLocation particle, List<Map<EnumFacing, ResourceLocation>> textures) {
        super(particle, textures);
        this.textures = textures.get(0);
    }

    @Override
    protected List<BakedQuad> getQuads(IBlockState rawState, EnumFacing side) {
        Map<EnumFacing, ResourceLocation> covers;
        EnumFacing face = rawState.getValue(BlockTileEntity.facingProperty);
        Ic2BlockStateInstance state = (Ic2BlockStateInstance) rawState;

        if (state.hasValue(CoverHandler.COVER_HANDLER_PROPERTY)) {
            CoverHandler handler = state.getValue(CoverHandler.COVER_HANDLER_PROPERTY);
            covers = EntryStream.of(handler.covers)
                .mapValues(ICover::getIcon)
                .toImmutableMap();
        }
        else covers = Collections.emptyMap();

        VerticalRotation verticalRotation = getVerticalRotation(state);
        EnumFacing rotatedSide = rotateSide(verticalRotation, face, side, covers);
        TextureAtlasSprite sprite = getSpriteFromDirection(face, side, rotatedSide, state, covers);
        BlockFaceUV faceUV = new BlockFaceUV(BLOCK_FACE_UVS[side.getIndex()], getTextureRotation(side, face));
        BakedQuad quad = BAKERY.makeBakedQuad(ZERO, side == EnumFacing.DOWN ? MAX_DOWN : MAX, new BlockPartFace(side, 0, this.textures.get(side).toString(), faceUV), sprite, side, ModelRotation.X0_Y0, null, true, true);
        return Collections.singletonList(quad);
    }

    private static int getTextureRotation(EnumFacing side, EnumFacing facing) {
        if (Util.verticalFacings.contains(side)) {
            if (facing == EnumFacing.NORTH) return 180;
            else if (facing == EnumFacing.WEST) return side == EnumFacing.UP ? -90 : 90;
            else if (facing == EnumFacing.EAST) return side == EnumFacing.UP ? 90 : -90;
        }
        return 0;
    }

    protected VerticalRotation getVerticalRotation(Ic2BlockStateInstance state) {
        return state.hasValue(PropertyHelper.VERTICAL_ROTATION_PROPERTY) ? state.getValue(PropertyHelper.VERTICAL_ROTATION_PROPERTY) : VerticalRotation.MIRROR_BACK;
    }

    /**
     * @param side        the side to get the texture for
     * @param rotatedSide the current side relative to the block's facing
     * @return the side's texture depending on the block's facing
     */
    private TextureAtlasSprite getSpriteFromDirection(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockStateInstance state, Map<EnumFacing, ResourceLocation> covers) {
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();

        if (covers.containsKey(side)) return map.getAtlasSprite(covers.get(rotatedSide).toString());

        PropertyHelper.AnimationSpeed prop = state.getValue(PropertyHelper.ANIMATION_SPEED_PROPERTY);
        if (prop != null && prop.getSides().contains(rotatedSide) && prop.getValue() > 1) {
            return map.getAtlasSprite(this.textures.get(rotatedSide).toString() + prop.getValue());
        }

        PropertyHelper.TextureOverride overrides = state.getValue(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY);
        if (overrides != null) {
            EnumFacing actualSide = overrides.isAbsolute() ? side : rotatedSide;
            if (overrides.hasOverride(actualSide)) return map.getAtlasSprite(overrides.getTextureOverride(actualSide).toString());
        }

        EnumFacing outputSide = state.getValue(PropertyHelper.OUTPUT_SIDE_PROPERTY);
        if (outputSide != null && outputSide == side) {
            String textureName = side == EnumFacing.DOWN ? "bottom" : side == EnumFacing.UP ? "top" : "side";
            return map.getAtlasSprite(String.format("%s:blocks/machines/machine_%s_pipe", Reference.MODID, textureName));
        }

        return getSprite(face, side, rotatedSide, state);
    }

    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockStateInstance state) {
        return this.sprites.get(this.textures.get(rotatedSide));
    }

    private static EnumFacing rotateSide(VerticalRotation behavior, EnumFacing face, EnumFacing side, Map<EnumFacing, ResourceLocation> covers) {
        if (!covers.containsKey(side) && face != EnumFacing.NORTH) {
            if (face == side) return EnumFacing.NORTH;
            else if (Util.verticalFacings.contains(face)) {
                return behavior.rotation.apply(face, side);
            }
            else if (Util.horizontalFacings.contains(side)) {
                if (face == EnumFacing.SOUTH) return side.getOpposite();
                else return face.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? side.rotateY().getOpposite() : side.rotateY();
            }
        }

        return side;
    }
}
