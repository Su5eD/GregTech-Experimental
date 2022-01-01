package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TileEntityBasicMachine<R extends IMachineRecipe<RI, List<ItemStack>>, RI, I, RM extends IGtRecipeManagerBasic<RI, I, R>> extends TileEntityGTMachine<R, RI, I, RM> {
    @NBTPersistent
    public EnumFacing outputSide = EnumFacing.SOUTH;
    public final InvSlotOutput queueOutputSlot;
    public final GtSlotProcessableItemStack<RM, I> queueInputSlot;
    public final InvSlot extraSlot;
    protected boolean outputBlocked;

    @NBTPersistent
    public boolean provideEnergy;
    @NBTPersistent
    public boolean autoOutput = true;
    @NBTPersistent
    public boolean splitInput;

    public TileEntityBasicMachine(RM recipeManager) {
        this(recipeManager, false);
    }

    public TileEntityBasicMachine(RM recipeManager, boolean wildcardInput) {
        super(1, recipeManager, wildcardInput);
        this.extraSlot = getExtraSlot();
        this.queueInputSlot = getInputSlot("queueInput", GtUtil.INV_SIDE_VERTICAL, wildcardInput);
        this.queueOutputSlot = getOutputSlot("queueOutput", 1);
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }
    
    @Override
    public int getBaseSourceTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 2000;
    }

    @Override
    protected int getMinimumStoredEU() {
        return 1000;
    }

    protected InvSlot getExtraSlot() {
        InvSlotDischarge slot = new InvSlotDischarge(this, InvSlot.Access.IO, 1, false, InvSlot.InvSide.NOTSIDE);
        addDischargingSlot(slot);
        return slot;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        EnumFacing facing = getFacing();
        return Arrays.stream(EnumFacing.VALUES)
                .filter(side -> side != facing)
                .collect(Collectors.toSet());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.provideEnergy ? Collections.singleton(this.outputSide) : Util.noFacings;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        updateRender();
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.outputSide = getFacing().getOpposite();
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public GtSlotProcessableItemStack<RM, I> getInputSlot(String name, boolean acceptAnything) {
        return getInputSlot(name, InvSlot.InvSide.SIDE, acceptAnything);
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance ret = super.getExtendedState(state);
        return getFacing() != this.outputSide ? ret.withProperty(PropertyHelper.OUTPUT_SIDE_PROPERTY, this.outputSide) : ret;
    }

    @Override
    public R getRecipe() {
        relocateStacks();

        R recipe = this.recipeManager.getRecipeFor(getInput());
        return fitRecipe(recipe);
    }

    protected R fitRecipe(R recipe) {
        if (recipe != null) {
            List<ItemStack> output = recipe.getOutput();
            if (this.outputSlot.canAdd(output) || this.queueOutputSlot.canAdd(output)) {
                this.outputBlocked = false;
                return recipe;
            } else this.outputBlocked = true;
        }
        return null;
    }

    protected void relocateStacks() {}

    protected abstract I getInput();

    @Override
    protected boolean strictInputSides() {
        return this.splitInput;
    }

    @Override
    public boolean isInputSide(EnumFacing side) {
        return side != getFacing();
    }

    @Override
    public boolean isOutputSide(EnumFacing side) {
        return side != getFacing();
    }

    public void moveStack(InvSlot src, InvSlot dest) {
        ItemStack srcItem = src.get();
        ItemStack destItem = dest.get();
        if (!srcItem.isEmpty() && destItem.isEmpty()) {
            src.clear();
            dest.put(srcItem);
        } else if (ItemHandlerHelper.canItemStacksStack(srcItem, destItem)) {
            int toMove = Math.min(destItem.getMaxStackSize() - destItem.getCount(), srcItem.getCount());
            srcItem.shrink(toMove);
            destItem.grow(toMove);
        }
    }

    @Override
    public void addOutput(List<ItemStack> output) {
        if (this.outputSlot.add(output) > 0) this.queueOutputSlot.add(output);

        dumpOutput();
    }

    @Override
    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        ItemStack ret = super.adjustDrop(drop, wrench);
        
        return ret == null ? BlockItems.Component.MACHINE_PARTS.getItemStack() : ret;
    }
    
    public void switchProvideEnergy() {
        this.provideEnergy = !this.provideEnergy;
        this.energy.refreshSides();
    }
    
    public void switchAutoOutput() {
        this.autoOutput = !this.autoOutput;
    }
    
    public void switchSplitInput() {
        this.splitInput = !this.splitInput;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.tickCounter % 1200 == 0 || this.outputBlocked) dumpOutput();
    }

    public void dumpOutput() {
        if (this.autoOutput) {
            ItemStack output = this.outputSlot.get();
            if (!output.isEmpty() && canUseEnergy(500)) {
                TileEntity dest = this.world.getTileEntity(this.pos.offset(this.outputSide));
                if (dest != null) {
                    int cost = StackUtil.transfer(this, dest, this.outputSide, 64);
                    if (cost > 0) {
                        useEnergy(cost);
                        ItemStack queueOutput = this.queueOutputSlot.get();
                        if (!queueOutput.isEmpty()) useEnergy(StackUtil.transfer(this, dest, this.outputSide, 64));
                    }
                }
            }
        }
    }

    @Override
    protected boolean setFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (this.outputSide != facing) {
            this.outputSide = facing;
            updateRender();
            return true;
        }
        return false;
    }

    @Override
    protected boolean needsConstantEnergy() {
        return false;
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("outputSide");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("outputSide")) updateRender();
    }

    @Override
    public ContainerBasicMachine<?> getGuiContainer(EntityPlayer player) {
        return new ContainerBasicMachine<>(player, this);
    }
}
