package mods.gregtechmod.init;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverRegistry;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.cover.*;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Locale;

public class CoverLoader {
    public enum Covers {
        ACTIVE_DETECTOR(CoverActiveDetector::new),
        CONVEYOR(CoverConveyor::new),
        CRAFTING(CoverCrafting::new),
        DRAIN(CoverDrain::new),
        ENERGY_ONLY(CoverEnergyOnly::new),
        ENERGY_METER(CoverEnergyMeter::new),
        GENERIC(CoverGeneric::new),
        ITEM_METER(CoverItemMeter::new),
        ITEM_VALVE(CoverValve::new),
        LIQUID_METER(CoverLiquidMeter::new),
        MACHINE_CONTROLLER(CoverMachineController::new),
        NORMAL(CoverNormal::new),
        PUMP_MODULE(CoverPump::new),
        REDSTONE_CONDUCTOR(CoverRedstoneConductor::new),
        REDSTONE_ONLY(CoverRedstoneOnly::new),
        REDSTONE_SIGNALIZER(CoverRedstoneSignalizer::new),
        SCREEN(CoverScreen::new),
        SOLAR_PANEL((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 1, 0.25)),
        SOLAR_PANEL_HV((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 512, 64)),
        SOLAR_PANEL_LV((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 8, 1)),
        SOLAR_PANEL_MV((tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 64, 8)),
        VENT(CoverVent::new);

        private final TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor;

        Covers(TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor) {
            this.constructor = constructor;
        }
    }

    public static void registerCovers() {
        GregTechMod.logger.info("Registering covers");
        ICoverRegistry coverRegistry = new CoverRegistry();
        GtUtil.setPrivateStaticValue(GregTechAPI.class, "coverRegistry", coverRegistry);
        
        for(Covers type : Covers.values()) {
            String name = type.name().toLowerCase(Locale.ROOT);
            GregTechMod.logger.debug("Registering cover " + name);
            coverRegistry.registerCover(name, type.constructor);
        }
    }
}
