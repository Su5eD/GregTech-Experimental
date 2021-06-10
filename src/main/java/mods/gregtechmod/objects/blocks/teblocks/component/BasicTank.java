package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Fluids;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotOutput;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.inventory.GtFluidTank;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Function;

public class BasicTank extends TileEntityComponent {
    public GtFluidTank content;
    public final InvSlotConsumableLiquid inputSlot;
    public final InvSlotOutput outputSlot;
    
    private int tickCounter;
    
    public <T extends TileEntityBlock & IInventorySlotHolder<?> & ICoverable> BasicTank(T parent, int capacity, Fluids fluidComponent) {
        this(parent, capacity, fluidComponent, tank -> new InvSlotConsumableLiquidByTank(parent, "tankInputSlot", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Both, tank));
    }
    
    public <T extends TileEntityBlock & IInventorySlotHolder<?> & ICoverable> BasicTank(T parent, int capacity, Fluids fluidComponent, Function<GtFluidTank, InvSlotConsumableLiquid> inputSlotFactory) {
        super(parent);
        this.content = new GtFluidTank(parent, "content", capacity);
        fluidComponent.addTank(this.content);
        this.inputSlot = inputSlotFactory.apply(this.content);
        this.outputSlot = new InvSlotOutput(parent, "tankOutputSlot", 1);
    }
    
    @Override
    public boolean enableWorldTick() {
        return !this.parent.getWorld().isRemote;
    }

    @Override
    public void onWorldTick() {
        if (this.tickCounter++ % 10 == 0) {
            this.inputSlot.processIntoTank(this.content, this.outputSlot);
            this.inputSlot.processFromTank(this.content, this.outputSlot);
        }
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        this.content.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.content.writeToNBT(nbt);
        return nbt;
    }
}
