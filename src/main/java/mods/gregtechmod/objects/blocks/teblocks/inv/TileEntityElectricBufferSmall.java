package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IHasGui;
import ic2.core.block.comp.RedstoneEmitter;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.block.state.UnlistedBooleanProperty;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiElectricBufferSmall;
import mods.gregtechmod.inventory.invslot.GtSlotElectricBuffer;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.PropertyHelper.TextureOverride;
import mods.gregtechmod.util.PropertyHelper.VerticalRotation;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TileEntityElectricBufferSmall extends TileEntityUpgradable implements IHasGui, INetworkClientTileEntityEventListener {
    private static final TextureOverride OVERRIDE_DOWN;
    private static final TextureOverride OVERRIDE_UP;
    public static final IUnlistedProperty<Boolean> REDSTONE_TEXTURE_PROPERTY = new UnlistedBooleanProperty("redstoneTextures");

    public final BooleanCountdown inventoryModified = new BooleanCountdown(1);
    public final InvSlot buffer;
    private final RedstoneEmitter emitter;

    @NBTPersistent
    public boolean outputEnergy;
    @NBTPersistent
    public boolean redstoneIfFull;
    @NBTPersistent
    public boolean invertRedstone;
    @NBTPersistent
    private int targetStackSize;
    private int success;

    static {
        OVERRIDE_DOWN = overrideHorizontal("blocks/machines/electric_buffer_small/electric_buffer_small_down");
        OVERRIDE_UP = overrideHorizontal("blocks/machines/electric_buffer_small/electric_buffer_small_up");
    }

    private static TextureOverride overrideHorizontal(String texturePath) {
        ResourceLocation texture = new ResourceLocation(Reference.MODID, texturePath);
        Map<EnumFacing, ResourceLocation> overrides = Util.horizontalFacings.stream()
                .collect(Collectors.toMap(Function.identity(), side -> texture));
        return new TextureOverride(overrides, true);
    }

    public TileEntityElectricBufferSmall() {
        this.buffer = new GtSlotElectricBuffer(this, "buffer", InvSlot.Access.IO, 1);
        this.emitter = addComponent(new RedstoneEmitter(this));
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        int invSize = getSizeInventory();
        boolean hasItem = !this.buffer.isEmpty();
        boolean invModified = this.inventoryModified.countDown();
        if (isAllowedToWork() && canUseEnergy(500) && (
                workJustHasBeenEnabled() 
                || this.tickCounter % 200 == 0
                || this.tickCounter % 5 == 0 && (this.success > 0 || hasItem && this.tickCounter % 10 == 0 && invSize <= 1)
                || this.success >= 20
                || invModified
        )) {
            this.success--;
            if (invSize > 1 || hasItem) {
                int cost = GtUtil.moveItemStack(this, this.world.getTileEntity(this.pos.offset(getOppositeFacing())), getOppositeFacing(), getFacing(), this.targetStackSize != 0 ? this.targetStackSize : 64, this.targetStackSize != 0 ? this.targetStackSize : 1, 64, invSize > 10 ? 2 : 1);
                if (cost > 0) {
                    this.success = 20;
                    useEnergy(20);
                }
            }

            if (this.redstoneIfFull) {
                this.emitter.setLevel(this.invertRedstone ? 0 : 15);
                for (InvSlot slot : InvUtil.getInvSlots(this)) {
                    if (slot.isEmpty()) {
                        this.emitter.setLevel(this.invertRedstone ? 15 : 0);
                        useEnergy(1);
                        break;
                    }
                }
            } else this.emitter.setLevel(this.invertRedstone ? 15 : 0);
        }
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote && side == getOppositeFacing()) {
            this.targetStackSize = (this.targetStackSize + 1) % 64;
            if (this.targetStackSize == 0) GtUtil.sendMessage(player, Reference.MODID + ".teblock.electric_buffer_small.no_regulate");
            else GtUtil.sendMessage(player, Reference.MODID + ".teblock.electric_buffer_small.regulate", this.targetStackSize);
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }

    @Override
    protected int getMinimumStoredEU() {
        return 1000;
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        EnumFacing facing = getFacing();
        Ic2BlockStateInstance ret = super.getExtendedState(state)
                .withProperty(REDSTONE_TEXTURE_PROPERTY, this.emitter.getLevel() > 0);

        return facing == EnumFacing.UP ? ret.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, OVERRIDE_DOWN) 
                : facing == EnumFacing.DOWN ? ret.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, OVERRIDE_UP)
                : ret;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return side != getOppositeFacing() && super.canInsertItem(index, stack, side);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        return side == getOppositeFacing() && super.canExtractItem(index, stack, side);
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesExcept(getOppositeFacing());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.outputEnergy ? Collections.singleton(getOppositeFacing()) : Collections.emptySet();
    }

    @Override
    public int getSteamCapacity() {
        return getEUCapacity();
    }

    @Override
    public long getMjCapacity() {
        return getEUCapacity();
    }

    @Override
    protected VerticalRotation getVerticalRotation() {
        return VerticalRotation.ROTATE_X;
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }

    @Override
    public int getBaseSourceTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1250;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getOppositeFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    protected int getWeakPower(EnumFacing side) {
        return this.emitter.getLevel();
    }

    @Override
    public ContainerElectricBufferSmall getGuiContainer(EntityPlayer player) {
        return new ContainerElectricBufferSmall(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricBufferSmall(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        boolean value = event % 2 != 0;
        switch (event) {
            case 0:
            case 1:
                this.outputEnergy = value;
                this.energy.refreshSides();
                break;
            case 2:
            case 3:
                this.redstoneIfFull = value;
                break;
            case 4:
            case 5:
                this.invertRedstone = value;
                break;
        }
    }
}
