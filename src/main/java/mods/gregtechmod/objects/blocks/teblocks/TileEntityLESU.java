package mods.gregtechmod.objects.blocks.teblocks;

import ic2.api.energy.EnergyNet;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.util.Util;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiLESU;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerLESU;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class TileEntityLESU extends TileEntityEnergy implements IHasGui {
    private boolean notify = true;
    private boolean init;
    private int storage = 2000000000;
    
    public final InvSlotCharge chargeSlot;
    public final InvSlotDischarge dischargeSlot;

    public TileEntityLESU() {
        super(null);
        
        this.chargeSlot = new InvSlotCharge(this, 1);
        this.energy.addChargingSlot(this.chargeSlot);
        
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.IO, 1, false, InvSlot.InvSide.NOTSIDE);
        this.energy.addDischargingSlot(this.dischargeSlot);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.init = true;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (this.notify || this.init) {
            this.storage = 1000000;
            if (stepToFindOrCallLESUController(this.world, this.pos, new ArrayList<>()) < 2) {
                this.storage = Math.min(2000000000, 1000000 * stepToGetLESUAmount(this.pos, new ArrayList<>()));
            }
            this.notify = this.init = false;
            IC2.network.get(true).updateTileEntityField(this, "storage");
            updateChargeTier();
            IC2.network.get(true).initiateTileEntityEvent(this, 0, true);
            rerender();
            this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, true);
        }
    }
    
    private void updateChargeTier() {
        int tier = getSourceTier();
        this.chargeSlot.setTier(tier);
        this.dischargeSlot.setTier(tier);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("storage");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("storage")) updateChargeTier();
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        Set<EnumFacing> sides = new HashSet<>(Util.allFacings);
        sides.remove(getFacing());
        return sides;
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return Collections.singleton(getFacing());
    }

    @Override
    public int getEUCapacity() {
        return this.storage;
    }

    @Override
    public double getMaxInputEUp() {
        return EnergyNet.instance.getPowerFromTier(Math.min(getSourceTier(), 3));
    }

    @Override
    public double getMaxOutputEUp() {
        return Math.min(Math.max(this.storage / 1000000 + 4, 1), 512);
    }

    @Override
    public int getSinkTier() {
        return EnergyNet.instance.getTierFromPower(getMaxInputEUp());
    }

    @Override
    public int getSourceTier() {
        return EnergyNet.instance.getTierFromPower(getMaxOutputEUp());
    }
    
    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        int tier = getSourceTier();
        String tierName = tier <= 1 ? "lv" : tier == 2 ? "mv" : "hv";
        return super.getExtendedState(state)
                .withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new PropertyHelper.TextureOverride(EnumFacing.NORTH, new ResourceLocation(Reference.MODID, "blocks/machines/lesu/lesu_" + tierName + "_out")));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.storage = nbt.getInteger("storage");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("storage", this.storage);
        return super.writeToNBT(nbt);
    }

    @Override
    public ContainerLESU getGuiContainer(EntityPlayer player) {
        return new ContainerLESU(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiLESU(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public static int stepToFindOrCallLESUController(World world, BlockPos pos, List<BlockPos> list) {
        list.add(pos);
        int controllerCount = 0;
        if (isLESUController(world, pos)) {
            TileEntityLESU te = (TileEntityLESU) world.getTileEntity(pos);
            if (te != null) {
                te.notify = true;
                ++controllerCount;
            }
        }
        
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos offset = pos.offset(facing);
            if (isLESUBlock(world, offset) && !list.contains(offset)) {
                controllerCount += stepToFindOrCallLESUController(world, offset, list);
            }
        }
        
        return controllerCount;
    }

    public int stepToGetLESUAmount(BlockPos pos, List<BlockPos> list) {
        list.add(pos);
        
        int storageAmount = 1;
        
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos offset = pos.offset(facing);
            if (isLESUStorage(world, offset) && !list.contains(offset)) {
                storageAmount += stepToGetLESUAmount(offset, list);
            }
        }
        
        return storageAmount;
    }

    public static boolean isLESUBlock(World world, BlockPos pos) {
        return isLESUStorage(world, pos) || isLESUController(world, pos);
    }

    public static boolean isLESUStorage(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == BlockItems.Block.LESUBLOCK.getInstance();
    }

    public static boolean isLESUController(World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof TileEntityLESU;
    }
}
