package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.item.TurbineRotor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TurbineRotorItem extends ResourceItem {
    private final int efficiency;
    private final int efficiencyMultiplier;

    public TurbineRotorItem(ExtendedItemProperties<?> properties, int efficiency, int efficiencyMultiplier) {
        super(properties);

        this.efficiency = efficiency;
        this.efficiencyMultiplier = efficiencyMultiplier;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new TurbineRotorHandler();
    }

    public class TurbineRotorHandler implements TurbineRotor, ICapabilityProvider {
        private final LazyOptional<TurbineRotor> optional = LazyOptional.of(() -> this);

        @Override
        public int getEfficiency() {
            return efficiency;
        }

        @Override
        public int getEfficiencyMultiplier() {
            return efficiencyMultiplier;
        }

        @Override
        public int getDamageToComponent() {
            return 1;
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.TURBINE_ROTOR.orEmpty(cap, this.optional);
        }
    }
}
