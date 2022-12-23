package dev.su5ed.gregtechmod.item;

import com.mojang.authlib.GameProfile;
import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.event.ScannerEvent;
import dev.su5ed.gregtechmod.api.machine.IMachineProgress;
import dev.su5ed.gregtechmod.api.machine.PowerHandler;
import dev.su5ed.gregtechmod.api.machine.ScannerInfoProvider;
import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.GtLocale;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScannerItem extends ElectricItem {

    public ScannerItem() {
        super(new ElectricItemProperties()
            .maxCharge(100000)
            .transferLimit(100)
            .energyTier(1)
            .operationEnergyCost(25000)
            .showTier(false)
            .autoDescription());
    }

    protected ScannerItem(ElectricItemProperties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (ModHandler.canUseEnergy(stack, this.operationEnergyCost)) {
            if (!context.getLevel().isClientSide) {
                Player player = context.getPlayer();
                Pair<Collection<Component>, Double> scan = getCoordinateScan(stack, context);
                if (this.operationEnergyCost > 0) ModHandler.useEnergy(stack, scan.getValue(), player);
                scan.getKey().forEach(c -> player.displayClientMessage(c, false));
            }
            return InteractionResult.SUCCESS;
        }
        return super.onItemUseFirst(stack, context);
    }

    public static Pair<Collection<Component>, Double> getCoordinateScan(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        Player player = context.getPlayer();

        List<Component> ret = new ArrayList<>();
        double energyCost = 0;
        BlockEntity be = level.getBlockEntity(pos);
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        ret.add(Component.literal("-------------------"));
        ret.add(GtLocale.translateScan("name", block.getName()));
        ret.add(GtLocale.translateScan("id", ForgeRegistries.BLOCKS.getKey(block)));
        //noinspection deprecation
        ret.add(GtLocale.translateScan("hardness_resistance", state.getDestroySpeed(level, pos), block.getExplosionResistance()));

        if (be != null) {
            IFluidHandler fluidHandler = be.getCapability(ForgeCapabilities.FLUID_HANDLER, side).orElse(null);
            if (fluidHandler != null) {
                energyCost += 500;
                for (int i = 0; i < fluidHandler.getTanks(); i++) {
                    FluidStack fluidStack = fluidHandler.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        int capacity = fluidHandler.getTankCapacity(i);
                        int amount = fluidStack.getAmount();

                        ret.add(GtLocale.translateScan("tank", i, amount, capacity, fluidStack.getDisplayName()));
                    }
                }
            }
            
            if (ModHandler.ic2Loaded) {
                Pair<BlockEntity, Double> pair = IC2ScanHandler.addPreScanInfo(level, pos, state, block, be, player, ret);
                be = pair.getLeft();
                energyCost += pair.getRight();
            }

            if (be instanceof UpgradableBlockEntity upgradable) {
                energyCost += 500;
                int value;
                if ((value = upgradable.getUpgradeCount(UpgradeCategory.OVERCLOCKER)) > 0) ret.add(GtLocale.translateScan("overclockers", value));
                if ((value = upgradable.getUpgradeCount(UpgradeCategory.TRANSFORMER)) > 0) ret.add(GtLocale.translateScan("transformers", value));
//                if ((value = upgradable.getUpgradeCount(UpgradeCategory.HV_TRANSFORMER)) > 0) ret.add(GtLocale.translateScan("hv_transformers", value));
                if ((value = upgradable.getExtraEUCapacity()) > 0) ret.add(GtLocale.translateScan("extra_capacity", value));
            }

            if (be instanceof IMachineProgress machine) {
                energyCost += 400;
                int maxProgress = machine.getMaxProgress();
                if (maxProgress > 0) ret.add(GtLocale.translateScan("progress", machine.getProgress(), maxProgress));
            }

            CoverHandler coverHandler = be.getCapability(Capabilities.COVER_HANDLER, side).orElse(null);
            if (coverHandler != null) {
                energyCost += 300;
                coverHandler.getCoverAtSide(side).ifPresent(cover -> {
                    Item item = cover.getItem();
                    if (item != null) {
                        Component name = cover.getItem().getName(ItemStack.EMPTY);
                        Component component;
                        int tickRate = cover.getTickRate();
                        if (tickRate <= 1) {
                            String key = tickRate < 1 ? "cover_ticked_never" : "cover_ticked_1";
                            component = GtLocale.translateScan(key, name);
                        }
                        else {
                            component = GtLocale.translateScan("cover_ticked_n", name, tickRate);
                        }
                        ret.add(component);
                    }
                });
            }

            if (be instanceof UpgradableBlockEntity upgradable) {
                GameProfile owner = upgradable.getOwner();
                if (owner != null) {
                    energyCost += 200;
                    ret.add(GtLocale.translateScan("owner", owner.getName()));
                }
            }

            if (ModHandler.ic2Loaded) {
                energyCost += IC2ScanHandler.addPostScanInfo(be, ret);
            }

            if (be instanceof ScannerInfoProvider provider) {
                energyCost += 500;
                List<Component> temp = provider.getScanInfo(player, pos, 3);
                ret.addAll(temp);
            }
        }

        ScannerEvent event = new ScannerEvent(stack, context);
        event.increaseEUCost(energyCost);
        MinecraftForge.EVENT_BUS.post(event);

        return event.isCanceled() ? Pair.of(Collections.emptyList(), event.getEUCost()) : Pair.of(ret, event.getEUCost());
    }
    
    private static class IC2ScanHandler {
        public static Pair<BlockEntity, Double> addPreScanInfo(Level level, BlockPos pos, BlockState state, Block block, BlockEntity be, Player player, List<Component> ret) {
            double energyCost = 0;
            
            if (be instanceof IReactorChamber chamber) {
                energyCost += 500;
                be = (BlockEntity) chamber.getReactorInstance();
            }

            if (be instanceof IReactor reactor) {
                energyCost += 500;
                ret.add(GtLocale.translateScan("reactor", reactor.getHeat(), reactor.getMaxHeat(), reactor.getHeatEffectModifier(), reactor.getReactorEUEnergyOutput()));
            }

            if (block instanceof IWrenchable wrenchable) {
                energyCost += 100;
                ret.add(GtLocale.translateScan("facing_drops", wrenchable.getFacing(level, pos), wrenchable.getWrenchDrops(level, pos, state, be, player, 0)));
                ret.add(GtLocale.translateScan(wrenchable.wrenchCanRemove(level, pos, player) ? "wrenchable" : "not_wrenchable"));
            }

            IEnergyTile energyTile = EnergyNet.instance.getTile(level, pos);
            if (energyTile instanceof IEnergySink sink) {
                energyCost += 400;
                ret.add(GtLocale.translateScan("demanded_energy", sink.getDemandedEnergy()));
                double maxInput = be.getCapability(Capabilities.ENERGY_HANDLER)
                    .map(PowerHandler::getMaxInputEUp)
                    .orElseGet(() -> EnergyNet.instance.getPowerFromTier(sink.getSinkTier()));
                ret.add(GtLocale.translateScan("max_safe_input", maxInput));
            }

            if (energyTile instanceof IEnergySource source) {
                energyCost += 400;
                double maxOutput = be.getCapability(Capabilities.ENERGY_HANDLER)
                    .map(PowerHandler::getMaxOutputEUp)
                    .orElseGet(() -> EnergyNet.instance.getPowerFromTier(source.getSourceTier()));
                ret.add(GtLocale.translateScan("offered_energy", source.getOfferedEnergy()));
                ret.add(GtLocale.translateScan("max_output", maxOutput));
            }

            if (energyTile instanceof IEnergyConductor conductor) {
                energyCost += 200;
                ret.add(GtLocale.translateScan("conduction_loss", conductor.getConductionLoss()));
            }

            if (energyTile instanceof IEnergyStorage storage) {
                energyCost += 200;
                ret.add(GtLocale.translateScan("contained_energy", storage.getStored(), storage.getCapacity()));
                ret.add(GtLocale.translateScan(storage.isTeleporterCompatible(Direction.UP) ? "teleported_compatible" : "not_teleported_compatible"));
            }
            
            return Pair.of(be, energyCost);
        }
        
        public static double addPostScanInfo(BlockEntity be, List<Component> ret) {
            double energyCost = 0;
            if (be instanceof ICropTile crop) {
                energyCost += 1000;
                CropCard card = crop.getCrop();
                if (crop.getScanLevel() < 4) {
                    energyCost += 10000;
                    crop.setScanLevel(4);
                }

                ret.add(GtLocale.translateScan("crop_type", Component.translatable(card.getUnlocalizedName()), crop.getStatGrowth(), crop.getStatGain(), crop.getStatResistance()));
                ret.add(GtLocale.translateScan("crop_fertilizer", crop.getStorageNutrients(), crop.getStorageWater(), crop.getStorageWeedEX(), crop.getScanLevel()));
                ret.add(GtLocale.translateScan("crop_environment", crop.getTerrainNutrients(), crop.getTerrainHumidity(), crop.getTerrainAirQuality()));

                ret.add(GtLocale.translateScan("attributes", String.join(", ", card.getAttributes())));
                ret.add(GtLocale.translateScan("discoverer", card.getDiscoveredBy()));
            }
            return energyCost;
        }
    }
}
