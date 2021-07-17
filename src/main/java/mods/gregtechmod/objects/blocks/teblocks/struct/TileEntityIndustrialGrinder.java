package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.ContainerBase;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiIndustrialGrinder;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableGrinder;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialGrinder;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityIndustrialGrinder extends TileEntityStructureBase<Object, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    public final GtSlotProcessableGrinder secondaryInput;
    public final InvSlotOutput fluidContainerOutput;
    public final Fluids.InternalFluidTank waterTank;
    
    private ItemStack pendingFluidContainer;
    
    public TileEntityIndustrialGrinder() {
        super("industrial_grinder", 3, GtRecipes.industrialGrinder);
        
        this.secondaryInput = new GtSlotProcessableGrinder(this, "secondary_input", 1);
        this.fluidContainerOutput = new InvSlotOutput(this, "fluid_output", 1);
        this.waterTank = this.fluids.addTank(new GtFluidTank(this, "water_tank", Util.allFacings, Collections.emptySet(), fluid -> fluid == FluidRegistry.WATER, 10000));
    }

    @Override
    protected int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
                Arrays.asList(
                        "SSS",
                        "SSS",
                        "SSS",
                        "   "
                ),
                Arrays.asList(
                        "RRR",
                        "RWR",
                        "RRR",
                        " X "
                ),
                Arrays.asList(
                        "SSS",
                        "SSS",
                        "SSS",
                        "   "
                )
        );
    }

    @Override
    protected void getStructureElements(Map<Character, Predicate<IBlockState>> map) {
        map.put('S', state -> state.getBlock() == BlockItems.Block.STANDARD_MACHINE_CASING.getInstance());
        map.put('R', state -> state.getBlock() == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance());
        map.put('W', state -> state.getBlock() == Blocks.WATER);
    }

    @Override
    public void consumeInput(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        List<IRecipeIngredient> inputs = recipe.getInput();
        this.inputSlot.consume(inputs.get(0).getCount());
        
        IRecipeIngredientFluid fluid = (IRecipeIngredientFluid) inputs.get(1);
        int count = fluid.getCount();
        int mb = count * Fluid.BUCKET_VOLUME;
        if (fluid.apply(FluidRegistry.WATER) && this.waterTank.getFluidAmount() >= mb) {
            this.waterTank.drainInternal(mb, true);
        }
        else {
            ItemStack container = this.secondaryInput.consume(count, true);
            this.pendingFluidContainer = StackUtil.copyWithSize(FluidUtil.tryEmptyContainer(container, GtUtil.VOID_TANK, mb, null, true).result, container.getCount());
        }
    }
    
    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> getRecipe() {
        ItemStack input = this.inputSlot.get();
        IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe = getFluidRecipe(input, this.waterTank.getFluid());
        if (recipe != null) return recipe;
        
        return this.recipeManager.getRecipeFor(Arrays.asList(input, this.secondaryInput.get()));
    }
    
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> getFluidRecipe(ItemStack stack, FluidStack fluid) {
        return this.recipeManager.getRecipes().stream()
                .filter(recipe -> {
                    List<IRecipeIngredient> inputs = recipe.getInput();
                    return inputs.get(0).apply(stack) && ((IRecipeIngredientFluid) inputs.get(1)).apply(fluid);
                })
                .findAny()
                .orElse(null);
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
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialGrinder(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialGrinder(new ContainerIndustrialGrinder(player, this));
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
