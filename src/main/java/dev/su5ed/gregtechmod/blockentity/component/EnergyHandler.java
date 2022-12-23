package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.machine.ElectricBlockEntity;
import dev.su5ed.gregtechmod.api.machine.PowerHandler;
import dev.su5ed.gregtechmod.api.machine.PowerProvider;
import dev.su5ed.gregtechmod.api.util.ChargingSlot;
import dev.su5ed.gregtechmod.api.util.DischargingSlot;
import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.power.PowerStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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

import static dev.su5ed.gregtechmod.api.Reference.location;

public class EnergyHandler<T extends BaseBlockEntity & ElectricBlockEntity> extends GtComponentBase<T> implements PowerHandler {
    private static final ResourceLocation NAME = location("energy_handler");

    private final PowerStorage storage;
    private final LazyOptional<PowerHandler> optional = LazyOptional.of(() -> this);

    // TODO draw power from providers
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
    public PowerProvider getDefaultPowerProvider() {
        return this.storage;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public void onFieldUpdate(String name) {

    }

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == Capabilities.ENERGY_HANDLER) {
            return this.optional.cast();
        }
        return super.getCapability(cap, side);
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
        return this.storage.useEnergy(amount, simulate);
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
        return this.storage.getStoredEnergy();
    }

    @Override
    public int getEnergyCapacity() {
        return this.storage.getCapacity();
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
    public boolean isSink() {
        return this.storage.isSink();
    }

    @Override
    public boolean isSource() {
        return this.storage.isSource();
    }
}
