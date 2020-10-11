package mods.gregtechmod.objects.items.tools;

import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemDebugScanner extends ItemScanner {

    public ItemDebugScanner() {
        super("debug_scanner", null,1000000000, 0, 4);
        setRegistryName("debug_scanner");
        setFolder("tool");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {}

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/ODScanner.ogg", true, IC2.audioManager.getDefaultVolume());
            return EnumActionResult.PASS;
        }
        if (player instanceof EntityPlayerMP) {
            ArrayList<String> aList = new ArrayList<>();
            getCoordinateScan(aList, player, world, 1, pos, side, hitX, hitY, hitZ);
            for (String s : aList) IC2.platform.messagePlayer(player, s);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
