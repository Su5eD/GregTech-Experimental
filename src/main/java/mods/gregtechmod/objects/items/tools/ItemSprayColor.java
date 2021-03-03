package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSprayColor extends ItemToolCrafting {
    public final EnumDyeColor color;

    public ItemSprayColor(EnumDyeColor color) {
        super("spray_color_"+color.getName(), "To give the World more Color", 512, 0, 8, 0);
        this.color = color;
        setRegistryName("spray_color_"+color.getName());
        setTranslationKey("spray_color_"+color.getName());
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getInstance());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Enough for dying " + (stack.getMaxDamage() + 1) + " blocks in world " + GtUtil.translate(Reference.MODID+".color."+this.color.getTranslationKey()));
        tooltip.add("Enough for crafting " + ((stack.getMaxDamage() + 1) / this.craftingDamage) + " times");
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) return EnumActionResult.PASS;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return EnumActionResult.PASS;
        ItemStack stack = player.inventory.getCurrentItem();
        if (block == Blocks.WOOL || (block.getRegistryName().toString().equals("thermalfoundation:rockwool"))) {
            int meta = block.getMetaFromState(state);
            int targetMeta = this.color.getMetadata();
            if (block != Blocks.WOOL) targetMeta = 15 - targetMeta;

            if (meta == targetMeta) return EnumActionResult.PASS;
            if (GtUtil.damageStack(player, stack, 1)) world.setBlockState(pos, block.getStateFromMeta(targetMeta));
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
