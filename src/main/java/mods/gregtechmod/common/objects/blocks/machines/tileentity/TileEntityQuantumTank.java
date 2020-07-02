package mods.gregtechmod.common.objects.blocks.machines.tileentity;

import com.google.common.collect.Sets;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotOutput;
import mods.gregtechmod.client.gui.GuiQuantumTank;
import mods.gregtechmod.common.core.ConfigLoader;
import mods.gregtechmod.common.cover.ICover;
import mods.gregtechmod.common.inventory.GtFluidTank;
import mods.gregtechmod.common.objects.blocks.machines.container.ContainerQuantumTank;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.base.TileEntityCoverable;
import mods.gregtechmod.common.util.IGregtechMachine;
import mods.gregtechmod.common.util.SidedRedstoneEmitter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.List;

public class TileEntityQuantumTank extends TileEntityCoverable implements IHasGui, IGregtechMachine {
    public Fluids fluids;
    public GtFluidTank content;
    public final InvSlotConsumableLiquid inputSlot;
    public final InvSlotOutput outputSlot;
    private int timer = 0;
    protected SidedRedstoneEmitter rsEmitter;

    public TileEntityQuantumTank() {
        this.fluids = addComponent(new Fluids(this));
        this.content = (GtFluidTank) fluids.addTank(new GtFluidTank(this, "content", ConfigLoader.quantumTankCapacity));
        this.inputSlot = new InvSlotConsumableLiquidByTank(this, "inputSlot", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Both, this.content);
        this.outputSlot = new InvSlotOutput(this, "outputSlot", 1);
        this.allowedCovers = Sets.newHashSet("generic", "normal", "drain", "item_meter", "liquid_meter"); //TODO: check if filter works!
        this.rsEmitter = addComponent(new SidedRedstoneEmitter(this));
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        else if (player.isSneaking()) return false;

        for (ICover cover : handler.covers.values()) if (!cover.opensGui(side)) return false;

        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        if (timer%10 == 0) {
            if (this.inputSlot.processIntoTank(this.content, this.outputSlot)) System.out.println("processed!");
            if (this.inputSlot.processFromTank(this.content, this.outputSlot)) System.out.println("out!");
        }
        for (ICover cover : handler.covers.values()) {
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
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
        return new GuiQuantumTank(new ContainerQuantumTank(entityPlayer, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("With a capacity of 488.28125 chunks!");
    }

    @Override
    protected boolean canConnectRedstone(EnumFacing side) {
        EnumFacing aSide = side.getOpposite();
        if (handler.covers.containsKey(aSide)) {
            ICover cover = handler.covers.get(aSide);
            return cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone();
        }
        return false;
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
    public void increaseProgress(double amount) {

    }

    @Override
    public void setRedstoneOutput(EnumFacing side, byte strength) {
        this.rsEmitter.setLevel(side, strength);
    }

    @Override
    protected int getWeakPower(EnumFacing side) {
        return this.rsEmitter.getLevel(side.getOpposite());
    }

    @Override
    public double getInputVoltage() {
        return 0;
    }

    @Override
    public double getStoredEU() {
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
    public void markForCoverBehaviorUpdate() {

    }

    @Override
    public void disableWorking() {

    }

    @Override
    public void enableWorking() {

    }

    @Override
    public boolean isAllowedToWork() {
        return false;
    }

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
}