package mods.gregtechmod.core;

import mods.gregtechmod.api.GregTechAPI;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class DynamicConfig {
    public static void init() {
        File dynamicCfgFile = new File(new File(GregTechMod.configDir, "GregTech"), "DynamicConfig.cfg");
        GregTechAPI.dynamicConfig = new Configuration(dynamicCfgFile);
        GregTechAPI.dynamicConfig.load();
    }
}
