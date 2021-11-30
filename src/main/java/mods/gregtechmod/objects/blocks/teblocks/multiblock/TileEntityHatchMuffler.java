package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.block.TileEntityBlock;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileEntityHatchMuffler extends TileEntityBlock {
    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GtLocale.translateTeBlockDescription("hatch_muffler"));
    }
    
    public boolean polluteEnvironment() {
        return GtUtil.isAir(world, pos.offset(getFacing()));
    }
}
