package mods.gregtechmod.objects.blocks.teblocks.generator;

import ic2.core.ContainerBase;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiThermalGenerator;
import mods.gregtechmod.inventory.GtSlotProcessableFuel;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerThermalGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class TileEntityThermalGenerator extends TileEntityUpgradable {
    protected final IFuelManagerFluid<IFuel<IRecipeIngredient, List<ItemStack>>> fuelManager;

    public BasicTank tank;
    private double solidFuelEnergy;
    
    public TileEntityThermalGenerator() {
        super("thermal_generator");
        this.displayEnergyCapacity = true;
        this.fuelManager = GtFuels.hot;

        this.tank = addComponent(new BasicTank(this, 10000, this.fluids, tank -> new GtSlotProcessableFuel(this, "tankInputSlot", this.fuelManager)));
        
        this.allowedCovers = EnumSet.of(CoverType.GENERIC, CoverType.IO, CoverType.CONTROLLER, CoverType.METER);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (this.tickCounter % 10 == 0) {
            IFuel<IRecipeIngredient, List<ItemStack>> fuel = getFuel();
            if (canOperate(fuel)) {
                processInput(fuel);
                double energy = getFuelValue(fuel.getEnergy());
                
                setActive(true);
                while(canAddEnergy()) {
                    if (this.solidFuelEnergy >= energy) {
                        this.solidFuelEnergy -= energy;
                    } else if (this.tank.content.getFluidAmount() > 0) {
                        this.tank.content.drainInternal(1, true);
                    } else break;
                    
                    addEnergy(energy);
                }
            } else setActive(false);
        }
    }
    
    private double getFuelValue(double energy) {
        return energy * 4 / 5;
    }
    
    private boolean canAddEnergy() {
        return getStoredEU() < getMaxOutputEUp() * 10 + 512;
    }
    
    protected boolean canOperate(IFuel<IRecipeIngredient, List<ItemStack>> fuel) {
        return this.isAllowedToWork() && fuel != null;
    }

    protected IFuel<IRecipeIngredient, List<ItemStack>> getFuel() {
        FluidStack fluid = this.tank.content.getFluid();
        if (fluid != null) return this.fuelManager.getFuel(fluid.getFluid());
        
        ItemStack input = this.tank.inputSlot.get();
        return !input.isEmpty() ? this.fuelManager.getFuel(input) : null;
    }
    
    protected void processInput(IFuel<IRecipeIngredient, List<ItemStack>> fuel) {
        ItemStack stack = this.tank.inputSlot.get();
        IRecipeIngredient input = fuel.getInput();
        if (!(input instanceof IRecipeIngredientFluid) && input.apply(stack)) {
            this.solidFuelEnergy = getFuelValue(fuel.getEnergy()) * Fluid.BUCKET_VOLUME;
            this.tank.inputSlot.consume(input.getCount());
            this.tank.outputSlot.add(fuel.getOutput());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setDouble("solidFuelEnergy", this.solidFuelEnergy);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.solidFuelEnergy = nbt.getDouble("solidFuelEnergy");
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerThermalGenerator(entityPlayer, this);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
        return new GuiThermalGenerator(new ContainerThermalGenerator(entityPlayer, this), this.tank.content);
    }

    public double getSolidFuelEnergy() {
        return this.solidFuelEnergy;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Collections.emptySet();
    }
    
    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return Util.allFacings;
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return AdjustableEnergy.createSource(this, 1000000, 1, 24, getSourceSides());
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
    public void markForExplosion() {}
}
