package mods.gregtechmod.core;

import ic2.api.item.ITeBlockSpecialItem;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.item.block.ItemBlockTileEntity;
import ic2.core.ref.IC2Material;
import ic2.core.ref.TeBlock.DefaultDrop;
import ic2.core.ref.TeBlock.HarvestTool;
import ic2.core.ref.TeBlock.ITePlaceHandler;
import ic2.core.util.Util;
import mods.gregtechmod.objects.blocks.tileentities.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityDigitalChest;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityQuantumChest;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityQuantumTank;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public enum GregtechTeBlock implements ITeBlock, ITeBlock.ITeBlockCreativeRegisterer, ITeBlockSpecialItem {
    industrial_centrifuge(TileEntityIndustrialCentrifuge.class, 1, true, Collections.singleton(EnumFacing.NORTH), false, HarvestTool.Wrench, DefaultDrop.Machine, 5, 10, EnumRarity.COMMON, IC2Material.MACHINE, false, true),
    digital_chest(TileEntityDigitalChest.class, 2, false, Util.allFacings, true, HarvestTool.Wrench, DefaultDrop.Machine, 5, 10, EnumRarity.COMMON, IC2Material.MACHINE, false, true),
    quantum_chest(TileEntityQuantumChest.class, 3, false, Util.allFacings, true, HarvestTool.Wrench, DefaultDrop.AdvMachine, 5, 10, EnumRarity.UNCOMMON, IC2Material.MACHINE, false, true),
    quantum_tank(TileEntityQuantumTank.class, 4, false, Collections.singleton(EnumFacing.NORTH), false, HarvestTool.Wrench, DefaultDrop.AdvMachine, 5, 10, EnumRarity.UNCOMMON, IC2Material.MACHINE, false, true),
    sonictron(TileEntitySonictron.class, 5, true, Collections.emptySet(), false, HarvestTool.Wrench, DefaultDrop.Self, 5, 10, EnumRarity.COMMON, Material.IRON, false, false);

    public static final ResourceLocation LOCATION;
    private final int itemMeta;
    private final boolean hasActive;
    private final Set<EnumFacing> supportedFacings;
    private final boolean allowWrenchRotating;
    private final HarvestTool harvestTool;
    private final DefaultDrop defaultDrop;
    private final float hardness;
    private final float explosionResistance;
    private final EnumRarity rarity;
    private final Material material;
    private final boolean transparent;
    private Class<? extends TileEntityBlock> teClass;
    public static final GregtechTeBlock[] VALUES;
    private TileEntityBlock dummyTe;
    private ITePlaceHandler placeHandler;
    private final boolean hasBakedModel;

    @SuppressWarnings("deprecation")
    GregtechTeBlock(Class<? extends TileEntityBlock> teClass, int itemMeta, boolean hasActive, Set<EnumFacing> supportedFacings, boolean allowWrenchRotating, HarvestTool harvestTool, DefaultDrop defaultDrop, float hardness, float explosionResistance, EnumRarity rarity, Material material, boolean transparent, boolean hasBakedModel) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.hasActive = hasActive;
        this.supportedFacings = supportedFacings;
        this.allowWrenchRotating = allowWrenchRotating;
        this.harvestTool = harvestTool;
        this.defaultDrop = defaultDrop;
        this.hardness = hardness;
        this.explosionResistance = explosionResistance;
        this.rarity = rarity;
        this.material = material;
        this.transparent = transparent;
        this.hasBakedModel = hasBakedModel;

        if(teClass != null) {
            GameRegistry.registerTileEntity(teClass, "gregtechmod:" + getName());
        }
 	}
    static {
        LOCATION = new ResourceLocation("gregtechmod", "teblock");
        VALUES = values();
    }

    public boolean hasBakedModel() {
        return this.hasBakedModel;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return LOCATION;
    }

    @Override
    public boolean hasItem() {
        return (this.teClass != null && this.itemMeta != -1);
    }

    @Nullable
    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return this.hasActive;
    }

    @Override
    public java.util.Set<EnumFacing> getSupportedFacings() {
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
    public ic2.core.ref.TeBlock.HarvestTool getHarvestTool() {
        return this.harvestTool;
    }

    @Override
    public DefaultDrop getDefaultDrop() {
        return this.defaultDrop;
    }

    @Override
    public net.minecraft.item.EnumRarity getRarity() {
        return this.rarity;
    }

    @Override
    public boolean allowWrenchRotating() {
        return this.allowWrenchRotating;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public void setPlaceHandler(ITePlaceHandler handler) {
        if (this.placeHandler != null)
            throw new RuntimeException("duplicate place handler");
        this.placeHandler = handler;
    }

    @Nullable
    @Override
    public ITePlaceHandler getPlaceHandler() {
        return this.placeHandler;
    }

    @Nullable
    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    public void addSubBlocks(NonNullList<ItemStack> list, BlockTileEntity block, ItemBlockTileEntity item, CreativeTabs tab) {
        block.setCreativeTab(GregtechMod.GREGTECH_TAB);
        if (tab == CreativeTabs.SEARCH)
            for (GregtechTeBlock type : VALUES) {
                if (type.hasItem()) {
                    list.add(block.getItemStack(type));
                }
            }
    }
    public static void buildDummies() {
 		for (GregtechTeBlock block : values()) {
 			if (block.teClass != null) {
 				try {
                    GregtechMod.LOGGER.info("Building dummy TeBlock for "+block.name());
 					block.dummyTe = block.teClass.newInstance();
 				} catch (Exception e) {
 					GregtechMod.LOGGER.error(e.getMessage());
 				}
 			}
 		}
 	}

    @Override
    public boolean doesOverrideDefault(ItemStack itemStack) {
        return this.hasBakedModel;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack itemStack) {
        String location = GregtechMod.MODID+":teblock/"+this.name();
        return new ModelResourceLocation(location, this.name());
    }
}

