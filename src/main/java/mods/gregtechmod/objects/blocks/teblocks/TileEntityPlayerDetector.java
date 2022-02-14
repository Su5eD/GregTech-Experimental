package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.util.Util;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.GtRedstoneEmitter;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.Collection;
import java.util.Locale;
import java.util.function.BiPredicate;

public class TileEntityPlayerDetector extends TileEntityEnergy {
    @NBTPersistent
    private Mode mode = Mode.DETECT_ALL;
    private final GtRedstoneEmitter emitter;

    public TileEntityPlayerDetector() {
        this.emitter = addComponent(new GtRedstoneEmitter(this, () -> {}));
        setPrivate(true);
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean ret = super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
        if (!ret) {
            this.mode = this.mode.next();
            String key = GtLocale.buildKeyTeBlock(this, "mode", this.mode.name().toLowerCase(Locale.ROOT));
            GtUtil.sendMessage(player, key);
        }
        return true;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (this.tickCounter % 20 == 0) {
            setActive(false);
            int strength = 0;
            if (tryUseEnergy(50)) {
                for (EntityPlayer player : this.world.playerEntities) {
                    int dist = Math.max(1, (int) player.getDistance(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5));
                    if (dist < 16 && this.mode.predicate.test(this, player)) {
                        setActive(true);
                        strength = 16 - dist;
                        break;
                    }
                }
            }
            this.emitter.setLevel(strength);
        }
    }

    @Override
    protected int getWeakPower(EnumFacing side) {
        return this.emitter.getLevel();
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public int getEUCapacity() {
        return 10000;
    }

    private enum Mode {
        DETECT_ALL(JavaUtil.alwaysTrueBi()),
        DETECT_OWNER(TileEntityCoverBehavior::checkAccess),
        DETECT_NON_OWNER((te, player) -> !te.checkAccess(player));

        public final BiPredicate<TileEntityPlayerDetector, EntityPlayer> predicate;

        private static final Mode[] VALUES = values();

        Mode(BiPredicate<TileEntityPlayerDetector, EntityPlayer> predicate) {
            this.predicate = predicate;
        }

        public Mode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }
    }
}
