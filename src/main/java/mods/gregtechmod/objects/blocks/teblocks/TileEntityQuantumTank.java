package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.gui.GuiBasicTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerQuantumTank;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class TileEntityQuantumTank extends TileEntityCoverBehavior implements IHasGui {
    public BasicTank tank;

    public TileEntityQuantumTank() {
        Fluids fluids = addComponent(new Fluids(this));
        this.tank = addComponent(new BasicTank(this, GregTechConfig.FEATURES.quantumTankCapacity, fluids));
        
        this.allowedCovers = EnumSet.of(CoverType.GENERIC, CoverType.IO, CoverType.CONTROLLER, CoverType.METER);
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerQuantumTank(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiBasicTank<>(new ContainerQuantumTank(entityPlayer, this), this.tank.content);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GtUtil.translateTeBlockDescription("quantum_tank"));
    }
    
    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Collections.emptySet();
    }

    @Override
    public double getMaxInputEUp() {
        return 0;
    }

    @Override
    public double getStoredEU() {
        return 0;
    }

    @Override
    public double getEUCapacity() {
        return 0;
    }

    @Override
    public double getAverageEUInput() {
        return 0;
    }

    @Override
    public double getAverageEUOutput() {
        return 0;
    }

    @Override
    public double getStoredSteam() {
        return 0;
    }

    @Override
    public double getSteamCapacity() {
        return 0;
    }

    @Override
    public long getStoredMj() {
        return 0;
    }

    @Override
    public long getMjCapacity() {
        return 0;
    }

    @Override
    public void setMjCapacity(long capacity) {}

    @Override
    public void updateEnet() {}

    @Override
    public void addEnergy(double amount) {}

    @Override
    public double useEnergy(double amount, boolean simulate) {
        return 0;
    }

    @Override
    public double getUniversalEnergy() {
        return 0;
    }

    @Override
    public double getUniversalEnergyCapacity() {
        return 0;
    }

    @Override
    public int getSinkTier() {
        return 0;
    }

    @Override
    public int getSourceTier() {
        return 0;
    }

    @Override
    public double getMaxOutputEUt() {
        return 0;
    }

    @Override
    public void markForExplosion() {}
}
