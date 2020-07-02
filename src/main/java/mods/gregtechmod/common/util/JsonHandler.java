package mods.gregtechmod.common.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import mods.gregtechmod.common.core.GregtechMod;
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

    public JsonHandler(String name) {
        this.rawTextures = readFromJSON(name);
        this.textures = generateMapFromJSON(rawTextures);
        this.particle = new ResourceLocation(GregtechMod.MODID, rawTextures.get("particle").substring(GregtechMod.MODID.length()+1));
    }

    public LinkedTreeMap<String, String> readFromJSON(String name) {
        try {
            Gson gson = new Gson();
            File file = Loader.instance().activeModContainer().getSource();
            FileSystem fs = FileSystems.newFileSystem(file.toPath(), null);

            Reader reader = Files.newBufferedReader(fs.getPath("/assets/"+GregtechMod.MODID+"/models/item/teblock/"+name+".json"));

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
            ResourceLocation location = new ResourceLocation(GregtechMod.MODID, map.get(entry).substring(GregtechMod.MODID.length()+1));
            EnumFacing facing;
            if ((facing = EnumFacing.byName(entry)) != null) textures.put(facing, location);
        }
        return textures;
    }
}
