package mods.gregtechmod.objects.items;

import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.tileentities.TileEntitySonictron;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDataOrb extends ItemBase {
    
    public ItemDataOrb() {
        super("data_orb", null);
        setRegistryName("data_orb");
        setTranslationKey("data_orb");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        setFolder("component");
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.getCount() > 1 || world.isRemote) return EnumActionResult.PASS;
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySonictron) {
            TileEntitySonictron sonictron = (TileEntitySonictron) tileEntity;
            ItemStack[] inventory = getNBTInventory(stack);
            if (player.isSneaking()) {
                if (getDataTitle(stack).equals("Sonictron-Data"))
                    copyInventory(sonictron, inventory);
            } else {
                copyInventory(inventory, sonictron);
                setDataTitle(stack, "Sonictron-Data");
                setDataName(stack, "" + inventory.hashCode());
            }
            setNBTInventory(stack, inventory);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!getDataTitle(stack).equals("")) {
            tooltip.add(getDataTitle(stack));
            tooltip.add(getDataName(stack));
        }
    }

    private void copyInventory(ItemStack[] inventory, IInventory newContent) {
        for (int i = 0; i < newContent.getSizeInventory(); i++) {
            inventory[i] = newContent.getStackInSlot(i);
        }
    }

    private void copyInventory(IInventory inventory, ItemStack[] newContent) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, newContent[i]);
        }
    }

    public static String getDataName(ItemStack stack) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);
        return stackNBT.getString("dataName");
    }

    public static String getDataTitle(ItemStack stack) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);
        return stackNBT.getString("dataTitle");
    }

    public static NBTTagCompound setDataName(ItemStack stack, String dataName) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);
        stackNBT.setString("dataName", dataName);
        return stackNBT;
    }

    public static NBTTagCompound setDataTitle(ItemStack stack, String dataTitle) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);
        stackNBT.setString("dataTitle", dataTitle);
        return stackNBT;
    }

    public static ItemStack[] getNBTInventory(ItemStack stack) {
        ItemStack[] inventory = new ItemStack[256];
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);

        NBTTagList stackList = stackNBT.getTagList("Inventory", 10);
        for (int i = 0; i < stackList.tagCount(); i++) {
            NBTTagCompound tag = stackList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0) {
                inventory[slot] = new ItemStack(tag);
            }
        }
        return inventory;
    }

    public static NBTTagCompound setNBTInventory(ItemStack stack, ItemStack[] inventory) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);

        NBTTagList stackList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                item.writeToNBT(tag);
                stackList.appendTag(tag);
            }
        }
        stackNBT.setTag("Inventory", stackList);
        stack.setTagCompound(stackNBT);
        return stackNBT;
    }
}
