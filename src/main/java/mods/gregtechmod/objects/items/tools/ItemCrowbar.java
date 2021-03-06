package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCrowbar extends ItemToolBase {

    public ItemCrowbar() {
        super("crowbar", "To remove covers from machines", 256, 6);
        setRegistryName("crowbar");
        setTranslationKey("crowbar");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) return EnumActionResult.PASS;
        TileEntity block = world.getTileEntity(pos);
        if (block instanceof ICoverable) {
            if (((ICoverable)block).removeCover(facing, false)) {
                ItemStack current = player.inventory.getCurrentItem();
                current.damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
