package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Locale;

public class CoverDrain extends CoverBase {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("drain");

    @NBTPersistent
    protected DrainMode mode = DrainMode.IMPORT;

    public CoverDrain(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public void doCoverThings() {
        if (!canWork()) return;

        TileEntity te = (TileEntity) this.te;
        World world = te.getWorld();
        BlockPos pos = te.getPos();
        BlockPos offset = pos.offset(side);
        Block block = world.getBlockState(offset).getBlock();

        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
        if (handler != null && mode.isImport) {
            if (side == EnumFacing.UP && world.isRaining()) {
                if (world.getPrecipitationHeight(pos).getY() - 2 < pos.getY()) {
                    int amount = (int) (world.getBiome(pos).getRainfall() * 10);
                    if (amount > 0) {
                        handler.fill(new FluidStack(FluidRegistry.WATER, world.isThundering() ? amount * 2 : amount), true);
                    }
                }
            }
            FluidStack liquid;
            if (block == Blocks.WATER) liquid = new FluidStack(FluidRegistry.WATER, 1000);
            else if (block == Blocks.LAVA) liquid = new FluidStack(FluidRegistry.LAVA, 1000);
            else if (block instanceof IFluidBlock) liquid = ((IFluidBlock) block).drain(world, offset, false);
            else liquid = null;

            if (liquid != null) {
                Fluid fluid = liquid.getFluid();
                if (fluid != null) {
                    if (side == EnumFacing.DOWN && !(fluid.getDensity() <= 0) || side == EnumFacing.UP && !(fluid.getDensity() >= 0)) return;

                    if (handler.fill(liquid, false) == liquid.amount) {
                        handler.fill(liquid, true);
                        world.setBlockToAir(offset);
                    }
                }
            }
        }
        if (!mode.isImport && block != Blocks.AIR && (block instanceof BlockLiquid || block instanceof IFluidBlock)) world.setBlockState(offset, Blocks.AIR.getDefaultState(), 0);
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        return canWork();
    }

    @Override
    public int getTickRate() {
        return mode.tickRate;
    }

    public boolean canWork() {
        return !(mode.conditional && te instanceof IGregTechMachine && ((IGregTechMachine) te).isAllowedToWork() == mode.inverted);
    }

    @Override
    public CoverType getType() {
        return CoverType.IO;
    }

    private enum DrainMode {
        IMPORT(50, true),
        IMPORT_CONDITIONAL(50, true, true),
        IMPORT_CONDITIONAL_INVERTED(50, true, true, true),
        KEEP_LIQUIDS_AWAY(1),
        KEEP_LIQUIDS_AWAY_CONDITIONAL(1, false, true),
        KEEP_LIQUIDS_AWAY_CONDITIONAL_INVERTED(1, false, true, true);

        private static final DrainMode[] VALUES = values();
        public final int tickRate;
        public final boolean isImport;
        public final boolean conditional;
        public final boolean inverted;

        DrainMode(int tickRate) {
            this(tickRate, false);
        }

        DrainMode(int tickRate, boolean isImport) {
            this(tickRate, isImport, false);
        }

        DrainMode(int tickRate, boolean isImport, boolean conditional) {
            this(tickRate, isImport, conditional, false);
        }

        DrainMode(int tickRate, boolean isImport, boolean conditional, boolean inverted) {
            this.tickRate = tickRate;
            this.isImport = isImport;
            this.conditional = conditional;
            this.inverted = inverted;
        }

        public DrainMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return GtLocale.buildKey("cover", "inventory_mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
