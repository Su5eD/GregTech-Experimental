package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.blockentity.SonictronBlockEntity;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.datagen.DataGenerators;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
import dev.su5ed.gregtechmod.network.NetworkHandler;
import dev.su5ed.gregtechmod.object.ModCovers;
import dev.su5ed.gregtechmod.object.ModMenus;
import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeTypes;
import dev.su5ed.gregtechmod.util.loot.ConditionLootModifier;
import dev.su5ed.gregtechmod.world.ModConfiguredFeatures;
import dev.su5ed.gregtechmod.world.ModPlacedFeatures;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class GregTechMod {
    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean isClassic; // PLACEHOLDER until ic2 profiles are ported

    public GregTechMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        ModHandler.initMods();

        GregTechAPIImpl.createAndInject();
        ModCovers.init(bus);
        ModMenus.init(bus);
        ModConfiguredFeatures.init(bus);
        ModPlacedFeatures.init(bus);
        ConditionLootModifier.init(bus);
        ModRecipeTypes.init(bus);
        ModRecipeSerializers.init(bus);
        bus.register(ModObjects.class);
        bus.register(DataGenerators.class);
        bus.register(Capabilities.class);

        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, GregTechConfig.CLIENT_SPEC);
        ctx.registerConfig(ModConfig.Type.COMMON, GregTechConfig.COMMON_SPEC);

        NetworkHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup started");

        GregTechNetwork.registerPackets();
        ModHandler.registerCrops();
        SonictronBlockEntity.loadSonictronSounds();
    }
}
