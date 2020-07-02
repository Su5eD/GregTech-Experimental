package mods.gregtechmod.common.objects.blocks.machines.tileentity.base;

import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import ic2.core.ref.FluidName;
import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.cover.ICover;
import mods.gregtechmod.common.util.IGregtechMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TileEntityGTMachine extends TileEntityUpgradable implements IHasGui, IUpgradableBlock, IGuiValueProvider, IExplosionPowerOverride, INetworkTileEntityEventListener, IGregtechMachine {
    protected double progress;
    public int operationLength = 40;
    private boolean enableWorking = true;

    @GuiSynced
    protected float guiProgress;

    public AudioSource audioSource;

    public final InvSlotProcessable<IRecipeInput, Collection<ItemStack>, ItemStack> inputSlot;
    public InvSlotOutput outputSlot;

    protected Collection<ItemStack> pendingRecipe = new ArrayList<ItemStack>() {};

    public TileEntityGTMachine(int maxEnergy, int energyConsume, byte outputSlots, byte inputSlots, int aDefaultTier, IMachineRecipeManager<IRecipeInput, Collection<ItemStack>, ItemStack> recipeSet) {
        super(maxEnergy, aDefaultTier, energyConsume);
        this.progress = 0;
        this.inputSlot = new InvSlotProcessableGeneric(this, "input", inputSlots, recipeSet);
        this.outputSlot = new InvSlotOutput(this, "output", outputSlots);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.progress = nbt.getDouble("progress");
        this.operationLength = nbt.getInteger("operationLength");
        this.enableWorking = nbt.getBoolean("enableWorking");
        for (int i = 0; i < 4; i++) {
            if(nbt.getTag("mOutput"+i) != null) {
                NBTTagCompound tNBT = (NBTTagCompound) nbt.getTag("mOutput"+i);
                ItemStack stack = new ItemStack(tNBT);
                pendingRecipe.add(stack);
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("progress", this.progress);
        nbt.setInteger("operationLength", this.operationLength);
        nbt.setBoolean("enableWorking", this.enableWorking);
        if(pendingRecipe.size() > 0) {
            for (int i = 0; i < pendingRecipe.size(); i++) {
                NBTTagCompound tNBT = new NBTTagCompound();
                ItemStack stack = (ItemStack) pendingRecipe.toArray()[i];
                stack.writeToNBT(tNBT);
                nbt.setTag("mOutput"+i, tNBT);
            }
        }
        return nbt;
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        updateUpgrades(null);
    }

    @Override
    protected boolean wrenchCanRemove(EntityPlayer player) {
        if (isPrivate && !checkAccess(owner, player.getGameProfile())) {
            IC2.platform.messagePlayer(player, "This block is owned by "+player.getGameProfile().getName()+", only they can remove it.");
            return false;
        }
        return true;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result = getOutput();
        if (canOperate(result)) {
            if (this.energy.canUseEnergy(energyConsume)) {
                this.energy.useEnergy(energyConsume);
                needsInvUpdate = operate(result);
            } else if (hasSteamUpgrade && canDrainSteam(neededSteam = getRequiredSteam())) {
                needsInvUpdate = operate(result);
                steamTank.drain(neededSteam, true);
            } else stop();
        }

        this.guiProgress = (float) this.progress / this.operationLength;
        if (needsInvUpdate) super.markDirty();
    }

    public boolean canDrainSteam(int requiredAmount) {
        if (requiredAmount < 1 || steamTank == null) return false;
        return steamTank.getFluidAmount() >= requiredAmount;
    }

    public float getSteamMultiplier() {
        float multiplier = 2;
        Fluid fluid = this.steamTank.getFluid().getFluid();

        if (fluid == FluidName.superheated_steam.getInstance()) multiplier *= supersteamBalance;
        else if (fluid == FluidRegistry.getFluid("steam")) multiplier *= steamBalance;

        return multiplier;
    }

    public int getRequiredSteam() {
        return (int) (this.energyConsume * getSteamMultiplier());
    }

    protected boolean canOperate(MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result) {
        boolean canWork = getActive() || pendingRecipe.size() > 0;
        if (!canWork && !enableWorking) return false;
        return canWork || result != null;
    }

    protected boolean operate(MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result) {
        boolean needsInvUpdate = false;
            if (this.progress == 0) (IC2.network.get(true)).initiateTileEntityEvent( this, 0, true);
            if (!getActive() && pendingRecipe.size() < 1) {
                consumeInput(result);
                this.operationLength = result.getRecipe().getMetaData().getInteger("duration");
                this.pendingRecipe.addAll(result.getOutput());
            }
            setOverclock();
            setActive(true);
            this.progress += Math.pow(2, overclockersCount);
            if (this.progress >= this.operationLength) {
                addOutput(pendingRecipe);
                needsInvUpdate = true;
                this.progress = 0;
                (IC2.network.get(true)).initiateTileEntityEvent( this, 2, true);
                setActive(false);
                pendingRecipe.clear();
            }
        return needsInvUpdate;
    }

    protected void stop() {
        if (this.progress != 0 && getActive()) (IC2.network.get(true)).initiateTileEntityEvent(this, 1, true);
        if (ConfigLoader.constantNeedOfEnergy) this.progress = 0;
        setActive(false);
    }

    public void consumeInput(MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result) {
        this.inputSlot.consume(result);
    }

    public void addOutput(Collection<ItemStack> processResult) {
        this.outputSlot.add(processResult);
    }

    public MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> getOutput() {
        if (this.inputSlot.isEmpty()) return null;
        MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> output = this.inputSlot.process();
        if (output == null || output.getRecipe().getMetaData() == null) return null;
        if (this.outputSlot.canAdd(output.getOutput())) return output;
        return null;
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null)
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
        switch (event) {
            case 0:
                if (this.audioSource != null)
                    this.audioSource.play();
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null)
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
                }
                break;
            case 2:
                if (this.audioSource != null)
                    this.audioSource.stop();
                break;
        }
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    public double getGuiValue(String name) {
        if (name.equals("progress"))
            return this.guiProgress;
        throw new IllegalArgumentException(getClass().getSimpleName() + " Cannot get value for " + name);
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public int getMaxProgress() {
        return operationLength;
    }

    @Override
    public void increaseProgress(double amount) {
        this.progress += amount;
    }

    @Override
    public boolean isActive() {
        return this.getActive();
    }

    @Override
    public void setRedstoneOutput(EnumFacing side, byte strength) {
        this.rsEmitter.setLevel(side, strength);
    }

    @Override
    protected int getWeakPower(EnumFacing side) {
        return rsEmitter.getLevel(side.getOpposite());
    }

    @Override
    protected boolean canConnectRedstone(EnumFacing side) {
        EnumFacing aSide = side.getOpposite();
        if (handler.covers.containsKey(aSide)) {
            ICover cover = handler.covers.get(aSide);
            return cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone();
        }
        return false;
    }

    @Override
    public double getInputVoltage() {
        return 8*Math.pow(4, this.energy.getSinkTier());
    }

    @Override
    public double getStoredEU() {
        return this.energy.getEnergy();
    }

    @Override
    public double getEUCapacity() {
        return this.energy.getCapacity();
    }

    @Override
    public int getAverageEUInput() {
        return this.averageEUIn;
    }

    @Override
    public int getAverageEUOutput() {
        return 0;
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
    public void markForCoverBehaviorUpdate() {
        this.needsCoverBehaviorUpdate = true;
    }

    @Override
    public void disableWorking() {
        this.enableWorking = false;
    }

    @Override
    public void enableWorking() {
        this.enableWorking = true;
    }

    @Override
    public boolean isAllowedToWork() {
        return this.enableWorking;
    }

    @Override
    public double addEnergy(double amount) {
        return this.energy.addEnergy(amount);
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        if (this.energy.canUseEnergy(amount)) return this.energy.useEnergy(amount, simulate);
        else if (canDrainSteam(getRequiredSteam())) {
            return steamTank.drain(getRequiredSteam(), true).amount;
        }
        return 0;
    }

    public double convertSteamToEU() {
        return steamTank.getFluidAmount() / getSteamMultiplier();
    }

    @Override
    public double getUniversalEnergy() {
        double convertedSteam;
        if (this.energy.getEnergy() < (convertedSteam = convertSteamToEU())) return convertedSteam;
        return this.energy.getEnergy();
    }
}
