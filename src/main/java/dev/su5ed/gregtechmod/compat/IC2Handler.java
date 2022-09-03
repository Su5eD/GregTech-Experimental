package dev.su5ed.gregtechmod.compat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IWrenchable;
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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class IC2Handler {

    static List<ItemStack> getChargedVariants(Item item) {
        return Arrays.asList(getChargedStack(item, 0), getChargedStack(item, Double.MAX_VALUE));
    }

    static boolean isEnergyItem(Item item) {
        return item instanceof IElectricItem;
    }

    static double getCharge(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack);
    }

    static double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getChargeLevel(stack);
    }

    static boolean canUse(ItemStack stack, double energy) {
        return ElectricItem.manager.canUse(stack, energy);
    }

    static boolean use(ItemStack stack, double energy, @Nullable LivingEntity user) {
        return ElectricItem.manager.use(stack, energy, user);
    }

    static String getEnergyTooltip(ItemStack stack) {
        return ElectricItem.manager.getToolTip(stack);
    }

    static void depleteStackEnergy(ItemStack stack) {
        if (stack.getItem() instanceof IElectricItem) {
            ElectricItem.manager.discharge(stack, Double.MAX_VALUE, Integer.MAX_VALUE, true, false, false);
        }
    }

    static boolean isWrenchable(Block block) {
        return block instanceof IWrenchable;
    }

    static boolean canSetFacing(Block block, Level level, BlockPos pos, Direction direction, Player player) {
        return ((IWrenchable) block).canSetFacing(level, pos, direction, player);
    }

    static Direction getFacing(Block block, Level level, BlockPos pos) {
        return ((IWrenchable) block).getFacing(level, pos);
    }

    static boolean setFacing(Block block, Level level, BlockPos pos, Direction side, Player player) {
        return ((IWrenchable) block).setFacing(level, pos, side, player);
    }
    
    static boolean wrenchCanRemove(Block block, Level level, BlockPos pos, Player player) {
        return ((IWrenchable) block).wrenchCanRemove(level, pos, player);
    }
    
    static List<ItemStack> getWrenchDrops(Block block, Level level, BlockPos pos, BlockState state, BlockEntity be, Player player, int fortune) {
        return ((IWrenchable) block).getWrenchDrops(level, pos, state, be, player, fortune);
    }

    private static ItemStack getChargedStack(Item item, double charge) {
        ItemStack stack = new ItemStack(item);
        ElectricItem.manager.charge(stack, charge, Integer.MAX_VALUE, true, false);
        return stack;
    }

    private IC2Handler() {}
}
