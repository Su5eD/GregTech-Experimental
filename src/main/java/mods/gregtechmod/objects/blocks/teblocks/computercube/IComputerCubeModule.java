package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.core.ContainerBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface IComputerCubeModule {
    ResourceLocation getName();
    
    ContainerBase<?> getGuiContainer(EntityPlayer player, TileEntityComputerCube base);

    GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base);

    @Nullable
    ResourceLocation getTexture();
}
