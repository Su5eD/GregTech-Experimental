package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.api.energy.EnergyNet;
import ic2.core.block.comp.Energy;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.*;

public abstract class TileEntityEnergy extends TileEntityCoverBehavior {
    protected Energy energy;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    
    protected double[] averageEUInputs = new double[] { 0,0,0,0,0 };
    protected double[] averageEUOutputs = new double[] { 0,0,0,0,0 };
    protected int averageEUInputIndex = 0;
    protected int averageEUOutputIndex = 0;
    private double previousEU;
    public double averageEUIn;
    public double averageEUOut;

    public TileEntityEnergy(double maxEnergy, int defaultTier) {
        this.energy = addComponent(new Energy(this, maxEnergy, getSinkDirs(), getSourceDirs(), defaultTier));
        this.defaultTier = defaultTier;
        this.defaultEnergyStorage = maxEnergy;
    }
    
    protected Set<EnumFacing> getSourceDirs() {
        return Collections.emptySet();
    }
    
    @Override
    public int getSinkTier() {
        return this.energy.getSinkTier();
    }

    @Override
    public int getSourceTier() {
        return this.energy.getSourceTier();
    }
    
    @Override
    public void addEnergy(double amount) {
        if (amount > getMaxInputEUp()) markForExplosion();
        this.energy.addEnergy(amount);
    }
    
    @Override
    public double getMaxInputEUp() {
        return EnergyNet.instance.getPowerFromTier(getSinkTier());
    }
    
    @Override
    public double getMaxOutputEUp() {
        return EnergyNet.instance.getPowerFromTier(getSourceTier());
    }
    
    protected int getOutputPackets() {
        return 1;
    }
    
    @Override
    public double getStoredEU() {
        return this.energy.getEnergy();
    }
    
    @Override
    public double getEUCapacity() {
        return this.energy.getCapacity();
    }
    
    @Override
    public double getAverageEUInput() {
        return this.averageEUIn;
    }
    
    @Override
    public double getAverageEUOutput() {
        return this.averageEUOut;
    }

    @Override
    public void updateEnet() {
        Set<EnumFacing> sinkDirs = new HashSet<>(getSinkDirs());
        this.coverHandler.covers.entrySet().stream()
                .filter(entry -> !entry.getValue().allowEnergyTransfer())
                .map(Map.Entry::getKey)
                .forEach(sinkDirs::remove);
    
        Set<EnumFacing> oldSourceDirs = this.energy.getSourceDirs();
        Set<EnumFacing> sourceDirs = new HashSet<>(getSourceDirs());
        this.energy.setDirections(sinkDirs, sourceDirs);
    
        updateSourceTier();
    
        if (oldSourceDirs.size() != sourceDirs.size() || !oldSourceDirs.containsAll(sourceDirs) && !sourceDirs.containsAll(oldSourceDirs)) {
            this.energy.onUnloaded();
            this.energy.onLoaded();
        }
    }
    
    protected void updateSourceTier() {
        int packetCount = getOutputPackets();
        this.energy.setMultiSource(packetCount > 1);
        this.energy.setPacketOutput(packetCount);
    
        int sourceTier = getSourceTier();
        this.energy.setSourceTier(sourceTier);
    }
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        double currentEU = this.energy.getEnergy();
        double input = currentEU - previousEU;
        this.previousEU = currentEU;
            
        if (input > 0) averageEUInputs[averageEUInputIndex] = input;
        if (++averageEUInputIndex >= averageEUInputs.length) averageEUInputIndex = 0;
        if (++averageEUOutputIndex >= averageEUOutputs.length) averageEUOutputIndex = 0;
        
        if (!this.energy.getSinkDirs().isEmpty()) {
            double sum = Arrays.stream(averageEUInputs).sum();
            averageEUIn = sum / averageEUInputs.length;
        } else averageEUIn = 0;
        
        if (!this.energy.getSourceDirs().isEmpty()) {
            double sum = Arrays.stream(averageEUOutputs).sum();
            averageEUOut = sum / averageEUOutputs.length;
        } else averageEUOut = 0;
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GtUtil.translateInfo("max_energy_in", Math.round(getMaxInputEUp())));
    }
}
