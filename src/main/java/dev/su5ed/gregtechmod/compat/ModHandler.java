package dev.su5ed.gregtechmod.compat;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.recipe.CellType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ModHandler {
    public static final String IC2_MODID = "ic2";

    public static final List<String> BASE_MODS = List.of(IC2_MODID); // More mods to come
    public static boolean ic2Loaded;
    public static boolean buildcraftLoaded;

    public static void initMods() {
        ModList list = ModList.get();
        ic2Loaded = list.isLoaded(IC2_MODID);
        
        if (!DatagenModLoader.isRunningDataGen() && BASE_MODS.stream().noneMatch(list::isLoaded)) {
            throw new IllegalStateException("At least one of the following base mods is required: " + BASE_MODS);
        }
    }
    
    public static boolean isEnergyItem(ItemStack stack) {
        return ic2Loaded && IC2Handler.isEnergyItem(stack); 
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
    
    public static boolean matchCellType(CellType type, ItemStack stack) {
        // TODO Universal Fluid Cell
        return !stack.hasTag() && (type == CellType.CELL && stack.is(GregTechTags.EMPTY_FLUID_CELL) || type == CellType.FUEL_CAN && stack.is(GregTechTags.EMPTY_FUEL_CAN));
    }

    private ModHandler() {}
}
