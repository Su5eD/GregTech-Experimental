package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.item.TurbineRotor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

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
        return new TurbineRotorHandler(this.efficiency, this.efficiencyMultiplier, 1);
    }

    public static class TurbineRotorHandler implements TurbineRotor, ICapabilityProvider {
        public static final ResourceLocation NAME = location("turbine_rotor");
        
        private final int efficiency;
        private final int efficiencyMultiplier;
        private final int damageToComponent;
        private final LazyOptional<TurbineRotor> optional = LazyOptional.of(() -> this);

        public TurbineRotorHandler(int efficiency, int efficiencyMultiplier, int damageToComponent) {
            this.efficiency = efficiency;
            this.efficiencyMultiplier = efficiencyMultiplier;
            this.damageToComponent = damageToComponent;
        }

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
            return this.damageToComponent;
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.TURBINE_ROTOR.orEmpty(cap, this.optional);
        }
    }
}
