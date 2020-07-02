package mods.gregtechmod.common.objects.items;

import mods.gregtechmod.common.objects.items.base.ItemBase;

public class Materials {

    public static void register() {
        registerIngots();
        registerNuggets();
        registerPlates();
        registerRods();
        registerDusts();
        registerSmallDusts();
    }

    private static void registerIngots() {
        for (Ingots type : Ingots.values()) {
            new ItemBase(type.name(), type.getDescription(), "ingot");
        }
    }

    private static void registerNuggets() {
        for (Nuggets type : Nuggets.values()) {
            new ItemBase(type.name(), type.getDescription(), "nugget");
        }
    }

    private static void registerPlates() {
        for (Plates type : Plates.values()) {
            new ItemBase(type.name(), type.getDescription(), "plate");
        }
    }

    private static void registerRods() {
        for (Rods type : Rods.values()) {
            new ItemBase(type.name(), type.getDescription(), "rod");
        }
    }

    private static void registerDusts() {
        for (Dusts type : Dusts.values()) {
            new ItemBase(type.name(), type.getDescription(), "dust");
        }
    }

    private static void registerSmallDusts() {
        for (Smalldusts type : Smalldusts.values()) {
            new ItemBase(type.name(), type.getDescription(), "smalldust");
        }
    }

    //TODO: Put all enums into alphabetical order! FOR STYLE!!
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
        plutonium("Pu"),
        soldering_iron_alloy("Sn9Sb1"),
        thorium("Th"),
        titanium("Ti"),
        tungsten("W"),
        tungstensteel("Vacuum Hardened"),
        zinc("Zn");

        private final String description;

        Ingots(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
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
        platinum(Ingots.plutonium.description),
        silver(Plates.silver.description),
        steel("Fe"),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        zinc(Ingots.zinc.description);

        private final String description;

        Nuggets(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
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

        private final String description;

        Plates(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
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

        private final String description;

        Rods(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
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
        plutonium(Ingots.plutonium.description),
        pyrite("FeS2"),
        pyrope("Al2Mg3Si3O12"),
        red_garnet("(Al2Mg3Si3O12)3(Al2Fe3Si3O12)5(Al2Mn3Si3O12)8"),
        redrock("(Na2LiAl2Si2)((CaCO3)2SiO2)3"),
        ruby("Al2O6Cr"),
        saltpeter("KNO3"),
        sapphire("Al2O6"), //TODO: Change to jewel's description when it's added
        sodalite("Al3Si3Na4Cl"),
        spessartine("Al2Mn3Si3O12"),
        sphalerite("ZnS"),
        steel("Fe"),
        thorium(Ingots.thorium.description),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        uranium("U"),
        uvarovite("Ca3Cr2Si3O12"),
        wood_pulp(null),
        yellow_garnet("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
        zinc(Ingots.zinc.description);

        private final String description;

        Dusts(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
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
        plutonium(Ingots.plutonium.description),
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
        thorium(Ingots.thorium.description),
        titanium(Ingots.titanium.description),
        tungsten(Ingots.tungsten.description),
        uranium(Dusts.uranium.description),
        uvarovite(Dusts.uvarovite.description),
        wood_pulp(Dusts.wood_pulp.description),
        yellow_garnet(Dusts.yellow_garnet.description),
        zinc(Ingots.zinc.description);

        private final String description;

        Smalldusts(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
