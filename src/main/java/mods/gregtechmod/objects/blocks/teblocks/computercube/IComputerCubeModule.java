package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.core.ContainerBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IComputerCubeModule {
    ResourceLocation getName();

    /**
     * @return send a network update to the client
     */
    boolean updateServer();

    ContainerBase<?> getGuiContainer(EntityPlayer player, TileEntityComputerCube base);

    @SideOnly(Side.CLIENT)
    GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base);

    @Nullable
    ResourceLocation getTexture();
}
