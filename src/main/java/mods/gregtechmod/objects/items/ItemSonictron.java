package mods.gregtechmod.objects.items;

import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.tool.HandHeldInventory;
import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiSonictron;
import mods.gregtechmod.objects.blocks.tileentities.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerSonictron;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.client.gui.GuiScreen;
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

public class ItemSonictron extends ItemBase implements IHandHeldInventory {

    public ItemSonictron() {
        super("sonictron_portable", null);
        setRegistryName("sonictron_portable");
        setTranslationKey("sonictron_portable");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.inventory.getCurrentItem();
        setCurrentIndex(stack, 0);
        if (IC2.platform.isSimulating()) {
            IC2.platform.launchGui(playerIn, this.getInventory(playerIn, stack));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntitySonictron) {
            TileEntitySonictron sonictron = (TileEntitySonictron) world.getTileEntity(pos);

            if (sonictron != null) {
                ArrayList<ItemStack> inventory = getNBTInventory(stack);
                if (player.isSneaking()) {
                    setNBTInventory(player, stack, sonictron.content);
                } else {
                    copyInventory(inventory.iterator(), sonictron);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        setCurrentIndex(stack, -1);
        return EnumActionResult.PASS;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        int	tickTimer = getTickTimer(stack),
                currentIndex = getCurrentIndex(stack);

        if (tickTimer++%2 == 0 && currentIndex>-1) {
            ArrayList<ItemStack> inventory = getNBTInventory(stack);
            GregTechMod.proxy.doSonictronSound(inventory.get(currentIndex), entityIn.world, entityIn.getPosition());
            if (++currentIndex>63) currentIndex=-1;
        }

        setTickTimer(stack, tickTimer);
        setCurrentIndex(stack, currentIndex);
    }

    public static int getCurrentIndex(ItemStack stack) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        if (stackNBT == null) stackNBT = new NBTTagCompound();
        return stackNBT.getInteger("currentIndex");
    }

    public static ArrayList<ItemStack> getNBTInventory(ItemStack stack) {
        ArrayList<ItemStack> inventory = new ArrayList<>();
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

    public static int getTickTimer(ItemStack stack) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        if (stackNBT == null) stackNBT = new NBTTagCompound();
        return stackNBT.getInteger("tickTimer");
    }

    public static NBTTagCompound setTickTimer(ItemStack stack, int time) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        if (stackNBT == null) stackNBT = new NBTTagCompound();
        stackNBT.setInteger("tickTimer", time);
        return stackNBT;
    }

    public static NBTTagCompound setCurrentIndex(ItemStack stack, int index) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) nbt = new NBTTagCompound();
        nbt.setInteger("currentIndex", index);
        return nbt;
    }

    public static void copyInventory(Iterator<ItemStack> from, IInventory to) {
        for(int i = 0; from.hasNext(); i++) {
            to.setInventorySlotContents(i, from.next());
        }
    }

    public static void setNBTInventory(EntityPlayer player, ItemStack stack, InvSlot slot) {
        HandHeldSonictron inventory = new HandHeldSonictron(player, stack, 64);
        copyInventory(slot.iterator(), inventory);
        inventory.saveAsThrown(stack);
    }

    @Override
    public IHasGui getInventory(EntityPlayer entityPlayer, ItemStack itemStack) {
        return new HandHeldSonictron(entityPlayer, itemStack, 64);
    }

    public static class HandHeldSonictron extends HandHeldInventory {

        public HandHeldSonictron(EntityPlayer player, ItemStack containerStack, int inventorySize) {
            super(player, containerStack, inventorySize);
        }

        @Override
        public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {
            return new ContainerSonictron(this);
        }

        @Override
        public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
            return new GuiSonictron(new ContainerSonictron(this));
        }

        @Override
        public String getName() {
            return "sonictron";
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }
    }
}
