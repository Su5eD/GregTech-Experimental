package mods.gregtechmod.objects.blocks.teblocks.computercube;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiComputerCubeMain;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCube;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeMain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ComputerCubeMain implements IComputerCubeModule {
    public static final ComputerCubeMain INSTANCE = new ComputerCubeMain();
    private static final ResourceLocation NAME = new ResourceLocation(Reference.MODID, "main");

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public boolean updateServer() {
        return false;
    }

    @Override
    public ContainerComputerCube getGuiContainer(EntityPlayer player, TileEntityComputerCube base) {
        return new ContainerComputerCubeMain(player, base);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base) {
        return new GuiComputerCubeMain(getGuiContainer(player, base));
    }

    @Nullable
    @Override
    public ResourceLocation getTexture() {
        return null;
    }
}
