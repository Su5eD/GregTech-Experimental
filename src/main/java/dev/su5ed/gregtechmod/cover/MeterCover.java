package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Locale;

public abstract class MeterCover extends BaseCover {
    @NBTPersistent
    protected MeterMode mode = MeterMode.NORMAL;

    protected MeterCover(ResourceLocation name, ICoverable be, Direction side, Item item) {
        super(name, be, side, item);
    }

    @Override
    public void doCoverThings() {
        if (this.be instanceof IGregTechMachine machine) {
            int strength = getRedstoneStrength();
            machine.setRedstoneOutput(this.side, this.mode == MeterMode.NORMAL ? strength : 15 - strength);
        }
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

    public abstract int getRedstoneStrength();

    public enum MeterMode {
        NORMAL,
        INVERTED;

        public static final MeterMode[] VALUES = values();

        public MeterMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "mode", name().toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public void onCoverRemove() {
        if (this.be instanceof IGregTechMachine machine) machine.setRedstoneOutput(this.side, 0);
    }

    @Override
    public int getTickRate() {
        return 5;
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
    public boolean overrideRedstoneOut() {
        return true;
    }

    @Override
    public CoverType getType() {
        return CoverType.METER;
    }
}
