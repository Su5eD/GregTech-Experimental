package mods.gregtechmod.objects.blocks.machines.tileentity;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.core.ConfigLoader;
import mods.gregtechmod.init.BlockItemLoader;
import mods.gregtechmod.objects.blocks.machines.tileentity.base.TileEntityDigitalChestBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileEntityQuantumChest extends TileEntityDigitalChestBase {

    public TileEntityQuantumChest() {
        super(ConfigLoader.quantumChestMaxItemCount, true);
    }

    public TileEntityQuantumChest(ItemStack storedItems, boolean isPrivate, @Nullable GameProfile owner) {
        super(ConfigLoader.quantumChestMaxItemCount, true);
        this.mainSlot.put(storedItems);
        if (isPrivate && owner != null) {
            this.isPrivate = true;
            this.owner = owner;
            this.upgradeSlot.put(new ItemStack((Item) BlockItemLoader.Upgrades.machine_lock.getInstance()));
        }
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
    }
}