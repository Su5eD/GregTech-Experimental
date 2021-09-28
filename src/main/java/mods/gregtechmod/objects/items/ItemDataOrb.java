package mods.gregtechmod.objects.items;

import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IDataOrbSerializable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class ItemDataOrb extends ItemBase {
    
    public ItemDataOrb() {
        super("data_orb", GtUtil.NULL_SUPPLIER);
        setRegistryName("data_orb");
        setTranslationKey("data_orb");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        setFolder("component");
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.getCount() != 1 || world.isRemote) return EnumActionResult.PASS;
        
        TileEntity te = world.getTileEntity(pos);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        String dataTitle = nbt.getString("dataTitle");
        
        if (te instanceof IDataOrbSerializable) {
            String teDataName = ((IDataOrbSerializable) te).getDataName();
            if (player.isSneaking()) {
                if (dataTitle.equals(teDataName)) {
                    NBTTagCompound data = nbt.getCompoundTag("data");
                    ((IDataOrbSerializable) te).loadDataFromOrb(data);
                }
            } else if (teDataName != null) {
                NBTTagCompound data = ((IDataOrbSerializable) te).saveDataToOrb();
                if (data != null) {
                    nbt.setString("dataTitle", teDataName);
                    nbt.setString("dataName", String.valueOf(data.hashCode()));
                    nbt.setTag("data", data);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        
        return EnumActionResult.PASS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        
        String dataTitle = nbt.getString("dataTitle");
        if (!dataTitle.isEmpty()) {
            tooltip.add(dataTitle);
            tooltip.add(nbt.getString("dataName"));
        }
    }
}
