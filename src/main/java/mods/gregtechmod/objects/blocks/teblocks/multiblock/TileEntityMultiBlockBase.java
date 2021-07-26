package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiMultiblock;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.Maintenance;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMultiblock;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.Structure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TileEntityMultiBlockBase extends TileEntityUpgradable implements IPanelInfoProvider, IHasGui {
    public final Structure<MultiBlockInstance> structure;
    protected final IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager;
    public final InvSlot machinePartSlot;
    
    private int pollution;
    protected int efficiency;
    protected int efficiencyIncrease;
    private int startUpCheck = 100;
    private int runtime;
    public final Maintenance maintenance;
    
    protected double fuelEnergy;
    protected int progress;
    protected int maxProgress;
    protected ItemStack fuelOutput;

    public TileEntityMultiBlockBase(String descriptionKey, IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager) {
        super(descriptionKey);
        this.fuelManager = fuelManager;
        this.maintenance = addComponent(new Maintenance(this));
        Map<Character, Predicate<BlockPos>> elements = new HashMap<>();
        getStructureElements(elements);
        this.structure = new Structure<>(getStructurePattern(), elements, map -> new MultiBlockInstance(this.world, map));
        this.machinePartSlot = new GtSlotFiltered(this, "machine_part", InvSlot.Access.NONE, 1, this::acceptsMachinePart);
    }
    
    protected abstract List<List<String>> getStructurePattern();
        
    protected abstract void getStructureElements(Map<Character, Predicate<BlockPos>> map);

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (tickCounter % 50 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing(), this.world);
        }
        
        if (--startUpCheck >= 0) return;
        Optional<Structure<MultiBlockInstance>.WorldStructure> struct = this.structure.getWorldStructure();
        if (!struct.map(Structure.WorldStructure::isValid).orElse(false)) {
            stopMachine();
            return;
        }
        
        MultiBlockInstance instance = struct.get().getInstance();
        instance.collectMaintenanceStatus(this.maintenance);
        int repairStatus = this.getRepairStatus();
        
        if (isAllowedToWork() && repairStatus > 0) {
            ItemStack machinePart = this.machinePartSlot.get();
            if (this.maxProgress > 0 && doRandomMaintenanceDamage(machinePart)) {
                if (!polluteEnvironment(instance, getPollutionPerTick(machinePart))) {
                    stopMachine();
                } else {
                    setActive(true);
                    onRunningTick(instance);
                    
                    if (++this.progress >= this.maxProgress) {
                        instance.addOutput(this.fuelOutput);
                    
                        this.efficiency = Math.max(0, Math.min(this.efficiency + this.efficiencyIncrease, getMaxEfficiency(machinePart) - (getIdealStatus() - repairStatus) * 1000));
                        this.progress = this.maxProgress = this.efficiencyIncrease = 0;
                        this.fuelOutput = null;
                        checkFuel(instance);
                        if (this.maxProgress <= 0) setActive(false);
                    }
                }
            } else {
                checkFuel(instance);
                this.efficiency = Math.max(0, this.efficiency - 1000);
            }
        } else stopMachine();
    }
    
    private void checkFuel(MultiBlockInstance instance) {
        if (isAllowedToWork()) {
            IFuel<IRecipeIngredient> fuel = getFuel(instance);
            if (fuel != null) {
                this.fuelOutput = getFuelOutput(fuel);
                processFuel(instance, fuel);
            }
        }
    }

    @Override
    protected void updateEntityClient() {
        super.updateEntityClient();
        
        if (tickCounter % 50 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing(), this.world);
        }
    }

    private boolean doRandomMaintenanceDamage(ItemStack machinePart) {
        if (!isCorrectMachinePart()) {
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
                    if (explodesOnComponentBreak(machinePart)) markForExplosion(10);
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
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("maintenance");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("efficiency", this.efficiency);
        nbt.setInteger("efficiencyIncrease", this.efficiencyIncrease);
        nbt.setInteger("pollution", this.pollution);
        nbt.setInteger("runtime", this.runtime);
        this.maintenance.writeToNBT(nbt);
        
        nbt.setDouble("fuelEnergy", this.fuelEnergy);
        nbt.setInteger("progress", this.progress);
        nbt.setInteger("maxProgress", this.maxProgress);
        
        if (this.fuelOutput != null) {
            NBTTagCompound output = new NBTTagCompound();
            this.fuelOutput.writeToNBT(output);
            nbt.setTag("fuelOutput", output);
        }
        
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.efficiency = nbt.getInteger("efficiency");
        this.efficiencyIncrease = nbt.getInteger("efficiencyIncrease");
        this.pollution = nbt.getInteger("pollution");
        this.runtime = nbt.getInteger("runtime");
        this.maintenance.readFromNBT(nbt);
        
        this.fuelEnergy = nbt.getDouble("fuelEnergy");
        this.progress = nbt.getInteger("progress");
        this.maxProgress = nbt.getInteger("maxProgress");
        
        if (nbt.hasKey("fuelOutput")) {
            this.fuelOutput = new ItemStack(nbt.getCompoundTag("fuelOutput"));
        }
    }

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
        return GtUtil.translateGeneric("progress") + ": " + GtUtil.translateGeneric("time_secs", this.progress / 20 + "/" + this.maxProgress / 20);
    }

    @Override
    public String getSecondaryInfo() {
        return GtUtil.translateGeneric("efficiency", this.efficiency / 100F);
    }

    @Override
    public String getTertiaryInfo() {
        int problems = this.getRepairStatus();
        return GtUtil.translateGeneric("problems", getIdealStatus() - problems);
    }

    @Override
    protected int getBaseSinkTier() {
        return 0;
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
        protected final List<TileEntityHatchInput> inputHatches = new ArrayList<>();
        protected final List<TileEntityHatchOutput> outputHatches = new ArrayList<>();
        protected final List<TileEntityHatchDynamo> dynamoHatches = new ArrayList<>();
        protected final List<TileEntityHatchMuffler> mufflerHatches = new ArrayList<>();
        protected final List<TileEntityHatchMaintenance> maintenanceHatches = new ArrayList<>();
        
        protected MultiBlockInstance(IBlockAccess world, Map<BlockPos, IBlockState> states) {
            states.keySet().stream()
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
        
        public List<FluidStack> getInputFluids() {
            return inputHatches.stream()
                    .map(TileEntityHatchInput::getFluid)
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
