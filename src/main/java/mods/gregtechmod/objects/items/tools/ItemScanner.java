package mods.gregtechmod.objects.items.tools;

import com.mojang.authlib.GameProfile;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.api.energy.EnergyNet;
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
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.event.ScannerEvent;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemElectricBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
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

import java.util.ArrayList;
import java.util.List;

public class ItemScanner extends ItemElectricBase {

    public ItemScanner() {
        super("scanner", 100000, 100, 1);
        setFolder("tool");
        setRegistryName("scanner");
        setTranslationKey("scanner");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    public ItemScanner(String name, int maxCharge, double transferLimit, int tier) {
        super(name, GtUtil.NULL_SUPPLIER, maxCharge, transferLimit, tier, 0, false);
        this.showTier = false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/ODScanner.ogg", true, IC2.audioManager.getDefaultVolume());
            return EnumActionResult.PASS;
        }
        ItemStack stack = player.inventory.getCurrentItem();
        if (player instanceof EntityPlayerMP && ElectricItem.manager.canUse(stack, 25000)) {
            ArrayList<String> list = new ArrayList<>();
            ElectricItem.manager.use(stack, getCoordinateScan(list, player, world, 1, pos, side, hitX, hitY, hitZ), player);
            for (String s : list) IC2.platform.messagePlayer(player, s);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    protected static double getCoordinateScan(ArrayList<String> list, EntityPlayer player, World world, int scanLevel, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (list == null) return 0;

        ArrayList<String> ret = new ArrayList<>();
        int EUCost = 0;
        TileEntity tileEntity = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        String name = tileEntity instanceof TileEntityInventory ? I18n.format(((TileEntityInventory) tileEntity).getName()) : block.getLocalizedName();
        ret.add("-------------------");
        if (tileEntity instanceof IInventory) ret.add("Name: " + name
                + "\nID: " + block.getTranslationKey()
                + "\nMetaData: " + block.getMetaFromState(state));
        else ret.add("Name: " + name
                + "\nID: " + block.getRegistryName()
                + "\nMetaData: " + block.getMetaFromState(state));

        ret.add("Hardness: " + state.getBlockHardness(world, pos) + "  Blast Resistance: " + block.getExplosionResistance(null));

        if (tileEntity != null) {
            if (tileEntity instanceof IFluidHandler) {
                EUCost+=500;
                IFluidTankProperties[] tTanks = ((IFluidHandler)tileEntity).getTankProperties();
                if (tTanks != null) for (byte i = 0; i < tTanks.length; i++) {
                    FluidStack fluid = tTanks[i].getContents();
                    ret.add("Tank " + i + ": " + (fluid == null ? 0 : fluid.amount) + " / " + tTanks[i].getCapacity() + " " + (fluid == null ? "" : GtUtil.capitalizeString(fluid.getFluid().getUnlocalizedName().replaceFirst("fluid.", ""))));
                }
            }

            if (tileEntity instanceof IReactorChamber) {
                EUCost+=500;
                tileEntity = (TileEntity)(((IReactorChamber)tileEntity).getReactorInstance());
            }

            if (tileEntity instanceof IReactor) {
                EUCost+=500;
                ret.add("Heat: " + ((IReactor)tileEntity).getHeat() + "/" + ((IReactor)tileEntity).getMaxHeat()
                        + "  HEM: " + ((IReactor)tileEntity).getHeatEffectModifier() + "  Base EU Output: " + ((IReactor)tileEntity).getReactorEUEnergyOutput());
            }

            if (tileEntity instanceof IWrenchable) {
                EUCost+=100;
                ret.add("Facing: " + ((IWrenchable)tileEntity).getFacing(world, pos) + " / Drops: " + ((IWrenchable)tileEntity).getWrenchDrops(world, pos, state, tileEntity, player, 0));
                ret.add(((IWrenchable)tileEntity).wrenchCanRemove(world, pos, player) ? "You can remove this with a Wrench" : "You can NOT remove this with a Wrench");
            }

            if (tileEntity instanceof IEnergySink) {
                EUCost+=400;
                ret.add("Demanded energy: "+ ((IEnergySink)tileEntity).getDemandedEnergy());
                ret.add("Max Safe Input: " + EnergyNet.instance.getPowerFromTier(((IEnergySink)tileEntity).getSinkTier()));
            }

            if (tileEntity instanceof IEnergySource) {
                EUCost+=400;
                ret.add("Offered energy: "+ ((IEnergySource)tileEntity).getOfferedEnergy());
                ret.add("Max Energy Output: " + EnergyNet.instance.getPowerFromTier(((IEnergySource)tileEntity).getSourceTier()));
            }

            if (tileEntity instanceof IEnergyConductor) {
                EUCost+=200;
                ret.add("Conduction Loss: " + ((IEnergyConductor)tileEntity).getConductionLoss());
            }

            if (tileEntity instanceof IEnergyStorage) {
                EUCost+=200;
                ret.add("Contained Energy: " + ((IEnergyStorage)tileEntity).getStored() + " of " + ((IEnergyStorage)tileEntity).getCapacity());
                ret.add(((IEnergyStorage)tileEntity).isTeleporterCompatible(EnumFacing.UP) ? "Teleporter Compatible" : "Not Teleporter Compatible");
            }

            if (tileEntity instanceof IUpgradableMachine) {
                EUCost+=500;
                int tValue;
                if (0 < (tValue = ((IUpgradableMachine)tileEntity).getOverclockersCount())) ret.add(tValue	+ " Overclocker Upgrades");
                if (0 < (tValue = ((IUpgradableMachine)tileEntity).getUpgradecount(IC2UpgradeType.TRANSFORMER))) ret.add(tValue	+ " Transformer Upgrades");
                if (0 < (tValue = ((IUpgradableMachine)tileEntity).getUpgradeCount(GtUpgradeType.TRANSFORMER))) ret.add(tValue	+ " HV-Transformer Upgrades");
                if (0 < (tValue = (int) ((IUpgradableMachine)tileEntity).getExtraEnergyStorage())) ret.add(tValue	+ " Upgraded EU Capacity");
            }

            if (tileEntity instanceof IGregtechMachine) {
                EUCost+=400;
                int maxProgress;
                if (0 < (maxProgress = ((IGregtechMachine)tileEntity).getMaxProgress())) ret.add("Progress: " + ((IGregtechMachine)tileEntity).getProgress() + " / " + maxProgress);
            }

            if (tileEntity instanceof ICoverable) {
                EUCost+=300;
                ICover cover = ((ICoverable)tileEntity).getCoverAtSide(side);
                if (cover != null) {
                    int tickRate = cover.getTickRate();
                    String info = "Cover " + cover.getItem().getDisplayName() + ", ticked " + (tickRate < 1 ? "never" : tickRate == 1 ? "every tick" : "every "+tickRate+" ticks") + String.join(", ", cover.getDescription());
                    ret.add(info);
                }
            }

            if (tileEntity instanceof IUpgradableMachine) {
                GameProfile owner  = ((IUpgradableMachine)tileEntity).getOwner();
                if (owner != null) ret.add("Owned by: " + owner.getName());
            }

            if (tileEntity instanceof ICropTile) {
                if (((ICropTile)tileEntity).getScanLevel() < 4) {
                    EUCost+=10000;
                    ((ICropTile)tileEntity).setScanLevel((byte)4);
                }
                EUCost+=1000;
                CropCard crop = ((ICropTile)tileEntity).getCrop();
                ret.add("Type -- Crop-Name: " + crop.getUnlocalizedName()
                        + "  Growth: " + ((ICropTile)tileEntity).getStatGrowth()
                        + "  Gain: " + ((ICropTile)tileEntity).getStatGain()
                        + "  Resistance: " + ((ICropTile)tileEntity).getStatResistance()
                );
                ret.add("Plant -- Fertilizer: " + ((ICropTile)tileEntity).getStorageNutrients()
                        + "  Water: " + ((ICropTile)tileEntity).getStorageWater()
                        + "  Weed-Ex: " + ((ICropTile)tileEntity).getStorageWeedEX()
                        + "  Scan-Level: " + ((ICropTile)tileEntity).getScanLevel()
                );
                ret.add("Environment -- Nutrients: " + ((ICropTile)tileEntity).getStorageNutrients()
                        + "  Humidity: " + ((ICropTile)tileEntity).getTerrainHumidity()
                        + "  Air-Quality: " + ((ICropTile)tileEntity).getTerrainAirQuality()
                );
                StringBuilder attributes = new StringBuilder();
                for (String tAttribute : crop.getAttributes()) {
                    attributes.append(", ").append(tAttribute);
                }
                ret.add("Attributes:" + attributes.toString().replaceFirst(",", ""));
                ret.add("Discovered by: " + crop.getDiscoveredBy());
            }
        }
        if (tileEntity instanceof IScannerInfoProvider) {
            EUCost+=500;
            List<String> temp = ((IScannerInfoProvider)tileEntity).getScanInfo(player, pos, 3);
            ret.addAll(temp);
        }

        ScannerEvent tEvent = new ScannerEvent(world, player, pos, side, scanLevel, block, tileEntity, ret, hitX, hitY, hitZ);
        tEvent.EUCost = EUCost;
        MinecraftForge.EVENT_BUS.post(tEvent);
        if (!tEvent.isCanceled()) {
            list.addAll(ret);
        }
        return tEvent.EUCost;
    }
}
