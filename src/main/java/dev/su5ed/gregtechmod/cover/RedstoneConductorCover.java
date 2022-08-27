package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.cover.CoverInteractionResult;
import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Locale;

public class RedstoneConductorCover extends BaseCover<IGregTechMachine> {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("redstone_conductor");

    protected ConductorMode mode = ConductorMode.STRONGEST;

    public RedstoneConductorCover(CoverType<IGregTechMachine> type, IGregTechMachine be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void tick() {
        BlockPos pos = ((BlockEntity) this.be).getBlockPos();
        Level level = ((BlockEntity) this.be).getLevel();

        if (this.mode == ConductorMode.STRONGEST) {
            int strongest = GtUtil.getStrongestSignal((BlockEntity) this.be, level, pos, this.side);
            this.be.setRedstoneOutput(this.side, strongest);
        }
        else {
            Direction side = Direction.from3DDataValue(this.mode.ordinal() - 1);
            this.be.setRedstoneOutput(this.side, GtUtil.getSignalFromSide(side, level, pos, (BlockEntity) this.be) - 1);
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return CoverInteractionResult.CHANGED;
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
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);
        tag.putEnum("mode", this.mode);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);
        this.mode = tag.getEnum("mode");
    }

    private enum ConductorMode {
        STRONGEST,
        DOWN,
        UP,
        NORTH,
        SOUTH,
        WEST,
        EAST;

        private static final ConductorMode[] VALUES = values();

        public ConductorMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "conductor_mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
