package mods.gregtechmod.common.objects.blocks.machines.tileentity.base;

import com.mojang.authlib.GameProfile;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import ic2.core.ref.FluidName;
import ic2.core.util.Util;
import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.cover.ICover;
import mods.gregtechmod.common.cover.ICoverable;
import mods.gregtechmod.common.inventory.GtFluidTank;
import mods.gregtechmod.common.inventory.GtUpgradeSlot;
import mods.gregtechmod.common.objects.items.GtUpgradeItem;
import mods.gregtechmod.common.util.IUpgradableMachine;
import mods.gregtechmod.common.util.SidedRedstoneEmitter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class TileEntityUpgradable extends TileEntityCoverable implements IUpgradableMachine, ICoverable {
    protected Energy energy;
    public final SidedRedstoneEmitter rsEmitter;
    protected int[] averageEUInputRaw = new int[] {0,0,0,0,0};
    protected int[] averageEUOutputRaw = new int[] {0,0,0,0,0};
    protected int averageEUInputIndex = 0;
    protected int averageEUOutputIndex = 0;
    protected int input;
    private int previousEU;
    public int averageEUIn;
    protected int tickCounter;

    protected GameProfile owner = null;
    protected boolean isPrivate = false;

    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final int defaultEnergyConsume;
    public int energyConsume;
    public InvSlot upgradeSlot;
    protected int overclockersCount = 0;
    protected boolean hasSteamUpgrade = false;
    public Fluids fluids;
    public Fluids.InternalFluidTank steamTank;
    int neededSteam;

    public double steamBalance = ConfigLoader.steamMultiplier;
    public double supersteamBalance = ConfigLoader.superHeatedSteamMultiplier;

    protected TileEntityUpgradable(int maxEnergy, int defaultTier, int defaultEnergyConsume) {
        this.energy = addComponent(new Energy(this, maxEnergy, Util.allFacings, Collections.emptySet(), defaultTier));
        this.defaultTier = defaultTier;
        this.defaultEnergyConsume = this.energyConsume =defaultEnergyConsume;
        this.upgradeSlot = new GtUpgradeSlot(this, "upgrades", InvSlot.Access.NONE, 4);
        this.defaultEnergyStorage = maxEnergy;
        this.fluids = addComponent(new Fluids(this));
        this.rsEmitter = addComponent(new SidedRedstoneEmitter(this));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        if (isPrivate) {
            if (!checkAccess(owner, player.getGameProfile())) {
                IC2.platform.messagePlayer(player, "Only "+ owner.getName()+" can access this.");
                return false;
            } else {
                IC2.platform.messagePlayer(player,"access granted for "+player.getGameProfile().getName());
            }
        }

        if (this.handler.covers.containsKey(side) && this.handler.covers.get(side).onCoverRightClick(player, hand, side, hitX, hitY, hitZ)) return true;

        for (ICover cover : handler.covers.values()) if (!cover.opensGui(side)) return false;

        ItemStack stack = player.inventory.getCurrentItem();
        Item currentItem = stack.getItem();

        if(upgradeSlot.accepts(stack)) {
            for (int i = 0; i < upgradeSlot.size(); i++) {
                ItemStack upgradeStack = upgradeSlot.get(i);
                int stackMeta = stack.getMetadata();

                if (upgradeStack.getMetadata() == stackMeta || upgradeStack.isEmpty()) {

                    if (currentItem instanceof IUpgradeItem) {
                        if (stackMeta == 0 && upgradeStack.getCount() >= 4) return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                        else if (stackMeta == 1 && upgradeStack.getCount() >= 3 - defaultTier) return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                    }
                    else {
                        GtUpgradeItem.GtUpgradeType type = GtUpgradeItem.GtUpgradeType.values()[stackMeta];
                        if (type == GtUpgradeItem.GtUpgradeType.machine_lock && !player.getGameProfile().equals(this.owner)) {
                            IC2.platform.messagePlayer(player, "You can't lock a machine you don't own!");
                            return true;
                        }
                        else if (!GtUpgradeItem.GtUpgradeType.values()[stackMeta].canBeInserted(upgradeStack.getCount(), this.energy.getSinkTier(), hasSteamUpgrade)) return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                    }

                    //TODO: Add if statement for grow
                    ItemStack bStack = stack.copy();
                    bStack.setCount(upgradeStack.getCount()+1);
                    this.upgradeSlot.put(i, bStack);
                    if (!player.capabilities.isCreativeMode) stack.splitStack(1);
                    break;
                }
            }
            updateUpgrades(player);
            return true;
        }
        else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public static boolean checkAccess(GameProfile owner, GameProfile playerProfile) {
        if (owner == null) return true;
        return owner.equals(playerProfile);
    }

    protected void setOverclock() {
        this.energyConsume = this.defaultEnergyConsume * (int)Math.pow(4, overclockersCount);
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer && !world.isRemote) this.owner = ((EntityPlayer) placer).getGameProfile();
    }

    public void updateUpgrade(ItemStack stack, int meta, EntityPlayer player) {
        switch (meta) {
            case 0:
                this.overclockersCount = stack.getCount();
                IC2.network.get(true).updateTileEntityField(TileEntityUpgradable.this, "overclockersCount");
                break;
            case 1:
                this.energy.setSinkTier(Math.min(defaultTier+stack.getCount(), 3));
                break;
            case 10:
                this.energy.setSinkTier(Math.min(energy.getSinkTier()+stack.getCount(), 5));
                break;
            case 11:
                this.energy.setCapacity(defaultEnergyStorage+(100000*stack.getCount()));
                break;
            case 12:
                this.energy.setCapacity(energy.getCapacity()+(100000*stack.getCount())); //TODO: buff maybe? or buff lithium batt?
                break;
            case 13:
                this.energy.setCapacity(energy.getCapacity()+(1000000*stack.getCount()));
                break;
            case 14:
                this.energy.setCapacity(energy.getCapacity()+(10000000*stack.getCount()));
                break;
            case 16:
                if (player != null) owner = player.getGameProfile();
                this.isPrivate = true;
                break;
            case 17:
                this.hasSteamUpgrade = true;
                if (steamTank == null) this.steamTank = this.fluids.addTank(createSteamTank());
                break;
            case 18:
                this.steamTank.setCapacity(2000 + stack.getCount() * 64000);
                break;
        }
    }

    public void updateUpgrades(EntityPlayer player) {
        if (world.isRemote) return;
        for (int i = 0; i < upgradeSlot.size(); i++) {
            ItemStack currentStack = upgradeSlot.get(i);
            if (currentStack.isEmpty()) continue;
            int currentMeta = currentStack.getMetadata();

            if (currentStack.getItem() instanceof GtUpgradeItem) currentMeta += 10;
            updateUpgrade(currentStack, currentMeta, player);
        }
    }

    public Fluids.InternalFluidTank createSteamTank() {
        return new GtFluidTank(this, "steamTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), Fluids.fluidPredicate(FluidRegistry.getFluid("steam"), FluidName.steam.getInstance(), FluidName.superheated_steam.getInstance()), 2000);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
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
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("ownerProfile")) owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("ownerProfile"));
        if (nbt.hasKey("steamTank")) {
            this.steamTank = createSteamTank();
            this.fluids.addTank(steamTank);
            steamTank.readFromNBT((NBTTagCompound)nbt.getTag("steamTank"));
        }
    }

    @Override
    public Set<ItemUpgradeModule.UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(ItemUpgradeModule.UpgradeType.overclocker, ItemUpgradeModule.UpgradeType.transformer);
    }

    @Override
    protected void updateEntityServer() {
        if (world.isRemote) return;
        super.updateEntityServer();
        for (ICover cover : handler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && tickCounter%tickRate == 0) cover.doCoverThings();
        }
        if (needsCoverBehaviorUpdate) {
            Set<EnumFacing> dirs = new HashSet<>(sinkDirections);
            for (Map.Entry<EnumFacing, ICover> entry : handler.covers.entrySet()) {
                if (!entry.getValue().allowEnergyTransfer()) dirs.remove(entry.getKey());
            }
            this.energy.setDirections(dirs, Collections.emptySet());
            needsCoverBehaviorUpdate = false;
        }
        averageEUIn = calculateAverageInput();
        tickCounter++;
    }

    private int calculateAverageInput() {
        if (this.energy.getSinkDirs().isEmpty()) return 0;
        int currentEU = (int) this.energy.getEnergy();
        input = currentEU - previousEU;
        previousEU = currentEU;

        if (input > 0) averageEUInputRaw[averageEUInputIndex] = input;
        if (++averageEUInputIndex  >= averageEUInputRaw.length) averageEUInputIndex  = 0;
        if (++averageEUOutputIndex >= averageEUOutputRaw.length) averageEUOutputIndex = 0;

        int rEU = 0;
        for (int tEU : averageEUInputRaw) rEU += tEU;
        return rEU / averageEUInputRaw.length;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        if (handler.covers.containsKey(side)) return handler.covers.get(side).letsItemsIn();
        return super.canInsertItem(index, stack, side);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        if (handler.covers.containsKey(side)) return handler.covers.get(side).letsItemsOut() && super.canExtractItem(index, stack, side);
        return super.canExtractItem(index, stack, side);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            IItemHandler handler = super.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new IItemHandler() { //hide upgrade slots
                @Override
                public int getSlots() {
                    return handler.getSlots() - 4;
                }

                @Nonnull
                @Override
                public ItemStack getStackInSlot(int slot) {
                    return handler.getStackInSlot(slot + 4);
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    return handler.insertItem(slot + 4, stack, simulate);
                }

                @Nonnull
                @Override
                public ItemStack extractItem(int slot, int amount, boolean simulate) {
                    return handler.extractItem(slot + 4, amount, simulate);
                }

                @Override
                public int getSlotLimit(int slot) {
                    return handler.getSlotLimit(slot + 4);
                }
            });
        }
        return super.getCapability(capability, facing);
    }
}
