package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.Util;
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import mods.gregtechmod.recipe.util.SteamHelper;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import mods.gregtechmod.util.nbt.Serializers.ItemStackListNBTSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TileEntityGTMachine<R extends IMachineRecipe<RI, List<ItemStack>>, RI, I, RM extends IGtRecipeManager<RI, I, R>> extends TileEntityUpgradable implements IHasGui, IMachineProgress, IExplosionPowerOverride, IPanelInfoProvider {
    public final RM recipeManager;
    public final GtSlotProcessableItemStack<RM, I> inputSlot;
    public InvSlotOutput outputSlot;

    @NBTPersistent(include = Include.NOT_EMPTY, handler = ItemStackListNBTSerializer.class)
    protected List<ItemStack> pendingRecipe = new ArrayList<>();
    @NBTPersistent
    protected double progress;
    @NBTPersistent
    public double baseEnergyConsume;
    @NBTPersistent
    public double energyConsume;
    @NBTPersistent
    public int maxProgress;

    public TileEntityGTMachine(int outputSlots, RM recipeManager) {
        this(outputSlots, recipeManager, false);
    }

    public TileEntityGTMachine(int outputSlots, RM recipeManager, boolean wildcardInput) {
        this.recipeManager = recipeManager;
        this.inputSlot = getInputSlot("input", wildcardInput);
        this.outputSlot = getOutputSlot("output", outputSlots);

        addGuiValue("progress", this::getGuiProgress);
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    public GtSlotProcessableItemStack<RM, I> getInputSlot(String name, boolean acceptAnything) {
        return new GtSlotProcessableItemStack<>(this, name, 1, acceptAnything ? null : recipeManager);
    }

    public GtSlotProcessableItemStack<RM, I> getInputSlot(String name, InvSlot.InvSide side, boolean acceptAnything) {
        return new GtSlotProcessableItemStack<>(this, name, InvSlot.Access.I, 1, side, acceptAnything ? null : recipeManager);
    }

    public InvSlotOutput getOutputSlot(String name, int count) {
        return new InvSlotOutput(this, name, count);
    }

    @Override
    protected void onExploded(Explosion explosion) {
        if (GregTechConfig.MACHINES.machineChainExplosions) markForExplosion();
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isProcessing()) {
            boolean hasEnoughEnergy = checkEnergy();
            if (hasEnoughEnergy) {
                processRecipe();
            }
            else stop();
        }
        else {
            R recipe = getRecipe();
            if (canProcessRecipe(recipe)) {
                updateEnergyConsume(recipe);

                boolean hasEnoughEnergy = checkEnergy();
                if (hasEnoughEnergy) {
                    if (this.maxProgress <= 0 && this.pendingRecipe.isEmpty()) {
                        prepareRecipeForProcessing(recipe);
                    }
                    processRecipe();
                }
                else stop();
            }
            else stop();
        }
    }

    protected boolean checkEnergy() {
        if (useEnergy(this.energyConsume) > 0 || this.hasMjUpgrade && this.receiver.extractPower(MjHelper.microJoules(this.energyConsume))) {
            return true;
        }
        else if (this.hasSteamUpgrade && canDrainSteam(this.neededSteam = SteamHelper.getSteamForEU(this.energyConsume, this.steamTank.getFluid()))) {
            this.steamTank.drain(this.neededSteam, true);
            return true;
        }
        return false;
    }

    protected void updateEnergyConsume(@Nullable R recipe) {
        if (recipe != null) {
            this.energyConsume = this.baseEnergyConsume = recipe.getEnergyCost();
            overclockEnergyConsume();
        }
    }

    protected boolean isProcessing() {
        boolean active = this.maxProgress > 0 || !this.pendingRecipe.isEmpty();
        if (!active && !isAllowedToWork()) return false;
        return active;
    }

    protected boolean canProcessRecipe(R recipe) {
        return recipe != null && isAllowedToWork() && canAddOutput(recipe);
    }

    protected boolean canAddOutput(R recipe) {
        return this.outputSlot.canAdd(recipe.getOutput());
    }

    protected void processRecipe() {
        setActive(true);
        this.progress += 1 << getUpgradeCount(IC2UpgradeType.OVERCLOCKER);
        if (this.progress >= this.maxProgress) {
            addOutput(pendingRecipe);
            this.progress = 0;
            this.energyConsume = 0;
            this.maxProgress = 0;
            this.pendingRecipe.clear();
            markDirty();
        }
    }

    protected void prepareRecipeForProcessing(R recipe) {
        recipe.getOutput().stream()
            .map(ItemStack::copy)
            .forEach(this.pendingRecipe::add);
        this.maxProgress = recipe.getDuration();
        consumeInput(recipe);
    }

    @Override
    protected void onUpdateIC2Upgrade(IC2UpgradeType type) {
        if (type == IC2UpgradeType.OVERCLOCKER) {
            overclockEnergyConsume();
        }
    }

    protected void overclockEnergyConsume() {
        this.energyConsume = this.baseEnergyConsume * (int) Math.pow(4, getUpgradeCount(IC2UpgradeType.OVERCLOCKER));
    }

    protected void stop() {
        if (needsConstantEnergy()) this.progress = 0;
        if (getActive()) setActive(false);
    }

    protected boolean needsConstantEnergy() {
        return GregTechConfig.MACHINES.constantNeedOfEnergy;
    }

    public void consumeInput(R recipe) {
        consumeInput(recipe, true);
    }

    protected abstract void consumeInput(R recipe, boolean consumeContainers);

    public void addOutput(List<ItemStack> output) {
        this.outputSlot.add(output);
    }

    public abstract R getRecipe();

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

    private double getGuiProgress() {
        return this.progress / Math.max(this.maxProgress, 1);
    }

    @Override
    public void getScanInfoPost(List<ITextComponent> scan, EntityPlayer player, BlockPos pos, int scanLevel) {
        super.getScanInfoPost(scan, player, pos, scanLevel);
        if (scanLevel > 0) scan.add(new TextComponentTranslation(GtLocale.buildKeyInfo("machine_" + (this.getActive() ? "active" : "inactive"))));
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtLocale.translateGeneric("progress") + ":";
    }

    @Override
    public String getSecondaryInfo() {
        double seconds = this.progress / (1 << getUpgradeCount(IC2UpgradeType.OVERCLOCKER)) / 20;
        return GtLocale.translateGeneric("time_secs", Math.round(seconds));
    }

    @Override
    public String getTertiaryInfo() {
        double seconds = this.maxProgress / (double) (1 << getUpgradeCount(IC2UpgradeType.OVERCLOCKER)) / 20;
        return "/" + GtLocale.translateGeneric("time_secs", Math.round(seconds));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
