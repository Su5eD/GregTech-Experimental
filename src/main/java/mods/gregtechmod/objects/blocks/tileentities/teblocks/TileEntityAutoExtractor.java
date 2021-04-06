package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.gui.GuiAutoExtractor;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.manager.IC2Recipes;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAutoExtractor extends TileEntityBasicMachine {

    public TileEntityAutoExtractor() {
        super("auto_extractor", IC2Recipes.EXTRACTOR);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoExtractor(new ContainerBasicMachine<>(player, this));
    }
}
