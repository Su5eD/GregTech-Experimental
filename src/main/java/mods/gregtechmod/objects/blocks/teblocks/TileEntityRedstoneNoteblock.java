package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import one.util.streamex.EntryStream;

import java.util.Map;

public class TileEntityRedstoneNoteblock extends TileEntityCoverBehavior {
    private static final Map<EnumFacing, Block> SOUNDS = EntryStream.of(
        EnumFacing.DOWN, Blocks.IRON_BLOCK,
        EnumFacing.UP, Blocks.GOLD_BLOCK,
        EnumFacing.NORTH, Blocks.STONE,
        EnumFacing.SOUTH, Blocks.SAND,
        EnumFacing.WEST, Blocks.GLASS,
        EnumFacing.EAST, Blocks.LOG
    )
        .toImmutableMap();
    
    private int redstoneStrength;
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (isAllowedToWork() && this.tickCounter % 20 == 0) {
            this.redstoneStrength = GtUtil.getStrongestRedstone(this, this.world, this.pos, null);
            if (this.redstoneStrength > 0) {
                doSound(this.redstoneStrength - 1);
                
                updateClientField("redstoneStrength");
            }
        }
    }
    
    public void doSound(int strength) {
        Block block = SOUNDS.get(getFacing());
        ItemStack stack = new ItemStack(block, 1 + (int) (strength * 1.714));
        TileEntitySonictron.doSonictronSound(stack, this.world, this.pos, 3);
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("redstoneStrength")) {
            this.world.spawnParticle(EnumParticleTypes.NOTE, this.pos.getX() + 0.5, this.pos.getY() + 1.2, this.pos.getZ() + 0.5, this.redstoneStrength / 24D, 0, 0);
        }
    }
}
