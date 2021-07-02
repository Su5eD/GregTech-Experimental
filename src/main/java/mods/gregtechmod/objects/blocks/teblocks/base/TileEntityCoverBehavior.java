package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.objects.blocks.teblocks.component.SidedRedstoneEmitter;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class TileEntityCoverBehavior extends TileEntityCoverable implements IGregTechMachine {
    private final String descriptionKey;
    
    public final SidedRedstoneEmitter rsEmitter;
    private boolean enableWorking = true;
    private boolean enableWorkingOld = true;
    private boolean enableInput = true;
    private boolean enableOutput = true;
    protected int tickCounter;

    public TileEntityCoverBehavior(String descriptionKey) {
        this.descriptionKey = descriptionKey;
        this.rsEmitter = addComponent(new SidedRedstoneEmitter(this));
    }

    @Override
    protected final boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (beforeOnActivated(stack, player, side) 
                || this.coverHandler.covers.containsKey(side) && this.coverHandler.covers.get(side).onCoverRightClick(player, hand, side, hitX, hitY, hitZ) 
                || world.isRemote) return true;
        if (player.isSneaking()) return false;
        
        for (ICover cover : coverHandler.covers.values()) {
            if (!cover.opensGui(side)) return false;
        }
        
        return onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }
    
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        for (ICover cover : coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && tickCounter % tickRate == 0) cover.doCoverThings();
        }

        if (enableWorking != enableWorkingOld) {
            enableWorkingOld = enableWorking;
            updateEnet();
        }
        tickCounter++;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.enableInput = nbt.getBoolean("enableInput");
        this.enableOutput = nbt.getBoolean("enableOutput");
        this.enableWorking = nbt.getBoolean("enableWorking");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("enableInput", this.enableInput);
        nbt.setBoolean("enableOutput", this.enableOutput);
        nbt.setBoolean("enableWorking", this.enableWorking);
        return super.writeToNBT(nbt);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        if (!enableInput) return false;
        else if (coverHandler.covers.containsKey(side)) return coverHandler.covers.get(side).letsItemsIn() && super.canInsertItem(index, stack, side);
        return tryInsert(index, stack, side);
    }

    protected boolean tryInsert(int index, ItemStack stack, EnumFacing side) {
        if (!stack.isEmpty()) {
            InvSlot targetSlot = InvUtil.getInventorySlot(this, index);
            if (targetSlot != null && targetSlot.canInput() && targetSlot.accepts(stack)) {
                if (targetSlot.preferredSide != InvSlot.InvSide.ANY && !strictInputSides() || targetSlot.preferredSide.matches(side)) {
                    return true;
                } else {
                    return InvUtil.getInvSlots(this).stream()
                            .allMatch(invSlot -> invSlot == targetSlot || invSlot.preferredSide == InvSlot.InvSide.ANY || !invSlot.preferredSide.matches(side) || !invSlot.canInput() || !invSlot.accepts(stack));
                }
            }
        }

        return false;
    }

    protected boolean strictInputSides() {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        if (!enableOutput) return false;
        else if (coverHandler.covers.containsKey(side)) return coverHandler.covers.get(side).letsItemsOut() && super.canExtractItem(index, stack, side);
        return super.canExtractItem(index, stack, side);
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
    protected boolean canConnectRedstone(EnumFacing side) {
        if (side != null) {
            EnumFacing oppositeSide = side.getOpposite();
            if (coverHandler.covers.containsKey(oppositeSide)) {
                ICover cover = coverHandler.covers.get(oppositeSide);
                return cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone() || cover.overrideRedstoneOut();
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (this.descriptionKey != null) tooltip.add(GtUtil.translateTeBlockDescription(this.descriptionKey));
    }

    @Override
    public void setInputEnabled(boolean value) {
        this.enableInput = value;
    }

    @Override
    public boolean isInputEnabled() {
        return this.enableInput;
    }

    @Override
    public void setOutputEnabled(boolean value) {
        this.enableOutput = value;
    }

    @Override
    public boolean isOutputEnabled() {
        return this.enableOutput;
    }

    @Override
    public void setAllowedToWork(boolean value) {
        this.enableWorking = value;
    }

    @Override
    public boolean isAllowedToWork() {
        return this.enableWorking;
    }

    @Override
    public void updateEnet() {}
}
