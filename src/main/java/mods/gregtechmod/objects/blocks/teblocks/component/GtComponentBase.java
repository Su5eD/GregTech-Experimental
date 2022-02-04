package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class GtComponentBase extends TileEntityComponent {

    public GtComponentBase(TileEntityBlock parent) {
        super(parent);
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {}

    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public void getScanInfo(List<String> scan, EntityPlayer player, BlockPos pos, int scanLevel) {}

    @Override
    public void readFromNbt(NBTTagCompound nbt) {
        NBTSaveHandler.readClassFromNBT(this, nbt);
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTSaveHandler.writeClassToNBT(this, nbt);
        return nbt;
    }
}
