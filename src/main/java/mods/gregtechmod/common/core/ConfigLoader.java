package mods.gregtechmod.common.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigLoader {
    public static int quantumChestMaxItemCount;
    public static int digitalChestMaxItemCount;
    public static double steamMultiplier;
    public static double superHeatedSteamMultiplier;
    public static boolean constantNeedOfEnergy;
    public static int quantumTankCapacity;
    public static boolean dynamicCentrifugeAnimationSpeed;
    public static boolean solarPanelCoverOvervoltageProtection;

    public static void loadConfig(FMLPreInitializationEvent event) {
        File mainFile = new File(new File(event.getModConfigurationDirectory(), "GregTech"), "GregTech.cfg");
        Configuration mainConfig = new Configuration(mainFile);
        mainConfig.load();

        quantumChestMaxItemCount = mainConfig.get("features", "QuantumChestMaxItemCount", 2000000000).getInt();
        quantumTankCapacity = mainConfig.get("features", "QuantumTankCapacity", 2000000000).getInt();
        digitalChestMaxItemCount = mainConfig.get("features", "digitalChestMaxItemCount", 32768).getInt();
        steamMultiplier = mainConfig.get("balance", "steamMultipler", 1.6, "Indicates the amount of universal steam per ic2 steam. This is used by the steam upgrade to fairly convert all kinds of steam to the same value.").getDouble();
        superHeatedSteamMultiplier = mainConfig.get("balance", "superHeatedSteamMultiplier", 0.5, "Serves the same purpose as the above, but for superheated steam.").getDouble();
        constantNeedOfEnergy = mainConfig.get("machines", "constantNeedOfEnergy", true).getBoolean();
        dynamicCentrifugeAnimationSpeed = mainConfig.get("features", "dynamicCentrifugeAnimationSpeed", true, "The centrifuge's animation speed depends on the amount of overclocker upgrades. The more you give, the faster it goes!").getBoolean();
        solarPanelCoverOvervoltageProtection = mainConfig.get("balance", "solarPanelCoverOvervoltageProtection", false, "Prevent MV and HV solar panel covers from overloading (and exploding) your machines").getBoolean();

        /*File dynamicCfgFile = new File(new File(event.getModConfigurationDirectory(), "GregTech"), "DynamicConfig.cfg");
        Configuration dynamicCfg = new Configuration(dynamicCfgFile);
        dynamicCfg.load();
        */

        if (mainConfig.hasChanged()) mainConfig.save();
    }
}
