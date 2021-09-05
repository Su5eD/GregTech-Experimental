package mods.gregtechmod.cover;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverProvider;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.QuadFunction;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.LazyValue;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

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
    SOLAR_PANEL_HV(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 512, 64)),
    SOLAR_PANEL_LV(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 8, 1)),
    SOLAR_PANEL_MV(CoverSolarPanel.class, (name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 64, 8)),
    VENT(CoverVent.class, CoverVent::new);

    public final LazyValue<ICoverProvider> instance;

    Cover(Class<? extends ICover> clazz, QuadFunction<ResourceLocation, ICoverable, EnumFacing, ItemStack, ICover> factory) {
        NBTSaveHandler.initClass(clazz);
        this.instance = new LazyValue<>(() -> new CoverProvider(factory)
                .setRegistryName(new ResourceLocation(Reference.MODID, name().toLowerCase(Locale.ROOT))));
    }

    private static class CoverProvider extends IForgeRegistryEntry.Impl<ICoverProvider> implements ICoverProvider {
        private final QuadFunction<ResourceLocation,ICoverable, EnumFacing, ItemStack, ICover> factory;

        public CoverProvider(QuadFunction<ResourceLocation, ICoverable, EnumFacing, ItemStack, ICover> factory) {
            this.factory = factory;
        }
        
        @Override
        public ICover constructCover(EnumFacing side, ICoverable parent, ItemStack stack) {
            return this.factory.apply(getRegistryName(), parent, side, stack);
        }
    }
}
