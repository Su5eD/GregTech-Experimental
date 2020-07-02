package mods.gregtechmod.common.objects.blocks.machines.tileentity;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.recipe.BasicMachineRecipeManager;
import ic2.core.ref.ItemName;
import ic2.core.util.Util;
import mods.gregtechmod.client.gui.GuiGtCentrifuge;
import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.init.ItemInit;
import mods.gregtechmod.common.inventory.GtFluidTank;
import mods.gregtechmod.common.objects.blocks.machines.container.ContainerGtCentrifuge;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.base.TileEntityGTMachine;
import mods.gregtechmod.common.objects.items.GtUpgradeItem;
import mods.gregtechmod.common.recipe.Recipes;
import mods.gregtechmod.common.util.GtProperties;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class TileEntityGtCentrifuge extends TileEntityGTMachine {

    public InvSlotConsumable cellSlot;
    public Fluids.InternalFluidTank lavaTank;
    private boolean usingLavaRecipe = false;
    private final ArrayList<ItemStack> lavaRecipeList;
    private final Set<EnumFacing> animatedFaces = new HashSet<EnumFacing>() {{
        addAll(Util.horizontalFacings);
        add(EnumFacing.UP);
    }};

    public TileEntityGtCentrifuge() {
        super(1200, 5, (byte)4, (byte)1, 1,Recipes.gtcentrifuge);
        this.cellSlot = new InvSlotConsumable(this, "cellSlot", 1) {
            @Override
            public boolean accepts(ItemStack stack) {
                return (stack.getItem() == ItemName.fluid_cell.getInstance()) && stack.getTagCompound() == null;
            }
        };
        this.lavaTank = this.fluids.addTank(new GtFluidTank(this, "lavaTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), Fluids.fluidPredicate(FluidRegistry.LAVA), 32000));
        this.lavaRecipeList = new ArrayList<ItemStack>() {{
            add(IC2Items.getItem("ingot", "tin"));
            add(new ItemStack(ItemInit.ITEMS.get("ingot_electrum"), 2));
        }};
    }

    public static void init() {
        Recipes.gtcentrifuge = new BasicMachineRecipeManager();
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance aState = super.getExtendedState(state);
        return getActive() ? aState.withProperty(GtProperties.ANIMATION_SPEED_PROPERTY, new GtProperties.AnimationSpeed(animatedFaces, ConfigLoader.dynamicCentrifugeAnimationSpeed ? Math.min(this.overclockersCount + 1, 3) : 3)) : aState;
    }

    @Override
    public void consumeInput(MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result) {
        if (this.usingLavaRecipe) this.lavaTank.drainInternal(16000, true);
        else if(result.getOutput().toString().contains("cell")) {
            this.cellSlot.consume(1);
            super.consumeInput(result);
        }
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
        return (new MachineRecipe(null, lavaRecipeList, meta).getResult(null));
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


    public ContainerBase<TileEntityGtCentrifuge> getGuiContainer(EntityPlayer player) {
        return new ContainerGtCentrifuge(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiGtCentrifuge(new ContainerGtCentrifuge(player, this));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    @Override
    public boolean shouldExplode() {
        return true;
    }

    @Override
    public float getExplosionPower(int i, float v) {
        return 2.5F;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer);
    }

    @Override
    public Set<GtUpgradeItem.GtUpgradeType> getCompatibleGtUpgrades() {
        return EnumSet.allOf(GtUpgradeItem.GtUpgradeType.class);
    }
}
