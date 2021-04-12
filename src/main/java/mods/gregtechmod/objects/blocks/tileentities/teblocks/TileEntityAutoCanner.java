package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiAutoCanner;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachineMultiInput;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityAutoCanner extends TileEntityBasicMachineMultiInput {

    public TileEntityAutoCanner() {
        super("auto_canner", GtRecipes.canner);
    }

    @Override
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAutoCanner(new ContainerBasicMachine<>(entityPlayer, this));
    }
}
