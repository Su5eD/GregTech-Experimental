package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IElectricMachine;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CoverValve extends CoverPump {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("valve");

    public CoverValve(ResourceLocation name, ICoverable te, Direction side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (canWork()) {
            if (shouldUseEnergy(128) && ((IElectricMachine) this.be).canUseEnergy(128)) {
                ((IElectricMachine) this.be).useEnergy(CoverConveyor.moveItemStack((BlockEntity) this.be, this.side, this.mode));
            }

            super.doCoverThings();
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
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
    public int getTickRate() {
        return 2;
    }
}
