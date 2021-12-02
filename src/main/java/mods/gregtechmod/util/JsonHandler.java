package mods.gregtechmod.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class JsonHandler {
    private static final Gson GSON = new Gson();
    
    public final JsonObject json;
    public final ResourceLocation particle;
    private final LazyValue<JsonHandler> parent;

    public JsonHandler(String path) {
        this.json = readJSON(path);
        this.parent = new LazyValue<>(() -> {
            String parentPath = new ResourceLocation(this.json.get("parent").getAsString()).getPath();
            return new JsonHandler("models/" + parentPath + ".json");
        });
        this.particle = getParticleTexture();
    }

    public static JsonObject readJSON(String path) {
        try(Reader reader = GtUtil.readAsset(path)) {
            return GSON.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not find resource " + path, e);
        }
    }
    
    public Map<EnumFacing, ResourceLocation> generateTextureMap() {
        Map<EnumFacing, ResourceLocation> map = generateTextureMap("textures");
        if (map.isEmpty()) {
            String parentPath = new ResourceLocation(this.json.get("parent").getAsString()).getPath();
            JsonHandler parent = new JsonHandler("models/" + parentPath + ".json");
            return parent.generateTextureMap("textures");
        }
        return map;
    }
    
    public Map<EnumFacing, ResourceLocation> generateTextureMap(String elementName) {
        JsonObject map = this.json.getAsJsonObject(elementName);
        Map<EnumFacing, ResourceLocation> textures = new HashMap<>();
        
        if (map != null) {
            map.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals("particle"))
                    .forEach(entry -> {
                        ResourceLocation location = new ResourceLocation(entry.getValue().getAsString());
                        textures.put(EnumFacing.byName(entry.getKey()), location);
                    });
        }
        
        return textures;
    }
    
    private ResourceLocation getParticleTexture() {
        JsonObject textures = this.json.getAsJsonObject("textures");
        if (textures == null) textures = this.parent.get().json.getAsJsonObject("textures");
        
        return new ResourceLocation(textures.get("particle").getAsString());
    }
}
