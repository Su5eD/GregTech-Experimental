package mods.gregtechmod.objects.items.tools;

import com.mojang.authlib.GameProfile;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.block.TileEntityInventory;
import ic2.core.init.Localization;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.event.ScannerEvent;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.objects.items.base.ItemElectricBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ItemScanner extends ItemElectricBase {

    public ItemScanner() {
        this("scanner", 100000, 100, 1);
    }

    public ItemScanner(String name, int maxCharge, int transferLimit, int tier) {
        super(name, maxCharge, transferLimit, tier);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
       tooltip.add("Tricorder");
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/ODScanner.ogg", true, IC2.audioManager.getDefaultVolume());
            return EnumActionResult.PASS;
        }
        ItemStack stack = player.inventory.getCurrentItem();
        if (player instanceof EntityPlayerMP && ElectricItem.manager.canUse(stack, 25000)) {
            ArrayList<String> aList = new ArrayList<>();
            ElectricItem.manager.use(stack, getCoordinateScan(aList, player, world, 1, pos, side, hitX, hitY, hitZ), player);
            for (String s : aList) IC2.platform.messagePlayer(player, s);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    protected static double getCoordinateScan(ArrayList<String> list, EntityPlayer player, World world, int scanLevel, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (list == null) return 0;

        ArrayList<String> aList = new ArrayList<>();
        int rEUAmount = 0;
        TileEntity tileEntity = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        //TODO: Replace with if statements
        String name = tileEntity instanceof TileEntityInventory ? Localization.translate(((TileEntityInventory) tileEntity).getName()) : block.getLocalizedName();
        aList.add("-------------------");
        try {
            if (tileEntity instanceof IInventory) aList.add("Name: " + name
                    + "\nID: " + block.getTranslationKey()
                    + "\nMetaData: " + block.getMetaFromState(state));
            else aList.add("Name: " + name
                    + "\nID: " + block.getRegistryName()
                    + "\nMetaData: " + block.getMetaFromState(state));

            aList.add("Hardness: " + state.getBlockHardness(world, pos) + "  Blast Resistance: " + block.getExplosionResistance(null));
        } catch(Throwable ignored) {}

        if (tileEntity != null) {
            try {
                if (tileEntity instanceof IFluidHandler) {
                    rEUAmount+=500;
                    IFluidTankProperties[] tTanks = ((IFluidHandler)tileEntity).getTankProperties();
                    if (tTanks != null) for (byte i = 0; i < tTanks.length; i++) {
                        FluidStack fluid = tTanks[i].getContents();
                        aList.add("Tank " + i + ": " + (fluid == null ? 0 : fluid.amount) + " / " + tTanks[i].getCapacity() + " " + (fluid == null ? "" : GtUtil.capitalizeString(fluid.getFluid().getUnlocalizedName().replaceFirst("fluid.", ""))));
                    }
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IReactorChamber) {
                    rEUAmount+=500;
                    tileEntity = (TileEntity)(((IReactorChamber)tileEntity).getReactorInstance());
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IReactor) {
                    rEUAmount+=500;
                    aList.add("Heat: " + ((IReactor)tileEntity).getHeat() + "/" + ((IReactor)tileEntity).getMaxHeat()
                            + "  HEM: " + ((IReactor)tileEntity).getHeatEffectModifier() + "  Base EU Output: " + ((IReactor)tileEntity).getReactorEUEnergyOutput());
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IWrenchable) {
                    rEUAmount+=100;
                    aList.add("Facing: " + ((IWrenchable)tileEntity).getFacing(world, pos) + " / Drops: " + ((IWrenchable)tileEntity).getWrenchDrops(world, pos, state, tileEntity, player, 0));
                    aList.add(((IWrenchable)tileEntity).wrenchCanRemove(world, pos, player) ? "You can remove this with a Wrench" : "You can NOT remove this with a Wrench");
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IEnergySink) {
                    rEUAmount+=400;
                    aList.add("Demanded energy: "+ ((IEnergySink)tileEntity).getDemandedEnergy());
                    aList.add("Max Safe Input: " + (8 * Math.pow(4, ((IEnergySink)tileEntity).getSinkTier())));
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IEnergySource) {
                    rEUAmount+=400;
                    aList.add("Offered energy: "+ ((IEnergySource)tileEntity).getOfferedEnergy());
                    aList.add("Max Energy Output: " + (8 * Math.pow(4, ((IEnergySource)tileEntity).getSourceTier())));
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IEnergyConductor) {
                    rEUAmount+=200;
                    aList.add("Conduction Loss: " + ((IEnergyConductor)tileEntity).getConductionLoss());
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IEnergyStorage) {
                    rEUAmount+=200;
                    aList.add("Contained Energy: " + ((IEnergyStorage)tileEntity).getStored() + " of " + ((IEnergyStorage)tileEntity).getCapacity());
                    aList.add(((IEnergyStorage)tileEntity).isTeleporterCompatible(EnumFacing.UP) ? "Teleporter Compatible" : "Not Teleporter Compatible");
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IUpgradableMachine) {
                    rEUAmount+=500;
                    int tValue;
                    if (0 < (tValue = ((IUpgradableMachine)tileEntity).getOverclockersCount())) aList.add(tValue	+ " Overclocker Upgrades");
                    if (0 < (tValue = ((IUpgradableMachine)tileEntity).getTransformerUpgradeCount())) aList.add(tValue	+ " Transformer Upgrades");
                    if (0 < (tValue = (int) ((IUpgradableMachine)tileEntity).getExtraEnergyStorage())) aList.add(tValue	+ " Upgraded EU Capacity");
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IGregtechMachine) {
                    rEUAmount+=400;
                    int maxProgress;
                    if (0 < (maxProgress = ((IGregtechMachine)tileEntity).getMaxProgress())) aList.add("Progress: " + ((IGregtechMachine)tileEntity).getProgress() + " / " + maxProgress);
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof ICoverable) {
                    rEUAmount+=300;
                    ICover cover = ((ICoverable)tileEntity).getCoverAtSide(side);
                    if (cover != null) {
                        int tickRate = cover.getTickRate();
                        String info = "Cover " + cover.getItem().getDisplayName() + ", ticked " + (tickRate < 1 ? "never" : tickRate == 1 ? "every tick" : "every "+tickRate+" ticks") + String.join(", ", cover.getAdditionalInformation());
                        aList.add(info);
                    }
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof IUpgradableMachine) {
                    GameProfile owner  = ((IUpgradableMachine)tileEntity).getOwner();
                    if (owner != null) aList.add("Owned by: " + owner.getName());
                }
            } catch(Throwable ignored) {}

            try {
                if (tileEntity instanceof ICropTile) {
                    if (((ICropTile)tileEntity).getScanLevel() < 4) {
                        rEUAmount+=10000;
                        ((ICropTile)tileEntity).setScanLevel((byte)4);
                    }
                    rEUAmount+=1000;
                    CropCard crop = ((ICropTile)tileEntity).getCrop();
                    aList.add("Type -- Crop-Name: " + crop.getUnlocalizedName()
                            + "  Growth: " + ((ICropTile)tileEntity).getStatGrowth()
                            + "  Gain: " + ((ICropTile)tileEntity).getStatGain()
                            + "  Resistance: " + ((ICropTile)tileEntity).getStatResistance()
                    );
                    aList.add("Plant -- Fertilizer: " + ((ICropTile)tileEntity).getStorageNutrients()
                            + "  Water: " + ((ICropTile)tileEntity).getStorageWater()
                            + "  Weed-Ex: " + ((ICropTile)tileEntity).getStorageWeedEX()
                            + "  Scan-Level: " + ((ICropTile)tileEntity).getScanLevel()
                    );
                    aList.add("Environment -- Nutrients: " + ((ICropTile)tileEntity).getStorageNutrients()
                            + "  Humidity: " + ((ICropTile)tileEntity).getTerrainHumidity()
                            + "  Air-Quality: " + ((ICropTile)tileEntity).getTerrainAirQuality()
                    );
                    StringBuilder attributes = new StringBuilder();
                    for (String tAttribute : crop.getAttributes()) {
                        attributes.append(", ").append(tAttribute);
                    }
                    aList.add("Attributes:" + attributes.toString().replaceFirst(",", ""));
                    aList.add("Discovered by: " + crop.getDiscoveredBy());
                }
            } catch(Throwable ignored) {}
        }
        try {
            if (tileEntity instanceof IScannerInfoProvider) {
                rEUAmount+=500;
                List<String> temp = ((IScannerInfoProvider)tileEntity).getScanInfo(player, pos, 3);
                aList.addAll(temp);
            }
        } catch(Throwable ignored) {}

        ScannerEvent tEvent = new ScannerEvent(world, player, pos, side, scanLevel, block, tileEntity, aList, hitX, hitY, hitZ);
        tEvent.mEUCost = rEUAmount;
        MinecraftForge.EVENT_BUS.post(tEvent);
        if (!tEvent.isCanceled()) {
            list.addAll(aList);
        }
        return tEvent.mEUCost;
    }
}
