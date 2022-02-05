package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.ModObjects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
        bus.register(DataGenerators.INSTANCE);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup started");
    }
}
