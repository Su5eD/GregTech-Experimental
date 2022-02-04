package mods.gregtechmod.objects.blocks.teblocks.base;

import com.mojang.authlib.GameProfile;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.machine.IScannerInfoProvider;
import mods.gregtechmod.objects.blocks.teblocks.component.GtComponentBase;
import mods.gregtechmod.objects.blocks.teblocks.component.SidedRedstoneEmitter;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import one.util.streamex.StreamEx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityCoverBehavior extends TileEntityCoverable implements IGregTechMachine, IScannerInfoProvider {
    @NBTPersistent(include = Include.NON_NULL)
    private GameProfile owner;
    @NBTPersistent
    private boolean isPrivate;

    public final SidedRedstoneEmitter rsEmitter;
    @NBTPersistent
    private boolean enableWorking = true;
    private final BooleanCountdown workStartedNow = createSingleCountDown();
    @NBTPersistent
    private boolean enableInput = true;
    @NBTPersistent
    private boolean enableOutput = true;

    public TileEntityCoverBehavior() {
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
            GtUtil.sendMessage(player, GtLocale.buildKeyInfo("access_error"), owner.getName());
            return true;
        }

        return onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public boolean checkAccess(EntityPlayer player) {
        return checkAccess(player.getGameProfile());
    }

    public boolean checkAccess(GameProfile profile) {
        return !this.isPrivate || this.owner == null || this.owner.equals(profile);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        for (ICover cover : this.coverHandler.covers.values()) {
            int tickRate = cover.getTickRate();
            if (tickRate > 0 && this.tickCounter % tickRate == 0) cover.doCoverThings();
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        ICover cover = getCoverAtSide(side);
        return this.enableInput && (cover == null || cover.letsItemsIn()) && isInputSide(side) && tryInsert(index, stack, side);
    }

    protected boolean tryInsert(int index, ItemStack stack, EnumFacing side) {
        if (!stack.isEmpty()) {
            InvSlot targetSlot = InvUtil.getInventorySlot(this, index);
            if (targetSlot != null && targetSlot.canInput() && targetSlot.accepts(stack)) {
                if (targetSlot.preferredSide != InvSlot.InvSide.ANY && !strictInputSides() || targetSlot.preferredSide.matches(side)) {
                    return true;
                }
                else {
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
        ICover cover = getCoverAtSide(side);
        return this.enableOutput && (cover == null || cover.letsItemsOut()) && super.canExtractItem(index, stack, side);
    }

    @Override
    public void setRedstoneOutput(EnumFacing side, int strength) {
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
            GtUtil.sendMessage(player, GtLocale.buildKeyInfo("wrench_error", player.getName()));
            return false;
        }
        return super.wrenchCanRemove(player);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("owner");
    }

    @Nonnull
    @Override
    public final List<ITextComponent> getScanInfo(EntityPlayer player, BlockPos pos, int scanLevel) {
        List<ITextComponent> scan = new ArrayList<>();
        if (scanLevel > 1) scan.add(new TextComponentTranslation(GtLocale.buildKeyInfo(checkAccess(player) ? "machine_accessible" : "machine_not_accessible")));
        
        getScanInfoPre(scan, player, pos, scanLevel);
        StreamEx.of(getComponents())
            .select(GtComponentBase.class)
            .forEach(component -> component.getScanInfo(scan, player, pos, scanLevel));
        getScanInfoPost(scan, player, pos, scanLevel);
        
        return scan;
    }

    public void getScanInfoPre(List<ITextComponent> scan, EntityPlayer player, BlockPos pos, int scanLevel) {}

    public void getScanInfoPost(List<ITextComponent> scan, EntityPlayer player, BlockPos pos, int scanLevel) {}

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
        if (this.enableWorking) this.workStartedNow.reset();
    }

    @Override
    public boolean workJustHasBeenEnabled() {
        return this.workStartedNow.get();
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
        updateClientField("owner");
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean value) {
        this.isPrivate = value;
    }
}
