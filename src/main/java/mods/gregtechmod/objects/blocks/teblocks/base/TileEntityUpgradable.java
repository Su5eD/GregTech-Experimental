package mods.gregtechmod.objects.blocks.teblocks.base;

import buildcraft.api.mj.MjAPI;
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
import mods.gregtechmod.recipe.util.SteamHelper;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
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
    private int extraTier;
    private int extraEUCapacity;

    protected MjReceiverWrapper receiver;
    protected boolean hasMjUpgrade;

    protected TileEntityUpgradable() {
        this.upgradeManager = addComponent(new UpgradeManager(this, this::onUpdate, this::onUpdateGTUpgrade, this::onUpdateIC2Upgrade));
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

    protected void onUpdateGTUpgrade(IGtUpgradeItem item, EntityPlayer player) {
        item.afterInsert(this, player);
    }

    protected void onUpdateIC2Upgrade(IC2UpgradeType type) {}

    @Override
    public void addExtraTier() {
        this.extraTier++;
    }

    @Override
    public final int getSinkTier() {
        return getBaseSinkTier() + getTransformerCount();
    }

    @Override
    public final int getSourceTier() {
        int transformers = getTransformerCount();
        int tier = getBaseSourceTier() + transformers;

        return transformers > 0 && isMultiplePacketsForTransformer() ? tier - 1 : tier;
    }

    @Override
    protected final int getSourcePackets() {
        int basePackets = getBaseSourcePackets();
        return basePackets == 1 && isMultiplePacketsForTransformer() && getTransformerCount() > 0 ? 4 : basePackets;
    }

    private int getTransformerCount() {
        int transformers = getUpgradeCount(IC2UpgradeType.TRANSFORMER);
        return transformers + this.extraTier;
    }

    protected boolean isMultiplePacketsForTransformer() {
        return true;
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

    public InternalFluidTank createSteamTank() {
        return new GtFluidTank(this, "steamTank", InvSlot.InvSide.ANY.getAcceptedSides(), InvSlot.InvSide.NOTSIDE.getAcceptedSides(), GtUtil.STEAM_PREDICATE, getSteamCapacity());
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
        list.add("upgradeManager");
        list.add("extraEUCapacity");
    }

    private void onUpdate() {
        updateClientField("upgradeManager");
        updateClientField("extraEUCapacity");
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
        if (!possibleUpgrades.isEmpty()) tooltip.add(GtLocale.translateTeBlock("info", "possible_upgrades", possibleUpgrades));
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
        if (this.steamTank == null) this.steamTank = this.fluids.addTank(createSteamTank());
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
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return GtUpgradeType.DEFAULT;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return IC2UpgradeType.DEFAULT;
    }

    @Override
    public boolean canUseEnergy(double amount) {
        return getUniversalEnergy() >= amount;
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        double discharged = super.useEnergy(amount, simulate);
        if (discharged > 0) return discharged;
        else if (this.hasMjUpgrade && this.receiver.extractPower(MjHelper.microJoules(amount))) return amount;
        else if (this.hasSteamUpgrade) {
            int steam = SteamHelper.getSteamForEU(amount, this.steamTank.getFluid());
            if (steam > 0 && canDrainSteam(steam)) {
                this.steamTank.drain(steam, true);
                return amount;
            }
        }
        return 0;
    }

    protected boolean canDrainSteam(int requiredAmount) {
        return requiredAmount > 0 && this.steamTank != null && this.steamTank.getFluidAmount() >= requiredAmount;
    }

    @Override
    public double getUniversalEnergy() {
        double steam = this.steamTank != null ? SteamHelper.getEUForSteam(this.steamTank.getFluid()) : 0;
        double mj = this.receiver != null ? MjHelper.toEU(this.receiver.getStored()) : 0;
        return Math.max(getStoredEU(), Math.max(steam, mj));
    }

    @Override
    public double getUniversalEnergyCapacity() {
        double steam = this.steamTank != null ? SteamHelper.getEUForSteam(this.steamTank.getFluid(), this.steamTank.getCapacity()) : 0;
        double mj = this.receiver != null ? MjHelper.toEU(this.receiver.getCapacity()) : 0;
        return Math.max(getStoredEU(), Math.max(steam, mj));
    }

    @Override
    public double getStoredSteam() {
        return this.steamTank != null ? this.steamTank.getFluidAmount() : 0;
    }

    @Override
    public int getSteamCapacity() {
        return 10000;
    }

    @Override
    public long getStoredMj() {
        return this.hasMjUpgrade ? this.receiver.getStored() : 0;
    }

    @Override
    public long getMjCapacity() {
        return 10000;
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
        if (this.receiver == null) this.receiver = new MjReceiverWrapper(MjHelper.microJoules(getMjCapacity()), MjHelper.microJoules(100));
        if (this.world != null) this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, false);
    }

    @Override
    public void getScanInfoPre(List<ITextComponent> scan, EntityPlayer player, BlockPos pos, int scanLevel) {
        if (scanLevel > 2) {
            scan.add(GtLocale.translateScan("meta-id", this.getBlockType().getItemStack(this.teBlock).getMetadata()));
        }
    }

    @Override
    public void getScanInfoPost(List<ITextComponent> scan, EntityPlayer player, BlockPos pos, int scanLevel) {
        if (scanLevel > 0) {
            if (this.hasSteamUpgrade) {
                FluidStack fluidStack = this.steamTank.getFluid();
                ITextComponent name = new TextComponentTranslation(fluidStack != null ? fluidStack.getUnlocalizedName() : GtLocale.buildKey("generic", "steam"));
                
                scan.add(GtLocale.translateScan("storage", this.steamTank.getFluidAmount(), this.steamTank.getCapacity(), name));
            }
            if (this.hasMjUpgrade) {
                scan.add(GtLocale.translateScan("storage", MjHelper.joules(this.receiver.getStored()), MjHelper.joules(this.receiver.getCapacity()), "MJ"));
            }
        }
    }
}
