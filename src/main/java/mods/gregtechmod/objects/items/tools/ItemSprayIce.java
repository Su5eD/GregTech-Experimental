package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemCraftingTool;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSprayIce extends ItemCraftingTool {

    public ItemSprayIce() {
        super("spray_ice", "Very effective against Slimes", 512, 4, ToolMaterial.IRON, 32, 16);
        setRegistryName("spray_ice");
        setTranslationKey("spray_ice");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.effectiveAganist.add("minecraft:slime");
        this.effectiveAganist.add("tconstruct:blueslime");
        this.effectiveAganist.add("twilightforest:fire_beetle");
        this.effectiveAganist.add("twilightforest:maze_slime");
        this.effectiveAganist.add("twilightforest:fire_beetle");
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.spray_can_empty.getInstance());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(18), 400, 2));
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 400, 2));
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) return EnumActionResult.PASS;
        pos.offset(side);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return EnumActionResult.PASS;
        ItemStack stack = player.inventory.getCurrentItem();
        if (block == Blocks.WATER && GtUtil.damageStack(player, stack, 1)) {
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
            return EnumActionResult.SUCCESS;
        }
        if (block == Blocks.LAVA && GtUtil.damageStack(player, stack, 1)) {
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
