package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.block.invslot.InvSlotConsumableLiquid;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Locale;

public class TileEntityHatchOutput extends TileEntityHatchIO {
    @NBTPersistent
    private Mode mode = Mode.LSI;

    public TileEntityHatchOutput() {
        super(InvSlotConsumableLiquid.OpType.Fill, false, true, false);
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
            return addOutput(fluid);
        }
        return false;
    }
    
    public boolean addOutput(FluidStack stack) {
        if (stack != null) {
            if (GtUtil.STEAM_PREDICATE.test(stack.getFluid()) ? this.mode.outputsSteam : this.mode.outputsLiquids) {
                int amount = this.tank.content.fill(stack, false);
                if (amount >= stack.amount) {
                    return this.tank.content.fill(stack, true) >= stack.amount;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        boolean ret = super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
        if (!ret) {
            this.mode = this.mode.next();
            GtUtil.sendMessage(player, GtLocale.buildKeyTeBlock(this, "mode", this.mode.name().toLowerCase(Locale.ROOT)));
        }
        return true;
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
            return VALUES[(ordinal() + 1) % VALUES.length];
        }
    }
}
