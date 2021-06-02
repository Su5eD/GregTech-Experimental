package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.inventory.GtInvSide;
import mods.gregtechmod.inventory.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TileEntityBasicMachine<R extends IMachineRecipe<RI, List<ItemStack>>, RI, I, RM extends IGtRecipeManagerBasic<RI, I, R>> extends TileEntityGTMachine<R, RI, I, RM> implements INetworkClientTileEntityEventListener {
    public EnumFacing outputSide = EnumFacing.SOUTH;
    public final InvSlotOutput queueOutputSlot;
    public final GtSlotProcessableItemStack<RM, I> queueInputSlot;
    public final InvSlot extraSlot;
    protected boolean outputBlocked;

    public boolean provideEnergy = false;
    public boolean autoOutput = true;
    public boolean splitInput = false;

    public TileEntityBasicMachine(String descriptionKey, RM recipeManager) {
        this(descriptionKey, recipeManager, false);
    }

    public TileEntityBasicMachine(String descriptionKey, RM recipeManager, boolean wildcardInput) {
        super(descriptionKey, 2000, 1, 1, recipeManager, wildcardInput);
        this.extraSlot = getExtraSlot();
        this.queueInputSlot = getInputSlot("queueInput", GtInvSide.VERTICAL, wildcardInput);
        this.queueOutputSlot = getOutputSlot("queueOutput", 1);
    }

    protected InvSlot getExtraSlot() {
        InvSlot slot = new InvSlotDischarge(this, InvSlot.Access.IO, 1, false, InvSlot.InvSide.NOTSIDE);
        this.energy.addManagedSlot(slot);
        return slot;
    }

    @Override
    protected Set<EnumFacing> getSinkDirs() {
        EnumFacing facing = getFacing();
        return Arrays.stream(EnumFacing.VALUES)
                .filter(side -> side != facing)
                .collect(Collectors.toSet());
    }

    @Override
    protected Set<EnumFacing> getSourceDirs() {
        return this.provideEnergy ? Collections.singleton(this.outputSide) : Collections.emptySet();
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        rerender();
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.outputSide = getFacing().getOpposite();
    }

    @Override
    public GtSlotProcessableItemStack<RM, I> getInputSlot(String name, boolean acceptAnything) {
        return getInputSlot(name, InvSlot.InvSide.SIDE, acceptAnything);
    }

    public GtSlotProcessableItemStack<RM, I> getInputSlot(String name, InvSlot.InvSide side, boolean acceptAnything) {
        return new GtSlotProcessableItemStack<>(this, name, InvSlot.Access.I, 1, side, acceptAnything ? null : recipeManager);
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance ret = super.getExtendedState(state);
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
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return side != getFacing() && super.canInsertItem(index, stack, side);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        return side != getFacing() && super.canExtractItem(index, stack, side);
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
        if (ret == null) ret = new ItemStack(BlockItems.Component.MACHINE_PARTS.getInstance());
        return ret;
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        boolean value = event % 2 != 0;
        switch (event) {
            case 0:
            case 1:
                this.provideEnergy = value;
                updateEnet();
                break;
            case 2:
            case 3:
                this.autoOutput = value;
                break;
            case 4:
            case 5:
                this.splitInput = value;
                break;
        }
    }

    @Override
    protected void onUpdateUpgrade(IGtUpgradeItem item, ItemStack stack, EntityPlayer player) {
        super.onUpdateUpgrade(item, stack, player);
        if (this.provideEnergy && item.getType() == GtUpgradeType.TRANSFORMER) updateSourceTier();
    }

    @Override
    protected void onUpdateUpgrade(IC2UpgradeType type, ItemStack stack) {
        super.onUpdateUpgrade(type, stack);
        if (this.provideEnergy && type == IC2UpgradeType.TRANSFORMER) updateSourceTier();
    }

    @Override
    public int getSourceTier() {
        if (this.provideEnergy) {
            int transformers = this.getUpgradeCount(IC2UpgradeType.TRANSFORMER) + this.getUpgradeCount(GtUpgradeType.TRANSFORMER);
            if (transformers > 0) return transformers;
        }
        return 1;
    }

    @Override
    protected int getOutputAmperage() {
        return this.getUpgradeCount(IC2UpgradeType.TRANSFORMER) > 0 ? 4 : super.getOutputAmperage();
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.tickCounter % 1200 == 0 || this.outputBlocked) dumpOutput();
    }

    public void dumpOutput() {
        if (this.autoOutput) {
            ItemStack output = this.outputSlot.get();
            if (!output.isEmpty() && this.energy.getEnergy() >= 500) {
                TileEntity dest = this.world.getTileEntity(this.pos.offset(this.outputSide));
                if (dest != null) {
                    int cost = StackUtil.transfer(this, dest, this.outputSide, 64);
                    if (cost > 0) {
                        this.energy.useEnergy(cost);
                        ItemStack queueOutput = this.queueOutputSlot.get();
                        if (!queueOutput.isEmpty()) this.energy.useEnergy(StackUtil.transfer(this, dest, this.outputSide, 64));
                    }
                }
            }
        }
    }

    @Override
    protected boolean setFacingWrench(EnumFacing facing, EntityPlayer player) {
        if (this.outputSide != facing) {
            this.outputSide = facing;
            updateEnet();
            rerender();
            return true;
        }
        return false;
    }

    @Override
    protected boolean needsConstantEnergy() {
        return false;
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("outputSide");
        return ret;
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("outputSide")) rerender();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound ret = super.writeToNBT(nbt);
        ret.setInteger("outputSide", this.outputSide.getIndex());
        ret.setBoolean("provideEnergy", this.provideEnergy);
        ret.setBoolean("autoOutput", this.autoOutput);
        ret.setBoolean("splitInput", this.splitInput);
        return ret;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.outputSide = EnumFacing.VALUES[nbt.getInteger("outputSide")];
        this.provideEnergy = nbt.getBoolean("provideEnergy");
        this.autoOutput = nbt.getBoolean("autoOutput");
        this.splitInput = nbt.getBoolean("splitInput");
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerBasicMachine<>(player, this);
    }
}
