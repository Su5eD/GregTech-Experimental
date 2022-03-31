package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.gui.GuiElectricSorter;
import mods.gregtechmod.inventory.invslot.GtSlot;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricSorter;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.stream.StreamSupport;

public class TileEntityElectricSorter extends TileEntityElectricSorterBase {
    public final InvSlot filter;

    public TileEntityElectricSorter() {
        this.filter = new GtSlot(this, "filter", InvSlot.Access.NONE, 9);
    }

    @Override
    public long getMjCapacity() {
        return getBaseEUCapacity() / 2;
    }

    @Override
    protected boolean applyFilter(ItemStack stack) {
        return StreamSupport.stream(this.filter.spliterator(), false)
            .anyMatch(s -> GtUtil.stackItemEquals(s, stack));
    }

    @Override
    public ContainerElectricSorter getGuiContainer(EntityPlayer player) {
        return new ContainerElectricSorter(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricSorter<>(getGuiContainer(player));
    }
}
