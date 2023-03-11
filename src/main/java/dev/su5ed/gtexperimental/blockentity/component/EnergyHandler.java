package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.api.machine.ElectricBlockEntity;
import dev.su5ed.gtexperimental.api.machine.PowerHandler;
import dev.su5ed.gtexperimental.api.machine.PowerProvider;
import dev.su5ed.gtexperimental.api.util.ChargingSlot;
import dev.su5ed.gtexperimental.api.util.DischargingSlot;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.util.power.PowerStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class EnergyHandler<T extends BaseBlockEntity & ElectricBlockEntity> extends GtComponentBase<T> implements PowerHandler {
    private static final ResourceLocation NAME = location("energy_handler");

    private final PowerStorage storage;
    private final LazyOptional<PowerHandler> optional = LazyOptional.of(() -> this);

    private final List<PowerProvider> powerProviders = new ArrayList<>();

    public EnergyHandler(T parent) {
        super(parent);

        this.storage = ModHandler.createEnergyProvider(parent);
        addPowerProvider(this.storage);
    }

    @Override
    public <U extends PowerProvider> Optional<U> getPowerProvider(Class<U> type) {
        return StreamEx.of(this.powerProviders)
            .select(type)
            .findFirst();
    }

    @Override
    public void addPowerProvider(PowerProvider provider) {
        this.powerProviders.add(provider);
        this.powerProviders.sort(Comparator.comparingInt(PowerProvider::getPriority).reversed());
        this.parent.configurePowerProvider(provider);
    }

    @Override
    public PowerProvider getPrimaryPowerProvider() {
        return this.storage;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public void onFieldUpdate(String name) {}

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        super.wasExploded(level, pos, explosion);

        if (GregTechConfig.COMMON.machineChainExplosions.get()) {
            this.storage.overload();
        }
    }

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == Capabilities.ENERGY_HANDLER) {
            return this.optional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);

        tag.put("storage", this.storage.serializeNBT());
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);

        this.storage.deserializeNBT(tag.getCompound("storage"));
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.storage.load();
    }

    @Override
    public void onUnload() {
        super.onUnload();

        this.storage.unload();
    }

    @Override
    public void tickServer() {
        super.tickServer();

        this.storage.tickServer();
        // TODO Machine safety
    }

    @Override
    public boolean addEnergy(double amount) {
        if (this.storage.isSink() && amount > getMaxInputEUp()) {
            overload();
        }
        return this.storage.charge(amount);
    }

    @Override
    public boolean canUseEnergy(double amount) {
        return getStoredEnergy() >= amount;
    }

    @Override
    public boolean tryUseEnergy(double amount, boolean simulate) {
        return useEnergy(amount, simulate) >= amount;
    }

    @Override
    public void addChargingSlot(ChargingSlot slot) {
        this.storage.addChargingSlot(slot);
    }

    @Override
    public void addDischargingSlot(DischargingSlot slot) {
        this.storage.addDischargingSlot(slot);
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        double used = 0;
        for (PowerProvider provider : this.powerProviders) {
            if (used < amount) {
                used += provider.useEnergy(amount - used, simulate);
            }
            else {
                break;
            }
        }
        return used;
    }

    @Override
    public int getSinkTier() {
        return this.storage.getSinkTier();
    }

    @Override
    public int getSourceTier() {
        return this.storage.getSourceTier();
    }

    @Override
    public double getMaxInputEUp() {
        return this.storage.getMaxInputEUp();
    }

    @Override
    public double getMaxOutputEUt() {
        return this.storage.getMaxOutputEUt();
    }

    @Override
    public double getMaxOutputEUp() {
        return this.storage.getMaxOutputEUp();
    }

    @Override
    public double getStoredEnergy() {
        return StreamEx.of(this.powerProviders)
            .mapToDouble(PowerProvider::getStoredEnergy)
            .sum();
    }

    @Override
    public int getEnergyCapacity() {
        return StreamEx.of(this.powerProviders)
            .mapToInt(PowerProvider::getCapacity)
            .sum();
    }

    @Override
    public double getAverageInput() {
        return this.storage.getAverageInput();
    }

    @Override
    public double getAverageOutput() {
        return this.storage.getAverageOutput();
    }

    @Override
    public void overload() {
        this.storage.overload();
    }

    @Override
    public void overload(float power) {
        this.storage.overload(power);
    }

    @Override
    public Collection<Direction> getSinkSides() {
        return this.storage.getSinkSides();
    }

    @Override
    public Collection<Direction> getSourceSides() {
        return this.storage.getSourceSides();
    }

    @Override
    public void refreshSides() {
        this.storage.refreshSides();
    }

    @Override
    public boolean isSink() {
        return this.storage.isSink();
    }

    @Override
    public boolean isSource() {
        return this.storage.isSource();
    }
}
