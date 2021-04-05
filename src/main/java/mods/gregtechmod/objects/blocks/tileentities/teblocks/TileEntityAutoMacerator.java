package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.ContainerBase;
import mods.gregtechmod.gui.GuiAutoMacerator;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.manager.IC2Recipes;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAutoMacerator extends TileEntityBasicMachine {

    public TileEntityAutoMacerator() {
        super("auto_macerator", IC2Recipes.MACERATOR);
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerBasicMachine<>(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoMacerator(new ContainerBasicMachine<>(player, this));
    }
}
