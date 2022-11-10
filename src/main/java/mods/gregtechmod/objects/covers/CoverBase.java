package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public abstract class CoverBase implements ICover {
    private final ResourceLocation name;
    protected final ICoverable te;
    protected final EnumFacing side;
    protected final ItemStack stack;

    public CoverBase(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        this.name = name;
        this.te = te;
        this.side = side;
        this.stack = stack;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public void doCoverThings() {}

    @Override
    public boolean onCoverRightClick(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return false;
    }

    @Override
    public boolean letsRedstoneIn() {
        return false;
    }

    @Override
    public boolean letsRedstoneOut() {
        return false;
    }

    @Override
    public boolean letsLiquidsIn() {
        return false;
    }

    @Override
    public boolean letsLiquidsOut() {
        return false;
    }

    @Override
    public boolean letsItemsIn() {
        return false;
    }

    @Override
    public boolean letsItemsOut() {
        return false;
    }

    @Override
    public boolean opensGui(EnumFacing side) {
        return side != this.side;
    }

    @Override
    public boolean acceptsRedstone() {
        return false;
    }

    @Override
    public boolean overrideRedstoneOut() {
        return false;
    }

    @Override
    public int getRedstoneInput() {
        return 0;
    }

    @Override
    public EnumFacing getSide() {
        return this.side;
    }

    @Override
    public ItemStack getItem() {
        return this.stack;
    }

    @Override
    public CoverType getType() {
        return CoverType.GENERIC;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTSaveHandler.writeClassToNBT(this, nbt);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTSaveHandler.readClassFromNBT(this, nbt);
    }

    @Override
    public int getTickRate() {
        return 0;
    }

    @Override
    public void onCoverRemove() {}
}
