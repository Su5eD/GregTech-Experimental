package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public abstract class InventoryCover extends BaseCover {
    @NBTPersistent
    protected InventoryMode mode = InventoryMode.EXPORT;

    protected InventoryCover(CoverType type, Coverable be, Direction side, Item item) {
        super(type, be, side, item);
    }

    public boolean canWork() {
        return !(this.be instanceof IGregTechMachine machine) || !(this.mode.conditional && machine.isAllowedToWork() == this.mode.inverted);
    }

    protected boolean shouldUseEnergy(double minCapacity) {
        if (this.mode.consumesEnergy(this.side) && this.be instanceof IElectricMachine electric) {
            double capacity = this.be instanceof UpgradableBlockEntity upgradable ? upgradable.getUniversalEnergyCapacity() : electric.getEUCapacity();
            return capacity >= minCapacity;
        }
        return false;
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return CoverInteractionResult.UPDATE;
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
    public CoverCategory getCategory() {
        return CoverCategory.IO;
    }
}
