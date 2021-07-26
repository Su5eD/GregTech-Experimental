package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.block.invslot.InvSlotConsumableLiquid;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Locale;

public class TileEntityHatchOutput extends TileEntityHatchIO {
    private Mode mode = Mode.LSI;

    public TileEntityHatchOutput() {
        super("hatch_output", InvSlotConsumableLiquid.OpType.Fill, false, true);
    }

    public Mode getMode() {
        return this.mode;
    }

    public boolean addOutput(ItemStack stack) {
        if (!stack.isEmpty()) {
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid == null) {
                if (this.mode.outputsItems && this.tank.outputSlot.canAdd(stack)) {
                    this.tank.outputSlot.add(stack);
                    return true;
                }
            }
            else return addOutput(fluid);
        }
        return false;
    }
    
    public boolean addOutput(FluidStack stack) {
        if (stack != null) {
            if (GtUtil.STEAM_PREDICATE.apply(stack.getFluid()) ? this.mode.outputsSteam : this.mode.outputsLiquids) {
                int amount = this.tank.content.fill(stack, false);
                if (amount >= stack.amount) {
                    return this.tank.content.fill(stack, true) >= stack.amount;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player) {
        boolean ret = super.onScrewdriverActivated(stack, side, player);
        if (!ret) {
            this.mode = this.mode.next();
            GtUtil.sendMessage(player, Reference.MODID + ".teblock.hatch_output.mode." + this.mode.name().toLowerCase(Locale.ROOT));
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.mode = Mode.VALUES[nbt.getInteger("mode")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", this.mode.ordinal());
        return super.writeToNBT(nbt);
    }

    public enum Mode {
        LSI(true, true, true),
        SI(true, false, true),
        SL(true, true, false),
        S(true, false, false),
        LI(false, true, true),
        I_ONLY(false, false, true),
        L_ONLY(false, true, false),
        NOTHING(false, false, false);
        
        public final boolean outputsSteam;
        public final boolean outputsLiquids;
        public final boolean outputsItems;
        
        private static final Mode[] VALUES = values();
        
        Mode(boolean outputsSteam, boolean outputsLiquids, boolean outputsItems) {
            this.outputsSteam = outputsSteam;
            this.outputsLiquids = outputsLiquids;
            this.outputsItems = outputsItems;
        }
        
        public Mode next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }
    }
}
