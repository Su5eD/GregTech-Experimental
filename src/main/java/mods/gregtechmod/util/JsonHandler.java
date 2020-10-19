package mods.gregtechmod.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;

public class JsonHandler {
    public final LinkedTreeMap<String, String> rawTextures;
    public final HashMap<EnumFacing, ResourceLocation> textures;
    public final ResourceLocation particle;

    public JsonHandler(String name, String path) {
        this.rawTextures = readFromJSON(path+"/"+name);
        this.textures = generateMapFromJSON(rawTextures);
        this.particle = new ResourceLocation(GregTechMod.MODID, rawTextures.get("particle").substring(GregTechMod.MODID.length()+1));
    }

    public LinkedTreeMap<String, String> readFromJSON(String name) {
        try {
            Gson gson = new Gson();
            File file = Loader.instance().activeModContainer().getSource();
            FileSystem fs = FileSystems.newFileSystem(file.toPath(), null);

            Reader reader = Files.newBufferedReader(fs.getPath("/assets/"+ GregTechMod.MODID+"/models/item/"+name+".json"));

            HashMap<String , LinkedTreeMap<String, String>> map = gson.fromJson(reader, HashMap.class);

            reader.close();
            return map.get("textures");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public HashMap<EnumFacing, ResourceLocation> generateMapFromJSON(LinkedTreeMap<String, String> map) {
        HashMap<EnumFacing, ResourceLocation> textures = new HashMap<>();
        assert map != null;
        for (String entry : map.keySet()) {
            ResourceLocation location = new ResourceLocation(GregTechMod.MODID, map.get(entry).substring(GregTechMod.MODID.length()+1));
            EnumFacing facing = EnumFacing.byName(entry);
            textures.put(facing, location);
        }
        return textures;
    }
}
