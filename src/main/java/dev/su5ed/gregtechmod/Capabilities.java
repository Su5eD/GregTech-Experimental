package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.item.SolderingMetal;
import dev.su5ed.gregtechmod.api.item.SolderingTool;
import dev.su5ed.gregtechmod.api.item.TurbineRotor;
import dev.su5ed.gregtechmod.api.machine.MachineController;
import dev.su5ed.gregtechmod.api.machine.PowerHandler;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.util.DataOrbSerializable;
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
    public static final Capability<PowerHandler> ENERGY_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<MachineController> MACHINE_CONTROLLER = CapabilityManager.get(new CapabilityToken<>() {});

    // ItemStack capabilites
    public static final Capability<SolderingTool> SOLDERING_TOOL = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<SolderingMetal> SOLDERING_METAL = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<TurbineRotor> TURBINE_ROTOR = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<DataOrbSerializable> DATA_ORB = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<Upgrade> UPGRADE = CapabilityManager.get(new CapabilityToken<>() {});

    // Electric Armor
    public static final Capability<JumpCharge> JUMP_CHARGE = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<LightSource> LIGHT_SOURCE = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(CoverHandler.class);
        event.register(PowerHandler.class);
        event.register(MachineController.class);

        event.register(SolderingTool.class);
        event.register(SolderingMetal.class);
        event.register(TurbineRotor.class);
        event.register(DataOrbSerializable.class);
        event.register(Upgrade.class);

        event.register(JumpCharge.class);
        event.register(LightSource.class);
    }

    private Capabilities() {}
}
