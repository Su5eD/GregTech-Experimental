package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.gui.GuiThermalGenerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityThermalGenerator extends TileEntityFluidGenerator {
    
    public TileEntityThermalGenerator() {
        super(GtFuels.hot);
    }

    @Override
    public int getSourceTier() {
        return 1;
    }

    @Override
    public double getMaxOutputEUp() {
        return 24;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000000;
    }

    @Override
    protected double getFuelValue(double energy) {
        return energy * 4 / 5;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiThermalGenerator(getGuiContainer(player), this.tank.content);
    }
}
