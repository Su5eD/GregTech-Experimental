package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverValve extends CoverPump {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("valve");

    public CoverValve(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (canWork()) {
            if (shouldUseEnergy(128)) {
                if (((IElectricMachine) te).canUseEnergy(128)) ((IElectricMachine) te).useEnergy(CoverConveyor.moveItemStack((TileEntity)te, side, mode));
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
        return mode.allowsInput();
    }

    @Override
    public boolean letsItemsOut() {
        return mode.allowsOutput();
    }

    @Override
    public int getTickRate() {
        return 2;
    }
}
