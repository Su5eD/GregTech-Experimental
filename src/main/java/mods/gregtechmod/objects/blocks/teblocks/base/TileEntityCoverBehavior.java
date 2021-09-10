package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.objects.blocks.teblocks.component.GtComponentBase;
import mods.gregtechmod.objects.blocks.teblocks.component.SidedRedstoneEmitter;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityCoverBehavior extends TileEntityCoverable implements IGregTechMachine, IScannerInfoProvider {
    protected final String descriptionKey;
    
    public final SidedRedstoneEmitter rsEmitter;
    @NBTPersistent
    private boolean enableWorking = true;
    private boolean enableWorkingOld = true;
    @NBTPersistent
    private boolean enableInput = true;
    @NBTPersistent
    private boolean enableOutput = true;
    protected int tickCounter;

    public TileEntityCoverBehavior(String descriptionKey) {
        this.descriptionKey = descriptionKey;
        this.rsEmitter = addComponent(new SidedRedstoneEmitter(this));
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        for (TileEntityComponent component : getComponents()) {
            if (component instanceof GtComponentBase) ((GtComponentBase) component).onPlaced(stack, placer, facing);
        }
    }

    @Override
    protected final boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (player.isSneaking()) return false;
        else if (beforeActivated(stack, player, side) 
                || this.coverHandler.covers.containsKey(side) && this.coverHandler.covers.get(side).onCoverRightClick(player, hand, side, hitX, hitY, hitZ) 
                || this.world.isRemote) return true;
        
        for (ICover cover : coverHandler.covers.values()) {
            if (!cover.opensGui(side)) return true;
        }
        
        for (TileEntityComponent component : this.getComponents()) {
            if (component instanceof GtComponentBase && ((GtComponentBase) component).onActivated(player, hand, side, hitX, hitY, hitZ)) return true;
        }
        
        return onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }
    
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        for (ICover cover : this.coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && this.tickCounter % tickRate == 0) cover.doCoverThings();
        }

        if (this.enableWorking != this.enableWorkingOld) {
            this.enableWorkingOld = this.enableWorking;
        }
        
        ++this.tickCounter;
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

    @Nonnull
    @Override
    public final List<String> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel) {
        List<String> scan = new ArrayList<>();
        getScanInfoPre(scan, player, pos, scanLevel);
        for (TileEntityComponent component : getComponents()) {
            if (component instanceof GtComponentBase) ((GtComponentBase) component).getScanInfo(scan, player, pos, scanLevel);
        }
        getScanInfoPost(scan, player, pos, scanLevel);
        return scan;
    }
    
    public void getScanInfoPre(List<String> scan, EntityPlayer player, BlockPos pos, int scanLevel) {}
    
    public void getScanInfoPost(List<String> scan, EntityPlayer player, BlockPos pos, int scanLevel) {}

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
}
