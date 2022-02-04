package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.function.Function;

public class BasicTank extends GtComponentBase {
    public GtFluidTank content;
    public final InvSlotConsumableLiquid inputSlot;
    public final InvSlotOutput outputSlot;
    private final boolean slowProcessing;

    private int tickCounter;

    public <T extends TileEntityBlock & IInventorySlotHolder<?> & ICoverable> BasicTank(T parent, int capacity, Fluids fluidComponent) {
        this(parent, fluidComponent, getDefaultFluidTank(parent, capacity, Util.allFacings, Util.allFacings), InvSlotConsumableLiquid.OpType.Both, true);
    }

    public <T extends TileEntityBlock & IInventorySlotHolder<?> & ICoverable> BasicTank(T parent, Fluids fluidComponent, GtFluidTank tank, InvSlotConsumableLiquid.OpType opType, boolean slowProcessing) {
        this(parent, fluidComponent, tank, fluidTank -> new InvSlotConsumableLiquidByTank(parent, "tankInputSlot", InvSlot.Access.I, 1, InvSlot.InvSide.NOTSIDE, opType, fluidTank), slowProcessing);
    }

    public <T extends TileEntityBlock & IInventorySlotHolder<?> & ICoverable> BasicTank(T parent, Fluids fluidComponent, GtFluidTank tank, Function<GtFluidTank, InvSlotConsumableLiquid> inputSlotFactory, boolean slowProcessing) {
        super(parent);
        this.content = tank;
        fluidComponent.addTank(this.content);
        this.inputSlot = inputSlotFactory.apply(this.content);
        this.outputSlot = new InvSlotOutput(parent, "tankOutputSlot", 1);
        this.slowProcessing = slowProcessing;
    }

    private static GtFluidTank getDefaultFluidTank(ICoverable parent, int capacity, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides) {
        return new GtFluidTank(parent, "content", inputSides, outputSides, fluid -> true, capacity);
    }

    @Override
    public boolean enableWorldTick() {
        return !this.parent.getWorld().isRemote;
    }

    @Override
    public void onWorldTick() {
        if (this.slowProcessing && this.tickCounter++ % 10 != 0) return;

        this.inputSlot.processIntoTank(this.content, this.outputSlot);
        this.inputSlot.processFromTank(this.content, this.outputSlot);
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        this.content.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNbt() {
        return this.content.writeToNBT(new NBTTagCompound());
    }
}
