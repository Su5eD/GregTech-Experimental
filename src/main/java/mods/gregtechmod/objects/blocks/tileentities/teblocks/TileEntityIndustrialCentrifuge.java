package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import com.google.common.collect.Sets;
import ic2.api.item.IC2Items;
import ic2.core.ContainerBase;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.item.ItemFluidCell;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.inventory.GtFluidTankProcessable;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityGTMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerIndustrialCentrifuge;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class TileEntityIndustrialCentrifuge extends TileEntityGTMachine<IRecipeCellular, IGtRecipeManagerCellular> {

    public InvSlotConsumable cellSlot;
    public Fluids.InternalFluidTank tank;
    private static final Set<EnumFacing> animatedSides = Sets.newHashSet(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP);

    public TileEntityIndustrialCentrifuge() {
        super(10000, 4, 1, 1, GtRecipes.industrialCentrifuge);
        this.cellSlot = new InvSlotConsumable(this, "cellSlot", 1) {
            @Override
            public boolean accepts(ItemStack stack) {
                return (stack.getItem() == ItemName.fluid_cell.getInstance()) && stack.getTagCompound() == null;
            }
        };
        this.tank = this.fluids.addTank(new GtFluidTankProcessable<>(this, "tank", GtRecipes.industrialCentrifuge, InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), 32000));
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if(field.equals("overclockersCount")) rerender();
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance extendedState = super.getExtendedState(state);
        return getActive() ? extendedState.withProperty(PropertyHelper.ANIMATION_SPEED_PROPERTY, new PropertyHelper.AnimationSpeed(animatedSides, GregTechConfig.GENERAL.dynamicCentrifugeAnimationSpeed ? Math.min(this.overclockersCount + 1, 3) : 3)) : extendedState;
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
                        int cellsFromInputSlot = input.getItem() instanceof ItemFluidCell ? Math.min(recipe.getCells(), cells) : 0;
                        this.inputSlot.consume(cells, false, true);
                        this.cellSlot.consume(recipe.getCells() - cellsFromInputSlot);
                        this.maxProgress *= 1.5;
                        addCellsToOutput(StackUtil.copyWithSize(input, cells - recipe.getCells()));
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
            super.consumeInput(recipe, true);
        }
        this.cellSlot.consume(recipe.getCells());
    }

    public static boolean isCell(Item item) {
        String name = item.getRegistryName().toString();
        return item instanceof ItemFluidCell || name.equals("forestry:can") || name.equals("forestry:capsule") || name.equals("forestry:refractory");
    }

    public static boolean isFilledBucket(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof UniversalBucket || (item instanceof ItemBucket && FluidUtil.getFluidContained(stack) != null) || item instanceof ItemBucketMilk;
    }

    public void addCellsToOutput(ItemStack input) {
        Item item = input.getItem();
        if (this.pendingRecipe.size() < 4) {
            if (item instanceof ItemFluidCell) this.pendingRecipe.add(StackUtil.copyWithSize(IC2Items.getItem("fluid_cell"), input.getCount()));
            else this.pendingRecipe.add(StackUtil.copyWithSize(IC2Items.getItem("ingot", "tin"), getTinForCells(input)));
        } else {
            for (ItemStack stack : this.pendingRecipe) {
                if (stack.isItemEqual(IC2Items.getItem("ingot", "tin"))) {
                    stack.grow(getTinForCells(input));
                    return;
                }
            }
        }
    }

    private int getTinForCells(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemFluidCell) return stack.getCount();
        else if (item.getRegistryName().toString().equals("forestry:can")) return stack.getCount() / 4;
        return 0;
    }

    @Override
    public IRecipeCellular getRecipe() {
        ItemStack stack = this.inputSlot.get();
        int cells = this.cellSlot.get().getCount();
        IRecipeCellular recipe = this.recipeManager.getRecipeFor(stack, cells);
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
                        if (stack.getItem() instanceof ItemFluidCell) cells += input.getCount() - this.tank.getFluidAmount() / Fluid.BUCKET_VOLUME;
                    }
                    recipe = this.recipeManager.getRecipeFor(fluidContained, cells);
                }
            }
        }
        return recipe != null && this.outputSlot.canAdd(recipe.getOutput()) ? recipe : null;
    }

    public ContainerBase<TileEntityIndustrialCentrifuge> getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialCentrifuge(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialCentrifuge(new ContainerIndustrialCentrifuge(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
