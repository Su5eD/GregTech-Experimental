package mods.gregtechmod.objects.items.tools;

import ic2.api.crops.ICropTile;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemCraftingTool;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSprayBug extends ItemCraftingTool {

    public ItemSprayBug() {
        super("spray_bug", "A very 'buggy' Spray", 128, 2, ToolMaterial.IRON, 8, 4);
        setRegistryName("spray_bug");
        setTranslationKey("spray_bug");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.effectiveAganist.add("minecraft:spider");
        this.effectiveAganist.add("minecraft:cave_spider");
        this.effectiveAganist.add("twilightforest:hedge_spider");
        this.effectiveAganist.add("twilightforest:king_spider");
        this.effectiveAganist.add("twilightforest:swarm_spider");
        this.effectiveAganist.add("twilightforest:tower_broodling");
        this.effectiveAganist.add("twilightforest:fire_beetle");
        this.effectiveAganist.add("twilightforest:slime_beetle");
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.spray_can_empty.getInstance());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 60, 1));
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 600, 1));
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) return EnumActionResult.PASS;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return EnumActionResult.PASS;
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ICropTile) {
            ItemStack stack = player.inventory.getCurrentItem();
            int cropTile = ((ICropTile)tileEntity).getStorageWeedEX();
            if (cropTile <= 100 && GtUtil.damageStack(player, stack, 1)) {
                ((ICropTile)tileEntity).setStorageWeedEX(cropTile + 100);
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
