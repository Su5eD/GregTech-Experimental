package mods.gregtechmod.api;

import mods.gregtechmod.api.util.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID, category = "")
@Config.LangKey(Reference.MODID+".config.title")
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class GregTechConfig {

    @Config.LangKey(Reference.MODID+".config.general")
    public static final General GENERAL = new General();

    @Config.LangKey(Reference.MODID+".config.features")
    public static final Features FEATURES = new Features();

    @Config.LangKey(Reference.MODID+".config.balance")
    public static final Balance BALANCE = new Balance();

    @Config.LangKey(Reference.MODID+".config.machines")
    public static final Machines MACHINES = new Machines();

    @Config.LangKey(Reference.MODID+".config.unification")
    public static final Unification UNIFICATION = new Unification();

    @Config.LangKey(Reference.MODID+".config.worldgen")
    public static final WorldGen WORLDGEN = new WorldGen();

    @Config.LangKey(Reference.MODID+".config,blastfurnacerequirements")
    public static final BlastFurnaceRequirements BLAST_FURNACE_REQUIREMENTS = new BlastFurnaceRequirements();

    public static class General {
        public boolean connectedMachineCasingTextures = true;
        @Config.Comment("The centrifuge's animation speed depends on the amount of overclocker upgrades. The more you give, the faster it goes!")
        public boolean dynamicCentrifugeAnimationSpeed = true;
        public boolean hiddenOres = true;
        public boolean harderStone = false;

        public String[] specialUnificationTargets = new String[0];
    }

    public static class Features {
        public int quantumChestMaxItemCount = 2000000000;
        public int quantumTankCapacity = 2000000000;
        public int digitalChestMaxItemCount = 32768;
        @Config.RangeInt(min = 1, max = 64)
        public int upgradeStackSize = 4;
        @Config.RangeInt(min = 16, max = 64)
        public int maxLogStackSize = 64;
        @Config.RangeInt(min = 16, max = 64)
        public int maxPlankStackSize = 64;
    }

    public static class Balance {
        @Config.Comment("Indicates the amount of 1mb universal steam per 1mb ic2 steam. This is used by the steam upgrade to convert all kinds of steam to the same value.")
        public double steamMultiplier = 1.6;
        @Config.Comment("Indicates the amount of 1mb ic2 steam per 1mb ic2 steam. This is used by the steam upgrade to convert all kinds of steam to the same value.")
        public double superHeatedSteamMultiplier = 2;
        @Config.Comment("Prevent MV and HV solar panel covers from overloading (and exploding) your machines")
        public boolean solarPanelCoverOvervoltageProtection = false;
        public float LVExplosionPower = 2;
        public float MVExplosionPower = 3;
        public float HVExplosionPower = 4;
        public float EVExplosionPower = 5;
        public float IVExplosionPower = 6;
    }

    public static class Machines {
        public boolean constantNeedOfEnergy = true;
        public boolean machineFlammable = true;
        @Config.Comment("Fire causes explosions")
        public boolean machineFireExplosions = true;
        @Config.Comment("Wirefire on explosion")
        public boolean machineWireFire = true;
        @Config.Comment("Rain causes explosions")
        public boolean machineRainExplosions = true;
        @Config.Comment("Lightning causes explosions")
        public boolean machineThunderExplosions = true;
        @Config.Comment("Nearby explosions cause machines to explode")
        public boolean machineChainExplosions = true;
    }

    public static class Unification {
        public boolean forestry = true;
        public boolean railcraft = true;
        public boolean projectred = true;
        public boolean thaumcraft = true;
        public boolean thermalfoundation = true;
    }

    public static class WorldGen {
        public boolean retrogen = true;
        public boolean generateInVoid = false;

        @Config.RequiresMcRestart
        public boolean galena = true;
        @Config.RequiresMcRestart
        public boolean iridium = true;
        @Config.RequiresMcRestart
        public boolean ruby = true;
        @Config.RequiresMcRestart
        public boolean sapphire = true;
        @Config.RequiresMcRestart
        public boolean bauxite = true;
        @Config.RequiresMcRestart
        public boolean tetrahedrite = true;
        @Config.RequiresMcRestart
        public boolean cassiterite = true;
        @Config.RequiresMcRestart
        public boolean sphalerite_overworld = true;

        @Config.RequiresMcRestart
        public boolean tungstate = true;
        @Config.RequiresMcRestart
        public boolean sheldonite = true;
        @Config.RequiresMcRestart
        public boolean olivine = true;
        @Config.RequiresMcRestart
        public boolean sodalite = true;

        @Config.RequiresMcRestart
        public boolean pyrite_tiny = true;
        @Config.RequiresMcRestart
        public boolean pyrite_small = true;
        @Config.RequiresMcRestart
        public boolean pyrite_medium = true;
        @Config.RequiresMcRestart
        public boolean pyrite_large = true;
        @Config.RequiresMcRestart
        public boolean pyrite_huge = true;

        @Config.RequiresMcRestart
        public boolean cinnabar_tiny = true;
        @Config.RequiresMcRestart
        public boolean cinnabar_small = true;
        @Config.RequiresMcRestart
        public boolean cinnabar_medium = true;
        @Config.RequiresMcRestart
        public boolean cinnabar_large = false;
        @Config.RequiresMcRestart
        public boolean cinnabar_huge = false;

        @Config.RequiresMcRestart
        public boolean sphalerite_tiny = true;
        @Config.RequiresMcRestart
        public boolean sphalerite_small = true;
        @Config.RequiresMcRestart
        public boolean sphalerite_medium = true;
        @Config.RequiresMcRestart
        public boolean sphalerite_large = true;
        @Config.RequiresMcRestart
        public boolean sphalerite_huge = true;

        @Config.RequiresMcRestart
        public boolean endAsteroids = true;
    }

    public static class BlastFurnaceRequirements {
        public boolean aluminium = true;
        public boolean steel = true;
        public boolean titanium = true;
        public boolean chrome = true;
        public boolean tungsten = true;
    }

    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }
    }
}