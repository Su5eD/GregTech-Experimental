package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.datagen.DataGenerators;
import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.setup.ClientSetup;
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
    private static final Logger LOGGER = LogManager.getLogger();

    public GregTechMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        
        bus.register(ModObjects.INSTANCE);
        bus.register(ClientSetup.INSTANCE);
        bus.register(DataGenerators.INSTANCE);

        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, GregTechConfig.CLIENT_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup started");
    }
}
