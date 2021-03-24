package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityDigitalChestBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileEntityDigitalChest extends TileEntityDigitalChestBase {

    public TileEntityDigitalChest() {
        super(GregTechConfig.FEATURES.digitalChestMaxItemCount, false);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(GtUtil.translateTeBlockDescription("digital_chest"));
    }
}
