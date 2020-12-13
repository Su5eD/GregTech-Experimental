package mods.gregtechmod.api;

import com.mojang.authlib.GameProfile;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.util.ArmorPerk;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.api.util.TriFunction;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidTank;

import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class BlockItems {
    public static Block lightSource;
    public static Item sensor_kit;
    public static Item sensor_card;

    public enum Blocks {
        advanced_machine(4, 30),
        aluminium(3, 30),
        brass(3.5F, 30),
        chrome(10, 100),
        electrum(4, 30),
        fusion_coil(4, 30),
        green_sapphire(4.5F, 30),
        highly_advanced_machine(10, 100),
        invar(4.5F, 30),
        iridium(3.5F, 600),
        iridium_reinforced_stone(100, 300),
        lesublock(4, 30),
        nickel(3, 45),
        olivine(4.5F, 30),
        osmium(4, 900),
        platinum(4, 30),
        ruby(4.5F, 30),
        sapphire(4.5F, 30),
        titanium(10, 200),
        tungsten(4.5F, 100),
        zinc(3.5F, 30);
        private Block instance;
        public final float hardness;
        public final float resistance;

        Blocks(float hardness, float resistance) {
            this.hardness = hardness;
            this.resistance = resistance;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Block block) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = block;
        }

        public Block getInstance() {
            return this.instance;
        }
    }

    public enum ConnectedBlocks {
        advanced_machine_casing(3, 30),
        iridium_reinforced_tungsten_steel(200, 400),
        reinforced_machine_casing(3, 60),
        standard_machine_casing(3, 30),
        tungsten_steel(100, 300);

        private Block instance;
        public final float hardness;
        public final float resistance;

        ConnectedBlocks(float hardness, float resistance) {
            this.hardness = hardness;
            this.resistance = resistance;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Block block) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = block;
        }

        public Block getInstance() {
            return this.instance;
        }
    }

    public enum Ores {
        galena(3, 0, 0, (fortune, drops) -> {}),
        iridium(20, 30, 21, (fortune, drops) -> {
            ItemStack iridium = IC2Items.getItem("misc_resource", "iridium_ore");
            iridium.setCount(1 + GtUtil.RANDOM.nextInt(1 + fortune / 2));
            drops.add(iridium);
            //TODO: Add as oredict alternative
            //GT_OreDictUnificator.getFirstOre("dustIridium", 1 + GtUtil.RANDOM.nextInt(1 + fortune / 2)
        }),
        ruby(4, 3, 5, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.ruby.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 32 / (fortune + 1))) == 0) drops.add(new ItemStack(Miscellaneous.red_garnet.instance));
        }),
        sapphire(4, 3, 5, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.sapphire.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 64 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Miscellaneous.green_sapphire.instance, 1));
        }),
        bauxite(3, 0, 0, (fortune, drops) -> {}),
        pyrite(2, 1, 1, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.pyrite.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
        }),
        cinnabar(3, 3, 3, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.cinnabar.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Items.REDSTONE, 1));
        }),
        sphalerite(2, 1, 1, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.sphalerite.instance, 2 + GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dusts.zinc.instance));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 32 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dusts.yellow_garnet.instance));
        }),
        tungstate(4, 0, 0, (fortune, drops) -> {}),
        sheldonite(3.5F, 0, 0, (fortune, drops) -> {}),
        olivine(3, 0, 0, (fortune, drops) -> {
            drops.add(new ItemStack(Miscellaneous.olivine.instance, 1 + GtUtil.RANDOM.nextInt(1 + fortune)));
        }),
        sodalite(3, 0, 0, (fortune, drops) -> {
            drops.add(new ItemStack(Dusts.sodalite.instance, 6 + 3 * GtUtil.RANDOM.nextInt(1 + fortune)));
            if (GtUtil.RANDOM.nextInt(Math.max(1, 4 / (fortune + 1))) == 0)
                drops.add(new ItemStack(Dusts.aluminium.instance));
        }),
        tetrahedrite(3, 0, 0, (fortune, drops) -> {}),
        cassiterite(3, 0, 0, (fortune, drops) -> {});
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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Block block) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = block;
        }

        public Block getInstance() {
            return this.instance;
        }
    }

    public enum Ingots {
        aluminium("Al"),
        antimony("Sb"),
        battery_alloy("Pb4Sb1"),
        brass("ZnCu3"),
        chrome("Cr"),
        electrum("AgAu"),
        hot_tungsten_steel(null),
        invar("Fe2Ni"),
        iridium("Ir"),
        iridium_alloy(null),
        magnalium("MgAl2"),
        nickel("Ni"),
        osmium("Os"),
        platinum("Pt"),
        plutonium("Pu", true),
        soldering_iron_alloy("Sn9Sb1"),
        thorium("Th", true),
        titanium("Ti"),
        tungsten("W"),
        tungsten_steel("Vacuum Hardened"),
        zinc("Zn");

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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Nuggets {
        aluminium(Ingots.aluminium.description),
        antimony(Ingots.antimony.description),
        brass(Ingots.brass.description),
        chrome(Ingots.chrome.description),
        copper("Cu"),
        electrum(Ingots.electrum.description),
        invar(Ingots.invar.description),
        lead("Pg"),
        nickel(Ingots.nickel.description),
        osmium(Ingots.osmium.description),
        platinum(Ingots.platinum.description),
        silver(Plates.silver.description),
        steel("Fe"),
        tin("Sn"),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        zinc(Ingots.zinc.description);

        private Item instance;
        public final String description;

        Nuggets(String description) {
            this.description = description;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Plates {
        aluminium(Ingots.aluminium.description),
        battery_alloy(Ingots.battery_alloy.description),
        brass(Ingots.brass.description),
        chrome(Ingots.chrome.description),
        electrum(Ingots.electrum.description),
        invar(Ingots.invar.description),
        magnalium(Ingots.magnalium.description),
        nickel(Ingots.nickel.description),
        osmium(Ingots.osmium.description),
        platinum(Ingots.platinum.description),
        silicon("Si2"),
        silver("Ag"),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        tungsten_steel(Ingots.tungsten_steel.description),
        wood(null),
        zinc(Ingots.zinc.description);

        private Item instance;
        public final String description;

        Plates(String description) {
            this.description = description;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Rods {
        lead("Pb"),
        bronze("SnCu3"),
        tungsten_steel(Ingots.tungsten_steel.description),
        zinc(Ingots.zinc.description),
        osmium(Ingots.osmium.description),
        brass(Ingots.brass.description),
        copper("Cu"),
        tin("Sn"),
        iron("Fe"),
        steel("Fe"),
        titanium(Ingots.titanium.description),
        platinum(Ingots.platinum.description),
        invar(Ingots.invar.description),
        nickel(Ingots.nickel.description),
        chrome(Ingots.chrome.description),
        aluminium(Ingots.aluminium.description),
        gold("Au"),
        electrum(Ingots.electrum.description),
        silver("Ag"),
        iridium(Ingots.iridium.description),
        tungsten(Ingots.tungsten.description);

        private Item instance;
        public final String description;

        Rods(String description) {
            this.description = description;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Dusts {
        almandine("Al2Fe3Si3O12"),
        aluminium(Ingots.aluminium.description),
        andradite("Ca3Fe2Si3O12"),
        antimony(Ingots.antimony.description),
        basalt("(Mg2Fe2SiO4)(CaCO3)3(SiO2)8C4"),
        bauxite("TiAl16H10O12"),
        brass(Ingots.brass.description),
        calcite("CaCO3"),
        charcoal("C"),
        chrome(Ingots.chrome.description),
        cinnabar("HgS"),
        dark_ashes("C"),
        electrum(Ingots.electrum.description),
        emerald("Be3Al2Si6O18"),
        ender_eye("BeK4N5Cl6C4S2"),
        ender_pearl("BeK4N5Cl6"),
        flint("SiO2"),
        galena("Pb3Ag3S2"),
        green_sapphire(Miscellaneous.green_sapphire.description),
        grossular("Ca3Al2Si3O12"),
        invar(Ingots.invar.description),
        lazurite("Al6Si6Ca8Na8"),
        magnesium("Mg"),
        manganese("Mn"),
        marble("Mg(CaCO3)7"),
        netherrack(null),
        nickel("Ni"),
        olivine(Miscellaneous.olivine.description),
        osmium("Os"),
        phosphorus("Ca3(PO4)2"),
        platinum("Pt"),
        plutonium(Ingots.plutonium.description, true),
        pyrite("FeS2"),
        pyrope("Al2Mg3Si3O12"),
        red_garnet(Miscellaneous.red_garnet.description),
        redrock("(Na2LiAl2Si2)((CaCO3)2SiO2)3"),
        ruby(Miscellaneous.ruby.description),
        saltpeter("KNO3"),
        sapphire(Miscellaneous.sapphire.description),
        sodalite("Al3Si3Na4Cl"),
        spessartine("Al2Mn3Si3O12"),
        sphalerite("ZnS"),
        steel("Fe"),
        thorium(Ingots.thorium.description, true),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        uranium("U", true),
        uvarovite("Ca3Cr2Si3O12"),
        wood(null),
        yellow_garnet(Miscellaneous.yellow_garnet.description),
        zinc(Ingots.zinc.description);

        private Item instance;
        public final String description;
        public final boolean hasEffect;

        Dusts(String description) {
            this(description, false);
        }

        Dusts(String description, boolean hasEffect) {
            this.description = description;
            this.hasEffect = hasEffect;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Smalldusts {
        almandine(Dusts.almandine.description),
        aluminium(Ingots.aluminium.description),
        andradite(Dusts.andradite.description),
        antimony(Ingots.antimony.description),
        basalt(Dusts.basalt.description),
        bauxite(Dusts.bauxite.description),
        brass(Ingots.brass.description),
        calcite(Dusts.calcite.description),
        charcoal(Dusts.charcoal.description),
        chrome(Ingots.chrome.description),
        cinnabar(Dusts.cinnabar.description),
        clay("Na2LiAl2Si2"),
        coal("C2"),
        dark_ashes(Dusts.dark_ashes.description),
        diamond("C128"),
        electrum(Ingots.electrum.description),
        emerald(Dusts.emerald.description),
        ender_eye(Dusts.ender_eye.description),
        ender_pearl(Dusts.ender_pearl.description),
        flint(Dusts.flint.description),
        galena(Dusts.galena.description),
        glowstone,
        green_sapphire(Dusts.green_sapphire.description),
        grossular(Dusts.grossular.description),
        gunpowder,
        invar(Ingots.invar.description),
        lazurite(Dusts.lazurite.description),
        magnesium(Dusts.magnesium.description),
        manganese(Dusts.manganese.description),
        marble(Dusts.marble.description),
        netherrack(Dusts.netherrack.description),
        nickel(Ingots.nickel.description),
        olivine(Dusts.olivine.description),
        osmium(Ingots.osmium.description),
        phosphorus(Dusts.phosphorus.description),
        platinum(Ingots.platinum.description),
        plutonium(Ingots.plutonium.description, true),
        pyrite(Dusts.pyrite.description),
        pyrope(Dusts.pyrope.description),
        red_garnet(Dusts.red_garnet.description),
        redrock(Dusts.redrock.description),
        redstone,
        ruby(Dusts.ruby.description),
        saltpeter(Dusts.saltpeter.description),
        sapphire(Dusts.sapphire.description),
        sodalite(Dusts.sodalite.description),
        spessartine(Dusts.spessartine.description),
        sphalerite(Dusts.sphalerite.description),
        steel(Dusts.steel.description),
        thorium(Ingots.thorium.description, true),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        uranium(Dusts.uranium.description, true),
        uvarovite(Dusts.uvarovite.description),
        wood(Dusts.wood.description),
        yellow_garnet(Dusts.yellow_garnet.description),
        zinc(Ingots.zinc.description);

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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Upgrades {
        hv_transformer(GtUpgradeType.transformer, 2, 3, "Higher tier of the transformer upgrade", "craftingHVTUpgrade", (stack, machine) -> machine.getTier() < 5, (stack, machine, player) -> machine.setSinkTier(Math.min(machine.getSinkTier()+stack.getCount(), 5))),
        lithium_battery(GtUpgradeType.battery, 4, 1, "Adds 100000 EU to the energy capacity", "craftingLiBattery", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000*stack.getCount()))),
        energy_crystal(GtUpgradeType.battery, 4, 2, "Adds 100000 EU to the energy capacity", "crafting100kEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000*stack.getCount()))),
        lapotron_crystal(GtUpgradeType.battery, 4, 3, "Adds 1 Million EU to the energy capacity", "crafting1kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(1000000*stack.getCount()))),
        energy_orb(GtUpgradeType.battery, 4, 4, "Adds 10 Million EU to the energy capacity", "crafting10kkEUStore", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(1000000*stack.getCount()))),
        quantum_chest(GtUpgradeType.quantum_chest, 1, 0, "Upgrades a Digital Chest to a Quantum chest", "craftingQuantumChestUpgrade"),
        machine_lock(GtUpgradeType.lock, 1, 0, "Makes a machine private for the one, who applies this upgrade", "craftingLock", (stack, machine, player) -> {
            GameProfile owner = machine.getOwner();
            if (owner != null && !player.getGameProfile().equals(owner)) {
                if (!player.world.isRemote) player.sendMessage(new TextComponentString("You can't lock a machine you don't own!"));
                return true;
            }
            return false;
        }, (stack, machine, player) -> {
            if (player != null && !machine.isPrivate()) machine.setPrivate(true, player.getGameProfile());
        }),
        steam_upgrade(GtUpgradeType.steam, 1, 1,"Lets Machines consume Steam at 2mb per EU (lossless)", "craftingSteamUpgrade", (stack, machine, player) -> {
            if (!machine.hasSteamTank()) machine.addSteamTank();
        }),

        steam_tank(GtUpgradeType.steam, 4, 1, "Increases Steam Capacity by 64 Buckets", "craftingSteamTank", (stack, machine) ->  machine.hasSteamTank(), (stack, machine, player) -> {
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

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, null, GtUtil.alwaysTrue(), (stack, machine, player) -> false, onUpdate);
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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Covers {
        active_detector("Emits redstone if the machine has work", "craftingWorkDetector"),
        conveyor("Moves items around", "craftingConveyor"),
        crafting("A workbench on a cover", "craftingWorkBench"),
        drain("Collects liquids and rain", "craftingDrain"),
        eu_meter("Outputs redstone depending on stored energy", "craftingEnergyMeter"),
        item_meter("Outputs redstone depending on stored items", "craftingItemMeter"),
        item_valve("Moves items and liquids at once!", "craftingItemValve"),
        liquid_meter("Outputs redstone depending on stored liquids", "craftingLiquidMeter"),
        machine_controller("This can control machines with redstone", "craftingWorkController"),
        pump_module("Moves liquids around", "craftingPump"),
        redstone_conductor("Throughputs redstone to the cover facing", "craftingRedstoneConductor"),
        redstone_signalizer("Applies a constant redstone signal to a machine", "craftingRedstoneSignalizer"),
        screen("Displays things", "craftingMonitorTier02"),
        solar_panel("Makes energy from the Sun", "craftingSolarPanel"),
        solar_panel_hv("Makes energy from the Sun at 512EU/t", "craftingSolarPanelHV"),
        solar_panel_lv("Makes energy from the Sun at 8EU/t", "craftingSolarPanelLV"),
        solar_panel_mv("Makes energy from the Sun at 64EU/t", "craftingSolarPanelMV");

        private Item instance;
        public final String description;
        public final String oreDict;

        Covers(String description, String oreDict) {
            this.description = description;
            this.oreDict = oreDict;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Components {
        energy_flow_circuit("energy_only", "Manages large amounts of energy", "craftingCircuitTier07"),
        data_control_circuit("redstone_only", "Basic computer processor", "craftingCircuitTier03"),
        superconductor("Conducts Energy losslessly", "craftingSuperconductor"),
        data_storage_circuit("Stores Data", "craftingCircuitTier05"),
        lithium_battery(false, "craftingLiBattery"),
        coil_kanthal("Standard Heating Coil", "craftingHeatingCoilTier01"),
        coil_nichrome("Advanced Heating Coil", "craftingHeatingCoilTier02"),
        coil_cupronickel("Cheap and simple Heating Coil", "craftingHeatingCoilTier00"),
        hull_aluminium("Machine Block", "craftingRawMachineTier01"),
        hull_brass("Cheap Machine Block", "craftingRawMachineTier00"),
        hull_bronze("Cheap Machine Block", "craftingRawMachineTier00"),
        hull_iron("Machine Block", "craftingRawMachineTier01"),
        hull_steel("Advanced Machine Block", "craftingRawMachineTier02"),
        hull_titanium("Very Advanced Machine Block", "craftingRawMachineTier03"),
        hull_tungsten_steel("Very Advanced Machine Block", "craftingRawMachineTier03"),
        circuit_board_basic("Just a simple Circuit Plate", "craftingCircuitBoardTier02"),
        circuit_board_advanced("Standard Circuit Plate", "craftingCircuitBoardTier04"),
        circuit_board_processor("Highly advanced Circuit Plate", "craftingCircuitBoardTier06"),
        turbine_blade_bronze("Heavy Turbine Blade", "craftingTurbineBladeBronze"),
        turbine_blade_carbon("Ultralight Turbine Blade", "craftingTurbineBladeCarbon"),
        turbine_blade_magnalium("Light Turbine Blade", "craftingTurbineBladeMagnalium"),
        turbine_blade_steel("Standard Turbine Blade", "craftingTurbineBladeSteel"),
        turbine_blade_tungsten_steel("Durable Turbine Blade", "craftingTurbineBladeTungstenSteel"),
        gear_iron("An Iron Gear", "gearIron"),
        gear_bronze("A Bronze Gear", "gearBronze"),
        gear_steel("A Steel Gear", "gearSteel"),
        gear_titanium("A Titanium Gear", "gearTitanium"),
        gear_tungsten_steel("A Tungstensteel Gear", "gearTungstenSteel"),
        gear_iridium("An Iridium Gear", "gearIridium"),
        turbine_rotor_bronze("Turbine Efficiency:  60%", false),
        turbine_rotor_steel("Turbine Efficiency:  80%", false),
        turbine_rotor_magnalium("Turbine Efficiency: 100%", false),
        turbine_rotor_tungsten_steel("Turbine Efficiency:  90%", false),
        turbine_rotor_carbon("Turbine Efficiency: 125%", false),
        lava_filter("Filters Lava in Thermal Boilers", false),
        diamond_sawblade("Caution! This is very sharp.", "craftingDiamondBlade"),
        diamond_grinder("Fancy Grinding Head", "craftingGrinder"),
        wolframium_grinder("Regular Grinding Head", "craftingGrinder"),
        machine_parts("Random Machine Parts", "craftingMachineParts"),
        advanced_circuit_parts("Part of advanced Circuitry", "craftingCircuitPartsTier04"),
        duct_tape("If you can't fix it with this, use more of it!", "craftingDuctTape"),
        data_orb(false, "craftingCircuitTier08");

        private Item instance;
        public final String description;
        public final String oreDict;
        public final boolean autoInit;
        public final String coverName;

        Components(boolean autoInit) {
            this(autoInit, null);
        }

        Components(String description, boolean autoInit) {
            this(description, autoInit, null);
        }

        Components(boolean autoInit, String oreDict) {
            this(null, autoInit, oreDict);
        }

        Components(String description, String oreDict) {
            this(description, true, oreDict);
        }

        Components(String coverName, String description, String oreDict) {
            this.description = description;
            this.autoInit = false;
            this.oreDict = oreDict;
            this.coverName = coverName;
        }

        Components(String description, boolean autoInit, String oreDict) {
            this.description = description;
            this.autoInit = autoInit;
            this.oreDict = oreDict;
            this.coverName = null;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Crafting {
        mortar_iron,
        mortar_flint;

        private Item instance;

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Tools {
        crowbar("craftingToolCrowbar"),
        debug_scanner,
        drill_advanced("craftingToolLargeDrill"),
        rock_cutter,
        rubber_hammer("craftingToolSoftHammer"),
        saw_advanced,
        scanner,
        screwdriver("craftingToolScrewdriver"),
        soldering_tool("craftingToolSolderingIron"),
        tesla_staff,
        wrench_advanced,
        destructorpack,
        lapotronic_energy_orb("crafting10kkEUStore"),
        sonictron_portable,
        spray_bug,
        spray_ice("molecule_1n"),
        spray_hardener,
        spray_foam,
        spray_pepper,
        spray_hydration;

        public final String oreDict;
        private Item instance;

        Tools() {
            this(null);
        }

        Tools(String oreDict) {
            this.oreDict = oreDict;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }
    
    public enum ColorSprays {
        white,
        orange,
        magenta,
        light_blue,
        yellow,
        lime,
        pink,
        gray,
        silver,
        cyan,
        purple,
        blue,
        brown,
        green,
        red,
        black;

        private Item instance;

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Wrenches {
        iron(128, 4),
        bronze(256, 6),
        steel(512, 8),
        tungsten_steel(5120, 10);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Wrenches(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum JackHammers {
        bronze(50, 10000, 1, 50, 7.5F, false),
        steel(100, 10000, 1, 50, 15F, false),
        diamond(250, 100000, 2, 100, 45F, true);

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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Hammers {
        iron(128, 4),
        bronze(256, 6),
        steel(512, 8),
        tungsten_steel(5120, 10);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Hammers(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Saws {
        iron(128, 3, 2),
        bronze(256, 4, 3),
        steel(1280, 6, 4),
        tungsten_steel(5120, 8, 5);

        private Item instance;
        public final int durability;
        public final int efficiency;
        public final int entityDamage;

        Saws(int durability, int efficiency, int entityDamage) {
            this.durability = durability;
            this.efficiency = efficiency;
            this.entityDamage = entityDamage;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum SolderingMetals {
        lead(10),
        tin(50);

        private Item instance;
        public final int durability;

        SolderingMetals(int durability) {
            this.durability = durability;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Files {
        iron(128, 2),
        bronze(256, 3),
        steel(1280, 3),
        tungsten_steel(5120, 4);

        private Item instance;
        public final int durability;
        public final int entityDamage;

        Files(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    /**
     * Items for fluid-less cells and the ice cell
     */
    public enum Cells {
        ice("H2O"),
        nitrocarbon("NC"),
        sodium_sulfide("NaS"),
        sulfur("S"),
        sulfuric_acid("H2SO4");

        private Item instance;
        public final String description;

        Cells(String description) {
            this.description = description;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum NuclearCoolantPacks {
        coolant_nak_60k(60000, "crafting60kCoolantStore"),
        coolant_nak_180k(180000, "crafting180kCoolantStore"),
        coolant_nak_360k(360000, "crafting360kCoolantStore"),
        coolant_helium_60k(60000, "crafting60kCoolantStore"),
        coolant_helium_180k(180000, "crafting180kCoolantStore"),
        coolant_helium_360k(360000, "crafting360kCoolantStore");

        private Item instance;
        public final int heatStorage;
        public final String oreDict;

        NuclearCoolantPacks(int heatStorage, String oreDict) {
            this.heatStorage = heatStorage;
            this.oreDict = oreDict;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum NuclearFuelRods {
        thorium(1, 25000, 0.25F, 1, 0.25F),
        thorium_dual(2, 25000, 0.25F, 1, 0.25F),
        thorium_quad(4, 25000, 0.25F, 1, 0.25F),
        plutonium(1, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_uranium")),
        plutonium_dual(2, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_dual_uranium")),
        plutonium_quad(4, 20000, 2, 2, 2, IC2Items.getItem("nuclear", "depleted_quad_uranium"));

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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Armor {
        cloaking_device(EntityEquipmentSlot.CHEST, 10000000, 8192, 4, 0, 0, false, ArmorPerk.invisibility_field),
        lapotronpack(EntityEquipmentSlot.CHEST, 10000000, 8192, 4, 0, 0, true, "crafting10kkEUPack"),
        lithium_batpack(EntityEquipmentSlot.CHEST, 600000, 128, 1, 0, 0, true, "crafting600kEUPack"),
        ultimate_cheat_armor(EntityEquipmentSlot.CHEST, 1000000000, Integer.MAX_VALUE, 1, 10, 100, true, EnumSet.allOf(ArmorPerk.class).toArray(new ArmorPerk[0])),
        light_helmet(EntityEquipmentSlot.HEAD, 10000, 32, 1, 0, 0, false, ArmorPerk.lamp, ArmorPerk.solarpanel);

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

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Miscellaneous {
        greg_coin("I put the G in 'Gregoriette'"),
        credit_copper("0.125 Credits"),
        credit_silver("8 Credits"),
        credit_gold("64 Credits"),
        credit_diamond("512 Credits"),
        ruby("Al206Cr", "gemRuby"),
        sapphire("Al206", "gemSapphire"),
        green_sapphire("Al206", "gemGreenSapphire"),
        olivine("Mg2Fe2SiO4", "gemOlivine"),
        lazurite_chunk("(Al6Si6Ca8Na8)8", "chunkLazurite"),
        red_garnet("(Al2Mg3Si3O12)3(Al2Fe3Si3O12)5(Al2Mn3Si3O12)8", "gemGarnetRed"),
        yellow_garnet("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3", "gemGarnetYellow"),
        indigo_blossom,
        indigo_dye(null, "dyeBlue"),
        flour(null, "dustWheat"),
        spray_can_empty("Used for making Sprays and storing Colors", "craftingSprayCan");

        private Item instance;
        public final String description;
        public final String oreDict;
        public final boolean autoInit;

        Miscellaneous() {
            this(null);
        }

        Miscellaneous(String description) {
            this(description, null, true);
        }

        Miscellaneous(String description, String oreDict) {
            this(description, oreDict, true);
        }

        Miscellaneous(String description, String oreDict, boolean autoInit) {
            this.description = description;
            this.oreDict = oreDict;
            this.autoInit = autoInit;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Crops {
        //Most crops have been already implemented by ic2, so don't be surprised why many are missing
        indigo("Eloraam", new ItemStack(Miscellaneous.indigo_blossom.instance), new ItemStack(Miscellaneous.indigo_blossom.instance, 4), 4, 4, 1, 0, 2, 1, 1, 0, 4, 0, true, "Flower", "Color", "Ingredient"),
        tine("Gregorius Techneticies", new ItemStack(Nuggets.tin.instance), ItemStack.EMPTY, 3, 3, 2, 0, 5, 2, 0, 3, 0, 0, false, "Shiny", "Metal", "Pine", "Tin", "Bush"),
        coppon("Mr. Brain", new ItemStack(Nuggets.copper.instance), ItemStack.EMPTY, 3, 3, 2, 0, 6, 2, 0, 1, 1, 1, false, "Shiny", "Metal", "Cotton", "Copper", "Bush"),
        argentia("Eloraam", new ItemStack(Nuggets.silver.instance), ItemStack.EMPTY, 4, 4, 3, 0, 7, 2, 0, 1, 0, 0, false, "Shiny", "Metal", "Silver", "Reed"),
        plumbilia("KingLemming", new ItemStack(Nuggets.lead.instance), ItemStack.EMPTY, 4, 4, 3, 0, 6, 2, 0, 3, 1, 1, false, "Heavy", "Metal", "Lead", "Reed");

        private Item instance;
        public final String discoverer;
        public final ItemStack drop;
        public final ItemStack[] specialDrops;
        public final ItemStack baseSeed;
        public final int maxSize;
        public final int harvestSize;
        public final int afterHarvestSize;
        public final int growthSpeed;
        public final int tier;
        public final int statChemistry;
        public final int statConsumable;
        public final int statDefensive;
        public final int statColorful;
        public final int statWeed;
        public final boolean hasItem;
        public final String[] attributes;

        Crops(String discoverer, ItemStack drop, ItemStack baseSeed, int maxSize, int harvestSize, int afterHarvestSize, int growthSpeed, int tier, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, boolean hasItem, String... attributes) {
            this(discoverer, drop, new ItemStack[0], baseSeed, maxSize, harvestSize, afterHarvestSize, growthSpeed, tier, statChemistry, statConsumable, statDefensive, statColorful, statWeed, hasItem, attributes);
        }

        Crops(String discoverer, ItemStack drop, ItemStack[] specialDrops, ItemStack baseSeed, int maxSize, int harvestSize, int afterHarvestSize, int growthSpeed, int tier, int statChemistry, int statConsumable, int statDefensive, int statColorful, int statWeed, boolean hasItem, String... attributes) {
            this.discoverer = discoverer;
            this.drop = drop;
            this.specialDrops = specialDrops;
            this.baseSeed = baseSeed;
            this.maxSize = maxSize;
            this.harvestSize = harvestSize;
            this.afterHarvestSize = afterHarvestSize;
            this.growthSpeed = growthSpeed;
            this.tier = tier;
            this.statChemistry = statChemistry;
            this.statConsumable = statConsumable;
            this.statDefensive = statDefensive;
            this.statColorful = statColorful;
            this.statWeed = statWeed;
            this.hasItem = hasItem;
            this.attributes = attributes;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Books {
        manual("Gregorius Techneticies", 11),
        manual2("Gregorius Techneticies", 9),
        machine_safety("Gregorius Techneticies", 7),
        cover_up("Gregorius Techneticies", 5),
        greg_os_manual("Gregorius Techneticies", 8),
        greg_os_manual2("Gregorius Techneticies", 11),
        upgrade_dictionary("Gregorius Techneticies", 21),
        crop_dictionary("Mr. Kenny", 32),
        energy_systems("Gregorius Techneticies", 7),
        microwave_oven_manual("Kitchen Industries", 6),
        turbine_manual("Gregorius Techneticies", 19),
        thermal_boiler_manual("Gregorius Techneticies", 16);

        public final String author;
        public final int pages;
        private ItemStack instance;

        Books(String author, int pages) {
            this.author = author;
            this.pages = pages;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(ItemStack item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemStack getInstance() {
            return this.instance;
        }
    }
}