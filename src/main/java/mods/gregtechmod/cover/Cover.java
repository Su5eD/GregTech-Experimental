package mods.gregtechmod.cover;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverProvider;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.QuadFunction;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Locale;

public enum Cover {
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
    SOLAR_PANEL((name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 1, 0.25)),
    SOLAR_PANEL_HV((name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 512, 64)),
    SOLAR_PANEL_LV((name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 8, 1)),
    SOLAR_PANEL_MV((name, tile, side, stack) -> new CoverSolarPanel(name, tile, side, stack, 64, 8)),
    VENT(CoverVent::new);

    public final LazyValue<ICoverProvider> instance;

    Cover(QuadFunction<ResourceLocation, ICoverable, EnumFacing, ItemStack, ICover> factory) {
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
