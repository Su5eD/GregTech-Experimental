package mods.gregtechmod.init;

import mods.gregtechmod.api.cover.CoverRegistry;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.cover.type.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class CoverLoader {
    public enum Covers {
        active_detector(CoverActiveDetector::new),
        conveyor(CoverConveyor::new),
        crafting(CoverCrafting::new),
        drain(CoverDrain::new),
        energy_only(CoverEnergyOnly::new),
        eu_meter(CoverEUMeter::new),
        generic(CoverGeneric::new),
        item_meter(CoverItemMeter::new),
        item_valve(CoverValve::new),
        liquid_meter(CoverLiquidMeter::new),
        machine_controller(CoverMachineController::new),
        normal(CoverNormal::new),
        pump_module(CoverPump::new),
        redstone_conductor(CoverRedstoneConductor::new),
        redstone_only(CoverRedstoneOnly::new),
        redstone_signalizer(CoverRedstoneSignalizer::new),
        screen(CoverScreen::new),
        solar_panel((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 1, 0.25)),
        solar_panel_hv((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 512, 64)),
        solar_panel_lv((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 8, 1)),
        solar_panel_mv((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 64, 8)),
        vent(CoverVent::new);

        private final TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor;

        Covers(TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor) {
            this.constructor = constructor;
        }
    }

    public static void registerCovers() {
        GregtechMod.LOGGER.info("Registering covers");
        for(Covers type : Covers.values()) {
            GregtechMod.LOGGER.debug("Registering cover "+type.name());
            CoverRegistry.registerCover(type.name(), type.constructor);
        }
    }
}
