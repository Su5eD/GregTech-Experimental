package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.IHasGui;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableFuel;
import mods.gregtechmod.inventory.tank.GtFluidTankProcessableFuel;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFluidGenerator;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileEntityFluidGenerator extends TileEntityGenerator implements IHasGui {
    protected final IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager;
    
    public BasicTank tank;
    @NBTPersistent
    private double fuelEnergy;
    @NBTPersistent
    private double solidFuelEnergy;

    protected TileEntityFluidGenerator(IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager) {
        this.fuelManager = fuelManager;
        this.tank = addComponent(new BasicTank(this, this.fluids, new GtFluidTankProcessableFuel<>(this, "content", fuelManager, 10000), tank -> new GtSlotProcessableFuel(this, "tankInputSlot", this.fuelManager), true));
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

    public double getSolidFuelEnergy() {
        return this.solidFuelEnergy;
    }

    @Override
    public ContainerFluidGenerator getGuiContainer(EntityPlayer player) {
        return new ContainerFluidGenerator(player, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
