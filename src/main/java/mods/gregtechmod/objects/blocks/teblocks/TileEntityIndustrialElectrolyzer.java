package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiIndustrialElectrolyzer;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialElectrolyzer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityIndustrialElectrolyzer extends TileEntityIndustrialCentrifugeBase {

    public TileEntityIndustrialElectrolyzer() {
        super("industrial_electrolyzer", 64000, GtRecipes.industrialElectrolyzer, false);
    }

    @Override
    protected int getBaseSinkTier() {
        return 2;
    }

    @Override
    public ContainerIndustrialElectrolyzer getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialElectrolyzer(player, this);
    }
        
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialElectrolyzer(getGuiContainer(player));
    }
}
