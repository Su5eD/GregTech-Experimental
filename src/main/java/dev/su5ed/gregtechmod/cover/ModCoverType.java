package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;

public enum ModCoverType {
    ACTIVE_DETECTOR(ActiveDetectorCover::new),
    CONVEYOR(ConveyorCover::new),
    CRAFTING(CraftingCover::new),
    DRAIN(DrainCover::new),
    ENERGY_ONLY(EnergyOnlyCover::new),
    ENERGY_METER(EnergyMeterCover::new),
    GENERIC(GenericCover::new),
    ITEM_METER(ItemMeterCover::new),
    ITEM_VALVE(ValveCover::new),
    LIQUID_METER(LiquidMeterCover::new),
    MACHINE_CONTROLLER(MachineControllerCover::new),
    NORMAL(NormalCover::new),
    PUMP_MODULE(PumpCover::new),
    REDSTONE_CONDUCTOR(RedstoneConductorCover::new),
    REDSTONE_ONLY(RedstoneOnlyCover::new),
    REDSTONE_SIGNALIZER(RedstoneSignalizerCover::new),
    SCREEN(ScreenCover::new),
    SOLAR_PANEL((type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 1, 0.25)),
    SOLAR_PANEL_LV((type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 8, 1)),
    SOLAR_PANEL_MV((type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 64, 8)),
    SOLAR_PANEL_HV((type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 512, 64)),
    VENT(VentCover::new);

    private final Lazy<CoverType> instance;

    ModCoverType(CoverType.CoverSupplier factory) {
        this.instance = Lazy.of(() -> new CoverType(factory)
            .setRegistryName(name().toLowerCase(Locale.ROOT)));
    }

    public CoverType get() {
        return this.instance.get();
    }
}
