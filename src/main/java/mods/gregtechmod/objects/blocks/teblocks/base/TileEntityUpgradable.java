package mods.gregtechmod.objects.blocks.teblocks.base;

import buildcraft.api.mj.MjAPI;
import com.mojang.authlib.GameProfile;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.compat.buildcraft.MjReceiverWrapper;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.inventory.slot.GtUpgradeSlot;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TileEntityUpgradable extends TileEntityEnergy implements IUpgradableMachine, IScannerInfoProvider {
    protected GameProfile owner = null;
    protected boolean isPrivate;
    
    public InvSlot upgradeSlot;
    public int overclockersCount;
    protected boolean hasSteamUpgrade;
    public Fluids fluids;
    public Fluids.InternalFluidTank steamTank;
    protected int neededSteam;

    protected MjReceiverWrapper receiver;
    protected boolean hasMjUpgrade;

    protected TileEntityUpgradable(String descriptionKey) {
        super(descriptionKey);
        this.upgradeSlot = new GtUpgradeSlot(this, "upgrades", InvSlot.Access.NONE, 8);
        this.fluids = addComponent(new Fluids(this));
    }
    
    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (isPrivate) {
            if (!GtUtil.checkAccess(this, owner, player.getGameProfile())) {
                GtUtil.sendMessage(player, Reference.MODID+".info.access_error", owner.getName());
                return false;
            }
        }

        ItemStack stack = player.inventory.getCurrentItem();
        Item currentItem = stack.getItem();

        if(upgradeSlot.accepts(stack)) {
            for (int i = 0; i < upgradeSlot.size(); i++) {
                ItemStack upgradeStack = upgradeSlot.get(i);
                int upgradeCount = upgradeStack.getCount();
                boolean areItemsEqual = StackUtil.checkItemEquality(stack, upgradeStack);

                if (currentItem instanceof IUpgradeItem && (areItemsEqual || upgradeStack.isEmpty())) {
                    IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
                    if (upgradeType != null) {
                        if (upgradeCount >= upgradeType.maxCount || upgradeType == IC2UpgradeType.TRANSFORMER && upgradeCount >= upgradeType.maxCount - this.defaultSinkTier + 1) return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
                    }
                }
                else if (currentItem instanceof IGtUpgradeItem && (areItemsEqual || upgradeStack.isEmpty())) {
                    if (((IGtUpgradeItem)currentItem).beforeInsert(upgradeStack, this, player)) return true;
                    else if (!((IGtUpgradeItem)currentItem).canBeInserted(upgradeStack, this)) return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
                }
                else continue;

                if (areItemsEqual) {
                    upgradeStack.grow(1);
                } else {
                    this.upgradeSlot.put(i, StackUtil.copyWithSize(stack, 1));
                }

                ItemStack copy = StackUtil.copyWithSize(stack, 1);
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                updateUpgrade(copy, player);
                break;
            }
            return true;
        }
        else {
            return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("overclockersCount");
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer && !world.isRemote) this.owner = ((EntityPlayer) placer).getGameProfile();
    }

    public void updateUpgrade(ItemStack stack, EntityPlayer player) {
        if (world.isRemote) return;

        Item currentItem = stack.getItem();
        if (currentItem instanceof IGtUpgradeItem) {
            onUpdateUpgrade((IGtUpgradeItem) currentItem, stack, player);
        } else if (currentItem instanceof IUpgradeItem) {
            IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
            onUpdateUpgrade(upgradeType, stack);
        }
    }

    protected void onUpdateUpgrade(IGtUpgradeItem item, ItemStack stack, EntityPlayer player) {
        item.afterInsert(stack, this, player);
    }

    protected void onUpdateUpgrade(IC2UpgradeType type, ItemStack stack) {
        switch (type) {
            case OVERCLOCKER:
                setOverclockerCount(this.overclockersCount + stack.getCount());
                break;
            case TRANSFORMER:
                this.energy.setSinkTier(Math.min(this.energy.getSinkTier() + stack.getCount(), 3));
                break;
            case BATTERY:
                this.energy.setCapacity(this.energy.getCapacity() + 10000 * stack.getCount());
                break;
        }
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!world.isRemote) {
            for (int i = 0; i < upgradeSlot.size(); i++) {
                ItemStack currentStack = upgradeSlot.get(i);
                if (currentStack.isEmpty()) continue;

                updateUpgrade(currentStack, null);
            }
        }
    }

    public Fluids.InternalFluidTank createSteamTank() {
        return new GtFluidTank(this, "steamTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), Fluids.fluidPredicate(FluidRegistry.getFluid("steam"), FluidName.steam.getInstance(), FluidName.superheated_steam.getInstance()), 10000);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (owner != null) {
            NBTTagCompound ownerCompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(ownerCompound, owner);
            nbt.setTag("ownerProfile", ownerCompound);
        }
        if (hasSteamUpgrade) {
            NBTTagCompound tNBT = new NBTTagCompound();
            steamTank.writeToNBT(tNBT);
            nbt.setTag("steamTank", tNBT);
        }
        if (hasMjUpgrade) {
            nbt.setTag("mj", this.receiver.serializeNBT());
        }
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("ownerProfile")) owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("ownerProfile"));
        if (nbt.hasKey("steamTank")) {
            this.hasSteamUpgrade = true;
            this.steamTank = createSteamTank();
            this.fluids.addTank(steamTank);
            steamTank.readFromNBT(nbt.getCompoundTag("steamTank"));
        }
        if (nbt.hasKey("mj")) {
            this.addMjUpgrade();
            this.receiver.deserializeNBT(nbt.getCompoundTag("mj"));
        }
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return GtUtil.correctStacksize(super.getAuxDrops(fortune));
    }

    @Override
    protected boolean canSetFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (isPrivate && !GtUtil.checkAccess(this, owner, player.getGameProfile())) {
            return false;
        }
        return super.canSetFacingWrench(facing, player);
    }

    @Override
    protected boolean wrenchCanRemove(EntityPlayer player) {
        if (isPrivate && !GtUtil.checkAccess(this, owner, player.getGameProfile())) {
            GtUtil.sendMessage(player, Reference.MODID+".info.wrench_error", player.getName());
            return false;
        }
        return super.wrenchCanRemove(player);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        Set<String> possibleUpgrades = new LinkedHashSet<>();
        possibleUpgrades.addAll(this.getCompatibleIC2Upgrades()
                .stream()
                .sorted(Comparator.comparing(Enum::ordinal))
                .map(entry -> entry.toString().substring(0, 1))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        possibleUpgrades.addAll(this.getCompatibleGtUpgrades()
                .stream()
                .filter(upgrade -> upgrade.display)
                .map(entry -> entry.toString().substring(0, 1))
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        if (!possibleUpgrades.isEmpty()) tooltip.add(GtUtil.translate("teblock.info.possible_upgrades")+": " + String.join(" ", possibleUpgrades));
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new MachineItemStackHandler(this, side));
        } else if (ModHandler.buildcraftLib && (capability == MjHelper.RECEIVER_CAPABILITY || capability == MjHelper.CONNECTOR_CAPABILITY)) {
            return MjAPI.CAP_RECEIVER.cast(this.receiver);
        }
        return super.getCapability(capability, side);
    }

    /**
     * An IItemHandler that hides upgrade slots
     */
    public static class MachineItemStackHandler extends SidedInvWrapper implements IItemHandler {

        public MachineItemStackHandler(ISidedInventory inventory, EnumFacing side) {
            super(inventory, side);
        }

        @Override
        public int getSlots() {
            return super.getSlots() - 8;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return super.getStackInSlot(slot + 8);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return super.insertItem(slot + 8, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot + 8, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot + 8);
        }
    }

    @Override
    public boolean hasSteamTank() {
        return this.steamTank != null;
    }

    @Nullable
    @Override
    public FluidTank getSteamTank() {
        return this.steamTank;
    }

    @Override
    public void addSteamTank() {
        this.hasSteamUpgrade = true;
        if (steamTank == null) this.steamTank = this.fluids.addTank(createSteamTank());
    }

    @Nullable
    @Override
    public GameProfile getOwner() {
        return this.owner;
    }

    @Override
    public void setEUcapacity(double capacity) {
        this.energy.setCapacity(capacity);
    }

    @Override
    public void setSinkTier(int tier) {
        this.energy.setSinkTier(tier);
    }

    @Override
    public double getExtraEnergyStorage() {
        return this.energy.getCapacity() - this.defaultEnergyStorage;
    }

    @Override
    public int getUpgradeCount(GtUpgradeType type) {
        int total = 0;
        for (ItemStack stack : this.upgradeSlot) {
            Item item = stack.getItem();
            if (item instanceof IGtUpgradeItem && ((IGtUpgradeItem) item).getType() == type) total += stack.getCount();
        }
        return total;
    }

    @Override
    public int getUpgradeCount(IC2UpgradeType type) {
        int total = 0;
        for (ItemStack stack : this.upgradeSlot) {
            IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
            if (upgradeType != null && upgradeType == type) total += stack.getCount();
        }
        return total;
    }

    @Override
    public int getOverclockersCount() {
        return this.overclockersCount;
    }

    @Override
    public void setOverclockerCount(int count) {
        this.overclockersCount = count;
        IC2.network.get(true).updateTileEntityField(this, "overclockersCount");
    }

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    @Override
    public void setPrivate(boolean value, GameProfile owner) {
        this.isPrivate = value;
        this.owner = owner;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return GtUpgradeType.DEFAULT;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return IC2UpgradeType.DEFAULT;
    }
    
    protected float getSteamMultiplier() {
        float multiplier = 0.5F;
        if (this.steamTank.getFluidAmount() < 1) return multiplier;
        Fluid fluid = this.steamTank.getFluid().getFluid();
    
        if (fluid == FluidName.superheated_steam.getInstance()) multiplier *= GregTechConfig.BALANCE.superHeatedSteamMultiplier;
        else if (fluid == FluidRegistry.getFluid("steam")) multiplier /= GregTechConfig.BALANCE.steamMultiplier;
    
        return multiplier;
    }
    
    protected boolean canDrainSteam(int requiredAmount) {
        if (requiredAmount < 1 || steamTank == null) return false;
        return steamTank.getFluidAmount() >= requiredAmount;
    }
    
    protected int getEnergyForSteam(double amount) {
        return (int) Math.round(amount / getSteamMultiplier());
    }
     
    @Override
    public double useEnergy(double amount, boolean simulate) {
        double discharged = this.energy.discharge(amount, simulate);
        if (discharged > 0) return discharged;
        else if (this.hasMjUpgrade && this.receiver.extractPower(MjHelper.convert(amount))) return amount;
        else if (hasSteamUpgrade) {
            int energy = getEnergyForSteam(amount);
            if (canDrainSteam(energy)) {
                steamTank.drain(energy, true);
                return amount;
            }
        }
        return 0;
    }
    
    @Override
    public double getUniversalEnergy() {
        double steam = this.hasSteamUpgrade ? steamTank.getFluidAmount() * getSteamMultiplier() : 0;
        double mj = this.hasMjUpgrade ? this.receiver.getStored() / (double) MjHelper.MJ : 0;
        return Math.max(this.energy.getStoredEnergy(), Math.max(steam, mj));
    }
    
    @Override
    public double getUniversalEnergyCapacity() {
        double steam = this.hasSteamUpgrade ? this.steamTank.getCapacity() * getSteamMultiplier() : 0;
        double mj = this.hasMjUpgrade ? this.receiver.getCapacity() / (double) MjHelper.MJ : 0;
        return Math.max(this.energy.getCapacity(), Math.max(steam, mj));
    }

    @Override
    public double getStoredSteam() {
        if (steamTank != null) return steamTank.getFluidAmount();
        return 0;
    }
    
    @Override
    public double getSteamCapacity() {
        if (steamTank != null) return steamTank.getCapacity();
        return 0;
    }
    
    @Override
    public long getStoredMj() {
        return this.hasMjUpgrade ? this.receiver.getStored() : 0;
    }

    @Override
    public long getMjCapacity() {
        return this.hasMjUpgrade ? this.receiver.getCapacity() : 0;
    }

    @Override
    public void setMjCapacity(long capacity) {
        if (this.hasMjUpgrade) this.receiver.setCapacity(capacity);
    }

    @Override
    public boolean hasMjUpgrade() {
        return this.hasMjUpgrade;
    }

    @Override
    public void addMjUpgrade() {
        this.hasMjUpgrade = true;
        if (this.receiver == null) this.receiver = new MjReceiverWrapper(10000 * MjHelper.MJ, 100 * MjHelper.MJ, 0);
        if (this.world != null) this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, false);
    }

    @Nonnull
    @Override
    public List<String> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel) {
        List<String> ret = new ArrayList<>();
        if (scanLevel > 2) ret.add("Meta-ID: " + this.getBlockType().getItemStack(this.teBlock).getMetadata());
        if (scanLevel > 1) {
            ret.add(GtUtil.translateInfo(GtUtil.checkAccess(this, this.owner, player.getGameProfile()) ? "machine_accessible" : "machine_not_accessible"));
        }
        if (scanLevel > 0) {
            if (this.hasSteamUpgrade) {
                ret.add(this.steamTank.getFluidAmount() + " / " + this.steamTank.getCapacity() + " " + GtUtil.translateGeneric("steam"));
            }
            if (this.hasMjUpgrade) {
                ret.add(this.receiver.getStored() / MjHelper.MJ + " / " + this.receiver.getCapacity() / MjHelper.MJ + " MJ");
            }
        }
        return ret;
    }
}
