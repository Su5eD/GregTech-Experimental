package mods.gregtechmod.objects.items.tools;

import ic2.api.item.IC2Items;
import ic2.core.block.wiring.TileEntityCable;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemCraftingTool;
import mods.gregtechmod.util.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSprayHardener extends ItemCraftingTool {

    public ItemSprayHardener() {
        super("spray_hardener", "Construction Foam Hardener", 256, 0, ToolMaterial.IRON, 16, 0);
        setRegistryName("spray_hardener");
        setTranslationKey("spray_hardener");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.spray_can_empty.getInstance());
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) return EnumActionResult.PASS;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return EnumActionResult.PASS;
        ItemStack stack = player.inventory.getCurrentItem();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCable) {
            if (((TileEntityCable)tileEntity).isFoamed() && GtUtil.damageStack(player, stack, 1)) {
                ReflectionUtil.hardenCableFoam(tileEntity);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        Item itemFoam = IC2Items.getItem("foam", "normal").getItem(),
             itemWall = IC2Items.getItem("wall", "light_gray").getItem();
        ResourceLocation blockName = block.getRegistryName();
        if (blockName != null && blockName.equals(itemFoam.getRegistryName())) {
            if (GtUtil.damageStack(player, stack, 1))
                world.setBlockState(pos, Block.getBlockFromItem(itemWall).getStateFromMeta(7));
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
