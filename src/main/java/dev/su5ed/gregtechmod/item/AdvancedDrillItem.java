package dev.su5ed.gregtechmod.item;

import ic2.api.item.IMiningDrill;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.ToolActions;

public class AdvancedDrillItem extends ElectricToolItem implements IMiningDrill {
    private static final Tier ADVANCED_DRILL_TIER = new ForgeTier(5, 0, 0, 0, 0, null, Ingredient::of);

    public AdvancedDrillItem() {
        super(new ElectricToolItemProperties()
            .maxCharge(128000)
            .energyTier(3)
            .operationEnergyCost(250)
            .showTier(false)
            .hasEmptyVariant(true)
            .attackDamage(8)
            .destroySpeed(35)
            .tier(ADVANCED_DRILL_TIER)
            .actions(ToolActions.PICKAXE_DIG, ToolActions.SHOVEL_DIG)
            .blockTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL)
            .rarity(Rarity.UNCOMMON)
            .autoDescription());
    }

    @Override
    public int energyUse(ItemStack stack, Level level, BlockPos pos, BlockState state) {
        return this.operationEnergyCost / 4;
    }

    @Override
    public int breakTime(ItemStack stack, Level level, BlockPos pos, BlockState state) {
        return (int) this.destroySpeed;
    }

    @Override
    public boolean breakBlock(ItemStack stack, Level level, BlockPos pos, BlockState state) {
        return tryUsePower(stack, this.operationEnergyCost);
    }
}
