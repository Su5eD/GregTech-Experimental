package mods.gregtechmod.objects.items.base;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.reactor.ItemReactorUranium;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.ICustomItemModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemNuclearFuelRod extends ItemReactorUranium implements ICustomItemModel {
    private final String name;
    @Nullable
    private final ItemStack depletedStack;
    private final float energy;
    private final int radiation;
    private final float heat;

    public ItemNuclearFuelRod(String name, int cells, int duration, float energy, int radiation, float heat, @Nullable ItemStack depletedStack) {
        super(null, cells, duration);
        this.name = name;
        this.depletedStack = depletedStack;
        this.energy = energy;
        this.radiation = radiation;
        this.heat = heat;
        setMaxStackSize(1);
    }

    @Override
    public String getTranslationKey() {
        return GtLocale.buildKeyItem(this.name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GtLocale.translateItem("nuclear_rod.time_left", getMaxCustomDamage(stack) - getCustomDamage(stack)));
    }

    @Override
    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        return this.depletedStack != null ? this.depletedStack : super.getDepletedStack(stack, reactor);
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) reactor.addOutput(this.energy);
        return true;
    }

    //GT5U Code
    @Override
    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
        if (!reactor.produceEnergy()) return;

        for (int iteration = 0; iteration < this.numberOfCells; iteration++) {
            int pulses = 1 + this.numberOfCells / 2;
            if (!heatRun) {
                for (int i = 0; i < pulses; i++) {
                    acceptUraniumPulse(stack, reactor, stack, x, y, x, y, false);
                }
                checkPulseables(reactor, x, y, stack, false);
            } else {
                pulses += checkPulseables(reactor, x, y, stack, true);

                List<ItemStackPos> heatAcceptors = new ArrayList<>();
                addHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                addHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                addHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                addHeatAcceptor(reactor, x, y + 1, heatAcceptors);
                
                int triangularPulses = triangularNumber(pulses) * 4;
                int heat = getFinalHeat(stack, reactor, x, y, triangularPulses);
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
        
        if (getCustomDamage(stack) >= getMaxCustomDamage(stack) - 1) {
            reactor.setItemAt(x, y, getDepletedStack(stack, reactor));
        } else if (heatRun) {
            this.applyCustomDamage(stack, 1, null);
        }
    }
    
    private void addHeatAcceptor(IReactor reactor, int x, int y, Collection<ItemStackPos> heatAcceptors) {
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

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (this.radiation > 0 && entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 200, this.radiation * 100);
            }
        }
    }

    @Override
    public ResourceLocation getItemModel() {
        return GtUtil.getModelResourceLocation(this.name, "nuclear");
    }

    private static class ItemStackPos {
        public final ItemStack stack;
        public final int x;
        public final int y;

        public ItemStackPos(ItemStack stack, int x, int y) {
            this.stack = stack;
            this.x = x;
            this.y = y;
        }
    }
}
