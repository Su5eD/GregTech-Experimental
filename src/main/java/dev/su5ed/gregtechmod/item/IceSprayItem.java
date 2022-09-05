package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class IceSprayItem extends CraftingToolItem {

    public IceSprayItem() {
        super(new ToolItemProperties<>()
            .attackDamage(4)
            .selfDamageOnHit(16)
            .effectiveAganist(GregTechTags.ICE_SPRAY_EFFECTIVE)
            .durability(512)
            .autoDescription(), 32, Miscellaneous.EMPTY_SPRAY_CAN.getItem());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 2));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 2));
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos offset = context.getClickedPos().relative(context.getClickedFace());
        BlockState state = level.getBlockState(offset);
        Block block = state.getBlock();
        Player player = context.getPlayer();

        if (!state.isAir()) {
            if (block == Blocks.WATER && GtUtil.hurtStack(stack, 1, player)) {
                if (!level.isClientSide) level.setBlockAndUpdate(offset, Blocks.ICE.defaultBlockState());
                return InteractionResult.SUCCESS;
            }
            if (block == Blocks.LAVA && GtUtil.hurtStack(stack, 1, player)) {
                if (!level.isClientSide) level.setBlockAndUpdate(offset, Blocks.OBSIDIAN.defaultBlockState());
                return InteractionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}
