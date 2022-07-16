package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Locale;

public class MachineControllerCover extends BaseCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("machine_controller");

    @NBTPersistent
    protected ControllerMode mode = ControllerMode.NORMAL;

    public MachineControllerCover(CoverType type, Coverable be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void doCoverThings() {
        if (this.be instanceof IGregTechMachine machine) {
            Level level = ((BlockEntity) this.be).getLevel();
            BlockPos offset = ((BlockEntity) this.be).getBlockPos().relative(this.side);
            boolean isPowered = level.hasNeighborSignal(offset) || level.hasSignal(offset, this.side);
            machine.setAllowedToWork(isPowered == (this.mode == ControllerMode.NORMAL) && this.mode != ControllerMode.DISABLED);
        }
    }

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.CONTROLLER;
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
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

    private enum ControllerMode {
        NORMAL,
        INVERTED,
        DISABLED;

        private static final ControllerMode[] VALUES = values();

        public ControllerMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "mode", name().toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public void onCoverRemove() {
        if (this.be instanceof IGregTechMachine machine) machine.setAllowedToWork(true);
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn() {
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }
}
