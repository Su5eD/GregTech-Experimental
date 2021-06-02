package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiAssembler;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachineMultiInput;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAssembler extends TileEntityBasicMachineMultiInput {

    public TileEntityAssembler() {
        super("assembler", GtRecipes.assembler);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAssembler(new ContainerBasicMachine<>(player, this));
    }
}
