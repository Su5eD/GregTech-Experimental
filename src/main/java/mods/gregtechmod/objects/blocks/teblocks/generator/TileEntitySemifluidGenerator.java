package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.gui.GuiSemifluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySemifluidGenerator extends TileEntityFluidGenerator {

    public TileEntitySemifluidGenerator() {
        super("gt_semifluid_generator", GtFuels.denseLiquid);
    }

    @Override
    protected double getMaxOutputEUp() {
        return 8;
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiSemifluidGenerator(getGuiContainer(player), this.tank.content);
    }
}
