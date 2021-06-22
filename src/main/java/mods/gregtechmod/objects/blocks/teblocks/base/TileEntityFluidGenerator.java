package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.inventory.GtSlotProcessableFuel;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFluidGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileEntityFluidGenerator extends TileEntityGenerator implements IHasGui {
    protected final IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager;
    
    public BasicTank tank;
    private double fuelEnergy;
    private double solidFuelEnergy;

    protected TileEntityFluidGenerator(String descriptionKey, IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager) {
        super(descriptionKey);
        this.fuelManager = fuelManager;
        this.tank = addComponent(new BasicTank(this, 10000, this.fluids, tank -> new GtSlotProcessableFuel(this, "tankInputSlot", this.fuelManager)));
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (this.tickCounter % 10 == 0) {
            IFuel<IRecipeIngredient> fuel = getFuel();
            if (canOperate(fuel)) {
                if (fuel != null) {
                    processInput(fuel);
                    this.fuelEnergy = getFuelValue(fuel.getEnergy());
                }

                setActive(true);
                while (canAddEnergy()) {
                    if (this.solidFuelEnergy >= this.fuelEnergy) {
                        this.solidFuelEnergy -= this.fuelEnergy;
                    } else if (this.tank.content.getFluidAmount() > 0) {
                        this.tank.content.drainInternal(1, true);
                    } else break;

                    addEnergy(this.fuelEnergy);
                }
            } else setActive(false);
        }
    }

    protected double getFuelValue(double energy) {
        return energy;
    }

    private boolean canAddEnergy() {
        return getStoredEU() < getMaxOutputEUt() * 10 + 512;
    }

    protected boolean canOperate(IFuel<IRecipeIngredient> fuel) {
        return this.isAllowedToWork() && fuel != null || this.solidFuelEnergy > 0;
    }

    protected IFuel<IRecipeIngredient> getFuel() {
        FluidStack fluid = this.tank.content.getFluid();
        if (fluid != null) return this.fuelManager.getFuel(fluid.getFluid());

        ItemStack input = this.tank.inputSlot.get();
        return !input.isEmpty() ? this.fuelManager.getFuel(input) : null;
    }

    protected void processInput(IFuel<IRecipeIngredient> fuel) {
        ItemStack stack = this.tank.inputSlot.get();
        IRecipeIngredient input = fuel.getInput();
        if (!(input instanceof IRecipeIngredientFluid) && input.apply(stack)) {
            this.solidFuelEnergy = getFuelValue(fuel.getEnergy()) * Fluid.BUCKET_VOLUME;
            this.tank.inputSlot.consume(input.getCount(), false, true);
            this.tank.outputSlot.add(fuel.getOutput());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setDouble("solidFuelEnergy", this.solidFuelEnergy);
        nbt.setDouble("fuelEnergy", this.fuelEnergy);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.solidFuelEnergy = nbt.getDouble("solidFuelEnergy");
        this.fuelEnergy = nbt.getDouble("fuelEnergy");
    }

    public double getSolidFuelEnergy() {
        return this.solidFuelEnergy;
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return AdjustableEnergy.createSource(this, 1000000, 1, 24, getSourceSides());
    }
    
    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerFluidGenerator(player, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
