package mods.gregtechmod.objects.blocks.teblocks.energy;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.Util;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiChargeOMat;
import mods.gregtechmod.inventory.invslot.GtSlotChargeHybrid;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerChargeOMat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.stream.Stream;

public class TileEntityChargeOMat extends TileEntityEnergy implements IHasGui {
    public final GtSlotChargeHybrid[] chargeSlots;
    public final InvSlotOutput outputSlot;

    public TileEntityChargeOMat() {
        this.chargeSlots = Stream.generate(() -> new GtSlotChargeHybrid(this, "charge", getSourceTier(), this::isRedstonePowered))
                .limit(9)
                .peek(this::addChargingSlot)
                .peek(this::addDischargingSlot)
                .toArray(GtSlotChargeHybrid[]::new);
        this.outputSlot = new InvSlotOutput(this, "output", 9);
        this.energyCapacityTooltip = true;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (this.tickCounter % 20 == 0) {
            EntityPlayer player = this.world.getClosestPlayer(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 3, false);
            if (player != null) {
                int sinkTier = getSinkTier();
                int sourceTier = getSourceTier();
                int tier = Math.max(sinkTier, sourceTier);
                boolean discharge = isRedstonePowered();
                
                for (int i = 0; i < tier; i++) {
                    player.inventory.armorInventory.stream()
                            .filter(stack -> stack.getItem() instanceof IElectricItem)
                            .forEach(stack -> {
                                if (discharge) {
                                    double demandedEnergy = Math.min(getEUCapacity() - getStoredEU(), getMaxInputEUp());
                                    if (demandedEnergy > 0) {
                                        double decharged = ElectricItem.manager.discharge(stack, demandedEnergy, sinkTier, false, false, false);
                                        addEnergy(decharged);
                                    }
                                } else {
                                    double storedEU = getStoredEU();
                                    if (storedEU > 0) {
                                        double charged = ElectricItem.manager.charge(stack, storedEU, sourceTier, false, false);
                                        useEnergy(charged);
                                    }
                                }
                            });
                }
            }
        }
        
        if (this.tickCounter % 100 == 0) {
            for (GtSlotChargeHybrid slot : this.chargeSlots) {
                ItemStack stack = slot.get();

                if (!stack.isEmpty()) {
                    IElectricItem item = (IElectricItem) stack.getItem();
                    
                    if (isRedstonePowered() && (!item.canProvideEnergy(stack) || ElectricItem.manager.discharge(stack, 1000000, getSinkTier(), true, true, true) <= 0)
                            || ElectricItem.manager.charge(stack, 1000000, getSourceTier(), true, true) <= 0) {
                        moveToOutputSlot(slot, stack);
                    }
                }
            }
        }
    }
    
    private void moveToOutputSlot(InvSlot slot, ItemStack stack) {
        for (int i = 0; i < this.outputSlot.size(); i++) {
            if (this.outputSlot.isEmpty(i)) {
                slot.clear();
                this.outputSlot.put(i, stack);
                break;
            }
        }
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.world == null || isRedstonePowered() ? Util.allFacings : Util.noFacings;
    }

    @Override
    public ContainerChargeOMat getGuiContainer(EntityPlayer player) {
        return new ContainerChargeOMat(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiChargeOMat(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    @Override
    public int getSinkTier() {
        return GregTechMod.classic ? 4 : 5;
    }

    @Override
    public int getSourceTier() {
        return GregTechMod.classic ? 4 : 5;
    }

    @Override
    public int getEUCapacity() {
        return 10000000;
    }
}
