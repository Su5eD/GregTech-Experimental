package mods.gregtechmod.objects.blocks.teblocks.component;

import com.mojang.authlib.GameProfile;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class UpgradeManager extends GtComponentBase {
    private final Collection<ItemStack> upgrades = new ArrayList<>();
    private final TriConsumer<IGtUpgradeItem, ItemStack, EntityPlayer> onUpdateGTUpgrade;
    private final BiConsumer<IC2UpgradeType, ItemStack> onUpdateIC2Upgrade;
    
    private GameProfile owner;
    private boolean isPrivate;

    public <T extends TileEntityBlock & IUpgradableMachine> UpgradeManager(T parent, TriConsumer<IGtUpgradeItem, ItemStack, EntityPlayer> onUpdateGTUpgrade, BiConsumer<IC2UpgradeType, ItemStack> onUpdateIC2Upgrade) {
        super(parent);
        this.onUpdateGTUpgrade = onUpdateGTUpgrade;
        this.onUpdateIC2Upgrade = onUpdateIC2Upgrade;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.parent.getWorld().isRemote) {
            for (ItemStack stack : this.upgrades) {
                updateUpgrade(stack, null);
            }
        }
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        if (placer instanceof EntityPlayer && !this.parent.getWorld().isRemote) this.owner = ((EntityPlayer) placer).getGameProfile();
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!checkAccess(player)) {
            GtUtil.sendMessage(player, Reference.MODID + ".info.access_error", owner.getName());
            return true;
        }
        return false;
    }
    
    public boolean checkAccess(EntityPlayer player) {
        return !((IUpgradableMachine) this.parent).isPrivate() || this.owner == null || this.owner.equals(player.getGameProfile());
    }
    
    public void forceAddUpgrade(ItemStack stack) {
        ItemStack existing = this.upgrades.stream()
                .filter(stack::isItemEqual)
                .findFirst()
                .orElse(ItemStack.EMPTY);
        if (!existing.isEmpty()) existing.grow(stack.getCount());
        else this.upgrades.add(stack.copy());
    }

    public boolean addUpgrade(ItemStack stack, EntityPlayer player) {
        if (getTotalUpgradeCount() < 16 && isUpgrade(stack)) {
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
                    if (upgradeCount >= upgradeType.maxCount || upgradeType == IC2UpgradeType.TRANSFORMER && upgradeCount >= upgradeType.maxCount - ((IUpgradableMachine) this.parent).getBaseSinkTier() + 1) return false;
                }
            } else if (item instanceof IGtUpgradeItem && (areItemsEqual || existing.isEmpty())) {
                if (((IGtUpgradeItem) item).beforeInsert(existing, (IUpgradableMachine) this.parent, player)) return true;
                else if (!((IGtUpgradeItem) item).canBeInserted(existing, (IUpgradableMachine) this.parent)) return false;
            }

            ItemStack copy = StackUtil.copyWithSize(stack, 1);

            if (areItemsEqual) existing.grow(1);
            else this.upgrades.add(copy);

            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            updateUpgrade(copy, player);
            return true;
        }
        return false;
    }

    public Collection<ItemStack> getUpgrades() {
        return this.upgrades;
    }

    public int getTotalUpgradeCount() {
        return this.upgrades.stream()
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    public int getUpgradeCount(GtUpgradeType type) {
        int total = 0;
        for (ItemStack stack : this.upgrades) {
            Item item = stack.getItem();
            if (item instanceof IGtUpgradeItem && ((IGtUpgradeItem) item).getType() == type) total += stack.getCount();
        }
        return total;
    }

    public int getUpgradeCount(IC2UpgradeType type) {
        int total = 0;
        for (ItemStack stack : this.upgrades) {
            IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
            if (upgradeType != null && upgradeType == type) total += stack.getCount();
        }
        return total;
    }

    private boolean isUpgrade(ItemStack stack) {
        Item item = stack.getItem();
        
        if (item instanceof IGtUpgradeItem) {
            return ((IUpgradableMachine) this.parent).getCompatibleGtUpgrades().contains(((IGtUpgradeItem) item).getType());
        } else if (item instanceof IUpgradeItem) {
            Set<UpgradableProperty> properties = ((IUpgradableMachine) this.parent).getCompatibleIC2Upgrades().stream()
                    .map(type -> UpgradableProperty.valueOf(type.property))
                    .collect(Collectors.toSet());
            return ((IUpgradeItem) item).isSuitableFor(stack, properties);
        }
        return false;
    }

    private void updateUpgrade(ItemStack stack, @Nullable EntityPlayer player) {
        if (!this.parent.getWorld().isRemote) {
            Item currentItem = stack.getItem();
            int count = stack.getCount();
            
            if (currentItem instanceof IGtUpgradeItem) {
                for (int i = 0; i < count; i++) {
                    this.onUpdateGTUpgrade.accept((IGtUpgradeItem) currentItem, stack, player);
                }
            } else if (currentItem instanceof IUpgradeItem) {
                IC2UpgradeType upgradeType = GtUtil.getUpgradeType(stack);
                for (int i = 0; i < count; i++) {
                    this.onUpdateIC2Upgrade.accept(upgradeType, stack);
                }
            }
        }
    }

    public GameProfile getOwner() {
        return this.owner;
    }

    public void setOwner(GameProfile owner) {
        this.owner = owner;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean value) {
        this.isPrivate = value;
    }

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        GtUtil.stacksFromNBT(this.upgrades, nbt.getTagList("upgrades", 10));
        this.isPrivate = nbt.getBoolean("isPrivate");
        if (nbt.hasKey("ownerProfile")) this.owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("ownerProfile"));
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("upgrades", GtUtil.stacksToNBT(this.upgrades));
        nbt.setBoolean("isPrivate", this.isPrivate);
        if (this.owner != null) {
            NBTTagCompound ownerCompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(ownerCompound, this.owner);
            nbt.setTag("ownerProfile", ownerCompound);
        }
        return nbt;
    }

    @Override
    public void getScanInfo(List<String> scan, EntityPlayer player, BlockPos pos, int scanLevel) {
        if (scanLevel > 1) {
            scan.add(GtUtil.translateInfo(checkAccess(player) ? "machine_accessible" : "machine_not_accessible"));
        }
    }
}