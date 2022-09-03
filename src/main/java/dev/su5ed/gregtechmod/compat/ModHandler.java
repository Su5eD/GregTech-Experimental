package dev.su5ed.gregtechmod.compat;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.recipe.CellType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ModHandler {
    public static final String IC2_MODID = "ic2";
    public static final String RAILCRAFT_MODID = "railcraft";
    public static final String TWILIGHT_FOREST_MODID = "twilightforest";

    public static final List<String> BASE_MODS = List.of(IC2_MODID); // More mods to come
    public static boolean ic2Loaded;
    public static boolean buildcraftLoaded;
    public static boolean railcraftLoaded;

    public static void initMods() {
        ModList list = ModList.get();
        ic2Loaded = list.isLoaded(IC2_MODID);
        railcraftLoaded = list.isLoaded(RAILCRAFT_MODID);

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
        return ic2Loaded ? IC2Handler.getChargedVariants(item) : List.of();
    }

    public static boolean isWrenchable(Block block) {
        return ic2Loaded && IC2Handler.isWrenchable(block);
    }

    public static boolean canWrenchSetFacing(Block block, Level level, BlockPos pos, Direction direction, Player player) {
        return ic2Loaded && IC2Handler.canSetFacing(block, level, pos, direction, player);
    }

    public static Direction getFacing(Block block, Level level, BlockPos pos) {
        if (ic2Loaded) {
            return IC2Handler.getFacing(block, level, pos);
        }
        throw new IllegalStateException("IC2 is not loaded");
    }

    public static boolean setFacing(Block block, Level level, BlockPos pos, Direction side, Player player) {
        return ic2Loaded && IC2Handler.setFacing(block, level, pos, side, player);
    }

    public static boolean wrenchCanRemove(Block block, Level level, BlockPos pos, Player player) {
        return ic2Loaded && IC2Handler.wrenchCanRemove(block, level, pos, player);
    }

    public static List<ItemStack> getWrenchDrops(Block block, Level level, BlockPos pos, BlockState state, BlockEntity be, Player player, int fortune) {
        return ic2Loaded ? IC2Handler.getWrenchDrops(block, level, pos, state, be, player, fortune) : List.of();
    }

    public static boolean matchCellType(CellType type, ItemStack stack) {
        // TODO Universal Fluid Cell
        return !stack.hasTag() && (type == CellType.CELL && stack.is(GregTechTags.EMPTY_FLUID_CELL) || type == CellType.FUEL_CAN && stack.is(GregTechTags.EMPTY_FUEL_CAN));
    }

    private ModHandler() {}
}
