package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.core.ExplosionIC2;
import ic2.core.IHasGui;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.ref.FluidName;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.inventory.GtSlotProcessableItemStack;
import mods.gregtechmod.util.GtUtil;
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

public abstract class TileEntityGTMachine<R extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>, RM extends IGtRecipeManager<IRecipeIngredient, ItemStack, R>> extends TileEntityUpgradable implements IHasGui, IGuiValueProvider, IExplosionPowerOverride, IScannerInfoProvider, IPanelInfoProvider {
    public boolean shouldExplode;
    private boolean explode;
    private int explosionTier;
    public final RM recipeManager;
    public final GtSlotProcessableItemStack<RM> inputSlot;
    public InvSlotOutput outputSlot;

    protected List<ItemStack> pendingRecipe = new ArrayList<>();
    protected double progress;
    public double baseEnergyConsume;
    public double energyConsume;
    public int maxProgress;
    protected double guiProgress;

    public TileEntityGTMachine(String descriptionKey, double maxEnergy, int outputSlots, int inputSlots, int defaultTier, RM recipeManager) {
        super(descriptionKey, maxEnergy, defaultTier);
        this.progress = 0;
        this.recipeManager = recipeManager;
        this.inputSlot = getInputSlot(inputSlots);
        this.outputSlot = getOutputSlot(outputSlots);
    }

    public GtSlotProcessableItemStack<RM> getInputSlot(int count) {
        return new GtSlotProcessableItemStack<>(this, "input", count, recipeManager);
    }

    public InvSlotOutput getOutputSlot(int count) {
        return new InvSlotOutput(this, "output", count);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.progress = nbt.getDouble("progress");
        this.baseEnergyConsume = nbt.getDouble("baseEnergyConsume");
        this.energyConsume = nbt.getDouble("energyConsume");
        this.maxProgress = nbt.getInteger("operationLength");
        this.enableWorking = nbt.getBoolean("enableWorking");
        this.enableInput = nbt.getBoolean("enableInput");
        this.enableOutput = nbt.getBoolean("enableOutput");
        for (int i = 0; i < 4; i++) {
            if(nbt.hasKey("outputStack"+i)) {
                NBTTagCompound tNBT = (NBTTagCompound) nbt.getTag("outputStack"+i);
                ItemStack stack = new ItemStack(tNBT);
                pendingRecipe.add(stack);
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("progress", this.progress);
        nbt.setDouble("baseEnergyConsume", this.baseEnergyConsume);
        nbt.setDouble("energyConsume", this.energyConsume);
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

    @Override
    protected boolean wrenchCanRemove(EntityPlayer player) {
        if (isPrivate && !GtUtil.checkAccess(this, owner, player.getGameProfile())) {
            GtUtil.sendMessage(player, Reference.MODID+".info.wrench_error", player.getName());
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
        boolean dirty = false;
        if(this.explode) {
            this.energy.onUnloaded();
            this.explodeMachine(getExplosionPower(this.explosionTier, 1.5F));
        }
        if (shouldExplode) this.explode = true; //Extra step so machines don't explode before the packet of death is sent
        MachineSafety.checkSafety(this);
        R recipe = getRecipe();
        if (canOperate(recipe)) {
            if (recipe != null) updateEnergyConsume(recipe);

            if (this.energy.useEnergy(energyConsume) || hasMjUpgrade && this.receiver.extractPower(MjHelper.convert(energyConsume))) {
                dirty = processRecipe(recipe);
            } else if (hasSteamUpgrade && canDrainSteam(neededSteam = getEnergyForSteam(energyConsume))) {
                dirty = processRecipe(recipe);
                steamTank.drain(neededSteam, true);
            } else stop();
        } else stop();


        this.guiProgress = this.progress / Math.max(this.maxProgress, 1);
        if (dirty) markDirty();
    }

    protected void updateEnergyConsume(R recipe) {
        this.energyConsume = this.baseEnergyConsume = recipe.getEnergyCost();
        overclockEnergyConsume();
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

    public int getEnergyForSteam(double amount) {
        return (int) Math.round(amount / getSteamMultiplier());
    }

    protected boolean canOperate(R recipe) {
        boolean canWork = this.maxProgress > 0 || !this.pendingRecipe.isEmpty();
        if (!canWork && !enableWorking) return false;
        return canWork || recipe != null;
    }

    protected boolean processRecipe(R recipe) {
        boolean needsInvUpdate = false;
            if (this.maxProgress <= 0 && pendingRecipe.size() < 1) {
                recipe.getOutput().stream()
                        .map(ItemStack::copy)
                        .forEach(this.pendingRecipe::add);
                this.maxProgress = recipe.getDuration();
                consumeInput(recipe);
            }

            setActive(true);
            this.progress += Math.pow(2, overclockersCount);
            if (this.progress >= this.maxProgress) {
                addOutput(pendingRecipe);
                needsInvUpdate = true;
                this.progress = 0;
                this.energyConsume = 0;
                this.maxProgress = 0;
                pendingRecipe.clear();
            }
        return needsInvUpdate;
    }

    protected void overclockEnergyConsume() {
        this.energyConsume = this.baseEnergyConsume * (int) Math.pow(4, overclockersCount);
    }

    @Override
    public void setOverclockerCount(int count) {
        super.setOverclockerCount(count);
        overclockEnergyConsume();
    }

    protected void stop() {
        if (needsConstantEnergy()) this.progress = 0;
        setActive(false);
    }

    protected boolean needsConstantEnergy() {
        return GregTechConfig.MACHINES.constantNeedOfEnergy;
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

    public abstract R getRecipe();

    public double getGuiValue(String name) {
        if (name.equals("progress")) return this.guiProgress;

        throw new IllegalArgumentException("Cannot get value for " + name);
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
        return EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier());
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
    public double addEnergy(double amount) {
        if (amount > getInputVoltage()) markForExplosion();
        return this.energy.addEnergy(amount);
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        if (this.energy.canUseEnergy(amount)) return this.energy.useEnergy(amount, simulate);
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
        return Math.max(this.energy.getEnergy(), Math.max(steam, mj));
    }

    @Override
    public double getUniversalEnergyCapacity() {
        double steam = this.hasSteamUpgrade ? this.steamTank.getCapacity() * getSteamMultiplier() : 0;
        double mj = this.hasMjUpgrade ? this.receiver.getCapacity() / (double) MjHelper.MJ : 0;
        return Math.max(this.energy.getCapacity(), Math.max(steam, mj));
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
        if (scanLevel > 2) ret.add("Meta-ID: " + this.getBlockMetadata());
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
            ret.add(GtUtil.translateInfo("machine_"+(isActive() ? "active" : "inactive")));
        }
        return ret;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtUtil.translateGeneric("progress") + ":";
    }

    @Override
    public String getSecondaryInfo() {
        return GtUtil.translateGeneric("time_secs", Math.round(this.progress / Math.pow(2, this.overclockersCount) / 20));
    }

    @Override
    public String getTertiaryInfo() {
        return  "/" + GtUtil.translateGeneric("time_secs", Math.round(this.maxProgress / Math.pow(2, this.overclockersCount) / 20));
    }
}
