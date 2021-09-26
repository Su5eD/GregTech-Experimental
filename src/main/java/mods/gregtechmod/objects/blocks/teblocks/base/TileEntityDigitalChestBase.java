package mods.gregtechmod.objects.blocks.teblocks.base;

import com.mojang.authlib.GameProfile;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.invslot.GtSlotLargeItemStack;
import mods.gregtechmod.objects.blocks.teblocks.component.UpgradeManager;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
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

public abstract class TileEntityDigitalChestBase extends TileEntityCoverBehavior implements IUpgradableMachine, IPanelInfoProvider, IScannerInfoProvider {
    public int capacity;
    public final InvSlot content;
    protected final UpgradeManager upgradeManager;
    private boolean canDoubleClick;
    private long clickTime;

    public TileEntityDigitalChestBase(String descriptionKey, int capacity) {
        super(descriptionKey);
        this.capacity = capacity;
        this.content = new GtSlotLargeItemStack(this, "mainSlot", InvSlot.Access.IO);
        this.content.setStackSizeLimit(capacity);
        this.upgradeManager = addComponent(new UpgradeManager(this, () -> {}, (item, stack, player) -> item.afterInsert(stack, this, player), (item, stack) -> {}));
        
        this.coverBlacklist.add(CoverType.ENERGY);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new BarrelItemStackHandler(facing));
        else return super.getCapability(capability, facing);
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.world.isRemote || hand != EnumHand.MAIN_HAND) return true;
        ItemStack stack = player.getHeldItem(hand);
        if (GtUtil.isWrench(stack) || addUpgrade(stack, player)) return true;

        ItemStack slot = this.content.get();
        int availableSpace = this.capacity - slot.getCount();
        long time = this.world.getTotalWorldTime();

        if (!stack.isEmpty() && availableSpace > 0) {
            int insertCount = Math.min(stack.getCount(), availableSpace);
            boolean equal = StackUtil.checkItemEquality(stack, slot);

            if (!slot.isEmpty() && !equal) {
                GtUtil.sendMessage(player, slot.getCount() + " " + slot.getDisplayName());
                return true;
            }
            else if (slot.isEmpty()) {
                ItemStack bStack = stack.copy();
                this.content.put(bStack);
                player.setHeldItem(hand, ItemStack.EMPTY);
            }
            else {
                slot.grow(insertCount);
                stack.shrink(insertCount);
            }
            this.canDoubleClick = true;
        }
        else if (time - this.clickTime < 30 && this.canDoubleClick) { //doubleclick mechanics
            this.canDoubleClick = false;
            int totalCount = 0;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack currentStack = player.inventory.getStackInSlot(i);
                int instantAvailableSpace = this.capacity - slot.getCount() - totalCount;
                if (StackUtil.checkItemEquality(currentStack, this.content.get())) {
                    if (instantAvailableSpace < 1) break;
                    else if (instantAvailableSpace >= currentStack.getMaxStackSize()) player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    else player.inventory.setInventorySlotContents(i, currentStack.splitStack(instantAvailableSpace));
                    player.inventory.markDirty();
                    totalCount += Math.min(currentStack.getCount(), instantAvailableSpace);
                }
            }
            if (totalCount < 1) {
                GtUtil.sendMessage(player, slot.getCount() + " " + slot.getDisplayName());
                return true;
            }

            this.content.get().grow(totalCount);
            GregTechMod.runProxy(clientProxy -> clientProxy.playSound(SoundEvents.ENTITY_ITEM_PICKUP, ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.7F + 1F) * 2F));
        }
        else if (!slot.isEmpty()) {
            GtUtil.sendMessage(player, slot.getCount() + " " + slot.getDisplayName());
            this.canDoubleClick = false;
        }

        this.clickTime = time;
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void onClicked(EntityPlayer player) {
        if (!this.upgradeManager.checkAccess(player)) return;
        ItemStack slot = content.get();

        if (!slot.isEmpty() && !this.world.isRemote && player.getActiveHand() == EnumHand.MAIN_HAND) {
            long time = this.world.getTotalWorldTime();
            if (time - this.clickTime < 3) return;
            this.clickTime = time;

            ItemStack output = slot.copy();
            if (player.isSneaking()) output = slot.splitStack(1);
            else output = slot.splitStack(Math.min(64, output.getCount()));
            EntityItem entityItem = new EntityItem(getWorld(), getPos().getX() + getFacing().getXOffset()+0.5, getPos().getY() + getFacing().getYOffset() + 0.5, getPos().getZ()+getFacing().getZOffset()+0.5, output);
            entityItem.motionX = 0;
            entityItem.motionY = 0;
            entityItem.motionZ = 0;
            getWorld().spawnEntity(entityItem);
        }
    }

    @Override
    public void forceAddUpgrade(ItemStack stack) {
        this.upgradeManager.forceAddUpgrade(stack);
    }

    @Override
    public boolean addUpgrade(ItemStack stack, EntityPlayer player) {
        return this.upgradeManager.addUpgrade(stack, player);
    }

    @Override
    public void setFacing(EnumFacing facing) {
        super.setFacing(facing);
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return GtUtil.mergeCollection(GtUtil.correctStacksize(super.getAuxDrops(fortune)), this.upgradeManager.getUpgrades());
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
        return this.upgradeManager.getOwner();
    }

    @Override
    public void setOwner(GameProfile owner) {
        this.upgradeManager.setOwner(owner);
    }

    @Override
    public boolean isPrivate() {
        return this.upgradeManager.isPrivate();
    }

    @Override
    public void setPrivate(boolean value) {
        this.upgradeManager.setPrivate(value);
    }

    @Override
    public void addSteamTank() {}

    @Override
    public int getExtraEUCapacity() {
        return 0;
    }

    @Override
    public void addExtraEUCapacity(int extraCapacity) {}

    @Override
    public void addExtraSinkTier() {}

    @Override
    public int getUpgradeCount(GtUpgradeType type) {
        return 0;
    }

    @Override
    public int getUpgradeCount(IC2UpgradeType type) {
        return 0;
    }

    @Override
    public int getBaseSinkTier() {
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
            int availableSpace = capacity - content.get().getCount();
            ItemStack existing = content.get();

            if (!existing.isEmpty()) {
                if (!canInsertItem(slot, stack, this.side) || !ItemHandlerHelper.canItemStacksStack(existing, stack) || capacity <= existing.getCount()) return stack;
            }

            boolean reachedLimit = stack.getCount() >= capacity;
            int insertCount = Math.min(availableSpace, stack.getCount());
            if (!simulate) {
                if (existing.isEmpty()) {
                    content.put(reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, capacity) : stack);
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
        return "Max: "+this.capacity;
    }

    @Override
    public boolean hasMjUpgrade() {
        return false;
    }

    @Override
    public void addMjUpgrade() {}

    @Override
    public boolean addEnergy(double amount) {
        return false;
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
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
    public int getEUCapacity() {
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

    @Override
    public double getUniversalEnergy() {
        return 0;
    }

    @Override
    public double getUniversalEnergyCapacity() {
        return 0;
    }
}
