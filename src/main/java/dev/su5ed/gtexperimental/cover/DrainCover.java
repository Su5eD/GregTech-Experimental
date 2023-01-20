package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.api.cover.CoverInteractionResult;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import java.util.Locale;

public class DrainCover extends BaseCover<MachineController> {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("drain");

    protected DrainMode mode = DrainMode.IMPORT;

    public DrainCover(CoverType<MachineController> type, MachineController be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public void tick() {
        BlockEntity be = (BlockEntity) this.be;
        Level level = be.getLevel();
        BlockPos pos = be.getBlockPos();
        BlockPos offset = pos.relative(this.side);
        Block block = level.getBlockState(offset).getBlock();

        if (this.mode.isImport) {
            be.getCapability(ForgeCapabilities.FLUID_HANDLER, this.side).ifPresent(handler -> {
                if (this.side == Direction.UP && level.isRainingAt(pos)) {
                    int amount = (int) (level.getBiome(pos).value().getDownfall() * 10);
                    if (amount > 0) {
                        handler.fill(new FluidStack(Fluids.WATER, level.isThundering() ? amount * 2 : amount), FluidAction.EXECUTE);
                    }
                }

                FluidStack liquid;
                if (block == Blocks.WATER) liquid = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
                else if (block == Blocks.LAVA) liquid = new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME);
                else if (block instanceof IFluidBlock fluid) liquid = fluid.drain(level, offset, FluidAction.SIMULATE);
                else liquid = null;

                if (liquid != null) {
                    Fluid fluid = liquid.getFluid();
                    if (fluid != null) {
                        int density = fluid.getFluidType().getDensity();

                        if ((this.side != Direction.DOWN || density <= 0) && (this.side != Direction.UP || density >= 0) && handler.fill(liquid, FluidAction.SIMULATE) == liquid.getAmount()) {
                            handler.fill(liquid, FluidAction.EXECUTE);
                            level.removeBlock(offset, false);
                        }
                    }
                }
            });
        }
        if (!this.mode.isImport && block != Blocks.AIR && (block instanceof LiquidBlock || block instanceof IFluidBlock)) {
            level.setBlock(offset, Blocks.AIR.defaultBlockState(), 0);
        }
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return CoverInteractionResult.CHANGED;
    }

    @Override
    public boolean letsLiquidsIn() {
        return shouldTick();
    }

    @Override
    public boolean shouldTick() {
        return !this.mode.conditional || this.be.isAllowedToWork() != this.mode.inverted;
    }

    @Override
    public int getTickRate() {
        return this.mode.tickRate;
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);
        tag.putEnum("mode", this.mode);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);
        this.mode = tag.getEnum("mode");
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

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "inventory_mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
