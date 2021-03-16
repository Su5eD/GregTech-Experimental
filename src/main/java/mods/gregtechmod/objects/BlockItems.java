package mods.gregtechmod.objects;

import com.mojang.authlib.GameProfile;
import ic2.api.item.IC2Items;
import ic2.core.profile.NotExperimental;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.util.ArmorPerk;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.init.FluidLoader;
import mods.gregtechmod.objects.blocks.BlockBase;
import mods.gregtechmod.objects.blocks.BlockOre;
import mods.gregtechmod.objects.blocks.ConnectedBlock;
import mods.gregtechmod.objects.items.*;
import mods.gregtechmod.objects.items.base.*;
import mods.gregtechmod.objects.items.components.ItemLithiumBattery;
import mods.gregtechmod.objects.items.tools.*;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IObjectHolder;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidTank;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockItems {
    public static net.minecraft.block.Block lightSource;
    public static Item sensorKit;
    public static Item sensorCard;
    public static final Map<String, ItemCellClassic> CLASSIC_CELLS = Stream.<FluidLoader.IFluidProvider>concat(
            Arrays.stream(FluidLoader.Liquid.values()),
            Arrays.stream(FluidLoader.Gas.values())
    ).collect(Collectors.toMap(FluidLoader.IFluidProvider::getName,
            provider -> new ItemCellClassic(provider.getName(), provider.getDescription(), provider.getFluid())));

    public enum Block {
        ADVANCED_MACHINE(4, 30),
        ADVANCED_MACHINE_CASING(ConnectedBlock::new, 3, 30),
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
        IRIDIUM_REINFORCED_TUNGSTEN_STEEL(ConnectedBlock::new, 200, 400),
        LESUBLOCK(4, 30),
        NICKEL(3, 45),
        OLIVINE(4.5F, 30),
        OSMIUM(4, 900),
        PLATINUM(4, 30),
        REINFORCED_MACHINE_CASING(ConnectedBlock::new, 3, 60),
        RUBY(4.5F, 30),
        SAPPHIRE(4.5F, 30),
        STANDARD_MACHINE_CASING(ConnectedBlock::new, 3, 30),
        TITANIUM(10, 200),
        TUNGSTEN(4.5F, 100),
        TUNGSTEN_STEEL(ConnectedBlock::new, 100, 300),
        ZINC(3.5F, 30);

        private net.minecraft.block.Block instance;
        private final Supplier<net.minecraft.block.Block> constructor;
        private final float hardness;
        private final float resistance;

        Block(float hardness, float resistance) {
            this(() -> new BlockBase(Material.IRON), hardness, resistance);
        }

        Block(Supplier<net.minecraft.block.Block> constructor, float hardness, float resistance) {
            this.constructor = constructor;
            this.hardness = hardness;
            this.resistance = resistance;
        }

        public net.minecraft.block.Block getInstance() {
            if (this.instance == null) {
                this.instance = this.constructor.get()
                        .setRegistryName("block_" + this.name().toLowerCase(Locale.ROOT))
                        .setTranslationKey("block_" + this.name().toLowerCase(Locale.ROOT))
                        .setCreativeTab(GregTechMod.GREGTECH_TAB)
                        .setHardness(hardness)
                        .setResistance(resistance);
            }

            return this.instance;
        }
    }

    public enum Ore {
        GALENA(3, 0, 0, (fortune, drops) -> {}),
        IRIDIUM(20, 30, 21, (fortune, drops) -> {
            ItemStack iridium = IC2Items.getItem("misc_resource", "iridium_ore");
            iridium.setCount(1 + GtUtil.RANDOM.nextInt(1 + fortune / 2));
            drops.add(iridium);
        }),
        RUBY(4, 3, 5, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.RUBY.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 32 / (fortune + 1))) == 0) drops.add(new ItemStack(Miscellaneous.RED_GARNET.instance));
        }),
        SAPPHIRE(4, 3, 5, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.SAPPHIRE.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 64 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Miscellaneous.GREEN_SAPPHIRE.instance, 1));
        }),
        BAUXITE(3, 0, 0, (fortune, drops) -> {}),
        PYRITE(2, 1, 1, (fortune, drops) -> {
            drops.add(new ItemStack(Dust.PYRITE.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
        }),
        CINNABAR(3, 3, 3, (fortune, drops) -> {
            drops.add(new ItemStack(Dust.CINNABAR.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Items.REDSTONE, 1));
        }),
        SPHALERITE(2, 1, 1, (fortune, drops) -> {
            drops.add(new ItemStack(Dust.SPHALERITE.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dust.ZINC.instance));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 32 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dust.YELLOW_GARNET.instance));
        }),
        TUNGSTATE(4, 0, 0, (fortune, drops) -> {}),
        SHELDONITE(3.5F, 0, 0, (fortune, drops) -> {}),
        OLIVINE(3, 0, 0, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.OLIVINE.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
        }),
        SODALITE(3, 0, 0, (fortune, drops) -> {
            drops.add(new ItemStack(Dust.SODALITE.instance, 6 + 3 * GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dust.ALUMINIUM.instance));
        }),
        TETRAHEDRITE(3, 0, 0, (fortune, drops) -> {}),
        CASSITERITE(3, 0, 0, (fortune, drops) -> {});
        private net.minecraft.block.Block instance;
        public final float hardness;
        public final int dropChance;
        public final int dropRandom;
        public final BiConsumer<Integer, List<ItemStack>> loot;

        Ore(float hardness, int dropChance, int dropRandom, BiConsumer<Integer, List<ItemStack>> loot) {
            this.hardness = hardness;
            this.dropChance = dropChance;
            this.dropRandom = dropRandom;
            this.loot = loot;
        }

        public net.minecraft.block.Block getInstance() {
            if (this.instance == null) {
                String name = this.name().toLowerCase(Locale.ROOT)+"_ore";
                this.instance = new BlockOre(this.name().toLowerCase(Locale.ROOT), this.dropChance, this.dropRandom, this.loot)
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB)
                        .setHardness(this.hardness);
            }

            return this.instance;
        }
    }

    public enum Ingot {
        ALUMINIUM("Al"),
        ANTIMONY("Sb"),
        BATTERY_ALLOY("Pb4Sb1"),
        BRASS("ZnCu3"),
        CHROME("Cr"),
        ELECTRUM("AgAu"),
        HOT_TUNGSTEN_STEEL(null),
        INVAR("Fe2Ni"),
        IRIDIUM("Ir"),
        IRIDIUM_ALLOY(null),
        MAGNALIUM("MgAl2"),
        NICKEL("Ni"),
        OSMIUM("Os"),
        PLATINUM("Pt"),
        PLUTONIUM("Pu", true),
        SOLDERING_ALLOY("Sn9Sb1"),
        THORIUM("Th", true),
        TITANIUM("Ti"),
        TUNGSTEN("W"),
        TUNGSTEN_STEEL("Vacuum Hardened"),
        ZINC("Zn");

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Ingot(String description) {
            this(description, false);
        }

        Ingot(String description, boolean hasEffect) {
            this.description = description;
            this.hasEffect = hasEffect;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "ingot_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description, this.hasEffect)
                        .setFolder("ingot")
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Nugget {
        ALUMINIUM(Ingot.ALUMINIUM.description),
        ANTIMONY(Ingot.ANTIMONY.description),
        BRASS(Ingot.BRASS.description),
        CHROME(Ingot.CHROME.description),
        COPPER("Cu"),
        ELECTRUM(Ingot.ELECTRUM.description),
        INVAR(Ingot.INVAR.description),
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

        private Item instance;
        public final String description;

        Nugget(String description) {
            this.description = description;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "nugget_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("nugget")
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Plate implements IObjectHolder {
        ALUMINIUM(Ingot.ALUMINIUM.description),
        BATTERY_ALLOY(Ingot.BATTERY_ALLOY.description),
        BRASS(Ingot.BRASS.description),
        CHROME(Ingot.CHROME.description),
        ELECTRUM(Ingot.ELECTRUM.description),
        INVAR(Ingot.INVAR.description),
        MAGNALIUM(Ingot.MAGNALIUM.description),
        NICKEL(Ingot.NICKEL.description),
        OSMIUM(Ingot.OSMIUM.description),
        PLATINUM(Ingot.PLATINUM.description),
        @NotExperimental
        REFINED_IRON("Fe"),
        SILICON("Si2"),
        SILVER("Ag"),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        TUNGSTEN_STEEL(Ingot.TUNGSTEN_STEEL.description),
        WOOD(null),
        ZINC(Ingot.ZINC.description);

        private Item instance;
        public final String description;

        Plate(String description) {
            this.description = description;
        }

        @Override
        public Item getInstance() {
            if (this.instance == null) {
                String name = "plate_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("plate")
                        .setRegistryName(name)
                        .setTranslationKey(name);
                if (GtUtil.shouldEnable(this)) this.instance.setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Rod implements IObjectHolder {
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

        private Item instance;
        public final String description;

        Rod(String description) {
            this.description = description;
        }

        @Override
        public Item getInstance() {
            if (this.instance == null) {
                String name = "rod_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("rod")
                        .setRegistryName(name)
                        .setTranslationKey(name);
                if (GtUtil.shouldEnable(this)) this.instance.setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Dust {
        ALMANDINE("Al2Fe3Si3O12"),
        ALUMINIUM(Ingot.ALUMINIUM.description),
        ANDRADITE("Ca3Fe2Si3O12"),
        ANTIMONY(Ingot.ANTIMONY.description),
        BASALT("(Mg2Fe2SiO4)(CaCO3)3(SiO2)8C4"),
        BAUXITE("TiAl16H10O12"),
        BRASS(Ingot.BRASS.description),
        CALCITE("CaCO3"),
        CHARCOAL("C"),
        CHROME(Ingot.CHROME.description),
        CINNABAR("HgS"),
        DARK_ASHES("C"),
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
        MAGNESIUM("Mg"),
        MANGANESE("Mn"),
        MARBLE("Mg(CaCO3)7"),
        NICKEL("Ni"),
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
        STEEL("Fe"),
        THORIUM(Ingot.THORIUM.description, true),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        URANIUM("U", true),
        UVAROVITE("Ca3Cr2Si3O12"),
        WOOD,
        YELLOW_GARNET("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
        ZINC(Ingot.ZINC.description);

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Dust() {
            this(null);
        }

        Dust(String description) {
            this(description, false);
        }

        Dust(String description, boolean hasEffect) {
            this.description = description;
            this.hasEffect = hasEffect;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "dust_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("dust")
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Smalldust {
        ALMANDINE(Dust.ALMANDINE.description),
        ALUMINIUM(Ingot.ALUMINIUM.description),
        ANDRADITE(Dust.ANDRADITE.description),
        ANTIMONY(Ingot.ANTIMONY.description),
        BASALT(Dust.BASALT.description),
        BAUXITE(Dust.BAUXITE.description),
        BRASS(Ingot.BRASS.description),
        CALCITE(Dust.CALCITE.description),
        CHARCOAL(Dust.CHARCOAL.description),
        CHROME(Ingot.CHROME.description),
        CINNABAR(Dust.CINNABAR.description),
        CLAY("Na2LiAl2Si2"),
        COAL("C2"),
        DARK_ASHES(Dust.DARK_ASHES.description),
        DIAMOND("C128"),
        ELECTRUM(Ingot.ELECTRUM.description),
        EMERALD(Dust.EMERALD.description),
        ENDER_EYE(Dust.ENDER_EYE.description),
        ENDER_PEARL(Dust.ENDER_PEARL.description),
        ENDSTONE,
        FLINT(Dust.FLINT.description),
        GALENA(Dust.GALENA.description),
        GLOWSTONE,
        GREEN_SAPPHIRE(Dust.GREEN_SAPPHIRE.description),
        GROSSULAR(Dust.GROSSULAR.description),
        GUNPOWDER,
        INVAR(Ingot.INVAR.description),
        LAZURITE(Dust.LAZURITE.description),
        MAGNESIUM(Dust.MAGNESIUM.description),
        MANGANESE(Dust.MANGANESE.description),
        MARBLE(Dust.MARBLE.description),
        NETHERRACK,
        NICKEL(Ingot.NICKEL.description),
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
        SODALITE(Dust.SODALITE.description),
        SPESSARTINE(Dust.SPESSARTINE.description),
        SPHALERITE(Dust.SPHALERITE.description),
        STEEL(Dust.STEEL.description),
        THORIUM(Ingot.THORIUM.description, true),
        TITANIUM(Ingot.TITANIUM.description),
        TUNGSTEN(Ingot.TUNGSTEN.description),
        URANIUM(Dust.URANIUM.description, true),
        UVAROVITE(Dust.UVAROVITE.description),
        WOOD(Dust.WOOD.description),
        YELLOW_GARNET(Dust.YELLOW_GARNET.description),
        ZINC(Ingot.ZINC.description);

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Smalldust() {
            this(null);
        }

        Smalldust(String description) {
            this(description, false);
        }

        Smalldust(String description, boolean hasEffect) {
            this.description = description;
            this.hasEffect = hasEffect;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "smalldust_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description, this.hasEffect)
                        .setFolder("smalldust")
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Upgrade {
        HV_TRANSFORMER(GtUpgradeType.TRANSFORMER, 2, 3, "Higher tier of the transformer upgrade", "craftingHVTUpgrade", (stack, machine, player) -> machine.setSinkTier(Math.min(machine.getSinkTier() + stack.getCount(), 5))),
        LITHIUM_BATTERY(GtUpgradeType.BATTERY, 4, 1, "Adds 100000 EU to the energy capacity", "craftingLiBattery", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000 * stack.getCount()))),
        ENERGY_CRYSTAL(GtUpgradeType.BATTERY, 4, GregTechMod.classic ? 2 : 3, String.format("Adds %s EU to the energy capacity", GregTechMod.classic ? "100000" : "1 Million"), GregTechMod.classic ? "crafting100kEUStore" : "crafting1kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+((GregTechMod.classic ? 100000 : 1000000) * stack.getCount()))),
        LAPOTRON_CRYSTAL(GtUpgradeType.BATTERY, 4, GregTechMod.classic ? 3 : 4, String.format("Adds %s Million EU to the energy capacity", GregTechMod.classic ? 1 : 2), GregTechMod.classic ? "crafting1kkEUStore" : "crafting10kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+((GregTechMod.classic ? 1000000 : 10000000) * stack.getCount()))),
        ENERGY_ORB(GtUpgradeType.BATTERY, 4, GregTechMod.classic ? 4 : 5, String.format("Adds %s Million EU to the energy capacity", GregTechMod.classic ? 10 : 100), GregTechMod.classic ? "crafting10kkEUStore" : "crafting100kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+((GregTechMod.classic ? 10000000 : 100000000) * stack.getCount()))),
        QUANTUM_CHEST(GtUpgradeType.OTHER, 1, 0, "Upgrades a Digital Chest to a Quantum chest", "craftingQuantumChestUpgrade"),
        MACHINE_LOCK(GtUpgradeType.LOCK, 1, 0, "Makes a machine private for the one, who applies this upgrade", "craftingLock", (stack, machine, player) -> {
            GameProfile owner = machine.getOwner();
            if (owner != null && !player.getGameProfile().equals(owner)) {
                if (!player.world.isRemote) player.sendMessage(new TextComponentString("You can't lock a machine you don't own!"));
                return true;
            }
            return false;
        }, (stack, machine, player) -> {
            if (player != null && !machine.isPrivate()) machine.setPrivate(true, player.getGameProfile());
        }),
        STEAM_UPGRADE(GtUpgradeType.STEAM, 1, 1,"Lets Machines consume Steam at 2mb per EU (lossless)", "craftingSteamUpgrade", (stack, machine, player) -> {
            if (!machine.hasSteamTank()) machine.addSteamTank();
        }),

        STEAM_TANK(GtUpgradeType.STEAM, 4, 1, "Increases Steam Capacity by 64 Buckets", "craftingSteamTank", (stack, machine) ->  machine.hasSteamTank(), (stack, machine, player) -> {
            FluidTank steamTank = machine.getSteamTank();
            if (steamTank != null) steamTank.setCapacity(steamTank.getCapacity() + (64000 * stack.getCount()));
        });

        private Item instance;
        public final GtUpgradeType type;
        public final int maxCount;
        public final int requiredTier;
        public final String description;
        public final String oreDict;
        public BiPredicate<ItemStack, IUpgradableMachine> condition;
        public final TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> beforeInsert;
        public final TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert;

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict) {
            this(type, maxCount, requiredTier, description, oreDict, GtUtil.alwaysTrue(), (stack, machine, player) -> false, (stack, machine, player) -> {});
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, description, oreDict, GtUtil.alwaysTrue(), (stack, machine, player) -> false, afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, BiPredicate<ItemStack, IUpgradableMachine> condition, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, description, oreDict, condition, (stack, machine, player) -> false, afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> beforeInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert) {
            this(type, maxCount, requiredTier, description, oreDict, GtUtil.alwaysTrue(), beforeInsert, afterInsert);
        }

        Upgrade(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, BiPredicate<ItemStack, IUpgradableMachine> condition, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> beforeInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> afterInsert) {
            this.type = type;
            this.maxCount = maxCount;
            this.requiredTier = requiredTier;
            this.description = description;
            this.oreDict = oreDict;
            this.condition = condition;
            this.beforeInsert = beforeInsert;
            this.afterInsert = afterInsert;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemUpgrade(this.name().toLowerCase(Locale.ROOT), this.description, this.type, this.maxCount, this.requiredTier, this.condition, this.beforeInsert, this.afterInsert)
                        .setFolder("upgrade")
                        .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                        .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Cover {
        ACTIVE_DETECTOR("Emits redstone if the machine has work", "craftingWorkDetector"),
        CONVEYOR("Moves items around", "craftingConveyor"),
        CRAFTING("A workbench on a cover", "craftingWorkBench"),
        DRAIN("Collects liquids and rain", "craftingDrain"),
        ENERGY_ONLY("energy_flow_circuit", "Manages large amounts of energy", "craftingCircuitTier07"),
        EU_METER("Outputs redstone depending on stored energy", "craftingEnergyMeter"),
        ITEM_METER("Outputs redstone depending on stored items", "craftingItemMeter"),
        ITEM_VALVE("Moves items and liquids at once!", "craftingItemValve"),
        LIQUID_METER("Outputs redstone depending on stored liquids", "craftingLiquidMeter"),
        MACHINE_CONTROLLER("This can control machines with redstone", "craftingWorkController"),
        PUMP_MODULE("Moves liquids around", "craftingPump"),
        REDSTONE_CONDUCTOR("Throughputs redstone to the cover facing", "craftingRedstoneConductor"),
        REDSTONE_ONLY("data_control_circuit", "Basic computer processor", "craftingCircuitTier06"),
        REDSTONE_SIGNALIZER("Applies a constant redstone signal to a machine", "craftingRedstoneSignalizer"),
        SCREEN("Displays things", "craftingMonitorTier02"),
        SOLAR_PANEL("Makes energy from the Sun", "craftingSolarPanel"),
        SOLAR_PANEL_HV("Makes energy from the Sun at 512EU/t", "craftingSolarPanelHV"),
        SOLAR_PANEL_LV("Makes energy from the Sun at 8EU/t", "craftingSolarPanelLV"),
        SOLAR_PANEL_MV("Makes energy from the Sun at 64EU/t", "craftingSolarPanelMV");

        private Item instance;
        public final String registryName;
        public final String description;
        public final String oreDict;

        Cover(String registryName, String description, String oreDict) {
            this.registryName = registryName;
            this.description = description;
            this.oreDict = oreDict;
        }

        Cover(String description, String oreDict) {
            this.registryName = name().toLowerCase(Locale.ROOT);
            this.description = description;
            this.oreDict = oreDict;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemCover(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("coveritem")
                        .setRegistryName(this.registryName)
                        .setTranslationKey(this.registryName)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum TurbineRotor {
        BRONZE(60, 15000),
        STEEL(80, 10000),
        MAGNALIUM(100, 10000),
        TUNGSTEN_STEEL(90, 30000),
        CARBON(125, 2500);

        private Item instance;
        private final int efficiency;
        private final int durability;

        TurbineRotor(int efficiency, int durability) {
            this.efficiency = efficiency;
            this.durability = durability;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "turbine_rotor_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(name, "Turbine Efficiency:  "+this.efficiency+"%", this.durability)
                        .setFolder("component")
                        .setEnchantable(false)
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB)
                        .setMaxStackSize(1)
                        .setNoRepair();
            }

            return this.instance;
        }
    }

    public enum Component {
        SUPERCONDUCTOR("Conducts Energy losslessly", "craftingSuperconductor"),
        DATA_STORAGE_CIRCUIT("Stores Data", "craftingCircuitTier05"),
        LITHIUM_BATTERY(ItemLithiumBattery::new, "craftingLiBattery"),
        COIL_KANTHAL("Standard Heating Coil", "craftingHeatingCoilTier01"),
        COIL_NICHROME("Advanced Heating Coil", "craftingHeatingCoilTier02"),
        COIL_CUPRONICKEL("Cheap and simple Heating Coil", "craftingHeatingCoilTier00"),
        HULL_ALUMINIUM("Machine Block", "craftingRawMachineTier01"),
        HULL_BRASS("Cheap Machine Block", "craftingRawMachineTier00"),
        HULL_BRONZE("Cheap Machine Block", "craftingRawMachineTier00"),
        HULL_IRON("Machine Block", "craftingRawMachineTier01"),
        HULL_STEEL("Advanced Machine Block", "craftingRawMachineTier02"),
        HULL_TITANIUM("Very Advanced Machine Block", "craftingRawMachineTier03"),
        HULL_TUNGSTEN_STEEL("Very Advanced Machine Block", "craftingRawMachineTier03"),
        CIRCUIT_BOARD_BASIC("Just a simple Circuit Plate", "craftingCircuitBoardTier02"),
        CIRCUIT_BOARD_ADVANCED("Standard Circuit Plate", "craftingCircuitBoardTier04"),
        CIRCUIT_BOARD_PROCESSOR("Highly advanced Circuit Plate", "craftingCircuitBoardTier06"),
        TURBINE_BLADE_BRONZE("Heavy Turbine Blade", "craftingTurbineBladeBronze"),
        TURBINE_BLADE_CARBON("Ultralight Turbine Blade", "craftingTurbineBladeCarbon"),
        TURBINE_BLADE_MAGNALIUM("Light Turbine Blade", "craftingTurbineBladeMagnalium"),
        TURBINE_BLADE_STEEL("Standard Turbine Blade", "craftingTurbineBladeSteel"),
        TURBINE_BLADE_TUNGSTEN_STEEL("Durable Turbine Blade", "craftingTurbineBladeTungstenSteel"),
        GEAR_IRON(GregTechMod.classic ? "A Refined Iron Gear" : "An Iron Gear", "gearIron"),
        GEAR_BRONZE("A Bronze Gear", "gearBronze"),
        GEAR_STEEL("A Steel Gear", "gearSteel"),
        GEAR_TITANIUM("A Titanium Gear", "gearTitanium"),
        GEAR_TUNGSTEN_STEEL("A Tungstensteel Gear", "gearTungstenSteel"),
        GEAR_IRIDIUM("An Iridium Gear", "gearIridium"),
        DIAMOND_SAWBLADE("Caution! This is very sharp.", "craftingDiamondBlade"),
        DIAMOND_GRINDER("Fancy Grinding Head", "craftingGrinder"),
        WOLFRAMIUM_GRINDER("Regular Grinding Head", "craftingGrinder"),
        MACHINE_PARTS("Random Machine Parts", "craftingMachineParts"),
        ADVANCED_CIRCUIT_PARTS("Part of advanced Circuitry", "craftingCircuitPartsTier04"),
        DUCT_TAPE("If you can't fix it with this, use more of it!", "craftingDuctTape"),
        DATA_ORB(ItemDataOrb::new, "craftingCircuitTier08");

        private Item instance;
        private final Supplier<Item> constructor;
        public final String oreDict;

        Component(Supplier<Item> constructor, String oreDict) {
            this.constructor = constructor;
            this.oreDict = oreDict;
        }

        Component(String description, String oreDict) {
            this.constructor = () -> new ItemBase(this.name().toLowerCase(Locale.ROOT), description)
                                        .setFolder("component")
                                        .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                                        .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            this.oreDict = oreDict;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = this.constructor.get();
            }

            return this.instance;
        }
    }

    public enum Tool {
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
        LAPOTRONIC_ENERGY_ORB(() -> new ItemElectricBase("lapotronic_energy_orb", null, GregTechMod.classic ? 10000000 : 100000000, 8192, GregTechMod.classic ? 4 : 5, 0, true)
                .setFolder("tool")
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

        public final Supplier<Item> constructor;
        public final String oreDict;
        private Item instance;

        Tool(Supplier<Item> constructor) {
            this(constructor, null);
        }

        Tool(Supplier<Item> constructor, String oreDict) {
            this.constructor = constructor;
            this.oreDict = oreDict;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = this.constructor.get();
            }

            return this.instance;
        }
    }
    
    public enum ColorSpray {
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

        private Item instance;

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemSprayColor(EnumDyeColor.byMetadata(this.ordinal()));
            }

            return this.instance;
        }
    }

    public enum Wrench {
        IRON(128, 4),
        BRONZE(256, 6),
        STEEL(512, 8),
        TUNGSTEN_STEEL(5120, 10);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Wrench(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemWrench("wrench_"+this.name().toLowerCase(Locale.ROOT), this.durability, this.entityDamage)
                                    .setRegistryName("wrench_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum JackHammer {
        BRONZE(50, 10000, 1, 50, 7.5F, false),
        STEEL(100, 10000, 1, 50, 15F, false),
        DIAMOND(250, 100000, 2, 100, 45F, true);

        private Item instance;
        public final int operationEnergyCost;
        public final int maxCharge;
        public final int tier;
        public final int transferLimit;
        public final float efficiency;
        public final boolean canMineObsidian;

        JackHammer(int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency, boolean canMineObsidian) {
            this.operationEnergyCost = operationEnergyCost;
            this.maxCharge = maxCharge;
            this.tier = tier;
            this.transferLimit = transferLimit;
            this.efficiency = efficiency;
            this.canMineObsidian = canMineObsidian;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemJackHammer("jack_hammer_"+this.name().toLowerCase(Locale.ROOT), this.operationEnergyCost, this.maxCharge, this.tier, this.transferLimit, this.efficiency, this.canMineObsidian)
                                    .setRegistryName("jack_hammer_"+this.name().toLowerCase(Locale.ROOT))
                                    .setTranslationKey("jack_hammer_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Hammer {
        IRON(128, 4),
        BRONZE(256, 6),
        STEEL(512, 8),
        TUNGSTEN_STEEL(5120, 10);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Hammer(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemHardHammer(this.name().toLowerCase(Locale.ROOT), "To give a machine a hard whack", this.durability, this.entityDamage)
                                    .setRegistryName("hammer_"+this.name().toLowerCase(Locale.ROOT))
                                    .setTranslationKey("hammer_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Saw {
        IRON(128, 3, 2),
        BRONZE(256, 4, 3),
        STEEL(1280, 6, 4),
        TUNGSTEN_STEEL(5120, 8, 5);

        private Item instance;
        public final int durability;
        public final int efficiency;
        public final int entityDamage;

        Saw(int durability, int efficiency, int entityDamage) {
            this.durability = durability;
            this.efficiency = efficiency;
            this.entityDamage = entityDamage;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemSaw(this.name().toLowerCase(Locale.ROOT), this.durability, this.efficiency, this.entityDamage)
                                    .setRegistryName("saw_"+this.name().toLowerCase(Locale.ROOT))
                                    .setTranslationKey("saw_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum SolderingMetal {
        LEAD(10),
        TIN(50);

        private Item instance;
        public final int durability;

        SolderingMetal(int durability) {
            this.durability = durability;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemSolderingMetal(this.name().toLowerCase(Locale.ROOT), this.durability)
                                    .setRegistryName("soldering_"+this.name().toLowerCase(Locale.ROOT))
                                    .setTranslationKey("soldering_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum File {
        IRON(128, 2),
        BRONZE(256, 3),
        STEEL(1280, 3),
        TUNGSTEN_STEEL(5120, 4);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        File(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemFile(this.name().toLowerCase(Locale.ROOT), this.durability, this.entityDamage)
                        .setRegistryName("file_"+this.name().toLowerCase(Locale.ROOT))
                        .setTranslationKey("file_"+this.name().toLowerCase(Locale.ROOT))
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Cell {
        CARBON("C"),
        ICE("H2O"),
        NITROCARBON("NC"),
        SODIUM_SULFIDE("NaS"),
        SULFUR("S"),
        SULFURIC_ACID("H2SO4");

        private Item instance;
        public final String description;

        Cell(String description) {
            this.description = description;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "cell_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                                    .setFolder("cell")
                                    .setRegistryName(name)
                                    .setTranslationKey(name)
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum NuclearCoolantPack {
        COOLANT_NAK_60K(60000, "crafting60kCoolantStore"),
        COOLANT_NAK_180K(180000, "crafting180kCoolantStore"),
        COOLANT_NAK_360K(360000, "crafting360kCoolantStore"),
        COOLANT_HELIUM_60K(60000, "crafting60kCoolantStore"),
        COOLANT_HELIUM_180K(180000, "crafting180kCoolantStore"),
        COOLANT_HELIUM_360K(360000, "crafting360kCoolantStore");

        private Item instance;
        public final int heatStorage;
        public final String oreDict;

        NuclearCoolantPack(int heatStorage, String oreDict) {
            this.heatStorage = heatStorage;
            this.oreDict = oreDict;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemNuclearHeatStorage(this.name().toLowerCase(Locale.ROOT), this.heatStorage)
                                    .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum NuclearFuelRod {
        THORIUM(1, 25000, 0.25F, 1, 0.25F),
        THORIUM_DUAL(2, 25000, 0.25F, 1, 0.25F),
        THORIUM_QUAD(4, 25000, 0.25F, 1, 0.25F),
        PLUTONIUM(1, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_uranium")),
        PLUTONIUM_DUAL(2, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_dual_uranium")),
        PLUTONIUM_QUAD(4, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_quad_uranium"));

        private Item instance;
        public final int cells;
        public final int duration;
        public final float energy;
        public final int radiation;
        public final float heat;
        public final ItemStack depletedStack;

        NuclearFuelRod(int cells, int duration, float energy, int radiation, float heat) {
            this(cells, duration, energy, radiation, heat, null);
        }

        NuclearFuelRod(int cells, int duration, float energy, int radiation, float heat, ItemStack depletedStack) {
            this.cells = cells;
            this.duration = duration;
            this.energy = energy;
            this.radiation = radiation;
            this.heat = heat;
            this.depletedStack = depletedStack;
        }

        public Item getInstance() {
            if (this.instance == null) { //TODO: Rename prefix to rod (or fuel_rod)
                this.instance = new ItemNuclearFuelRod("cell_"+this.name().toLowerCase(Locale.ROOT), this.cells, this.duration, this.energy, this.radiation, this.heat, this.depletedStack)
                                    .setRegistryName("cell_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Armor {
        CLOAKING_DEVICE(EntityEquipmentSlot.CHEST, GregTechMod.classic ? 10000000 : 100000000, 8192, GregTechMod.classic ? 4 : 5, 0, 0, false, ArmorPerk.INVISIBILITY_FIELD),
        LAPOTRONPACK(EntityEquipmentSlot.CHEST, GregTechMod.classic ? 10000000 : 100000000, 8192, GregTechMod.classic ? 4 : 5, 0, 0, true, GregTechMod.classic ? "crafting10kkEUPack" : "crafting100kkEUPack"),
        LITHIUM_BATPACK(EntityEquipmentSlot.CHEST, 600000, 128, 1, 0, 0, true, "crafting600kEUPack"),
        ULTIMATE_CHEAT_ARMOR(EntityEquipmentSlot.CHEST, 1000000000, Integer.MAX_VALUE, 1, 10, 100, true, EnumSet.allOf(ArmorPerk.class).toArray(new ArmorPerk[0])),
        LIGHT_HELMET(EntityEquipmentSlot.HEAD, 10000, 32, 1, 0, 0, false, ArmorPerk.LAMP, ArmorPerk.SOLARPANEL);

        private Item instance;
        public final EntityEquipmentSlot slot;
        public final int maxCharge;
        public final int transferLimit;
        public final int tier;
        public final int damageEnergyCost;
        public final double absorbtionDamage;
        public final boolean chargeProvider;
        public final String oreDict;
        public final ArmorPerk[] perks;

        Armor(EntityEquipmentSlot slot, int maxCharge, int transferLimit, int tier, int damageEnergyCost, double absorbtionPercentage, boolean chargeProvider, ArmorPerk... perks) {
            this(slot, maxCharge, transferLimit, tier, damageEnergyCost, absorbtionPercentage, chargeProvider, null, perks);
        }

        Armor(EntityEquipmentSlot slot, int maxCharge, int transferLimit, int tier, int damageEnergyCost, double absorbtionPercentage, boolean chargeProvider, String oreDict, ArmorPerk... perks) {
            this.slot = slot;
            this.maxCharge = maxCharge;
            this.transferLimit = transferLimit;
            this.tier = tier;
            this.damageEnergyCost = damageEnergyCost;
            this.absorbtionDamage = absorbtionPercentage;
            this.chargeProvider = chargeProvider;
            this.oreDict = oreDict;
            this.perks = perks;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemArmorElectricBase(this.name().toLowerCase(Locale.ROOT), this.slot, this.maxCharge, this.transferLimit, this.tier, this.damageEnergyCost, this.absorbtionDamage, this.chargeProvider, this.perks)
                                .setFolder("armor")
                                .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                                .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                                .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Miscellaneous {
        GREG_COIN("I put the G in 'Gregoriette'"),
        CREDIT_COPPER("0.125 Credits"),
        CREDIT_SILVER("8 Credits"),
        CREDIT_GOLD("64 Credits"),
        CREDIT_DIAMOND("512 Credits"),
        RUBY(Dust.RUBY.description, "gemRuby"),
        SAPPHIRE(Dust.SAPPHIRE.description, "gemSapphire"),
        GREEN_SAPPHIRE(Dust.GREEN_SAPPHIRE.description, "gemGreenSapphire"),
        OLIVINE(Dust.OLIVINE.description, "gemOlivine"),
        LAZURITE_CHUNK("(Al6Si6Ca8Na8)8", "chunkLazurite"),
        RED_GARNET(Dust.RED_GARNET.description, "gemGarnetRed"),
        YELLOW_GARNET(Dust.YELLOW_GARNET.description, "gemGarnetYellow"),
        INDIGO_BLOSSOM,
        INDIGO_DYE(null, "dyeBlue"),
        FLOUR(null, "dustWheat"),
        SPRAY_CAN_EMPTY("Used for making Sprays and storing Colors", "craftingSprayCan"),
        LAVA_FILTER(() -> new ItemBase("lava_filter", "Filters Lava in Thermal Boilers", 100)
                            .setFolder("component")
                            .setEnchantable(false)
                            .setRegistryName("lava_filter")
                            .setTranslationKey("lava_filter")
                            .setCreativeTab(GregTechMod.GREGTECH_TAB)
                            .setMaxStackSize(1)
                            .setNoRepair()),
        MORTAR_FLINT("Used to turn ingots into dust"),
        MORTAR_IRON(() -> new ItemMortar("iron", 63, IC2Items.getItem("dust", "iron"))
                            .setRegistryName("mortar_iron")
                            .setTranslationKey("mortar_iron")
                            .setCreativeTab(GregTechMod.GREGTECH_TAB));

        private Item instance;
        private final Supplier<Item> constructor;
        public final String oreDict;

        Miscellaneous() {
            this((String) null);
        }

        Miscellaneous(String description) {
            this(description, null);
        }

        Miscellaneous(String description, String oreDict) {
            this.constructor = () -> new ItemBase(this.name().toLowerCase(Locale.ROOT), description)
                                        .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                                        .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            this.oreDict = oreDict;
        }

        Miscellaneous(Supplier<Item> constructor) {
            this.constructor = constructor;
            this.oreDict = null;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = this.constructor.get();
            }

            return this.instance;
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

        public final String author;
        public final int pages;
        private ItemStack instance;

        Book(String author, int pages) {
            this.author = author;
            this.pages = pages;
        }

        public ItemStack getInstance() {
            if (this.instance == null) {
                this.instance = GtUtil.getWrittenBook(this.name().toLowerCase(Locale.ROOT), this.author, this.pages, this.ordinal());
            }

            return this.instance;
        }
    }
}