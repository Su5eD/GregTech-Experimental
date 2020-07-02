package mods.gregtechmod.common.cover;

import mods.gregtechmod.common.cover.types.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CoverRegistry {
    private static final Map<String, BiFunction<ICoverable, EnumFacing, Function<ItemStack, ICover>>> coverMap = new HashMap<>();
    private static final Map<Class<? extends ICover>, String> nameMap = new HashMap<>();

    public static void init() {
        registerCover("generic", (tile, side) -> stack -> new CoverGeneric(tile, side, stack));
        registerCover("vent", (tile, side) -> stack -> new CoverVent(tile, side, stack));
        registerCover("drain", (tile, side) -> stack -> new CoverDrain(tile, side, stack));
        registerCover("active_detector", (tile, side) -> stack -> new CoverActiveDetector(tile, side, stack));
        registerCover("eu_meter", (tile, side) -> stack -> new CoverEUMeter(tile, side, stack));
        registerCover("item_meter", (tile, side) -> stack -> new CoverItemMeter(tile, side, stack));
        registerCover("liquid_meter", (tile, side) -> stack -> new CoverLiquidMeter(tile, side, stack));
        registerCover("normal", (tile, side) -> stack -> new CoverNormal(tile, side, stack));
        registerCover("machine_controller", (tile, side) -> stack -> new CoverMachineController(tile, side, stack));
        registerCover("screen", (tile, side) -> stack -> new CoverScreen(tile, side, stack));
        registerCover("solar_panel", (tile, side) -> stack -> new CoverSolarPanel(tile, side, stack, 1, 0.25));
        registerCover("solar_panel_lv", (tile, side) -> stack -> new CoverSolarPanel(tile, side, stack, 5, 1));
        registerCover("solar_panel_mv", (tile, side) -> stack -> new CoverSolarPanel(tile, side, stack, 64, 8));
        registerCover("solar_panel_hv", (tile, side) -> stack -> new CoverSolarPanel(tile, side, stack, 512, 64));
        registerCover("crafting", (tile, side) -> stack -> new CoverCrafting(tile, side, stack));
        registerCover("conveyor", (tile, side) -> stack -> new CoverConveyor(tile, side, stack));
        registerCover("pump", (tile, side) -> stack -> new CoverPump(tile, side, stack));
    }

    public static void registerCover(String name, BiFunction<ICoverable, EnumFacing, Function<ItemStack, ICover>> cover) {
        if (coverMap.put(name, cover) != null) throw new IllegalStateException("duplicate name: " + name);
        nameMap.put(cover.apply(null, null).apply(null).getClass(), name);
    }

    public static ICover constructCover(String name, EnumFacing side, ICoverable te, ItemStack stack) {
        BiFunction<ICoverable, EnumFacing, Function<ItemStack, ICover>> constructor = coverMap.get(name);
        return constructor.apply(te, side).apply(stack);
    }

    public static String getCoverName(ICover cover) {
        return nameMap.get(cover.getClass());
    }
}
