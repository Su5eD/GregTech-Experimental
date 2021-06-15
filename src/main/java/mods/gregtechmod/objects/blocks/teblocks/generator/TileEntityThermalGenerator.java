package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.gui.GuiThermalGenerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFluidGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityThermalGenerator extends TileEntityFluidGenerator {
    
    public TileEntityThermalGenerator() {
        super("thermal_generator", GtFuels.hot);
    }

    @Override
    protected double getFuelValue(double energy) {
        return energy * 4 / 5;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiThermalGenerator(new ContainerFluidGenerator(player, this), this.tank.content);
    }
}
