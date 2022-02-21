package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Locale;

public class RedstoneConductorCover extends GenericCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("redstone_conductor");

    @NBTPersistent
    protected ConductorMode mode = ConductorMode.STRONGEST;

    public RedstoneConductorCover(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (this.be instanceof IGregTechMachine machine) {
            BlockPos pos = ((BlockEntity) this.be).getBlockPos();
            Level level = ((BlockEntity) this.be).getLevel();

            if (mode == ConductorMode.STRONGEST) {
                int strongest = GtUtil.getStrongestSignal(this.be, level, pos, this.side);
                machine.setRedstoneOutput(this.side, strongest);
            }
            else {
                Direction side = Direction.from3DDataValue(this.mode.ordinal() - 1);
                machine.setRedstoneOutput(this.side, GtUtil.getSignalFromSide(side, level, pos, this.be) - 1);
            }
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
    public boolean onScrewdriverClick(Player player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return true;
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
    public CoverType getType() {
        return CoverType.UTIL;
    }
}
