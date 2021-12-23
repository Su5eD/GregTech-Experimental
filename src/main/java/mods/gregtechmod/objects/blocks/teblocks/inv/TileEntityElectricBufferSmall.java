package mods.gregtechmod.objects.blocks.teblocks.inv;

import mods.gregtechmod.gui.GuiElectricBufferSmall;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityElectricBufferSmall extends TileEntityElectricBuffer {

    public TileEntityElectricBufferSmall() {
        super(1);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1250;
    }

    @Override
    public ContainerElectricBufferSmall getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBufferSmall(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricBufferSmall(getGuiContainer(player));
    }
}
