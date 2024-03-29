package mods.gregtechmod.objects.items.tools;

import ic2.core.IC2;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemHammer;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRubberHammer extends ItemHammer {

    public ItemRubberHammer() {
        super("rubber", 128, 4);
        setRegistryName("hammer_rubber");
        setTranslationKey("hammer_rubber");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        GregTechAPI.instance().registerSoftHammer(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(GtLocale.translateItem("hammer_rubber.description_2"));
        tooltip.add(GtLocale.translateItem("hammer_rubber.description_3"));
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
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        IC2.platform.playSoundSp("Tools/RubberTrampoline.ogg", 1.0F, 1.0F);
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity te = world.getTileEntity(pos);
        EnumActionResult result;

        if (te instanceof IGregTechMachine && GtUtil.damageStack(player, player.inventory.getCurrentItem(), 1)) {
            ((IGregTechMachine) te).setAllowedToWork(!((IGregTechMachine) te).isAllowedToWork());

            GtUtil.sendMessage(player, GtLocale.buildKeyItem("hammer_rubber", "processing_" + (((IGregTechMachine) te).isAllowedToWork() ? "enabled" : "disabled")));
            result = EnumActionResult.SUCCESS;
        }
        else {
            result = super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }

        if (result == EnumActionResult.SUCCESS) {
            IC2.platform.playSoundSp("Tools/RubberTrampoline.ogg", 1, 1);
        }
        
        return result;
    }
}
