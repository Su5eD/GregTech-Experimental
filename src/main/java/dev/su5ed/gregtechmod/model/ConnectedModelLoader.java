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
    private final Map<String, ResourceLocation> textures;
    private final ResourceLocation particle;
    
    public ConnectedModelLoader(String name) {
        this.textures = StreamEx.of(ConnectedModel.TEXTURE_PARTS)
            .mapToEntry(part -> name + "_" + part)
            .prepend("", name)
            .mapValues(texture -> location("block", "connected", name, texture))
            .toImmutableMap();
        this.particle = this.textures.get("");
    }

    @Override
    public ConnectedModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new ConnectedModelGeometry(this.particle, this.textures);
    }
}