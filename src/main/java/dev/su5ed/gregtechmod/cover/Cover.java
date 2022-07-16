package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;

public enum Cover {
    ACTIVE_DETECTOR(ActiveDetectorCover.class, ActiveDetectorCover::new),
    CONVEYOR(ConveyorCover.class, ConveyorCover::new),
    CRAFTING(CraftingCover.class, CraftingCover::new),
    DRAIN(DrainCover.class, DrainCover::new),
    ENERGY_ONLY(EnergyOnlyCover.class, EnergyOnlyCover::new),
    ENERGY_METER(EnergyMeterCover.class, EnergyMeterCover::new),
    GENERIC(GenericCover.class, GenericCover::new),
    ITEM_METER(ItemMeterCover.class, ItemMeterCover::new),
    ITEM_VALVE(ValveCover.class, ValveCover::new),
    LIQUID_METER(LiquidMeterCover.class, LiquidMeterCover::new),
    MACHINE_CONTROLLER(MachineControllerCover.class, MachineControllerCover::new),
    NORMAL(NormalCover.class, NormalCover::new),
    PUMP_MODULE(PumpCover.class, PumpCover::new),
    REDSTONE_CONDUCTOR(RedstoneConductorCover.class, RedstoneConductorCover::new),
    REDSTONE_ONLY(RedstoneOnlyCover.class, RedstoneOnlyCover::new),
    REDSTONE_SIGNALIZER(RedstoneSignalizerCover.class, RedstoneSignalizerCover::new),
    SCREEN(ScreenCover.class, ScreenCover::new),
    SOLAR_PANEL(SolarPanelCover.class, (type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 1, 0.25)),
    SOLAR_PANEL_LV(SolarPanelCover.class, (type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 8, 1)),
    SOLAR_PANEL_MV(SolarPanelCover.class, (type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 64, 8)),
    SOLAR_PANEL_HV(SolarPanelCover.class, (type, tile, side, stack) -> new SolarPanelCover(type, tile, side, stack, 512, 64)),
    VENT(VentCover.class, VentCover::new);

    private final Lazy<CoverType> instance;

    Cover(Class<? extends ICover> clazz, CoverType.CoverSupplier factory) {
        NBTSaveHandler.initClass(clazz);
        this.instance = Lazy.of(() -> new CoverType(factory)
            .setRegistryName(name().toLowerCase(Locale.ROOT)));
    }

    public CoverType getType() {
        return this.instance.get();
    }
}
