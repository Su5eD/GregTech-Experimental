package mods.gregtechmod.objects;

import ic2.api.item.ITeBlockSpecialItem;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.ITeBlock;
import ic2.core.block.ITeBlock.ITeBlockCreativeRegisterer;
import ic2.core.block.TileEntityBlock;
import ic2.core.item.block.ItemBlockTileEntity;
import ic2.core.profile.Version;
import ic2.core.ref.IC2Material;
import ic2.core.ref.TeBlock.DefaultDrop;
import ic2.core.ref.TeBlock.HarvestTool;
import ic2.core.util.Util;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.*;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import mods.gregtechmod.objects.blocks.teblocks.energy.*;
import mods.gregtechmod.objects.blocks.teblocks.generator.*;
import mods.gregtechmod.objects.blocks.teblocks.inv.*;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.*;
import mods.gregtechmod.objects.blocks.teblocks.struct.*;
import mods.gregtechmod.util.LazyValue;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import static mods.gregtechmod.objects.GregTechTEBlock.ActiveType.*;

public enum GregTechTEBlock implements ITeBlock, ITeBlockCreativeRegisterer, ITeBlockSpecialItem {
    INDUSTRIAL_CENTRIFUGE(TileEntityIndustrialCentrifuge.class, 1, ACTIVE_GUI, Util.onlyNorth),
    DIGITAL_CHEST(TileEntityDigitalChest.class, 2, NO_ACTIVE, Util.allFacings),
    QUANTUM_CHEST(TileEntityQuantumChest.class, 3, NO_ACTIVE, Util.allFacings),
    QUANTUM_TANK(TileEntityQuantumTank.class, 4, NO_ACTIVE, Util.horizontalFacings),
    SONICTRON(TileEntitySonictron.class, 5, ACTIVE_GUI, Util.noFacings, ModelType.DEFAULT),
    AUTO_MACERATOR(TileEntityAutoMacerator.class, 6, ACTIVE_GUI, Util.horizontalFacings),
    AUTO_EXTRACTOR(TileEntityAutoExtractor.class, 7, ACTIVE_GUI, Util.horizontalFacings),
    AUTO_COMPRESSOR(TileEntityAutoCompressor.class, 8, ACTIVE_GUI, Util.horizontalFacings),
    AUTO_RECYCLER(TileEntityAutoRecycler.class, 9, ACTIVE_GUI, Util.horizontalFacings),
    AUTO_ELECTRIC_FURNACE(TileEntityAutoElectricFurnace.class, 10, ACTIVE_GUI, Util.horizontalFacings),
    WIREMILL(TileEntityWiremill.class, 11, ACTIVE_GUI, Util.horizontalFacings),
    GT_ALLOY_SMELTER(TileEntityAlloySmelter.class, 12, ACTIVE_GUI, Util.horizontalFacings),
    AUTO_CANNER(TileEntityAutoCanner.class, 13, ACTIVE_GUI, Util.horizontalFacings),
    BENDER(TileEntityBender.class, 14, ACTIVE_GUI, Util.horizontalFacings),
    UNIVERSAL_MACERATOR(TileEntityUniversalMacerator.class, 15, ACTIVE_GUI, Util.horizontalFacings),
    MICROWAVE(TileEntityMicrowave.class, 16, ACTIVE_GUI, Util.horizontalFacings),
    PRINTER(TileEntityPrinter.class, 17, ACTIVE_GUI, Util.horizontalFacings),
    ASSEMBLER(TileEntityAssembler.class, 18, ACTIVE_GUI, Util.horizontalFacings),
    LATHE(TileEntityLathe.class, 19, ACTIVE_GUI, Util.horizontalFacings),
    INDUSTRIAL_ELECTROLYZER(TileEntityIndustrialElectrolyzer.class, 20, ACTIVE_GUI, Util.onlyNorth),
    CHEMICAL_REACTOR(TileEntityChemicalReactor.class, 21, ACTIVE_GUI, Util.onlyNorth),
    INDUSTRIAL_BLAST_FURNACE(TileEntityIndustrialBlastFurnace.class, 22, NO_ACTIVE, Util.horizontalFacings, true),
    INDUSTRIAL_GRINDER(TileEntityIndustrialGrinder.class, 23, NO_ACTIVE, Util.horizontalFacings, true),
    IMPLOSION_COMPRESSOR(TileEntityImplosionCompressor.class, 24, NO_ACTIVE, Util.horizontalFacings, true),
    VACUUM_FREEZER(TileEntityVacuumFreezer.class, 25, NO_ACTIVE, Util.horizontalFacings, true),
    DISTILLATION_TOWER(TileEntityDistillationTower.class, 26, NO_ACTIVE, Util.horizontalFacings, true),
    THERMAL_GENERATOR(TileEntityThermalGenerator.class, 27, ACTIVE_GUI, Util.horizontalFacings),
    DIESEL_GENERATOR(TileEntityDieselGenerator.class, 28, ACTIVE_GUI, Util.horizontalFacings),
    GT_SEMIFLUID_GENERATOR(TileEntitySemifluidGenerator.class, 29, NO_ACTIVE, Util.horizontalFacings),
    GAS_TURBINE(TileEntityGasTurbine.class, 30, NO_ACTIVE, Util.horizontalFacings),
    PLASMA_GENERATOR(TileEntityPlasmaGenerator.class, 31, ACTIVE_GUI, Util.allFacings),
    MAGIC_ENERGY_CONVERTER(TileEntityMagicEnergyConverter.class, 32, ACTIVE_GUI, Util.allFacings),
    LIGHTNING_ROD(TileEntityLightningRod.class, 33, NO_ACTIVE, Util.onlyNorth),
    MAGIC_ENERGY_ABSORBER(TileEntityMagicEnergyAbsorber.class, 34, ACTIVE_GUI, Util.noFacings),
    DRAGON_EGG_ENERGY_SIPHON(TileEntityDragonEggEnergySiphon.class, 35, ACTIVE_GUI, Util.noFacings),
    HATCH_INPUT(TileEntityHatchInput.class, 36, NO_ACTIVE, Util.allFacings),
    HATCH_OUTPUT(TileEntityHatchOutput.class, 37, NO_ACTIVE, Util.allFacings),
    HATCH_MAINTENANCE(TileEntityHatchMaintenance.class, 38, NO_ACTIVE, Util.allFacings),
    HATCH_DYNAMO(TileEntityHatchDynamo.class, 39, NO_ACTIVE, Util.allFacings),
    HATCH_MUFFLER(TileEntityHatchMuffler.class, 40, NO_ACTIVE, Util.allFacings),
    INDUSTRIAL_SAWMILL(TileEntityIndustrialSawmill.class, 41, NO_ACTIVE, Util.horizontalFacings, true),
    THERMAL_BOILER(TileEntityThermalBoiler.class, 42, ACTIVE_GUI, Util.horizontalFacings),
    LARGE_STEAM_TURBINE(TileEntityLargeSteamTurbine.class, 43, ACTIVE_GUI, Util.horizontalFacings),
    LARGE_GAS_TURBINE(TileEntityLargeGasTurbine.class, 44, ACTIVE_GUI, Util.horizontalFacings),
    SUPERCONDENSATOR(TileEntitySupercondensator.class, 45, NO_ACTIVE, Util.allFacings),
    SUPERCONDUCTOR_WIRE(TileEntitySuperconductorWire.class, 46, NO_ACTIVE, Util.noFacings, ModelType.CONNECTED),
    LESU(TileEntityLESU.class, 47, NO_ACTIVE, Util.allFacings),
    AESU(TileEntityAESU.class, 48, NO_ACTIVE, Util.allFacings),
    IDSU(TileEntityIDSU.class, 49, NO_ACTIVE, Util.allFacings),
    COMPUTER_CUBE(TileEntityComputerCube.class, 50, NO_ACTIVE, Util.horizontalFacings),
    CHARGE_O_MAT(TileEntityChargeOMat.class, 51, NO_ACTIVE, Util.horizontalFacings),
    ADVANCED_PUMP(TileEntityAdvancedPump.class, 52, ACTIVE_GUI, Util.horizontalFacings),
    ADVANCED_SAFE(TileEntityAdvancedSafe.class, 53, NO_ACTIVE, Util.horizontalFacings),
    MATTER_FABRICATOR(TileEntityMatterFabricator.class, 54, ACTIVE_GUI, Util.noFacings),
    GT_TELEPORTER(TileEntityGtTeleporter.class, 55, ACTIVE_GUI, Util.allFacings),
    TESSERACT_GENERATOR(TileEntityTesseractGenerator.class, 56, NO_ACTIVE, Util.allFacings),
    TESSERACT_TERMINAL(TileEntityTesseractTerminal.class, 57, NO_ACTIVE, Util.allFacings),
    ELECTRIC_BUFFER_SMALL(TileEntityElectricBufferSmall.class, 58, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_BUFFER_LARGE(TileEntityElectricBufferLarge.class, 59, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_BUFFER_ADVANCED(TileEntityElectricBufferAdvanced.class, 60, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_ITEM_CLEARER(TileEntityElectricItemClearer.class, 61, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    CROP_HARVESTOR(TileEntityCropHarvestor.class, 62, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_ROCK_BREAKER(TileEntityElectricRockBreaker.class, 63, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    SCRAPBOXINATOR(TileEntityScrapboxinator.class, 64, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_SORTER(TileEntityElectricSorter.class, 65, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_TYPE_SORTER(TileEntityElectricTypeSorter.class, 66, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_TRANSLOCATOR(TileEntityElectricTranslocator.class, 67, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_TRANSLOCATOR_ADVANCED(TileEntityElectricTranslocatorAdvanced.class, 68, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_REGULATOR_ADVANCED(TileEntityElectricRegulatorAdvanced.class, 69, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    ELECTRIC_INVENTORY_MANAGER(TileEntityElectricInventoryManager.class, 70, NO_ACTIVE, Util.onlyNorth),
    ELECTRIC_CRAFTING_TABLE(TileEntityElectricCraftingTable.class, 71, NO_ACTIVE, Util.allFacings, ModelType.ELECTRIC_BUFFER),
    PLAYER_DETECTOR(TileEntityPlayerDetector.class, 72, ACTIVE, Util.noFacings),
    REDSTONE_NOTE_BLOCK(TileEntityRedstoneNoteblock.class, 73, NO_ACTIVE, Util.allFacings),
    BUTTON_PANEL(TileEntityButtonPanel.class, 74, ACTIVE, Util.allFacings, ModelType.BUTTON_PANEL),
    REDSTONE_DISPLAY(TileEntityRedstoneDisplay.class, 75, NO_ACTIVE, Util.allFacings, ModelType.REDSTONE_DISPLAY),
    REDSTONE_SCALE(TileEntityRedstoneScale.class, 76, NO_ACTIVE, Util.allFacings, ModelType.REDSTONE_SCALE),
    WOOD_SHELF(TileEntityShelfWood.class, 77, NO_ACTIVE, Util.horizontalFacings, ModelType.SHELF),
    METAL_SHELF(TileEntityShelfMetal.class, 78, NO_ACTIVE, Util.horizontalFacings, ModelType.SHELF),
    FILE_CABINET(TileEntityFileCabinet.class, 79, NO_ACTIVE, Util.horizontalFacings),
    METAL_DESK(TileEntityMetalDesk.class, 80, NO_ACTIVE, Util.horizontalFacings),
    COMPARTMENT(TileEntityCompartment.class, 81, NO_ACTIVE, Util.horizontalFacings, ModelType.COMPARTMENT),
    MACHINE_BOX(TileEntityMachineBox.class, 82, NO_ACTIVE, Util.allFacings, ModelType.MACHINE_BOX);

    public static final ResourceLocation LOCATION = new ResourceLocation(Reference.MODID, "teblock");
    public static final GregTechTEBlock[] VALUES = values();
    public static BlockTileEntity blockTE;

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final ActiveType activeType;
    private final Set<EnumFacing> supportedFacings;
    private final DefaultDrop defaultDrop;
    private final float hardness;
    private final float explosionResistance;
    private final EnumRarity rarity;
    private final ModelType modelType;
    private final boolean isStructure;

    private final LazyValue<TileEntityBlock> dummyTe = new LazyValue<>(this::buildDummyTeBlock);

    GregTechTEBlock(Class<? extends TileEntityBlock> teClass, int itemMeta, ActiveType activeType, Set<EnumFacing> supportedFacings) {
        this(teClass, itemMeta, activeType, supportedFacings, false);
    }

    GregTechTEBlock(Class<? extends TileEntityBlock> teClass, int itemMeta, ActiveType activeType, Set<EnumFacing> supportedFacings, ModelType modelType) {
        this(teClass, itemMeta, activeType, supportedFacings, modelType, false);
    }

    GregTechTEBlock(Class<? extends TileEntityBlock> teClass, int itemMeta, ActiveType activeType, Set<EnumFacing> supportedFacings, boolean isStructure) {
        this(teClass, itemMeta, activeType, supportedFacings, ModelType.BAKED, isStructure);
    }

    GregTechTEBlock(Class<? extends TileEntityBlock> teClass, int itemMeta, ActiveType activeType, Set<EnumFacing> supportedFacings, ModelType modelType, boolean isStructure) {
        this(teClass, itemMeta, activeType, supportedFacings, DefaultDrop.Self, 10, 30, EnumRarity.COMMON, modelType, isStructure);
    }

    GregTechTEBlock(Class<? extends TileEntityBlock> teClass, int itemMeta, ActiveType activeType, Set<EnumFacing> supportedFacings, DefaultDrop defaultDrop, float hardness, float explosionResistance, EnumRarity rarity, ModelType modelType, boolean isStructure) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.activeType = activeType;
        this.supportedFacings = supportedFacings;
        this.defaultDrop = defaultDrop;
        this.hardness = hardness;
        this.explosionResistance = explosionResistance;
        this.rarity = rarity;
        this.modelType = modelType;
        this.isStructure = isStructure;

        GameRegistry.registerTileEntity(teClass, new ResourceLocation(Reference.MODID, getName()));
        NBTSaveHandler.initClass(teClass);
    }

    public boolean isStructure() {
        return this.isStructure;
    }

    public ModelType getModelType() {
        return this.modelType;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return LOCATION;
    }

    @Override
    public boolean hasItem() {
        return true;
    }

    @Nonnull
    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return this.activeType.active;
    }

    @Override
    public Set<EnumFacing> getSupportedFacings() {
        return this.supportedFacings;
    }

    @Override
    public float getHardness() {
        return this.hardness;
    }

    @Override
    public float getExplosionResistance() {
        return this.explosionResistance;
    }

    @Override
    public HarvestTool getHarvestTool() {
        return HarvestTool.Wrench;
    }

    @Override
    public DefaultDrop getDefaultDrop() {
        return this.defaultDrop;
    }

    @Override
    public EnumRarity getRarity() {
        return this.rarity;
    }

    @Override
    public boolean allowWrenchRotating() {
        return !getSupportedFacings().isEmpty();
    }

    @Override
    public Material getMaterial() {
        return IC2Material.MACHINE;
    }

    @Nullable
    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe.get();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    @Override
    public void addSubBlocks(NonNullList<ItemStack> list, BlockTileEntity block, ItemBlockTileEntity item, CreativeTabs tab) {
        if (tab == CreativeTabs.SEARCH) {
            Arrays.stream(VALUES)
                .filter(teblock -> Version.shouldEnable(teblock.teClass))
                .map(block::getItemStack)
                .forEach(list::add);
        }
    }

    public TileEntityBlock buildDummyTeBlock() {
        if (this.teClass != null) {
            try {
                GregTechMod.LOGGER.debug("Building dummy TeBlock for {}", getName());
                return this.teClass.newInstance();
            } catch (Exception e) {
                GregTechMod.LOGGER.catching(e);
            }
        }
        return null;
    }

    @Override
    public boolean doesOverrideDefault(ItemStack stack) {
        return this.modelType != ModelType.DEFAULT;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        String name = getName();
        String location = Reference.MODID + ":teblock/" + name;

        if (this.isStructure) location += "_valid";
        else if (this.activeType.gui) location += "_active";

        return new ModelResourceLocation(location, name);
    }

    public enum ModelType {
        DEFAULT,
        BAKED,
        CONNECTED,
        ELECTRIC_BUFFER,
        BUTTON_PANEL,
        REDSTONE_DISPLAY,
        REDSTONE_SCALE,
        SHELF,
        COMPARTMENT,
        MACHINE_BOX;
    }
    
    public enum ActiveType {
        ACTIVE(true, false),
        ACTIVE_GUI(true, true),
        NO_ACTIVE(false, false);
        
        private final boolean active;
        private final boolean gui;

        ActiveType(boolean active, boolean gui) {
            this.active = active;
            this.gui = gui;
        }
    }
}

