package mods.gregtechmod.objects.blocks.teblocks.energy;

import mods.gregtechmod.gui.GuiIDSU;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIDSU;
import mods.gregtechmod.world.IDSUData;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityIDSU extends TileEntityChargerBase {
    private IDSUData.EnergyWrapper wrapper = IDSUData.EnergyWrapper.EMPTY;

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.wrapper = IDSUData.get(this.world).getOrCreateWrapper(getOwner().getId());
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
    public int getBaseSourceTier() {
        return 5;
    }

    @Override
    protected int getBaseEUCapacity() {
        return IDSUData.EnergyWrapper.CAPACITY;
    }

    @Override
    public ContainerIDSU getGuiContainer(EntityPlayer player) {
        return new ContainerIDSU(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIDSU(getGuiContainer(player));
    }

    public class IDSUEnergy extends DynamicAdjustableEnergy {

        @Override
        public double getStoredEnergy() {
            return TileEntityIDSU.this.wrapper.getEnergy();
        }

        @Override
        public void forceCharge(double amount) {}

        @Override
        protected double injectEnergy(double amount) {
            double injected = TileEntityIDSU.this.wrapper.addEnergy(amount);
            updateAverageEUInput(injected);
            return injected;
        }

        @Override
        public double discharge(double amount, boolean simulate) {
            return TileEntityIDSU.this.wrapper.removeEnergy(amount, simulate);
        }
    }
}
