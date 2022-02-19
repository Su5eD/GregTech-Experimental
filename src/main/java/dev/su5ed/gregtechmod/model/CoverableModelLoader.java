package dev.su5ed.gregtechmod.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.util.ModelUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

import java.util.Map;

public class CoverableModelLoader implements IModelLoader<CoverableModelGeometry> {

    @Override
    public CoverableModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        JsonObject json = modelContents.getAsJsonObject("textures");
        ResourceLocation particle = new ResourceLocation(json.get("particle").getAsString());
        Map<Direction, ResourceLocation> textures = ModelUtil.generateTextureMap(json);
        
        return new CoverableModelGeometry(particle, textures);
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {}
}
