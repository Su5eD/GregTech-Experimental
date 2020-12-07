package mods.gregtechmod.objects.blocks.tileentities.machines;

import com.google.common.collect.Sets;
import ic2.api.item.IC2Items;
import ic2.core.ContainerBase;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.ref.ItemName;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.objects.blocks.tileentities.machines.base.TileEntityGTMachine;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerIndustrialCentrifuge;
import mods.gregtechmod.recipe.RecipeCentrifuge;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TileEntityIndustrialCentrifuge extends TileEntityGTMachine<IRecipeCentrifuge, Integer> {

    public InvSlotConsumable cellSlot;
    public Fluids.InternalFluidTank lavaTank;
    private boolean usingLavaRecipe = false;
    private static IRecipeCentrifuge lavaRecipe;
    private static final Set<EnumFacing> animatedSides = Sets.newHashSet(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP);

    public TileEntityIndustrialCentrifuge() {
        super(1200, 5, (byte)4, (byte)1, 1, GtRecipes.industrial_centrifuge);
        this.cellSlot = new InvSlotConsumable(this, "cellSlot", 1) {
            @Override
            public boolean accepts(ItemStack stack) {
                return (stack.getItem() == ItemName.fluid_cell.getInstance()) && stack.getTagCompound() == null;
            }
        };
        this.lavaTank = this.fluids.addTank(new GtFluidTank(this, "lavaTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), Fluids.fluidPredicate(FluidRegistry.LAVA), 32000));
    }

    public static void init() {
        List<ItemStack> lavaRecipeOutput = Arrays.asList(IC2Items.getItem("ingot", "tin"),
                new ItemStack(BlockItems.Ingots.electrum.getInstance(), 2));
        lavaRecipe = new RecipeCentrifuge(null, lavaRecipeOutput, 200, 0);
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if(field.equals("overclockersCount")) rerender();
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance aState = super.getExtendedState(state);
        return getActive() ? aState.withProperty(PropertyHelper.ANIMATION_SPEED_PROPERTY, new PropertyHelper.AnimationSpeed(animatedSides, GregTechConfig.GENERAL.dynamicCentrifugeAnimationSpeed ? Math.min(this.overclockersCount + 1, 3) : 3)) : aState;
    }

    @Override
    public void consumeInput(IRecipeCentrifuge recipe) {
        if (this.usingLavaRecipe) {
            this.lavaTank.drainInternal(16000, true);
            return;
        }
        this.cellSlot.consume(recipe.getCells());
        super.consumeInput(recipe);
    }

    @Override
    public IRecipeCentrifuge getOutput() {
        this.usingLavaRecipe = false;
        if(this.getActive() && (!this.inputSlot.isEmpty() && this.lavaTank.getFluidAmount() >= 16000)) {
            return lavaRecipe;
        }
        IRecipeCentrifuge recipe;
        if(this.inputSlot.isEmpty() && this.lavaTank.getFluidAmount() < 16000) return null;
        else if ((recipe = this.inputSlot.process(Collections.singletonMap("cells", this.cellSlot.get().getCount()))) != null) return recipe;

        if((this.inputSlot.isEmpty() && this.outputSlot.canAdd(lavaRecipe.getOutput()))) {
            if (this.lavaTank.getFluid().getFluid() == FluidRegistry.LAVA && this.lavaTank.getFluidAmount() >= 16000) {
                this.usingLavaRecipe = true;
                return lavaRecipe;
            }
        }
        return null;
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
