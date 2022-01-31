package mods.gregtechmod.core;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.util.JavaUtil;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class DynamicConfig {
    public static void init() {
        File dynamicCfgFile = GregTechMod.configDir.resolve("GregTech/DynamicConfig.cfg").toFile();
        Configuration config = new Configuration(dynamicCfgFile);
        config.load();
        JavaUtil.setStaticValue(GregTechAPI.class, "dynamicConfig", config);
    }
}
