package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.IMachineProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class VentCover extends BaseCover<IMachineProgress> {
    private final double efficiency;

    public VentCover(CoverType<IMachineProgress> type, IMachineProgress be, Direction side, Item item) {
        super(type, be, side, item);
        this.efficiency = getVentType(item).efficiency;
    }

    @Override
    public void tick() {
        if (this.be.isActive()) {
            Level level = ((BlockEntity) this.be).getLevel();
            BlockPos pos = ((BlockEntity) this.be).getBlockPos();
            if (level.getBlockState(pos.relative(this.side)).getCollisionShape(level, pos) == null) {
                int maxProgress = this.be.getMaxProgress();
                double amplifier = maxProgress / 100D * this.efficiency;
                double increase = amplifier / (maxProgress - 2D);
                this.be.increaseProgress(increase);
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
        NORMAL(1.5, GregTechTags.HEAT_VENT),
        SPREAD(3, GregTechTags.COMPONENT_HEAT_VENT),
        ADVANCED(3, GregTechTags.ADVANCED_HEAT_VENT, GregTechTags.OVERCLOCKED_HEAT_VENT);

        private final double efficiency;
        private final Set<TagKey<Item>> items;
        private final ResourceLocation icon;

        @SafeVarargs
        VentType(double efficiency, TagKey<Item>... items) {
            this.efficiency = efficiency;
            this.items = Set.of(items);
            this.icon = location("block", "cover", "vent_" + name().toLowerCase(Locale.ROOT));
        }

        @SuppressWarnings("deprecation")
        public boolean apply(Item item) {
            return StreamEx.of(this.items)
                .anyMatch(item.builtInRegistryHolder()::is);
        }

        public ResourceLocation getIcon() {
            return this.icon;
        }
    }

    @Override
    public int getTickRate() {
        return 1;
    }
}
