package dev.su5ed.gtexperimental.util.upgrade;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.upgrade.Upgrade;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class IC2UpgradeCapabilityProvider implements ICapabilityProvider {
    public static final ResourceLocation NAME = location("upgrade");

    private final Upgrade instance;
    private final LazyOptional<Upgrade> optionalData;

    public IC2UpgradeCapabilityProvider(ItemStack stack, IC2UpgradeAdapter adapter) {
        this.instance = new IC2UpgradeType(stack, adapter.category, adapter.maxCount);
        this.optionalData = LazyOptional.of(() -> this.instance);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return Capabilities.UPGRADE.orEmpty(cap, this.optionalData);
    }

    public enum IC2UpgradeAdapter {
        OVERCLOCKER(UpgradableProperty.Processing, UpgradeCategory.OVERCLOCKER, be -> 4),
        TRANSFORMER(UpgradableProperty.Transformer, UpgradeCategory.TRANSFORMER, be -> 2 - be.getBaseSinkTier() + 1),
        BATTERY(UpgradableProperty.EnergyStorage, UpgradeCategory.BATTERY, be -> 16);

        public final UpgradableProperty property;
        public final UpgradeCategory category;
        public final Function<UpgradableBlockEntity, Integer> maxCount;

        IC2UpgradeAdapter(UpgradableProperty property, UpgradeCategory category, Function<UpgradableBlockEntity, Integer> maxCount) {
            this.property = property;
            this.category = category;
            this.maxCount = maxCount;
        }

        public static Optional<IC2UpgradeAdapter> of(ItemStack stack, IUpgradeItem item) {
            return StreamEx.of(values())
                .findFirst(adapter -> item.isSuitableFor(stack, Set.of(adapter.property)));
        }
    }
}
