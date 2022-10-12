package dev.su5ed.gregtechmod.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.su5ed.gregtechmod.util.ModelUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.Map;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class OreModelLoader implements IGeometryLoader<OreModelGeometry> {
    public static final ResourceLocation NAME = location("ore");

    @Override
    public OreModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject json = jsonObject.getAsJsonObject("textures");
        ResourceLocation particle = new ResourceLocation(json.get("particle").getAsString());
        Map<Direction, ResourceLocation> textures = ModelUtil.generateTextureMap(json);
        Map<Direction, ResourceLocation> texturesNether = generateTextureMap(jsonObject, "textures_nether");
        Map<Direction, ResourceLocation> texturesEnd = generateTextureMap(jsonObject, "textures_end");
        return new OreModelGeometry(particle, textures, texturesNether, texturesEnd);
    }

    public Map<Direction, ResourceLocation> generateTextureMap(JsonObject json, String elementName) {
        return ModelUtil.generateTextureMap(json.getAsJsonObject(elementName));
    }
}
