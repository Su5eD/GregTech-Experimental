package mods.gregtechmod.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.EntryStream;

import javax.annotation.Nullable;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class JsonHandler {
    public static final Gson GSON = new Gson();

    public final JsonObject json;
    public final ResourceLocation particle;
    private final LazyValue<JsonHandler> parent;

    public JsonHandler(String path) {
        this.json = readAssetJSON(path);
        this.parent = new LazyValue<>(() -> {
            String parentPath = new ResourceLocation(this.json.get("parent").getAsString()).getPath();
            return new JsonHandler("models/" + parentPath + ".json");
        });
        this.particle = getParticleTexture();
    }

    public static JsonObject readAssetJSON(String path) {
        try (Reader reader = GtUtil.readAsset(path)) {
            return GSON.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not find resource " + path, e);
        }
    }

    public static JsonObject readJSON(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return GSON.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not read json " + path, e);
        }
    }

    @Nullable
    public ResourceLocation getResouceLocationElement(String name) {
        JsonElement element = this.json.get(name);
        return element != null ? new ResourceLocation(element.getAsString()) : null;
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

        return map != null ? EntryStream.of(map.entrySet().iterator())
            .removeKeys("particle"::equals)
            .mapKeys(EnumFacing::byName)
            .mapValues(json -> new ResourceLocation(json.getAsString()))
            .toImmutableMap()
            : Collections.emptyMap();
    }

    private ResourceLocation getParticleTexture() {
        JsonObject textures = this.json.getAsJsonObject("textures");
        if (textures == null) textures = this.parent.get().json.getAsJsonObject("textures");

        JsonElement particle = textures.get("particle");
        return particle != null ? new ResourceLocation(particle.getAsString()) : null;
    }
}
