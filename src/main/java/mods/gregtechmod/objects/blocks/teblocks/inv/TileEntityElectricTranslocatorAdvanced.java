package mods.gregtechmod.objects.blocks.teblocks.inv;

import mods.gregtechmod.gui.GuiElectricTranslocatorAdvanced;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricTranslocatorAdvanced;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

public class TileEntityElectricTranslocatorAdvanced extends TileEntityElectricTranslocator {
    @NBTPersistent
    public EnumFacing inputSide = EnumFacing.DOWN;
    @NBTPersistent
    public EnumFacing outputSide = EnumFacing.DOWN;

    public TileEntityElectricTranslocatorAdvanced() {
        super(1800);
    }

    public void switchInputFacing() {
        this.inputSide = GtUtil.getNextFacing(this.inputSide);

        updateClientField("inputSide");
    }

    public void switchOutputFacing() {
        this.outputSide = GtUtil.getNextFacing(this.outputSide);

        updateClientField("outputSide");
    }

    @Override
    protected EnumFacing transferFromSide() {
        return this.inputSide;
    }

    @Override
    protected EnumFacing transferToSide() {
        return this.outputSide;
    }

    @Override
    public ContainerElectricTranslocatorAdvanced getGuiContainer(EntityPlayer player) {
        return new ContainerElectricTranslocatorAdvanced(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricTranslocatorAdvanced(getGuiContainer(player));
    }
}
