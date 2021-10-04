package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.machine.BlockMiningPipe;
import ic2.core.ref.BlockName;
import ic2.core.util.LiquidUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiAdvancedPump;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedPump;
import mods.gregtechmod.util.LazyValue;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class TileEntityAdvancedPump extends TileEntityUpgradable implements IHasGui {
    private static final LazyValue<IBlockState> MINING_PIPE_STATE = new LazyValue<>(() -> BlockName.mining_pipe.getBlockState(BlockMiningPipe.MiningPipeType.pipe));
    private static final LazyValue<IBlockState> MINING_PIPE_TIP_STATE = new LazyValue<>(() -> BlockName.mining_pipe.getBlockState(BlockMiningPipe.MiningPipeType.tip));
    
    public final InvSlotConsumableLiquid inputSlot;
    public final InvSlot pipeSlot;
    public final InvSlotOutput outputSlot;
    private final Stack<BlockPos> pumpList = new Stack<>();
    private final FluidTank fluidTank;
    
    @NBTPersistent(include = Include.NON_NULL)
    private Block pumpedBlock;

    public TileEntityAdvancedPump() {
        super("advanced_pump");

        this.inputSlot = new InvSlotConsumableLiquid(this, "input", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill);
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.pipeSlot = new GtSlotFiltered(this, "pipe", InvSlot.Access.I, 1, stack -> stack.isItemEqual(ModHandler.miningPipe));

        this.fluidTank = this.fluids.addTankExtract("fluid", 16000);
    }

    protected static boolean isFluidSource(IBlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        return block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, pos) || block instanceof BlockLiquid && state.getValue(BlockLiquid.LEVEL) == 0;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.tickCounter % 10 == 0) {
            this.inputSlot.processFromTank(this.fluidTank, this.outputSlot);

            if (isAllowedToWork() && canUseEnergy(2560)) {
                if (this.fluidTank.getFluidAmount() + Fluid.BUCKET_VOLUME <= this.fluidTank.getCapacity()) {
                    boolean movedOneDown = false;
                    if (this.tickCounter % 100 == 0) {
                        movedOneDown = moveOneDown();
                    }

                    if (hasNoTarget()) {
                        List<EnumFacing> sides = Arrays.asList(EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
                        BlockPos pumpHead = getPumpHeadPos();
                        for (EnumFacing facing : sides) {
                            BlockPos offset = pumpHead.offset(facing);
                            if (hasNoTarget()) getFluid(offset);
                            else break;
                        }
                    } else if (getPumpHeadPos().getY() < this.pos.getY()) {
                        if (movedOneDown || this.pumpList.isEmpty() && this.tickCounter % 200 == 100 || this.tickCounter % 72000 == 100) {
                            this.pumpList.clear();
                            for (int y = this.pos.getY() - 1, yHead = getPumpHeadPos().getY(); this.pumpList.empty() && y >= yHead; y--) {
                                scanForFluid(this.pos.getX(), y, this.pos.getZ(), this.pos.getX(), this.pos.getZ(), 64);
                            }
                        }
                        if (!movedOneDown && !this.pumpList.empty()) {
                            BlockPos pos = this.pumpList.pop();
                            consumeFluid(pos.getX(), pos.getY(), pos.getZ());
                        }
                    }
                }
                setActive(!this.pumpList.empty());
            }
            else setActive(false);
        }
    }

    private boolean hasNoTarget() {
        return this.pumpedBlock == null;
    }

    private void getFluid(BlockPos pos) {
        Block block = this.world.getBlockState(pos).getBlock();

        if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
            this.pumpedBlock = Blocks.LAVA;
        } else if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            this.pumpedBlock = Blocks.WATER;
        } else if (block instanceof IFluidBlock) {
            this.pumpedBlock = block;
        } else {
            this.pumpedBlock = null;
        }
    }

    private void consumeFluid(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();

        if (this.pumpedBlock == block) {
            if (isFluidSource(state, this.world, pos)) {
                FluidStack fluid = LiquidUtil.drainBlock(this.world, pos, false);
                this.fluidTank.fillInternal(fluid, true);
                useEnergy(1280, false);
            } else {
                useEnergy(320, false);
            }

            this.world.setBlockToAir(pos);
        }
    }

    private void scanForFluid(int x, int y, int z, int rootX, int rootZ, int distance) {
        boolean posX = addIfFluidAndNotAlreadyAdded(x + 1, y, z);
        boolean negX = addIfFluidAndNotAlreadyAdded(x - 1, y, z);
        boolean posZ = addIfFluidAndNotAlreadyAdded(x, y, z + 1);
        boolean negZ = addIfFluidAndNotAlreadyAdded(x, y, z - 1);

        if (posX && x < rootX + distance) scanForFluid(x + 1, y, z, rootX, rootZ, distance);
        if (negX && x > rootX - distance) scanForFluid(x - 1, y, z, rootX, rootZ, distance);
        if (posZ && z < rootZ + distance) scanForFluid(x, y, z + 1, rootX, rootZ, distance);
        if (negZ && z > rootZ - distance) scanForFluid(x, y, z - 1, rootX, rootZ, distance);
    }
    
    private boolean addIfFluidAndNotAlreadyAdded(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!this.pumpList.contains(pos)) {
            Block block = this.world.getBlockState(pos).getBlock();
            if (this.pumpedBlock == block) {
                this.pumpList.add(pos);
                return true;
            }
        }
        return false;
    }

    private boolean moveOneDown() {
        if (!this.pipeSlot.isEmpty()) {
            BlockPos yHead = getPumpHeadPos();

            consumeFluid(yHead.getX(), yHead.getY() - 1, yHead.getZ());
            BlockPos newHeadPos = yHead.add(0, -1, 0);
            if (this.world.isAirBlock(newHeadPos) && this.world.setBlockState(newHeadPos, MINING_PIPE_TIP_STATE.get())) {
                if (!yHead.equals(this.pos)) this.world.setBlockState(yHead, MINING_PIPE_STATE.get());
                this.pipeSlot.get().shrink(1);
                return true;
            }
        }
        return false;
    }

    private BlockPos getPumpHeadPos() {
        BlockPos yPosRoot = this.pos.add(0, -1, 0);
        BlockPos yPos = yPosRoot;

        while (this.world.getBlockState(yPos) == MINING_PIPE_STATE.get()) yPos = yPos.add(0, -1, 0);

        if (yPos.equals(yPosRoot)) {
            if (this.world.getBlockState(yPos) != MINING_PIPE_TIP_STATE.get()) return this.pos;
        } else if (this.world.getBlockState(yPos) != MINING_PIPE_TIP_STATE.get()) {
            this.world.setBlockState(yPos, MINING_PIPE_STATE.get());
        }

        return yPos;
    }

    @Override
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 100000;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    public ContainerAdvancedPump getGuiContainer(EntityPlayer player) {
        return new ContainerAdvancedPump(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAdvancedPump(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}
}
