package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.ITextureMode;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.Map;
import java.util.Set;

public class ModelTextureMode extends ModelTeBlock {
    private final Map<? extends ITextureMode, Int2ObjectOpenHashMap<ResourceLocation>> textures;

    public ModelTextureMode(String name, String basePath, Set<? extends ITextureMode> modes, ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures) {
        super(particle, textures);
        
        this.textures = StreamEx.of(modes)
            .mapToEntry(mode -> getTextureLocations(name, basePath, mode.getName(), mode.getCount()))
            .peekValues(map -> map.values().forEach(location -> this.sprites.put(location, null)))
            .toImmutableMap();
    }

    protected static Int2ObjectOpenHashMap<ResourceLocation> getTextureLocations(String teName, String basePath, String name, int count) {
        String path = basePath + teName + "/";
        return IntStreamEx.range(count)
            .mapToEntry(i -> i, i -> new ResourceLocation(Reference.MODID, path + name + "/" + teName + "_" + name + "_" + i))
            .toCustomMap(Int2ObjectOpenHashMap::new);
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        if (face == side && state.hasValue(PropertyHelper.TEXTURE_MODE_INFO_PROPERTY)) {
            PropertyHelper.TextureModeInfo info = state.getValue(PropertyHelper.TEXTURE_MODE_INFO_PROPERTY);
            Int2ObjectOpenHashMap<ResourceLocation> map = this.textures.get(info.mode);
            if (map == null) throw new IllegalArgumentException("Texture map not found for mode " + info.mode);
            ResourceLocation location = map.get(info.index);
            return this.sprites.get(location);
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
