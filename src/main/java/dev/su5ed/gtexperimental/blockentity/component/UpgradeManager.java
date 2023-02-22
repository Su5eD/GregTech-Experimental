package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.upgrade.Upgrade;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import ic2.core.util.StackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class UpgradeManager<T extends BaseBlockEntity & UpgradableBlockEntity> extends GtComponentBase<T> {
    public static final ResourceLocation NAME = location("upgrade_manager");
    private static final int MAX_UPGRADE_COUNT = 16;

    private final Consumer<UpgradeCategory> onUpdate;

    // TODO: Network to client
    private final List<UpgradeInstance> upgrades = new ArrayList<>();

    public UpgradeManager(T parent, Consumer<UpgradeCategory> onUpdate) {
        super(parent);
        this.onUpdate = onUpdate;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.parent.getLevel().isClientSide) {
            StreamEx.of(this.upgrades).forEach(upgrade -> upgrade.upgrade().update(this.parent, null, upgrade.stack()));
        }
    }

    public List<UpgradeInstance> getUpgrades() {
        return this.upgrades;
    }

    public int getTotalUpgradeCount() {
        return this.upgrades.stream()
            .map(UpgradeInstance::stack)
            .mapToInt(ItemStack::getCount)
            .sum();
    }

    public int getUpgradeCount(UpgradeCategory category) {
        // TODO CACHE
        return this.upgrades.stream()
            .filter(upgrade -> upgrade.upgrade.getCategory() == category)
            .map(UpgradeInstance::stack)
            .mapToInt(ItemStack::getCount)
            .sum();
    }

    public boolean addUpgrade(ItemStack stack, Player player) {
        if (!stack.isEmpty() && getTotalUpgradeCount() < MAX_UPGRADE_COUNT) {
            Upgrade upgrade = stack.getCapability(Capabilities.UPGRADE).orElse(null);
            if (upgrade == null) {
                return false;
            }

            UpgradeCategory category = upgrade.getCategory();
            if (!this.parent.getCompatibleUpgrades().contains(category)) {
                return false;
            }

            UpgradeInstance instance = StreamEx.of(this.upgrades)
                .findFirst(i -> i.stack().sameItem(stack))
                .orElse(null);
            ItemStack existingStack = instance == null ? ItemStack.EMPTY : instance.stack();
            boolean areItemsEqual = StackUtil.checkItemEquality(stack, existingStack);

            if (areItemsEqual || instance == null) {
                Upgrade.InjectResult injectResult = upgrade.beforeInsert(this.parent, player, existingStack);
                if (injectResult == Upgrade.InjectResult.SUCCESS) {
                    return true;
                }
                else if (injectResult == Upgrade.InjectResult.REJECT) {
                    return false;
                }

                ItemStack copy = ItemHandlerHelper.copyStackWithSize(stack, 1);
                if (areItemsEqual) {
                    existingStack.grow(1);
                }
                else {
                    Upgrade copyUpgrade = copy.getCapability(Capabilities.UPGRADE)
                        .orElseThrow(() -> new IllegalStateException("Expected cloned ItemStack to contain an Upgrade but got null"));
                    instance = new UpgradeInstance(copy, copyUpgrade);
                    this.upgrades.add(instance);
                }

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                instance.upgrade().update(this.parent, player, copy);
                this.onUpdate.accept(category);
                this.parent.setChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public int getPriority() {
        // Higher priority to ensure we're loaded before FluidHandler
        // for PowerProvider tank capacities
        return 1;
    }

    @Override
    public void onFieldUpdate(String name) {

    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);

        ListTag upgradesTag = new ListTag();
        this.upgrades.forEach(upgrade -> upgradesTag.add(upgrade.save()));
        tag.put("upgrades", upgradesTag);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);

        ListTag upgradesTag = tag.getList("upgrades", Tag.TAG_COMPOUND);
        StreamEx.of(upgradesTag)
            .select(CompoundTag.class)
            .map(UpgradeInstance::of)
            .nonNull()
            .forEach(this.upgrades::add);
    }

    public record UpgradeInstance(ItemStack stack, Upgrade upgrade) {
        public static UpgradeInstance of(CompoundTag tag) {
            ItemStack stack = ItemStack.of(tag.getCompound("stack"));
            Upgrade upgrade = stack.getCapability(Capabilities.UPGRADE).orElse(null);
            if (upgrade == null) {
                GregTechMod.LOGGER.error("Expected ItemStack {} to contain an upgrade but got null", stack);
                return null;
            }
            return new UpgradeInstance(stack, upgrade);
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.put("stack", this.stack.save(new CompoundTag()));
            return tag;
        }
    }
}
