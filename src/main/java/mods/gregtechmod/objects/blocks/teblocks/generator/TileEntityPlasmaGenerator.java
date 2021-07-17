package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.gui.GuiPlasmaGenerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerPlasmaGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPlasmaGenerator extends TileEntityFluidGenerator {

    public TileEntityPlasmaGenerator() {
        super("plasma_generator", GtFuels.plasma);
    }

    @Override
    public int getSourceTier() {
        return 4;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000000000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiPlasmaGenerator(new ContainerPlasmaGenerator(player, this), this.tank.content);
    }
}
