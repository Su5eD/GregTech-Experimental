package mods.gregtechmod.objects.covers;

import ic2.api.item.IC2Items;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collection;

public class CoverVent extends CoverGeneric {
    private final double efficiency;

    public CoverVent(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
        this.efficiency = getVentType(stack).efficiency;
    }

    @Override
    public void doCoverThings() {
        if (this.te instanceof IMachineProgress && ((IMachineProgress) this.te).getActive()) {
            World world = ((TileEntity) this.te).getWorld();
            BlockPos pos = ((TileEntity) this.te).getPos();
            if (world.getBlockState(pos.offset(this.side)).getCollisionBoundingBox(world, pos) == null) {
                int maxProgress = ((IMachineProgress) this.te).getMaxProgress();
                double amplifier = maxProgress / 100D * this.efficiency;
                double increase = amplifier / (maxProgress - 2D);
                ((IMachineProgress) this.te).increaseProgress(increase);
            }
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return getVentType(this.stack).getIcon();
    }

    public static boolean isVent(ItemStack stack) {
        return Arrays.stream(VentType.values())
            .anyMatch(vent -> vent.apply(stack));
    }

    public static VentType getVentType(ItemStack stack) {
        return Arrays.stream(VentType.values())
            .filter(vent -> vent.apply(stack))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid vent ItemStack: " + stack));
    }

    public enum VentType {
        NORMAL(1.5, "machine_vent_rotating", IC2Items.getItem("heat_vent")),
        SPREAD(3, "adv_machine_vent", IC2Items.getItem("component_heat_vent")),
        ADVANCED(3, "adv_machine_vent_rotating", IC2Items.getItem("advanced_heat_vent"), IC2Items.getItem("overclocked_heat_vent"));

        private final double efficiency;
        private final Collection<ItemStack> stacks;
        private final ResourceLocation icon;

        VentType(double efficiency, String icon, ItemStack... stacks) {
            this.efficiency = efficiency;
            this.stacks = Arrays.asList(stacks);
            this.icon = new ResourceLocation(Reference.MODID, "blocks/covers/" + icon);
        }

        public boolean apply(ItemStack stack) {
            return this.stacks.stream()
                .anyMatch(coverItem -> GtUtil.stackEquals(coverItem, stack, false));
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
    public CoverType getType() {
        return CoverType.OTHER;
    }
}
