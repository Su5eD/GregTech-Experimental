package mods.gregtechmod.objects.blocks.teblocks.base;

import com.mojang.authlib.GameProfile;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.slot.GtUpgradeSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityQuantumChest;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileEntityDigitalChestBase extends TileEntityCoverBehavior implements IUpgradableMachine, IPanelInfoProvider {
    protected GameProfile owner = null;
    protected boolean isPrivate = false;
    private boolean canDoubleClick;
    protected boolean isQuantumChest;
    protected InvSlot content;
    protected final GtUpgradeSlot upgradeSlot;
    public long clickTime;
    public int maxItemCount;
    protected int availableSpace;

    public TileEntityDigitalChestBase(String descriptionKey, int stackLimit, boolean isQuantumChest) {
        super(descriptionKey);
        this.maxItemCount = stackLimit;
        this.isQuantumChest = isQuantumChest;
        this.content = new InvSlot(this, "mainSlot", InvSlot.Access.IO, 1);
        this.content.setStackSizeLimit(stackLimit);
        this.upgradeSlot = new GtUpgradeSlot(this, "lockUpgradeSlot", InvSlot.Access.NONE, 1);
        
        this.coverBlacklist.add(CoverType.ENERGY);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new BarrelItemStackHandler(facing));
        else return super.getCapability(capability, facing);
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) return true;
        ItemStack stack = player.getHeldItem(hand);
        ItemStack slot = content.get();
        availableSpace = maxItemCount - slot.getCount();
        if (stack.getItem().getRegistryName().toString().contains("wrench")) return true; // FIXME What was I thinking ???

        if (isPrivate && !world.isRemote) { //has to be executed on server side only
            if (!GtUtil.checkAccess(this, owner, player.getGameProfile())) {
                GtUtil.sendMessage(player, Reference.MODID+".info.access_error", owner.getName());
                return true;
            }
        }

        if (this.upgradeSlot.accepts(stack)) {
            Item currentItem = stack.getItem();
            ItemStack upgradeStack = this.upgradeSlot.get();

            if (((IGtUpgradeItem)currentItem).getName().equals("quantum_chest") && !isQuantumChest) { //has to be executed on both sides
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                world.removeTileEntity(pos);
                TileEntityQuantumChest te = new TileEntityQuantumChest(slot, isPrivate, player.getGameProfile());
                te.markDirty();
                world.setTileEntity(pos, te);
                ((TileEntityDigitalChestBase)world.getTileEntity(pos)).setFacing(this.getFacing());
                return true;
            }
            else if (!world.isRemote && ((IGtUpgradeItem)currentItem).canBeInserted(upgradeStack, this)) { //has to be executed server-side only
                if (((IGtUpgradeItem)currentItem).beforeInsert(upgradeStack, this, player)) return true;
                this.upgradeSlot.put(!player.capabilities.isCreativeMode ? stack.splitStack(1) : stack.copy().splitStack(1));
                ((IGtUpgradeItem) currentItem).afterInsert(stack, this, player);
                return true;
            }
        }

        if (super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ) || world.isRemote) return true;

        long time = world.getTotalWorldTime();

        if (!stack.isEmpty() && availableSpace > 0) {
            int insertCount = Math.min(stack.getCount(), availableSpace);
            boolean equal = StackUtil.checkItemEquality(stack, slot);

            if (!slot.isEmpty() && !equal) {
                GtUtil.sendMessage(player, slot.getCount()+" "+slot.getDisplayName());
                return true;
            }
            else if (slot.isEmpty()) {
                ItemStack bStack = stack.copy();
                content.put(bStack);
                player.setHeldItem(hand, ItemStack.EMPTY);
            }
            else {
                slot.grow(insertCount);
                stack.shrink(insertCount);
            }
            canDoubleClick = true;
        }
        else if (time - clickTime < 30 && canDoubleClick) { //doubleclick mechanics
            canDoubleClick = false;
            int totalCount = 0;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack currentStack = player.inventory.getStackInSlot(i);
                availableSpace = maxItemCount - slot.getCount() - totalCount;
                if (StackUtil.checkItemEquality(currentStack, content.get())) {
                    if (availableSpace < 1) break;
                    else if (availableSpace >= currentStack.getMaxStackSize()) player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    else if (availableSpace > 0) player.inventory.setInventorySlotContents(i, currentStack.splitStack(availableSpace));
                    player.inventory.markDirty();
                    totalCount += Math.min(currentStack.getCount(), availableSpace);
                }
            }
            if (totalCount < 1) {
                GtUtil.sendMessage(player, slot.getCount() + " " + slot.getDisplayName());
                return true;
            }

            content.get().grow(totalCount);
            GregTechMod.proxy.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.5F);
        }
        else if (!slot.isEmpty()) {
            GtUtil.sendMessage(player, slot.getCount() + " " + slot.getDisplayName());
            canDoubleClick = false;
        }

        clickTime = time;
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void onClicked(EntityPlayer player) {
        if(isPrivate && !GtUtil.checkAccess(this, owner, player.getGameProfile())) return;
        ItemStack slot = content.get();

        if (!slot.isEmpty() && !world.isRemote && player.getActiveHand() == EnumHand.MAIN_HAND) {
            long time = world.getTotalWorldTime();
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
            int count = nbt.getInteger("contentCount");
            ItemStack content = new ItemStack((NBTTagCompound) nbt.getTag("content"));
            content.setCount(count);
            this.content.put(content);
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
        if (!content.isEmpty()) {
            NBTTagCompound tNBT = new NBTTagCompound();
            ItemStack content = this.content.get().copy();
            content.setCount(1);
            content.writeToNBT(tNBT);
            nbt.setTag("content", tNBT);
            nbt.setInteger("contentCount", this.content.get().getCount());
        }
        return super.writeToNBT(nbt);
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return GtUtil.correctStacksize(super.getAuxDrops(fortune));
    }

    @Override
    public boolean hasSteamTank() {
        return false;
    }

    @Nullable
    @Override
    public FluidTank getSteamTank() {
        return null;
    }

    @Nullable
    @Override
    public GameProfile getOwner() {
        return this.owner;
    }

    @Override
    public void setEUcapacity(double capacity) {}

    @Override
    public void setSinkTier(int tier) {}

    @Override
    public void setOverclockerCount(int count) {}

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    @Override
    public void setPrivate(boolean value, GameProfile owner) {
        this.isPrivate = true;
    }

    @Override
    public void addSteamTank() {}

    @Override
    public double getExtraEnergyStorage() {
        return 0;
    }

    @Override
    public int getUpgradeCount(GtUpgradeType type) {
        return 0;
    }

    @Override
    public int getUpgradeCount(IC2UpgradeType type) {
        return 0;
    }

    @Override
    public int getOverclockersCount() {
        return 0;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return EnumSet.of(GtUpgradeType.LOCK, GtUpgradeType.OTHER);
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }

    @Override
    public void markForExplosion() {}

    @Override
    public void markForExplosion(float power) {}

    private class BarrelItemStackHandler extends ItemStackHandler {
        private final EnumFacing side;

        private BarrelItemStackHandler(EnumFacing side) {
            this.side = side;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;
            validateSlotIndex(slot);
            availableSpace = maxItemCount - content.get().getCount();
            ItemStack existing = content.get();

            if (!existing.isEmpty()) {
                if (!canInsertItem(slot, stack, this.side) || !ItemHandlerHelper.canItemStacksStack(existing, stack) || maxItemCount <= existing.getCount()) return stack;
            }

            boolean reachedLimit = stack.getCount() >= maxItemCount;
            int insertCount = Math.min(availableSpace, stack.getCount());
            if (!simulate) {
                if (existing.isEmpty()) {
                    content.put(reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, maxItemCount) : stack);
                } else {
                    content.get().grow(insertCount);
                }
                onContentsChanged(slot);
            }
            return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - insertCount);
        }
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack existing = content.get();

            if (amount == 0 || !canExtractItem(slot, existing, side) || existing.isEmpty()) return ItemStack.EMPTY;

            validateSlotIndex(slot);

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract) {
                if (!simulate) {
                    content.put(ItemStack.EMPTY);
                    onContentsChanged(slot);
                }
                return existing;
            }
            else {
                if (!simulate) {
                    content.put(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    onContentsChanged(slot);
                }
                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return this.content.get().getDisplayName();
    }

    @Override
    public String getSecondaryInfo() {
        return String.valueOf(this.content.get().getCount());
    }

    @Override
    public String getTertiaryInfo() {
        return "Max: "+this.maxItemCount;
    }

    @Override
    public boolean hasMjUpgrade() {
        return false;
    }

    @Override
    public void addMjUpgrade() {}

    @Override
    public void addEnergy(double amount) {}

    @Override
    public double useEnergy(double amount, boolean simulate) {
        return 0;
    }

    @Override
    public double getUniversalEnergy() {
        return 0;
    }

    @Override
    public double getUniversalEnergyCapacity() {
        return 0;
    }

    @Override
    public int getSinkTier() {
        return 0;
    }

    @Override
    public int getSourceTier() {
        return 0;
    }

    @Override
    public double getMaxInputEUp() {
        return 0;
    }

    @Override
    public double getMaxOutputEUt() {
        return 0;
    }

    @Override
    public double getStoredEU() {
        return 0;
    }

    @Override
    public double getEUCapacity() {
        return 0;
    }

    @Override
    public double getAverageEUInput() {
        return 0;
    }

    @Override
    public double getAverageEUOutput() {
        return 0;
    }

    @Override
    public double getStoredSteam() {
        return 0;
    }

    @Override
    public double getSteamCapacity() {
        return 0;
    }

    @Override
    public long getStoredMj() {
        return 0;
    }

    @Override
    public long getMjCapacity() {
        return 0;
    }

    @Override
    public void setMjCapacity(long capacity) {}
}
