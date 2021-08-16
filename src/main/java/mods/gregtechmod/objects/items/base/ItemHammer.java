package mods.gregtechmod.objects.items.base;

import com.google.common.collect.Sets;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

    public ItemHammer(String material, String descriptionKey, int durability, int entityDamage) {
        super("hammer_" + material, () -> GtUtil.translateItemDescription(descriptionKey), durability, entityDamage, ToolMaterial.WOOD);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (GtUtil.tryRotateBlock(ROTATABLE_BLOCKS, world, pos, side, player, hand)) return EnumActionResult.SUCCESS;
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
