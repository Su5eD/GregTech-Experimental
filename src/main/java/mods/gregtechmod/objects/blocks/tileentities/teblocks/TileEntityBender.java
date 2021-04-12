package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiBender;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachineSingleInput;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityBender extends TileEntityBasicMachineSingleInput {

    public TileEntityBender() {
        super("bender", GtRecipes.bender);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBender(new ContainerBasicMachine<>(player, this));
    }
}
