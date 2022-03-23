package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedIntegerProperty;
import ic2.core.util.Util;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.inventory.invslot.GtSlot;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityMachineBox extends TileEntityUpgradable {
    public static final IUnlistedProperty<Integer> MACHINE_TIER_PROPERTY = new UnlistedIntegerProperty("machineTier");
    
    private int machineTier = getBaseSinkTier();

    public TileEntityMachineBox() {
        new GtSlot(this, "content", InvSlot.Access.IO, 1);
        this.fluids.addTank(new GtFluidTank(this, "fluidContent", Util.allFacings, Util.allFacings, JavaUtil.alwaysTrue(), 1000));
    }

    @Override
    protected void onUpdateGTUpgrade(IGtUpgradeItem item, EntityPlayer player) {
        super.onUpdateGTUpgrade(item, player);
        
        if (item.getType() == GtUpgradeType.TRANSFORMER) incrementMachineTier();
    }

    @Override
    protected void onUpdateIC2Upgrade(IC2UpgradeType type) {
        super.onUpdateIC2Upgrade(type);
        
        if (type == IC2UpgradeType.TRANSFORMER) incrementMachineTier();
    }
    
    private void incrementMachineTier() {
        this.machineTier++;
        updateClientField("machineTier");
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        return super.getExtendedState(state)
            .withProperty(MACHINE_TIER_PROPERTY, this.machineTier);
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }

    @Override
    public int getBaseSourceTier() {
        return 1;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return allSidesExceptFacing();
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return facingSideOnly();
    }

    @Override
    public long getMjCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    public int getSteamCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    protected int getMinimumStoredEU() {
        return 1000;
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("machineTier");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("machineTier")) rerender();
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }
}
