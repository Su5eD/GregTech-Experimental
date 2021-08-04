package mods.gregtechmod.api.event;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

import java.util.ArrayList;

/**
 * Collects additional information for the scanner.
 * This event is <b>cancellable</b>.
 */
@Cancelable
public class ScannerEvent extends WorldEvent {
    public final EntityPlayer player;
    public final int scanLevel;
    public final BlockPos pos;
    public final ArrayList<String> list;
    public final EnumFacing side;
    public final float hitX, hitY, hitZ;
    public final TileEntity tileEntity;
    public final Block block;

    public int euCost = 0;

    public ScannerEvent(World world, EntityPlayer player, BlockPos pos, EnumFacing side, int scanLevel, Block block, TileEntity tileEntity, ArrayList<String> list, float hitX, float hitY, float hitZ) {
        super(world);
        this.player = player;
        this.scanLevel = scanLevel;
        this.tileEntity = tileEntity;
        this.block = block;
        this.list = list;
        this.side = side;
        this.pos = pos;
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitZ = hitZ;
    }
}
