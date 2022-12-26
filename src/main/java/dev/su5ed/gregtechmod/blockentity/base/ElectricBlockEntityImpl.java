package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.api.machine.ElectricBlockEntity;
import dev.su5ed.gregtechmod.api.machine.PowerProvider;
import dev.su5ed.gregtechmod.blockentity.component.EnergyHandler;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.JavaUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class ElectricBlockEntityImpl extends CoverableBlockEntity implements ElectricBlockEntity {
    private final EnergyHandler<ElectricBlockEntityImpl> energy;

    public ElectricBlockEntityImpl(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.energy = addComponent(new EnergyHandler<>(this));
    }

    public boolean showEnergyCapacity() {
        return false;
    }
    
    @Override
    public void configurePowerProvider(PowerProvider provider) {}

    @Override
    public int getEnergyCapacity() {
        return 0;
    }

    @Override
    public Collection<Direction> getSinkSides() {
        return Set.of();
    }

    @Override
    public Collection<Direction> getSourceSides() {
        return Set.of();
    }
    
    @Override
    public int getSinkTier() {
        return 0;
    }
    
    @Override
    public int getSourceTier() {
        return 0;
    }

    @Override
    public int getSourcePackets() {
        return 1;
    }

    @Override
    public double getMaxOutputEUp() {
        int sourceTier = getSourceTier();
        return sourceTier > 0 ? ModHandler.getEnergyFromTier(sourceTier) : 0;
    }

    @Override
    public double getMinimumStoredEnergy() {
        return 512;
    }

    protected boolean enableMachineSafety() {
        return true;
    }
    
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        
        if (this.energy.isSink()) {
            tooltip.add(GtLocale.key("info", "max_energy_in").toComponent(Math.round(this.energy.getMaxInputEUp())).withStyle(ChatFormatting.GRAY));
        }
        if (this.energy.isSource()) {
            tooltip.add(GtLocale.key("info", "max_energy_out").toComponent(Math.round(getMaxOutputEUp())).withStyle(ChatFormatting.GRAY));
            int packets = getSourcePackets();
            if (packets > 1) {
                tooltip.add(GtLocale.key("info", "output_packets").toComponent(packets).withStyle(ChatFormatting.GRAY));
            }
        }
        if (showEnergyCapacity()) {
            tooltip.add(GtLocale.key("info", "eu_storage").toComponent(JavaUtil.formatNumber(this.energy.getEnergyCapacity())));
        }
    }
}
