package mods.gregtechmod.model;

import mods.gregtechmod.util.LazyValue;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.property.IExtendedBlockState;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModelBlockOre extends ModelBase {
    private final LazyValue<TextureMap> textureMap = new LazyValue<>(() -> Minecraft.getMinecraft().getTextureMapBlocks());

    private final Map<EnumFacing, ResourceLocation> textures;
    private final Map<EnumFacing, ResourceLocation> texturesNether;
    private final Map<EnumFacing, ResourceLocation> texturesEnd;

    public ModelBlockOre(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures, Map<EnumFacing, ResourceLocation> texturesNether, Map<EnumFacing, ResourceLocation> texturesEnd) {
        super(particle, StreamEx.of(textures, texturesNether, texturesEnd));
        this.textures = textures;
        this.texturesNether = texturesNether;
        this.texturesEnd = texturesEnd;
    }

    @Override
    protected List<BakedQuad> getQuads(IBlockState state, EnumFacing side) {
        PropertyHelper.DimensionalTextureInfo textureInfo = state != null ? ((IExtendedBlockState) state).getValue(PropertyHelper.TEXTURE_INDEX_PROPERTY) : null;

        BlockPartFace face = new BlockPartFace(side.getOpposite(), 0, this.textures.get(side).toString(), FACE_UV);
        BakedQuad quad = BAKERY.makeBakedQuad(ModelBase.ZERO, side == EnumFacing.DOWN ? MAX_DOWN : MAX, face, getOreTexture(side, textureInfo), side, ModelRotation.X0_Y0, null, true, true);
        return Collections.singletonList(quad);
    }

    public TextureAtlasSprite getOreTexture(EnumFacing side, @Nullable PropertyHelper.DimensionalTextureInfo info) {
        if (info != null) {
            EnumFacing sideOverride = info.sideOverrides.get(side);
            if (sideOverride != null) {
                ResourceLocation texture;
                if (info.dimension == DimensionType.OVERWORLD) {
                    texture = getTextureWithFallback(sideOverride, this.textures, this.texturesNether, this.texturesEnd);
                }
                else if (info.dimension == DimensionType.NETHER) {
                    texture = getTextureWithFallback(sideOverride, this.texturesNether, this.textures, this.texturesEnd);
                }
                else {
                    texture = getTextureWithFallback(sideOverride, this.texturesEnd, this.textures, this.texturesNether);
                }
                return this.sprites.get(texture);
            }
            return this.textureMap.get().getAtlasSprite("minecraft:blocks/" + (info.dimension == DimensionType.OVERWORLD ? "stone" : info.dimension == DimensionType.NETHER ? "netherrack" : "end_stone"));
        }
        return this.sprites.get(this.textures.get(null));
    }

    @SafeVarargs
    private final ResourceLocation getTextureWithFallback(EnumFacing side, Map<EnumFacing, ResourceLocation>... textures) {
        return StreamEx.of(textures)
            .map(map -> map.get(side))
            .nonNull()
            .findFirst()
            .orElse(null);
    }
}
