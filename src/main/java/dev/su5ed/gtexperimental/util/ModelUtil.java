package dev.su5ed.gtexperimental.util;

import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import one.util.streamex.EntryStream;

import java.util.Map;

public final class ModelUtil {

    private ModelUtil() {}

    public static Map<Direction, ResourceLocation> generateTextureMap(JsonObject json) {
        return json != null ? EntryStream.of(json.entrySet().iterator())
            .withoutKeys("particle")
            .mapKeys(Direction::byName)
            .mapValues(element -> new ResourceLocation(element.getAsString()))
            .toImmutableMap()
            : Map.of();
    }
}
