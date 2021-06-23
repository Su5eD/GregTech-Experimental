package mods.gregtechmod.objects.blocks.teblocks.generator;

import ic2.core.block.BlockIC2Fence;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGenerator;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class TileEntityLightningRod extends TileEntityGenerator {
    // TODO add a recipe for this when the supercondensator is added
    public TileEntityLightningRod() {
        super("lightning_rod");
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (tickCounter % 256 == 0 && (world.isThundering() || world.isRaining() && GtUtil.RANDOM.nextInt(10) == 0)) {
            int rodValue = 0;
            boolean valid = true;
            
            int y = pos.getY();
            int height = world.getHeight();
            
            for (int i = 1; i < height - y - 1; i++) {
                Block block = world.getBlockState(pos.offset(EnumFacing.UP, i)).getBlock();
                
                if (valid && block instanceof BlockIC2Fence) rodValue++;
                else {
                    valid = false;
                    if (block != Blocks.AIR) {
                        rodValue = 0;
                        break;
                    }
                }
            }
            
            if (!world.isThundering() && y + rodValue < 128) rodValue = 0;
            
            if (GtUtil.RANDOM.nextInt(4096 * height) < rodValue * (y + rodValue)) {
                addEnergy(25000000);
                world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), y + rodValue, pos.getZ(), false));
            }
        }
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return AdjustableEnergy.createSource(this, 100000000, 5, 8192, getSourceSides());
    }

    @Override
    protected boolean enableMachineSafety() {
        return false;
    }
}
