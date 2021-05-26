package mods.gregtechmod.objects.blocks.teblocks.base;

import com.google.common.collect.Sets;
import ic2.api.item.IC2Items;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.item.ItemClassicCell;
import ic2.core.item.ItemFluidCell;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.GtFluidTankProcessable;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Set;

public abstract class TileEntityIndustrialCentrifugeBase extends TileEntityGTMachine<IRecipeCellular, IRecipeIngredient, ItemStack, IGtRecipeManagerCellular> {
    public InvSlotConsumable cellSlot;
    public Fluids.InternalFluidTank tank;
    private static final Set<EnumFacing> animatedSides = Sets.newHashSet(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP);
    private final boolean dynamicAnimationSpeed;

    protected TileEntityIndustrialCentrifugeBase(String descriptionKey, int defaultTier, int tankCapacity, IGtRecipeManagerCellular recipeManager, boolean dynamicAnimationSpeed) {
        super(descriptionKey, 10000, 4, defaultTier, recipeManager);
        this.dynamicAnimationSpeed = dynamicAnimationSpeed;
        this.cellSlot = new InvSlotConsumable(this, "cellSlot", 1) {
            @Override
            public boolean accepts(ItemStack stack) {
                return GregTechMod.classic && stack.isItemEqual(ModHandler.emptyFuelCan) || stack.isItemEqual(ModHandler.emptyCell) && stack.getTagCompound() == null;
            }
        };
        this.tank = this.fluids.addTank(new GtFluidTankProcessable<>(this, "tank", GtRecipes.industrialCentrifuge, InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), tankCapacity));
    }

    @Override
    public void consumeInput(IRecipeCellular recipe, boolean consumeContainers) {
        this.inputSlot.consume(recipe == null ? 1 : recipe.getInput().getCount(), consumeContainers);
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if(field.equals("overclockersCount")) rerender();
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance extendedState = super.getExtendedState(state);
        return this.dynamicAnimationSpeed && getActive() ? extendedState.withProperty(PropertyHelper.ANIMATION_SPEED_PROPERTY, new PropertyHelper.AnimationSpeed(animatedSides, GregTechConfig.GENERAL.dynamicCentrifugeAnimationSpeed ? Math.min(this.overclockersCount + 1, 3) : 3)) : extendedState;
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
                        Item item = input.getItem();
                        int cellsFromInputSlot = item instanceof ItemFluidCell || item instanceof ItemClassicCell || item instanceof ItemCellClassic ? Math.min(recipe.getCells(), cells) : 0;
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

    public static boolean isCell(Item item) {
        return item instanceof ItemFluidCell || item instanceof ItemClassicCell || item instanceof ItemCellClassic || StackUtil.checkItemEquality(ModHandler.can, item) || StackUtil.checkItemEquality(ModHandler.waxCapsule, item) || StackUtil.checkItemEquality(ModHandler.refractoryCapsule, item);
    }

    public static boolean isFilledBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof UniversalBucket || item instanceof ItemBucket && FluidUtil.getFluidContained(stack) != null || item instanceof ItemBucketMilk;
    }

    public static CellAdditionResult addCellsToOutput(ItemStack input, List<ItemStack> output) {
        Item item = input.getItem();
        if (output.size() < 4) {
            output.add(new ItemStack(item, input.getCount()));
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
        if (item instanceof ItemFluidCell || item instanceof ItemClassicCell || item instanceof ItemCellClassic) return stack.getCount();
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
                        if (item instanceof ItemFluidCell || item instanceof ItemClassicCell || item instanceof ItemCellClassic) cells += input.getCount() - this.tank.getFluidAmount() / Fluid.BUCKET_VOLUME;
                    }
                    recipe = this.recipeManager.getRecipeFor(fluidContained, cells);
                }
            }
        }
        return recipe;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public enum CellAdditionResult {
        ADD,
        DISSOLVE,
        FAIL
    }
}
