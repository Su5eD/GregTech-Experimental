package dev.su5ed.gregtechmod.util;

import com.google.common.base.Preconditions;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class GtUtil {
    public static final Collection<Direction> VERTICAL_FACINGS = EnumSet.of(Direction.DOWN, Direction.UP);
    public static final Collection<Direction> HORIZONTAL_FACINGS = EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    private GtUtil() {}

    public static boolean isVerticalFacing(Direction direction) {
        return VERTICAL_FACINGS.contains(direction);
    }

    public static boolean isHorizontalFacing(Direction direction) {
        return HORIZONTAL_FACINGS.contains(direction);
    }

    public static Material getMaterial(ResourceLocation location) {
        return new Material(InventoryMenu.BLOCK_ATLAS, location);
    }

    public static void sendActionBarMessage(Player player, GtLocale.TranslationKey key, Object... args) {
        if (player instanceof ServerPlayer serverPlayer) serverPlayer.displayClientMessage(key.toComponent(args), true);
    }

    public static ResourceLocation getCoverTexture(String name) {
        return location("block", "cover", name);
    }

    public static FluidStack drainBlock(BlockEntity blockEntity, Direction side, int amount, FluidAction action) {
        return blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            .map(handler -> handler.drain(amount, action))
            .orElse(FluidStack.EMPTY);
    }

    public static int getStrongestSignal(ICoverable te, Level level, BlockPos pos, Direction excludeFacing) {
        return StreamEx.of(Direction.values())
            .filter(side -> excludeFacing == null || side != excludeFacing)
            .mapToInt(side -> getSignalFromSide(side, level, pos, te))
            .max()
            .orElse(0);
    }

    public static int getSignalFromSide(Direction side, Level level, BlockPos pos, ICoverable be) {
        int power = level.getSignal(pos.relative(side), side);

        return be.getCoverAtSide(side)
            .map(cover -> cover.letsRedstoneIn() ? Math.max(power, cover.getRedstoneInput()) : 0)
            .orElse(power);
    }

    public static void damageEntity(LivingEntity target, LivingEntity attacker, float damage) {
        int oldHurtResistanceTime = target.invulnerableTime;
        target.invulnerableTime = 0;
        target.hurt(DamageSource.mobAttack(attacker), damage);
        target.invulnerableTime = oldHurtResistanceTime;
    }
    
    public static void hurtStack(ItemStack stack, int damage, Player player) {
        if (player instanceof ServerPlayer sp && !player.isCreative()) {
            stack.hurt(damage, player.getRandom(), sp);
        }
    }
    
    public static void assertServerSide(@Nullable Level level) {
        Preconditions.checkState(level == null || !level.isClientSide, "Must only be called on the server side");
    }
    
    public static void assertClientSide(@Nullable Level level) {
        Preconditions.checkState(level == null || level.isClientSide, "Must only be called on the client side");
    }
}
