package mods.gregtechmod.objects.blocks.teblocks.fusion;

import ic2.core.IHasGui;
import ic2.core.network.GrowingBuffer;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.Either;
import mods.gregtechmod.gui.GuiFusionReactor;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.AdjustableEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFusionReactor;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.struct.Structure;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import one.util.streamex.StreamEx;

import java.io.DataInput;
import java.io.IOException;
import java.util.*;

public class TileEntityFusionComputer extends TileEntityUpgradable implements IHasGui, IMachineProgress, IPanelInfoProvider {
    public final Structure<FusionReactorStructure> structure;

    @NBTPersistent
    public double progress;
    @NBTPersistent
    public int maxProgress;
    @NBTPersistent
    public double energyConsume;
    @NBTPersistent
    private Either<ItemStack, FluidStack> pendingRecipe;
    
    private int startupCheck;

    public TileEntityFusionComputer() {
        this.structure = new Structure<>(getStructurePattern(), getStructureElements(), this::createStructureInstance, this::onInvalidate);
    }

    /*
     X - this
     H - Advanced Machine Casing
     C - Fusion Coil
     E - Fusion Energy Injector
     I - Fusion Material Injector (Primary)
     M - Fusion Material Injector (Secondary)
     P - Fusion Material Extractor
     */
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
            Arrays.asList(
                "               ",
                "      IHI      ",
                "    HH   HH    ",
                "   H       H   ",
                "  H         H  ",
                "  H         H  ",
                " I           I ",
                " H           H ",
                " I           I ",
                "  H         H  ",
                "  H         H  ",
                "   H       H   ",
                "    HH   HH    ",
                "      IHI      ",
                "               "
            ),
            Arrays.asList(
                "      PHP      ",
                "    HHCCCHH    ",
                "   ECCPHPCCE   ",
                "  ECEH   HECE  ",
                " HCE       ECH ",
                " HCH       HCH ",
                "PCP         PCP",
                "HCH         HCH",
                "PCP         PCP",
                " HCH       HCH ",
                " HCE       ECH ",
                "  ECEH   HECE  ",
                "   ECCPXPCCE   ",
                "    HHCCCHH    ",
                "      PHP      "
            ),
            Arrays.asList(
                "               ",
                "      MHM      ",
                "    HH   HH    ",
                "   H       H   ",
                "  H         H  ",
                "  H         H  ",
                " M           M ",
                " H           H ",
                " M           M ",
                "  H         H  ",
                "  H         H  ",
                "   H       H   ",
                "    HH   HH    ",
                "      MHM      ",
                "               "
            )
        );
    }

    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        return new StructureElementGatherer(this::getWorld)
            .block('H', BlockItems.Block.ADVANCED_MACHINE_CASING.getBlockInstance())
            .block('C', BlockItems.Block.FUSION_COIL.getBlockInstance())
            .id('P', builder -> builder
                .parent('H')
                .tileEntity(TileEntityFusionMaterialExtractor.class))
            .id('M', builder -> builder
                .parent('H')
                .tileEntity(TileEntityFusionMaterialInjector.class))
            .id('I', builder -> builder
                .parent('M'))
            .id('E', builder -> builder
                .parent('H')
                .tileEntity(TileEntityFusionEnergyInjector.class))
            .gather();
    }

    protected FusionReactorStructure createStructureInstance(EnumFacing facing, Map<Character, Collection<BlockPos>> elements) {
        return new FusionReactorStructure((FusionEnergy) this.energy, this.world, elements);
    }
    
    private void onInvalidate(FusionReactorStructure instance) {
        setActive(false, instance);
        ((FusionEnergy) this.energy).setEnergyInjectors(Collections.emptyList());
        this.startupCheck = 100;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.structure.checkWorldStructure(this.pos, this.getFacing());
        if (!getActive()) this.startupCheck = 100;
    }

    @Override
    protected AdjustableEnergy createEnergyComponent() {
        return new FusionEnergy();
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    protected void onBlockBreak() {
        super.onBlockBreak();
        this.structure.getWorldStructure()
            .map(Structure.WorldStructure::getInstance)
            .ifPresent(this::onInvalidate);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (this.tickCounter % 5 == 0) {
            this.structure.checkWorldStructure(this.pos, this.getFacing());
        }

        Optional<Structure<FusionReactorStructure>.WorldStructure> struct = this.structure.getWorldStructure();
        if (!struct.map(Structure.WorldStructure::isValid).orElse(false)) {
            stop();
            return;
        }
        
        if (this.startupCheck > 0) this.startupCheck--;
        
        FusionReactorStructure instance = struct.get().getInstance();
        if (isProcessing()) {
            if (canUseEnergy(this.energyConsume)) {
                processRecipe(instance);
            }
            else stop(instance);
        }
        else {
            IRecipeFusion recipe = getRecipe(instance);
            if (this.startupCheck == 0 && canProcessRecipe(recipe)) {
                this.energyConsume = recipe.getEnergyCost();

                if (canUseEnergy(this.energyConsume)) {
                    if (this.maxProgress <= 0 && this.pendingRecipe == null) {
                        prepareRecipeForProcessing(instance, recipe);
                    }
                    processRecipe(instance);
                }
                else stop(instance);
            }
            else stop(instance);
        }
    }
    
    public IRecipeFusion getRecipe(FusionReactorStructure instance) {
        List<FluidStack> input = instance.getInput();
        return GtRecipes.fusion.getRecipeFor(input);
    }
    
    public boolean canProcessRecipe(IRecipeFusion recipe) {
        return recipe != null && isAllowedToWork() && (getActive() || canUseEnergy(recipe.getStartEnergy()));
    }
    
    public void prepareRecipeForProcessing(FusionReactorStructure instance, IRecipeFusion recipe) {
        this.pendingRecipe = recipe.getOutput();
        this.maxProgress = recipe.getDuration();
        consumeInput(instance, recipe);
    }
    
    public void consumeInput(FusionReactorStructure instance, IRecipeFusion recipe) {
        List<IRecipeIngredientFluid> ingredients = recipe.getInput();
        StreamEx.of(instance.getPrimaryMaterialInjectors(), instance.getSecondaryMaterialInjectors())
            .forEach(injectors -> StreamEx.of(injectors)
                .mapToEntry(injector -> injector.tank.content.drain(Integer.MAX_VALUE, false))
                .nonNullValues()
                .flatMapValues(fluid -> StreamEx.of(ingredients)
                    .filter(ingredient -> ingredient.apply(fluid))
                    .limit(1))
                .forKeyValue((injector, ingredient) -> injector.tank.content.drain(ingredient.getMilliBuckets(), true)));
        if (!getActive()) useEnergy(recipe.getStartEnergy());
    }
    
    public boolean isProcessing() {
        return isAllowedToWork() && (this.maxProgress > 0 || this.pendingRecipe != null);
    }
    
    private void processRecipe(FusionReactorStructure instance) {
        if (!getActive()) setActive(true, instance);
        
        useEnergy(this.energyConsume);
        if (++this.progress >= this.maxProgress) {
            addOutput(this.pendingRecipe);
            this.energyConsume = 0;
            this.progress = 0;
            this.maxProgress = 0;
            this.pendingRecipe = null;
            markDirty();
        }
    }
    
    private void addOutput(Either<ItemStack, FluidStack> output) {
        this.structure.getWorldStructure()
            .map(Structure.WorldStructure::getInstance)
            .map(FusionReactorStructure::getEnergyExtractors)
            .flatMap(extractors -> extractors.stream()
                .filter(te -> output.mapWhen(te::canAddOutput, te::canAddOutput))
                .findFirst())
            .ifPresent(te -> output.when(te::addOutput, te::addOutput));
    }
    
    private void stop(FusionReactorStructure instance) {
        if (getActive()) setActive(false, instance);
        stop();
    }
    
    private void stop() {
        this.progress = 0;
        this.maxProgress = 0;
        this.energyConsume = 0;
        this.pendingRecipe = null;
        if (getActive()) setActive(false);
    }
    
    public void setActive(boolean active, FusionReactorStructure instance) {
        setActive(active);
        StreamEx.of(instance.energyInjectors, instance.materialExtractors, instance.primaryMaterialInjectors, instance.secondaryMaterialInjectors)
            .flatMap(Collection::stream)
            .forEach(te -> te.setActive(active));
    }

    @Override
    protected int getBaseEUCapacity() {
        return this.energy.getCapacity();
    }

    @Override
    public double getStoredEU() {
        return this.energy.getStoredEnergy();
    }

    @Override
    public double getProgress() {
        return this.progress;
    }

    @Override
    public int getMaxProgress() {
        return this.maxProgress;
    }

    @Override
    public void increaseProgress(double amount) {
        this.progress += amount;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return EnumSet.of(GtUpgradeType.LOCK);
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }

    @Override
    public ContainerFusionReactor getGuiContainer(EntityPlayer player) {
        return new ContainerFusionReactor(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiFusionReactor(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    @Override
    public String getMainInfo() {
        return GtLocale.translateGeneric("progress") + ":";
    }

    @Override
    public String getSecondaryInfo() {
        double seconds = this.progress / 20;
        return GtLocale.translateGeneric("time_secs", Math.round(seconds));
    }

    @Override
    public String getTertiaryInfo() {
        double seconds = this.maxProgress / 20D;
        return "/" + GtLocale.translateGeneric("time_secs", Math.round(seconds));
    }
    
    @Override
    public boolean isGivingInformation() {
        return this.structure.isValid();
    }
    
    public static class FusionReactorStructure {
        private final List<TileEntityFusionEnergyInjector> energyInjectors;
        private final List<TileEntityFusionMaterialInjector> primaryMaterialInjectors;
        private final List<TileEntityFusionMaterialInjector> secondaryMaterialInjectors;
        private final List<TileEntityFusionMaterialExtractor> materialExtractors;

        public FusionReactorStructure(FusionEnergy energy, World world, Map<Character, Collection<BlockPos>> elements) {
            this.energyInjectors = getComponentTileEntities(world, elements, 'E', TileEntityFusionEnergyInjector.class);
            this.primaryMaterialInjectors = getComponentTileEntities(world, elements, 'I', TileEntityFusionMaterialInjector.class);
            this.secondaryMaterialInjectors = getComponentTileEntities(world, elements, 'M', TileEntityFusionMaterialInjector.class);
            this.materialExtractors = getComponentTileEntities(world, elements, 'P', TileEntityFusionMaterialExtractor.class);
            
            energy.setEnergyInjectors(this.energyInjectors);
        }
        
        public List<TileEntityFusionMaterialExtractor> getEnergyExtractors() {
            return this.materialExtractors;
        }
        
        public List<FluidStack> getInput() {
            return StreamEx.of(this.primaryMaterialInjectors, this.secondaryMaterialInjectors)
                .map(this::getInput)
                .nonNull()
                .toList();
        }

        public List<TileEntityFusionMaterialInjector> getPrimaryMaterialInjectors() {
            return this.primaryMaterialInjectors;
        }

        public List<TileEntityFusionMaterialInjector> getSecondaryMaterialInjectors() {
            return this.secondaryMaterialInjectors;
        }

        private FluidStack getInput(List<TileEntityFusionMaterialInjector> injectors) {
            return StreamEx.of(injectors)
                .map(te -> te.tank.content.drain(Integer.MAX_VALUE, false))
                .nonNull()
                .findFirst()
                .orElse(null);
        }

        private <T> List<T> getComponentTileEntities(World world, Map<Character, Collection<BlockPos>> elements, char id, Class<T> type) {
            return StreamEx.of(elements.get(id))
                .map(world::getTileEntity)
                .select(type)
                .toList();
        }
    }

    public class FusionEnergy extends AdjustableEnergy {
        private List<TileEntityFusionEnergyInjector> energyInjectors = Collections.emptyList();
        private double clientEnergy;

        public FusionEnergy() {
            super(TileEntityFusionComputer.this);
        }

        @Override
        public int getCapacity() {
            return this.energyInjectors.stream()
                .mapToInt(TileEntityEnergy::getEUCapacity)
                .sum();
        }

        @Override
        public double getStoredEnergy() {
            return TileEntityFusionComputer.this.world.isRemote ? this.clientEnergy 
                : this.energyInjectors.stream()
                .mapToDouble(TileEntityEnergy::getStoredEU)
                .sum();
        }

        @Override
        public double discharge(double amount, boolean simulate) {
            double remaining = amount;
            for (TileEntityFusionEnergyInjector injector : this.energyInjectors) {
                if (remaining > 0) {
                    remaining -= injector.useEnergy(Math.min(remaining, injector.getStoredEU()));
                }
                else break;
            }
            
            double discharged = amount - remaining;
            updateAverageEUOutput(discharged);
            return discharged;
        }

        @Override
        public Collection<EnumFacing> getSinkSides() {
            return Util.noFacings;
        }

        @Override
        public Collection<EnumFacing> getSourceSides() {
            return Util.noFacings;
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
        public double getMaxOutputEUp() {
            return 0;
        }

        @Override
        protected double getOfferedEnergy() {
            return 0;
        }

        @Override
        protected DelegateBase createDelegate() {
            return new SinkDelegate();
        }

        @Override
        public void readFromNbt(NBTTagCompound nbt) {}

        @Override
        public NBTTagCompound writeToNbt() {
            return new NBTTagCompound();
        }

        @Override
        public void onContainerUpdate(EntityPlayerMP player) {
            GrowingBuffer buf = new GrowingBuffer(8);
            buf.writeDouble(getStoredEnergy());
            buf.flip();
            
            setNetworkUpdate(player, buf);
        }

        @Override
        public void onNetworkUpdate(DataInput in) throws IOException {
            this.clientEnergy = in.readDouble();
        }

        public void setEnergyInjectors(List<TileEntityFusionEnergyInjector> energyInjectors) {
            this.energyInjectors = energyInjectors;
        }
    }
}
