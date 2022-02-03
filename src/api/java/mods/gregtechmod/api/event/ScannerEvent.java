package mods.gregtechmod.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.List;

/**
 * Collects additional information for the scanner.
 * This event is <b>cancelable</b>.
 */
@Cancelable
public class ScannerEvent extends WorldEvent {
    public final Player player;
    public final int scanLevel;
    public final BlockPos pos;
    public final List<String> list;
    public final Direction side;
    public final float hitX, hitY, hitZ;
    public final BlockEntity tileEntity;
    public final Block block;

    public int euCost = 0;

    public ScannerEvent(Level world, Player player, BlockPos pos, Direction side, int scanLevel, Block block, BlockEntity tileEntity, List<String> list, float hitX, float hitY, float hitZ) {
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
