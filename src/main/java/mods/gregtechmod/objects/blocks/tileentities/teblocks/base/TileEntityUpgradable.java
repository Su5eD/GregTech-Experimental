package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import buildcraft.api.mj.MjAPI;
import com.mojang.authlib.GameProfile;
import ic2.api.energy.EnergyNet;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.compat.buildcraft.MjReceiverWrapper;
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

public abstract class TileEntityUpgradable extends TileEntityCoverBehavior implements IUpgradableMachine {
    private final String descriptionKey;
    protected Energy energy;
    protected int[] averageEUInputRaw = new int[] { 0,0,0,0,0 };
    protected int[] averageEUOutputRaw = new int[] { 0,0,0,0,0 };
    protected int averageEUInputIndex = 0;
    protected int averageEUOutputIndex = 0;
    protected int input;
    private int previousEU;
    public int averageEUIn;

    protected GameProfile owner = null;
    protected boolean isPrivate;

    public final int defaultTier;
    public final int defaultEnergyStorage;
    public InvSlot upgradeSlot;
    public int overclockersCount;
    protected boolean hasSteamUpgrade;
    public Fluids fluids;
    public Fluids.InternalFluidTank steamTank;
    protected int neededSteam;

    protected MjReceiverWrapper receiver;
    protected boolean hasMjUpgrade;

    protected TileEntityUpgradable(String descriptionKey, int maxEnergy, int defaultTier) {
        this.descriptionKey = descriptionKey;
        this.energy = addComponent(new Energy(this, maxEnergy, Util.allFacings, Collections.emptySet(), defaultTier));
        this.defaultTier = defaultTier;
        this.upgradeSlot = new GtUpgradeSlot(this, "upgrades", InvSlot.Access.NONE, 8);
        this.defaultEnergyStorage = maxEnergy;
        this.fluids = addComponent(new Fluids(this));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (attemptUseCrowbar(stack, side, player) || attemptUseScrewdriver(stack, side, player)) return true;

        if (world.isRemote) return true;

        if (this.coverHandler.covers.containsKey(side) && this.coverHandler.covers.get(side).onCoverRightClick(player, hand, side, hitX, hitY, hitZ)) return true;
        for (ICover cover : coverHandler.covers.values()) if (!cover.opensGui(side)) return false;

        if (isPrivate) {
            if (!GtUtil.checkAccess(this, owner, player.getGameProfile())) {
                GtUtil.sendMessage(player, Reference.MODID+".info.access_error", owner.getName());
                return false;
            }
        }

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
                    if (((IGtUpgradeItem)currentItem).beforeInsert(upgradeStack, this, player)) return true;
                    else if (!((IGtUpgradeItem)currentItem).canBeInserted(upgradeStack, this)) return super.onActivated(player, hand, side, hitX, hitY, hitZ);
                }
                else continue;

                if (areItemsEqual) {
                    upgradeStack.grow(1);
                } else {
                    this.upgradeSlot.put(i, StackUtil.copyWithSize(stack, 1));
                }

                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                updateUpgrade(StackUtil.copyWithSize(stack, 1), player);
                break;
            }
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

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer && !world.isRemote) this.owner = ((EntityPlayer) placer).getGameProfile();
    }

    public void updateUpgrade(ItemStack stack, EntityPlayer player) {
        if (world.isRemote) return;

        Item currentItem = stack.getItem();
        if (currentItem instanceof IGtUpgradeItem) {
            ((IGtUpgradeItem)currentItem).afterInsert(stack, this, player);
        } else if (currentItem instanceof IUpgradeItem) {
            IC2UpgradeType upgradeType = IC2UpgradeType.fromStack(stack);
            switch (upgradeType) {
                case OVERCLOCKER:
                    setOverclockerCount(this.overclockersCount + stack.getCount());
                    break;
                case TRANSFORMER:
                    this.energy.setSinkTier(Math.min(this.energy.getSinkTier() + stack.getCount(), 3));
                    break;
                case BATTERY:
                    this.energy.setCapacity(this.energy.getCapacity()+(10000 * stack.getCount()));
                    break;
            }
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
        if (descriptionKey != null) tooltip.add(GtUtil.translateTeBlockDescription(descriptionKey));
        tooltip.add(GtUtil.translateInfo("max_energy_in", Math.round(EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier()))));
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
        tooltip.add(GtUtil.translate("teblock.info.possible_upgrades")+": " + String.join(" ", possibleUpgrades));
    }

    private int calculateAverageInput() {
        if (this.energy.getSinkDirs().isEmpty()) return 0;
        int currentEU = (int) this.energy.getEnergy();
        this.input = currentEU - previousEU;
        this.previousEU = currentEU;

        if (input > 0) averageEUInputRaw[averageEUInputIndex] = input;
        if (++averageEUInputIndex >= averageEUInputRaw.length) averageEUInputIndex  = 0;
        if (++averageEUOutputIndex >= averageEUOutputRaw.length) averageEUOutputIndex = 0;

        int rEU = 0;
        for (int tEU : averageEUInputRaw) rEU += tEU;
        return rEU / averageEUInputRaw.length;
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
}