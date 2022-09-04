package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.ModContainers;
import dev.su5ed.gregtechmod.object.ModCovers;
import dev.su5ed.gregtechmod.datagen.DataGenerators;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
import dev.su5ed.gregtechmod.network.NetworkHandler;
import dev.su5ed.gregtechmod.object.ModObjects;
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
        ModContainers.init(bus);
        bus.register(ModObjects.INSTANCE);
        bus.register(DataGenerators.INSTANCE);
        bus.register(Capabilities.class);

        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, GregTechConfig.CLIENT_SPEC);
        ctx.registerConfig(ModConfig.Type.COMMON, GregTechConfig.COMMON_SPEC);

        NetworkHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup started");
        
        GregTechNetwork.registerPackets();
    }
}
