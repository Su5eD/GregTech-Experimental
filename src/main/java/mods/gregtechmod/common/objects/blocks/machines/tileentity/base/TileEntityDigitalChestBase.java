package mods.gregtechmod.common.objects.blocks.machines.tileentity.base;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.TileEntityQuantumChest;
import mods.gregtechmod.common.objects.items.GtUpgradeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class TileEntityDigitalChestBase extends TileEntityCoverable {

    protected GameProfile owner = null;
    protected boolean isPrivate = false;
    private boolean canDoubleClick;
    protected boolean isQuantumChest;
    protected InvSlot mainSlot;
    public long clickTime;
    public int maxItemCount;
    protected int availableSpace;
    private final ItemStackHandler handler;

    public TileEntityDigitalChestBase(int stackLimit, boolean isQuantumChest) {
        this.maxItemCount = stackLimit;
        this.isQuantumChest = isQuantumChest;
        this.mainSlot = new InvSlot(this, "mainSlot", InvSlot.Access.IO, 1);
        this.mainSlot.setStackSizeLimit(stackLimit);
        this.allowedCovers = Sets.newHashSet("generic", "normal", "item_meter", "crafting");
        this.handler = new ItemStackHandler(1) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.isEmpty()) return ItemStack.EMPTY;
                validateSlotIndex(slot);
                availableSpace = maxItemCount - mainSlot.get().getCount();
                ItemStack existing = mainSlot.get();

                if (!existing.isEmpty()) {
                    if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) return stack;
                }

                if (maxItemCount <= existing.getCount()) return stack;

                boolean reachedLimit = stack.getCount() >= maxItemCount;
                int insertCount = Math.min(availableSpace, stack.getCount());
                if (!simulate) {
                    if (existing.isEmpty()) {
                        mainSlot.put(reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, maxItemCount) : stack);
                    } else {
                        mainSlot.get().grow(insertCount);
                    }
                    onContentsChanged(slot);
                }
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - insertCount);
            }
            //TODO: Bugcheck
            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (amount == 0) return ItemStack.EMPTY;

                validateSlotIndex(slot);

                ItemStack existing = mainSlot.get();

                if (existing.isEmpty()) return ItemStack.EMPTY;

                int toExtract = Math.min(amount, existing.getMaxStackSize());

                if (existing.getCount() <= toExtract)
                {
                    if (!simulate)
                    {
                        mainSlot.put(ItemStack.EMPTY);
                        onContentsChanged(slot);
                    }
                    return existing;
                }
                else
                {
                    if (!simulate)
                    {
                        mainSlot.put(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                        onContentsChanged(slot);
                    }
                    return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
                }
            }
        };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return false;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.handler);
        else return super.getCapability(capability, facing);
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) return true;
        ItemStack stack = player.getHeldItem(hand);
        ItemStack slot = mainSlot.get();
        availableSpace = maxItemCount - slot.getCount();
        if (stack.getItem().getRegistryName().toString().contains("wrench")) return true; //TODO: Fix chceck
        if (stack.getItem() instanceof GtUpgradeItem) {
            if (stack.getMetadata() == 8 && !isPrivate &&!world.isRemote) { //has to be executed on server side only
                stack.splitStack(1);
                owner = player.getGameProfile();
                isPrivate = true;
                return true;
            }
            else if (stack.getMetadata() == 7 && !isQuantumChest) { //has to be executed on both sides
                stack.splitStack(1);
                world.removeTileEntity(pos);
                TileEntityQuantumChest te = new TileEntityQuantumChest(slot, isPrivate, player.getGameProfile());
                te.markDirty();
                world.setTileEntity(pos, te);
                ((TileEntityQuantumChest) Objects.requireNonNull(world.getTileEntity(pos))).setFacing(this.getFacing());
                return true;
            }
        }

        if (world.isRemote) return true;

        if (isPrivate) {
            if (!TileEntityUpgradable.checkAccess(owner, player.getGameProfile())) {
                IC2.platform.messagePlayer(player, "Only "+this.owner.getName()+" can access this.");
                return true;
            } else IC2.platform.messagePlayer(player,"access granted for "+player.getGameProfile().getName());
        }

        if (!stack.isEmpty() && availableSpace > 0) {
            int insertCount = Math.min(stack.getCount(), availableSpace);
            boolean equal = areItemsEqual(stack, slot);

            if (!slot.isEmpty() && !equal) {
                IC2.platform.messagePlayer(player, slot.getCount()+" "+slot.getDisplayName());
                return true;
            }
            else if (slot.isEmpty()) {
                ItemStack bStack = stack.copy();
                mainSlot.put(bStack);
                player.setHeldItem(hand, ItemStack.EMPTY);
            }
            else if (equal) {
                slot.grow(insertCount);
                stack.splitStack(insertCount);
            }
            canDoubleClick = true;
            return true;
        }
        //doubleclick mechanics
        long time = world.getWorldTime();
        if (time - clickTime < 30L && canDoubleClick) {
            int totalCount = 0;
            for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                ItemStack currentStack = player.inventory.mainInventory.get(i);
                availableSpace = maxItemCount - slot.getCount() - totalCount;
                if (areItemsEqual(currentStack, mainSlot.get())) {
                    if (availableSpace < 1) break;
                    if (availableSpace >= currentStack.getMaxStackSize()) player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                    else if (availableSpace > 0) player.inventory.mainInventory.set(i, currentStack.splitStack(availableSpace));
                    player.inventory.markDirty();
                    totalCount += Math.min(currentStack.getCount(), availableSpace);
                }
            }
            if (totalCount < 1) return true;
            mainSlot.get().grow(totalCount);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_CHICKEN_EGG, 1.5F));
            canDoubleClick = false;
        }
        else if (!slot.isEmpty()) IC2.platform.messagePlayer(player, slot.getCount()+" "+slot.getDisplayName());
        clickTime = time;
        canDoubleClick = false;
        return true;
    }

    @Override
    protected void onClicked(EntityPlayer player) {
        if(isPrivate && !TileEntityUpgradable.checkAccess(owner, player.getGameProfile())) return;
        ItemStack slot = mainSlot.get();

        if (!slot.isEmpty() && !world.isRemote && player.getActiveHand() == EnumHand.MAIN_HAND) {
            long time = world.getWorldTime();
            if (time - clickTime < 3) return;
            clickTime = time;

            ItemStack output = slot.copy();
            if (player.isSneaking()) output = slot.splitStack(1);
            else output = slot.splitStack(Math.min(64, output.getCount()));
            EntityItem tEntity = new EntityItem(getWorld(), getPos().getX() + getFacing().getXOffset()+0.5, getPos().getY() + getFacing().getYOffset() + 0.5, getPos().getZ()+getFacing().getZOffset()+0.5, output);
            tEntity.motionX = 0;
            tEntity.motionY = 0;
            tEntity.motionZ = 0;
            getWorld().spawnEntity(tEntity);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("ownerProfile")) this.owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("ownerProfile"));
        this.isPrivate = nbt.getBoolean("private");
        if (nbt.hasKey("content")) {
            int count = nbt.getInteger("sCount");
            ItemStack content = new ItemStack((NBTTagCompound) nbt.getTag("content"));
            content.setCount(count);
            mainSlot.put(content);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.owner != null) {
            NBTTagCompound ownerCompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(ownerCompound, this.owner);
            nbt.setTag("ownerProfile", ownerCompound);
            nbt.setBoolean("private", isPrivate);
        }
        if (!mainSlot.isEmpty()) {
            NBTTagCompound tNBT = new NBTTagCompound();
            ItemStack content = mainSlot.get().copy();
            content.setCount(1);
            content.writeToNBT(tNBT);
            nbt.setTag("content", tNBT);
            nbt.setInteger("sCount", mainSlot.get().getCount());
        }
        return super.writeToNBT(nbt);
    }
    private boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata();
    }
}
