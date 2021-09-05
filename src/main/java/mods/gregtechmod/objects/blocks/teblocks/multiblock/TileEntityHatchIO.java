package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiBasicTank;
import mods.gregtechmod.inventory.tank.DynamicGtFluidTank;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerHatchIO;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("Guava")
public abstract class TileEntityHatchIO extends TileEntityCoverBehavior implements IHasGui {
    private final boolean wildcardInput;
    private final Predicate<EnumFacing> facingPredicate = side -> side == getFacing();
    public final BasicTank tank;

    public TileEntityHatchIO(String descriptionKey, InvSlotConsumableLiquid.OpType opType, boolean isInput, boolean isOutput, boolean wildcardInput) {
        super(descriptionKey);
        Fluids fluids = addComponent(new Fluids(this));
        GtFluidTank fluidTank = new DynamicGtFluidTank(this, "content", isInput ? facingPredicate : Predicates.alwaysFalse(), isOutput ? facingPredicate : Predicates.alwaysFalse(), Predicates.alwaysTrue(), 16000);
        this.tank = addComponent(new BasicTank(this, fluids, fluidTank, t -> new HatchTankInputSlot(opType, t), false));
        this.wildcardInput = wildcardInput;
        
        this.coverBlacklist.add(CoverType.GENERIC);
        this.coverBlacklist.add(CoverType.ENERGY);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return this.facingPredicate.test(side) && super.canInsertItem(index, stack, side);
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance extendedState = super.getExtendedState(state);
        EnumFacing facing = getFacing();
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            return extendedState.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new PropertyHelper.TextureOverride(EnumFacing.NORTH, new ResourceLocation(Reference.MODID, "blocks/machines/machine_" + (facing == EnumFacing.UP ? "top" : "bottom") + "_pipe")));
        }
        return extendedState;
    }

    @Override
    public ContainerHatchIO getGuiContainer(EntityPlayer player) {
        return new ContainerHatchIO(player, this);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBasicTank<>(getGuiContainer(player), this.tank.content);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
    
    private class HatchTankInputSlot extends InvSlotConsumableLiquidByTank {

        public HatchTankInputSlot(OpType opType, IFluidTank tank) {
            super(TileEntityHatchIO.this, "tankInputSlot", InvSlot.Access.I, 1, InvSlot.InvSide.NOTSIDE, opType, tank);
        }

        @Override
        public boolean accepts(ItemStack stack) {
            return wildcardInput || super.accepts(stack);
        }
    }
}
