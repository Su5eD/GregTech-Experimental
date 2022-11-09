package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.inventory.invslot.GtConsumableCell;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.GtUtil.CellAdditionResult;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class TileEntityIndustrialCentrifugeBase extends TileEntityGTMachine<IRecipeCellular, IRecipeIngredient, ItemStack, IGtRecipeManagerCellular> {
    public GtConsumableCell cellSlot;
    public Fluids.InternalFluidTank tank;

    protected TileEntityIndustrialCentrifugeBase(int tankCapacity, IGtRecipeManagerCellular recipeManager) {
        super(4, recipeManager);
        this.cellSlot = new GtConsumableCell(this, "cellSlot", 1);
        this.tank = this.fluids.addTank(new GtFluidTank(this, "tank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), GtRecipes.industrialCentrifuge::hasRecipeFor, tankCapacity));
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected void consumeInput(IRecipeCellular recipe, boolean consumeContainers) {
        this.inputSlot.consume(recipe == null ? 1 : recipe.getInput().getCount(), consumeContainers);
    }

    @Override
    protected void onUpdateIC2Upgrade(IC2UpgradeType type) {
        if (type == IC2UpgradeType.OVERCLOCKER) {
            updateRender();
        }
    }

    @Override
    public void consumeInput(IRecipeCellular recipe) {
        IRecipeIngredient ingredient = recipe.getInput();
        if (ingredient instanceof IRecipeIngredientFluid) {
            ItemStack input = this.inputSlot.get();
            int mb = ((IRecipeIngredientFluid) ingredient).getMilliBuckets();
            FluidStack fluid = FluidUtil.getFluidContained(input);
            if (fluid != null) {
                FluidStack fluidInTank = this.tank.getFluid();
                if (GtUtil.isCell(input.getItem()) || GtUtil.isFilledBucket(input)) {
                    int cells = mb / Fluid.BUCKET_VOLUME;
                    if (fluidInTank != null && fluidInTank.isFluidEqual(fluid)) {
                        int drain = cells - input.getCount();
                        if (drain > 0) {
                            cells -= drain;
                            this.tank.drainInternal(drain * Fluid.BUCKET_VOLUME, true);
                        }
                    }
                    if (GtUtil.isFilledBucket(input)) {
                        this.inputSlot.put(new ItemStack(Items.BUCKET));
                    }
                    else if (cells > 0) {
                        int cellsFromInputSlot = GtUtil.isIC2Cell(input.getItem()) ? Math.min(recipe.getCells(), cells) : 0;
                        this.inputSlot.consume(cells, false, true);
                        this.cellSlot.consume(recipe.getCells() - cellsFromInputSlot);
                        if (GtUtil.addCellsToOutput(ItemHandlerHelper.copyStackWithSize(input, cells - cellsFromInputSlot), this.pendingRecipe) == CellAdditionResult.DISSOLVE) {
                            this.maxProgress *= 1.5;
                        }
                        return;
                    }
                }
                else if (input.getCount() == 1) {
                    IFluidHandler handler = FluidUtil.getFluidHandler(input);
                    if (handler != null) {
                        int drain = mb;
                        if (fluidInTank != null && fluidInTank.isFluidEqual(fluid)) {
                            int diff = drain - fluid.amount;
                            if (diff >= 1000) {
                                drain -= diff;
                                this.tank.drainInternal(diff, true);
                            }
                        }
                        handler.drain(drain, true);
                    }
                }
            }
            else this.tank.drainInternal(mb, true);
        }
        else {
            this.consumeInput(recipe, true);
        }
        this.cellSlot.consume(recipe.getCells());
    }

    @Override
    public IRecipeCellular getRecipe() {
        ItemStack stack = this.inputSlot.get();
        ItemStack cell = this.cellSlot.get();
        int cells = cell.getCount();
        IRecipeCellular recipe = this.recipeManager.getRecipeFor(stack, cell);
        if (recipe == null) {
            FluidStack fluidInTank = this.tank.getFluid();
            recipe = this.recipeManager.getRecipeFor(fluidInTank, cells);

            if (recipe == null) {
                FluidStack fluidContained = FluidUtil.getFluidContained(stack);
                if (fluidContained != null && fluidContained.isFluidEqual(fluidInTank)) {
                    fluidContained.amount = fluidContained.amount * stack.getCount() + this.tank.getFluidAmount();
                    IRecipeCellular availableRecipe = this.recipeManager.getRecipeFor(fluidContained);
                    if (availableRecipe != null) {
                        IRecipeIngredient input = availableRecipe.getInput();
                        Item item = stack.getItem();
                        if (GtUtil.isIC2Cell(item)) cells += input.getCount() - this.tank.getFluidAmount() / Fluid.BUCKET_VOLUME;
                    }
                    recipe = this.recipeManager.getRecipeFor(fluidContained, cells);
                }
            }
        }
        return recipe;
    }
}
