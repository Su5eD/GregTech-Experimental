package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.util.Util;
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class TileEntityGTMachine<R extends IMachineRecipe<RI, List<ItemStack>>, RI, I, RM extends IGtRecipeManager<RI, I, R>> extends TileEntityUpgradable implements IHasGui, IMachineProgress, IGuiValueProvider, IExplosionPowerOverride, IPanelInfoProvider {
    public final RM recipeManager;
    public final GtSlotProcessableItemStack<RM, I> inputSlot;
    public InvSlotOutput outputSlot;

    protected List<ItemStack> pendingRecipe = new ArrayList<>();
    protected double progress;
    public double baseEnergyConsume;
    public double energyConsume;
    public int maxProgress;
    protected double guiProgress;

    public TileEntityGTMachine(String descriptionKey, int outputSlots, RM recipeManager) {
        this(descriptionKey, outputSlots, recipeManager, false);
    }

    public TileEntityGTMachine(String descriptionKey, int outputSlots, RM recipeManager, boolean wildcardInput) {
        super(descriptionKey);
        this.recipeManager = recipeManager;
        this.inputSlot = getInputSlot("input", wildcardInput);
        this.outputSlot = getOutputSlot("output", outputSlots);
    }
    
    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }
       
    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return Collections.emptySet();
    }

    public GtSlotProcessableItemStack<RM, I> getInputSlot(String name, boolean acceptAnything) {
        return new GtSlotProcessableItemStack<>(this, name, 1, acceptAnything ? null : recipeManager);
    }

    public InvSlotOutput getOutputSlot(String name, int count) {
        return new InvSlotOutput(this, name, count);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.progress = nbt.getDouble("progress");
        this.baseEnergyConsume = nbt.getDouble("baseEnergyConsume");
        this.energyConsume = nbt.getDouble("energyConsume");
        this.maxProgress = nbt.getInteger("operationLength");
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
    protected void onExploded(Explosion explosion) {
        if (GregTechConfig.MACHINES.machineChainExplosions) markForExplosion();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean dirty = false;
        R recipe = getRecipe();
        if (canOperate(recipe)) {
            updateEnergyConsume(recipe);

            if (this.energy.discharge(energyConsume) > 0 || hasMjUpgrade && this.receiver.extractPower(MjHelper.convert(energyConsume))) {
                dirty = processRecipe(recipe);
            } else if (hasSteamUpgrade && canDrainSteam(neededSteam = getEnergyForSteam(energyConsume))) {
                dirty = processRecipe(recipe);
                steamTank.drain(neededSteam, true);
            } else stop();
        } else stop();


        this.guiProgress = this.progress / Math.max(this.maxProgress, 1);
        if (dirty) markDirty();
    }

    protected void updateEnergyConsume(@Nullable R recipe) {
        if (recipe != null) {
            this.energyConsume = this.baseEnergyConsume = recipe.getEnergyCost();
            overclockEnergyConsume();
        }
    }

    protected boolean canOperate(R recipe) {
        boolean canWork = this.maxProgress > 0 || !this.pendingRecipe.isEmpty();
        if (!canWork && !isAllowedToWork()) return false;
        return canWork || recipe != null && canAddOutput(recipe);
    }
    
    protected boolean canAddOutput(R recipe) {
        return this.outputSlot.canAdd(recipe.getOutput());
    }

    protected boolean processRecipe(R recipe) {
        boolean needsInvUpdate = false;
            if (this.maxProgress <= 0 && pendingRecipe.size() < 1) {
                prepareRecipeForProcessing(recipe);
            }

            setActive(true);
            this.progress += Math.pow(2, getUpgradeCount(IC2UpgradeType.OVERCLOCKER));
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

    protected void prepareRecipeForProcessing(R recipe) {
        recipe.getOutput().stream()
                .map(ItemStack::copy)
                .forEach(this.pendingRecipe::add);
        this.maxProgress = recipe.getDuration();
        consumeInput(recipe);
    }

    @Override
    protected void onUpdateUpgrade(IC2UpgradeType type, ItemStack stack) {
        if (type == IC2UpgradeType.OVERCLOCKER) {
            overclockEnergyConsume();
        }
    }

    protected void overclockEnergyConsume() {
        this.energyConsume = this.baseEnergyConsume * (int) Math.pow(4, getUpgradeCount(IC2UpgradeType.OVERCLOCKER));
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

    public abstract void consumeInput(R recipe, boolean consumeContainers);

    public void addOutput(List<ItemStack> output) {
        this.outputSlot.add(output);
    }

    public abstract R getRecipe();

    public double getGuiValue(String name) {
        if (name.equals("progress")) return this.guiProgress;

        throw new IllegalArgumentException("Cannot get value for " + name);
    }

    @Override
    public boolean isActive() {
        return getActive();
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

    @Nonnull
    @Override
    public List<String> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel) {
        List<String> ret = super.getScanInfo(player, pos, scanLevel);
        if (scanLevel > 0) ret.add(GtUtil.translateInfo("machine_" + (isActive() ? "active" : "inactive")));
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
        return GtUtil.translateGeneric("time_secs", Math.round(this.progress / Math.pow(2, getUpgradeCount(IC2UpgradeType.OVERCLOCKER)) / 20));
    }

    @Override
    public String getTertiaryInfo() {
        return  "/" + GtUtil.translateGeneric("time_secs", Math.round(this.maxProgress / Math.pow(2, getUpgradeCount(IC2UpgradeType.OVERCLOCKER)) / 20));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
