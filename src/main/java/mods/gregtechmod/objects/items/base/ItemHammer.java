package mods.gregtechmod.objects.items.base;

import com.google.common.collect.Sets;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

public class ItemHammer extends ItemToolBase {
    private static final Collection<Block> ROTATABLE_BLOCKS = Sets.newHashSet(Blocks.LOG, Blocks.HAY_BLOCK, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.DROPPER, Blocks.DISPENSER, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.FURNACE, Blocks.LIT_FURNACE, Blocks.CHEST, Blocks.HOPPER);

    public ItemHammer(String material, int durability, int entityDamage) {
        this(material, "hammer_" + material, durability, entityDamage);
    }

    public ItemHammer(String material, String descriptionKey, int durability, int attackDamage) {
        super("hammer_" + material, () -> GtLocale.translateItemDescription(descriptionKey), durability, attackDamage, ToolMaterial.WOOD);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();
        ItemStack stack = player.getHeldItem(hand);
        if (ROTATABLE_BLOCKS.contains(block)) {
            block.rotateBlock(world, pos, side);
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
