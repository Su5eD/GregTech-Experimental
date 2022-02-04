package mods.gregtechmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Code taken from McJtyLib's <a href="https://github.com/McJtyMods/McJtyLib/blob/1.12/src/main/java/mcjty/lib/varia/McJtyLibTeleporter.java">McJtyLibTeleporter</a> and
 * <a href="https://github.com/McJtyMods/McJtyLib/blob/1.12/src/main/java/mcjty/lib/varia/TeleportationTools.java">TeleportationTools</a>
 *
 * @author McJty
 */
public class TeleportUtil {

    public static void performTeleport(Entity entity, DimensionType dimension, double x, double y, double z) {
        DimensionType oldDim = entity.getEntityWorld().provider.getDimensionType();

        if (oldDim != dimension) teleportToDimension(entity, dimension, x, y, z);
        else entity.setPositionAndUpdate(x, y, z);
    }

    public static void teleportToDimension(Entity entity, DimensionType dimension, double x, double y, double z) {
        MinecraftServer server = entity.getEntityWorld().getMinecraftServer();
        if (server != null) {
            int dimId = dimension.getId();
            WorldServer worldServer = server.getWorld(dimId);

            entity.changeDimension(dimId, new GtTeleporter(worldServer, x, y, z));
            entity.setPositionAndUpdate(x, y, z);
        }
    }

    private static class GtTeleporter extends Teleporter {
        private final double x, y, z;

        public GtTeleporter(WorldServer world, double x, double y, double z) {
            super(world);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void placeEntity(World world, Entity entity, float rotationYaw) {
            placeInPortal(entity, rotationYaw);
        }

        @Override
        public void placeInPortal(Entity entity, float rotationYaw) {
            this.world.getBlockState(new BlockPos(this.x, this.y, this.z)); //dummy load to maybe gen chunk

            entity.setPosition(this.x, this.y, this.z);
            entity.motionX = entity.motionY = entity.motionZ = 0;
        }
    }
}
