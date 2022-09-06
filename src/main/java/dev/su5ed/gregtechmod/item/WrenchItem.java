package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.GregTechConfig;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.JavaUtil;
import ic2.api.item.IEnhancedOverlayProvider;
import ic2.api.tile.IWrenchable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class WrenchItem extends ToolItem implements IEnhancedOverlayProvider {
    private final int rotateCost;
    private final int removeCost;
    
    public WrenchItem(int durability, int attackDamage) {
        this(new ToolItemProperties<>()
            .attackDamage(attackDamage)
            .selfDamageOnHit(3)
            .durability(durability)
            .effectiveAganist(GregTechTags.WRENCH_EFFECTIVE)
            .multiDescription(i -> "wrench", 2), 1, 10);
    }

    public WrenchItem(ToolItemProperties<?> properties, int rotateCost, int removeCost) {
        super(properties.setNoEnchant().multiDescription(item -> "wrench", 2));
        this.rotateCost = rotateCost;
        this.removeCost = removeCost;
    }

    @Override
    public boolean providesEnhancedOverlay(Level level, BlockPos pos, Direction side, Player player, ItemStack stack) {
        return ModHandler.ic2Loaded && IC2WrenchHandler.providesEnhancedOverlay(level, pos, player);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return ModHandler.ic2Loaded ? IC2WrenchHandler.onItemUseFirst(this, stack, context) : super.onItemUseFirst(stack, context);
    }
    
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.hurt(8, JavaUtil.RANDOM, null) ? ItemStack.EMPTY : copy;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        if (ModHandler.buildcraftLoaded) {
            components.add(GtLocale.itemKey("wrench", "description_bc").toComponent().withStyle(ChatFormatting.GRAY));
        }
    }

    protected boolean canUseWrench(ItemStack stack, int amount) {
        return true;
    }
    
    protected void useWrench(ItemStack stack, int amount, Player player, InteractionHand hand) {
        GtUtil.hurtStack(stack, amount, player, hand);
    }

    public static Direction rotateWrenchSide(Direction side, double x, double y, double z) {
        return switch (side) {
            case DOWN, UP -> rotateSide(side, x, z, Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH);
            case NORTH, SOUTH -> rotateSide(side, x, y, Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP);
            case WEST, EAST -> rotateSide(side, z, y, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP);
        };
    }
    
    private static Direction rotateSide(Direction side, double x, double y, Direction left, Direction right, Direction down, Direction up) {
        Direction back = side.getOpposite();
        if (x <= 0.25D) {
            return y < 0.25D || y > 0.75D ? back : left;
        }
        if (x > 0.75D) {
            return y < 0.25D || y > 0.75D ? back : right;
        }
        return y < 0.25D ? down : y > 0.75D ? up : side;
    }
    
    private static void dropItem(ItemStack stack, Level level, BlockPos pos) {
        ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }
    
    private static class IC2WrenchHandler {
        public static boolean providesEnhancedOverlay(Level level, BlockPos pos, Player player) {
            if (GregTechConfig.COMMON.enhancedWrenchOverlay.get()) {
                Block block = level.getBlockState(pos).getBlock();
                return block instanceof IWrenchable wrenchable && Arrays.stream(Direction.values())
                    .anyMatch(dir -> wrenchable.canSetFacing(level, pos, dir, player));
            }
            return false;
        }
        
        public static InteractionResult onItemUseFirst(WrenchItem wrench, ItemStack stack, UseOnContext context) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            Player player = context.getPlayer();
            InteractionHand hand = context.getHand();

            if (wrench.canUseWrench(stack, wrench.rotateCost) && block instanceof IWrenchable wrenchable) {
                Direction face = wrenchable.getFacing(level, pos);
                Vec3 vec = context.getClickLocation();
                double x = vec.x() - pos.getX();
                double y = vec.y() - pos.getY();
                double z = vec.z() - pos.getZ();
                Direction rotated = rotateWrenchSide(context.getClickedFace(), x, y, z);

                if (rotated != face && wrenchable.setFacing(level, pos, rotated, player)) {
                    wrench.useWrench(stack, wrench.rotateCost, player, hand);
                    return InteractionResult.SUCCESS;
                }

                if (wrenchable.wrenchCanRemove(level, pos, player) && wrench.canUseWrench(stack, wrench.removeCost)) {
                    wrench.useWrench(stack, wrench.removeCost, player, hand);
                    if (!level.isClientSide) {
                        block.playerWillDestroy(level, pos, state, player);
                        if (level.removeBlock(pos, false)) {
                            block.destroy(level, pos, state);
                        }
                        wrenchable.getWrenchDrops(level, pos, state, level.getBlockEntity(pos), player, 0)
                            .forEach(drop -> dropItem(drop, level, pos));
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }
}
