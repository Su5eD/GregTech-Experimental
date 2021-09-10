package mods.gregtechmod.objects.blocks.teblocks.energy;

import mods.gregtechmod.gui.GuiIDSU;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIDSU;
import mods.gregtechmod.world.IDSUData;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityIDSU extends TileEntityChargerBase {
    private IDSUData.EnergyWrapper wrapper = IDSUData.EnergyWrapper.EMPTY;

    public TileEntityIDSU() {
        super("idsu");
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.wrapper = IDSUData.get(this.world).getOrCreateWrapper(getOwner().getId());
            this.energy.forceCharge(getStoredEU());
        }
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return new IDSUEnergy();
    }

    @Override
    public int getBaseSinkTier() {
        return 5;
    }

    @Override
    public int getSourceTier() {
        return 5;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000000000;
    }

    @Override
    public double getStoredEU() {
        return this.wrapper.getEnergy();
    }

    @Override
    public ContainerEnergyStorage<?> getGuiContainer(EntityPlayer player) {
        return new ContainerIDSU(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIDSU(getGuiContainer(player));
    }
    
    public class IDSUEnergy extends DynamicAdjustableEnergy {

        @Override
        protected double injectEnergy(double amount) {
            double ret = super.injectEnergy(amount);
            TileEntityIDSU.this.wrapper.addEnergy(ret);
            return ret;
        }

        @Override
        public double discharge(double amount, boolean simulate) {
            double ret = super.discharge(amount, simulate);
            TileEntityIDSU.this.wrapper.removeEnergy(ret);
            return ret;
        }
    }
}
