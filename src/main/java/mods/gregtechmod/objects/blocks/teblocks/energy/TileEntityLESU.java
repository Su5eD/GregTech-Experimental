package mods.gregtechmod.objects.blocks.teblocks.energy;

import ic2.api.energy.EnergyNet;
import ic2.core.IC2;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiLESU;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityLESU extends TileEntityChargerBase {
    private boolean notify = true;
    private boolean init;
    @NBTPersistent
    private int storage = 1000000;

    public TileEntityLESU() {
        super(null);
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
            if (stepToFindOrCallLESUController(this.world, this.pos, new ArrayList<>()) < 2) {
                this.storage = 1000000 * stepToGetLESUAmount(this.pos, new ArrayList<>());
            } else this.storage = 1000000;
            
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
    public int getBaseSinkTier() {
        return Math.max(1, EnergyNet.instance.getTierFromPower(getMaxInputEUp()));
    }

    @Override
    protected int getBaseEUCapacity() {
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
    public int getSourceTier() {
        return Math.max(1, EnergyNet.instance.getTierFromPower(getMaxOutputEUp()));
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiLESU(getGuiContainer(player));
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        int tier = getSourceTier();
        String tierName = tier == 1 ? "lv" : tier == 2 ? "mv" : "hv";
        return super.getExtendedState(state)
                .withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new PropertyHelper.TextureOverride(EnumFacing.NORTH, new ResourceLocation(Reference.MODID, "blocks/machines/lesu/lesu_" + tierName + "_out")));
    }

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
        controllerCount += Arrays.stream(EnumFacing.VALUES)
                .map(pos::offset)
                .filter(offset -> isLESUBlock(world, offset) && !list.contains(offset))
                .mapToInt(offset -> stepToFindOrCallLESUController(world, offset, list))
                .sum();
        return controllerCount;
    }

    public int stepToGetLESUAmount(BlockPos pos, List<BlockPos> list) {
        list.add(pos);

        return Arrays.stream(EnumFacing.VALUES)
                .map(pos::offset)
                .filter(offset -> isLESUStorage(world, offset) && !list.contains(offset))
                .mapToInt(offset -> stepToGetLESUAmount(offset, list))
                .sum() + 1;
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
