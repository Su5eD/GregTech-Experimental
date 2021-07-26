package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialCentrifuge;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityIndustrialCentrifuge extends TileEntityIndustrialCentrifugeBase {

    public TileEntityIndustrialCentrifuge() {
        super("industrial_centrifuge", 32000, GtRecipes.industrialCentrifuge, true);
    }

    @Override
    protected int getBaseSinkTier() {
        return 1;
    }

    @Override
    public ContainerIndustrialCentrifuge getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialCentrifuge(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialCentrifuge(getGuiContainer(player));
    }
}
