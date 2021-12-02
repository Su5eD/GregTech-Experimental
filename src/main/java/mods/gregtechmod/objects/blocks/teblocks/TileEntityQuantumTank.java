package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.gui.GuiBasicTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerQuantumTank;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityQuantumTank extends TileEntityCoverBehavior implements IHasGui {
    public BasicTank tank;

    public TileEntityQuantumTank() {
        Fluids fluids = addComponent(new Fluids(this));
        this.tank = addComponent(new BasicTank(this, GregTechConfig.FEATURES.quantumTankCapacity, fluids));
        
        this.coverBlacklist.add(CoverType.ENERGY);
    }

    @Override
    public ContainerQuantumTank getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerQuantumTank(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBasicTank<>(getGuiContainer(player), this.tank.content);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
