package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiMultiblock;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.Maintenance;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMultiblock;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import mods.gregtechmod.util.struct.Structure;
import mods.gregtechmod.util.struct.StructureElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TileEntityMultiBlockBase<T extends TileEntityMultiBlockBase.MultiBlockInstance> extends TileEntityUpgradable implements IPanelInfoProvider, IHasGui {
    public final Structure<T> structure;
    protected final IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager;
    public final InvSlot machinePartSlot;
    
    @NBTPersistent
    protected int efficiency;
    @NBTPersistent
    protected int efficiencyIncrease;
    @NBTPersistent
    private int pollution;
    @NBTPersistent
    private int runtime;
    private int startUpCheck = 100;
    public final Maintenance maintenance;
    
    @NBTPersistent
    protected double fuelEnergy;
    @NBTPersistent
    protected int progress;
    @NBTPersistent
    protected int maxProgress;
    @NBTPersistent(include = Include.NON_NULL)
    protected ItemStack fuelOutput;

    public TileEntityMultiBlockBase(IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager) {
        this.fuelManager = fuelManager;
        this.maintenance = addComponent(new Maintenance(this));
        this.structure = new Structure<>(getStructurePattern(), getStructureElements(), this::createStructureInstance, this::onInvalidate);
        this.machinePartSlot = new GtSlotFiltered(this, "machine_part", InvSlot.Access.NONE, 1, this::acceptsMachinePart);
    }
    
    protected abstract List<List<String>> getStructurePattern();
        
    protected abstract Map<Character, Collection<StructureElement>> getStructureElements();
    
    protected abstract T createStructureInstance(EnumFacing facing, Map<Character, Collection<BlockPos>> elements);

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.structure.checkWorldStructure(this.pos, this.getFacing());
    }

    @Override
    protected void updateEntityClient() {
        super.updateEntityClient();
            
        if (this.tickCounter++ % 5 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing());
        }
    }
        
    @Override
    protected void preTickServer() {
        super.preTickServer();
        
        if (this.tickCounter % 5 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing());
        }
        
        if (--this.startUpCheck >= 0) return;
        Optional<Structure<T>.WorldStructure> struct = this.structure.getWorldStructure();
        if (!struct.map(Structure.WorldStructure::isValid).orElse(false)) {
            stopMachine();
            return;
        }
        
        T instance = struct.get().getInstance();
        instance.collectMaintenanceStatus(this.maintenance);
        int repairStatus = this.getRepairStatus();
        
        if (isAllowedToWork() && repairStatus > 0) {
            ItemStack machinePart = this.machinePartSlot.get();
            if (this.maxProgress > 0 && doRandomMaintenanceDamage(machinePart)) {
                if (!polluteEnvironment(instance, getPollutionPerTick(machinePart))) {
                    stopMachine();
                } else {
                    setActive(true);
                    onStart(instance);
                    onRunningTick(instance);
                    
                    if (++this.progress >= this.maxProgress) {
                        addOutput(instance);
                    
                        this.efficiency = Math.max(0, Math.min(this.efficiency + this.efficiencyIncrease, getMaxEfficiency(machinePart) - (getIdealStatus() - repairStatus) * 1000));
                        this.progress = this.maxProgress = this.efficiencyIncrease = 0;
                        this.fuelOutput = null;
                        checkFuel(instance);
                        if (this.maxProgress <= 0) {
                            setActive(false);
                            onStop(instance);
                        }
                    }
                }
            } else {
                checkFuel(instance);
                this.efficiency = Math.max(0, this.efficiency - 1000);
            }
        } else stopMachine();
    }
    
    protected void onStart(T instance) {}
    
    protected void onStop(T instance) {}
    
    private void checkFuel(MultiBlockInstance instance) {
        if (isAllowedToWork()) {
            IFuel<IRecipeIngredient> fuel = getFuel(instance);
            if (fuel != null) {
                this.fuelOutput = getFuelOutput(fuel);
                if (consumeInput(instance, fuel)) processFuel(instance, fuel);
            }
        }
    }

    private boolean doRandomMaintenanceDamage(ItemStack machinePart) {
        if (!isCorrectMachinePart() || getRepairStatus() == 0) {
            stopMachine();
            return false;
        }
        
        if (this.runtime++ > 1000) {
            this.runtime = 0;
            
            this.maintenance.doRandomMaintenanceDamage(this.world.rand);
                    
            if (!machinePart.isEmpty() && world.rand.nextInt(2) == 0) {
                machinePart.setItemDamage(machinePart.getItemDamage() + getDamageToComponent(machinePart));
                if (machinePart.getItemDamage() >= machinePart.getMaxDamage()) {
                    this.machinePartSlot.clear();
                    if (explodesOnComponentBreak(machinePart)) {
                        stopMachine();
                        markForExplosion(7);
                    }
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public void onRunningTick(MultiBlockInstance instance) {
        int generatedEU = (int) (this.fuelEnergy * this.efficiency / 10000L);
        instance.addEnergyOutput(generatedEU);
    }

    public boolean polluteEnvironment(MultiBlockInstance instance, int pollutionLevel) {
        this.pollution += pollutionLevel;
        instance.polluteEnvironment(this.pollution);
        return this.pollution < 10000;
    }
    
    public int getIdealStatus() { 
        return 6;
    }
    
    public void stopMachine() {
        this.efficiency = this.efficiencyIncrease = this.progress = this.maxProgress = 0;
        this.fuelEnergy = 0;
        this.fuelOutput = null;
        setAllowedToWork(false);
        setActive(false);
        this.structure.getWorldStructure()
                .map(Structure.WorldStructure::getInstance)
                .ifPresent(this::onStop);
    }

    /**
     * Required for the machine to run
     */
    public abstract boolean isCorrectMachinePart();

    /**
     * @return Whether the item should be accepted in the {@link #machinePartSlot}
     */
    public abstract boolean acceptsMachinePart(ItemStack stack);
    
    public abstract int getDamageToComponent(ItemStack stack);
    
    public abstract boolean explodesOnComponentBreak(ItemStack stack);
    
    public abstract int getPollutionPerTick(ItemStack stack);
    
    public abstract int getMaxEfficiency(ItemStack stack);
    
    public abstract IFuel<IRecipeIngredient> getFuel(MultiBlockInstance instance);
    public abstract void processFuel(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel);
    public abstract ItemStack getFuelOutput(IFuel<IRecipeIngredient> fuel);
    
    protected void addOutput(MultiBlockInstance instance) {
        instance.addOutput(this.fuelOutput);
    }
    
    protected Optional<IFuel<IRecipeIngredient>> getFluidFuel(MultiBlockInstance instance) {
        return instance.getInputFluids().stream()
                .map(FluidStack::getFluid)
                .map(this.fuelManager::getFuel)
                .filter(Objects::nonNull)
                .findFirst();
    }

    protected boolean consumeInput(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel) {
        IRecipeIngredient input = fuel.getInput();
        if (input instanceof IRecipeIngredientFluid) {
            return consumeFluidInput(instance, (IRecipeIngredientFluid) input);
        } else {
            return input.getMatchingInputs()
                    .stream()
                    .anyMatch(instance::depleteInput);
        }
    }

    protected boolean consumeFluidInput(MultiBlockInstance instance, IRecipeIngredientFluid input) {
        int buckets = input.getCount() * Fluid.BUCKET_VOLUME;
        return input.getMatchingFluids()
                .stream()
                .map(fluid -> new FluidStack(fluid, buckets))
                .anyMatch(instance::depleteInput);
    }
            
    private int getRepairStatus() {
        return (int) Stream.of(
                this.maintenance.getWrench(),
                this.maintenance.getScrewdriver(),
                this.maintenance.getSoftHammer(),
                this.maintenance.getHardHammer(),
                this.maintenance.getSolderingTool(),
                this.maintenance.getCrowbar()
        )
                .filter(Boolean::booleanValue)
                .count();
    }

    @Override
    protected void onBlockBreak() {
        super.onBlockBreak();
        this.structure.getWorldStructure()
                .map(Structure.WorldStructure::getInstance)
                .ifPresent(this::onInvalidate);
    }

    protected void onInvalidate(T instance) {}

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.singleton(GtUpgradeType.LOCK);
    }
    
    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtLocale.translateGeneric("progress") + ": " + GtLocale.translateGeneric("time_secs", this.progress / 20 + "/" + this.maxProgress / 20);
    }

    @Override
    public String getSecondaryInfo() {
        return GtLocale.translateGeneric("efficiency", this.efficiency / 100F);
    }

    @Override
    public String getTertiaryInfo() {
        int problems = this.getRepairStatus();
        return GtLocale.translateGeneric("problems", getIdealStatus() - problems);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 0;
    }
    
    @Override
    public ContainerMultiblock getGuiContainer(EntityPlayer player) {
        return new ContainerMultiblock(player, this);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiMultiblock(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public static class MultiBlockInstance {
        protected final Collection<BlockPos> positions;
        protected final List<TileEntityHatchInput> inputHatches = new ArrayList<>();
        protected final List<TileEntityHatchOutput> outputHatches = new ArrayList<>();
        protected final List<TileEntityHatchDynamo> dynamoHatches = new ArrayList<>();
        protected final List<TileEntityHatchMuffler> mufflerHatches = new ArrayList<>();
        protected final List<TileEntityHatchMaintenance> maintenanceHatches = new ArrayList<>();
        
        protected MultiBlockInstance(IBlockAccess world, Map<Character, Collection<BlockPos>> elements) {
            this.positions = elements.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            
            this.positions.stream()
                    .map(world::getTileEntity)
                    .filter(Objects::nonNull)
                    .forEach(tileEntity -> {
                        findHatch(tileEntity, TileEntityHatchInput.class, inputHatches);
                        findHatch(tileEntity, TileEntityHatchOutput.class, outputHatches);
                        findHatch(tileEntity, TileEntityHatchDynamo.class, dynamoHatches);
                        findHatch(tileEntity, TileEntityHatchMuffler.class, mufflerHatches);
                        findHatch(tileEntity, TileEntityHatchMaintenance.class, maintenanceHatches);
                    });
        }
        
        @SuppressWarnings("unchecked")
        protected <T extends TileEntity> void findHatch(TileEntity tileEntity, Class<T> clazz, List<T> list) {
            if (clazz.isInstance(tileEntity)) list.add((T) tileEntity);
        }
        
        public void collectMaintenanceStatus(Maintenance target) {
            maintenanceHatches.stream()
                    .map(TileEntityHatchMaintenance::getMaintenance)
                    .forEach(target::collectMaintenanceStatus);
        }
        
        public void addEnergyOutput(int eu) {
            dynamoHatches.stream()
                    .anyMatch(hatch -> hatch.addEnergy(eu));
        }
        
        public void polluteEnvironment(int pollutionLevel) {
            for (TileEntityHatchMuffler hatch : mufflerHatches) {
                if (pollutionLevel >= 10000) {
                    if (hatch.polluteEnvironment()) pollutionLevel -= 10000;
                    continue;
                }
                break;
            }
        }
        
        public boolean addOutput(ItemStack stack) {
            return outputHatches.stream()
                    .anyMatch(hatch -> hatch.addOutput(stack));
        }
        
        public boolean addOutput(FluidStack fluid) {
            return outputHatches.stream()
                    .anyMatch(hatch -> hatch.addOutput(fluid));
        }
        
        public List<ItemStack> getInputItems() {
            return inputHatches.stream()
                    .map(TileEntityHatchInput::getItem)
                    .filter(stack -> !stack.isEmpty())
                    .collect(Collectors.toList());
        }
        
        public List<FluidStack> getInputFluids() {
            return inputHatches.stream()
                    .map(TileEntityHatchInput::getFluid)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        
        public boolean depleteInput(ItemStack stack) {
            return inputHatches.stream()
                    .anyMatch(hatch -> hatch.depleteInput(stack));
        }
        
        public boolean depleteInput(FluidStack fluid) {
            return inputHatches.stream()
                    .anyMatch(hatch -> hatch.depleteInput(fluid));
        }
    }
}
