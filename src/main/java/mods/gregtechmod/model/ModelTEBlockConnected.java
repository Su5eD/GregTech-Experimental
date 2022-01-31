package mods.gregtechmod.model;

import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntitySuperconductorWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashMap;
import java.util.Map;

public class ModelTEBlockConnected extends ModelBlockConnected {

    @SafeVarargs
    public ModelTEBlockConnected(Map<String, ResourceLocation> mainTextures, Map<String, ResourceLocation>... extras) {
        super(false, mainTextures, extras);
    }

    @Override
    protected Map<EnumFacing, Boolean> getConnections(IBlockState rawState) {
        Map<EnumFacing, Boolean> connections = new HashMap<>();
        Ic2BlockStateInstance state = (Ic2BlockStateInstance) rawState;
        connections.put(EnumFacing.DOWN, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_DOWN));
        connections.put(EnumFacing.UP, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_UP));
        connections.put(EnumFacing.NORTH, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_NORTH));
        connections.put(EnumFacing.SOUTH, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_SOUTH));
        connections.put(EnumFacing.WEST, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_WEST));
        connections.put(EnumFacing.EAST, getOrDefault(state, TileEntitySuperconductorWire.CONNECTED_EAST));
        return connections;
    }
    
    private static Boolean getOrDefault(Ic2BlockStateInstance state, IUnlistedProperty<Boolean> property) {
        Boolean value = state.getValue(property);
        return value != null ? value : false;
    }
}
