package mods.gregtechmod.model;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.component.CoverHandler;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

import java.util.Collections;
import java.util.HashMap;
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
    protected IBakedModel generateModel(IBlockState rawState) {
        Map<EnumFacing, ResourceLocation> covers = new HashMap<>();
        EnumFacing face = rawState.getValue(BlockTileEntity.facingProperty);
        Ic2BlockState.Ic2BlockStateInstance state = (Ic2BlockState.Ic2BlockStateInstance)rawState;
        Map<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<>();

        if (state.hasValue(CoverHandler.COVER_HANDLER_PROPERTY)) {
            CoverHandler handler = state.getValue(CoverHandler.COVER_HANDLER_PROPERTY);
            for (Map.Entry<EnumFacing, ICover> entry : handler.covers.entrySet()) {
                EnumFacing side = entry.getKey();
                covers.put(side, handler.covers.get(side).getIcon());
            }
        }

        for (EnumFacing side : EnumFacing.VALUES) {
            TextureAtlasSprite sprite = getSpriteFromDirection(side, rotateSide(face, side, covers), state, covers);
            faceQuads.put(side, Collections.singletonList(getQuad(new Vector3f(0,0,0), new Vector3f(16, side == EnumFacing.DOWN ? 0 : 16,16), side, face, sprite)));
        }

        return new SimpleBakedModel(Collections.emptyList(), faceQuads, true, true, getParticleTexture(), ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE);
    }

    private BakedQuad getQuad(Vector3f from, Vector3f to, EnumFacing direction, EnumFacing facing, TextureAtlasSprite sprite) {
        return bakery.makeBakedQuad(from, to, new BlockPartFace(direction, 0, this.textures.get(direction).toString(), new BlockFaceUV(BLOCK_FACE_UVS[direction.getIndex()], getRotation(direction, facing))), sprite, direction, ModelRotation.X0_Y0, null, true, true);
    }
    
    private static int getRotation(EnumFacing side, EnumFacing facing) {
        if (Util.verticalFacings.contains(side)) {
            if (facing == EnumFacing.NORTH) return 180;
            else if (facing == EnumFacing.WEST) return side == EnumFacing.UP ? -90 : 90;
            else if (facing == EnumFacing.EAST) return side == EnumFacing.UP ? 90 : -90;
        }
        return 0;
    }

    /**
     * @param side the side to get the texture for
     * @param rotatedSide the current side relative to the block's facing
     * @return the side's texture depending on the block's facing
     */
    private TextureAtlasSprite getSpriteFromDirection(EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state, Map<EnumFacing, ResourceLocation> covers) {
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        PropertyHelper.AnimationSpeed prop;
        PropertyHelper.TextureOverride overrides;

        if (covers.containsKey(side)) return map.getAtlasSprite(covers.get(rotatedSide).toString());
        else if ((prop = state.getValue(PropertyHelper.ANIMATION_SPEED_PROPERTY)) != null && prop.getSides().contains(rotatedSide) && prop.getValue() > 1) {
            return map.getAtlasSprite(textures.get(rotatedSide).toString() + prop.getValue());
        }
        else if ((overrides = state.getValue(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY)) != null && overrides.hasOverride(rotatedSide)) {
            return map.getAtlasSprite(overrides.getTextureOverride(rotatedSide).toString());
        }
        else if (state.hasValue(PropertyHelper.OUTPUT_SIDE_PROPERTY) && side == state.getValue(PropertyHelper.OUTPUT_SIDE_PROPERTY)) {
            String textureName = side == EnumFacing.DOWN ? "bottom" : side == EnumFacing.UP ? "top" : "side";
            return map.getAtlasSprite(String.format("%s:blocks/machines/machine_%s_pipe", Reference.MODID, textureName));
        }
        else return getSprite(side, rotatedSide, state);
    }
    
    protected TextureAtlasSprite getSprite(EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        return sprites.get(textures.get(rotatedSide));
    }
    
    private static EnumFacing rotateSide(EnumFacing face, EnumFacing side, Map<EnumFacing, ResourceLocation> covers) {
        if (!covers.containsKey(side) && face != EnumFacing.NORTH) {
            if (face == side) return EnumFacing.NORTH;
            else if (Util.verticalFacings.contains(face)) {
                if (side == EnumFacing.NORTH) return EnumFacing.SOUTH;
            }
            else if (!Util.verticalFacings.contains(side)) {
                if (face == EnumFacing.SOUTH) return side.getOpposite();
                else return side.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? side.rotateY() : side.rotateY().getOpposite();
            }
        }
        
        return side;
    }
}
