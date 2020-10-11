package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import ic2.core.util.LiquidUtil;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CoverDrain extends CoverGeneric {
    protected byte mode;

    public CoverDrain(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregTechMod.MODID, "blocks/covers/drain");
    }

    @Override
    public void doCoverThings() {
        if (mode % 3 > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != ((mode % 3) < 2))) return;

        World world = ((TileEntity)te).getWorld();
        BlockPos pos = ((TileEntity)te).getPos();
        Block block = world.getBlockState(pos.offset(side)).getBlock();

        if (LiquidUtil.isFluidTile(((TileEntity)te), side) && mode < 3) {
            IFluidHandler handler = ((TileEntity)te).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            if (side == EnumFacing.UP && world.isRaining()) {
                if (world.getPrecipitationHeight(pos).getY() - 2 < pos.getY()) {
                    int amount = (int) (world.getBiome(pos).getRainfall()*10);
                    if (amount > 0) {
                        handler.fill(new FluidStack(FluidRegistry.WATER, world.isThundering()?amount*2 : amount), true);
                    }
                }
            }
            FluidStack liquid = null;
            if (block == Blocks.WATER) liquid = new FluidStack(FluidRegistry.WATER, 1000);
            else if (block == Blocks.LAVA) liquid = new FluidStack(FluidRegistry.LAVA, 1000);
            else if (block instanceof IFluidBlock) liquid = ((IFluidBlock)block).drain(world, pos.offset(side), false);

            if (liquid != null && liquid.getFluid() != null) {
                if((side == EnumFacing.DOWN && !(liquid.getFluid().getDensity() <= 0)) || (side == EnumFacing.UP && !(liquid.getFluid().getDensity() >= 0))) return;

                if (handler.fill(liquid, false) == liquid.amount) {
                    handler.fill(liquid, true);
                    world.setBlockToAir(pos.offset(side));
                }
            }
        }
        if (mode >= 3 && block != Blocks.AIR && (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA || block == Blocks.WATER || block == Blocks.FLOWING_WATER || block instanceof IFluidBlock)) world.setBlockState(pos.offset(side), Blocks.AIR.getDefaultState(), 0);
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode + 1)%6);
        if (!player.world.isRemote) return true;

        switch (mode) {
            case 0:
                IC2.platform.messagePlayer(player, "Import");
                break;
            case 1:
                IC2.platform.messagePlayer(player, "Import (conditional)");
                break;
            case 2:
                IC2.platform.messagePlayer(player, "Import (invert cond)");
                break;
            case 3:
                IC2.platform.messagePlayer(player, "Keep Liquids Away");
                break;
            case 4:
                IC2.platform.messagePlayer(player, "Keep Liquids Away (conditional)");
                break;
            case 5:
                IC2.platform.messagePlayer(player, "Keep Liquids Away (invert cond)");
                break;
        }
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        return !(mode > 1 && te instanceof IGregtechMachine && (((IGregtechMachine)te).isAllowedToWork() != mode < 2));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", this.mode);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = nbt.getByte("mode");
    }

    @Override
    public short getTickRate() {
        return mode < 3 ? (short)50 : 1;
    }
}
