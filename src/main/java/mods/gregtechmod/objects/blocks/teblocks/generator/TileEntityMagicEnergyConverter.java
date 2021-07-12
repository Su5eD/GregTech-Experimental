package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiMagicEnergyConverter;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFluidGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMagicEnergyConverter extends TileEntityFluidGenerator {
    
    public TileEntityMagicEnergyConverter() {
        super("magic_energy_converter", GtFuels.magic);
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return AdjustableEnergy.createSource(this, 1000000000, 1, 24, getSourceSides());
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
        return new GuiMagicEnergyConverter(new ContainerFluidGenerator(player, this), this.tank.content);
    }
}
