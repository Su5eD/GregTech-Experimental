package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.List;

public class CoilHandler extends GtComponentBase {
    @NBTPersistent
    public int heatingCoilTier;
    private final int coilCount;
    private final Runnable onUpdate;
    
    public CoilHandler(TileEntityBlock parent, int coilCount, Runnable onUpdate) {
        super(parent);
        this.coilCount = coilCount;
        this.onUpdate = onUpdate;
    }

    @Override
    public void onLoaded() {
        this.onUpdate.run();
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!parent.getWorld().isRemote) {
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
                    onUpdate.run();
                    return true;
                }
            }
        }

        return false;
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
