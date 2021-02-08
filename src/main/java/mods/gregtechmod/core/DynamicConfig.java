package mods.gregtechmod.core;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class DynamicConfig {
    public static Configuration config;

    public static void init() {
        File dynamicCfgFile = new File(new File(GregTechMod.configDir, "GregTech"), "DynamicConfig.cfg");
        config = new Configuration(dynamicCfgFile);
        config.load();
    }
}
