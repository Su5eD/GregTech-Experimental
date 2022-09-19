package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.api.crops.ICropTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BugSprayItem extends CraftingToolItem {

    public BugSprayItem() {
        super(new ToolItemProperties<>()
            .attackDamage(2)
            .attackSpeed(-3)
            .selfDamageOnHit(4)
            .effectiveAganist(GregTechTags.BUG_SPRAY_EFFECTIVE)
            .durability(128)
            .autoDescription(), 8, Miscellaneous.EMPTY_SPRAY_CAN.getItem());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
        target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 1));
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return ModHandler.ic2Loaded ? IC2BugSprayHandler.onItemUseFirst(stack, context) : super.onItemUseFirst(stack, context);
    }
    
    private static class IC2BugSprayHandler {
        public static InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
            Level level = context.getLevel();
            if (!level.isClientSide) {
                BlockPos pos = context.getClickedPos();
                BlockState state = level.getBlockState(pos);
                if (!state.isAir()) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof ICropTile crop) {
                        int amount = crop.getStorageWeedEX();
                        if (amount <= 100 && GtUtil.hurtStack(stack, 1, context.getPlayer())) {
                            crop.setStorageWeedEX(amount + 100);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
            return InteractionResult.PASS;
        }
    }
}
