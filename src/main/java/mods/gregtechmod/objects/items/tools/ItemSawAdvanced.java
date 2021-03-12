package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricCrafting;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IShearable;

import java.util.EnumSet;
import java.util.List;

public class ItemSawAdvanced extends ItemToolElectricCrafting {

    public ItemSawAdvanced() {
        super("saw_advanced", "For sawing logs into planks", 1000, 12, 128000, 3, 200, false, 4, EnumSet.of(ToolClass.Axe, ToolClass.Sword, ToolClass.Shears));
        setRegistryName("saw_advanced");
        setTranslationKey("saw_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 12;
        this.showTier = false;
        this.hasEmptyVariant = true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (player.world.isRemote || player.capabilities.isCreativeMode) {
            return false;
        }

        Block block = player.world.getBlockState(pos).getBlock();
        if (block instanceof IShearable) {
            IShearable target = (IShearable)block;
            if (target.isShearable(stack, player.world, pos) && ElectricItem.manager.use(stack, this.operationEnergyCost, player)) {
                List<ItemStack> drops = target.onSheared(stack, player.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

                for (ItemStack drop : drops) {
                    float f = 0.7F;
                    double d  = (double)(GtUtil.RANDOM.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(GtUtil.RANDOM.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(GtUtil.RANDOM.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.world, (double)pos.getX() + d, (double)pos.getY() + d1, (double)pos.getZ() + d2, drop);
                    entityitem.setDefaultPickupDelay();
                    player.world.spawnEntity(entityitem);
                }

                player.addStat(StatList.getBlockStats(block));
                player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                return true;
            }
        }
        return false;
    }
}
