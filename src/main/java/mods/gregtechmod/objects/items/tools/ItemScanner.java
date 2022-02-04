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
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.items.base.ItemElectricBase;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
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
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemScanner extends ItemElectricBase {
    private final boolean useEnergy;

    public ItemScanner() {
        super("scanner", 100000, 100, 1);
        this.useEnergy = true;
        setFolder("tool");
        setRegistryName("scanner");
        setTranslationKey("scanner");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    public ItemScanner(String name, int maxCharge, double transferLimit, int tier, boolean useEnergy) {
        super(name, JavaUtil.NULL_SUPPLIER, maxCharge, transferLimit, tier, 0, false);
        this.useEnergy = useEnergy;
        this.showTier = false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/ODScanner.ogg", true, IC2.audioManager.getDefaultVolume());
            return EnumActionResult.PASS;
        }
        ItemStack stack = player.inventory.getCurrentItem();
        if (!this.useEnergy || ElectricItem.manager.canUse(stack, 25000)) {
            Pair<Collection<String>, Integer> scan = getCoordinateScan(player, world, 1, pos, side, hitX, hitY, hitZ);
            if (this.useEnergy) ElectricItem.manager.use(stack, scan.getValue(), player);
            scan.getKey().stream()
                .map(TextComponentString::new)
                .forEach(player::sendMessage);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    // FIXME Remove client-only class usage, use ITextComponent instead 
    @SuppressWarnings("ConstantConditions")
    public static Pair<Collection<String>, Integer> getCoordinateScan(EntityPlayer player, World world, int scanLevel, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        List<String> ret = new ArrayList<>();
        int energyCost = 0;
        TileEntity tileEntity = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        String name = tileEntity instanceof TileEntityInventory ? I18n.format(((TileEntityInventory) tileEntity).getName()) : block.getLocalizedName();

        ret.add("-------------------");
        String id = tileEntity instanceof IInventory ? block.getTranslationKey() : block.getRegistryName().toString();
        ret.add(GtLocale.translateScan("name", name));
        ret.add(GtLocale.translateScan("id", id));
        ret.add(GtLocale.translateScan("metadata", block.getMetaFromState(state)));
        //noinspection deprecation
        ret.add(GtLocale.translateScan("hardness_resistance", state.getBlockHardness(world, pos), block.getExplosionResistance(null)));

        if (tileEntity != null) {
            if (tileEntity instanceof IFluidHandler) {
                energyCost += 500;
                IFluidTankProperties[] properties = ((IFluidHandler) tileEntity).getTankProperties();
                if (properties != null) {
                    for (int i = 0; i < properties.length; i++) {
                        FluidStack fluid = properties[i].getContents();
                        int amount = fluid == null ? 0 : fluid.amount;
                        String fluidName = fluid == null ? "" : JavaUtil.capitalizeString(fluid.getFluid().getUnlocalizedName().replaceFirst("fluid.", ""));
                        ret.add(GtLocale.translateScan("tank", i, amount, properties[i].getCapacity(), fluidName));
                    }
                }
            }

            if (tileEntity instanceof IReactorChamber) {
                energyCost += 500;
                tileEntity = (TileEntity) ((IReactorChamber) tileEntity).getReactorInstance();
            }

            if (tileEntity instanceof IReactor) {
                energyCost += 500;
                ret.add(GtLocale.translateScan("reactor", ((IReactor) tileEntity).getHeat(), ((IReactor) tileEntity).getMaxHeat(), ((IReactor) tileEntity).getHeatEffectModifier(), ((IReactor) tileEntity).getReactorEUEnergyOutput()));
            }

            if (block instanceof IWrenchable) {
                energyCost += 100;
                ret.add(GtLocale.translateScan("facing_drops", ((IWrenchable) block).getFacing(world, pos), ((IWrenchable) block).getWrenchDrops(world, pos, state, tileEntity, player, 0)));
                ret.add(GtLocale.translateScan(((IWrenchable) block).wrenchCanRemove(world, pos, player) ? "wrenchable" : "not_wrenchable"));
            }

            IEnergyTile energyTile = EnergyNet.instance.getTile(world, pos);
            if (energyTile instanceof IEnergySink) {
                energyCost += 400;
                ret.add(GtLocale.translateScan("demanded_energy", ((IEnergySink) energyTile).getDemandedEnergy()));
                double maxInput = tileEntity instanceof IElectricMachine ? ((IElectricMachine) tileEntity).getMaxInputEUp() : EnergyNet.instance.getPowerFromTier(((IEnergySink) energyTile).getSinkTier());
                ret.add(GtLocale.translateScan("max_safe_input", maxInput));
            }

            if (energyTile instanceof IEnergySource) {
                energyCost += 400;
                ret.add(GtLocale.translateScan("max_output", energyTile instanceof AdjustableEnergy.SourceDelegate ? ((AdjustableEnergy.SourceDelegate) energyTile).getMaxOutputEUp() : EnergyNet.instance.getPowerFromTier(((IEnergySource) energyTile).getSourceTier())));
            }

            if (energyTile instanceof IEnergyConductor) {
                energyCost += 200;
                ret.add(GtLocale.translateScan("conduction_loss", ((IEnergyConductor) energyTile).getConductionLoss()));
            }

            if (energyTile instanceof IEnergyStorage) {
                energyCost += 200;
                ret.add(GtLocale.translateScan("contained_energy", ((IEnergyStorage) energyTile).getStored(), ((IEnergyStorage) energyTile).getCapacity()));
                ret.add(GtLocale.translateScan(((IEnergyStorage) energyTile).isTeleporterCompatible(EnumFacing.UP) ? "teleported_compatible" : "not_teleported_compatible"));
            }

            if (tileEntity instanceof IUpgradableMachine) {
                energyCost += 500;
                int value;
                if ((value = ((IUpgradableMachine) tileEntity).getUpgradeCount(IC2UpgradeType.OVERCLOCKER)) > 0) ret.add(GtLocale.translateScan("overclockers", value));
                if ((value = ((IUpgradableMachine) tileEntity).getUpgradeCount(IC2UpgradeType.TRANSFORMER)) > 0) ret.add(GtLocale.translateScan("transformers", value));
                if ((value = ((IUpgradableMachine) tileEntity).getUpgradeCount(GtUpgradeType.TRANSFORMER)) > 0) ret.add(GtLocale.translateScan("hv_transformers", value));
                if ((value = ((IUpgradableMachine) tileEntity).getExtraEUCapacity()) > 0) ret.add(GtLocale.translateScan("extra_capacity", value));
            }

            if (tileEntity instanceof IMachineProgress) {
                energyCost += 400;
                int maxProgress = ((IMachineProgress) tileEntity).getMaxProgress();
                if (maxProgress > 0) ret.add(GtLocale.translateScan("progress", ((IMachineProgress) tileEntity).getProgress(), maxProgress));
            }

            if (tileEntity instanceof ICoverable) {
                energyCost += 300;
                ICover cover = ((ICoverable) tileEntity).getCoverAtSide(side);
                if (cover != null) {
                    String displayName = cover.getItem().getDisplayName();
                    String description = String.join(", ", cover.getDescription());
                    String translated;
                    int tickRate = cover.getTickRate();
                    if (tickRate <= 1) {
                        String key = tickRate < 1 ? "cover_ticked_never" : "cover_ticked_1";
                        translated = GtLocale.translateScan(key, displayName);
                    }
                    else translated = GtLocale.translateScan("cover_ticked_n", displayName, tickRate);

                    ret.add(!description.isEmpty() ? translated + ", " + description : translated);
                }
            }

            if (tileEntity instanceof IUpgradableMachine) {
                GameProfile owner = ((IUpgradableMachine) tileEntity).getOwner();
                if (owner != null) ret.add(GtLocale.translateScan("owner", owner.getName()));
            }

            if (tileEntity instanceof ICropTile) {
                ICropTile tile = (ICropTile) tileEntity;
                CropCard crop = tile.getCrop();
                if (tile.getScanLevel() < 4) {
                    energyCost += 10000;
                    tile.setScanLevel((byte) 4);
                }
                energyCost += 1000;

                ret.add(GtLocale.translateScan("crop_type", I18n.format(crop.getUnlocalizedName()), tile.getStatGrowth(), tile.getStatGain(), tile.getStatResistance()));
                ret.add(GtLocale.translateScan("crop_fertilizer", tile.getStorageNutrients(), tile.getStorageWater(), tile.getStorageWeedEX(), tile.getScanLevel()));
                ret.add(GtLocale.translateScan("crop_environment", tile.getTerrainNutrients(), tile.getTerrainHumidity(), tile.getTerrainAirQuality()));

                ret.add(GtLocale.translateScan("attributes", String.join(", ", crop.getAttributes())));
                ret.add(GtLocale.translateScan("discoverer", crop.getDiscoveredBy()));
            }

            if (tileEntity instanceof IScannerInfoProvider) {
                energyCost += 500;
                List<String> temp = ((IScannerInfoProvider) tileEntity).getScanInfo(player, pos, 3);
                ret.addAll(temp);
            }
        }

        ScannerEvent event = new ScannerEvent(world, player, pos, side, scanLevel, block, tileEntity, ret, hitX, hitY, hitZ);
        event.euCost = energyCost;
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) return Pair.of(Collections.emptyList(), event.euCost);
        else return Pair.of(ret, event.euCost);
    }
}
