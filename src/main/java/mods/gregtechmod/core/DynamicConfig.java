package mods.gregtechmod.core;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.util.GtUtil;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class DynamicConfig {
    public static void init() {
        File dynamicCfgFile = new File(new File(GregTechMod.configDir, "GregTech"), "DynamicConfig.cfg");
        Configuration config = new Configuration(dynamicCfgFile);
        config.load();
        GtUtil.setPrivateStaticValue(GregTechAPI.class, "dynamicConfig", config);
    }
}
