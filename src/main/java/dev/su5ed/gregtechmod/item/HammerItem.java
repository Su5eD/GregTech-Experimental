package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends ToolItem {

    public HammerItem(ToolItemProperties<?> properties) {
        super(properties.selfDamageOnHit(3));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        BlockState rotated = state.rotate(level, pos, Rotation.CLOCKWISE_90);
        if (rotated != state) {
            level.setBlock(pos, rotated, Block.UPDATE_ALL);
            GtUtil.hurtStack(stack, 1, context.getPlayer(), context.getHand());
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
