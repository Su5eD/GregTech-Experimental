package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.gui.GuiGasTurbine;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFluidGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGasTurbine extends TileEntityFluidGenerator {

    public TileEntityGasTurbine() {
        super("gas_turbine", GtFuels.gas);
    }

    @Override
    protected double getMaxOutputEUp() {
        return 16;
    }

    @Override
    public int getSourceTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000000;
    }

    @Override
    protected double getFuelValue(double energy) {
        return energy * 3 / 4;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiGasTurbine(new ContainerFluidGenerator(player, this), this.tank.content);
    }
}
