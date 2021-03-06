package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import com.mojang.authlib.GameProfile;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.inventory.slot.GtUpgradeSlot;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TileEntityUpgradable extends TileEntityCoverBehavior implements IUpgradableMachine, ICoverable {
    protected Energy energy;
    protected int[] averageEUInputRaw = new int[] {0,0,0,0,0};
    protected int[] averageEUOutputRaw = new int[] {0,0,0,0,0};
    protected int averageEUInputIndex = 0;
    protected int averageEUOutputIndex = 0;
    protected int input;
    private int previousEU;
    public int averageEUIn;

    protected GameProfile owner = null;
    protected boolean isPrivate = false;

    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final int defaultEnergyConsume;
    public int energyConsume;
    public InvSlot upgradeSlot;
    public int overclockersCount;
    protected boolean hasSteamUpgrade = false;
    public Fluids fluids;
    public Fluids.InternalFluidTank steamTank;
    int neededSteam;

    protected TileEntityUpgradable(int maxEnergy, int defaultTier, int defaultEnergyConsume) {
        this.energy = addComponent(new Energy(this, maxEnergy, Util.allFacings, Collections.emptySet(), defaultTier));
        this.defaultTier = defaultTier;
        this.defaultEnergyConsume = this.energyConsume =defaultEnergyConsume;
        this.upgradeSlot = new GtUpgradeSlot(this, "upgrades", InvSlot.Access.NONE, 8);
        this.defaultEnergyStorage = maxEnergy;
        this.fluids = addComponent(new Fluids(this));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;

        if (isPrivate) {
            if (!checkAccess(owner, player.getGameProfile())) {
                IC2.platform.messagePlayer(player, "Only "+ owner.getName()+" can access this.");
                return false;
            }
        }

        if (this.coverHandler.covers.containsKey(side) && this.coverHandler.covers.get(side).onCoverRightClick(player, hand, side, hitX, hitY, hitZ)) return true;

        for (ICover cover : coverHandler.covers.values()) if (!cover.opensGui(side)) return false;

        ItemStack stack = player.inventory.getCurrentItem();
        Item currentItem = stack.getItem();

        if(upgradeSlot.accepts(stack)) {
            for (int i = 0; i < upgradeSlot.size(); i++) {
                ItemStack upgradeStack = upgradeSlot.get(i);
                int upgradeCount = upgradeStack.getCount();
                boolean areItemsEqual = StackUtil.checkItemEquality(stack, upgradeStack);

                if (currentItem instanceof IUpgradeItem && (areItemsEqual || upgradeStack.isEmpty())) {
                    IC2UpgradeType upgradeType = IC2UpgradeType.fromStack(stack);
                    if (upgradeType != null) {
                        if (upgradeCount >= upgradeType.maxCount || (upgradeType == IC2UpgradeType.TRANSFORMER && upgradeCount >= upgradeType.maxCount - this.defaultTier + 1)) return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                    }
                }
                else if (currentItem instanceof IGtUpgradeItem && ((areItemsEqual || upgradeStack.isEmpty()))) {
                    if (((IGtUpgradeItem)currentItem).onInsert(upgradeStack, this, player)) return true;
                    else if (!((IGtUpgradeItem)currentItem).canBeInserted(upgradeStack, this)) return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                }
                else continue;

                if (areItemsEqual) {
                    upgradeStack.grow(1);
                } else {
                    ItemStack bStack = stack.copy();
                    bStack.setCount(upgradeStack.getCount()+1);
                    this.upgradeSlot.put(i, bStack);
                }

                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                break;
            }
            updateUpgrades(player);
            return true;
        }
        else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("overclockersCount");
        return ret;
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

    public void updateUpgrade(ItemStack stack, EntityPlayer player) {
        Item currentItem = stack.getItem();
        if (currentItem instanceof IGtUpgradeItem) {
            ((IGtUpgradeItem)currentItem).onUpdate(stack, this, player);
        } else if (currentItem instanceof IUpgradeItem) {
            IC2UpgradeType upgradeType = IC2UpgradeType.fromStack(stack);
            switch (upgradeType) {
                case OVERCLOCKER:
                    this.overclockersCount = stack.getCount();
                    IC2.network.get(true).updateTileEntityField(this, "overclockersCount");
                    break;
                case TRANSFORMER:
                    this.energy.setSinkTier(Math.min(defaultTier+stack.getCount(), 3));
                    break;
                case BATTERY:
                    this.energy.setCapacity(this.defaultEnergyStorage+(10000*stack.getCount()));
                    break;
            }
        }
    }

    public void updateUpgrades(EntityPlayer player) {
        if (world.isRemote) return;

        for (int i = 0; i < upgradeSlot.size(); i++) {
            ItemStack currentStack = upgradeSlot.get(i);
            if (currentStack.isEmpty()) continue;

            updateUpgrade(currentStack, player);
        }
    }

    public Fluids.InternalFluidTank createSteamTank() {
        return new GtFluidTank(this, "steamTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), Fluids.fluidPredicate(FluidRegistry.getFluid("steam"), FluidName.steam.getInstance(), FluidName.superheated_steam.getInstance()), 2000);
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
        return super.writeToNBT(nbt);
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
    protected void updateEntityServer() {
        if (world.isRemote) return;
        super.updateEntityServer();
        if (needsCoverBehaviorUpdate) {
            Set<EnumFacing> dirs = new HashSet<>(sinkDirections);
            for (Map.Entry<EnumFacing, ICover> entry : coverHandler.covers.entrySet()) {
                if (!entry.getValue().allowEnergyTransfer()) dirs.remove(entry.getKey());
            }
            this.energy.setDirections(dirs, Collections.emptySet());
            needsCoverBehaviorUpdate = false;
        }
        averageEUIn = calculateAverageInput();
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return GtUtil.correctStacksize(super.getAuxDrops(fortune));
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
        tooltip.add("Possible Upgrades: " + String.join(" ", possibleUpgrades));
    }

    private int calculateAverageInput() {
        if (this.energy.getSinkDirs().isEmpty()) return 0;
        int currentEU = (int) this.energy.getEnergy();
        this.input = currentEU - previousEU;
        this.previousEU = currentEU;

        if (input > 0) averageEUInputRaw[averageEUInputIndex] = input;
        if (++averageEUInputIndex  >= averageEUInputRaw.length) averageEUInputIndex  = 0;
        if (++averageEUOutputIndex >= averageEUOutputRaw.length) averageEUOutputIndex = 0;

        int rEU = 0;
        for (int tEU : averageEUInputRaw) rEU += tEU;
        return rEU / averageEUInputRaw.length;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new MachineItemStackHandler(this, side));
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
            return super.getSlots() - 4;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return super.getStackInSlot(slot + 4);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return super.insertItem(slot + 4, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot + 4, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot + 4);
        }
    }

    @Override
    public int getTier() {
        return this.energy.getSinkTier();
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
    public int getSinkTier() {
        return this.energy.getSinkTier();
    }

    @Override
    public int getDefaultSinkTier() {
        return this.defaultTier;
    }

    @Override
    public int getSourceTier() {
        return this.energy.getSourceTier();
    }

    @Override
    public double getDefaultEUCapacity() {
        return this.defaultEnergyStorage;
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
    public void setSourceTier(int tier) {}

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
    public int getUpgradecount(IC2UpgradeType type) {
        int total = 0;
        for (ItemStack stack : this.upgradeSlot) {
            IC2UpgradeType upgradeType = IC2UpgradeType.fromStack(stack);
            if (upgradeType != null && upgradeType == type) total += stack.getCount();
        }
        return total;
    }

    @Override
    public int getUpgradecount(IGtUpgradeItem upgrade) {
        int total = 0;
        for (ItemStack stack : this.upgradeSlot) {
            Item item = stack.getItem();
            if (item instanceof IGtUpgradeItem && ((IGtUpgradeItem) item).getName().equals(upgrade.getName())) total += stack.getCount();
            else if (item instanceof IUpgradeItem) {
                IC2UpgradeType type = IC2UpgradeType.fromStack(stack);
                if (type != null && type.itemType.equals(upgrade.getType().name())) total += stack.getCount();
            }
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
    }

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    @Override
    public void setPrivate(boolean value, GameProfile owner) {
        this.isPrivate = true;
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
}