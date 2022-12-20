package dev.su5ed.gregtechmod.compat;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.recipe.CellType;
import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.EntryStream;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ModHandler {
    public static final String IC2_MODID = "ic2";
    public static final String RAILCRAFT_MODID = "railcraft";
    public static final String TWILIGHT_FOREST_MODID = "twilightforest";
    public static final String THERMAL_MODID = "thermal";

    public static final Map<String, BaseMod.Provider> BASE_MODS = Map.of( // More mods to come
        IC2_MODID, new IC2BaseMod.Provider()
    );
    private static BaseMod.Provider activeBaseModProvider;
    private static BaseMod activeBaseMod;
    
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

        Optional<BaseMod.Provider> baseMod = EntryStream.of(BASE_MODS)
            .filterKeys(list::isLoaded)
            .values()
            .findFirst();
        if (baseMod.isEmpty()) {
            if (!DatagenModLoader.isRunningDataGen()) {
                throw new IllegalStateException("At least one of the following base mods is required: " + BASE_MODS.keySet());
            }
        }
        else {
            activeBaseModProvider = baseMod.get();
            activeBaseMod = activeBaseModProvider.createBaseMod();
        }
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return isEnergyItem(stack.getItem());
    }

    public static boolean isEnergyItem(Item item) {
        return activeBaseMod.isEnergyItem(item);
    }

    public static double getEnergyCharge(ItemStack stack) {
        return activeBaseMod.getEnergyCharge(stack);
    }

    public static double getChargeLevel(ItemStack stack) {
        return activeBaseMod.getChargeLevel(stack);
    }

    public static boolean canUseEnergy(ItemStack stack, double energy) {
        return activeBaseMod.canUseEnergy(stack, energy);
    }

    public static boolean useEnergy(ItemStack stack, double energy, @Nullable LivingEntity user) {
        return activeBaseMod.useEnergy(stack, energy, user);
    }

    @Nullable
    public static String getEnergyTooltip(ItemStack stack) {
        return activeBaseMod.getEnergyTooltip(stack);
    }

    public static void depleteStackEnergy(ItemStack stack) {
        activeBaseMod.depleteStackEnergy(stack);
    }

    public static List<ItemStack> getChargedVariants(Item item) {
        return activeBaseMod.getChargedVariants(item);
    }
    
    public static ItemStack getChargedStack(Item item, double charge) {
        return activeBaseMod.getChargedStack(item, charge);
    }
    
    public static double chargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return activeBaseMod.chargeStack(stack, amount, tier, ignoreTransferLimit, simulate);
    }
    
    public static double dischargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        return activeBaseMod.dischargeStack(stack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    public static boolean matchCellType(CellType type, ItemStack stack) {
        // TODO Universal Fluid Cell
        return !stack.hasTag() && (type == CellType.CELL && stack.is(GregTechTags.EMPTY_FLUID_CELL) || type == CellType.FUEL_CAN && stack.is(GregTechTags.EMPTY_FUEL_CAN));
    }
    
    public static Item getModItem(String name) {
        String mapped = activeBaseModProvider.mapItemName(name);
        ResourceLocation location = new ResourceLocation(activeBaseModProvider.getModid(), mapped);
        return ForgeRegistries.ITEMS.getValue(location);
    }
    
    public static Map<String, ResourceLocation> getAllModItems(String name) {
        return EntryStream.of(BASE_MODS)
            .mapToValue((modid, provider) -> {
                String mapped = provider.mapItemName(name);
                return new ResourceLocation(modid, mapped);
            })
            .toMap();
    }
    
    public static void registerCrops() {
        if (ic2Loaded) {
            CropLoader.registerCrops();
        }
    }

    private ModHandler() {}
}
