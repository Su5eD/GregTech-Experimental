package mods.gregtechmod.objects.items.tools;

import ic2.core.IC2;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.objects.items.base.ItemHammer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRubberHammer extends ItemHammer {

    public ItemRubberHammer(String material, String description, int durability, int attackDamage) {
        super(material, description, durability, attackDamage);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Can enable/disable machines");
        tooltip.add("Can rotate some blocks as well");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        checkEnchantment(stack);
        return false;
    }

    private void checkEnchantment(ItemStack stack) {
        if (!stack.isItemEnchanted()) stack.addEnchantment(Enchantments.KNOCKBACK, 2);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        IC2.platform.playSoundSp("Tools/RubberTrampoline.ogg", 1.0F, 1.0F);
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if(tileEntity instanceof IGregtechMachine) {
            if (((IGregtechMachine) tileEntity).isAllowedToWork()) ((IGregtechMachine) tileEntity).disableWorking();
            else ((IGregtechMachine) tileEntity).enableWorking();
            if (world.isRemote) {
                IC2.platform.messagePlayer(player, "Machine processing: "+(((IGregtechMachine) tileEntity).isAllowedToWork() ? "Enabled" : "Disabled"));
                IC2.platform.playSoundSp("Tools/RubberTrampoline.ogg", 1.0F, 1.0F);
            }
            return EnumActionResult.SUCCESS;
        }

        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
