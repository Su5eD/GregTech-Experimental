package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.api.upgrade.IUpgradeItem;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import mods.gregtechmod.util.nbt.Serializers.ItemStackListNBTSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UpgradeManager extends GtComponentBase {
    private static final int MAX_UPGRADE_COUNT = 16;
    
    private final Runnable onUpdate;
    private final BiConsumer<IGtUpgradeItem, EntityPlayer> onUpdateGTUpgrade;
    private final Consumer<IC2UpgradeType> onUpdateIC2Upgrade;

    @NBTPersistent(include = Include.NOT_EMPTY, handler = ItemStackListNBTSerializer.class)
    private final List<ItemStack> upgrades = new ArrayList<>();

    public <T extends TileEntityBlock & IUpgradableMachine> UpgradeManager(T parent, Runnable onUpdate, BiConsumer<IGtUpgradeItem, EntityPlayer> onUpdateGTUpgrade, Consumer<IC2UpgradeType> onUpdateIC2Upgrade) {
        super(parent);
        this.onUpdate = onUpdate;
        this.onUpdateGTUpgrade = onUpdateGTUpgrade;
        this.onUpdateIC2Upgrade = onUpdateIC2Upgrade;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.parent.getWorld().isRemote) {
            StreamEx.of(this.upgrades)
                .forEach(stack -> IntStreamEx.range(stack.getCount())
                    .forEach(i -> updateUpgrade(stack, null)));
        }
    }

    public void forceAddUpgrade(ItemStack stack) {
        ItemStack existing = StreamEx.of(this.upgrades)
            .findFirst(stack::isItemEqual)
            .orElse(ItemStack.EMPTY);
        
        if (!existing.isEmpty()) existing.grow(stack.getCount());
        else this.upgrades.add(stack.copy());
    }

    public boolean addUpgrade(ItemStack stack, EntityPlayer player) {
        if (getTotalUpgradeCount() < MAX_UPGRADE_COUNT && isUpgrade(stack)) {
            Item item = stack.getItem();
            ItemStack existing = this.upgrades.stream()
                .filter(stack::isItemEqual)
                .findFirst()
                .orElse(ItemStack.EMPTY);
            int upgradeCount = existing.getCount();
            boolean areItemsEqual = StackUtil.checkItemEquality(stack, existing);

            if (item instanceof IUpgradeItem && (areItemsEqual || existing.isEmpty())) {
                IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
                if (upgradeType != null) {
                    if (upgradeCount >= upgradeType.maxCount || upgradeType == IC2UpgradeType.TRANSFORMER && upgradeCount >= upgradeType.maxCount - ((IUpgradableMachine) this.parent).getBaseSinkTier() + 1)
                        return false;
                }
            } else if (item instanceof IGtUpgradeItem && (areItemsEqual || existing.isEmpty())) {
                if (((IGtUpgradeItem) item).beforeInsert((IUpgradableMachine) this.parent, player))
                    return true;
                else if (!((IGtUpgradeItem) item).canBeInserted(existing, (IUpgradableMachine) this.parent))
                    return false;
            }

            if (areItemsEqual) existing.grow(1);
            else this.upgrades.add(ItemHandlerHelper.copyStackWithSize(stack, 1));

            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            updateUpgrade(stack, player);
            this.onUpdate.run();
            return true;
        }
        return false;
    }

    public List<ItemStack> getUpgrades() {
        return this.upgrades;
    }

    public int getTotalUpgradeCount() {
        return this.upgrades.stream()
            .mapToInt(ItemStack::getCount)
            .sum();
    }

    public int getUpgradeCount(GtUpgradeType type) {
        return this.upgrades.stream()
            .filter(stack -> {
                Item item = stack.getItem();
                return item instanceof IGtUpgradeItem && ((IGtUpgradeItem) item).getType() == type;
            })
            .mapToInt(ItemStack::getCount)
            .sum();
    }

    public int getUpgradeCount(IC2UpgradeType type) {
        return this.upgrades.stream()
            .filter(stack -> GtUtil.getUpgradeType(stack) == type)
            .mapToInt(ItemStack::getCount)
            .sum();
    }

    private boolean isUpgrade(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof IGtUpgradeItem) {
            return ((IUpgradableMachine) this.parent).getCompatibleGtUpgrades().contains(((IGtUpgradeItem) item).getType());
        } else if (item instanceof IUpgradeItem) {
            return StreamEx.of(((IUpgradableMachine) this.parent).getCompatibleIC2Upgrades())
                .map(IC2UpgradeType::getProperty)
                .toSetAndThen(properties -> ((IUpgradeItem) item).isSuitableFor(stack, properties));
        }
        return false;
    }

    private void updateUpgrade(ItemStack stack, @Nullable EntityPlayer player) {
        if (!this.parent.getWorld().isRemote) {
            Item item = stack.getItem();
            
            if (item instanceof IGtUpgradeItem) {
                this.onUpdateGTUpgrade.accept((IGtUpgradeItem) item, player);
            } else if (item instanceof IUpgradeItem) {
                IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
                this.onUpdateIC2Upgrade.accept(upgradeType);
            }
        }
    }
}
