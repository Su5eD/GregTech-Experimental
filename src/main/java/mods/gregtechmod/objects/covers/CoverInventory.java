package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public abstract class CoverInventory extends CoverBase {
    @NBTPersistent
    protected InventoryMode mode = InventoryMode.EXPORT;

    public CoverInventory(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    public boolean canWork() {
        if (this.te instanceof IGregTechMachine) {
            return !(mode.conditional && ((IGregTechMachine) te).isAllowedToWork() == mode.inverted);
        }
        return true;
    }

    protected boolean shouldUseEnergy(double minCapacity) {
        if (this.mode.consumesEnergy(side) && te instanceof IElectricMachine) {
            double capacity = te instanceof IUpgradableMachine ? ((IUpgradableMachine) te).getUniversalEnergyCapacity() : ((IElectricMachine) te).getEUCapacity();
            return capacity >= minCapacity;
        }
        return false;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    @Override
    public boolean letsItemsIn() {
        return mode.allowsInput();
    }

    @Override
    public boolean letsItemsOut() {
        return mode.allowsOutput();
    }

    @Override
    public boolean allowEnergyTransfer() {
        return true;
    }

    @Override
    public boolean letsRedstoneIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneOut() {
        return true;
    }

    @Override
    public CoverType getType() {
        return CoverType.IO;
    }
}
