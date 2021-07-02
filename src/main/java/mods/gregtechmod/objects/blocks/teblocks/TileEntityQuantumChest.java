package mods.gregtechmod.objects.blocks.teblocks;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityDigitalChestBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileEntityQuantumChest extends TileEntityDigitalChestBase {

    @SuppressWarnings("unused")
    public TileEntityQuantumChest() {
        super("quantum_chest", GregTechConfig.FEATURES.quantumChestMaxItemCount, true);
    }

    public TileEntityQuantumChest(ItemStack storedItems, boolean isPrivate, @Nullable GameProfile owner) {
        super("quantum_chest", GregTechConfig.FEATURES.quantumChestMaxItemCount, true);
        this.content.put(storedItems);
        if (isPrivate && owner != null) {
            this.isPrivate = true;
            this.owner = owner;
            this.upgradeSlot.put(new ItemStack(BlockItems.Upgrade.MACHINE_LOCK.getInstance()));
        }
    }
}
