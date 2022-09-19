package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.item.SolderingMetal;
import dev.su5ed.gregtechmod.api.item.SolderingTool;
import dev.su5ed.gregtechmod.util.capability.JumpCharge;
import dev.su5ed.gregtechmod.util.capability.LightSource;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class Capabilities {
    // Block Entity capabilities
    public static final Capability<CoverHandler> COVER_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
    
    // ItemStack capabilites
    public static final Capability<SolderingTool> SOLDERING_TOOL = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<SolderingMetal> SOLDERING_METAL = CapabilityManager.get(new CapabilityToken<>() {});
    
    // Electric Armor
    public static final Capability<JumpCharge> JUMP_CHARGE = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<LightSource> LIGHT_SOURCE = CapabilityManager.get(new CapabilityToken<>() {});
    
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(CoverHandler.class);
        event.register(SolderingTool.class);
        event.register(SolderingMetal.class);
        event.register(JumpCharge.class);
        event.register(LightSource.class);
    }
    
    private Capabilities() {}
}
