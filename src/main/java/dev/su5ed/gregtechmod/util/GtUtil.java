package dev.su5ed.gregtechmod.util;

import com.google.common.base.Preconditions;
import dev.su5ed.gregtechmod.Capabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.registries.RegistryManager;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class GtUtil {
    public static final Collection<Direction> ALL_FACINGS = EnumSet.allOf(Direction.class);
    public static final Collection<Direction> NORTH_FACING = EnumSet.of(Direction.NORTH);
    public static final Collection<Direction> VERTICAL_FACINGS = EnumSet.of(Direction.DOWN, Direction.UP);
    public static final Collection<Direction> HORIZONTAL_FACINGS = EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    public static final RandomSource RANDOM = RandomSource.create();

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
        player.displayClientMessage(key.toComponent(args), true);
    }

    public static ResourceLocation getCoverTexture(String name) {
        return location("block", "cover", name);
    }

    public static FluidStack drainBlock(BlockEntity blockEntity, Direction side, int amount, FluidAction action) {
        return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            .map(handler -> handler.drain(amount, action))
            .orElse(FluidStack.EMPTY);
    }
    
    public static BlockEntity getNeighborFluidBlockEntity(BlockEntity be, Direction side) {
        BlockEntity neighbor = be.getLevel().getBlockEntity(be.getBlockPos().relative(side));
        return neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, side.getOpposite()).isPresent() ? neighbor : null;
    }

    public static int getStrongestSignal(BlockEntity be, Level level, BlockPos pos, Direction excludeFacing) {
        return StreamEx.of(Direction.values())
            .filter(side -> excludeFacing == null || side != excludeFacing)
            .mapToInt(side -> getSignalFromSide(side, level, pos, be))
            .max()
            .orElse(0);
    }

    public static int getSignalFromSide(Direction side, Level level, BlockPos pos, BlockEntity be) {
        int power = level.getSignal(pos.relative(side), side);

        return be.getCapability(Capabilities.COVER_HANDLER).resolve()
            .flatMap(handler -> handler.getCoverAtSide(side))
            .map(cover -> cover.letsRedstoneIn() ? Math.max(power, cover.getRedstoneInput()) : 0)
            .orElse(power);
    }
    
    public static void updateRender(BlockEntity be) {
        Level level = be.getLevel();
        if (level.isClientSide) be.requestModelDataUpdate();
        BlockState state = be.getBlockState();
        level.sendBlockUpdated(be.getBlockPos(), state, state, Block.UPDATE_IMMEDIATE);
    }
    
    public static void damageEntity(LivingEntity target, LivingEntity attacker, float damage) {
        int oldHurtResistanceTime = target.invulnerableTime;
        target.invulnerableTime = 0;
        target.hurt(DamageSource.mobAttack(attacker), damage);
        target.invulnerableTime = oldHurtResistanceTime;
    }
    
    public static boolean hurtStack(ItemStack stack, int damage, Player player) {
        return hurtStack(stack, damage, player, p -> {});
    }

    public static boolean hurtStack(ItemStack stack, int damage, Player player, InteractionHand hand) {
        return hurtStack(stack, damage, player, p -> p.broadcastBreakEvent(hand));
    }

    public static boolean hurtStack(ItemStack stack, int damage, Player player, Consumer<Player> onBroken) {
        if (player.level.isClientSide) return true;

        int value = stack.getDamageValue();
        int maxDamage = stack.getMaxDamage();
        if (maxDamage > 0 && value + damage <= maxDamage) {
            stack.hurtAndBreak(damage, player, onBroken);
            return true;
        }
        return false;
    }
    
    public static boolean stackEquals(ItemStack first, ItemStack second) {
        return stackEquals(first, second, true);
    }
    
    public static boolean stackEquals(ItemStack first, ItemStack second, boolean matchTag) {
        return first.sameItem(second)
            && (!matchTag || first.hasTag() == second.hasTag() && first.hasTag() && first.getTag().equals(second.getTag()));
    }
    
    public static void assertServerSide(@Nullable Level level) {
        Preconditions.checkState(level == null || !level.isClientSide, "Must only be called on the server side");
    }
    
    public static void assertClientSide(@Nullable Level level) {
        Preconditions.checkState(level == null || level.isClientSide, "Must only be called on the client side");
    }
    
    public static double calculateTransferLimit(int tier) {
        return (1 << tier) * 128;
    }
    
    public static void appendEnergyHoverText(List<Component> components, int energyTier, List<MutableComponent> description, boolean showTier, boolean isEmpty) {
        if (showTier && energyTier > 0) {
            components.add(GtLocale.key("info", "energy_tier").toComponent(energyTier).withStyle(ChatFormatting.GRAY));
        }

        List<MutableComponent> list = new ArrayList<>(description);
        if (!list.isEmpty() && isEmpty) {
            list.set(0, GtLocale.key("info", "empty").toComponent());
        }
        list.forEach(component -> components.add(component.withStyle(ChatFormatting.GRAY)));
    }
    
    public static void transportFluid(BlockEntity from, Direction fromSide, BlockEntity to, int amount) {
        transportFluid(from, fromSide, to, fromSide.getOpposite(), amount);
    }
    
    public static void transportFluid(BlockEntity from, Direction fromSide, BlockEntity to, Direction toSide, int amount) {
        from.getCapability(ForgeCapabilities.FLUID_HANDLER, fromSide).ifPresent(source ->
            to.getCapability(ForgeCapabilities.FLUID_HANDLER, toSide).ifPresent(destination ->
                FluidUtil.tryFluidTransfer(destination, source, amount, true)));
    }
    
    public static ResourceLocation guiTexture(String name) {
        return location("textures/gui/" + name + ".png");
    }
    
    public static String registryName(String... names) {
        return String.join("_", names);
    }
    
    public static <T> T getFirstTagEntry(TagKey<T> tag) {
        return RegistryManager.ACTIVE.<T>getRegistry(tag.registry().location()).tags().getTag(tag)
            .stream()
            .findFirst()
            .orElseThrow();
    }
}
