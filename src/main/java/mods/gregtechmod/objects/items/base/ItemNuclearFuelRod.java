package mods.gregtechmod.objects.items.base;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.reactor.ItemReactorUranium;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemNuclearFuelRod extends ItemReactorUranium implements IModelInfoProvider {
    private final String name;
    private final ItemStack depletedStack;
    private final float energy;
    private final int radioation;
    private final float heat;

    public ItemNuclearFuelRod(String name, int cells, int duration, float energy, int radiation, float heat, @Nullable ItemStack depletedStack) {
        super(null, cells, duration);
        setMaxStackSize(1);
        this.name = name;
        this.depletedStack = depletedStack;
        this.energy = energy;
        this.radioation = radiation;
        this.heat = heat;
    }

    @Override
    public String getTranslationKey() {
        return GregTechMod.MODID+".item."+this.name;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("Time left: "+(getMaxCustomDamage(stack) - getCustomDamage(stack)) + " secs");
    }

    @Override
    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        if (this.depletedStack != null) return this.depletedStack;
        return super.getDepletedStack(stack, reactor);
    }

    private void checkHeatAcceptor(IReactor reactor, int x, int y, ArrayList<ItemStackCoord> heatAcceptors) {
        ItemStack thing = reactor.getItemAt(x, y);
        if ((thing != null) && ((thing.getItem() instanceof IReactorComponent)) &&
                (((IReactorComponent) thing.getItem()).canStoreHeat(thing, reactor, x, y))) {
            heatAcceptors.add(new ItemStackCoord(thing, x, y));
        }
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) reactor.addOutput(1.0F * this.energy);
        return true;
    }

    //GT5U Code
    @Override
    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
        if (!reactor.produceEnergy()) {
            return;
        }
        for (int iteration = 0; iteration < this.numberOfCells; iteration++) {
            int pulses = 1 + this.numberOfCells / 2;
            if (!heatRun) {
                for (int i = 0; i < pulses; i++) {
                    acceptUraniumPulse(stack, reactor, stack, x, y, x, y, heatRun);
                }
                checkPulseable(reactor, x - 1, y, stack, x, y, heatRun);checkPulseable(reactor, x + 1, y, stack, x, y, heatRun);checkPulseable(reactor, x, y - 1, stack, x, y, heatRun);checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
            } else {
                pulses += checkPulseable(reactor, x - 1, y, stack, x, y, heatRun) + checkPulseable(reactor, x + 1, y, stack, x, y, heatRun) + checkPulseable(reactor, x, y - 1, stack, x, y, heatRun) + checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);

                int heat = triangularNumber(pulses) * 4;

                heat = getFinalHeat(stack, reactor, x, y, heat);

                ArrayList<ItemStackCoord> heatAcceptors = new ArrayList();
                checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);
                heat = Math.round(heat * this.heat);
                while ((heatAcceptors.size() > 0) && (heat > 0)) {

                    int dheat = heat / heatAcceptors.size();
                    heat -= dheat;
                    dheat = ((IReactorComponent) heatAcceptors.get(0).stack.getItem()).alterHeat(heatAcceptors.get(0).stack, reactor, heatAcceptors.get(0).x, heatAcceptors.get(0).y, dheat);
                    heat += dheat;
                    heatAcceptors.remove(0);
                }
                if (heat > 0) {
                    reactor.addHeat(heat);
                }
            }
        }
        if (getCustomDamage(stack) >= getMaxCustomDamage(stack) - 1) {
            reactor.setItemAt(x, y, this.getDepletedStack(stack, reactor));
        } else if (heatRun) {
            this.applyCustomDamage(stack, 1, null);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (this.radioation > 0 && (entity instanceof EntityLivingBase)) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 200, this.radioation * 100);
            }
        }
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, "nuclear"));
    }

    private static class ItemStackCoord {
        public final ItemStack stack;
        public final int x;
        public final int y;

        public ItemStackCoord(ItemStack stack, int x, int y) {
            this.stack = stack;
            this.x = x;
            this.y = y;
        }
    }
}
