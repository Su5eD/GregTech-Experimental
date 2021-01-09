package mods.gregtechmod.objects.items.base;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemHammer extends ItemToolBase {
    private static final Set<Block> ROTATABLE_BLOCKS = Sets.newHashSet(Blocks.LOG, Blocks.HAY_BLOCK, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.DROPPER, Blocks.DISPENSER, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.FURNACE, Blocks.LIT_FURNACE, Blocks.CHEST, Blocks.HOPPER);

    public ItemHammer(String material, @Nullable String description, int durability, int entityDamage) {
        super("hammer_"+material, description, durability, entityDamage, ToolMaterial.WOOD);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();
        ItemStack stack = player.getHeldItem(hand);
        if (ROTATABLE_BLOCKS.contains(block)) {
            stack.damageItem(1, player);
            block.rotateBlock(world, pos, side);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
