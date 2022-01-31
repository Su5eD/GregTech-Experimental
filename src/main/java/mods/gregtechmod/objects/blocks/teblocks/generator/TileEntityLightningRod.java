package mods.gregtechmod.objects.blocks.teblocks.generator;

import ic2.core.block.BlockIC2Fence;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGenerator;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class TileEntityLightningRod extends TileEntityGenerator {

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (this.tickCounter % 256 == 0 && (this.world.isThundering() || this.world.isRaining() && this.world.rand.nextInt(10) == 0)) {
            int rodValue = 0;
            boolean valid = true;
            
            int y = this.pos.getY();
            int height = this.world.getHeight();
            
            for (int i = 1; i < height - y - 1; i++) {
                Block block = this.world.getBlockState(this.pos.offset(EnumFacing.UP, i)).getBlock();
                
                if (valid && block instanceof BlockIC2Fence) rodValue++;
                else {
                    valid = false;
                    if (block != Blocks.AIR) {
                        rodValue = 0;
                        break;
                    }
                }
            }
            
            if (!this.world.isThundering() && y + rodValue < 128) rodValue = 0;
            
            if (this.world.rand.nextInt(4096 * height) < rodValue * (y + rodValue)) {
                addEnergy(25000000);
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.pos.getX(), y + rodValue, this.pos.getZ(), false));
            }
        }
    }

    @Override
    public int getBaseSourceTier() {
        return 5;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 100000000;
    }

    @Override
    protected boolean enableMachineSafety() {
        return false;
    }
}
