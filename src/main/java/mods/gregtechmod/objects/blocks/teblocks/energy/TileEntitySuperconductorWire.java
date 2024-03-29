package mods.gregtechmod.objects.blocks.teblocks.energy;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.block.state.UnlistedBooleanProperty;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TileEntitySuperconductorWire extends TileEntityEnergy {
    public static final IUnlistedProperty<Boolean> CONNECTED_DOWN = new UnlistedBooleanProperty("connected_down");
    public static final IUnlistedProperty<Boolean> CONNECTED_UP = new UnlistedBooleanProperty("connected_up");
    public static final IUnlistedProperty<Boolean> CONNECTED_NORTH = new UnlistedBooleanProperty("connected_north");
    public static final IUnlistedProperty<Boolean> CONNECTED_SOUTH = new UnlistedBooleanProperty("connected_south");
    public static final IUnlistedProperty<Boolean> CONNECTED_WEST = new UnlistedBooleanProperty("connected_west");
    public static final IUnlistedProperty<Boolean> CONNECTED_EAST = new UnlistedBooleanProperty("connected_east");

    private int connections;

    public TileEntitySuperconductorWire() {
        this.coverBlacklist.addAll(CoverType.VALUES);
    }

    @Override
    public double getMaxInputEUp() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double getMaxOutputEUp() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        updateConnections();
    }

    @Override
    protected void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        updateConnections();
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return new ConductorEnergy();
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return Util.allFacings;
    }

    @Override
    public int getEUCapacity() {
        return 0;
    }

    private void updateConnections() {
        if (!this.world.isRemote) {
            this.connections = Arrays.stream(EnumFacing.VALUES)
                .filter(this::isSideConnectable)
                .mapToInt(facing -> 1 << facing.getIndex())
                .reduce(0, (a, b) -> a | b);
            updateClientField("connections");
        }
    }

    private boolean isSideConnectable(EnumFacing side) {
        if (GregTechConfig.GENERAL.connectedTextures) {
            IEnergyTile energyTile = EnergyNet.instance.getTile(this.world, this.pos.offset(side));
            ConductorEnergy.DelegateConductor delegate = ((ConductorEnergy) this.energy).getDelegate();

            return energyTile instanceof IEnergyAcceptor && ((IEnergyAcceptor) energyTile).acceptsEnergyFrom(delegate, side.getOpposite())
                || energyTile instanceof IEnergyEmitter && ((IEnergyEmitter) energyTile).emitsEnergyTo(delegate, side.getOpposite());
        }
        return false;
    }

    private boolean isSideConnected(EnumFacing side) {
        return (this.connections & 1 << side.getIndex()) != 0;
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        return state.withProperty(CONNECTED_DOWN, isSideConnected(EnumFacing.DOWN))
            .withProperty(CONNECTED_UP, isSideConnected(EnumFacing.UP))
            .withProperty(CONNECTED_NORTH, isSideConnected(EnumFacing.NORTH))
            .withProperty(CONNECTED_SOUTH, isSideConnected(EnumFacing.SOUTH))
            .withProperty(CONNECTED_WEST, isSideConnected(EnumFacing.WEST))
            .withProperty(CONNECTED_EAST, isSideConnected(EnumFacing.EAST));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("connections");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("connections")) updateRender();
    }

    public class ConductorEnergy extends DynamicAdjustableEnergy {

        @Override
        protected AdjustableEnergy.DelegateBase createDelegate() {
            return new DelegateConductor();
        }

        private DelegateConductor getDelegate() {
            return (DelegateConductor) this.delegate;
        }

        private class DelegateConductor extends DelegateBase implements IEnergyConductor {

            @Override
            public double getConductionLoss() {
                return 0;
            }

            @Override
            public double getInsulationEnergyAbsorption() {
                return 8192;
            }

            @Override
            public double getInsulationBreakdownEnergy() {
                return Integer.MAX_VALUE;
            }

            @Override
            public double getConductorBreakdownEnergy() {
                return Integer.MAX_VALUE;
            }

            @Override
            public void removeInsulation() {
                markForExplosion(10);
            }

            @Override
            public void removeConductor() {
                markForExplosion(10);
            }

            @Override
            public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing facing) {
                return true;
            }

            @Override
            public boolean emitsEnergyTo(IEnergyAcceptor acceptor, EnumFacing facing) {
                return true;
            }
        }
    }
}
