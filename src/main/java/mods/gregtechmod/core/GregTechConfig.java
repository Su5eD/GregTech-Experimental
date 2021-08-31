package mods.gregtechmod.core;

import mods.gregtechmod.api.util.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID, category = "")
@LangKey(Reference.MODID + ".config.title")
@EventBusSubscriber(modid = Reference.MODID)
public class GregTechConfig {

    @LangKey(Reference.MODID + ".config.general")
    public static final General GENERAL = new General();

    @LangKey(Reference.MODID + ".config.features")
    public static final Features FEATURES = new Features();

    @LangKey(Reference.MODID + ".config.balance")
    public static final Balance BALANCE = new Balance();

    @LangKey(Reference.MODID + ".config.machines")
    public static final Machines MACHINES = new Machines();

    @LangKey(Reference.MODID + ".config.unification")
    public static final Unification UNIFICATION = new Unification();

    @LangKey(Reference.MODID + ".config.worldgen")
    public static final WorldGen WORLDGEN = new WorldGen();

    @LangKey(Reference.MODID + ".config.disabled_recipes")
    public static final DisabledRecipes DISABLED_RECIPES = new DisabledRecipes();

    public static class General {
        public boolean connectedTextures = true;
        @Comment("The centrifuge's animation speed depends on the amount of overclocker upgrades. The more you give, the faster it goes!")
        public boolean dynamicCentrifugeAnimationSpeed = true;
        public boolean hiddenOres = true;
        public boolean harderStone = false;
        public boolean woodNeedsSawForCrafting = true;
        public boolean smallerWoodToolDurability = true;
        public boolean smallerStoneToolDurability = true;
        public boolean enhancedWrenchOverlay = true;

        public boolean showCapes = true;
    }

    public static class Features {
        public int quantumChestMaxItemCount = 2000000000;
        public int quantumTankCapacity = 2000000000;
        public int digitalChestMaxItemCount = 32768;
        @RangeInt(min = 1, max = 64)
        public int upgradeStackSize = 4;
    }

    public static class Balance {
        @Comment("Indicates the amount of 1mb universal steam per 1mb ic2 steam. This is used by the steam upgrade to convert all kinds of steam to the same value.")
        public double steamMultiplier = 1.6;
        @Comment("Indicates the amount of 1mb ic2 steam per 1mb ic2 steam. This is used by the steam upgrade to convert all kinds of steam to the same value.")
        public double superHeatedSteamMultiplier = 2;
        @Comment("Prevent MV and HV solar panel covers from overloading (and exploding) your machines")
        public boolean solarPanelCoverOvervoltageProtection = false;
        public float LVExplosionPower = 2;
        public float MVExplosionPower = 3;
        public float HVExplosionPower = 4;
        public float EVExplosionPower = 5;
        public float IVExplosionPower = 6;
    }

    public static class Machines {
        @Name("magic_energy_absorber")
        @LangKey(Reference.MODID + ".teblock.magic_energy_absorber")
        public final MagicEnergyAbsorber magicEnergyAbsorber = new MagicEnergyAbsorber();
        
        @Name("dragon_egg_energy_siphon")
        @LangKey(Reference.MODID + ".teblock.dragon_egg_energy_siphon")
        public final DragonEggEnergySiphon dragonEggEnergySiphon = new DragonEggEnergySiphon();
        
        @Comment("Makes active machines lose their current progress when they run out of power")
        public boolean constantNeedOfEnergy = true;
        @Comment("If set tot true, machines will have a chance to catch fire in the rain")
        public boolean machineFlammable = true;
        @Comment("Fire causes explosions")
        public boolean machineFireExplosions = true;
        @Comment("Wirefire on explosion")
        public boolean machineWireFire = true;
        @Comment("Rain causes explosions")
        public boolean machineRainExplosions = true;
        @Comment("Lightning causes explosions")
        public boolean machineThunderExplosions = true;
        @Comment("Nearby explosions cause machines to explode")
        public boolean machineChainExplosions = true;
        public int matterFabricationRate = 10000000;
        
        public static class MagicEnergyAbsorber {
            @Comment("EU/t generated from an Ender Crystal")
            public int energyPerEnderCrystal = 320;
            @Comment("EU/t generated from Vis. Requires Thaumcraft to be installed.")
            public int energyFromVis = 12800;
        }
        
        public static class DragonEggEnergySiphon {
            @Comment("EU/t generated from a Dragon Egg")
            public int dragonEggEnergy = 1024;
            public boolean allowMultipleEggs = false;
            @Comment("If thaumcraft is installed, has a chance of releasing flux into the aura")
            public boolean outputFlux = true;
        }
    }

    public static class Unification {
        public boolean forestry = false;
        public boolean railcraft = false;
        public boolean projectred = true;
        public boolean thaumcraft = true;
        public boolean thermalfoundation = false;
        
        @Comment("Defines overrides for oredict unification. Each entry represents an itemstack in the form of <registry name>:<metadata>")
        public String[] specialUnificationTargets = new String[0];
    }

    public static class WorldGen {
        public boolean retrogen = true;
        public boolean generateInVoid = false;

        @RequiresMcRestart
        public boolean galena = true;
        @RequiresMcRestart
        public boolean iridium = true;
        @RequiresMcRestart
        public boolean ruby = true;
        @RequiresMcRestart
        public boolean sapphire = true;
        @RequiresMcRestart
        public boolean bauxite = true;
        @RequiresMcRestart
        public boolean tetrahedrite = true;
        @RequiresMcRestart
        public boolean cassiterite = true;
        @RequiresMcRestart
        public boolean sphalerite_overworld = true;

        @RequiresMcRestart
        public boolean tungstate = true;
        @RequiresMcRestart
        public boolean sheldonite = true;
        @RequiresMcRestart
        public boolean olivine = true;
        @RequiresMcRestart
        public boolean sodalite = true;

        @RequiresMcRestart
        public boolean pyriteTiny = true;
        @RequiresMcRestart
        public boolean pyriteSmall = true;
        @RequiresMcRestart
        public boolean pyriteMedium = true;
        @RequiresMcRestart
        public boolean pyriteLarge = true;
        @RequiresMcRestart
        public boolean pyriteHuge = true;

        @RequiresMcRestart
        public boolean cinnabarTiny = true;
        @RequiresMcRestart
        public boolean cinnabarSmall = true;
        @RequiresMcRestart
        public boolean cinnabarMedium = true;
        @RequiresMcRestart
        public boolean cinnabarLarge = false;
        @RequiresMcRestart
        public boolean cinnabarHuge = false;

        @RequiresMcRestart
        public boolean sphaleriteTiny = true;
        @RequiresMcRestart
        public boolean sphaleriteSmall = true;
        @RequiresMcRestart
        public boolean sphaleriteMedium = true;
        @RequiresMcRestart
        public boolean sphaleriteLarge = true;
        @RequiresMcRestart
        public boolean sphaleriteHuge = true;

        @RequiresMcRestart
        public boolean endAsteroids = true;
    }

    public static class DisabledRecipes {
        public boolean bronzeIngotCrafting = true;
        public boolean massFabricator = true;
        public boolean enchantingTable = false;
        public boolean enderChest = false;
        @Comment("Classic profile only")
        public boolean depletedUranium8 = true;
        public boolean easyMobGrinderRecycling = true;
        public boolean easyStoneRecycling = true;
    }

    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }
    }
}
