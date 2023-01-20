package dev.su5ed.gtexperimental.item.upgrade;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.upgrade.Upgrade;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.item.ResourceItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public abstract class UpgradeItemBase extends ResourceItem {
    private final UpgradeCategory upgradeCategory;
    private final int maxCount;
    private final int requiredTier;

    public UpgradeItemBase(UpgradeCategory upgradeCategory, int maxCount, int requiredTier) {
        super(new ExtendedItemProperties<>().autoDescription());

        this.upgradeCategory = upgradeCategory;
        this.maxCount = maxCount;
        this.requiredTier = requiredTier;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return GregTechConfig.COMMON.upgradeStackSize.get();
    }

    public int getExtraEUCapacity(ItemStack stack) {
        return 0;
    }

    public Upgrade.InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
        if (stack.getCount() >= this.maxCount && Math.max(machine.getSinkTier(), machine.getSourceTier()) < this.requiredTier) {
            return Upgrade.InjectResult.REJECT;
        }
        return Upgrade.InjectResult.PASS;
    }

    public void update(UpgradableBlockEntity machine, @Nullable Player player, ItemStack stack) {

    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new GTUpgradeCapabilityProvider(this, stack);
    }

    private static class GTUpgradeCapabilityProvider implements ICapabilityProvider, Upgrade {
        private final UpgradeItemBase item;
        private final ItemStack stack;
        private final LazyOptional<Upgrade> optional;

        public GTUpgradeCapabilityProvider(UpgradeItemBase item, ItemStack stack) {
            this.item = item;
            this.stack = stack;
            this.optional = LazyOptional.of(() -> this);
        }

        @Override
        public UpgradeCategory getCategory() {
            return this.item.upgradeCategory;
        }

        @Override
        public InjectResult beforeInsert(UpgradableBlockEntity machine, Player player, ItemStack stack) {
            return this.item.beforeInsert(machine, player, stack);
        }

        @Override
        public void update(UpgradableBlockEntity machine, @Nullable Player player, ItemStack stack) {
            this.item.update(machine, player, stack);
        }

        @Override
        public int getExtraEUCapacity() {
            return this.item.getExtraEUCapacity(this.stack);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
            return Capabilities.UPGRADE.orEmpty(cap, this.optional);
        }
    }
}
