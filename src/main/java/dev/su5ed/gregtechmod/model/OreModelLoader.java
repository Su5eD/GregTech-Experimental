package dev.su5ed.gregtechmod.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import one.util.streamex.EntryStream;

import java.util.Map;

public class OreModelLoader implements IModelLoader<OreModeGeometry> {

    @Override
    public OreModeGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        JsonObject json = modelContents.getAsJsonObject("textures");
        ResourceLocation particle = new ResourceLocation(json.get("particle").getAsString());
        Map<Direction, ResourceLocation> textures = generateTextureMap(json);
        Map<Direction, ResourceLocation> texturesNether = generateTextureMap(modelContents, "textures_nether");
        Map<Direction, ResourceLocation> texturesEnd = generateTextureMap(modelContents, "textures_end");
        return new OreModeGeometry(particle, textures, texturesNether, texturesEnd);
    }

    public Map<Direction, ResourceLocation> generateTextureMap(JsonObject json, String elementName) {
        return generateTextureMap(json.getAsJsonObject(elementName));
    }

    public Map<Direction, ResourceLocation> generateTextureMap(JsonObject json) {
        return json != null ? EntryStream.of(json.entrySet().iterator())
            .withoutKeys("particle")
            .mapKeys(Direction::byName)
            .mapValues(element -> new ResourceLocation(element.getAsString()))
            .toImmutableMap()
            : Map.of();
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {}
}
