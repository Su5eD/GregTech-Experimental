package mods.gregtechmod.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.Reader;
import java.util.HashMap;

public class JsonHandler {
    public final HashMap<String, LinkedTreeMap<String, String>> jsonMap;
    public final ResourceLocation particle;

    public JsonHandler(String name, String path) {
        this.jsonMap = readFromJSON(path+"/"+name);
        this.particle = new ResourceLocation(Reference.MODID, jsonMap.get("textures").get("particle").substring(Reference.MODID.length()+1));
    }

    public HashMap<String, LinkedTreeMap<String, String>> readFromJSON(String name) {
        try {
            Gson gson = new Gson();
            Reader reader = GtUtil.readAsset("models/item/" + name + ".json");
            HashMap<String, LinkedTreeMap<String, String>> map = gson.<HashMap<String, LinkedTreeMap<String, String>>>fromJson(reader, HashMap.class);

            reader.close();
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public HashMap<EnumFacing, ResourceLocation> generateMapFromJSON(String elementName) {
        HashMap<EnumFacing, ResourceLocation> elementMap = new HashMap<>();
        LinkedTreeMap<String, String> map = this.jsonMap.get(elementName);
        if (map != null) {
            for (String entry : map.keySet()) {
                if (!entry.equals("particle")) {
                    ResourceLocation location = new ResourceLocation(Reference.MODID, map.get(entry).substring(Reference.MODID.length()+1));
                    elementMap.put(EnumFacing.byName(entry), location);
                }
            }
        }
        return elementMap;
    }
}
