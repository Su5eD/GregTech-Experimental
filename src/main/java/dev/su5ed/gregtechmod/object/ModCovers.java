package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.machine.IMachineProgress;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.cover.ActiveDetectorCover;
import dev.su5ed.gregtechmod.cover.ConveyorCover;
import dev.su5ed.gregtechmod.cover.CraftingCover;
import dev.su5ed.gregtechmod.cover.DrainCover;
import dev.su5ed.gregtechmod.cover.EnergyMeterCover;
import dev.su5ed.gregtechmod.cover.EnergyOnlyCover;
import dev.su5ed.gregtechmod.cover.GenericCover;
import dev.su5ed.gregtechmod.cover.ItemMeterCover;
import dev.su5ed.gregtechmod.cover.LiquidMeterCover;
import dev.su5ed.gregtechmod.cover.MachineControllerCover;
import dev.su5ed.gregtechmod.cover.NormalCover;
import dev.su5ed.gregtechmod.cover.PumpCover;
import dev.su5ed.gregtechmod.cover.RedstoneConductorCover;
import dev.su5ed.gregtechmod.cover.RedstoneOnlyCover;
import dev.su5ed.gregtechmod.cover.RedstoneSignalizerCover;
import dev.su5ed.gregtechmod.cover.ScreenCover;
import dev.su5ed.gregtechmod.cover.SolarPanelCover;
import dev.su5ed.gregtechmod.cover.ValveCover;
import dev.su5ed.gregtechmod.cover.VentCover;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

@SuppressWarnings("unused")
public final class ModCovers {
    private static final DeferredRegister<CoverType<?>> COVER_TYPES = DeferredRegister.create(location("cover_type"), Reference.MODID);
    @SuppressWarnings("RedundantTypeArguments") // Java can't infer generic types here...
    public static final Supplier<IForgeRegistry<CoverType<?>>> REGISTRY = COVER_TYPES.<CoverType<?>>makeRegistry(c(CoverType.class), RegistryBuilder::new);
    
    public static final RegistryObject<CoverType<IMachineProgress>> ACTIVE_DETECTOR = register("active_detector", CoverCategory.METER, IMachineProgress.class, ActiveDetectorCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> CONVEYOR = register("conveyor", CoverCategory.IO, BlockEntity.class, ConveyorCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> CRAFTING = register("crafting", CoverCategory.UTIL, BlockEntity.class, CraftingCover::new);
    public static final RegistryObject<CoverType<IGregTechMachine>> DRAIN = register("drain", CoverCategory.IO, IGregTechMachine.class, DrainCover::new);
    public static final RegistryObject<CoverType<IGregTechMachine>> ENERGY_ONLY = register("energy_only", CoverCategory.OTHER, IGregTechMachine.class, EnergyOnlyCover::new);
    public static final RegistryObject<CoverType<IElectricMachine>> ENERGY_METER = register("energy_meter", CoverCategory.METER, IElectricMachine.class, EnergyMeterCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> GENERIC = register("generic", CoverCategory.GENERIC, BlockEntity.class, GenericCover::new);
    public static final RegistryObject<CoverType<IGregTechMachine>> ITEM_METER = register("item_meter", CoverCategory.METER, IGregTechMachine.class, ItemMeterCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> ITEM_VALVE = register("item_valve", CoverCategory.IO, BlockEntity.class, ValveCover::new);
    public static final RegistryObject<CoverType<IGregTechMachine>> LIQUID_METER = register("liquid_meter", CoverCategory.METER, IGregTechMachine.class, LiquidMeterCover::new);
    public static final RegistryObject<CoverType<IGregTechMachine>> MACHINE_CONTROLLER = register("machine_controller", CoverCategory.CONTROLLER, IGregTechMachine.class, MachineControllerCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> NORMAL = register("normal", CoverCategory.GENERIC, BlockEntity.class, NormalCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> PUMP_MODULE = register("pump_module", CoverCategory.IO, BlockEntity.class, PumpCover::new);
    public static final RegistryObject<CoverType<IGregTechMachine>> REDSTONE_CONDUCTOR = register("redstone_conductor", CoverCategory.UTIL, IGregTechMachine.class, RedstoneConductorCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> REDSTONE_ONLY = register("redstone_only", CoverCategory.OTHER, BlockEntity.class, RedstoneOnlyCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> REDSTONE_SIGNALIZER = register("redstone_signalizer", CoverCategory.METER, BlockEntity.class, RedstoneSignalizerCover::new);
    public static final RegistryObject<CoverType<BlockEntity>> SCREEN = register("screen", CoverCategory.UTIL, BlockEntity.class, ScreenCover::new);
    public static final RegistryObject<CoverType<IElectricMachine>> SOLAR_PANEL = register("solar_panel", CoverCategory.ENERGY, IElectricMachine.class, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 1, 0.25));
    public static final RegistryObject<CoverType<IElectricMachine>> SOLAR_PANEL_LV = register("solar_panel_lv", CoverCategory.ENERGY, IElectricMachine.class, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 8, 1));
    public static final RegistryObject<CoverType<IElectricMachine>> SOLAR_PANEL_MV = register("solar_panel_mv", CoverCategory.ENERGY, IElectricMachine.class, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 64, 8));
    public static final RegistryObject<CoverType<IElectricMachine>> SOLAR_PANEL_HV = register("solar_panel_hv", CoverCategory.ENERGY, IElectricMachine.class, (type, machine, side, item) -> new SolarPanelCover(type, machine, side, item, 512, 64));
    public static final RegistryObject<CoverType<IMachineProgress>> VENT = register("vent", CoverCategory.OTHER, IMachineProgress.class, VentCover::new);

    public static void init(IEventBus bus) {
        COVER_TYPES.register(bus);
    }
    
    private static <T> RegistryObject<CoverType<T>> register(String name, CoverCategory category, Class<T> coverableClass, CoverType.CoverSupplier<T> factory) {
        return COVER_TYPES.register(name, () -> new CoverType<>(category, coverableClass, factory));
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> c(Class<?> cls) {
        return (Class<T>) cls;
    }
}
