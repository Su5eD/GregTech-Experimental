package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemSprayBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSprayIce extends ItemSprayBase {

    public ItemSprayIce() {
        super("spray_ice", 512, 4, 32, 16);
        setRegistryName("spray_ice");
        setTranslationKey("spray_ice");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.effectiveAganist.add("minecraft:slime");
        this.effectiveAganist.add("tconstruct:blueslime");
        this.effectiveAganist.add("twilightforest:fire_beetle");
        this.effectiveAganist.add("twilightforest:maze_slime");
        this.effectiveAganist.add("twilightforest:slime_beetle");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 400, 2));
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 2));
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        BlockPos offset = pos.offset(side);
        IBlockState state = world.getBlockState(offset);
        Block block = state.getBlock();
        ItemStack stack = player.getHeldItem(hand);
        
        if (block == Blocks.WATER && GtUtil.damageStack(player, stack, 1)) {
            if (!world.isRemote) world.setBlockState(offset, Blocks.ICE.getDefaultState());
            return EnumActionResult.SUCCESS;
        }
        if (block == Blocks.LAVA && GtUtil.damageStack(player, stack, 1)) {
            if (!world.isRemote) world.setBlockState(offset, Blocks.OBSIDIAN.getDefaultState());
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
