package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.util.GtLocale;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.ref.Ic2Items;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NuclearFuelRodItem extends ResourceItem implements IReactorComponent {
    private final int cellCount;
    private final int maxDuration;
    private final float energy;
    private final int radiation;
    private final float heat;
    private final LazyOptional<Item> depleted;
    
    public NuclearFuelRodItem(int cellCount, int maxDuration, float energy, int radiation, float heat) {
        this(cellCount, maxDuration, energy, radiation, heat, ModHandler.ic2Loaded ? LazyOptional.of(() -> IC2RadiationHandler.getDepletedStack(cellCount)) : LazyOptional.empty());
    }

    public NuclearFuelRodItem(int cellCount, int maxDuration, float energy, int radiation, float heat, LazyOptional<Item> depleted) {
        super(new ExtendedItemProperties<>()
            .setNoRepair()
            .setNoEnchant()
            .rarity(Rarity.UNCOMMON));
        this.cellCount = cellCount;
        this.maxDuration = maxDuration;
        this.energy = energy;
        this.radiation = radiation;
        this.heat = heat;
        this.depleted = depleted;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getDuration(stack) < this.maxDuration;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.round(getDurationLevel(stack) * 13.0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb((float) (getDurationLevel(stack) / 3.0), 1, 1);
    }

    @Nullable
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag isAdvanced) {
        components.add(GtLocale.itemKey("fuel_rod", "time_left").toComponent(this.maxDuration - getDuration(stack)).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, components, isAdvanced);
    }

    //GT5U Code
    @Override
    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
        if (!reactor.produceEnergy()) return;

        for (int iteration = 0; iteration < this.cellCount; iteration++) {
            int pulses = 1 + this.cellCount / 2;
            if (!heatRun) {
                for (int i = 0; i < pulses; i++) {
                    acceptUraniumPulse(stack, reactor, stack, x, y, x, y, false);
                }
                checkPulseables(reactor, x, y, stack, false);
            }
            else {
                pulses += checkPulseables(reactor, x, y, stack, true);

                List<ItemStackPos> heatAcceptors = new ArrayList<>();
                addHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                addHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                addHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                addHeatAcceptor(reactor, x, y + 1, heatAcceptors);

                int heat = triangularNumber(pulses) * 4;
                heat = Math.round(heat * this.heat);
                while (!heatAcceptors.isEmpty() && heat > 0) {
                    int dheat = heat / heatAcceptors.size();
                    heat -= dheat;
                    dheat = ((IReactorComponent) heatAcceptors.get(0).stack.getItem()).alterHeat(heatAcceptors.get(0).stack, reactor, heatAcceptors.get(0).x, heatAcceptors.get(0).y, dheat);
                    heat += dheat;
                    heatAcceptors.remove(0);
                }
                if (heat > 0) reactor.addHeat(heat);
            }
        }

        if (getDuration(stack) >= this.maxDuration - 1) {
            reactor.setItemAt(x, y, this.depleted.map(ItemStack::new).orElse(ItemStack.EMPTY));
        }
        else if (heatRun) {
            addDuration(stack, 1);
        }
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) reactor.addOutput(this.energy);
        return true;
    }

    @Override
    public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return false;
    }

    @Override
    public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return 0;
    }

    @Override
    public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return 0;
    }

    @Override
    public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        return 0;
    }

    @Override
    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return this.cellCount * 2;
    }

    @Override
    public boolean canBePlacedIn(ItemStack stack, IReactor reactor) {
        return true;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (ModHandler.ic2Loaded) IC2RadiationHandler.onUsingTick(this.radiation, player);
    }

    private static void addHeatAcceptor(IReactor reactor, int x, int y, Collection<ItemStackPos> heatAcceptors) {
        ItemStack component = reactor.getItemAt(x, y);
        if (component != null) {
            Item item = component.getItem();
            if (item instanceof IReactorComponent && ((IReactorComponent) item).canStoreHeat(component, reactor, x, y)) {
                heatAcceptors.add(new ItemStackPos(component, x, y));
            }
        }
    }

    private static int checkPulseables(IReactor reactor, int x, int y, ItemStack stack, boolean heatRun) {
        return checkPulseable(reactor, x - 1, y, stack, x, y, heatRun)
            + checkPulseable(reactor, x + 1, y, stack, x, y, heatRun)
            + checkPulseable(reactor, x, y - 1, stack, x, y, heatRun)
            + checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
    }

    private static int checkPulseable(IReactor reactor, int x, int y, ItemStack pulseStack, int pulseX, int pulseY, boolean heatrun) {
        ItemStack stack = reactor.getItemAt(x, y);
        return stack != null && stack.getItem() instanceof IReactorComponent component
            && component.acceptUraniumPulse(stack, reactor, pulseStack, x, y, pulseX, pulseY, heatrun) ? 1 : 0;
    }

    private static int triangularNumber(int x) {
        return (x * x + x) / 2;
    }
    
    private double getDurationLevel(ItemStack stack) {
        return Mth.clamp(getDuration(stack) / (double) this.maxDuration, 0, 1);
    }
    
    private int getDuration(ItemStack stack) {
        return stack.getOrCreateTag().getInt("duration");
    }
    
    private void addDuration(ItemStack stack, int count) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("duration", tag.getInt("duration") + count);
    }

    private record ItemStackPos(ItemStack stack, int x, int y) {}
    
    private static class IC2RadiationHandler {
        private static void onUsingTick(int radiation, LivingEntity player) {
            if (radiation > 0) {
                if (!ItemArmorHazmat.hasCompleteHazmat(player)) {
                    IC2Potion.radiation.applyTo(player, 200, radiation * 100);
                }
            }
        }
        
        private static Item getDepletedStack(int cellCount) {
            return switch (cellCount) {
                case 1 -> Ic2Items.DEPLETED_URANIUM_FUEL_ROD;
                case 2 -> Ic2Items.DEPLETED_DUAL_URANIUM_FUEL_ROD;
                case 4 -> Ic2Items.QUAD_URANIUM_FUEL_ROD;
                default -> throw new IllegalArgumentException("No default depleted item for cell count " + cellCount);
            };
        }
    }
}
