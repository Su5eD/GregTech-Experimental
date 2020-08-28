package mods.gregtechmod.api.cover;

import mods.gregtechmod.common.cover.type.*;
import mods.gregtechmod.common.util.TriFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class CoverRegistry {
    private static final Map<String, TriFunction<ICoverable, EnumFacing, ItemStack, ICover>> COVER_MAP = new HashMap<>();
    private static final Map<Class<? extends ICover>, String> NAME_MAP = new HashMap<>();
    
    public static void init() {
        registerCover("generic", CoverGeneric::new);
        registerCover("vent", CoverVent::new);
        registerCover("drain", CoverDrain::new);
        registerCover("active_detector", CoverActiveDetector::new);
        registerCover("eu_meter", CoverEUMeter::new);
        registerCover("item_meter", CoverItemMeter::new);
        registerCover("liquid_meter", CoverLiquidMeter::new);
        registerCover("normal", CoverNormal::new);
        registerCover("machine_controller", CoverMachineController::new);
        registerCover("screen", CoverScreen::new);
        registerCover("solar_panel", (tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 1, 0.25));
        registerCover("solar_panel_lv", (tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 8, 1));
        registerCover("solar_panel_mv", (tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 64, 8));
        registerCover("solar_panel_hv", (tile, side, stack) -> new CoverSolarPanel(tile, side, stack, 512, 64));
        registerCover("crafting", CoverCrafting::new);
        registerCover("conveyor", CoverConveyor::new);
        registerCover("pump_module", CoverPump::new);
        registerCover("item_valve", CoverValve::new);
        registerCover("energy_only", CoverEnergyOnly::new);
        registerCover("redstone_only", CoverRedstoneOnly::new);
        registerCover("redstone_conductor", CoverRedstoneConductor::new);
        registerCover("redstone_signalizer", CoverRedstoneSignalizer::new);
    }

    public static void registerCover(String name, TriFunction<ICoverable, EnumFacing, ItemStack, ICover> cover) {
        if (COVER_MAP.put(name, cover) != null) throw new IllegalStateException("duplicate name: " + name);
        NAME_MAP.put(cover.apply(null, null, null).getClass(), name);
    }

    public static ICover constructCover(String name, EnumFacing side, ICoverable te, ItemStack stack) {
        TriFunction<ICoverable, EnumFacing, ItemStack, ICover> constructor = COVER_MAP.get(name);
        return constructor.apply(te, side, stack);
    }

    public static String getCoverName(ICover cover) {
        return NAME_MAP.get(cover.getClass());
    }
}
