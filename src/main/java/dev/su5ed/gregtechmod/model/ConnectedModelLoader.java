package dev.su5ed.gregtechmod.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import one.util.streamex.StreamEx;

import java.util.Map;

public class ConnectedModelLoader implements IModelLoader<ConnectedModelGeometry> {
    public static final ResourceLocation LOCATION = new ResourceLocation(Reference.MODID, "connected_model_loader");
    
    private final Map<String, ResourceLocation> textures;
    private final ResourceLocation particle;
    
    public ConnectedModelLoader(String name) {
        this.textures = StreamEx.of(ConnectedModel.TEXTURE_PARTS)
            .mapToEntry(part -> name + "_" + part)
            .prepend("", name)
            .mapValues(texture -> new ResourceLocation(Reference.MODID, "block/connected/" + name + "/" + texture))
            .toImmutableMap();
        this.particle = this.textures.get("");
    }

    @Override
    public ConnectedModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new ConnectedModelGeometry(this.particle, this.textures);
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {}
}
