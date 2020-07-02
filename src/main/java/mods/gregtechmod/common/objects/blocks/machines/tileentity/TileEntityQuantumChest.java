package mods.gregtechmod.common.objects.blocks.machines.tileentity;

import com.mojang.authlib.GameProfile;
import ic2.core.block.state.BlockStateUtil;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.model.ModelUtil;
import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.base.TileEntityDigitalChestBase;
import mods.gregtechmod.common.util.GtProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

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
        }
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
    }

    //TODO: Move this to translocator class (doesn't exist yet)
    /*@Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        HashMap<EnumFacing, ResourceLocation> overrides = new HashMap<>();

        if (getFacing() == EnumFacing.WEST) {
            //overrides.put(EnumFacing.WEST, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_right"));
            overrides.put(EnumFacing.UP, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_right"));
            //overrides.put(EnumFacing.DOWN, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_left"));
        }
        else if (getFacing() == EnumFacing.EAST) {
            //overrides.put(EnumFacing.SOUTH, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_left"));
            overrides.put(EnumFacing.UP, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_left"));
            //overrides.put(EnumFacing.DOWN, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_right"));
        }
        else if (getFacing() == EnumFacing.SOUTH) {
            overrides.put(EnumFacing.UP, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_up"));
            overrides.put(EnumFacing.DOWN, new ResourceLocation(GregtechMod.MODID, "blocks/machines/translocator/translocator_up"));
        }

        return state.withProperty(GtProperties.TEXTURE_OVERRIDE_PROPERTY, new GtProperties.TextureOverride(overrides)).withProperty(GtProperties.UV_LOCK_PROPERTY, true);
    }*/
}