package mods.gregtechmod.objects.blocks.teblocks;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityDigitalChestBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityQuantumChest extends TileEntityDigitalChestBase {

    @SuppressWarnings("unused")
    public TileEntityQuantumChest() {
        super(GregTechConfig.FEATURES.quantumChestMaxItemCount, true);
    }

    public TileEntityQuantumChest(ItemStack storedItems, boolean isPrivate, @Nullable GameProfile owner) {
        super(GregTechConfig.FEATURES.quantumChestMaxItemCount, true);
        this.content.put(storedItems);
        if (isPrivate && owner != null) {
            this.isPrivate = true;
            this.owner = owner;
            this.upgradeSlot.put(new ItemStack(BlockItems.Upgrade.MACHINE_LOCK.getInstance()));
        }
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(GtUtil.translateTeBlockDescription("quantum_chest"));
    }
}
