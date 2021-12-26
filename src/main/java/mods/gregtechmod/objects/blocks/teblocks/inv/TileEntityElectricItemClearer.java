package mods.gregtechmod.objects.blocks.teblocks.inv;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiElectricItemClearer;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class TileEntityElectricItemClearer extends TileEntityElectricBuffer {

    public TileEntityElectricItemClearer() {
        super(1);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        int overclockers = getUpgradeCount(IC2UpgradeType.OVERCLOCKER);
        if (isAllowedToWork() && this.buffer.isEmpty() && canUseEnergy(64 * (1 << overclockers)) && (workJustHasBeenEnabled() || this.tickCounter % 20 == 0 || this.success == 20)) {
            int offset = 2 + overclockers;
            int negOffset = 1 + overclockers;
            int rangeExt = 2 + overclockers * 2;
            BlockPos offsetPos = this.pos.offset(getFacing(), offset);
            BlockPos beginPos = offsetPos.add(-negOffset, -negOffset, -negOffset);
            BlockPos endPos = offsetPos.add(rangeExt, rangeExt, rangeExt);
            
            ItemStack stack = GtUtil.collectItemFromArea(this.world, beginPos, endPos);
            this.buffer.put(stack);
        }
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return IC2UpgradeType.DEFAULT;
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public ContainerElectricBufferSmall<?> getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBufferSmall<>(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricItemClearer(getGuiContainer(player));
    }
}
