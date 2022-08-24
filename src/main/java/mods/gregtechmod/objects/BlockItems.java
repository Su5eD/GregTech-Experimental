package mods.gregtechmod.objects;

import com.mojang.authlib.GameProfile;
import ic2.api.item.IC2Items;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.profile.NotExperimental;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.buildcraft.MjHelper;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.*;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityQuantumChest;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityDigitalChestBase;
import mods.gregtechmod.objects.items.*;
import mods.gregtechmod.objects.items.base.*;
import mods.gregtechmod.objects.items.components.ItemLithiumBattery;
import mods.gregtechmod.objects.items.components.ItemTurbineRotor;
import mods.gregtechmod.objects.items.tools.*;
import mods.gregtechmod.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.*;

public class BlockItems {
    private static final String DESCRIPTION_DELEGATE = GregTechMod.classic ? "classic_description" : "description";

    public static net.minecraft.block.Block lightSource;
    public static Item sensorKit;
    public static Item sensorCard;
    public static Map<String, ItemCellClassic> classicCells;

    public enum Block implements IBlockItemProvider {
        ADVANCED_MACHINE_CASING(BlockConnected::new, 3, 30, EnumRarity.UNCOMMON, true, null),
        ALUMINIUM(3, 30),
        BRASS(3.5F, 30),
        CHROME(10, 100),
        ELECTRUM(4, 30),
        FUSION_COIL(4, 30),
        GREEN_SAPPHIRE(4.5F, 30),
        HIGHLY_ADVANCED_MACHINE(10, 100),
        INVAR(4.5F, 30),
        IRIDIUM(3.5F, 600),
        IRIDIUM_REINFORCED_STONE(100, 300),
        IRIDIUM_REINFORCED_TUNGSTEN_STEEL(BlockConnected::new, 200, 400, true),
        LEAD(3, 60),
        LESUBLOCK(BlockLESU::new, 4, 30),
        NICKEL(3, 45),
        OLIVINE(4.5F, 30),
        OSMIUM(4, 900),
        PLATINUM(4, 30),
        REINFORCED_MACHINE_CASING(BlockConnectedTurbine::new, 3, 60, EnumRarity.UNCOMMON, true, "large_gas_turbine"),
        RUBY(4.5F, 30),
        SAPPHIRE(4.5F, 30),
        SILVER(3, 30),
        STANDARD_MACHINE_CASING(BlockConnectedTurbine::new, 3, 30, EnumRarity.COMMON, true, "large_steam_turbine"),
        STEEL(3, 100),
        TITANIUM(10, 200),
        TUNGSTEN(4.5F, 100),
        TUNGSTEN_STEEL(BlockConnected::new, 100, 300, true),
        ZINC(3.5F, 30);

        private final LazyValue<net.minecraft.block.Block> instance;
        private final EnumRarity rarity;
        private final boolean hasConnectedModel;
        @Nullable
        private final String extraTextures;

        Block(float hardness, float resistance) {
            this(() -> new BlockBase(Material.IRON), hardness, resistance);
        }

        Block(Supplier<net.minecraft.block.Block> constructor, float hardness, float resistance) {
            this(str -> constructor.get(), hardness, resistance, false);
        }

        Block(Function<String, net.minecraft.block.Block> constructor, float hardness, float resistance, boolean hasConnectedModel) {
            this(constructor, hardness, resistance, EnumRarity.COMMON, hasConnectedModel, null);
        }

        Block(Function<String, net.minecraft.block.Block> constructor, float hardness, float resistance, EnumRarity rarity, boolean hasConnectedModel, @Nullable String extraTextures) {
            this.instance = new LazyValue<>(() -> constructor.apply(name())
                .setRegistryName("block_" + name().toLowerCase(Locale.ROOT))
                .setTranslationKey("block_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB)
                .setHardness(hardness)
                .setResistance(resistance));
            this.rarity = rarity;
            this.hasConnectedModel = hasConnectedModel;
            this.extraTextures = extraTextures;
        }

        @Override
        public net.minecraft.block.Block getBlockInstance() {
            return this.instance.get();
        }
        
        @Override
        public Item getInstance() {
            return Item.getItemFromBlock(getBlockInstance());
        }

        @Override
        public EnumRarity getRarity() {
            return this.rarity;
        }

        public boolean hasConnectedModel() {
            return this.hasConnectedModel;
        }

        @Nullable
        public String getExtraTextures() {
            return this.extraTextures;
        }
    }

    public enum Ore implements IBlockItemProvider {
        GALENA(HarvestLevel.Stone, 3, 0, 0),
        IRIDIUM(HarvestLevel.Diamond, 20, 30, 21, EnumRarity.UNCOMMON, (fortune, drops, rand) -> {
            ItemStack iridium = IC2Items.getItem("misc_resource", "iridium_ore");
            iridium.setCount(1 + rand.nextInt(1 + fortune / 2));
            drops.add(iridium);
        }),
        RUBY(HarvestLevel.Iron, 4, 3, 5, (fortune, drops, rand) -> {
            drops.add(Miscellaneous.RUBY.getItemStack(1 + rand.nextInt(1 + fortune)));
            if (rand.nextInt(Math.max(1, 32 / (fortune + 1))) == 0) drops.add(Miscellaneous.RED_GARNET.getItemStack());
        }),
        SAPPHIRE(HarvestLevel.Iron, 4, 3, 5, (fortune, drops, rand) -> {
            drops.add(Miscellaneous.SAPPHIRE.getItemStack(1 + rand.nextInt(1 + fortune)));
            if (rand.nextInt(Math.max(1, 64 / (fortune + 1))) == 0) drops.add(Miscellaneous.GREEN_SAPPHIRE.getItemStack());
        }),
        BAUXITE(HarvestLevel.Stone, 3, 0, 0),
        PYRITE(HarvestLevel.Stone, 2, 1, 1, (fortune, drops, rand) -> {
            drops.add(Dust.PYRITE.getItemStack(2 + rand.nextInt(1 + fortune)));
        }),
        CINNABAR(HarvestLevel.Iron, 3, 3, 3, (fortune, drops, rand) -> {
            drops.add(Dust.CINNABAR.getItemStack(2 + rand.nextInt(1 + fortune)));
            if (rand.nextInt(Math.max(1, 4 / (fortune + 1))) == 0) drops.add(new ItemStack(Items.REDSTONE));
        }),
        SPHALERITE(HarvestLevel.Stone, 2, 1, 1, (fortune, drops, rand) -> {
            drops.add(Dust.SPHALERITE.getItemStack(2 + rand.nextInt(1 + fortune)));
            if (rand.nextInt(Math.max(1, 4 / (fortune + 1))) == 0) drops.add(Dust.ZINC.getItemStack());
            if (rand.nextInt(Math.max(1, 32 / (fortune + 1))) == 0) drops.add(Dust.YELLOW_GARNET.getItemStack());
        }),
        TUNGSTATE(HarvestLevel.Iron, 4, 0, 0),
        SHELDONITE(HarvestLevel.Diamond, 3.5F, 0, 0, EnumRarity.UNCOMMON, (fortune, drops, rand) -> {}),
        OLIVINE(HarvestLevel.Diamond, 3, 0, 0, (fortune, drops, rand) -> {
            drops.add(Miscellaneous.OLIVINE.getItemStack(1 + rand.nextInt(1 + fortune)));
        }),
        SODALITE(HarvestLevel.Iron, 3, 0, 0, (fortune, drops, rand) -> {
            drops.add(Dust.SODALITE.getItemStack(6 + 3 * rand.nextInt(1 + fortune)));
            if (rand.nextInt(Math.max(1, 4 / (fortune + 1))) == 0) drops.add(Dust.ALUMINIUM.getItemStack());
        }),
        TETRAHEDRITE(HarvestLevel.Iron, 3, 0, 0),
        CASSITERITE(HarvestLevel.Iron, 3, 0, 0);
        
        private final LazyValue<net.minecraft.block.Block> instance;
        private final EnumRarity rarity;
        
        Ore(HarvestLevel level, float hardness, int dropChance, int dropRandom) {
            this(level, hardness, dropChance, dropRandom, (fortune, drops, rand) -> {});
        }

        Ore(HarvestLevel level, float hardness, int dropChance, int dropRandom, TriConsumer<Integer, List<ItemStack>, Random> loot) {
            this(level, hardness, dropChance, dropRandom, EnumRarity.COMMON, loot);
        }

        Ore(HarvestLevel level, float hardness, int dropChance, int dropRandom, EnumRarity rarity, TriConsumer<Integer, List<ItemStack>, Random> loot) {
            String name = name().toLowerCase(Locale.ROOT) + "_ore";
            this.instance = new LazyValue<>(() -> new BlockOre(name().toLowerCase(Locale.ROOT), level, dropChance, dropRandom, loot)
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB)
                .setHardness(hardness));
            this.rarity = rarity;
        }

        @Override
        public net.minecraft.block.Block getBlockInstance() {
            return this.instance.get();
        }

        @Override
        public Item getInstance() {
            return Item.getItemFromBlock(getBlockInstance());
        }

        @Override
        public EnumRarity getRarity() {
            return this.rarity;
        }
    }

    public enum Ingot implements IItemProvider {
        ALUMINIUM("Al"),
        ANTIMONY("Sb"),
        BATTERY_ALLOY("Pb4Sb1"),
        BRASS("ZnCu3"),
        CHROME("Cr"),
        ELECTRUM("AgAu"),
        HOT_TUNGSTEN_STEEL,
        INVAR("Fe2Ni"),
        IRIDIUM("Ir"),
        IRIDIUM_ALLOY(JavaUtil.NULL_SUPPLIER, false, EnumRarity.UNCOMMON),
        LEAD("Pb"),
        MAGNALIUM("MgAl2"),
        NICKEL("Ni"),
        OSMIUM("Os"),
        PLATINUM("Pt"),
        PLUTONIUM("Pu", true),
        SILVER("Ag"),
        SOLDERING_ALLOY("Sn9Sb1"),
        STEEL("Fe"),
        THORIUM("Th", true),
        TITANIUM("Ti"),
        TUNGSTEN("W"),
        TUNGSTEN_STEEL(() -> GtLocale.translateItemDescription("ingot_tungsten_steel")),
        ZINC("Zn");

        private final LazyValue<Item> instance;
        public final Supplier<String> description;

        Ingot() {
            this(JavaUtil.NULL_SUPPLIER);
        }

        Ingot(String description) {
            this(description, false);
        }

        Ingot(String description, boolean hasEffect) {
            this(() -> description, hasEffect);
        }

        Ingot(Supplier<String> description) {
            this(description, false);
        }
        
        Ingot(Supplier<String> description, boolean hasEffect) {
            this(description, hasEffect, null);
        }

        Ingot(Supplier<String> description, boolean hasEffect, EnumRarity rarity) {
            this.description = description;

            String name = name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemBase(name, this.description, hasEffect)
                .setFolder("ingot")
                .setRarity(rarity)
                .setRegistryName("ingot_" + name)
                .setTranslationKey("ingot_" + name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Nugget implements IItemProvider {
        ALUMINIUM(Ingot.ALUMINIUM.description),
        ANTIMONY(Ingot.ANTIMONY.description),
        BRASS(Ingot.BRASS.description),
        CHROME(Ingot.CHROME.description),
        COPPER("Cu"),
        ELECTRUM(Ingot.ELECTRUM.description),
        INVAR(Ingot.INVAR.description),
        IRIDIUM(Ingot.IRIDIUM.description),
        LEAD("Pg"),
        NICKEL(Ingot.NICKEL.description),
        OSMIUM(Ingot.OSMIUM.description),
        PLATINUM(Ingot.PLATINUM.description),
        SILVER(Plate.SILVER.description),
        STEEL("Fe"),
        TIN("Sn"),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        ZINC(Ingot.ZINC.description);

        private final LazyValue<Item> instance;

        Nugget(String description) {
            this(() -> description);
        }

        Nugget(Supplier<String> description) {
            String name = "nugget_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemBase(name().toLowerCase(Locale.ROOT), description)
                .setFolder("nugget")
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Plate implements IItemProvider {
        ALUMINIUM(Ingot.ALUMINIUM.description),
        BATTERY_ALLOY(Ingot.BATTERY_ALLOY.description),
        BRASS(Ingot.BRASS.description),
        BRONZE(Rod.BRONZE.description),
        CHROME(Ingot.CHROME.description),
        COPPER(Rod.COPPER.description),
        ELECTRUM(Ingot.ELECTRUM.description),
        GOLD(Rod.GOLD.description),
        INVAR(Ingot.INVAR.description),
        IRIDIUM(Ingot.IRIDIUM.description),
        IRON("Fe"),
        LEAD(Ingot.LEAD.description),
        MAGNALIUM(Ingot.MAGNALIUM.description),
        NICKEL(Ingot.NICKEL.description),
        OSMIUM(Ingot.OSMIUM.description),
        PLATINUM(Ingot.PLATINUM.description),
        @NotExperimental
        REFINED_IRON("Fe"),
        SILICON("Si2"),
        SILVER("Ag"),
        STEEL("Fe"),
        TIN(Rod.TIN.description),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        TUNGSTEN_STEEL(Ingot.TUNGSTEN_STEEL.description),
        WOOD,
        ZINC(Ingot.ZINC.description);

        private final LazyValue<Item> instance;
        public final Supplier<String> description;

        Plate() {
            this(JavaUtil.NULL_SUPPLIER);
        }

        Plate(String description) {
            this(() -> description);
        }

        Plate(Supplier<String> description) {
            this.description = description;

            String name = "plate_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> {
                Item item = new ItemBase(name().toLowerCase(Locale.ROOT), this.description)
                    .setFolder("plate")
                    .setRegistryName(name)
                    .setTranslationKey(name);
                if (ProfileDelegate.shouldEnable(this)) item.setCreativeTab(GregTechMod.GREGTECH_TAB);
                return item;
            });
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Rod implements IItemProvider {
        ALUMINIUM(Ingot.ALUMINIUM.description),
        BRASS(Ingot.BRASS.description),
        BRONZE("SnCu3"),
        CHROME(Ingot.CHROME.description),
        COPPER("Cu"),
        ELECTRUM(Ingot.ELECTRUM.description),
        GOLD("Au"),
        INVAR(Ingot.INVAR.description),
        IRIDIUM(Ingot.IRIDIUM.description),
        IRON("Fe"),
        LEAD("Pb"),
        NICKEL(Ingot.NICKEL.description),
        OSMIUM(Ingot.OSMIUM.description),
        PLATINUM(Ingot.PLATINUM.description),
        @NotExperimental
        REFINED_IRON("Fe"),
        SILVER("Ag"),
        STEEL("Fe"),
        TIN("Sn"),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        TUNGSTEN_STEEL(Ingot.TUNGSTEN_STEEL.description),
        ZINC(Ingot.ZINC.description);

        private final LazyValue<Item> instance;
        public final Supplier<String> description;

        Rod(String description) {
            this(() -> description);
        }

        Rod(Supplier<String> description) {
            this.description = description;

            String name = "rod_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> {
                Item item = new ItemBase(name().toLowerCase(Locale.ROOT), this.description)
                    .setFolder("rod")
                    .setRegistryName(name)
                    .setTranslationKey(name);
                if (ProfileDelegate.shouldEnable(this)) item.setCreativeTab(GregTechMod.GREGTECH_TAB);
                return item;
            });
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Dust implements IItemProvider {
        ALMANDINE("Al2Fe3Si3O12"),
        ALUMINIUM(Ingot.ALUMINIUM.description),
        ANDRADITE("Ca3Fe2Si3O12"),
        ANTIMONY(Ingot.ANTIMONY.description),
        ASHES("C"),
        BASALT("(Mg2Fe2SiO4)(CaCO3)3(SiO2)8C4"),
        BAUXITE("TiAl16H10O12"),
        BRASS(Ingot.BRASS.description),
        CALCITE("CaCO3"),
        CHARCOAL("C"),
        CHROME(Ingot.CHROME.description),
        CINNABAR("HgS"),
        CLAY("Na2LiAl2Si2"),
        DARK_ASHES("C"),
        DIAMOND("C128"),
        ELECTRUM(Ingot.ELECTRUM.description),
        EMERALD("Be3Al2Si6O18"),
        ENDER_EYE("BeK4N5Cl6C4S2"),
        ENDER_PEARL("BeK4N5Cl6"),
        ENDSTONE,
        FLINT("SiO2"),
        GALENA("Pb3Ag3S2"),
        GREEN_SAPPHIRE("Al206"),
        GROSSULAR("Ca3Al2Si3O12"),
        INVAR(Ingot.INVAR.description),
        LAZURITE("Al6Si6Ca8Na8"),
        LEAD(Ingot.LEAD.description),
        MAGNESIUM("Mg"),
        MANGANESE("Mn"),
        MARBLE("Mg(CaCO3)7"),
        NETHERRACK,
        NICKEL("Ni"),
        OBSIDIAN("MgFeSiO8"),
        OLIVINE("Mg2Fe2SiO4"),
        OSMIUM("Os"),
        PHOSPHORUS("Ca3(PO4)2"),
        PLATINUM("Pt"),
        PLUTONIUM(Ingot.PLUTONIUM.description, true),
        PYRITE("FeS2"),
        PYROPE("Al2Mg3Si3O12"),
        RED_GARNET("(Al2Mg3Si3O12)3(Al2Fe3Si3O12)5(Al2Mn3Si3O12)8"),
        REDROCK("(Na2LiAl2Si2)((CaCO3)2SiO2)3"),
        RUBY("Al206Cr"),
        SALTPETER("KNO3"),
        SAPPHIRE("Al206"),
        SODALITE("Al3Si3Na4Cl"),
        SPESSARTINE("Al2Mn3Si3O12"),
        SPHALERITE("ZnS"),
        SILVER(Ingot.SILVER.description),
        STEEL("Fe"),
        SULFUR("S"),
        THORIUM(Ingot.THORIUM.description, true),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        URANIUM("U", true),
        UVAROVITE("Ca3Cr2Si3O12"),
        WOOD,
        YELLOW_GARNET("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
        ZINC(Ingot.ZINC.description);

        private final LazyValue<Item> instance;
        public final Supplier<String> description;

        Dust() {
            this(JavaUtil.NULL_SUPPLIER);
        }

        Dust(String description) {
            this(description, false);
        }

        Dust(String description, boolean hasEffect) {
            this(() -> description, hasEffect);
        }

        Dust(Supplier<String> description) {
            this(description, false);
        }

        Dust(Supplier<String> description, boolean hasEffect) {
            this.description = description;
            String name = "dust_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemBase(name().toLowerCase(Locale.ROOT), this.description, hasEffect)
                .setFolder("dust")
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Smalldust implements IItemProvider {
        ALMANDINE(Dust.ALMANDINE.description),
        ALUMINIUM(Ingot.ALUMINIUM.description),
        ANDRADITE(Dust.ANDRADITE.description),
        ANTIMONY(Ingot.ANTIMONY.description),
        ASHES("C"),
        BASALT(Dust.BASALT.description),
        BAUXITE(Dust.BAUXITE.description),
        BRASS(Ingot.BRASS.description),
        BRONZE(Rod.BRONZE.description),
        CALCITE(Dust.CALCITE.description),
        CHARCOAL(Dust.CHARCOAL.description),
        CHROME(Ingot.CHROME.description),
        CINNABAR(Dust.CINNABAR.description),
        CLAY(Dust.CLAY.description),
        COAL("C2"),
        COPPER(Rod.COPPER.description),
        DARK_ASHES(Dust.DARK_ASHES.description),
        DIAMOND(Dust.DIAMOND.description),
        ELECTRUM(Ingot.ELECTRUM.description),
        EMERALD(Dust.EMERALD.description),
        ENDER_EYE(Dust.ENDER_EYE.description),
        ENDER_PEARL(Dust.ENDER_PEARL.description),
        ENDSTONE,
        FLINT(Dust.FLINT.description),
        GALENA(Dust.GALENA.description),
        GLOWSTONE,
        GOLD(Rod.GOLD.description),
        GREEN_SAPPHIRE(Dust.GREEN_SAPPHIRE.description),
        GROSSULAR(Dust.GROSSULAR.description),
        GUNPOWDER,
        INVAR(Ingot.INVAR.description),
        IRON("Fe"),
        LAZURITE(Dust.LAZURITE.description),
        LEAD(Dust.LEAD.description),
        MAGNESIUM(Dust.MAGNESIUM.description),
        MANGANESE(Dust.MANGANESE.description),
        MARBLE(Dust.MARBLE.description),
        NETHERRACK,
        NICKEL(Ingot.NICKEL.description),
        OBSIDIAN(Dust.OBSIDIAN.description),
        OLIVINE(Dust.OLIVINE.description),
        OSMIUM(Ingot.OSMIUM.description),
        PHOSPHORUS(Dust.PHOSPHORUS.description),
        PLATINUM(Ingot.PLATINUM.description),
        PLUTONIUM(Ingot.PLUTONIUM.description, true),
        PYRITE(Dust.PYRITE.description),
        PYROPE(Dust.PYROPE.description),
        RED_GARNET(Dust.RED_GARNET.description),
        REDROCK(Dust.REDROCK.description),
        REDSTONE,
        RUBY(Dust.RUBY.description),
        SALTPETER(Dust.SALTPETER.description),
        SAPPHIRE(Dust.SAPPHIRE.description),
        SILVER(Dust.SILVER.description),
        SODALITE(Dust.SODALITE.description),
        SPESSARTINE(Dust.SPESSARTINE.description),
        SPHALERITE(Dust.SPHALERITE.description),
        STEEL(Dust.STEEL.description),
        SULFUR(Dust.SULFUR.description),
        THORIUM(Ingot.THORIUM.description, true),
        TIN(Rod.TIN.description),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        URANIUM(Dust.URANIUM.description, true),
        UVAROVITE(Dust.UVAROVITE.description),
        WOOD(Dust.WOOD.description),
        YELLOW_GARNET(Dust.YELLOW_GARNET.description),
        ZINC(Ingot.ZINC.description);

        private final LazyValue<Item> instance;

        Smalldust() {
            this(JavaUtil.NULL_SUPPLIER);
        }

        Smalldust(String description) {
            this(description, false);
        }

        Smalldust(String description, boolean hasEffect) {
            this(() -> description, hasEffect);
        }

        Smalldust(Supplier<String> description) {
            this(description, false);
        }

        Smalldust(Supplier<String> description, boolean hasEffect) {
            String name = "smalldust_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemBase(name().toLowerCase(Locale.ROOT), description, hasEffect)
                .setFolder("smalldust")
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Upgrade implements IOreDictItemProvider {
        HV_TRANSFORMER(GtUpgradeType.TRANSFORMER, 2, 3, "craftingHVTUpgrade", (machine, player) -> machine.addExtraTier()),
        LITHIUM_BATTERY(GtUpgradeType.BATTERY, 16, 1, "craftingLiBattery", (machine, player) -> machine.addExtraEUCapacity(100000)),
        ENERGY_CRYSTAL(GtUpgradeType.BATTERY, 16, GregTechMod.classic ? 2 : 3, DESCRIPTION_DELEGATE, GregTechMod.classic ? "crafting100kEUStore" : "crafting1kkEUStore", EnumRarity.COMMON, (machine, player) -> machine.addExtraEUCapacity(GregTechMod.classic ? 100000 : 1000000)),
        LAPOTRON_CRYSTAL(GtUpgradeType.BATTERY, 16, GregTechMod.classic ? 3 : 4, DESCRIPTION_DELEGATE, GregTechMod.classic ? "crafting1kkEUStore" : "crafting10kkEUStore", EnumRarity.UNCOMMON, (machine, player) -> machine.addExtraEUCapacity(GregTechMod.classic ? 1000000 : 10000000)),
        ENERGY_ORB(GtUpgradeType.BATTERY, 16, GregTechMod.classic ? 4 : 5, DESCRIPTION_DELEGATE, GregTechMod.classic ? "crafting10kkEUStore" : "crafting100kkEUStore", EnumRarity.RARE, (machine, player) -> machine.addExtraEUCapacity(GregTechMod.classic ? 10000000 : 100000000)),
        MACHINE_LOCK(GtUpgradeType.LOCK, 1, 0, "craftingLock", (machine, player) -> {
            if (player != null && !player.getGameProfile().equals(machine.getOwner())) {
                GtUtil.sendMessage(player, GtLocale.buildKeyItem("machine_lock", "error"));
                return true;
            }
            return false;
        }, (machine, player) -> {
            if (!machine.isPrivate()) machine.setPrivate(true);
        }),
        QUANTUM_CHEST(GtUpgradeType.OTHER, 1, 0, "craftingQuantumChestUpgrade", (machine, player) -> {
            if (machine instanceof TileEntityDigitalChestBase) {
                BlockPos pos = ((TileEntityDigitalChestBase) machine).getPos();
                EnumFacing facing = ((TileEntityDigitalChestBase) machine).getFacing();
                ItemStack content = ((TileEntityDigitalChestBase) machine).content.get();
                GameProfile owner = machine.getOwner();

                player.world.removeTileEntity(pos);

                TileEntityQuantumChest te = new TileEntityQuantumChest();
                te.content.put(content);
                te.setOwner(owner);
                if (machine.isPrivate()) {
                    te.setPrivate(true);
                    te.forceAddUpgrade(MACHINE_LOCK.getItemStack());
                }

                player.world.setTileEntity(pos, te);
                te.setFacing(facing);
                player.world.setBlockState(pos, te.getBlockState());
            }
        }),
        STEAM_UPGRADE(GtUpgradeType.STEAM, 1, 1, "craftingSteamUpgrade", (machine, player) -> {
            if (!machine.hasSteamTank()) machine.addSteamTank();
        }),
        STEAM_TANK(GtUpgradeType.STEAM, 16, 1, "craftingSteamTank", IUpgradableMachine::hasSteamTank, JavaUtil.alwaysFalseBi(), (machine, player) -> {
            FluidTank steamTank = machine.getSteamTank();
            if (steamTank != null) steamTank.setCapacity(steamTank.getCapacity() + 64000);
        }),
        PNEUMATIC_GENERATOR(GtUpgradeType.MJ, 1, 1, "craftingPneumaticGenerator", (machine, player) -> {
            if (!ModHandler.buildcraftLib) {
                GtUtil.sendMessage(player, GtLocale.buildKeyInfo("buildcraft_absent"));
                return true;
            }
            return false;
        }, (machine, player) -> machine.addMjUpgrade()),
        RS_ENERGY_CELL(GtUpgradeType.MJ, 16, 1, "craftingEnergyCellUpgrade", IUpgradableMachine::hasMjUpgrade, (machine, player) -> {
            if (!ModHandler.buildcraftLib) {
                GtUtil.sendMessage(player, GtLocale.buildKeyInfo("buildcraft_absent"));
                return true;
            }
            return false;
        }, (machine, player) -> {
            machine.setMjCapacity(machine.getMjCapacity() + MjHelper.microJoules(100000));
        });

        private final LazyValue<Item> instance;
        public final String oreDict;

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String oreDict, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, "description", oreDict, JavaUtil.alwaysTrue(), JavaUtil.alwaysFalseBi(), afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String descriptionKey, String oreDict, EnumRarity rarity, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, descriptionKey, oreDict, rarity, JavaUtil.alwaysTrue(), JavaUtil.alwaysFalseBi(), afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String oreDict, BiPredicate<IUpgradableMachine, EntityPlayer> beforeInsert, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, "description", oreDict, JavaUtil.alwaysTrue(), beforeInsert, afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String oreDict, Predicate<IUpgradableMachine> condition, BiPredicate<IUpgradableMachine, EntityPlayer> beforeInsert, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, "description", oreDict, condition, beforeInsert, afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String descriptionKey, String oreDict, Predicate<IUpgradableMachine> condition, BiPredicate<IUpgradableMachine, EntityPlayer> beforeInsert, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, descriptionKey, oreDict, null, condition, beforeInsert, afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String descriptionKey, String oreDict, EnumRarity rarity, Predicate<IUpgradableMachine> condition, BiPredicate<IUpgradableMachine, EntityPlayer> beforeInsert, BiConsumer<IUpgradableMachine, EntityPlayer> afterInsert) {
            this.oreDict = oreDict;

            String name = name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemUpgrade(name, name + "." + descriptionKey, type, maxCount, requiredTier, condition, beforeInsert, afterInsert)
                .setFolder("upgrade")
                .setRarity(rarity)
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }
    }

    public enum CoverItem implements IOreDictItemProvider {
        ACTIVE_DETECTOR("craftingWorkDetector"),
        CONVEYOR("craftingConveyor"),
        CRAFTING("craftingWorkBench"),
        DRAIN("craftingDrain"),
        ENERGY_ONLY("energy_flow_circuit", "craftingCircuitTier07", EnumRarity.RARE),
        ENERGY_METER("craftingEnergyMeter"),
        ITEM_METER("craftingItemMeter"),
        ITEM_VALVE("craftingItemValve"),
        LIQUID_METER("craftingLiquidMeter"),
        MACHINE_CONTROLLER("craftingWorkController"),
        PUMP_MODULE("craftingPump"),
        REDSTONE_CONDUCTOR("craftingRedstoneConductor"),
        REDSTONE_ONLY("data_control_circuit", "craftingCircuitTier06", EnumRarity.RARE),
        REDSTONE_SIGNALIZER("craftingRedstoneSignalizer"),
        SCREEN("craftingMonitorTier02"),
        SOLAR_PANEL("craftingSolarPanel"),
        SOLAR_PANEL_HV("craftingSolarPanelHV"),
        SOLAR_PANEL_LV("craftingSolarPanelLV"),
        SOLAR_PANEL_MV("craftingSolarPanelMV");

        private final LazyValue<Item> instance;
        public final String oreDict;

        CoverItem(String oreDict) {
            this(null, oreDict);
        }
        
        CoverItem(String itemName, String oreDict) {
            this(itemName, oreDict, null);
        }

        CoverItem(String itemName, String oreDict, EnumRarity rarity) {
            this.oreDict = oreDict;
            this.instance = new LazyValue<>(() -> {
                Cover cover = Cover.valueOf(name());
                String name = itemName != null ? itemName : name().toLowerCase(Locale.ROOT);

                return new ItemCover(cover.name().toLowerCase(Locale.ROOT), cover.instance.get(), name)
                    .setFolder("coveritem")
                    .setRarity(rarity)
                    .setRegistryName(name)
                    .setTranslationKey(name)
                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            });
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }
    }

    public enum TurbineRotor implements IItemProvider {
        BRONZE(60, 10, 15000),
        STEEL(80, 20, 10000),
        MAGNALIUM(100, 50, 10000),
        TUNGSTEN_STEEL(90, 15, 30000),
        CARBON(125, 100, 2500);

        private final LazyValue<Item> instance;

        TurbineRotor(int efficiency, int efficiencyMultiplier, int durability) {

            String name = "turbine_rotor_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemTurbineRotor(name, durability, efficiency, efficiencyMultiplier)
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Component implements IOreDictItemProvider {
        SUPERCONDUCTOR("description", "craftingSuperconductor", EnumRarity.RARE),
        DATA_STORAGE_CIRCUIT("craftingCircuitTier05"),
        LITHIUM_BATTERY(ItemLithiumBattery::new, "craftingLiBattery"),
        COIL_KANTHAL("craftingHeatingCoilTier01"),
        COIL_NICHROME("craftingHeatingCoilTier02"),
        COIL_CUPRONICKEL("craftingHeatingCoilTier00"),
        HULL_ALUMINIUM("craftingRawMachineTier01"),
        HULL_BRASS("craftingRawMachineTier00"),
        HULL_BRONZE("craftingRawMachineTier00"),
        HULL_IRON("craftingRawMachineTier01"),
        HULL_STEEL("craftingRawMachineTier02"),
        HULL_TITANIUM("craftingRawMachineTier03"),
        HULL_TUNGSTEN_STEEL("craftingRawMachineTier03"),
        CIRCUIT_BOARD_BASIC("craftingCircuitBoardTier02"),
        CIRCUIT_BOARD_ADVANCED("craftingCircuitBoardTier04"),
        CIRCUIT_BOARD_PROCESSOR("craftingCircuitBoardTier06"),
        TURBINE_BLADE_BRONZE("craftingTurbineBladeBronze"),
        TURBINE_BLADE_CARBON("craftingTurbineBladeCarbon"),
        TURBINE_BLADE_MAGNALIUM("craftingTurbineBladeMagnalium"),
        TURBINE_BLADE_STEEL("craftingTurbineBladeSteel"),
        TURBINE_BLADE_TUNGSTEN_STEEL("craftingTurbineBladeTungstenSteel"),
        GEAR_IRON(DESCRIPTION_DELEGATE, "gearIron", null),
        GEAR_BRONZE("gearBronze"),
        GEAR_STEEL("gearSteel"),
        GEAR_TITANIUM("gearTitanium"),
        GEAR_TUNGSTEN_STEEL("gearTungstenSteel"),
        GEAR_IRIDIUM("gearIridium"),
        DIAMOND_SAWBLADE("craftingDiamondBlade"),
        DIAMOND_GRINDER("craftingGrinder"),
        WOLFRAMIUM_GRINDER("craftingGrinder"),
        MACHINE_PARTS("craftingMachineParts"),
        ADVANCED_CIRCUIT_PARTS("craftingCircuitPartsTier04"),
        DUCT_TAPE("craftingDuctTape"),
        DATA_ORB(ItemDataOrb::new, "craftingCircuitTier08");

        private final LazyValue<Item> instance;
        public final String oreDict;

        Component(String oreDict) {
            this("description", oreDict, null);
        }

        Component(String descriptionKey, String oreDict, EnumRarity rarity) {
            String name = name().toLowerCase(Locale.ROOT);
            this.oreDict = oreDict;

            this.instance = new LazyValue<>(() -> new ItemBase(name, () -> GtLocale.translateItem(name + "." + descriptionKey))
                .setFolder("component")
                .setRarity(rarity)
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        Component(Supplier<Item> constructor, String oreDict) {
            this.oreDict = oreDict;

            this.instance = new LazyValue<>(constructor);
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }

        @Override
        public boolean isWildcard() {
            return true;
        }
    }

    public enum Tool implements IOreDictItemProvider {
        CROWBAR(ItemCrowbar::new, "craftingToolCrowbar"),
        DEBUG_SCANNER(ItemDebugScanner::new),
        DRILL_ADVANCED(ItemDrillAdvanced::new, "craftingToolLargeDrill"),
        ROCK_CUTTER(ItemRockCutter::new),
        RUBBER_HAMMER(ItemRubberHammer::new, "craftingToolSoftHammer"),
        SAW_ADVANCED(ItemSawAdvanced::new),
        SCANNER(ItemScanner::new),
        SCREWDRIVER(ItemScrewdriver::new, "craftingToolScrewdriver"),
        SOLDERING_TOOL(ItemSolderingTool::new, "craftingToolSolderingIron"),
        TESLA_STAFF(ItemTeslaStaff::new),
        WRENCH_ADVANCED(ItemWrenchAdvanced::new),
        DESTRUCTORPACK(ItemDestructorPack::new),
        LAPOTRONIC_ENERGY_ORB(() -> new ItemElectricBase("lapotronic_energy_orb", JavaUtil.NULL_SUPPLIER, GregTechMod.classic ? 10000000 : 100000000, 8192, GregTechMod.classic ? 4 : 5, 0, true)
            .setFolder("tool")
            .setRarity(EnumRarity.RARE)
            .setRegistryName("lapotronic_energy_orb")
            .setTranslationKey("lapotronic_energy_orb")
            .setCreativeTab(GregTechMod.GREGTECH_TAB), GregTechMod.classic ? "crafting10kkEUStore" : "crafting100kkEUStore"),
        SONICTRON_PORTABLE(ItemSonictron::new),
        SPRAY_BUG(ItemSprayBug::new),
        SPRAY_ICE(ItemSprayIce::new, "molecule_1n"),
        SPRAY_HARDENER(ItemSprayHardener::new),
        SPRAY_FOAM(ItemSprayFoam::new),
        SPRAY_PEPPER(ItemSprayPepper::new),
        SPRAY_HYDRATION(ItemSprayHydration::new);

        public final String oreDict;
        private final LazyValue<Item> instance;

        Tool(Supplier<Item> constructor) {
            this(constructor, null);
        }

        Tool(Supplier<Item> constructor, String oreDict) {
            this.oreDict = oreDict;

            this.instance = new LazyValue<>(constructor);
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }

        @Override
        public boolean isWildcard() {
            return true;
        }
    }

    public enum ColorSpray implements IItemProvider {
        WHITE,
        ORANGE,
        MAGENTA,
        LIGHT_BLUE,
        YELLOW,
        LIME,
        PINK,
        GRAY,
        SILVER,
        CYAN,
        PURPLE,
        BLUE,
        BROWN,
        GREEN,
        RED,
        BLACK;

        private final LazyValue<Item> instance;

        ColorSpray() {
            this.instance = new LazyValue<>(() -> new ItemSprayColor(EnumDyeColor.byMetadata(this.ordinal())));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Wrench implements IItemProvider {
        IRON(128, 4),
        BRONZE(256, 6),
        STEEL(512, 8),
        TUNGSTEN_STEEL(5120, 10);

        private final LazyValue<Item> instance;

        Wrench(int durability, int entityDamage) {
            this.instance = new LazyValue<>(() -> new ItemWrench("wrench_" + name().toLowerCase(Locale.ROOT), durability, entityDamage)
                .setRegistryName("wrench_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum JackHammer implements IItemProvider {
        BRONZE(50, 10000, 1, 50, 7.5F, false),
        STEEL(100, 10000, 1, 50, 15F, false),
        DIAMOND(250, 100000, 2, 100, 45F, true);

        private final LazyValue<Item> instance;

        JackHammer(int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency, boolean canMineObsidian) {
            this.instance = new LazyValue<>(() -> new ItemJackHammer("jack_hammer_" + name().toLowerCase(Locale.ROOT), operationEnergyCost, maxCharge, tier, transferLimit, efficiency, canMineObsidian)
                .setRarity(canMineObsidian ? EnumRarity.UNCOMMON : null)
                .setRegistryName("jack_hammer_" + name().toLowerCase(Locale.ROOT))
                .setTranslationKey("jack_hammer_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Hammer implements IItemProvider {
        IRON(128, 4),
        BRONZE(256, 6),
        STEEL(512, 8),
        TUNGSTEN_STEEL(5120, 10);

        private final LazyValue<Item> instance;

        Hammer(int durability, int entityDamage) {
            this.instance = new LazyValue<>(() -> new ItemHardHammer(name().toLowerCase(Locale.ROOT), durability, entityDamage)
                .setRegistryName("hammer_" + name().toLowerCase(Locale.ROOT))
                .setTranslationKey("hammer_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Saw implements IItemProvider {
        IRON(128, 3, 2),
        BRONZE(256, 4, 3),
        STEEL(1280, 6, 4),
        TUNGSTEN_STEEL(5120, 8, 5);

        private final LazyValue<Item> instance;

        Saw(int durability, int efficiency, int entityDamage) {
            this.instance = new LazyValue<>(() -> new ItemSaw(name().toLowerCase(Locale.ROOT), durability, efficiency, entityDamage)
                .setRegistryName("saw_" + name().toLowerCase(Locale.ROOT))
                .setTranslationKey("saw_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum SolderingMetal implements IItemProvider {
        LEAD(10),
        TIN(50);

        private final LazyValue<Item> instance;

        SolderingMetal(int durability) {
            this.instance = new LazyValue<>(() -> new ItemSolderingMetal(name().toLowerCase(Locale.ROOT), durability)
                .setRegistryName("soldering_" + name().toLowerCase(Locale.ROOT))
                .setTranslationKey("soldering_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum File implements IItemProvider {
        IRON(128, 2),
        BRONZE(256, 3),
        STEEL(1280, 3),
        TUNGSTEN_STEEL(5120, 4);

        private final LazyValue<Item> instance;
        public final int durability;
        public final int entityDamage;

        File(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;

            this.instance = new LazyValue<>(() -> new ItemFile(name().toLowerCase(Locale.ROOT), this.durability, this.entityDamage)
                .setRegistryName("file_" + name().toLowerCase(Locale.ROOT))
                .setTranslationKey("file_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Cell implements IItemProvider {
        CARBON("C"),
        ICE("H2O"),
        NITROCARBON("NC"),
        SODIUM_SULFIDE("NaS"),
        SULFUR("S"),
        SULFURIC_ACID("H2SO4");

        private final LazyValue<Item> instance;

        Cell(String description) {
            String name = "cell_" + name().toLowerCase(Locale.ROOT);
            this.instance = new LazyValue<>(() -> new ItemBase(name().toLowerCase(Locale.ROOT), description)
                .setFolder("cell")
                .setRegistryName(name)
                .setTranslationKey(name)
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum NuclearCoolantPack implements IOreDictItemProvider {
        COOLANT_NAK_60K(60000, "crafting60kCoolantStore"),
        COOLANT_NAK_180K(180000, "crafting180kCoolantStore"),
        COOLANT_NAK_360K(360000, "crafting360kCoolantStore"),
        COOLANT_HELIUM_60K(60000, "crafting60kCoolantStore"),
        COOLANT_HELIUM_180K(180000, "crafting180kCoolantStore"),
        COOLANT_HELIUM_360K(360000, "crafting360kCoolantStore");

        private final LazyValue<Item> instance;
        public final String oreDict;

        NuclearCoolantPack(int heatStorage, String oreDict) {
            this.oreDict = oreDict;

            this.instance = new LazyValue<>(() -> new ItemNuclearHeatStorage(name().toLowerCase(Locale.ROOT), heatStorage)
                .setRegistryName(name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }

        @Override
        public boolean isWildcard() {
            return true;
        }
    }

    public enum NuclearFuelRod implements IItemProvider {
        THORIUM(1, 25000, 0.25F, 1, 0.25F),
        THORIUM_DUAL(2, 25000, 0.25F, 1, 0.25F),
        THORIUM_QUAD(4, 25000, 0.25F, 1, 0.25F),
        PLUTONIUM(1, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_uranium")),
        PLUTONIUM_DUAL(2, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_dual_uranium")),
        PLUTONIUM_QUAD(4, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_quad_uranium"));

        private final LazyValue<Item> instance;

        NuclearFuelRod(int cells, int duration, float energy, int radiation, float heat) {
            this(cells, duration, energy, radiation, heat, null);
        }

        NuclearFuelRod(int cells, int duration, float energy, int radiation, float heat, ItemStack depletedStack) {
            this.instance = new LazyValue<>(() -> new ItemNuclearFuelRod("fuel_rod_" + name().toLowerCase(Locale.ROOT), cells, duration, energy, radiation, heat, depletedStack)
                .setRegistryName("fuel_rod_" + name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }
    }

    public enum Armor implements IOreDictItemProvider {
        CLOAKING_DEVICE(EntityEquipmentSlot.CHEST, GregTechMod.classic ? 10000000 : 100000000, 8192, GregTechMod.classic ? 4 : 5, 0, 0, false, null, EnumRarity.EPIC, ArmorPerk.INVISIBILITY_FIELD),
        LAPOTRONPACK(EntityEquipmentSlot.CHEST, GregTechMod.classic ? 10000000 : 100000000, 8192, GregTechMod.classic ? 4 : 5, 0, 0, true, GregTechMod.classic ? "crafting10kkEUPack" : "crafting100kkEUPack", EnumRarity.EPIC),
        LITHIUM_BATPACK(EntityEquipmentSlot.CHEST, 600000, 128, 1, 0, 0, true, "crafting600kEUPack", null),
        ULTIMATE_CHEAT_ARMOR(EntityEquipmentSlot.CHEST, 1000000000, Integer.MAX_VALUE, 1, 10, 100, true, ArmorPerk.values()),
        LIGHT_HELMET(EntityEquipmentSlot.HEAD, 10000, 32, 1, 0, 0, false, ArmorPerk.LAMP, ArmorPerk.SOLARPANEL);

        private final LazyValue<Item> instance;
        public final String oreDict;

        Armor(EntityEquipmentSlot slot, int maxCharge, int transferLimit, int tier, int damageEnergyCost, double absorbtionPercentage, boolean chargeProvider, ArmorPerk... perks) {
            this(slot, maxCharge, transferLimit, tier, damageEnergyCost, absorbtionPercentage, chargeProvider, null, null, perks);
        }

        Armor(EntityEquipmentSlot slot, int maxCharge, int transferLimit, int tier, int damageEnergyCost, double absorbtionPercentage, boolean chargeProvider, String oreDict, EnumRarity rarity, ArmorPerk... perks) {
            this.oreDict = oreDict;

            this.instance = new LazyValue<>(() -> new ItemArmorElectricBase(name().toLowerCase(Locale.ROOT), slot, maxCharge, transferLimit, tier, damageEnergyCost, absorbtionPercentage, chargeProvider, perks)
                .setFolder("armor")
                .setRarity(rarity)
                .setRegistryName(name().toLowerCase(Locale.ROOT))
                .setTranslationKey(name().toLowerCase(Locale.ROOT))
                .setCreativeTab(GregTechMod.GREGTECH_TAB));
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }

        @Override
        public boolean isWildcard() {
            return true;
        }
    }

    public enum Miscellaneous implements IOreDictItemProvider {
        GREG_COIN,
        CREDIT_COPPER(() -> GtLocale.translateGenericDescription("credit", 0.125), null),
        CREDIT_SILVER(() -> GtLocale.translateGenericDescription("credit", 8), null),
        CREDIT_GOLD(() -> GtLocale.translateGenericDescription("credit", 64), null),
        CREDIT_DIAMOND(() -> GtLocale.translateGenericDescription("credit", 512), null),
        RUBY(Dust.RUBY.description, "gemRuby"),
        SAPPHIRE(Dust.SAPPHIRE.description, "gemSapphire"),
        GREEN_SAPPHIRE(Dust.GREEN_SAPPHIRE.description, "gemGreenSapphire"),
        OLIVINE(Dust.OLIVINE.description, "gemOlivine"),
        LAZURITE_CHUNK("(Al6Si6Ca8Na8)8", "chunkLazurite"),
        RED_GARNET(Dust.RED_GARNET.description, "gemGarnetRed"),
        YELLOW_GARNET(Dust.YELLOW_GARNET.description, "gemGarnetYellow"),
        INDIGO_BLOSSOM(JavaUtil.NULL_SUPPLIER, null),
        INDIGO_DYE(JavaUtil.NULL_SUPPLIER, "dyeBlue"),
        FLOUR(JavaUtil.NULL_SUPPLIER, "dustWheat"),
        SPRAY_CAN_EMPTY((Supplier<String>) null, "craftingSprayCan"),
        LAVA_FILTER(() -> new ItemBase("lava_filter", 100)
            .setFolder("component")
            .setEnchantable(false)
            .setRegistryName("lava_filter")
            .setTranslationKey("lava_filter")
            .setCreativeTab(GregTechMod.GREGTECH_TAB)
            .setMaxStackSize(1)
            .setNoRepair()),
        MORTAR_FLINT(() -> GtLocale.translateItemDescription("mortar"), null),
        MORTAR_IRON(() -> new ItemMortar("iron", 63, IC2Items.getItem("dust", "iron"))
            .setRegistryName("mortar_iron")
            .setTranslationKey("mortar_iron")
            .setCreativeTab(GregTechMod.GREGTECH_TAB));

        private final LazyValue<Item> instance;
        public final String oreDict;

        Miscellaneous() {
            this((Supplier<String>) null, null);
        }

        Miscellaneous(String description, String oreDict) {
            this(() -> description, oreDict);
        }

        Miscellaneous(Supplier<String> description, String oreDict) {
            this.oreDict = oreDict;

            this.instance = new LazyValue<>(() -> {
                String name = name().toLowerCase(Locale.ROOT);
                return new ItemBase(name, description != null ? description : () -> GtLocale.translateItemDescription(name))
                    .setRegistryName(name().toLowerCase(Locale.ROOT))
                    .setTranslationKey(name().toLowerCase(Locale.ROOT))
                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            });
        }

        Miscellaneous(Supplier<Item> constructor) {
            this.oreDict = null;

            this.instance = new LazyValue<>(constructor);
        }

        @Override
        public Item getInstance() {
            return this.instance.get();
        }

        @Nullable
        @Override
        public String getOreDictName() {
            return this.oreDict;
        }
    }

    public enum Book {
        MANUAL("Gregorius Techneticies", 11),
        MANUAL2("Gregorius Techneticies", 9),
        MACHINE_SAFETY("Gregorius Techneticies", 7),
        COVER_UP("Gregorius Techneticies", 5),
        GREG_OS_MANUAL("Gregorius Techneticies", 8),
        GREG_OS_MANUAL2("Gregorius Techneticies", 11),
        UPGRADE_DICTIONARY("Gregorius Techneticies", 21),
        CROP_DICTIONARY("Mr. Kenny", 32),
        ENERGY_SYSTEMS("Gregorius Techneticies", 7),
        MICROWAVE_OVEN_MANUAL("Kitchen Industries", 6),
        TURBINE_MANUAL("Gregorius Techneticies", 19),
        THERMAL_BOILER_MANUAL("Gregorius Techneticies", 16);
        
        private final LazyValue<ItemStack> instance;

        Book(String author, int pages) {
            this.instance = new LazyValue<>(() -> getWrittenBook(name().toLowerCase(Locale.ROOT), author, pages, this.ordinal()));
        }

        public ItemStack getInstance() {
            return this.instance.get();
        }

        public static ItemStack getWrittenBook(String name, String author, int pages, int ordinal) {
            ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
            stack.setTagInfo("title", new NBTTagString(GtLocale.translateKey("book", name, "name")));
            stack.setTagInfo("author", new NBTTagString(author));
            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < pages; i++) {
                String page = '\"' + GtLocale.translateKey("book", name, "page" + (i < 10 ? "0" + i : i)) + '\"';
                if (i < 48) {
                    if (page.length() < 256) {
                        tagList.appendTag(new NBTTagString(page));
                    }
                    else {
                        GregTechMod.LOGGER.warn("String for written book too long: " + page);
                    }
                }
                else {
                    GregTechMod.LOGGER.warn("Too many pages for written book: " + name);
                    break;
                }
            }
            tagList.appendTag(new NBTTagString("\"Credits to " + author + " for writing this Book. This was Book Nr. " + (ordinal + 1) + " at its creation. Gotta get 'em all!\""));
            stack.setTagInfo("pages", tagList);
            return stack;
        }
    }
}
