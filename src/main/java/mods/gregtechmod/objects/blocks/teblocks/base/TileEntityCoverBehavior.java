package mods.gregtechmod.objects.blocks.teblocks.base;

import com.mojang.authlib.GameProfile;
import ic2.core.IC2;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.component.GtComponentBase;
import mods.gregtechmod.objects.blocks.teblocks.component.SidedRedstoneEmitter;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
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
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityCoverBehavior extends TileEntityCoverable implements IGregTechMachine, IScannerInfoProvider {
    protected final String descriptionKey;
    
    @NBTPersistent(include = Include.NON_NULL)
    private GameProfile owner;
    @NBTPersistent
    private boolean isPrivate;
    
    public final SidedRedstoneEmitter rsEmitter;
    @NBTPersistent
    private boolean enableWorking = true;
    private boolean enableWorkingOld = true;
    @NBTPersistent
    private boolean enableInput = true;
    @NBTPersistent
    private boolean enableOutput = true;

    public TileEntityCoverBehavior(String descriptionKey) {
        this.descriptionKey = descriptionKey;
        this.rsEmitter = addComponent(new SidedRedstoneEmitter(this));
    }

    @Override
    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        
        if (placer instanceof EntityPlayer && !this.world.isRemote) setOwner(((EntityPlayer) placer).getGameProfile());
        
        for (TileEntityComponent component : getComponents()) {
            if (component instanceof GtComponentBase) ((GtComponentBase) component).onPlaced(stack, placer, facing);
        }
    }

    @Override
    protected final boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote) {
            ItemStack stack = player.inventory.getCurrentItem();
            if (player.isSneaking()) return false;
            else if (beforeActivated(stack, player, side, hitX, hitY, hitZ) 
                    || this.coverHandler.covers.containsKey(side) 
                    && this.coverHandler.covers.get(side).onCoverRightClick(player, hand, side, hitX, hitY, hitZ)) return true;
            
            for (ICover cover : this.coverHandler.covers.values()) {
                if (!cover.opensGui(side)) return true;
            }
            
            for (TileEntityComponent component : this.getComponents()) {
                if (component instanceof GtComponentBase && ((GtComponentBase) component).onActivated(player, hand, side, hitX, hitY, hitZ)) return true;
            }
        }
        
        if (!checkAccess(player)) {
            GtUtil.sendMessage(player, Reference.MODID + ".info.access_error", owner.getName());
            return true;
        }
        
        return onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }
    
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }
    
    public boolean checkAccess(EntityPlayer player) {
        return !this.isPrivate || this.owner == null || this.owner.equals(player.getGameProfile());
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        for (ICover cover : this.coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && this.tickCounter % tickRate == 0) cover.doCoverThings();
        }

        if (this.enableWorking != this.enableWorkingOld) {
            this.enableWorkingOld = this.enableWorking;
        }
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
                            .allMatch(invSlot -> invSlot == targetSlot || invSlot.preferredSide == InvSlot.InvSide.ANY 
                                    || !checkDynamicInvSide(invSlot.preferredSide, side) || !invSlot.canInput() || !invSlot.accepts(stack));
                }
            }
        }

        return false;
    }
    
    private boolean checkDynamicInvSide(InvSlot.InvSide invSide, EnumFacing side) {
        return invSide == GtUtil.INV_SIDE_NS ? getFacing().getAxis() == side.getAxis() : invSide.matches(side);
    }

    protected boolean strictInputSides() {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        if (!this.enableOutput) return false;
        else if (this.coverHandler.covers.containsKey(side)) return this.coverHandler.covers.get(side).letsItemsOut() && super.canExtractItem(index, stack, side);
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
    protected boolean canConnectRedstone(@Nullable EnumFacing side) {
        if (side != null) {
            EnumFacing oppositeSide = side.getOpposite();
            if (this.coverHandler.covers.containsKey(oppositeSide)) {
                ICover cover = this.coverHandler.covers.get(oppositeSide);
                return cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone() || cover.overrideRedstoneOut();
            }
        }
        return false;
    }

    @Override
    protected boolean canSetFacingWrench(EnumFacing facing, EntityPlayer player) {
        return checkAccess(player) && super.canSetFacingWrench(facing, player);
    }

    @Override
    protected boolean wrenchCanRemove(EntityPlayer player) {
        if (!checkAccess(player)) {
            GtUtil.sendMessage(player, Reference.MODID + ".info.wrench_error", player.getName());
            return false;
        }
        return super.wrenchCanRemove(player);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("owner");
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
        if (scanLevel > 1) scan.add(GtUtil.translateInfo(checkAccess(player) ? "machine_accessible" : "machine_not_accessible"));
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
    
    @Nullable
    public GameProfile getOwner() {
        return this.owner;
    }

    public void setOwner(GameProfile owner) {
        this.owner = owner;
        IC2.network.get(true).updateTileEntityField(this, "owner");
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean value) {
        this.isPrivate = value;
    }
}
