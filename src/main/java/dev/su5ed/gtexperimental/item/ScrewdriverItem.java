package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.GregTechTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class ScrewdriverItem extends ToolItem {

    public ScrewdriverItem() {
        super(new ToolItemProperties<>()
            .attackSpeed(-2.5F)
            .durability(256)
            .multiDescription(3)
            .attackDamage(4)
            .effectiveAganist(GregTechTags.SCREWDRIVER_EFFECTIVE));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!context.getPlayer().isShiftKeyDown()) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof RepeaterBlock || block instanceof ComparatorBlock) {
                block.rotate(state, level, pos, Rotation.CLOCKWISE_90);
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}
