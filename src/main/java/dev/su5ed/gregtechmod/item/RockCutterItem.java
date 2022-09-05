package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;

public class RockCutterItem extends ElectricToolItem {

    public RockCutterItem() {
        super(new ElectricToolItemProperties()
            .maxCharge(10000)
            .energyTier(1)
            .operationEnergyCost(500)
            .transferLimit(100)
            .attackDamage(1)
            .destroySpeed(2)
            .dropLevel(2)
            .actions(ToolActions.PICKAXE_DIG)
            .blockTags(BlockTags.MINEABLE_WITH_PICKAXE));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        checkEnchantments(stack);
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        checkEnchantments(stack);
        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        checkEnchantments(stack);
        return super.isFoil(stack);
    }

    private void checkEnchantments(ItemStack stack) {
        if (ModHandler.canUseEnergy(stack, this.operationEnergyCost)) {
            if (!stack.isEnchanted()) {
                stack.enchant(Enchantments.SILK_TOUCH, 3);
            }
        }
        else if (stack.isEnchanted()) {
            stack.getTag().remove("Enchantments");
        }
    }
}
