package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityButtonPanel;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;

import java.util.Map;

public class ModelButtonPanel extends ModelTeBlock {
    private static final String TEXTURE_LOCATION = "blocks/machines/button_panel/";

    private static final Int2ObjectOpenHashMap<ResourceLocation> TEXTURES_SMALL = getTextureLocations("small", 16);
    private static final Int2ObjectOpenHashMap<ResourceLocation> TEXTURES_LARGE = getTextureLocations("large", 5);
    private static final Int2ObjectOpenHashMap<ResourceLocation> TEXTURES_ROW = getTextureLocations("row", 5);

    public ModelButtonPanel(ResourceLocation particle, Map<EnumFacing, ResourceLocation> textures) {
        super(particle, textures);
        EntryStream.of(TEXTURES_SMALL)
            .append(TEXTURES_LARGE)
            .append(TEXTURES_ROW)
            .values()
            .forEach(location -> this.sprites.put(location, null));
    }

    private static Int2ObjectOpenHashMap<ResourceLocation> getTextureLocations(String name, int count) {
        return IntStreamEx.range(count)
            .mapToEntry(i -> i, i -> new ResourceLocation(Reference.MODID, TEXTURE_LOCATION + name + "/button_" + name + "_" + i))
            .toCustomMap(Int2ObjectOpenHashMap::new);
    }

    @Override
    protected TextureAtlasSprite getSprite(EnumFacing face, EnumFacing side, EnumFacing rotatedSide, Ic2BlockState.Ic2BlockStateInstance state) {
        if (face == side && state.hasValue(PropertyHelper.BUTTON_PANEL_INFO_PROPERTY)) {
            PropertyHelper.ButtonPanelInfo info = state.getValue(PropertyHelper.BUTTON_PANEL_INFO_PROPERTY);
            Int2ObjectOpenHashMap<ResourceLocation> map = info.mode == TileEntityButtonPanel.PanelMode.SMALL ? TEXTURES_SMALL : info.mode == TileEntityButtonPanel.PanelMode.LARGE ? TEXTURES_LARGE : TEXTURES_ROW;
            ResourceLocation location = map.get(info.index);
            return this.sprites.get(location);
        }
        return super.getSprite(face, side, rotatedSide, state);
    }
}
