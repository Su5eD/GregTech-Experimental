package mods.gregtechmod.objects.items.tools;

import ic2.api.crops.ICropTile;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemSprayBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSprayHydration extends ItemSprayBase {

    public ItemSprayHydration() {
        super("spray_hydration", 2560, 0, 20, 0);
        setRegistryName("spray_hydration");
        setTranslationKey("spray_hydration");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ICropTile) {
            int crop = ((ICropTile) tileEntity).getStorageWater();
            if (crop <= 100 && GtUtil.damageStack(player, player.inventory.getCurrentItem(), 1)) {
                ((ICropTile) tileEntity).setStorageWater(crop + 100);
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
