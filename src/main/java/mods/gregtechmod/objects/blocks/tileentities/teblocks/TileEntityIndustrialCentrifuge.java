package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.ContainerBase;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerIndustrialCentrifuge;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityIndustrialCentrifuge extends TileEntityIndustrialCentrifugeBase {

    public TileEntityIndustrialCentrifuge() {
        super("industrial_centrifuge", 1, 32000, GtRecipes.industrialCentrifuge, true);
    }

    public ContainerBase<TileEntityIndustrialCentrifugeBase> getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialCentrifuge(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialCentrifuge(new ContainerIndustrialCentrifuge(player, this));
    }
}
