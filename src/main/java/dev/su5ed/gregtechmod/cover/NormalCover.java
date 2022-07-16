package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.api.util.NBTTarget;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.FieldUpdateListener;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class NormalCover extends BaseCover implements FieldUpdateListener {
    public static final ResourceLocation TEXTURE_NORMAL = GtUtil.getCoverTexture("normal");
    public static final ResourceLocation TEXTURE_NOREDSTONE = GtUtil.getCoverTexture("noredstone");

    @NBTPersistent(target = NBTTarget.BOTH)
    protected MeterCover.MeterMode mode = MeterCover.MeterMode.NORMAL;

    public NormalCover(CoverType type, ICoverable be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return this.mode == MeterCover.MeterMode.NORMAL ? TEXTURE_NORMAL : TEXTURE_NOREDSTONE;
    }

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.GENERIC;
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.mode = this.mode.next();
        return CoverInteractionResult.UPDATE;
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
        if (name.equals("mode")) this.be.updateRenderClient();
    }
}
