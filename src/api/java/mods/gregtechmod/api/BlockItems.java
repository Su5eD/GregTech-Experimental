package mods.gregtechmod.api;

import com.mojang.authlib.GameProfile;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.ArmorPerk;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.TriFunction;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidTank;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.EnumSet;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
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
        iridium_reinforced_tungstensteel(200, 400),
        reinforced_machine_casing(3, 60),
        standard_machine_casing(3, 30),
        tungstensteel(100, 300);

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

    public enum Ingots {
        aluminium("Al"),
        antimony("Sb"),
        battery_alloy("Pb4Sb1"),
        brass("ZnCu3"),
        chrome("Cr"),
        electrum("AgAu"),
        hot_tungstensteel(null),
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
        tungstensteel("Vacuum Hardened"),
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
        electrum(Ingots.electrum.description),
        invar(Ingots.invar.description),
        nickel(Ingots.nickel.description),
        osmium(Ingots.osmium.description),
        platinum(Ingots.platinum.description),
        silver(Plates.silver.description),
        steel("Fe"),
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
        tungstensteel(Ingots.tungstensteel.description),
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
        tungstensteel(Ingots.tungstensteel.description),
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
        wood_pulp(null),
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
        dark_ashes(Dusts.dark_ashes.description),
        diamond("C128"),
        electrum(Ingots.electrum.description),
        emerald(Dusts.emerald.description),
        ender_eye(Dusts.ender_eye.description),
        ender_pearl(Dusts.ender_pearl.description),
        flint(Dusts.flint.description),
        galena(Dusts.galena.description),
        green_sapphire(Dusts.green_sapphire.description),
        grossular(Dusts.grossular.description),
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
        wood_pulp(Dusts.wood_pulp.description),
        yellow_garnet(Dusts.yellow_garnet.description),
        zinc(Ingots.zinc.description);

        private Item instance;
        public final String description;
        public final boolean hasEffect;

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
        hv_transformer(GtUpgradeType.transformer, 2, 3, "Higher tier of the transformer upgrade", (stack, machine) -> machine.getTier() < 5, (stack, machine, player) -> machine.setSinkTier(Math.min(machine.getSinkTier()+stack.getCount(), 5))),
        lithium_battery(GtUpgradeType.battery, 4, 1, "Adds 100000 EU to the energy capacity", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000*stack.getCount()))),
        energy_crystal(GtUpgradeType.battery, 4, 2, "Adds 100000 EU to the energy capacity", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(100000*stack.getCount()))),
        lapotron_crystal(GtUpgradeType.battery, 4, 3, "Adds 1 Million EU to the energy capacity", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(1000000*stack.getCount()))),
        energy_orb(GtUpgradeType.battery, 4, 4, "Adds 10 Million EU to the energy capacity", (stack, machine, player) -> machine.setEUcapacity(machine.getEUCapacity()+(1000000*stack.getCount()))),
        quantum_chest(GtUpgradeType.quantum_chest, 1, 0, "Upgrades a Digital Chest to a Quantum chest"),
        machine_lock(GtUpgradeType.lock, 1, 0, "Makes a machine private for the one, who applies this upgrade", (stack, machine, player) -> {
            GameProfile owner = machine.getOwner();
            if (owner != null && !player.getGameProfile().equals(owner)) {
                if (!player.world.isRemote) player.sendMessage(new TextComponentString("You can't lock a machine you don't own!"));
                return true;
            }
            return false;
        }, (stack, machine, player) -> {
            if (player != null && !machine.isPrivate()) machine.setPrivate(true, player.getGameProfile());
        }),
        steam_upgrade(GtUpgradeType.steam, 1, 1,"Lets Machines consume Steam at 2mb per EU (lossless)", (stack, machine, player) -> {
            if (!machine.hasSteamTank()) machine.addSteamTank();
        }),

        steam_tank(GtUpgradeType.steam, 4, 1, "Increases Steam Capacity by 64 Buckets", (stack, machine) ->  machine.hasSteamTank(), (stack, machine, player) -> {
            FluidTank steamTank = machine.getSteamTank();
            if (steamTank != null) steamTank.setCapacity(2000 + stack.getCount() * 64000);
        });

        private IGtUpgradeItem instance;
        public final GtUpgradeType type;
        public final int maxCount;
        public final int requiredTier;
        public final String description;
        public BiPredicate<ItemStack, IUpgradableMachine> condition;
        public final TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert;
        public final TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate;

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description) {
            this(type, maxCount, requiredTier, description, GtUtil.alwaysTrue(), (stack, machine, player) -> false, (stack, machine, player) -> {});
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, GtUtil.alwaysTrue(), (stack, machine, player) -> false, onUpdate);
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, BiPredicate<ItemStack, IUpgradableMachine> condition, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, condition, (stack, machine, player) -> false, onUpdate);
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this(type, maxCount, requiredTier, description, GtUtil.alwaysTrue(), onInsert, onUpdate);
        }

        Upgrades(GtUpgradeType type, int maxCount, int requiredTier, String description, BiPredicate<ItemStack, IUpgradableMachine> condition, TriFunction<ItemStack, IUpgradableMachine, EntityPlayer, Boolean> onInsert, TriConsumer<ItemStack, IUpgradableMachine, EntityPlayer> onUpdate) {
            this.type = type;
            this.maxCount = maxCount;
            this.requiredTier = requiredTier;
            this.description = description;
            this.condition = condition;
            this.onInsert = onInsert;
            this.onUpdate = onUpdate;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        public void setInstance(IGtUpgradeItem item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public IGtUpgradeItem getInstance() {
            return this.instance;
        }
    }

    public enum Covers {
        active_detector("Emits redstone if the machine has work"),
        conveyor("Moves items around"),
        crafting("A workbench on a cover"),
        drain("Collects liquids and rain"),
        eu_meter("Outputs redstone depending on stored energy"),
        item_meter("Outputs redstone depending on stored items"),
        item_valve("Moves items and liquids at once!"),
        liquid_meter("Outputs redstone depending on stored liquids"),
        machine_controller("This can control machines with redstone"),
        pump_module("Moves liquids around"),
        redstone_conductor("Throughputs redstone to the cover facing"),
        redstone_signalizer("Applies a constant redstone signal to a machine"),
        screen("Displays things"),
        solar_panel("Makes energy from the Sun"),
        solar_panel_hv("Makes energy from the Sun at 512EU/t"),
        solar_panel_lv("Makes energy from the Sun at 8EU/t"),
        solar_panel_mv("Makes energy from the Sun at 64EU/t");

        private Item instance;
        public final String description;

        Covers(String description) {
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

    public enum Components {
        data_control_circuit("redstone_only", "Basic computer processor"),
        energy_flow_circuit("energy_only", "Manages large amounts of energy"),
        lithium_battery,
        coil_kanthal("Standard Heating Coil"),
        coil_nichrome("Advanced Heating Coil"),
        coil_cupronickel("Cheap and simple Heating Coil"),
        hull_aluminium("Machine Block"),
        hull_brass("Cheap Machine Block"),
        hull_bronze("Cheap Machine Block"),
        hull_iron("Machine Block"),
        hull_steel("Advanced Machine Block"),
        hull_titanium("Very Advanced Machine Block"),
        hull_tungstensteel("Very Advanced Machine Block"),
        circuit_board_basic("Just a simple Circuit Plate"),
        circuit_board_advanced("Standard Circuit Plate"),
        circuit_board_processor("Highly advanced Circuit Plate"),
        turbine_blade_bronze("Heavy Turbine Blade"),
        turbine_blade_carbon("Ultralight Turbine Blade"),
        turbine_blade_magnalium("Light Turbine Blade"),
        turbine_blade_steel("Standard Turbine Blade"),
        turbine_blade_tungstensteel("Durable Turbine Blade"),
        gear_iron("A refined Iron Gear"), //TODO: Refined Iron is obsolete!
        gear_bronze("A Bronze Gear"),
        gear_steel("A Steel Gear"),
        gear_titanium("A Titanium Gear"),
        gear_tungstensteel("A Tungstensteel Gear"),
        gear_iridium("An Iridium Gear"),
        turbine_rotor_bronze("Turbine Efficiency:  60%", false),
        turbine_rotor_steel("Turbine Efficiency:  80%", false),
        turbine_rotor_magnalium("Turbine Efficiency: 100%", false),
        turbine_rotor_tungstensteel("Turbine Efficiency:  90%", false),
        turbine_rotor_carbon("Turbine Efficiency: 125%", false),
        lava_filter("Filters Lava in Thermal Boilers", false),
        superconductor("Conducts Energy losslessly"),
        data_storage_circuit("Stores Data"),
        diamond_sawblade("Caution! This is very sharp."),
        diamond_grinder("Fancy Grinding Head"),
        wolframium_grinder("Regular Grinding Head"),
        machine_parts("Random Machine Parts"),
        advanced_circuit_parts("Part of advanced Circuitry");

        private Item instance;
        public final String description;
        public final boolean autoInit;
        public final String coverName;

        Components() {
            this(null);
        }

        Components(String description) {
            this(description, true);
        }

        Components(String coverName, String description) {
            this.description = description;
            this.autoInit = false;
            this.coverName = coverName;
        }

        Components(String description, boolean autoInit) {
            this.description = description;
            this.autoInit = autoInit;
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
        crowbar,
        debug_scanner,
        drill_advanced,
        rock_cutter,
        rubber_hammer,
        saw_advanced,
        scanner,
        screwdriver,
        soldering_tool,
        tesla_staff,
        wrench_advanced;

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
        iron(128),
        bronze(256),
        steel(1280),
        tungstensteel(5120);

        private Item instance;
        public final int durability;

        Wrenches(int durability) {
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

    public enum JackHammers {
        bronze(50, 10000, 1, 50, 7.5F),
        steel(100, 10000, 1, 100, 15F),
        diamond(250, 100000, 2, 500, 45F);

        private Item instance;
        public final int operationEnergyCost;
        public final int maxCharge;
        public final int tier;
        public final int transferLimit;
        public final float efficiency;

        JackHammers(int operationEnergyCost, int maxCharge, int tier, int transferLimit, float efficiency) {
            this.operationEnergyCost = operationEnergyCost;
            this.maxCharge = maxCharge;
            this.tier = tier;
            this.transferLimit = transferLimit;
            this.efficiency = efficiency;
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
        iron(128, 4, Item.ToolMaterial.IRON),
        bronze(256, 6, Item.ToolMaterial.IRON),
        steel(512, 8, Item.ToolMaterial.IRON),
        tungstensteel(5120, 10, Item.ToolMaterial.IRON);

        private Item instance;
        public final int durability;
        public final int entityDamage;
        public final Item.ToolMaterial toolMaterial;

        Hammers(int durability, int entityDamage, Item.ToolMaterial toolMaterial) {
            this.durability = durability;
            this.entityDamage = entityDamage;
            this.toolMaterial = toolMaterial;
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
        iron(128, 3, 2, Item.ToolMaterial.IRON),
        bronze(256, 4, 3, Item.ToolMaterial.IRON),
        steel(1280, 6, 4, Item.ToolMaterial.IRON),
        tungstensteel(5120, 8, 5, Item.ToolMaterial.DIAMOND);

        private Item instance;
        public final int durability;
        public final int efficiency;
        public final int entityDamage;
        public final Item.ToolMaterial toolMaterial;

        Saws(int durability, int efficiency, int entityDamage, Item.ToolMaterial toolMaterial) {
            this.durability = durability;
            this.efficiency = efficiency;
            this.entityDamage = entityDamage;
            this.toolMaterial = toolMaterial;
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
        tungstensteel(5120, 4);

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
        coolant_nak_60k(60000),
        coolant_nak_180k(180000),
        coolant_nak_360k(360000),
        coolant_helium_60k(60000),
        coolant_helium_180k(180000),
        coolant_helium_360k(360000);

        private Item instance;
        public final int heatStorage;

        NuclearCoolantPacks(int heatStorage) {
            this.heatStorage = heatStorage;
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
        cloaking_device(EntityEquipmentSlot.CHEST, 100000000, 8192, 5, 0, 0, false, ArmorPerk.invisibility_field),
        lapotronpack(EntityEquipmentSlot.CHEST, 100000000, 8192, 5, 0, 0, true),
        lithium_batpack(EntityEquipmentSlot.CHEST, 600000, 128, 1, 0, 0, true),
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
        public final ArmorPerk[] perks;

        Armor(EntityEquipmentSlot slot, int maxCharge, int transferLimit, int tier, int damageEnergyCost, double absorbtionPercentage, boolean chargeProvider, ArmorPerk... perks) {
            this.slot = slot;
            this.maxCharge = maxCharge;
            this.transferLimit = transferLimit;
            this.tier = tier;
            this.damageEnergyCost = damageEnergyCost;
            this.absorbtionDamage = absorbtionPercentage;
            this.chargeProvider = chargeProvider;
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
        greg_coin("A minimalist GregTech logo on a coin"), //TODO: Change description
        credit_copper("0.125 Credits"),
        credit_silver("8 Credits"),
        credit_gold("64 Credits"),
        credit_diamond("512 Credits"),
        ruby("Al206Cr"),
        sapphire("Al206"),
        green_sapphire("Al206"),
        olivine("Mg2Fe2SiO4"),
        lazurite_chunk("(Al6Si6Ca8Na8)8"),
        red_garnet("(Al2Mg3Si3O12)3(Al2Fe3Si3O12)5(Al2Mn3Si3O12)8"),
        yellow_garnet("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
        duct_tape("If you can't fix it with this, use more of it!"),
        indigo_blossom,
        indigo_dye,
        flour,
        destructorpack("Mobile Trash Bin", false),
        lapotronic_energy_orb(false);

        private Item instance;
        public final String description;
        public final boolean autoInit;

        Miscellaneous() {
            this(null);
        }

        Miscellaneous(boolean autoInit) {
            this(null, autoInit);
        }

        Miscellaneous(String description) {
            this(description, true);
        }

        Miscellaneous(String description, boolean autoInit) {
            this.description = description;
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
}
