package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.api.item.IC2Items;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.ItemClassicCell;
import ic2.core.item.ItemFluidCell;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.inventory.invslot.GtConsumableCell;
import mods.gregtechmod.inventory.tank.GtFluidTankProcessable;
import mods.gregtechmod.objects.items.ItemCellClassic;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public abstract class TileEntityIndustrialCentrifugeBase extends TileEntityGTMachine<IRecipeCellular, IRecipeIngredient, ItemStack, IGtRecipeManagerCellular> {
    public GtConsumableCell cellSlot;
    public Fluids.InternalFluidTank tank;

    protected TileEntityIndustrialCentrifugeBase(int tankCapacity, IGtRecipeManagerCellular recipeManager) {
        super(4, recipeManager);
        this.cellSlot = new GtConsumableCell(this, "cellSlot", 1);
        this.tank = this.fluids.addTank(new GtFluidTankProcessable<>(this, "tank", GtRecipes.industrialCentrifuge, InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), tankCapacity));
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public void consumeInput(IRecipeCellular recipe, boolean consumeContainers) {
        this.inputSlot.consume(recipe == null ? 1 : recipe.getInput().getCount(), consumeContainers);
    }

    @Override
    protected void onUpdateIC2Upgrade(IC2UpgradeType type, ItemStack stack) {
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
                if (isCell(input.getItem()) || isFilledBucket(input)) {
                    int cells = mb / Fluid.BUCKET_VOLUME;
                    if (fluidInTank != null && fluidInTank.isFluidEqual(fluid)) {
                        int drain = cells - input.getCount();
                        if (drain > 0) {
                            cells -= drain;
                            this.tank.drainInternal(drain * Fluid.BUCKET_VOLUME, true);
                        }
                    }
                    if (isFilledBucket(input)) {
                        this.inputSlot.put(new ItemStack(Items.BUCKET));
                    } else if (cells > 0) {
                        int cellsFromInputSlot = isIC2Cell(input.getItem()) ? Math.min(recipe.getCells(), cells) : 0;
                        this.inputSlot.consume(cells, false, true);
                        this.cellSlot.consume(recipe.getCells() - cellsFromInputSlot);
                        if (addCellsToOutput(StackUtil.copyWithSize(input, cells - cellsFromInputSlot), this.pendingRecipe) == CellAdditionResult.DISSOLVE) this.maxProgress *= 1.5;
                        return;
                    }
                } else if (input.getCount() == 1) {
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
            } else this.tank.drainInternal(mb, true);
        } else {
            this.consumeInput(recipe, true);
        }
        this.cellSlot.consume(recipe.getCells());
    }
    
    public static boolean isIC2Cell(Item item) {
        return item instanceof ItemFluidCell || item instanceof ItemClassicCell || item instanceof ItemCellClassic;
    }

    public static boolean isCell(Item item) { // TODO Move to utils
        return isIC2Cell(item) || StackUtil.checkItemEquality(ModHandler.can, item) || StackUtil.checkItemEquality(ModHandler.waxCapsule, item) || StackUtil.checkItemEquality(ModHandler.refractoryCapsule, item);
    }

    public static boolean isFilledBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof UniversalBucket || item instanceof ItemBucket && FluidUtil.getFluidContained(stack) != null || item instanceof ItemBucketMilk;
    }

    public static CellAdditionResult addCellsToOutput(ItemStack input, List<ItemStack> output) {
        if (output.size() < 4) {
            Item item = input.getItem();
            output.add(new ItemStack(item instanceof ItemCellClassic ? ModHandler.emptyCell.getItem() : item, input.getCount()));
            return CellAdditionResult.ADD;
        } else {
            for (ItemStack stack : output) {
                if (stack.isItemEqual(IC2Items.getItem("ingot", "tin"))) {
                    stack.grow(getTinForCells(input));
                    return CellAdditionResult.DISSOLVE;
                }
            }
        }

        return CellAdditionResult.FAIL;
    }

    private static int getTinForCells(ItemStack stack) {
        Item item = stack.getItem();
        if (isIC2Cell(item)) return stack.getCount();
        else if (StackUtil.checkItemEquality(ModHandler.can, item)) return stack.getCount() / 4;
        return 0;
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
                        if (isIC2Cell(item)) cells += input.getCount() - this.tank.getFluidAmount() / Fluid.BUCKET_VOLUME;
                    }
                    recipe = this.recipeManager.getRecipeFor(fluidContained, cells);
                }
            }
        }
        return recipe;
    }

    public enum CellAdditionResult {
        ADD,
        DISSOLVE,
        FAIL
    }
}
