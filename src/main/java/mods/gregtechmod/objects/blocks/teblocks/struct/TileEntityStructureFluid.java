package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableSecondary;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class TileEntityStructureFluid<T, R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, RM extends IGtRecipeManagerSecondaryFluid<R>> extends TileEntityStructureBase<T, R, List<IRecipeIngredient>, List<ItemStack>, RM> {
    public final GtSlotProcessableSecondary<RM, List<ItemStack>> secondaryInput;
    public final InvSlotOutput fluidContainerOutput;
    public final Fluids.InternalFluidTank waterTank;
    
    private ItemStack pendingFluidContainer;

    public TileEntityStructureFluid(String descriptionKey, int outputSlots, RM recipeManager) {
        super(descriptionKey, outputSlots, recipeManager);
        
        this.secondaryInput = new GtSlotProcessableSecondary<>(this, "secondary_input", 1, recipeManager);
        this.fluidContainerOutput = new InvSlotOutput(this, "fluid_output", 1);
        this.waterTank = this.fluids.addTank(new GtFluidTank(this, "water_tank", Util.allFacings, Collections.emptySet(), fluid -> fluid == FluidRegistry.WATER, 10000));
    }

    @Override
    public void consumeInput(R recipe, boolean consumeContainers) {
        List<IRecipeIngredient> inputs = recipe.getInput();
        this.inputSlot.consume(inputs.get(0).getCount());

        IRecipeIngredientFluid fluid = (IRecipeIngredientFluid) inputs.get(1);
        int count = fluid.getCount();
        int mb = count * Fluid.BUCKET_VOLUME;
        if (fluid.apply(FluidRegistry.WATER) && this.waterTank.getFluidAmount() >= mb) {
            this.waterTank.drainInternal(mb, true);
        } else {
            ItemStack container = this.secondaryInput.consume(count, true);
            this.pendingFluidContainer = StackUtil.copyWithSize(FluidUtil.tryEmptyContainer(container, GtUtil.VOID_TANK, mb, null, true).result, container.getCount());
        }
    }
    
    @Override
    public R getRecipe() {
        ItemStack input = this.inputSlot.get();

        return this.recipeManager.getRecipeFor(Arrays.asList(input, this.secondaryInput.get()), this.waterTank.getFluid());
    }

    @Override
    public void addOutput(List<ItemStack> output) {
        super.addOutput(output);
        
        if (this.pendingFluidContainer != null) {
            this.fluidContainerOutput.add(this.pendingFluidContainer);
            this.pendingFluidContainer = null;
        }
    }

    @Override
    public double getGuiValue(String name) {
        if (name.equals("water_level")) return (double) this.waterTank.getFluidAmount() / this.waterTank.getCapacity();

        return super.getGuiValue(name);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.pendingFluidContainer != null) {
            NBTTagCompound stack = new NBTTagCompound();
            this.pendingFluidContainer.writeToNBT(stack);
            nbt.setTag("pendingFluidContainer", stack);
        }
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (nbt.hasKey("pendingFluidContainer")) this.pendingFluidContainer = new ItemStack(nbt.getCompoundTag("pendingFluidContainer"));
    }
}
