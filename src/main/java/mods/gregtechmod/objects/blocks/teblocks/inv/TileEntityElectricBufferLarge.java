package mods.gregtechmod.objects.blocks.teblocks.inv;

import mods.gregtechmod.gui.GuiElectricBufferLarge;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferLarge;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityElectricBufferLarge extends TileEntityElectricBuffer {

    public TileEntityElectricBufferLarge() {
        super(27);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public ContainerElectricBufferLarge getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBufferLarge(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricBufferLarge(getGuiContainer(player));
    }
}
