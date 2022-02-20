package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.util.QuadFunction;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Locale;

public enum Cover {
    ACTIVE_DETECTOR(CoverActiveDetector.class, CoverActiveDetector::new),
    CONVEYOR(CoverConveyor.class, CoverConveyor::new),
    CRAFTING(CoverCrafting.class, CoverCrafting::new),
    DRAIN(CoverDrain.class, CoverDrain::new),
    ENERGY_ONLY(CoverEnergyOnly.class, CoverEnergyOnly::new),
    ENERGY_METER(CoverEnergyMeter.class, CoverEnergyMeter::new),
    GENERIC(CoverGeneric.class, CoverGeneric::new),
    ITEM_METER(CoverItemMeter.class, CoverItemMeter::new),
    ITEM_VALVE(CoverValve.class, CoverValve::new),
    LIQUID_METER(CoverLiquidMeter.class, CoverLiquidMeter::new),
    MACHINE_CONTROLLER(CoverMachineController.class, CoverMachineController::new),
    NORMAL(CoverNormal.class, CoverNormal::new),
    PUMP_MODULE(CoverPump.class, CoverPump::new),
    REDSTONE_CONDUCTOR(CoverRedstoneConductor.class, CoverRedstoneConductor::new),
    REDSTONE_ONLY(CoverRedstoneOnly.class, CoverRedstoneOnly::new),
    REDSTONE_SIGNALIZER(CoverRedstoneSignalizer.class, CoverRedstoneSignalizer::new),
    SCREEN(CoverScreen.class, CoverScreen::new),
    SOLAR_PANEL(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 1, 0.25)),
    SOLAR_PANEL_LV(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 8, 1)),
    SOLAR_PANEL_MV(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 64, 8)),
    SOLAR_PANEL_HV(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 512, 64)),
    VENT(CoverVent.class, CoverVent::new);

    private final Lazy<ICoverProvider> instance;

    Cover(Class<? extends ICover> clazz, QuadFunction<ResourceLocation, ICoverable, Direction, ItemStack, ICover> factory) {
        NBTSaveHandler.initClass(clazz);
        this.instance = Lazy.of(() -> new CoverProvider(factory)
            .setRegistryName(name().toLowerCase(Locale.ROOT)));
    }

    public ICoverProvider getInstance() {
        return this.instance.get();
    }

    private static class CoverProvider extends ForgeRegistryEntry<ICoverProvider> implements ICoverProvider { // TODO Texture info
        private final QuadFunction<ResourceLocation, ICoverable, Direction, ItemStack, ICover> factory;

        public CoverProvider(QuadFunction<ResourceLocation, ICoverable, Direction, ItemStack, ICover> factory) {
            this.factory = factory;
        }

        @Override
        public ICover constructCover(Direction side, ICoverable parent, ItemStack stack) {
            return this.factory.apply(getRegistryName(), parent, side, stack);
        }
    }
}
