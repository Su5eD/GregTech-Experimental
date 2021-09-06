package mods.gregtechmod.objects.blocks.teblocks.base;

import buildcraft.api.mj.MjAPI;
import com.mojang.authlib.GameProfile;
import ic2.core.IC2;
import ic2.core.block.comp.Fluids;
import ic2.core.block.comp.Fluids.InternalFluidTank;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.machine.IElectricMachine;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.compat.buildcraft.MjReceiverWrapper;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.component.UpgradeManager;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TileEntityUpgradable extends TileEntityEnergy implements IScannerInfoProvider, IUpgradableMachine, IElectricMachine {
    public final UpgradeManager upgradeManager;
    public Fluids fluids;
    @NBTPersistent
    protected boolean hasSteamUpgrade;
    public InternalFluidTank steamTank;
    protected int neededSteam;
    private int extraSinkTier;
    private int extraEUCapacity;

    protected MjReceiverWrapper receiver;
    protected boolean hasMjUpgrade;

    protected TileEntityUpgradable(String descriptionKey) {
        super(descriptionKey);
        this.upgradeManager = addComponent(new UpgradeManager(this, () -> IC2.network.get(true).updateTileEntityField(this, "extraEUCapacity"), this::onUpdateGTUpgrade, this::onUpdateIC2Upgrade));
        this.fluids = addComponent(new Fluids(this));
    }
    
    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return addUpgrade(player.inventory.getCurrentItem(), player) || super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void forceAddUpgrade(ItemStack stack) {
        this.upgradeManager.forceAddUpgrade(stack);
    }

    @Override
    public boolean addUpgrade(ItemStack stack, EntityPlayer player) {
        return this.upgradeManager.addUpgrade(stack, player);
    }

    protected void onUpdateGTUpgrade(IGtUpgradeItem item, ItemStack stack, EntityPlayer player) {
        item.afterInsert(stack, this, player);
    }

    protected void onUpdateIC2Upgrade(IC2UpgradeType type, ItemStack stack) {}
    
    @Override
    public void addExtraSinkTier() {
        this.extraSinkTier++;
    }

    @Override
    public final int getSinkTier() {
        int transformers = getUpgradeCount(IC2UpgradeType.TRANSFORMER);
        return getBaseSinkTier() + transformers + this.extraSinkTier;
    }
    
    protected abstract int getBaseEUCapacity();
    
    @Override
    public void addExtraEUCapacity(int extraCapacity) {
        this.extraEUCapacity += extraCapacity;
    }

    @Override
    public final int getEUCapacity() {
        return getBaseEUCapacity() + getExtraEUCapacity();
    }
    
    @Override
    public int getExtraEUCapacity() {
        int ic2Batteries = getUpgradeCount(IC2UpgradeType.BATTERY) * 10000;
        return ic2Batteries + this.extraEUCapacity;
    }

    public Fluids.InternalFluidTank createSteamTank() {
        return new GtFluidTank(this, "steamTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), GtUtil.STEAM_PREDICATE, 10000);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.hasSteamUpgrade) {
            NBTTagCompound tag = new NBTTagCompound();
            this.steamTank.writeToNBT(tag);
            nbt.setTag("steamTank", tag);
        }
        if (this.hasMjUpgrade) {
            nbt.setTag("mj", this.receiver.serializeNBT());
        }
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("steamTank")) {
            this.hasSteamUpgrade = true;
            this.steamTank = this.fluids.addTank(createSteamTank());
            this.steamTank.readFromNBT(nbt.getCompoundTag("steamTank"));
        }
        if (nbt.hasKey("mj")) {
            this.addMjUpgrade();
            this.receiver.deserializeNBT(nbt.getCompoundTag("mj"));
        }
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("extraEUCapacity");
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = super.getAuxDrops(fortune);
        ret.addAll(this.upgradeManager.getUpgrades());
        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        String possibleUpgrades = Stream.concat(
                this.getCompatibleIC2Upgrades().stream()
                        .sorted(Comparator.comparing(Enum::ordinal))
                        .map(entry -> entry.toString().substring(0, 1)),
                this.getCompatibleGtUpgrades().stream()
                        .filter(upgrade -> upgrade.display)
                        .map(entry -> entry.toString().substring(0, 1))
                        .sorted()
        )
                .distinct()
                .collect(Collectors.joining(" "));
        if (!possibleUpgrades.isEmpty()) tooltip.add(GtUtil.translate("teblock.info.possible_upgrades", possibleUpgrades));
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (ModHandler.buildcraftLib && (capability == MjHelper.RECEIVER_CAPABILITY || capability == MjHelper.CONNECTOR_CAPABILITY)) {
            return MjAPI.CAP_RECEIVER.cast(this.receiver);
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean hasSteamTank() {
        return this.steamTank != null;
    }

    @Nullable
    @Override
    public FluidTank getSteamTank() {
        return this.steamTank;
    }

    @Override
    public void addSteamTank() {
        this.hasSteamUpgrade = true;
        if (steamTank == null) this.steamTank = this.fluids.addTank(createSteamTank());
    }

    @Override
    public GameProfile getOwner() {
        return this.upgradeManager.getOwner();
    }

    @Override
    public void setOwner(GameProfile owner) {
        this.upgradeManager.setOwner(owner);
    }

    @Override
    public int getUpgradeCount(GtUpgradeType type) {
        return this.upgradeManager.getUpgradeCount(type);
    }

    @Override
    public int getUpgradeCount(IC2UpgradeType type) {
        return this.upgradeManager.getUpgradeCount(type);
    }

    @Override
    public boolean isPrivate() {
        return this.upgradeManager.isPrivate();
    }

    @Override
    public void setPrivate(boolean value) {
        this.upgradeManager.setPrivate(value);
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return GtUpgradeType.DEFAULT;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return IC2UpgradeType.DEFAULT;
    }
    
    protected boolean canDrainSteam(int requiredAmount) {
        if (requiredAmount < 1 || this.steamTank == null) return false;
        return this.steamTank.getFluidAmount() >= requiredAmount;
    }
     
    @Override
    public double useEnergy(double amount, boolean simulate) {
        double discharged = this.energy.discharge(amount, simulate);
        if (discharged > 0) return discharged;
        else if (this.hasMjUpgrade && this.receiver.extractPower(MjHelper.toMicroJoules(amount))) return amount;
        else if (this.hasSteamUpgrade) {
            int energy = GtUtil.getSteamForEU(amount, this.steamTank.getFluid());
            if (canDrainSteam(energy)) {
                this.steamTank.drain(energy, true);
                return amount;
            }
        }
        return 0;
    }
    
    @Override
    public double getUniversalEnergy() {
        double steam = this.hasSteamUpgrade ? this.steamTank.getFluidAmount() * GtUtil.getSteamMultiplier(this.steamTank.getFluid()) : 0;
        double mj = this.hasMjUpgrade ? MjHelper.toEU(this.receiver.getStored()) : 0;
        return Math.max(getStoredEU(), Math.max(steam, mj));
    }
    
    @Override
    public double getUniversalEnergyCapacity() {
        double steam = this.hasSteamUpgrade ? this.steamTank.getCapacity() * GtUtil.getSteamMultiplier(this.steamTank.getFluid()) : 0;
        double mj = this.hasMjUpgrade ? MjHelper.toEU(this.receiver.getCapacity()) : 0;
        return Math.max(getStoredEU(), Math.max(steam, mj));
    }

    @Override
    public double getStoredSteam() {
        return this.steamTank != null ? this.steamTank.getFluidAmount() : 0;
    }
    
    @Override
    public double getSteamCapacity() {
        return this.steamTank != null ? this.steamTank.getCapacity() : 0;
    }
    
    @Override
    public long getStoredMj() {
        return this.hasMjUpgrade ? this.receiver.getStored() : 0;
    }

    @Override
    public long getMjCapacity() {
        return this.hasMjUpgrade ? this.receiver.getCapacity() : 0;
    }

    @Override
    public void setMjCapacity(long capacity) {
        if (this.hasMjUpgrade) this.receiver.setCapacity(capacity);
    }

    @Override
    public boolean hasMjUpgrade() {
        return this.hasMjUpgrade;
    }

    @Override
    public void addMjUpgrade() {
        this.hasMjUpgrade = true;
        if (this.receiver == null) this.receiver = new MjReceiverWrapper(10000 * MjHelper.MJ, 100 * MjHelper.MJ);
        if (this.world != null) this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, false);
    }

    @Override
    public void getScanInfoPre(List<String> scan, EntityPlayer player, BlockPos pos, int scanLevel) {
        if (scanLevel > 2) scan.add("Meta-ID: " + this.getBlockType().getItemStack(this.teBlock).getMetadata());
    }

    @Override
    public void getScanInfoPost(List<String> scan, EntityPlayer player, BlockPos pos, int scanLevel) {
        if (scanLevel > 0) {
            if (this.hasSteamUpgrade) {
                FluidStack fluidStack = this.steamTank.getFluid();
                String name = fluidStack != null ? fluidStack.getLocalizedName() : GtUtil.translateGeneric("steam");
                scan.add(this.steamTank.getFluidAmount() + " / " + this.steamTank.getCapacity() + " " + name);
            }
            if (this.hasMjUpgrade) {
                scan.add(this.receiver.getStored() / MjHelper.MJ + " / " + this.receiver.getCapacity() / MjHelper.MJ + " MJ");
            }
        }
    }
}
