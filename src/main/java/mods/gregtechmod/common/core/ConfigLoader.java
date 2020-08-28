package mods.gregtechmod.common.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigLoader {
    public static boolean connectedMachineCasingTextures;
    public static int quantumChestMaxItemCount;
    public static int digitalChestMaxItemCount;
    public static double steamMultiplier;
    public static double superHeatedSteamMultiplier;
    public static boolean constantNeedOfEnergy;
    public static int quantumTankCapacity;
    public static boolean dynamicCentrifugeAnimationSpeed;
    public static boolean solarPanelCoverOvervoltageProtection;
    public static double LVExplosionPower;
    public static double MVExplosionPower;
    public static double HVExplosionPower;
    public static int upgradeStackSize;

    public static void loadConfig(FMLPreInitializationEvent event) {
        File mainFile = new File(new File(event.getModConfigurationDirectory(), "GregTech"), "GregTech.cfg");
        Configuration mainConfig = new Configuration(mainFile);
        mainConfig.load();

        connectedMachineCasingTextures = mainConfig.get("general", "ConnectedMachineCasingTextures", true).getBoolean();
        quantumChestMaxItemCount = mainConfig.get("features", "QuantumChestMaxItemCount", 2000000000).getInt();
        quantumTankCapacity = mainConfig.get("features", "QuantumTankCapacity", 2000000000).getInt();
        digitalChestMaxItemCount = mainConfig.get("features", "digitalChestMaxItemCount", 32768).getInt();
        steamMultiplier = mainConfig.get("balance", "SteamMultipler", 1.6, "Indicates the amount of universal steam per ic2 steam. This is used by the steam upgrade to fairly convert all kinds of steam to the same value.").getDouble();
        superHeatedSteamMultiplier = mainConfig.get("balance", "SuperHeatedSteamMultiplier", 0.5, "Serves the same purpose as the above, for superheated steam.").getDouble();
        constantNeedOfEnergy = mainConfig.get("machines", "ConstantNeedOfEnergy", true).getBoolean();
        dynamicCentrifugeAnimationSpeed = mainConfig.get("features", "DynamicCentrifugeAnimationSpeed", true, "The centrifuge's animation speed depends on the amount of overclocker upgrades. The more you give, the faster it goes!").getBoolean();
        solarPanelCoverOvervoltageProtection = mainConfig.get("balance", "SolarPanelCoverOvervoltageProtection", false, "Prevent MV and HV solar panel covers from overloading (and exploding) your machines").getBoolean();
        LVExplosionPower = mainConfig.get("balance", "LVExplosionPower", 2.5).getDouble();
        MVExplosionPower = mainConfig.get("balance", "MVExplosionPower", 3).getDouble();
        HVExplosionPower = mainConfig.get("balance", "HVExplosionPower", 4).getDouble();
        upgradeStackSize = mainConfig.get("features", "UpgradeStackSize", 4).getInt();

        /*File dynamicCfgFile = new File(new File(event.getModConfigurationDirectory(), "GregTech"), "DynamicConfig.cfg");
        Configuration dynamicCfg = new Configuration(dynamicCfgFile);
        dynamicCfg.load();
        */

        if (mainConfig.hasChanged()) mainConfig.save();
    }
}
