package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.core.block.wiring.CableBlock;
import ic2.core.block.wiring.CableFoam;
import ic2.core.ref.Ic2Blocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class FoamSprayItem extends CraftingToolItem {

    public FoamSprayItem() {
        super(new ToolItemProperties<>()
            .durability(400)
            .autoDescription(), 25, Miscellaneous.EMPTY_SPRAY_CAN.getItem());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isShiftKeyDown()) {
            switchMode(player.getItemInHand(hand), player);
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return !context.getPlayer().isShiftKeyDown() && ModHandler.ic2Loaded
            ? IC2FoamHandler.onItemUseFirst(stack, context, getMode(stack))
            : super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        if (stack.getOrCreateTag().contains("mode")) {
            components.add(2, getMode(stack).getTooltip());
        }
    }

    public void switchMode(ItemStack stack, Player player) {
        SprayMode mode = getMode(stack).next();
        setMode(stack, mode);
        player.displayClientMessage(mode.getTooltip().withStyle(ChatFormatting.AQUA), true);
    }

    public SprayMode getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("mode")) {
            tag.putString("mode", SprayMode.SINGLE.name());
            return SprayMode.SINGLE;
        }
        return SprayMode.valueOf(tag.getString("mode"));
    }

    private void setMode(ItemStack stack, SprayMode mode) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("mode", mode.name());
    }

    private enum SprayMode {
        SINGLE,
        LINE,
        AREA;

        private static final SprayMode[] VALUES = values();

        public SprayMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public MutableComponent getTooltip() {
            return GtLocale.itemKey("foam_spray", "mode", name().toLowerCase(Locale.ROOT))
                .toComponent();
        }
    }

    private static class IC2FoamHandler {
        public static InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context, SprayMode mode) {
            Level level = context.getLevel();
            Player player = context.getPlayer();
            BlockPos pos = context.getClickedPos();
            Direction side = context.getClickedFace();
            BlockState state = level.getBlockState(pos);
            
            if (state.hasProperty(CableBlock.foamProperty) && state.getValue(CableBlock.foamProperty) == CableFoam.NONE) {
                if (!level.isClientSide) {
                    BlockState newState = state.setValue(CableBlock.foamProperty, CableFoam.SOFT);
                    level.setBlockAndUpdate(pos, newState);
                }
                return InteractionResult.SUCCESS;
            }
            
            pos = pos.relative(side);
            BlockState foamState = Ic2Blocks.FOAM.defaultBlockState();
            int xRot = Math.round(player.getXRot());
            Direction facing;
            if (xRot >= 65) {
                facing = Direction.UP;
            }
            else if (xRot <= -65) {
                facing = Direction.DOWN;
            }
            else {
                facing = switch (Mth.floor(player.getYRot() * 4F / 360F + 0.5) & 3) {
                    case 0 -> Direction.NORTH;
                    case 1 -> Direction.EAST;
                    case 2 -> Direction.SOUTH;
                    case 3 -> Direction.WEST;
                    default -> side;
                };
            }

            boolean success = false;
            switch (mode) {
                case SINGLE -> {
                    if (level.getBlockState(pos).isAir() && GtUtil.hurtStack(stack, 1, player)) {
                        if (!level.isClientSide) level.setBlockAndUpdate(pos, foamState);
                        success = true;
                    }
                }
                case LINE -> {
                    for (int i = 0; i < 4; i++) {
                        if (level.getBlockState(pos).isAir()) {
                            if (!GtUtil.hurtStack(stack, 1, player)) break;
                            if (!level.isClientSide) level.setBlockAndUpdate(pos, foamState);
                            success = true;
                        }
                        pos = pos.subtract(new Vec3i(facing.getStepX(), facing.getStepY(), facing.getStepZ()));
                    }
                }
                case AREA -> {
                    boolean factorX = facing.getStepX() == 0;
                    boolean factorY = facing.getStepY() == 0;
                    boolean factorZ = facing.getStepZ() == 0;
                    pos = pos.subtract(new Vec3i(factorX ? 1 : 0, factorY ? 1 : 0, factorZ ? 1 : 0));
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            BlockPos placePos = new BlockPos(pos.getX() + (factorX ? i : 0), pos.getY() + (!factorX && factorY ? i : 0) + (!factorZ && factorY ? j : 0), pos.getZ() + (factorZ ? j : 0));
                            if (level.getBlockState(placePos).isAir() && GtUtil.hurtStack(stack, 1, player)) {
                                if (!level.isClientSide) level.setBlockAndUpdate(placePos, foamState);
                                success = true;
                            }
                        }
                    }
                }
            }
            return success ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }
}
