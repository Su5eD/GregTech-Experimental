package mods.gregtechmod.objects.blocks.tileentities.machines.base;

import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipeResult;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import ic2.core.ref.FluidName;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TileEntityGTMachine extends TileEntityUpgradable implements IHasGui, IGuiValueProvider, IExplosionPowerOverride, INetworkTileEntityEventListener, IScannerInfoProvider, IPanelInfoProvider {
    protected double progress;
    public int maxProgress = 0;

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
        this.maxProgress = nbt.getInteger("operationLength");
        this.enableWorking = nbt.getBoolean("enableWorking");
        this.enableInput = nbt.getBoolean("enableInput");
        this.enableOutput = nbt.getBoolean("enableOutput");
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
        nbt.setInteger("operationLength", this.maxProgress);
        nbt.setBoolean("enableWorking", this.enableWorking);
        nbt.setBoolean("enableInput", this.enableInput);
        nbt.setBoolean("enableOutput", this.enableOutput);
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

        this.guiProgress = (float) this.progress / this.maxProgress;
        if (needsInvUpdate) super.markDirty();
    }

    public boolean canDrainSteam(int requiredAmount) {
        if (requiredAmount < 1 || steamTank == null) return false;
        return steamTank.getFluidAmount() >= requiredAmount;
    }

    public float getSteamMultiplier() {
        float multiplier = 2;
        if (this.steamTank.getFluidAmount() < 1) return multiplier;
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
                this.maxProgress = result.getRecipe().getMetaData().getInteger("duration");
                this.pendingRecipe.addAll(result.getOutput());
            }
            setOverclock();
            setActive(true);
            this.progress += Math.pow(2, overclockersCount);
            if (this.progress >= this.maxProgress) {
                addOutput(pendingRecipe);
                needsInvUpdate = true;
                this.progress = 0;
                (IC2.network.get(true)).initiateTileEntityEvent( this, 2, true);
                setActive(false);
                pendingRecipe.clear();
                this.maxProgress = 0;
            }
        return needsInvUpdate;
    }

    protected void stop() {
        if (this.progress != 0 && getActive()) (IC2.network.get(true)).initiateTileEntityEvent(this, 1, true);
        if (GregTechConfig.MACHINES.constantNeedOfEnergy) this.progress = 0;
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

    protected void explodeMachine(int power) {
        world.setBlockToAir(this.pos);
        new ExplosionIC2(world, null, pos.getX(), pos.getY(), pos.getZ(), power, 0.5F, ExplosionIC2.Type.Normal).doExplosion();
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public int getMaxProgress() {
        return maxProgress;
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
    public double addEnergy(double amount) {
        if (amount > getInputVoltage()) explodeMachine((int) getExplosionPower(this.energy.getSinkTier(), 2.5F));
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
        if (steamTank != null && this.energy.getEnergy() < (convertedSteam = convertSteamToEU())) {
            return convertedSteam;
        }
        return this.energy.getEnergy();
    }

    @Override
    public double getUniversalEnergyCapacity() {
        if (steamTank != null) return Math.max(this.energy.getCapacity(), steamTank.getCapacity() / getSteamMultiplier());
        else return this.energy.getCapacity();
    }

    @Override
    public boolean shouldExplode() {
        return true;
    }

    @Override
    public float getExplosionPower(int i, float v) {
        switch (this.energy.getSinkTier()) {
            case 2:
                return (float) GregTechConfig.BALANCE.MVExplosionPower;
            case 3:
                return (float) GregTechConfig.BALANCE.HVExplosionPower;

            default: return (float) GregTechConfig.BALANCE.LVExplosionPower;
        }
    }

    @Nonnull
    @Override
    public List<String> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel) {
        List<String> ret = new ArrayList<>();
        if (scanLevel > 2)
            ret.add("Meta-ID: " + this.getBlockMetadata());
        if (scanLevel > 1)
            ret.add("Is" + (checkAccess(this.owner, player.getGameProfile()) ? " " : " not ") + "accessible for you");
        if (scanLevel > 0) {
            if (getSteamCapacity() > 0 && this.hasSteamUpgrade)
                ret.add(this.steamTank.getFluidAmount() + " of " + this.steamTank.getCapacity() + " Steam");
            ret.add("Machine is " + (this.isActive() ? "active" : "inactive"));
        }
        return ret;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return "Progress:";
    }

    @Override
    public String getSecondaryInfo() {
        return (int) (this.progress / 20) + " secs";
    }

    @Override
    public String getTertiaryInfo() {
        return  "/" + (this.maxProgress / 20) + " secs";
    }
}
