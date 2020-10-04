package mods.gregtechmod.objects.blocks.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.Iterator;

public class TileEntityLightSource extends TileEntity implements ITickable {
    private int tickTimer = 0;

    @Override
    public void update() {
        if (!this.world.isRemote) {
             boolean temp = true;
             if (++tickTimer%20 == 0) {
                 Iterator<EntityPlayer> iterator = this.world.playerEntities.iterator();
                 while (iterator.hasNext() && temp) {
                     EntityPlayer player = iterator.next();
                     if (player.getPosition().up() == this.pos) temp = false;
                 }
                 if (temp) this.world.setBlockToAir(this.pos);
             }
        }
    }
}
