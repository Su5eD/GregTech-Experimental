package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.api.cover.CoverInteractionResult;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.network.FieldUpdateListener;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NormalCover extends BaseCover implements FieldUpdateListener {
    public static final ResourceLocation TEXTURE_NORMAL = GtUtil.getCoverTexture("normal");
    public static final ResourceLocation TEXTURE_NOREDSTONE = GtUtil.getCoverTexture("noredstone");

    @Networked
    protected MeterCover.MeterMode mode = MeterCover.MeterMode.NORMAL;

    public NormalCover(CoverType type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return this.mode == MeterCover.MeterMode.NORMAL ? TEXTURE_NORMAL : TEXTURE_NOREDSTONE;
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.mode = this.mode.next();
        return CoverInteractionResult.RERENDER;
    }

    @Override
    public boolean letsRedstoneIn() {
        return this.mode == MeterCover.MeterMode.NORMAL;
    }

    @Override
    public boolean letsRedstoneOut() {
        return this.mode == MeterCover.MeterMode.NORMAL;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsLiquidsIn() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsLiquidsOut() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsItemsIn() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsItemsOut() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public void onFieldUpdate(String name) {
        if (name.equals("mode")) GtUtil.updateRender(this.be);
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
}
