package mods.gregtechmod.objects.blocks.teblocks.energy;

import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiAESU;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAESU;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TileEntityAESU extends TileEntityChargerBase {
    @NBTPersistent
    public int outputVoltage;
    
    public final int maxOutputVoltage = GregTechMod.classic ? 2048 : 8192;

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.singleton(IC2UpgradeType.BATTERY);
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.singleton(GtUpgradeType.BATTERY);
    }

    @Override
    public int getBaseSinkTier() {
        return GregTechMod.classic ? 4 : 5;
    }

    @Override
    public int getSourceTier() {
        return GregTechMod.classic ? 4 : 5;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 100000000;
    }

    @Override
    public double getMaxOutputEUp() {
        return this.outputVoltage;
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.set(3, GtLocale.translateTeBlock("aesu", "max_energy_out", this.maxOutputVoltage));
    }

    @Override
    public ContainerEnergyStorage<TileEntityAESU> getGuiContainer(EntityPlayer player) {
        return new ContainerAESU(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAESU(getGuiContainer(player));
    }
}
