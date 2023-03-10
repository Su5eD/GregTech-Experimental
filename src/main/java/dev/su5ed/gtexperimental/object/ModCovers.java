package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.cover.CoverCategory;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.cover.ActiveDetectorCover;
import dev.su5ed.gtexperimental.cover.ConveyorCover;
import dev.su5ed.gtexperimental.cover.CraftingCover;
import dev.su5ed.gtexperimental.cover.DrainCover;
import dev.su5ed.gtexperimental.cover.EnergyMeterCover;
import dev.su5ed.gtexperimental.cover.EnergyOnlyCover;
import dev.su5ed.gtexperimental.cover.GenericCover;
import dev.su5ed.gtexperimental.cover.ItemMeterCover;
import dev.su5ed.gtexperimental.cover.LiquidMeterCover;
import dev.su5ed.gtexperimental.cover.MachineControllerCover;
import dev.su5ed.gtexperimental.cover.NormalCover;
import dev.su5ed.gtexperimental.cover.PumpCover;
import dev.su5ed.gtexperimental.cover.RedstoneConductorCover;
import dev.su5ed.gtexperimental.cover.RedstoneOnlyCover;
import dev.su5ed.gtexperimental.cover.RedstoneSignalizerCover;
import dev.su5ed.gtexperimental.cover.ScreenCover;
import dev.su5ed.gtexperimental.cover.SolarPanelCover;
import dev.su5ed.gtexperimental.cover.ValveCover;
import dev.su5ed.gtexperimental.cover.VentCover;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

@SuppressWarnings("unused")
public final class ModCovers {
    private static final DeferredRegister<CoverType> COVER_TYPES = DeferredRegister.create(location("cover_type"), Reference.MODID);
    public static final Supplier<IForgeRegistry<CoverType>> REGISTRY = COVER_TYPES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<CoverType> ACTIVE_DETECTOR = register("active_detector", CoverCategory.METER, Set.of(Capabilities.MACHINE_PROGRESS, Capabilities.MACHINE_CONTROLLER), ActiveDetectorCover::new);
    public static final RegistryObject<CoverType> CONVEYOR = register("conveyor", CoverCategory.IO, ConveyorCover::new);
    public static final RegistryObject<CoverType> CRAFTING = register("crafting", CoverCategory.UTIL, CraftingCover::new);
    public static final RegistryObject<CoverType> DRAIN = register("drain", CoverCategory.IO, Capabilities.MACHINE_CONTROLLER, DrainCover::new);
    public static final RegistryObject<CoverType> ENERGY_ONLY = register("energy_only", CoverCategory.OTHER, Capabilities.MACHINE_CONTROLLER, EnergyOnlyCover::new);
    public static final RegistryObject<CoverType> ENERGY_METER = register("energy_meter", CoverCategory.METER, Capabilities.ENERGY_HANDLER, EnergyMeterCover::new);
    public static final RegistryObject<CoverType> GENERIC = register("generic", CoverCategory.GENERIC, GenericCover::new);
    public static final RegistryObject<CoverType> ITEM_METER = register("item_meter", CoverCategory.METER, Capabilities.MACHINE_CONTROLLER, ItemMeterCover::new);
    public static final RegistryObject<CoverType> ITEM_VALVE = register("item_valve", CoverCategory.IO, ValveCover::new);
    public static final RegistryObject<CoverType> LIQUID_METER = register("liquid_meter", CoverCategory.METER, Capabilities.MACHINE_CONTROLLER, LiquidMeterCover::new);
    public static final RegistryObject<CoverType> MACHINE_CONTROLLER = register("machine_controller", CoverCategory.CONTROLLER, Capabilities.MACHINE_CONTROLLER, MachineControllerCover::new);
    public static final RegistryObject<CoverType> NORMAL = register("normal", CoverCategory.GENERIC, NormalCover::new);
    public static final RegistryObject<CoverType> PUMP_MODULE = register("pump_module", CoverCategory.IO, PumpCover::new);
    public static final RegistryObject<CoverType> REDSTONE_CONDUCTOR = register("redstone_conductor", CoverCategory.UTIL, Capabilities.MACHINE_CONTROLLER, RedstoneConductorCover::new);
    public static final RegistryObject<CoverType> REDSTONE_ONLY = register("redstone_only", CoverCategory.OTHER, RedstoneOnlyCover::new);
    public static final RegistryObject<CoverType> REDSTONE_SIGNALIZER = register("redstone_signalizer", CoverCategory.METER, RedstoneSignalizerCover::new);
    public static final RegistryObject<CoverType> SCREEN = register("screen", CoverCategory.UTIL, ScreenCover::new);
    public static final RegistryObject<CoverType> SOLAR_PANEL = register("solar_panel", CoverCategory.ENERGY, Capabilities.ENERGY_HANDLER, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 1, 0.25));
    public static final RegistryObject<CoverType> SOLAR_PANEL_LV = register("solar_panel_lv", CoverCategory.ENERGY, Capabilities.ENERGY_HANDLER, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 8, 1));
    public static final RegistryObject<CoverType> SOLAR_PANEL_MV = register("solar_panel_mv", CoverCategory.ENERGY, Capabilities.ENERGY_HANDLER, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 64, 8));
    public static final RegistryObject<CoverType> SOLAR_PANEL_HV = register("solar_panel_hv", CoverCategory.ENERGY, Capabilities.ENERGY_HANDLER, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 512, 64));
    public static final RegistryObject<CoverType> VENT = register("vent", CoverCategory.OTHER, Set.of(Capabilities.MACHINE_PROGRESS, Capabilities.MACHINE_CONTROLLER), VentCover::new);

    public static void init(IEventBus bus) {
        COVER_TYPES.register(bus);
    }

    private static RegistryObject<CoverType> register(String name, CoverCategory category, CoverType.CoverSupplier factory) {
        return register(name, category, be -> true, factory);
    }

    private static RegistryObject<CoverType> register(String name, CoverCategory category, Capability<?> capability, CoverType.CoverSupplier factory) {
        return register(name, category, Set.of(capability), factory);
    }

    private static RegistryObject<CoverType> register(String name, CoverCategory category, Set<Capability<?>> capabilities, CoverType.CoverSupplier factory) {
        return register(name, category, be -> capabilities.stream().allMatch(cap -> be.getCapability(cap).isPresent()), factory);
    }

    private static RegistryObject<CoverType> register(String name, CoverCategory category, Predicate<BlockEntity> condition, CoverType.CoverSupplier factory) {
        return COVER_TYPES.register(name, () -> new CoverType(category, condition, factory));
    }
}
