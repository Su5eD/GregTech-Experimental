package mods.gregtechmod.init;

import com.mojang.authlib.GameProfile;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.machine.IUpgradableMachine;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IGtUpgradeItem;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.objects.blocks.BlockBase;
import mods.gregtechmod.objects.blocks.ConnectedBlock;
import mods.gregtechmod.objects.items.ItemSensorCard;
import mods.gregtechmod.objects.items.ItemSensorKit;
import mods.gregtechmod.objects.items.ItemSolderingMetal;
import mods.gregtechmod.objects.items.base.*;
import mods.gregtechmod.objects.items.components.ItemLithiumBattery;
import mods.gregtechmod.objects.items.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public class BlockItemLoader {
    static final List<Block> BLOCKS = new LinkedList<>();
    static final List<Item> ITEMS = new LinkedList<>();

    private static Item registerItem(Item item) {
        ModContainer container;

        if ((container = Loader.instance().activeModContainer()) != null && !container.getModId().equals(GregtechMod.MODID)) throw new IllegalAccessError("only gregtech can call this");
        else if (ITEMS.contains(item)) throw new IllegalStateException("duplicate item: "+item);

        ITEMS.add(item);
        return item;
    }

    private static Block registerBlock(Block block) {
        ModContainer container;

        if ((container = Loader.instance().activeModContainer()) != null && !container.getModId().equals(GregtechMod.MODID)) throw new IllegalAccessError("only gregtech can call this");
        else if (BLOCKS.contains(block)) throw new IllegalStateException("duplicate block: "+block);

        BLOCKS.add(block);
        return block;
    }

    static void init() {
        initBlocks();
        initIngots();
        initNuggets();
        initPlates();
        initRods();
        initDusts();
        initSmallDusts();
        initCells();
        initCovers();
        initTools();
        initComponents();
        initNuclearComponents();
        initSpecials();
        initUpgrades();
    }

    private static void initBlocks() {
        for (Blocks type : Blocks.values()) {
            Block block = new BlockBase("block_"+type.name(), Material.IRON, type.hardness, type.resistance);
            type.setInstance(registerBlock(block));
            registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
        for (ConnectedBlocks type : ConnectedBlocks.values()) {
            Block block = new ConnectedBlock("block_"+type.name(), Material.IRON, type.hardness, type.resistance);
            type.setInstance(registerBlock(block));
            registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    private static void initIngots() {
        for (Ingots type : Ingots.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "ingot", type.hasEffect)));
        }
    }

    private static void initNuggets() {
        for (Nuggets type : Nuggets.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "nugget")));
        }
    }

    private static void initPlates() {
        for (Plates type : Plates.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "plate")));
        }
    }

    private static void initRods() {
        for (Rods type : Rods.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "rod")));
        }
    }

    private static void initDusts() {
        for (Dusts type : Dusts.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "dust", type.hasEffect)));
        }
    }

    private static void initSmallDusts() {
        for (Smalldusts type : Smalldusts.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "smalldust", type.hasEffect)));
        }
    }

    private static void initUpgrades() {
        for (Upgrades upgrade : Upgrades.values()) {
            ItemUpgrade item = new ItemUpgrade(upgrade.name(), upgrade.description, upgrade.type, upgrade.maxCount, upgrade.requiredTier, upgrade.condition, upgrade.onInsert, upgrade.onUpdate);
            registerItem(item);
            upgrade.setInstance(item);
        }
    }

    private static void initCovers() {
        for (Covers type : Covers.values()) {
            ItemCover item = new ItemCover(type.name(), type.description);
            registerItem(item);
            type.setInstance(item);
        }
    }

    private static void initTools() {
        Tools.drill_advanced.setInstance(registerItem(new ItemDrillAdvanced()));
        Tools.saw_advanced.setInstance(registerItem(new ItemSawAdvanced()));
        Tools.wrench_advanced.setInstance(registerItem(new ItemWrenchAdvanced()));
        Tools.crowbar.setInstance(registerItem(new ItemCrowbar()));
        Tools.screwdriver.setInstance(registerItem(new ItemScrewdriver()));
        Tools.rock_cutter.setInstance(registerItem(new ItemRockCutter()));
        Tools.rubber_hammer.setInstance(registerItem(new ItemRubberHammer()));
        Tools.soldering_tool.setInstance(registerItem(new ItemSolderingTool()));
        Tools.tesla_staff.setInstance(registerItem(new ItemTeslaStaff()));
        Tools.scanner.setInstance(registerItem(new ItemScanner()));
        Tools.debug_scanner.setInstance(registerItem(new ItemDebugScanner()));

        for (Wrenches type : Wrenches.values()) {
            ItemWrench item = new ItemWrench(type.name(), type.durability);
            registerItem(item);
            type.setInstance(item);
        }

        for (JackHammers type : JackHammers.values()) {
            ItemJackHammer item = new ItemJackHammer(type.name(), type.operationEnergyCost, type.maxCharge, type.tier, type.transferLimit, type.efficiency);
            registerItem(item);
            type.setInstance(item);
        }

        for (Hammers type : Hammers.values()) {
            ItemHammer item = new ItemHardHammer(type.name(), type.durability, type.entityDamage, type.toolMaterial);
            registerItem(item);
            type.setInstance(item);
        }

        for (Saws type : Saws.values()) {
            ItemSaw item = new ItemSaw(type.name(), type.durability, type.efficiency, type.entityDamage, type.toolMaterial);
            registerItem(item);
            type.setInstance(item);
        }

        for (SolderingMetals type : SolderingMetals.values()) {
            ItemSolderingMetal item = new ItemSolderingMetal(type.name(), type.durability);
            registerItem(item);
            type.setInstance(item);
        }

        for (Files type : Files.values()) {
            ItemFile item = new ItemFile(type.name(), type.durability, type.entityDamage, Item.ToolMaterial.IRON);
            registerItem(item);
            type.setInstance(item);
        }
    }

    private static void initComponents() {
        Components.data_control_circuit.setInstance(registerItem(new ItemCover(Components.data_control_circuit.name(), Components.data_control_circuit.coverName, Components.data_control_circuit.description, null, "component")));
        Components.energy_flow_circuit.setInstance(registerItem(new ItemCover(Components.energy_flow_circuit.name(), Components.energy_flow_circuit.coverName, Components.energy_flow_circuit.description, null, "component")));
        Components.lithium_battery.setInstance(registerItem(new ItemLithiumBattery()));
    }

    private static void initNuclearComponents() {
        for (NuclearCoolantPacks type : NuclearCoolantPacks.values()) {
            type.setInstance(registerItem(new ItemNuclearHeatStorage(type.name(), type.heatStorage)));
        }
        for (NuclearFuelRods type : NuclearFuelRods.values()) {
            type.setInstance(registerItem(new ItemNuclearFuelRod("cell_"+type.name(), type.cells, type.duration, type.energy, type.radiation, type.heat, type.depletedStack)));
        }
    }

    private static void initSpecials() {
        Specials.greg_coin.setInstance(registerItem(new ItemBase(Specials.greg_coin.name(), "A minimalist GregTech logo on a coin")));

        if (Loader.isModLoaded("energycontrol")) {
            Specials.sensor_kit.setInstance(registerItem(new ItemSensorKit()));
            Specials.sensor_card.setInstance(registerItem(new ItemSensorCard()));
        }
    }

    private static void initCells() {
        for (Cells type : Cells.values()) {
            type.setInstance(registerItem(new ItemBase(type.name(), type.description, "cell")));
        }
    }

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
        private void setInstance(Block block) {
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
        private void setInstance(Block block) {
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
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
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
        green_sapphire("Al2O6"),
        grossular("Ca3Al2Si3O12"),
        invar(Ingots.invar.description),
        lazurite("Al6Si6Ca8Na8"),
        magnesium("Mg"),
        manganese("Mn"),
        marble("Mg(CaCO3)7"),
        netherrack(null),
        nickel("Ni"),
        olivine("Mg2Fe2SiO4"),
        osmium("Os"),
        phosphorus("Ca3(PO4)2"),
        platinum("Pt"),
        plutonium(Ingots.plutonium.description, true),
        pyrite("FeS2"),
        pyrope("Al2Mg3Si3O12"),
        red_garnet("(Al2Mg3Si3O12)3(Al2Fe3Si3O12)5(Al2Mn3Si3O12)8"),
        redrock("(Na2LiAl2Si2)((CaCO3)2SiO2)3"),
        ruby("Al2O6Cr"),
        saltpeter("KNO3"),
        sapphire("Al2O6"), //TODO: Change to jewels' description when it's added
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
        yellow_garnet("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
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
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
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
        private void setInstance(IGtUpgradeItem item) {
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
        private void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Components {
        //Components go here
        data_control_circuit("redstone_only", "Basic computer processor"),
        energy_flow_circuit("energy_only", "Manages large amounts of energy"),
        lithium_battery;

        private Item instance;
        public final String description;
        public final boolean isCover;
        public final String coverName;

        Components() {
            this(null);
        }

        Components(String coverName, String description) {
            this.description = description;
            this.isCover = true;
            this.coverName = coverName;
        }

        Components(String description) {
            this.description = description;
            this.isCover = false;
            this.coverName = "";
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
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

        private ItemWrench instance;
        public final int durability;

        Wrenches(int durability) {
            this.durability = durability;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(ItemWrench item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemWrench getInstance() {
            return this.instance;
        }
    }

    public enum JackHammers {
        bronze(50, 10000, 1, 50, 7.5F),
        steel(100, 10000, 1, 100, 15F),
        diamond(250, 100000, 2, 500, 45F);

        private ItemJackHammer instance;
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
        private void setInstance(ItemJackHammer item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemJackHammer getInstance() {
            return this.instance;
        }
    }

    public enum Hammers {
        iron(128, 4, Item.ToolMaterial.IRON),
        bronze(256, 6, Item.ToolMaterial.IRON),
        steel(512, 8, Item.ToolMaterial.IRON),
        tungstensteel(5120, 10, Item.ToolMaterial.IRON);

        private ItemHammer instance;
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
        private void setInstance(ItemHammer item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemHammer getInstance() {
            return this.instance;
        }
    }

    public enum Saws {
        iron(128, 3, 2, Item.ToolMaterial.IRON),
        bronze(256, 4, 3, Item.ToolMaterial.IRON),
        steel(1280, 6, 4, Item.ToolMaterial.IRON),
        tungstensteel(5120, 8, 5, Item.ToolMaterial.DIAMOND);

        private ItemSaw instance;
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
        private void setInstance(ItemSaw item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemSaw getInstance() {
            return this.instance;
        }
    }

    public enum SolderingMetals {
        lead(10),
        tin(50);

        private ItemSolderingMetal instance;
        public final int durability;

        SolderingMetals(int durability) {
            this.durability = durability;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(ItemSolderingMetal item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemSolderingMetal getInstance() {
            return this.instance;
        }
    }

    public enum Files {
        iron(128, 2),
        bronze(256, 3),
        steel(1280, 3),
        tungstensteel(5120, 4);

        private ItemFile instance;
        public final int durability;
        public final int entityDamage;

        Files(int durability, int entityDamage) {
            this.durability = durability;
            this.entityDamage = entityDamage;
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(ItemFile item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public ItemFile getInstance() {
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
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
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
        private void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }

    public enum Specials {
        greg_coin, //TODO: Change description
        sensor_kit,
        sensor_card;

        private Item instance;

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(Item item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+name());
            this.instance = item;
        }

        public Item getInstance() {
            return this.instance;
        }
    }
}