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

public class CoverableModelLoader implements IGeometryLoader<CoverableModelGeometry> {
    public static final ResourceLocation NAME = location("coverable");

    @Override
    public CoverableModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject json = jsonObject.getAsJsonObject("textures");
        ResourceLocation particle = new ResourceLocation(json.get("particle").getAsString());
        Map<Direction, ResourceLocation> textures = ModelUtil.generateTextureMap(json);

        return new CoverableModelGeometry(particle, textures);
    }
}
