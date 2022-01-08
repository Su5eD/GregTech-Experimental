package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import ic2.core.network.GrowingBuffer;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;

public class CoilHandler extends GtComponentBase {
    @NBTPersistent
    public int heatingCoilTier;
    private final int coilCount;
    
    public CoilHandler(TileEntityBlock parent, int coilCount) {
        super(parent);
        this.coilCount = coilCount;
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!this.parent.getWorld().isRemote) {
            ItemStack stack = player.inventory.getCurrentItem();
            Item item = stack.getItem();
            boolean temp = false;

            if (player.capabilities.isCreativeMode || stack.getCount() >= this.coilCount) {
                if (this.heatingCoilTier <= 0 && item == BlockItems.Component.COIL_KANTHAL.getInstance()) {
                    temp = true;
                    this.heatingCoilTier = 1;
                }
                if (this.heatingCoilTier == 1 && item == BlockItems.Component.COIL_NICHROME.getInstance()) {
                    temp = true;
                    this.heatingCoilTier = 2;
                }

                if (temp) {
                    if (!player.capabilities.isCreativeMode) stack.shrink(this.coilCount);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buf = new GrowingBuffer(8);
        buf.writeInt(this.heatingCoilTier);
        buf.flip();

        setNetworkUpdate(player, buf);
    }

    @Override
    public void onNetworkUpdate(DataInput in) throws IOException {
        this.heatingCoilTier = in.readInt();
    }
    
    public int getMaxProgress(int maxProgress) {
        if (this.heatingCoilTier > 0) return maxProgress / this.heatingCoilTier;
        return maxProgress;
    }
    
    public List<ItemStack> addDrops(List<ItemStack> list) {
        if (this.heatingCoilTier > 0) list.add(new ItemStack(BlockItems.Component.COIL_KANTHAL.getInstance(), coilCount));
        if (this.heatingCoilTier > 1) list.add(new ItemStack(BlockItems.Component.COIL_NICHROME.getInstance(), coilCount));
        return list;
    }
}
