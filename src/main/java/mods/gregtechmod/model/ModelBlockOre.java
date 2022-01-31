package mods.gregtechmod.model;

import mods.gregtechmod.util.LazyValue;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.*;

public class ModelBlockOre extends ModelBase {
    private final LazyValue<TextureMap> textureMap = new LazyValue<>(() -> Minecraft.getMinecraft().getTextureMapBlocks());
    
    private final Map<EnumFacing, ResourceLocation> textures;
    private final Map<EnumFacing, ResourceLocation> texturesNether;
    private final Map<EnumFacing, ResourceLocation> texturesEnd;

    public ModelBlockOre(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> texturesNether, Map<EnumFacing, ResourceLocation> texturesEnd) {
        super(particle, Arrays.asList(textures, texturesNether, texturesEnd), false);
        this.textures = textures;
        this.texturesNether = texturesNether;
        this.texturesEnd = texturesEnd;
    }

    @Override
    public IBakedModel generateModel(IBlockState rawState) {
        PropertyHelper.DimensionalTextureInfo textureInfo = rawState != null ? ((IExtendedBlockState)rawState).getValue(PropertyHelper.TEXTURE_INDEX_PROPERTY) : null;
        Map<EnumFacing, List<BakedQuad>> faceQuads = new HashMap<>();

        Arrays.stream(EnumFacing.VALUES).forEach(facing -> faceQuads.put(facing, Collections.singletonList(getQuad(new Vector3f(0,0,0), new Vector3f(16, facing == EnumFacing.DOWN ? 0 : 16,16), facing, getOreTexture(facing, textureInfo)))));

        return new SimpleBakedModel(Collections.emptyList(), faceQuads, true, true, getParticleTexture(), ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE);
    }

    public TextureAtlasSprite getOreTexture(EnumFacing side, @Nullable PropertyHelper.DimensionalTextureInfo info) {
        if (info != null) {
            EnumFacing sideOverride = info.sideOverrides.get(side);
            if (sideOverride != null) {
                ResourceLocation texture;
                if (info.dimension == DimensionType.OVERWORLD) {
                    texture = getTextureWithFallback(sideOverride, this.textures, this.texturesNether, this.texturesEnd);
                } else if (info.dimension == DimensionType.NETHER) {
                    texture = getTextureWithFallback(sideOverride, this.texturesNether, this.textures, this.texturesEnd);
                } else {
                    texture = getTextureWithFallback(sideOverride, this.texturesEnd, this.textures, this.texturesNether);
                }
                return this.sprites.get(texture);
            } else {
                return this.textureMap.get().getAtlasSprite("minecraft:blocks/" + (info.dimension == DimensionType.OVERWORLD ? "stone" : info.dimension == DimensionType.NETHER ? "netherrack" : "end_stone"));
            }
        } else return this.sprites.get(this.textures.get(null));
    }
    
    private ResourceLocation getTextureWithFallback(EnumFacing side, Map<EnumFacing, ResourceLocation> primary, Map<EnumFacing, ResourceLocation> secondary, Map<EnumFacing, ResourceLocation> tertiary) {
        ResourceLocation loc = primary.get(side);
        if (loc == null) loc = secondary.get(side);
        if (loc == null) loc = tertiary.get(side);
        return loc;
    }

    private BakedQuad getQuad(Vector3f from, Vector3f to, EnumFacing direction, TextureAtlasSprite sprite) {
        return bakery.makeBakedQuad(from, to, new BlockPartFace(direction.getOpposite(), 0, this.textures.get(direction).toString(), new BlockFaceUV(new float[]{ 0, 0, 16, 16 }, 0)), sprite, direction, ModelRotation.X0_Y0, null, true, true);
    }
}
