package mods.gregtechmod.objects.items.tools;

import buildcraft.api.tools.IToolWrench;
import ic2.core.IC2;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWrenchBC extends ItemWrench implements IToolWrench {

    public ItemWrenchBC(String name, int durability) {
        super(name, durability);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Works as a BuildCraft wrench, too");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        wrench.damageItem(1, player);
        IC2.audioManager.playOnce(player, "Tools/wrench.ogg");
    }
}
