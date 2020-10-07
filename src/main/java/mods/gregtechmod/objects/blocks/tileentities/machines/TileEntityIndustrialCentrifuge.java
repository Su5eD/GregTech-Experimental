package mods.gregtechmod.objects.blocks.tileentities.machines;

import com.google.common.collect.Sets;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.MachineRecipeResult;
import ic2.core.ContainerBase;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.recipe.BasicMachineRecipeManager;
import ic2.core.ref.ItemName;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.recipe.Recipes;
import mods.gregtechmod.core.ConfigLoader;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.objects.blocks.tileentities.machines.base.TileEntityGTMachine;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerIndustrialCentrifuge;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TileEntityIndustrialCentrifuge extends TileEntityGTMachine {

    public InvSlotConsumable cellSlot;
    public Fluids.InternalFluidTank lavaTank;
    private boolean usingLavaRecipe = false;
    private static final List<ItemStack> lavaRecipeList = new ArrayList<>();
    private static final Set<EnumFacing> animatedSides = Sets.newHashSet(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP);

    public TileEntityIndustrialCentrifuge() {
        super(1200, 5, (byte)4, (byte)1, 1,Recipes.industrial_centrifuge);
        this.cellSlot = new InvSlotConsumable(this, "cellSlot", 1) {
            @Override
            public boolean accepts(ItemStack stack) {
                return (stack.getItem() == ItemName.fluid_cell.getInstance()) && stack.getTagCompound() == null;
            }
        };
        this.lavaTank = this.fluids.addTank(new GtFluidTank(this, "lavaTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), Fluids.fluidPredicate(FluidRegistry.LAVA), 32000));
    }

    public static void init() {
        Recipes.industrial_centrifuge = new BasicMachineRecipeManager();
        lavaRecipeList.add(IC2Items.getItem("ingot", "tin"));
        lavaRecipeList.add(new ItemStack(BlockItems.Ingots.electrum.getInstance(), 2));
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if(field.equals("overclockersCount")) rerender();
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance aState = super.getExtendedState(state);
        return getActive() ? aState.withProperty(PropertyHelper.ANIMATION_SPEED_PROPERTY, new PropertyHelper.AnimationSpeed(animatedSides, ConfigLoader.dynamicCentrifugeAnimationSpeed ? Math.min(this.overclockersCount + 1, 3) : 3)) : aState;
    }

    @Override
    public void consumeInput(MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result) {
        if (this.usingLavaRecipe) {
            this.lavaTank.drainInternal(16000, true);
            return;
        }
        else if(result.getOutput().toString().contains("cell")) {
            this.cellSlot.consume(1);
        }
        super.consumeInput(result);
    }

    @Override
    public MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> getOutput() {
        this.usingLavaRecipe = false;
        if(this.getActive() && (!this.inputSlot.isEmpty() && this.lavaTank.getFluidAmount() >= 16000)) {
            return getLavaRecipe();
        }
        MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> output;
        if(this.inputSlot.isEmpty() && this.lavaTank.getFluidAmount() < 16000) return null;
        else if (this.inputSlot.process() != null) {
            output = this.inputSlot.process();
            if (output.getOutput().toString().contains("cell") && this.cellSlot.isEmpty()) return null;
            return output;
        }
        if((this.inputSlot.isEmpty() && this.outputSlot.canAdd(lavaRecipeList))) {
            if (this.lavaTank.getFluid().getFluid() == FluidRegistry.LAVA && this.lavaTank.getFluidAmount() >= 16000) {
                this.usingLavaRecipe = true;
                return getLavaRecipe();
            }
        }
        return null;
    }

    private MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> getLavaRecipe() {
        NBTTagCompound meta = new NBTTagCompound();
        meta.setInteger("duration", 200);
        return new MachineRecipe<IRecipeInput, Collection<ItemStack>>(null, lavaRecipeList, meta).getResult(null);
    }

    protected void onLoaded() {
        super.onLoaded();
    }

    public String getStartSoundFile() {
        return "Machines/ExtractorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


    public ContainerBase<TileEntityIndustrialCentrifuge> getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialCentrifuge(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialCentrifuge(new ContainerIndustrialCentrifuge(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
