package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import com.google.common.collect.Sets;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotOutput;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.gui.GuiQuantumTank;
import mods.gregtechmod.inventory.GtFluidTank;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerQuantumTank;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityQuantumTank extends TileEntityCoverBehavior implements IHasGui {
    public Fluids fluids;
    public GtFluidTank content;
    public final InvSlotConsumableLiquid inputSlot;
    public final InvSlotOutput outputSlot;
    private int timer = 0;

    public TileEntityQuantumTank() {
        this.fluids = addComponent(new Fluids(this));
        this.content = (GtFluidTank) fluids.addTank(new GtFluidTank(this, "content", GregTechConfig.FEATURES.quantumTankCapacity));
        this.inputSlot = new InvSlotConsumableLiquidByTank(this, "inputSlot", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Both, this.content);
        this.outputSlot = new InvSlotOutput(this, "outputSlot", 1);
        this.allowedCovers = Sets.newHashSet("generic", "normal", "drain", "item_meter", "liquid_meter", "pump_module", "machine_controller", "item_valve");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return super.writeToNBT(nbt);
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        else if (player.isSneaking()) return false;

        for (ICover cover : coverHandler.covers.values()) if (!cover.opensGui(side)) return false;

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        if (timer%10 == 0) {
            this.inputSlot.processIntoTank(this.content, this.outputSlot);
            this.inputSlot.processFromTank(this.content, this.outputSlot);
        }
        for (ICover cover : coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && timer%tickRate == 0) cover.doCoverThings();
        }
        timer++;
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerQuantumTank(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
        return new GuiQuantumTank(new ContainerQuantumTank(entityPlayer, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GtUtil.translateTeBlockDescription("quantum_tank"));
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public double getProgress() {
        return 0;
    }

    @Override
    public int getMaxProgress() {
        return 0;
    }

    @Override
    public void increaseProgress(double amount) {}

    @Override
    public double getInputVoltage() {
        return 0;
    }

    @Override
    public double getStoredEU() {
        return 0;
    }

    @Override
    public double getDefaultEUCapacity() {
        return 0;
    }

    @Override
    public double getEUCapacity() {
        return 0;
    }

    @Override
    public int getAverageEUInput() {
        return 0;
    }

    @Override
    public int getAverageEUOutput() {
        return 0;
    }

    @Override
    public double getStoredSteam() {
        return 0;
    }

    @Override
    public double getSteamCapacity() {
        return 0;
    }

    @Override
    public void markForCoverBehaviorUpdate() {}

    @Override
    public double addEnergy(double amount) {
        return 0;
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        return 0;
    }

    @Override
    public double getUniversalEnergy() {
        return 0;
    }

    @Override
    public double getUniversalEnergyCapacity() {
        return 0;
    }

    @Override
    public double getOutputVoltage() {
        return 0;
    }

    @Override
    public void markForExplosion() {

    }
}