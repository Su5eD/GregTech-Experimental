package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiBasicTank;
import mods.gregtechmod.inventory.DynamicGtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerHatchIO;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public abstract class TileEntityHatchIO extends TileEntityCoverBehavior implements IHasGui {
    private final Predicate<EnumFacing> facingPredicate = side -> side == getFacing();
    public final BasicTank tank;

    public TileEntityHatchIO(String descriptionKey, InvSlotConsumableLiquid.OpType opType, boolean isInput, boolean isOutput) {
        super(descriptionKey);
        Fluids fluids = addComponent(new Fluids(this));
        this.tank = addComponent(new BasicTank(this, fluids, new DynamicGtFluidTank(this, "content", isInput ? facingPredicate : Predicates.alwaysFalse(), isOutput ? facingPredicate : Predicates.alwaysFalse(), Predicates.alwaysTrue(), 16000), opType, false));
        
        this.coverBlacklist.add(CoverType.GENERIC);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return this.facingPredicate.test(side) && super.canInsertItem(index, stack, side);
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        EnumFacing facing = getFacing();
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            Map<EnumFacing, ResourceLocation> overrides = new HashMap<>();
            overrides.put(EnumFacing.NORTH, new ResourceLocation(Reference.MODID, "blocks/machines/machine_" + (facing == EnumFacing.UP ? "top" : "bottom") + "_pipe"));
            return state.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new PropertyHelper.TextureOverride(overrides));
        }
        return super.getExtendedState(state);
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerHatchIO(player, this);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBasicTank<>(new ContainerHatchIO(player, this), this.tank.content);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
