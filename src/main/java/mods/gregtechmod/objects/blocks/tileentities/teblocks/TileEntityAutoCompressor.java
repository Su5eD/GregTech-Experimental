package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.gui.GuiAutoCompressor;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.manager.IC2Recipes;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAutoCompressor extends TileEntityBasicMachine {

    public TileEntityAutoCompressor() {
        super("auto_compressor", IC2Recipes.COMPRESSOR);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoCompressor(new ContainerBasicMachine<>(player, this));
    }
}