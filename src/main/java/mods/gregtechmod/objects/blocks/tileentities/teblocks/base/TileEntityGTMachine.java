package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.ref.FluidName;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.inventory.GtSlotProcessableItemStack;
import mods.gregtechmod.util.MachineSafety;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TileEntityGTMachine<R extends IGtMachineRecipe<IRecipeIngredient, Collection<ItemStack>>, RM extends IGtRecipeManager<IRecipeIngredient, ItemStack, R>> extends TileEntityUpgradable implements IHasGui, IGuiValueProvider, IExplosionPowerOverride, INetworkTileEntityEventListener, IScannerInfoProvider, IPanelInfoProvider {
    protected double progress;
    public int maxProgress = 0;
    public boolean shouldExplode;
    private boolean explode;
    private int explosionTier;
    protected float guiProgress;

    public AudioSource audioSource;

    public final RM recipeManager;
    public final GtSlotProcessableItemStack<RM> inputSlot;
    public InvSlotOutput outputSlot;

    protected Collection<ItemStack> pendingRecipe = new ArrayList<>();

    public TileEntityGTMachine(int maxEnergy, int energyConsume, byte outputSlots, byte inputSlots, int defaultTier, RM recipeManager) {
        super(maxEnergy, defaultTier, energyConsume);
        this.progress = 0;
        this.recipeManager = recipeManager;
        this.inputSlot = new GtSlotProcessableItemStack<>(this, "input", inputSlots, recipeManager);
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
            if(nbt.getTag("outputStack"+i) != null) {
                NBTTagCompound tNBT = (NBTTagCompound) nbt.getTag("outputStack"+i);
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
                nbt.setTag("outputStack"+i, tNBT);
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

    @Override
    protected void onExploded(Explosion explosion) {
        if (GregTechConfig.MACHINES.machineChainExplosions) markForExplosion();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if(this.explode) {
            this.energy.onUnloaded();
            this.explodeMachine(getExplosionPower(this.explosionTier, 1.5F));
        }
        if (shouldExplode) this.explode = true; //Extra step so machines don't explode before the packet of death is sent
        MachineSafety.checkSafety(this);
        R recipe = getRecipe();
        if (canOperate(recipe)) {
            if (this.energy.canUseEnergy(energyConsume)) {
                this.energy.useEnergy(energyConsume);
                needsInvUpdate = operate(recipe);
            } else if (hasSteamUpgrade && canDrainSteam(neededSteam = getRequiredSteam())) {
                needsInvUpdate = operate(recipe);
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
        float multiplier = 0.5F;
        if (this.steamTank.getFluidAmount() < 1) return multiplier;
        Fluid fluid = this.steamTank.getFluid().getFluid();

        if (fluid == FluidName.superheated_steam.getInstance()) multiplier *= GregTechConfig.BALANCE.superHeatedSteamMultiplier;
        else if (fluid == FluidRegistry.getFluid("steam")) multiplier /= GregTechConfig.BALANCE.steamMultiplier;

        return multiplier;
    }

    public int getRequiredSteam() {
        return Math.round(this.energyConsume / getSteamMultiplier());
    }

    protected boolean canOperate(R recipe) {
        boolean canWork = getActive() || pendingRecipe.size() > 0;
        if (!canWork && !enableWorking) return false;
        return canWork || recipe != null;
    }

    protected boolean operate(R recipe) {
        boolean needsInvUpdate = false;
            if (this.progress == 0) (IC2.network.get(true)).initiateTileEntityEvent( this, 0, true);
            if (!getActive() && pendingRecipe.size() < 1) {
                recipe.getOutput().stream().map(ItemStack::copy).forEach(this.pendingRecipe::add);
                this.maxProgress = recipe.getDuration();
                consumeInput(recipe);
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

    public void consumeInput(R recipe) {
        consumeInput(recipe, false);
    }

    public void consumeInput(R recipe, boolean consumeContainers) {
        this.inputSlot.consume(recipe, consumeContainers);
    }

    public void addOutput(Collection<ItemStack> processResult) {
        this.outputSlot.add(processResult);
    }

    public R getRecipe() {
        if (this.inputSlot.isEmpty()) return null;
        ItemStack input = this.inputSlot.get();
        R recipe = input.isEmpty() ? null : this.recipeManager.getRecipeFor(input);
        if (recipe == null) return null;
        if (this.outputSlot.canAdd(recipe.getOutput())) return recipe;
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

    public void explodeMachine(float power) {
        int x = this.pos.getX(), y = this.pos.getY(), z = this.pos.getZ();
        this.energy.onUnloaded();
        world.setBlockToAir(this.pos);
        new ExplosionIC2(world, null, x+0.5, y+0.5, z+0.5, power, 0.5F, ExplosionIC2.Type.Normal).doExplosion();
    }

    @Override
    protected boolean isFlammable(EnumFacing face) {
        return true;
    }

    @Override
    public void markForExplosion() {
        this.shouldExplode = true;
        this.explosionTier = this.energy.getSinkTier() + 1;
        if (GregTechConfig.MACHINES.machineWireFire) {
            double energy = this.energy.getEnergy();
            this.energy.onUnloaded();
            this.energy = Energy.asBasicSource(this, this.energy.getCapacity(), 5);
            this.energy.onLoaded();
            this.energy.forceAddEnergy(energy);
        }
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
    public double getOutputVoltage() {
        return 0;
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
        if (amount > getInputVoltage()) markForExplosion();
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

    @Override
    public double getUniversalEnergy() {
        if (this.steamTank != null) {
            double convertedSteam = steamTank.getFluidAmount() * getSteamMultiplier();
            if (this.energy.getEnergy() < convertedSteam) return convertedSteam;
        }
        return this.energy.getEnergy();
    }

    @Override
    public double getUniversalEnergyCapacity() {
        if (steamTank != null) return Math.max(this.energy.getCapacity(), steamTank.getCapacity() * getSteamMultiplier());
        else return this.energy.getCapacity();
    }

    @Override
    public boolean shouldExplode() {
        return true;
    }

    @Override
    public float getExplosionPower(int tier, float defaultPower) {
        switch (tier) {
            case 2:
                return GregTechConfig.BALANCE.MVExplosionPower;
            case 3:
                return GregTechConfig.BALANCE.HVExplosionPower;
            case 4:
                return GregTechConfig.BALANCE.EVExplosionPower;
            case 5:
                return GregTechConfig.BALANCE.IVExplosionPower;

            default: return GregTechConfig.BALANCE.LVExplosionPower;
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
        return Math.round(this.progress / Math.pow(2, this.overclockersCount) / 20) + " secs";
    }

    @Override
    public String getTertiaryInfo() {
        return  "/" + Math.round(this.maxProgress / Math.pow(2, this.overclockersCount) / 20) + " secs";
    }
}
