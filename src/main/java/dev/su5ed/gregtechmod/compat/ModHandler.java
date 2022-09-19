package dev.su5ed.gregtechmod.compat;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.recipe.CellType;
import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ModHandler {
    public static final String IC2_MODID = "ic2";
    public static final String RAILCRAFT_MODID = "railcraft";
    public static final String TWILIGHT_FOREST_MODID = "twilightforest";
    public static final String THERMAL_MODID = "thermal";

    public static final List<String> BASE_MODS = List.of(IC2_MODID); // More mods to come
    public static boolean ic2Loaded;
    public static boolean buildcraftLoaded;
    public static boolean railcraftLoaded;
    public static boolean thermalLoaded;

    public static void initMods() {
        ModList list = ModList.get();
        ic2Loaded = list.isLoaded(IC2_MODID);
        railcraftLoaded = list.isLoaded(RAILCRAFT_MODID);
        thermalLoaded = list.isLoaded(THERMAL_MODID);
        
        System.setProperty("coremod." + Reference.MODID + ".ic2_loaded", Boolean.toString(ic2Loaded));
        // (ugly) workaround for https://github.com/MinecraftForge/CoreMods/issues/31
        System.setProperty("true", "true");

        if (!DatagenModLoader.isRunningDataGen() && BASE_MODS.stream().noneMatch(list::isLoaded)) {
            throw new IllegalStateException("At least one of the following base mods is required: " + BASE_MODS);
        }
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return isEnergyItem(stack.getItem());
    }

    public static boolean isEnergyItem(Item item) {
        return ic2Loaded && IC2Handler.isEnergyItem(item);
    }

    public static double getEnergyCharge(ItemStack stack) {
        return ic2Loaded ? IC2Handler.getCharge(stack) : 0;
    }

    public static double getChargeLevel(ItemStack stack) {
        return ic2Loaded ? IC2Handler.getChargeLevel(stack) : 0;
    }

    public static boolean canUseEnergy(ItemStack stack, double energy) {
        return ic2Loaded && IC2Handler.canUse(stack, energy);
    }

    public static boolean useEnergy(ItemStack stack, double energy, @Nullable LivingEntity user) {
        return ic2Loaded && IC2Handler.use(stack, energy, user);
    }

    @Nullable
    public static String getEnergyTooltip(ItemStack stack) {
        return ic2Loaded ? IC2Handler.getEnergyTooltip(stack) : null;
    }

    public static void depleteStackEnergy(ItemStack stack) {
        if (ic2Loaded) IC2Handler.depleteStackEnergy(stack);
    }

    public static List<ItemStack> getChargedVariants(Item item) {
        return ic2Loaded ? IC2Handler.getChargedVariants(item) : List.of(new ItemStack(item));
    }
    
    public static ItemStack getChargedStack(Item item, double charge) {
        return ic2Loaded ? IC2Handler.getChargedStack(item, charge) : ItemStack.EMPTY;
    }
    
    public static double chargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return ic2Loaded ? IC2Handler.charge(stack, amount, tier, ignoreTransferLimit, simulate) : 0;
    }

    public static boolean matchCellType(CellType type, ItemStack stack) {
        // TODO Universal Fluid Cell
        return !stack.hasTag() && (type == CellType.CELL && stack.is(GregTechTags.EMPTY_FLUID_CELL) || type == CellType.FUEL_CAN && stack.is(GregTechTags.EMPTY_FUEL_CAN));
    }

    private ModHandler() {}
}
