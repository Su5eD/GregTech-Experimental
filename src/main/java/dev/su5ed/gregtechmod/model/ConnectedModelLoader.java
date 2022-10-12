package dev.su5ed.gregtechmod.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import one.util.streamex.StreamEx;

import java.util.Map;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class ConnectedModelLoader implements IGeometryLoader<ConnectedModelGeometry> {
    public static final ResourceLocation NAME = location("connected");

    @Override
    public ConnectedModelGeometry read(JsonObject json, JsonDeserializationContext deserializationContext) throws JsonParseException {
        String name = json.get("name").getAsString();
        Map<String, ResourceLocation> textures = StreamEx.of(ConnectedModel.TEXTURE_PARTS)
            .mapToEntry(part -> name + "_" + part)
            .prepend("", name)
            .mapValues(texture -> location("block", "connected", name, texture))
            .toImmutableMap();
        ResourceLocation particle = textures.get("");
        return new ConnectedModelGeometry(particle, textures);
    }
}
