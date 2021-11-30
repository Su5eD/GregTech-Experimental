package mods.gregtechmod.objects.items;

import ic2.core.IC2;
import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerSonictron;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemSonictron extends ItemBase {

    public ItemSonictron() {
        super("sonictron_portable", JavaUtil.NULL_SUPPLIER);
        setFolder("tool");
        setRegistryName("sonictron_portable");
        setTranslationKey("sonictron_portable");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.inventory.getCurrentItem();
        if (IC2.platform.isSimulating() && !playerIn.isSneaking()) {
            setCurrentIndex(stack, 0);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (!world.isRemote) {
            setCurrentIndex(stack, -1);
            if (world.getTileEntity(pos) instanceof TileEntitySonictron) {
                TileEntitySonictron sonictron = (TileEntitySonictron) world.getTileEntity(pos);
                List<ItemStack> inventory = getNBTInventory(stack);

                if (player.isSneaking()) setNBTInventory(stack, sonictron);
                else copyInventory(inventory.iterator(), sonictron);
                
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote) {
            int	currentIndex = getCurrentIndex(stack);
            if (world.getWorldTime() % 2 == 0 && currentIndex > -1) {
                List<ItemStack> inventory = getNBTInventory(stack);
                if (inventory.isEmpty() || currentIndex >= inventory.size()) return;
                
                TileEntitySonictron.doSonictronSound(inventory.get(currentIndex), entity.world, entity.getPosition());
                
                if (++currentIndex > 63) {
                    if (entity instanceof EntityPlayer && ((EntityPlayer)entity).openContainer instanceof ContainerSonictron) currentIndex = 0;
                    else currentIndex = -1;
                }
                setCurrentIndex(stack, currentIndex);
            }
        }
    }

    public static int getCurrentIndex(ItemStack stack) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);
        return stackNBT.getInteger("currentIndex");
    }

    public static List<ItemStack> getNBTInventory(ItemStack stack) {
        List<ItemStack> inventory = new ArrayList<>();
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        NBTTagList contentList = nbt.getTagList("Items", 10);

        for(int i = 0; i < contentList.tagCount(); ++i) {
            NBTTagCompound slotNbt = contentList.getCompoundTagAt(i);
            int slot = slotNbt.getByte("Slot");
            if (slot >= 0 && slot < 64) {
                inventory.add(new ItemStack(slotNbt));
            }
        }
        return inventory;
    }

    public static void setCurrentIndex(ItemStack stack, int index) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger("currentIndex", index);
    }

    public static void copyInventory(Iterator<ItemStack> from, IInventory to) {
        for(int i = 0; from.hasNext(); i++) {
            to.setInventorySlotContents(i, from.next());
        }
    }

    public static void setNBTInventory(ItemStack stack, IInventory inventory) {
        NBTTagCompound stackNBT = StackUtil.getOrCreateNbtData(stack);
        
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack invStack = inventory.getStackInSlot(i);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("Slot", (byte) i);
            invStack.writeToNBT(tag);
            tagList.appendTag(tag);
        }
        stackNBT.setTag("Items", tagList);
        stack.setTagCompound(stackNBT);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        setCurrentIndex(stack, -1);
    }
}
