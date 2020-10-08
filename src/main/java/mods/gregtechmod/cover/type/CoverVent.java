package mods.gregtechmod.cover.type;

import ic2.core.item.reactor.ItemReactorVent;
import ic2.core.item.reactor.ItemReactorVentSpread;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoverVent extends CoverGeneric {
    private float defaultEfficiency;
    private float efficiency;

    public CoverVent(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
        if (stack != null) this.defaultEfficiency = getVentType(stack).ordinal() <= 1 ? (float) 1.5 : 3;
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregtechMachine) || !((IGregtechMachine)te).isActive()) return;
        World world = ((TileEntity)te).getWorld();
        BlockPos pos = ((TileEntity) te).getPos();
        if (world.getBlockState(pos.offset(side)).getCollisionBoundingBox(world, pos) != null) return;

        efficiency = defaultEfficiency;
        float boost = 0; //the machine's current vent boost
        for (ICover cover : te.getCovers()) {
            if (cover.getSide() == this.side) continue;
            if (cover instanceof CoverVent) boost += ((CoverVent) cover).getEfficiency();
        }
        efficiency = calculateEfficiency(efficiency, boost);
        if (efficiency == 0) return;
        int maxProgress = ((IGregtechMachine)te).getMaxProgress();
        double amplifier = (double) (maxProgress / 100) * efficiency;
        ((IGregtechMachine) te).increaseProgress(amplifier / (maxProgress - 1));
    }

    public float calculateEfficiency(float efficiency, float boost) {
        return Math.max(0, Math.min(efficiency, 10-boost));
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public ResourceLocation getIcon() {
        VentType type = getVentType(stack);
        return type.getIcon();
    }

    public static boolean isVent(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemReactorVent || item instanceof ItemReactorVentSpread;
    }

    public static VentType getVentType(ItemStack stack) {
        if (stack == null) return null;
        if (stack.getItem() instanceof ItemReactorVent && stack.getTranslationKey().substring(4).matches("advanced_heat_vent|overclocked_heat_vent")) return VentType.ADVANCED;
        else if (stack.getItem() instanceof ItemReactorVentSpread) return VentType.SPREAD;
        return VentType.NORMAL;
    }

    public enum VentType {
        NORMAL(GregTechMod.MODID, VentType.COVER_PATH + "machine_vent_rotating"),
        SPREAD(GregTechMod.MODID, VentType.COVER_PATH + "adv_machine_vent"),
        ADVANCED(GregTechMod.MODID, VentType.COVER_PATH + "adv_machine_vent_rotating");

        private final String domain;
        private final String path;
        private static final String COVER_PATH = "blocks/covers/";

        VentType(String domain, String path) {
            this.domain = domain;
            this.path = path;
        }

        public ResourceLocation getIcon() {
            return new ResourceLocation(this.domain, this.path);
        }
    }

    @Override
    public short getTickRate() {
        return 1;
    }
}
