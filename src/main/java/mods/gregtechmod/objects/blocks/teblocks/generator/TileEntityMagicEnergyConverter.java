package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiMagicEnergyConverter;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMagicEnergyConverter extends TileEntityFluidGenerator {
    
    public TileEntityMagicEnergyConverter() {
        super(GtFuels.magic);
    }

    @Override
    public double getMaxOutputEUp() {
        return 24;
    }

    @Override
    public int getSourceTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000000000;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (shouldExplode) {
            ModHandler.polluteAura(this.world, this.pos, 20 * this.world.rand.nextInt(20), true);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiMagicEnergyConverter(getGuiContainer(player), this.tank.content);
    }
}
