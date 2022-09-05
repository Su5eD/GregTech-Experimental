package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.core.block.BlockFoam;
import ic2.core.block.wiring.CableBlock;
import ic2.core.block.wiring.CableFoam;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HardenerSprayItem extends CraftingToolItem {

    public HardenerSprayItem() {
        super(new ToolItemProperties<>()
            .durability(256)
            .autoDescription(), 16, Miscellaneous.EMPTY_SPRAY_CAN.getItem());
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return ModHandler.ic2Loaded ? IC2HardeningHandler.onItemUseFirst(stack, context) : super.onItemUseFirst(stack, context);
    }

    private static class IC2HardeningHandler {
        public static InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);

            if (state.hasProperty(CableBlock.foamProperty) && state.getValue(CableBlock.foamProperty) == CableFoam.NONE && GtUtil.hurtStack(stack, 1, context.getPlayer())) {
                if (!level.isClientSide) {
                    BlockState newState = state.setValue(CableBlock.foamProperty, CableFoam.DEFAULT_HARD);
                    level.setBlockAndUpdate(pos, newState);
                }
                return InteractionResult.SUCCESS;
            }
            if (state.hasProperty(BlockFoam.typeProperty) && GtUtil.hurtStack(stack, 1, context.getPlayer())) {
                if (!level.isClientSide) {
                    BlockState result = state.getValue(BlockFoam.typeProperty).getResult();
                    level.setBlockAndUpdate(pos, result);
                }
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }
    }
}
