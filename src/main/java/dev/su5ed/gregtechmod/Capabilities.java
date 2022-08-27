package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class Capabilities {
    public static final Capability<CoverHandler> COVERABLE = CapabilityManager.get(new CapabilityToken<>() {});
    
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(CoverHandler.class);
    }
    
    private Capabilities() {}
}
