package mods.gregtechmod.objects.blocks.teblocks.inv;

import mods.gregtechmod.gui.GuiElectricBufferAdvanced;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferAdvanced;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityElectricBufferAdvanced extends TileEntityElectricBufferSingle {
    @NBTPersistent
    public int targetSlot;

    public TileEntityElectricBufferAdvanced() {
        super(1);
    }

    @Override
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    public int getBaseSourcePackets() {
        return 4;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected boolean canWork() {
        return canUseEnergy(500) && (
                workJustHasBeenEnabled() 
                || this.tickCounter % 200 == 0
                || this.success > 0 && this.tickCounter % 5 == 0
                || this.success >= 20
                || this.inventoryModified.get()
            );
    }

    @Override
    protected int moveItem() {
        return GtUtil.moveItemStackIntoSlot(
                this, this.world.getTileEntity(this.pos.offset(getOppositeFacing())), 
                getOppositeFacing(), getFacing(),
                this.targetSlot,
                this.targetStackSize != 0 ? this.targetStackSize : 64, this.targetStackSize != 0 ? this.targetStackSize : 1
        );
    }

    @Override
    protected int getMaxSuccess() {
        return 30;
    }

    @Override
    public ContainerElectricBufferAdvanced getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBufferAdvanced(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricBufferAdvanced(getGuiContainer(player));
    }
}
