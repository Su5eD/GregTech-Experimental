package mods.gregtechmod.objects.items.tools;

import com.mojang.authlib.GameProfile;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
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
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.items.base.ItemElectricBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
        if (ElectricItem.manager.canUse(stack, 25000)) {
            ArrayList<String> list = new ArrayList<>();
            ElectricItem.manager.use(stack, getCoordinateScan(list, player, world, 1, pos, side, hitX, hitY, hitZ), player);
            for (String str : list) player.sendMessage(new TextComponentString(str));
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    protected static double getCoordinateScan(ArrayList<String> list, EntityPlayer player, World world, int scanLevel, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (list == null) return 0;

        ArrayList<String> ret = new ArrayList<>();
        int energyCost = 0;
        TileEntity tileEntity = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        String name = tileEntity instanceof TileEntityInventory ? I18n.format(((TileEntityInventory) tileEntity).getName()) : block.getLocalizedName();

        ret.add("-------------------");
        String id = tileEntity instanceof IInventory ? block.getTranslationKey() : block.getRegistryName().toString();
        ret.add(GtUtil.translateScan("name", name));
        ret.add(GtUtil.translateScan("id", id));
        ret.add(GtUtil.translateScan("metadata", block.getMetaFromState(state)));

        ret.add(GtUtil.translateScan("hardness_resistance", state.getBlockHardness(world, pos), block.getExplosionResistance(null)));

        if (tileEntity != null) {
            if (tileEntity instanceof IFluidHandler) {
                energyCost += 500;
                IFluidTankProperties[] properties = ((IFluidHandler)tileEntity).getTankProperties();
                if (properties != null) {
                    for (int i = 0; i < properties.length; i++) {
                        FluidStack fluid = properties[i].getContents();
                        int amount = fluid == null ? 0 : fluid.amount;
                        String fluidName = fluid == null ? "" : GtUtil.capitalizeString(fluid.getFluid().getUnlocalizedName().replaceFirst("fluid.", ""));
                        ret.add(GtUtil.translateScan("tank", i, amount, properties[i].getCapacity(), fluidName));
                    }
                }
            }

            if (tileEntity instanceof IReactorChamber) {
                energyCost += 500;
                tileEntity = (TileEntity) ((IReactorChamber) tileEntity).getReactorInstance();
            }

            if (tileEntity instanceof IReactor) {
                energyCost += 500;
                ret.add(GtUtil.translateScan("reactor", ((IReactor)tileEntity).getHeat(), ((IReactor)tileEntity).getMaxHeat(), ((IReactor)tileEntity).getHeatEffectModifier(), ((IReactor)tileEntity).getReactorEUEnergyOutput()));
            }

            if (tileEntity instanceof IWrenchable) {
                energyCost += 100;
                ret.add(GtUtil.translateScan("facing_drops", ((IWrenchable)tileEntity).getFacing(world, pos), ((IWrenchable)tileEntity).getWrenchDrops(world, pos, state, tileEntity, player, 0)));
                ret.add(GtUtil.translateScan(((IWrenchable)tileEntity).wrenchCanRemove(world, pos, player) ? "wrenchable" : "not_wrenchable"));
            }

            IEnergyTile energyTile = EnergyNet.instance.getTile(world, pos);
            if (energyTile instanceof IEnergySink) {
                energyCost += 400;
                ret.add(GtUtil.translateScan("demanded_energy", ((IEnergySink)energyTile).getDemandedEnergy()));
                ret.add(GtUtil.translateScan("max_safe_input", EnergyNet.instance.getPowerFromTier(((IEnergySink)energyTile).getSinkTier())));
            }

            if (energyTile instanceof IEnergySource) {
                energyCost += 400;
                ret.add(GtUtil.translateScan("max_output", energyTile instanceof AdjustableEnergy.SourceDelegate ? ((AdjustableEnergy.SourceDelegate) energyTile).getMaxOutputEUp() : ((IEnergySource) energyTile).getOfferedEnergy()));
            }

            if (energyTile instanceof IEnergyConductor) {
                energyCost += 200;
                ret.add(GtUtil.translateScan("conduction_loss", ((IEnergyConductor)energyTile).getConductionLoss()));
            }

            if (energyTile instanceof IEnergyStorage) {
                energyCost += 200;
                ret.add(GtUtil.translateScan("contained_energy", ((IEnergyStorage)energyTile).getStored(), ((IEnergyStorage)energyTile).getCapacity()));
                ret.add(GtUtil.translateScan(((IEnergyStorage)energyTile).isTeleporterCompatible(EnumFacing.UP) ? "teleported_compatible" : "not_teleported_compatible"));
            }

            if (tileEntity instanceof IUpgradableMachine) {
                energyCost += 500;
                int value;
                if ((value = ((IUpgradableMachine)tileEntity).getOverclockersCount()) > 0) ret.add(GtUtil.translateScan("overclockers", value));
                if ((value = ((IUpgradableMachine)tileEntity).getUpgradeCount(IC2UpgradeType.TRANSFORMER)) > 0) ret.add(GtUtil.translateScan("transformers", value));
                if ((value = ((IUpgradableMachine)tileEntity).getUpgradeCount(GtUpgradeType.TRANSFORMER)) > 0) ret.add(GtUtil.translateScan("hv_transformers", value));
                if ((value = (int) ((IUpgradableMachine)tileEntity).getExtraEnergyStorage()) > 0) ret.add(GtUtil.translateScan("extra_capacity", value));
            }

            if (tileEntity instanceof IMachineProgress) {
                energyCost += 400;
                int maxProgress = ((IMachineProgress)tileEntity).getMaxProgress();
                if (maxProgress > 0) ret.add(GtUtil.translateScan("progress", ((IMachineProgress)tileEntity).getProgress(), maxProgress));
            }

            if (tileEntity instanceof ICoverable) {
                energyCost += 300;
                ICover cover = ((ICoverable)tileEntity).getCoverAtSide(side);
                if (cover != null) {
                    String displayName = cover.getItem().getDisplayName();
                    String description = String.join(", ", cover.getDescription());
                    int tickRate = cover.getTickRate();
                    if (tickRate <= 1) {
                        String key = tickRate < 1 ? "cover_ticked_never" : "cover_ticked_1";
                        ret.add(GtUtil.translateScan(key, displayName, description));
                    } else ret.add(GtUtil.translateScan("cover_ticked_n", displayName, tickRate, description));
                }
            }

            if (tileEntity instanceof IUpgradableMachine) {
                GameProfile owner  = ((IUpgradableMachine)tileEntity).getOwner();
                if (owner != null) ret.add(GtUtil.translateScan("owner", owner.getName()));
            }

            if (tileEntity instanceof ICropTile) {
                ICropTile tile = (ICropTile) tileEntity;
                if (tile.getScanLevel() < 4) {
                    energyCost += 10000;
                    tile.setScanLevel((byte)4);
                }
                energyCost += 1000;

                ret.add(GtUtil.translateScan("crop_type", tile.getStatGrowth(), tile.getStatGain(), tile.getStatResistance()));
                ret.add(GtUtil.translateScan("crop_fertilizer", tile.getStorageNutrients(), tile.getStorageWater(), tile.getStorageWeedEX(), tile.getScanLevel()));
                ret.add(GtUtil.translateScan("crop_environment", tile.getTerrainNutrients(), tile.getTerrainHumidity(), tile.getTerrainAirQuality()));

                CropCard crop = tile.getCrop();
                ret.add(GtUtil.translateScan("attributes", String.join(", ", crop.getAttributes())));
                ret.add(GtUtil.translateScan("discoverer", crop.getDiscoveredBy()));
            }
        }
        if (tileEntity instanceof IScannerInfoProvider) {
            energyCost += 500;
            List<String> temp = ((IScannerInfoProvider)tileEntity).getScanInfo(player, pos, 3);
            ret.addAll(temp);
        }

        ScannerEvent event = new ScannerEvent(world, player, pos, side, scanLevel, block, tileEntity, ret, hitX, hitY, hitZ);
        event.EUCost = energyCost;
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) list.addAll(ret);

        return event.EUCost;
    }
}
