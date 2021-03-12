package mods.gregtechmod.objects.items.tools;

import ic2.core.IC2;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.objects.items.base.ItemHammer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHardHammer extends ItemHammer {

    public ItemHardHammer(String material, String description, int durability, int entityDamage) {
        super(material, description, durability, entityDamage);
        this.effectiveAganist.add("minecraft:villager_golem");
        this.effectiveAganist.add("twilightforest:tower_golem");
        this.effectiveAganist.add("thaumcraft:golem");
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        stack = stack.copy();
        if (stack.attemptDamageItem(4, GtUtil.RANDOM, null)) return ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Used to craft plates from ingots");
        tooltip.add("Can rotate some blocks as well");
        tooltip.add("Also used to toggle general machine states");
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof IGregtechMachine) {
                boolean input = ((IGregtechMachine) tileEntity).isInputEnabled();
                boolean output = ((IGregtechMachine) tileEntity).isOutputEnabled();

                if (input = !input) output = !output;

                if (input) ((IGregtechMachine) tileEntity).enableInput();
                else ((IGregtechMachine) tileEntity).disableInput();

                if (output) ((IGregtechMachine) tileEntity).enableOutput();
                else ((IGregtechMachine) tileEntity).disableOutput();

                IC2.platform.messagePlayer(player, "Auto-Input: "+(input ? "Enabled" : "Disabled")+"  Auto-Output: "+(output ? "Enabled" : "Disabled"));
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
