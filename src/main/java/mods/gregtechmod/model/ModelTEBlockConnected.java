package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntitySuperconductorWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;
import one.util.streamex.EntryStream;

import java.util.Map;

public class ModelTEBlockConnected extends ModelBlockConnected {

    @SafeVarargs
    public ModelTEBlockConnected(Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        super(null, mainTextures, extras);
    }

    @Override
    protected Map<EnumFacing, Boolean> getConnections(IBlockState rawState) {
        Ic2BlockStateInstance state = (Ic2BlockStateInstance) rawState;
        return EntryStream.of(
            EnumFacing.DOWN, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_DOWN),
            EnumFacing.UP, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_UP),
            EnumFacing.NORTH, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_NORTH),
            EnumFacing.SOUTH, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_SOUTH),
            EnumFacing.WEST, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_WEST),
            EnumFacing.EAST, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_EAST)
        )
            .toImmutableMap();
    }

    private static Boolean getOrDefault(Ic2BlockStateInstance state, IUnlistedProperty<Boolean> property) {
        Boolean value = state.getValue(property);
        return Boolean.TRUE.equals(value);
    }
}
