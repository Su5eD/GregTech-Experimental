package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverRedstoneSignalizer extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("redstone_signalizer");
    
    @NBTPersistent
    protected int signal;

    public CoverRedstoneSignalizer(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        this.signal = (this.signal + 1) % 15;
        GtUtil.sendMessage(player, GtLocale.buildKey("cover", "signal"), this.signal);
        return true;
    }

    @Override
    public int getRedstoneInput() {
        return this.signal;
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public CoverType getType() {
        return CoverType.METER;
    }
}
