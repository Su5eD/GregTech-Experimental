package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.machine.IUpgradableMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class CoverInventory extends CoverGeneric {
    @NBTPersistent
    protected InventoryMode mode = InventoryMode.EXPORT;

    public CoverInventory(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
    }

    public boolean canWork() {
        return !(this.be instanceof IGregTechMachine machine) || !(this.mode.conditional && machine.isAllowedToWork() == this.mode.inverted);
    }

    protected boolean shouldUseEnergy(double minCapacity) {
        if (this.mode.consumesEnergy(this.side) && this.be instanceof IElectricMachine electric) {
            double capacity = this.be instanceof IUpgradableMachine upgradable ? upgradable.getUniversalEnergyCapacity() : electric.getEUCapacity();
            return capacity >= minCapacity;
        }
        return false;
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return true;
    }

    @Override
    public boolean letsItemsIn() {
        return this.mode.allowsInput();
    }

    @Override
    public boolean letsItemsOut() {
        return this.mode.allowsOutput();
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
