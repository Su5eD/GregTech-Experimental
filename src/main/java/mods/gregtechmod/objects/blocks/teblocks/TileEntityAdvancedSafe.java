package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.gui.GuiAdvancedSafe;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedSafe;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAdvancedSafe extends TileEntityCoverBehavior implements IHasGui {
    public final InvSlot filter;
    public final InvSlot content;

    public TileEntityAdvancedSafe() {
        this.content = new InvSlot(this, "content", InvSlot.Access.IO, 27);
        this.filter = new InvSlot(this, "filter", InvSlot.Access.NONE, 1);
        
        setPrivate(true);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return super.canInsertItem(index, stack, side) && (this.filter.isEmpty() || GtUtil.stackEquals(this.filter.get(), stack));
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        return false;
    }

    @Override
    public ContainerAdvancedSafe getGuiContainer(EntityPlayer player) {
        return new ContainerAdvancedSafe(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAdvancedSafe(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}
}
