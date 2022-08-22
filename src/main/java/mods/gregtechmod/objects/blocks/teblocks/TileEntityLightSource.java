package mods.gregtechmod.objects.blocks.teblocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityLightSource extends TileEntity implements ITickable {
    private int tickTimer = 0;

    @Override
    public void update() {
        if (!this.world.isRemote) {
            boolean remove = true;
            if (++tickTimer % 20 == 0) {
                for (EntityPlayer player : this.world.playerEntities) {
                    if (player.getPosition().up() == this.pos) remove = false;
                }

                if (remove) this.world.setBlockToAir(this.pos);
            }
        }
    }
}
