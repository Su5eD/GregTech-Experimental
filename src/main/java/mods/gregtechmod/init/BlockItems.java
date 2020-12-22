package mods.gregtechmod.init;

import com.mojang.authlib.GameProfile;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.util.ArmorPerk;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.BlockBase;
import mods.gregtechmod.objects.blocks.BlockOre;
import mods.gregtechmod.objects.blocks.ConnectedBlock;
import mods.gregtechmod.objects.items.ItemDataOrb;
import mods.gregtechmod.objects.items.ItemDestructorPack;
import mods.gregtechmod.objects.items.ItemSolderingMetal;
import mods.gregtechmod.objects.items.ItemSonictron;
import mods.gregtechmod.objects.items.base.*;
import mods.gregtechmod.objects.items.components.ItemLithiumBattery;
import mods.gregtechmod.objects.items.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidTank;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class BlockItems {
    public static Block lightSource;
    public static Item sensorKit;
    public static Item sensorCard;

    public enum Blocks {
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

        private Block instance;
        private final Supplier<Block> constructor;
        private final float hardness;
        private final float resistance;

        Blocks(float hardness, float resistance) {
            this(() -> new BlockBase(Material.IRON), hardness, resistance);
        }

        Blocks(Supplier<Block> constructor, float hardness, float resistance) {
            this.constructor = constructor;
            this.hardness = hardness;
            this.resistance = resistance;
        }

        public Block getInstance() {
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

    public enum Ores {
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
            drops.add(new ItemStack(Dusts.PYRITE.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
        }),
        CINNABAR(3, 3, 3, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.CINNABAR.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Items.REDSTONE, 1));
        }),
        SPHALERITE(2, 1, 1, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.SPHALERITE.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dusts.ZINC.instance));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 32 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dusts.YELLOW_GARNET.instance));
        }),
        TUNGSTATE(4, 0, 0, (fortune, drops) -> {}),
        SHELDONITE(3.5F, 0, 0, (fortune, drops) -> {}),
        OLIVINE(3, 0, 0, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.OLIVINE.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
        }),
        SODALITE(3, 0, 0, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.SODALITE.instance, 6 + 3 * GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dusts.ALUMINIUM.instance));
        }),
        TETRAHEDRITE(3, 0, 0, (fortune, drops) -> {}),
        CASSITERITE(3, 0, 0, (fortune, drops) -> {});
        private Block instance;
        public final float hardness;
        public final int dropChance;
        public final int dropRandom;
        public final BiConsumer<Integer, List<ItemStack>> loot;

        Ores(float hardness, int dropChance, int dropRandom, BiConsumer<Integer, List<ItemStack>> loot) {
            this.hardness = hardness;
            this.dropChance = dropChance;
            this.dropRandom = dropRandom;
            this.loot = loot;
        }

        public Block getInstance() {
            if (this.instance == null) {
                String name = "block_"+this.name().toLowerCase(Locale.ROOT)+"_ore";
                this.instance = new BlockOre(this.name().toLowerCase(Locale.ROOT), this.dropChance, this.dropRandom, this.loot)
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB)
                        .setHardness(this.hardness);
            }

            return this.instance;
        }
    }

    public enum Ingots {
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
        SOLDERING_IRON_ALLOY("Sn9Sb1"),
        THORIUM("Th", true),
        TITANIUM("Ti"),
        TUNGSTEN("W"),
        TUNGSTEN_STEEL("Vacuum Hardened"),
        ZINC("Zn");

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Ingots(String description) {
            this(description, false);
        }

        Ingots(String description, boolean hasEffect) {
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

    public enum Nuggets {
        ALUMINIUM(Ingots.ALUMINIUM.description),
        ANTIMONY(Ingots.ANTIMONY.description),
        BRASS(Ingots.BRASS.description),
        CHROME(Ingots.CHROME.description),
        COPPER("Cu"),
        ELECTRUM(Ingots.ELECTRUM.description),
        INVAR(Ingots.INVAR.description),
        LEAD("Pg"),
        NICKEL(Ingots.NICKEL.description),
        OSMIUM(Ingots.OSMIUM.description),
        PLATINUM(Ingots.PLATINUM.description),
        SILVER(Plates.SILVER.description),
        STEEL("Fe"),
        TIN("Sn"),
        TITANIUM(Ingots.TITANIUM.description),
        TUNGSTEN(Ingots.TUNGSTEN.description),
        ZINC(Ingots.ZINC.description);

        private Item instance;
        public final String description;

        Nuggets(String description) {
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

    public enum Plates {
        ALUMINIUM(Ingots.ALUMINIUM.description),
        BATTERY_ALLOY(Ingots.BATTERY_ALLOY.description),
        BRASS(Ingots.BRASS.description),
        CHROME(Ingots.CHROME.description),
        ELECTRUM(Ingots.ELECTRUM.description),
        INVAR(Ingots.INVAR.description),
        MAGNALIUM(Ingots.MAGNALIUM.description),
        NICKEL(Ingots.NICKEL.description),
        OSMIUM(Ingots.OSMIUM.description),
        PLATINUM(Ingots.PLATINUM.description),
        SILICON("Si2"),
        SILVER("Ag"),
        TITANIUM(Ingots.TITANIUM.description),
        TUNGSTEN(Ingots.TUNGSTEN.description),
        TUNGSTEN_STEEL(Ingots.TUNGSTEN_STEEL.description),
        WOOD(null),
        ZINC(Ingots.ZINC.description);

        private Item instance;
        public final String description;

        Plates(String description) {
            this.description = description;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "plate_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("plate")
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Rods {
        LEAD("Pb"),
        BRONZE("SnCu3"),
        TUNGSTEN_STEEL(Ingots.TUNGSTEN_STEEL.description),
        ZINC(Ingots.ZINC.description),
        OSMIUM(Ingots.OSMIUM.description),
        BRASS(Ingots.BRASS.description),
        COPPER("Cu"),
        TIN("Sn"),
        IRON("Fe"),
        STEEL("Fe"),
        TITANIUM(Ingots.TITANIUM.description),
        PLATINUM(Ingots.PLATINUM.description),
        INVAR(Ingots.INVAR.description),
        NICKEL(Ingots.NICKEL.description),
        CHROME(Ingots.CHROME.description),
        ALUMINIUM(Ingots.ALUMINIUM.description),
        GOLD("Au"),
        ELECTRUM(Ingots.ELECTRUM.description),
        SILVER("Ag"),
        IRIDIUM(Ingots.IRIDIUM.description),
        TUNGSTEN(Ingots.TUNGSTEN.description);

        private Item instance;
        public final String description;

        Rods(String description) {
            this.description = description;
        }

        public Item getInstance() {
            if (this.instance == null) {
                String name = "rod_"+this.name().toLowerCase(Locale.ROOT);
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), this.description)
                        .setFolder("rod")
                        .setRegistryName(name)
                        .setTranslationKey(name)
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Dusts {
        ALMANDINE("Al2Fe3Si3O12"),
        ALUMINIUM(Ingots.ALUMINIUM.description),
        ANDRADITE("Ca3Fe2Si3O12"),
        ANTIMONY(Ingots.ANTIMONY.description),
        BASALT("(Mg2Fe2SiO4)(CaCO3)3(SiO2)8C4"),
        BAUXITE("TiAl16H10O12"),
        BRASS(Ingots.BRASS.description),
        CALCITE("CaCO3"),
        CHARCOAL("C"),
        CHROME(Ingots.CHROME.description),
        CINNABAR("HgS"),
        DARK_ASHES("C"),
        ELECTRUM(Ingots.ELECTRUM.description),
        EMERALD("Be3Al2Si6O18"),
        ENDER_EYE("BeK4N5Cl6C4S2"),
        ENDER_PEARL("BeK4N5Cl6"),
        ENDSTONE,
        FLINT("SiO2"),
        GALENA("Pb3Ag3S2"),
        GREEN_SAPPHIRE("Al206"),
        GROSSULAR("Ca3Al2Si3O12"),
        INVAR(Ingots.INVAR.description),
        LAZURITE("Al6Si6Ca8Na8"),
        MAGNESIUM("Mg"),
        MANGANESE("Mn"),
        MARBLE("Mg(CaCO3)7"),
        NICKEL("Ni"),
        OLIVINE("Mg2Fe2SiO4"),
        OSMIUM("Os"),
        PHOSPHORUS("Ca3(PO4)2"),
        PLATINUM("Pt"),
        PLUTONIUM(Ingots.PLUTONIUM.description, true),
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
        THORIUM(Ingots.THORIUM.description, true),
        TITANIUM(Ingots.TITANIUM.description),
        TUNGSTEN(Ingots.TUNGSTEN.description),
        URANIUM("U", true),
        UVAROVITE("Ca3Cr2Si3O12"),
        WOOD,
        YELLOW_GARNET("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
        ZINC(Ingots.ZINC.description);

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Dusts() {
            this(null);
        }

        Dusts(String description) {
            this(description, false);
        }

        Dusts(String description, boolean hasEffect) {
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

    public enum Smalldusts {
        ALMANDINE(Dusts.ALMANDINE.description),
        ALUMINIUM(Ingots.ALUMINIUM.description),
        ANDRADITE(Dusts.ANDRADITE.description),
        ANTIMONY(Ingots.ANTIMONY.description),
        BASALT(Dusts.BASALT.description),
        BAUXITE(Dusts.BAUXITE.description),
        BRASS(Ingots.BRASS.description),
        CALCITE(Dusts.CALCITE.description),
        CHARCOAL(Dusts.CHARCOAL.description),
        CHROME(Ingots.CHROME.description),
        CINNABAR(Dusts.CINNABAR.description),
        CLAY("Na2LiAl2Si2"),
        COAL("C2"),
        DARK_ASHES(Dusts.DARK_ASHES.description),
        DIAMOND("C128"),
        ELECTRUM(Ingots.ELECTRUM.description),
        EMERALD(Dusts.EMERALD.description),
        ENDER_EYE(Dusts.ENDER_EYE.description),
        ENDER_PEARL(Dusts.ENDER_PEARL.description),
        ENDSTONE,
        FLINT(Dusts.FLINT.description),
        GALENA(Dusts.GALENA.description),
        GLOWSTONE,
        GREEN_SAPPHIRE(Dusts.GREEN_SAPPHIRE.description),
        GROSSULAR(Dusts.GROSSULAR.description),
        GUNPOWDER,
        INVAR(Ingots.INVAR.description),
        LAZURITE(Dusts.LAZURITE.description),
        MAGNESIUM(Dusts.MAGNESIUM.description),
        MANGANESE(Dusts.MANGANESE.description),
        MARBLE(Dusts.MARBLE.description),
        NETHERRACK,
        NICKEL(Ingots.NICKEL.description),
        OLIVINE(Dusts.OLIVINE.description),
        OSMIUM(Ingots.OSMIUM.description),
        PHOSPHORUS(Dusts.PHOSPHORUS.description),
        PLATINUM(Ingots.PLATINUM.description),
        PLUTONIUM(Ingots.PLUTONIUM.description, true),
        PYRITE(Dusts.PYRITE.description),
        PYROPE(Dusts.PYROPE.description),
        RED_GARNET(Dusts.RED_GARNET.description),
        REDROCK(Dusts.REDROCK.description),
        REDSTONE,
        RUBY(Dusts.RUBY.description),
        SALTPETER(Dusts.SALTPETER.description),
        SAPPHIRE(Dusts.SAPPHIRE.description),
        SODALITE(Dusts.SODALITE.description),
        SPESSARTINE(Dusts.SPESSARTINE.description),
        SPHALERITE(Dusts.SPHALERITE.description),
        STEEL(Dusts.STEEL.description),
        THORIUM(Ingots.THORIUM.description, true),
        TITANIUM(Ingots.TITANIUM.description),
        TUNGSTEN(Ingots.TUNGSTEN.description),
        URANIUM(Dusts.URANIUM.description, true),
        UVAROVITE(Dusts.UVAROVITE.description),
        WOOD(Dusts.WOOD.description),
        YELLOW_GARNET(Dusts.YELLOW_GARNET.description),
        ZINC(Ingots.ZINC.description);

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Smalldusts() {
            this(null);
        }

        Smalldusts(String description) {
            this(description, false);
        }

        Smalldusts(String description, boolean hasEffect) {
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

    public enum Upgrades {
        HV_TRANSFORMER(GtUpgradeType.TRANSFORMER, 2, 3, "Higher tier of the transformer upgrade", "craftingHVTUpgrade", (stack, machine) -> machine.getTier() < 5, (stack, machine, player) -> machine.setSinkTier(Math.min(machine.getSinkTier()+stack.getCount(), 5))),
        LITHIUM_BATTERY(GtUpgradeType.BATTERY, 4, 1, "Adds 100000 EU to the energy capacity", "craftingLiBattery", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000*stack.getCount()))),
        ENERGY_CRYSTAL(GtUpgradeType.BATTERY, 4, 2, "Adds 100000 EU to the energy capacity", "crafting100kEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000*stack.getCount()))),
        LAPOTRON_CRYSTAL(GtUpgradeType.BATTERY, 4, 3, "Adds 1 Million EU to the energy capacity", "crafting1kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(1000000*stack.getCount()))),
        ENERGY_ORB(GtUpgradeType.BATTERY, 4, 4, "Adds 10 Million EU to the energy capacity", "crafting10kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(1000000*stack.getCount()))),
        QUANTUM_CHEST(GtUpgradeType.QUANTUM_CHEST, 1, 0, "Upgrades a Digital Chest to a Quantum chest", "craftingQuantumChestUpgrade"),
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
            if (steamTank != null) steamTank.setCapacity(2000 + stack.getCount() * 64000);
        });

        private Item instance;
        public final GtUpgradeType type;
        public final int maxCount;
        public final int requiredTier;
        public final String description;
        public final String oreDict;
        public BiPredicate<ItemStack, IUpgradableMachine> condition;
        public final TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert;
        public final TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate;

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict) {
            this(type, maxCount, requiredTier, description, oreDict, GtUtil.alwaysTrue(), (stack, machine, player) -> false, (stack, machine, player) -> {});
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, oreDict, GtUtil.alwaysTrue(), (stack, machine, player) -> false, onUpdate);
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, BiPredicate<ItemStack, IUpgradableMachine> condition, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, oreDict, condition, (stack, machine, player) -> false, onUpdate);
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, oreDict, GtUtil.alwaysTrue(), onInsert, onUpdate);
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, String oreDict, BiPredicate<ItemStack, IUpgradableMachine> condition, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this.type = type;
            this.maxCount = maxCount;
            this.requiredTier = requiredTier;
            this.description = description;
            this.oreDict = oreDict;
            this.condition = condition;
            this.onInsert = onInsert;
            this.onUpdate = onUpdate;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemUpgrade(this.name().toLowerCase(Locale.ROOT), this.description, this.type, this.maxCount, this.requiredTier, this.condition, this.onInsert, this.onUpdate)
                        .setFolder("upgrade")
                        .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                        .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                        .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Covers {
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
        REDSTONE_ONLY("data_control_circuit", "Basic computer processor", "craftingCircuitTier03"),
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

        Covers(String registryName, String description, String oreDict) {
            this.registryName = registryName;
            this.description = description;
            this.oreDict = oreDict;
        }

        Covers(String description, String oreDict) {
            this.registryName = name();
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

    public enum TurbineRotors {
        TURBINE_ROTOR_BRONZE(60, 15000),
        TURBINE_ROTOR_STEEL(80, 10000),
        TURBINE_ROTOR_MAGNALIUM(100, 10000),
        TURBINE_ROTOR_TUNGSTEN_STEEL(90, 30000),
        TURBINE_ROTOR_CARBON(125, 2500);

        private Item instance;
        private final int efficiency;
        private final int durability;

        TurbineRotors(int efficiency, int durability) {
            this.efficiency = efficiency;
            this.durability = durability;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemBase(this.name().toLowerCase(Locale.ROOT), "Turbine Efficiency:  "+this.efficiency+"%", this.durability)
                        .setFolder("component")
                        .setEnchantable(false)
                        .setRegistryName(this.name().toLowerCase(Locale.ROOT))
                        .setTranslationKey(this.name().toLowerCase(Locale.ROOT))
                        .setCreativeTab(GregTechMod.GREGTECH_TAB)
                        .setMaxStackSize(1)
                        .setNoRepair();
            }

            return this.instance;
        }
    }

    public enum Components {
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
        GEAR_IRON("An Iron Gear", "gearIron"),
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

        Components(Supplier<Item> constructor, String oreDict) {
            this.constructor = constructor;
            this.oreDict = oreDict;
        }

        Components(String description, String oreDict) {
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

    public enum Tools {
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
        LAPOTRONIC_ENERGY_ORB(() -> new ItemElectricBase("lapotronic_energy_orb", null, 10000000, 8192, 4)
                .setFolder("tool")
                .setRegistryName("lapotronic_energy_orb")
                .setTranslationKey("lapotronic_energy_orb")
                .setCreativeTab(GregTechMod.GREGTECH_TAB), "crafting10kkEUStore"),
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

        Tools(Supplier<Item> constructor) {
            this(constructor, null);
        }

        Tools(Supplier<Item> constructor, String oreDict) {
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
    
    public enum ColorSprays {
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

    public enum Wrenches {
        IRON(128, 4),
        BRONZE(256, 6),
        STEEL(512, 8),
        TUNGSTEN_STEEL(5120, 10);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Wrenches(int durability, int entityDamage) {
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

    public enum JackHammers {
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

        JackHammers(int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency, boolean canMineObsidian) {
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

    public enum Hammers {
        IRON(128, 4),
        BRONZE(256, 6),
        STEEL(512, 8),
        TUNGSTEN_STEEL(5120, 10);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Hammers(int durability, int entityDamage) {
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

    public enum Saws {
        IRON(128, 3, 2),
        BRONZE(256, 4, 3),
        STEEL(1280, 6, 4),
        TUNGSTEN_STEEL(5120, 8, 5);

        private Item instance;
        public final int durability;
        public final int efficiency;
        public final int entityDamage;

        Saws(int durability, int efficiency, int entityDamage) {
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

    public enum SolderingMetals {
        LEAD(10),
        TIN(50);

        private Item instance;
        public final int durability;

        SolderingMetals(int durability) {
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

    public enum Files {
        IRON(128, 2),
        BRONZE(256, 3),
        STEEL(1280, 3),
        TUNGSTEN_STEEL(5120, 4);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Files(int durability, int entityDamage) {
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

    public enum Cells {
        ICE("H2O"),
        NITROCARBON("NC"),
        SODIUM_SULFIDE("NaS"),
        SULFUR("S"),
        SULFURIC_ACID("H2SO4");

        private Item instance;
        public final String description;

        Cells(String description) {
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

    public enum NuclearCoolantPacks {
        COOLANT_NAK_60K(60000, "crafting60kCoolantStore"),
        COOLANT_NAK_180K(180000, "crafting180kCoolantStore"),
        COOLANT_NAK_360K(360000, "crafting360kCoolantStore"),
        COOLANT_HELIUM_60K(60000, "crafting60kCoolantStore"),
        COOLANT_HELIUM_180K(180000, "crafting180kCoolantStore"),
        COOLANT_HELIUM_360K(360000, "crafting360kCoolantStore");

        private Item instance;
        public final int heatStorage;
        public final String oreDict;

        NuclearCoolantPacks(int heatStorage, String oreDict) {
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

    public enum NuclearFuelRods {
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

        NuclearFuelRods(int cells, int duration, float energy, int radiation, float heat) {
            this(cells, duration, energy, radiation, heat, null);
        }

        NuclearFuelRods(int cells, int duration, float energy, int radiation, float heat, ItemStack depletedStack) {
            this.cells = cells;
            this.duration = duration;
            this.energy = energy;
            this.radiation = radiation;
            this.heat = heat;
            this.depletedStack = depletedStack;
        }

        public Item getInstance() {
            if (this.instance == null) {
                this.instance = new ItemNuclearFuelRod("cell_"+this.name().toLowerCase(Locale.ROOT), this.cells, this.duration, this.energy, this.radiation, this.heat, this.depletedStack)
                                    .setRegistryName("cell_"+this.name().toLowerCase(Locale.ROOT))
                                    .setCreativeTab(GregTechMod.GREGTECH_TAB);
            }

            return this.instance;
        }
    }

    public enum Armor {
        CLOAKING_DEVICE(EntityEquipmentSlot.CHEST, 10000000, 8192, 4, 0, 0, false, ArmorPerk.INVISIBILITY_FIELD),
        LAPOTRONPACK(EntityEquipmentSlot.CHEST, 10000000, 8192, 4, 0, 0, true, "crafting10kkEUPack"),
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
        RUBY(Dusts.RUBY.description, "gemRuby"),
        SAPPHIRE(Dusts.SAPPHIRE.description, "gemSapphire"),
        GREEN_SAPPHIRE(Dusts.GREEN_SAPPHIRE.description, "gemGreenSapphire"),
        OLIVINE(Dusts.OLIVINE.description, "gemOlivine"),
        LAZURITE_CHUNK("(Al6Si6Ca8Na8)8", "chunkLazurite"),
        RED_GARNET(Dusts.RED_GARNET.description, "gemGarnetRed"),
        YELLOW_GARNET(Dusts.YELLOW_GARNET.description, "gemGarnetYellow"),
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

    public enum Books {
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

        Books(String author, int pages) {
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