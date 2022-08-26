package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.machine.IMachineProgress;
import ic2.core.ref.Ic2Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class VentCover extends GenericCover {
    private final double efficiency;

    public VentCover(CoverType type, Coverable te, Direction side, Item item) {
        super(type, te, side, item);
        this.efficiency = getVentType(item).efficiency;
    }

    @Override
    public void doCoverThings() {
        if (this.be instanceof IMachineProgress machine && machine.isActive()) {
            Level level = ((BlockEntity) this.be).getLevel();
            BlockPos pos = ((BlockEntity) this.be).getBlockPos();
            if (level.getBlockState(pos.relative(this.side)).getCollisionShape(level, pos) == null) {
                int maxProgress = machine.getMaxProgress();
                double amplifier = maxProgress / 100D * this.efficiency;
                double increase = amplifier / (maxProgress - 2D);
                machine.increaseProgress(increase);
            }
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return getVentType(this.item).getIcon();
    }

    public static boolean isVent(ItemStack stack) {
        return !stack.isEmpty() && Arrays.stream(VentType.values())
            .anyMatch(vent -> vent.apply(stack.getItem()));
    }

    public static VentType getVentType(Item item) {
        return StreamEx.of(VentType.values())
            .findFirst(vent -> vent.apply(item))
            .orElseThrow(() -> new IllegalArgumentException("Invalid vent Item: " + item));
    }

    public enum VentType {
        NORMAL(1.5, Ic2Items.HEAT_VENT),
        SPREAD(3, Ic2Items.COMPONENT_HEAT_VENT),
        ADVANCED(3, Ic2Items.ADVANCED_HEAT_VENT, Ic2Items.OVERCLOCKED_HEAT_VENT);

        private final double efficiency;
        private final Collection<Item> items;
        private final ResourceLocation icon;

        VentType(double efficiency, Item... items) {
            this.efficiency = efficiency;
            this.items = List.of(items);
            this.icon = location("block", "cover", "vent_" + name().toLowerCase(Locale.ROOT));
        }

        public boolean apply(Item item) {
            return StreamEx.of(this.items)
                .anyMatch(item::equals);
        }

        public ResourceLocation getIcon() {
            return this.icon;
        }
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.OTHER;
    }
}
