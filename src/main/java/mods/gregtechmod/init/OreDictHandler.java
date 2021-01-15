package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Good luck reading this ;)
public class OreDictHandler {
    private Map<String, ItemStack> events = new HashMap<>();

    public static final Map<String, String> GTOreNames = new HashMap<>();

    private final List<String> ignoredNames = Arrays.asList("naquadah", "brickXyEngineering", "breederUranium", "diamondNugget", "infiniteBattery", "superconductor", "itemCharcoalSugar", "aluminumWire", "aluminiumWire", "silverWire",
            "tinWire", "eliteBattery", "advancedBattery", "transformer", "coil", "wireMill", "multimeter", "itemMultimeter", "chunkLazurite", "itemRecord", "aluminumNatural", "aluminiumNatural", "naturalAluminum", "naturalAluminium",
            "antimatterMilligram", "antimatterGram", "strangeMatter", "HSLivingmetalIngot", "oilMoving", "oilStill", "oilBucket", "orePetroleum", "dieselFuel", "lava", "water", "obsidianRod", "motor", "wrench", "coalGenerator",
            "electricFurnace", "ironTube", "netherTube", "obbyTube", "valvePart", "aquaRegia", "leatherSeal", "leatherSlimeSeal", "enrichedUranium", "batteryInfinite", "itemSuperconductor", "camoPaste", "CAMO_PASTE");

    private boolean activated = false;

    static {
        GTOreNames.put("battery", "crafting10kEUStore");
        GTOreNames.put("basicCircuit", "craftingCircuitTier02");
        GTOreNames.put("circuitBasic", "craftingCircuitTier02");
        GTOreNames.put("advancedCircuit", "craftingCircuitTier04");
        GTOreNames.put("circuitAdvanced", "craftingCircuitTier04");
        GTOreNames.put("eliteCircuit", "craftingCircuitTier06");
        GTOreNames.put("circuitElite", "craftingCircuitTier06");
        GTOreNames.put("basalt", "stoneBasalt");
        GTOreNames.put("marble", "stoneMarble");
        GTOreNames.put("mossystone", "stoneMossy");
        GTOreNames.put("MonazitOre", "oreMonazit");
        GTOreNames.put("blockQuickSilver", "blockQuicksilver");
        GTOreNames.put("ingotQuickSilver", "ingotQuicksilver");
        GTOreNames.put("ingotQuicksilver", "itemQuicksilver");
        GTOreNames.put("dustQuickSilver", "dustQuicksilver");
        GTOreNames.put("dustQuicksilver", "itemQuicksilver");
        GTOreNames.put("itemQuickSilver", "itemQuicksilver");
        GTOreNames.put("dustCharCoal", "dustCharcoal");
        GTOreNames.put("quartzCrystal", "crystalQuartz");
        GTOreNames.put("quartz", "crystalQuartz");
        GTOreNames.put("woodGas", "gasWood");
        GTOreNames.put("woodLog", "logWood");
        GTOreNames.put("pulpWood", "dustWood");
        GTOreNames.put("blockCobble", "stoneCobble");
        GTOreNames.put("gemPeridot", "gemOlivine");
        GTOreNames.put("dustPeridot", "dustOlivine");
        GTOreNames.put("dustDiamond", "itemDiamond");
        GTOreNames.put("gemDiamond", "itemDiamond");
        GTOreNames.put("dustLapis", "itemLazurite");
        GTOreNames.put("dustLapisLazuli", "itemLazurite");
        GTOreNames.put("dustLazurite", "itemLazurite");
        GTOreNames.put("craftingRawMachineTier01", "craftingRawMachineTier00");
        GTOreNames.put("dustSulfur", "craftingSulfurToGunpowder");
        GTOreNames.put("dustSaltpeter", "craftingSaltpeterToGunpowder");
        GTOreNames.put("crystalQuartz", "craftingQuartz");
        GTOreNames.put("crystalNetherQuartz", "craftingQuartz");
        GTOreNames.put("crystalCertusQuartz", "craftingQuartz");
        GTOreNames.put("dustQuartz", "craftingQuartz");
        GTOreNames.put("dustCertusQuartz", "craftingQuartz");
        GTOreNames.put("dustNetherQuartz", "craftingQuartz");
        GTOreNames.put("ingotQuartz", "craftingQuartz");
        GTOreNames.put("ingotNetherQuartz", "craftingQuartz");
        GTOreNames.put("ingotCertusQuartz", "craftingQuartz");
    }

    @SubscribeEvent
    public void registerOre(OreDictionary.OreRegisterEvent event) {
        String name;
        ItemStack ore;
        if (event == null || (ore = event.getOre()).isEmpty() || (name = event.getName()) == null || event.getName().isEmpty() || this.ignoredNames.contains(event.getName())) return;

        if (ore.getCount() != 1) GregTechAPI.logger.error("'" + name + "' is either being misused by another Mod or has been wrongly registered, as the stackSize of the Event-Stack is not 1");
        event.getOre().setCount(1);

        if (name.toLowerCase().contains("xych") || name.toLowerCase().contains("xyore") || name.toLowerCase().contains("aluminum")) return;

        String unifiedName = GTOreNames.get(name);
        if (unifiedName != null) OreDictUnificator.registerOre(unifiedName, ore);

        if (name.startsWith("denseOre")) {
            OreDictUnificator.registerOre(name.replaceFirst("denseOre", "oreDense"), ore);
            return;
        } else if (name.startsWith("netherOre")) {
            OreDictUnificator.registerOre(name.replaceFirst("netherOre", "oreNether"), ore);
            return;
        } else if (name.startsWith("endOre")) {
            OreDictUnificator.registerOre(name.replaceFirst("endOre", "oreEnd"), ore);
            return;
        } else if (name.startsWith("itemDrop")) {
            OreDictUnificator.registerOre(name.replaceFirst("itemDrop", "item"), ore);
            return;
        } else if (name.startsWith("stoneBlackGranite")) OreDictUnificator.registerOre("stoneGranite", ore);
        else if (name.startsWith("stoneRedGranite")) OreDictUnificator.registerOre("stoneGranite", ore);

        if (name.startsWith("plate") || name.startsWith("ore") || name.startsWith("dust") || name.startsWith("gem")
            || name.startsWith("ingot") || name.startsWith("nugget") || name.startsWith("block") || name.startsWith("stick")) OreDictUnificator.addAssociation(name, ore.copy());

        if (this.activated) registerRecipes(event.getName(), event.getOre());
        else this.events.put(event.getName(), event.getOre());
    }

    public void activateHandler() {
        this.activated = true;
        for (Map.Entry<String, ItemStack> entry : this.events.entrySet()) registerRecipes(entry.getKey(), entry.getValue());
        this.events = null;
    }

    private void processStoneOre(ItemStack stack, String name) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            GregTechAPI.jackHammerMinableBlocks.add(((ItemBlock)item).getBlock());
            if (stack.getMaxStackSize() > GregTechConfig.FEATURES.maxOtherBlockStackSize) item.setMaxStackSize(GregTechConfig.FEATURES.maxOtherBlockStackSize);
        }

        if (name.equals("stoneObsidian") && item instanceof ItemBlock) {
            ((ItemBlock)item).getBlock().setResistance(20.0F);
        }
    }

    public void registerRecipes(String name, ItemStack ore) {
        /*ItemStack ore = event.getOre();
        if (ore.isEmpty()) return;

        ore.setCount(1);
        Item item = ore.getItem();
        int meta = ore.getItemDamage();
        int cellCount = 0;
        String name = event.getName();
        ItemStack stack = new ItemStack(item, 1, meta);
        if (name.startsWith("plate") || name.startsWith("ore") || name.startsWith("dust") || name.startsWith("gem") || name.startsWith("ingot") || name.startsWith("nugget") || name.startsWith("block") || name.startsWith("stick")) OreDictUnificator.add(name, ore.copy());

        if (name.startsWith("drop")) name = name.replaceFirst("drop", "item");

        if (name.startsWith("stone")) {
            registerStoneRecipes(stack, name, event);
        } else if (name.startsWith("ore")) {
            registerOreRecipes(stack, name, event);
        } else if (name.startsWith("dust")) {
            registerDustRecipes(item, meta, name, event);
        } else if (name.startsWith("ingot")) {
            registerIngotRecipes(item, meta, name, event);
        } else if (name.startsWith("block")) {
            registerBlockRecipes(item, meta, name, event);
        } else if (name.startsWith("gem")) {
            registerGemRecipes(item, meta, name, event);
        } else if (name.startsWith("crystal") && !name.startsWith("crystalline")) {
            if (!name.equals("crystalQuartz") && name.equals("crystalNetherQuartz")) ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre("dustNetherQuartz", 1), null, 0, true);
        } else if (name.startsWith("plasma_")) {
            GregTech_API.sRecipeAdder.addFuel(ore, (GtUtil.getCapsuleCellContainerCount(ore) == 1) ? GtUtil.emptyCell : null, 8192, 4);
            GregTech_API.sRecipeAdder.addVacuumFreezerRecipe(ore, (GtUtil.getCapsuleCellContainerCount(ore) == 1) ? OreDictUnificator.getFirstCapsulatedOre(name.replaceFirst("plasma_", "molecule_"), 1) : OreDictUnificator.getFirstUnCapsulatedOre(name.replaceFirst("plasma_", "molecule_"), 1), 100);
        } else if (name.startsWith("molecule_")) {
            if (name.equals("molecule_1h")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(8, 1), new ItemStack(item, 4, meta), GT_Metitem_Cell.instance.getStack(9, 5), 3500);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(new ItemStack(item, 4, meta), ModHandler.getIC2Item("airCell", 1), ModHandler.getIC2Item("waterCell", 5), 10);
                }
                cellCount = 4 * GtUtil.getCapsuleCellContainerCount(ore) - 1;
                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 4, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 1, 1), null, null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 3000);
            } else if (name.equals("molecule_1he")) {
                cellCount = 16 * GtUtil.getCapsuleCellContainerCount(ore) - 1;
                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 16, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 1, 6), null, null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 10000);
            } else if (name.equals("molecule_1d") || name.equals("molecule_1h2")) {
                ArrayList tList = OreDictUnificator.getOres("molecule_1t");
                tList.addAll(OreDictUnificator.getOres("molecule_1h3"));
                Iterator<ItemStack> tIterator = tList.iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Cell.instance.getStack(131, 1), 128, -4096, 40000000));
                tIterator = OreDictUnificator.getOres("molecule_1he3").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Cell.instance.getStack(131, 1), 128, -2048, 60000000));
                cellCount = 4 * GtUtil.getCapsuleCellContainerCount(ore) - 1;
                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 4, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 1, 2), null, null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 3000);
            } else if (name.equals("molecule_1t") || name.equals("molecule_1h3")) {
                ArrayList tList = OreDictUnificator.getOres("molecule_1d");
                tList.addAll(OreDictUnificator.getOres("molecule_1h2"));
                Iterator<ItemStack> tIterator = tList.iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Cell.instance.getStack(131, 1), 128, -4096, 40000000));
            } else if (name.equals("molecule_1he3")) {
                ArrayList tList = OreDictUnificator.getOres("molecule_1d");
                tList.addAll(OreDictUnificator.getOres("molecule_1h2"));
                Iterator<ItemStack> tIterator = tList.iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Cell.instance.getStack(131, 1), 128, -2048, 60000000));
            } else if (name.equals("molecule_1w")) {
                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("molecule_1li").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), ModHandler.getIC2Item("iridiumOre", 1, OreDictUnificator.getFirstOre("dustIridium", 1)), 512, -32768, 150000000));
                tIterator = OreDictUnificator.getOres("molecule_1be").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Dust.instance.getStack(27, 1), 512, -32768, 100000000));
            } else if (name.equals("molecule_1li")) {
                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("molecule_1w").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, ModHandler.getIC2Item("iridiumOre", 1, OreDictUnificator.getFirstOre("dustIridium", 1)), 512, -32768, 150000000));
            } else if (name.equals("molecule_1be")) {
                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("molecule_1w").iterator();
                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addFusionReactorRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Dust.instance.getStack(27, 1), 512, -32768, 100000000));
            } else if (name.equals("molecule_1c_4h") || name.equals("molecule_1me")) {
                cellCount = 5 * GtUtil.getCapsuleCellContainerCount(ore) - 5;
                GregTech_API.sRecipeAdder.addElectrolyzerRecipe(new ItemStack(item, 5, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 4, 0), GregTech_API.getGregTechItem(2, 1, 8), null, (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 150, 50);
            } else if (name.equals("molecule_1si")) {
                cellCount = 2 * GtUtil.getCapsuleCellContainerCount(ore);
                GregTech_API.sRecipeAdder.addBlastRecipe(new ItemStack(item, 2, meta), null, GT_Metitem_Material.instance.getStack(36, 1), (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 1000, 128, 1500);
            } else if (name.equals("molecule_1c")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                    GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(0, 4), GT_Metitem_Cell.instance.getStack(9, 5), 3500);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(15, 1), ore, GT_Metitem_Cell.instance.getStack(39, 2), 1500);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(11, 1), ore, GT_Metitem_Cell.instance.getStack(33, 2), 250);
                }
            } else if (name.equals("molecule_1ca")) {
                GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(8, 1), GT_Metitem_Cell.instance.getStack(33, 2), 250);
            } else if (name.equals("molecule_1na")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                    GregTech_API.sRecipeAdder.addChemicalRecipe(GT_Metitem_Cell.instance.getStack(36, 1), ore, GT_Metitem_Cell.instance.getStack(37, 2), 100);
            } else if (name.equals("molecule_1s")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                    GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(12, 1), GT_Metitem_Cell.instance.getStack(37, 2), 100);
                    GregTech_API.sRecipeAdder.addCentrifugeRecipe(ore, 0, OreDictUnificator.get("dustSulfur", 1), null, null, GtUtil.emptyCell, 40);
                    GregTech_API.sRecipeAdder.addChemicalRecipe(ore, ModHandler.getIC2Item("waterCell", 2), GT_Metitem_Cell.instance.getStack(40, 3), 1150);
                }
            } else if (name.equals("molecule_1na_1s")) {
                if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                    GregTech_API.sRecipeAdder.addChemicalRecipe(new ItemStack(item, 2, meta), ModHandler.getIC2Item("airCell", 3), GT_Metitem_Cell.instance.getStack(32, 5), 4000);
            } else if (!name.equals("molecule_1cl")) {
                if (!name.equals("molecule_1k"))
                    if (name.equals("molecule_1n")) {
                        if (item.getItemStackLimit() >= 16)
                            for (ItemStack tStack : OreDictUnificator.getOres("craftingSprayCan"))
                                GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 16, meta), tStack.copy().splitStack(1), GregTech_API.getGregTechItem(91, 1, 0), ModHandler.getEmptyCell(GtUtil.getCapsuleCellContainerCount(ore) * 16), 1600, 2);
                        if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, ModHandler.getIC2Item("airCell", 1), GT_Metitem_Cell.instance.getStack(38, 2), 1250);
                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(8, 1), GT_Metitem_Cell.instance.getStack(39, 2), 1500);
                        }
                    } else if (name.equals("molecule_1n_1c")) {
                        if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                            GregTech_API.sRecipeAdder.addChemicalRecipe(new ItemStack(item, 3, meta), ModHandler.getIC2Item("waterCell", 3), GT_Metitem_Cell.instance.getStack(34, 6), 1750);
                    } else if (name.equals("molecule_2h_1s_4o")) {
                        GregTech_API.sRecipeAdder.addElectrolyzerRecipe(new ItemStack(item, 7, meta), 0, GT_Metitem_Cell.instance.getStack(0, 2), GT_Metitem_Cell.instance.getStack(36, 1), ModHandler.getIC2Item("airCell", 2), ModHandler.getEmptyCell(2), 40, 100);
                    } else if (!name.equals("molecule_1n_2o")) {
                        if (name.equals("molecule_1hg")) {
                            if (GtUtil.getCapsuleCellContainerCount(ore) == 1 &&
                                    ModHandler.mTCResource != null)
                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(ore, 0, new ItemStack(ModHandler.mTCResource.getItem(), 1, 3), null, null, GtUtil.emptyCell, 40);
                        } else if (name.equals("molecule_1ca_1c_3o")) {
                            if (GtUtil.getCapsuleCellContainerCount(ore) == 1)
                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(ore, 0, OreDictUnificator.get("dustCalcite", 1), null, null, GtUtil.emptyCell, 40);
                        } else if (!name.equals("molecule_2na_2s_8o")) {
                            if (!name.equals("molecule_2o"))
                                if (!name.equals("molecule_1o"))
                                    if (name.equals("molecule_3c_5h_3n_9o")) {
                                        if (GtUtil.getCapsuleCellContainerCount(ore) == 1) {
                                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, ModHandler.getIC2Item("coalfuelCell", 4), GT_Metitem_Cell.instance.getStack(35, 5), 250);
                                            GregTech_API.sRecipeAdder.addChemicalRecipe(ore, GT_Metitem_Cell.instance.getStack(18, 4), GT_Metitem_Cell.instance.getStack(22, 5), 250);
                                        }
                                    } else {
                                        System.out.println("Molecule Name: " + name + " !!!Unknown Molecule detected!!! Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information.");
                                    }
                        }
                    }
            }
        } else if (name.startsWith("nugget")) {
            ModHandler.addToRecyclerBlackList(ore);
            if (name.equals("nuggetGold"))
                if (ModHandler.mBCStoneGear == null) {
                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("stoneCobble").iterator();
                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 4, meta), ModHandler.getRCItem("part.gear.gold.plate", 1), 800, 1));
                } else {
                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.mBCStoneGear, new ItemStack(item, 4, meta), ModHandler.getRCItem("part.gear.gold.plate", 1), 800, 1);
                }
            if (name.equals("nuggetIridium") || name.equals("nuggetOsmium") || name.equals("nuggetUranium") || name.equals("nuggetPlutonium") || name.equals("nuggetThorium")) {
                ModHandler.addCompressionRecipe(new ItemStack(item, 9, meta), OreDictUnificator.getFirstOre(name.replaceFirst("nugget", "ingot"), 1));
            } else {
                GregTech_API.sRecipeAdder.addAlloySmelterRecipe(new ItemStack(item, 9, meta), null, OreDictUnificator.getFirstOre(name.replaceFirst("nugget", "ingot"), 1), 200, 1);
            }
            ModHandler.addShapelessCraftingRecipe(OreDictUnificator.getFirstOre(name.replaceFirst("nugget", "ingot"), 1), new Object[] {name, name, name, name, name, name, name, name, name});
            ModHandler.addShapelessCraftingRecipe(OreDictUnificator.getFirstOre(name, 9), new Object[] { name.replaceFirst("nugget", "ingot") });
        } else if (!name.startsWith("shard")) {
            if (name.startsWith("wax")) {
                if (name.equals("waxMagical"))
                    GregTech_API.sRecipeAdder.addFuel(ore, null, 6, 5);
            } else if (name.startsWith("element_")) {
                System.err.println("Depricated Prefix 'element_' @ " + name + " please change to 'molecule_'");
            } else if (name.startsWith("cell_")) {
                System.err.println("Depricated Prefix 'cell_' @ " + name + " Cells are now detected automatically, if you register as 'molecule_', so you don't need to register with this prefix any longer");
            } else if (!name.startsWith("element")) {
                if (!name.startsWith("cell"))
                    if (name.startsWith("crafting")) {
                        if (name.equals("craftingLiBattery")) {
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("cropnalyzer", 1, 32767), GregTech_API.getGregTechItem(63, 1, GregTech_API.getGregTechItem(63, 1, 0).getMaxDamage() - 1), 12800, 16);
                            Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateAluminium").iterator();
                            for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(4), GT_Metitem_Component.instance.getStack(26, 1), 3200, 4));
                        } else if (name.equals("craftingRawMachineTier01")) {
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.music, 4, 32767), new ItemStack(GregTech_API.sBlockList[1], 1, 66), 800, 1);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.stoneButton, 16, 32767), new ItemStack(GregTech_API.sBlockList[1], 1, 67), 800, 1);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GT_Metitem_Component.instance.getStack(22, 1), new ItemStack(GregTech_API.sBlockList[1], 1, 79), 1600, 2);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GT_Metitem_Component.instance.getStack(7, 1), ModHandler.getIC2Item("solarPanel", 1), 1600, 2);
                        } else if (name.equals("craftingGearTier02")) {
                            for (ItemStack tStack : OreDictUnificator.getOres("craftingGenerator"))
                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, tStack.copy().splitStack(1), GT_Metitem_Component.instance.getStack(25, 1), 3200, 4);
                        } else if (name.equals("craftingGenerator")) {
                            for (ItemStack tStack : OreDictUnificator.getOres("craftingGearTier02"))
                                GregTech_API.sRecipeAdder.addAssemblerRecipe(tStack.copy().splitStack(1), ore, GT_Metitem_Component.instance.getStack(25, 1), 3200, 4);
                        } else if (name.equals("craftingWireCopper")) {
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(GT_Metitem_Component.instance.getStack(48, 1), new ItemStack(item, 3, meta), ModHandler.getIC2Item("electronicCircuit", 1), 800, 1);
                            GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("electronicCircuit", 1), ore, ModHandler.getIC2Item("frequencyTransmitter", 1), 800, 1);
                        } else if (name.equals("craftingSprayCan")) {
                            GregTech_API.sRecipeAdder.addCannerRecipe(ModHandler.getIC2Item("grinPowder", 1), ore, ModHandler.getIC2Item("weedEx", 1), null, 800, 1);
                            GregTech_API.sRecipeAdder.addCannerRecipe(ModHandler.getIC2Item("waterCell", 16), ore, GregTech_API.getGregTechItem(95, 1, 0), ModHandler.getEmptyCell(16), 1600, 2);
                            GregTech_API.sRecipeAdder.addCannerRecipe(ModHandler.getIC2Item("constructionFoamPellet", 16), ore, GregTech_API.getGregTechItem(93, 1, 0), null, 1600, 2);
                            GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(Block.sand, 16, 0), ore, GregTech_API.getGregTechItem(92, 1, 0), null, 1600, 2);
                            for (ItemStack tStack : OreDictUnificator.getOres("molecule_1n")) {
                                if (tStack.getMaxStackSize() >= 16)
                                    GregTech_API.sRecipeAdder.addCannerRecipe(tStack.copy().splitStack(16), ore, GregTech_API.getGregTechItem(91, 1, 0), ModHandler.getEmptyCell(GtUtil.getCapsuleCellContainerCount(tStack) * 16), 1600, 2);
                            }
                            for (String tDyeName : GT_Utility.sDyeToMetaMapping.keySet()) {
                                for (ItemStack tStack : OreDictUnificator.getOres(tDyeName)) {
                                    if (tStack.getMaxStackSize() >= 16 && !tStack.getItem().hasContainerItem())
                                        GregTech_API.sRecipeAdder.addCannerRecipe(tStack.copy().splitStack(16), ore, GregTech_API.getGregTechItem(96 + ((Byte)GT_Utility.sDyeToMetaMapping.get(tDyeName)).byteValue(), 1, 0), null, 800, 1);
                                }
                            }
                        }
                    } else if (name.startsWith("dye")) {
                        if (item.getItemStackLimit() >= 16 && !item.hasContainerItem()) {
                            Iterator<ItemStack> tIterator = OreDictUnificator.getOres("craftingSprayCan").iterator();
                            for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 16, meta), ((ItemStack)tIterator.next()).copy().splitStack(1), GregTech_API.getGregTechItem(96 + ((Byte)GT_Utility.sDyeToMetaMapping.get(name)).byteValue(), 1, 0), null, 800, 1));
                        }
                    } else if (!name.startsWith("clump")) {
                        if (!name.startsWith("glass"))
                            if (!name.startsWith("gear"))
                                if (!name.startsWith("material"))
                                    if (!name.startsWith("storage"))
                                        if (!name.startsWith("tool"))
                                            if (!name.startsWith("food"))
                                                if (!name.startsWith("flower"))
                                                    if (name.startsWith("plate")) {
                                                        boolean temp = true;
                                                        if (temp)
                                                            if (name.startsWith("plateAlloy")) {
                                                                temp = false;
                                                                if (name.equals("plateAlloyCarbon")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("generator", 1), new ItemStack(item, 4, meta), ModHandler.getIC2Item("windMill", 1), 6400, 8);
                                                                } else if (name.equals("plateAlloyAdvanced")) {
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("stoneSmooth").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(8), ModHandler.getIC2Item("reinforcedStone", 8), 400, 4));
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.glass, 7, 0), ModHandler.getIC2Item("reinforcedGlass", 7), 400, 4);
                                                                }
                                                            } else {
                                                                ModHandler.removeRecipe(ore);
                                                            }
                                                        if (temp)
                                                            if (name.startsWith("plateDense")) {
                                                                temp = false;
                                                                ModHandler.addSmeltingAndAlloySmeltingRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plateDense", "ingot"), 8));
                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plateDense", "dust"), 8), null, 0, false);
                                                            } else {
                                                                ModHandler.addCompressionRecipe(new ItemStack(item, 8, meta), OreDictUnificator.getFirstOre(name.replaceFirst("plate", "plateDense"), 1));
                                                                ModHandler.addSmeltingAndAlloySmeltingRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plate", "ingot"), 1));
                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("plate", "dust"), 1), null, 0, false);
                                                            }
                                                        if (temp)
                                                            if (name.equals("plateCopper")) {
                                                                ModHandler.addCompressionRecipe(new ItemStack(item, 8, meta), ModHandler.getIC2Item("denseCopperPlate", 1));
                                                            } else if (name.equals("plateTin")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, null, ModHandler.getIC2Item("tinCan", 1), 400, 1);
                                                            } else if (name.equals("plateIron")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 3, meta), null, new ItemStack(Item.bucketEmpty, 1, 0), 400, 1);
                                                            } else if (name.equals("plateBronze")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(33, 1), 400, 8);
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickBronze"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(97, 1), 3200, 4);
                                                            } else if (name.equals("plateBrass")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(34, 1), 400, 8);
                                                            } else if (name.equals("plateRefinedIron")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 8, meta), GT_Metitem_Component.instance.getStack(22, 1), ModHandler.getIC2Item("machine", 1, GT_Metitem_Component.instance.getStack(37, 1)), 400, 8);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack((Block)Block.chest, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack(Block.chestTrapped, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("pump", 1), GT_Metitem_Component.instance.getStack(6, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateIron, 1, 32767), GT_Metitem_Component.instance.getStack(11, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateGold, 1, 32767), GT_Metitem_Component.instance.getStack(10, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("ecMeter", 1), GT_Metitem_Component.instance.getStack(15, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.fenceIron, 2), GT_Metitem_Component.instance.getStack(9, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneActive, 1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneIdle, 1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Item.comparator, 1), GT_Metitem_Component.instance.getStack(30, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.lever, 1), GT_Metitem_Component.instance.getStack(31, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.workbench, 1), GT_Metitem_Component.instance.getStack(64, 1), 800, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("energyCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(12, 1), 1600, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("lapotronCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(13, 1), 3200, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GregTech_API.getGregTechItem(37, 1, 32767), GT_Metitem_Component.instance.getStack(14, 1), 6400, 16);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), ModHandler.getIC2Item("electronicCircuit", 1), GT_Metitem_Component.instance.getStack(22, 4), 800, 16);
                                                                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateElectrum").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(2), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                tIterator = OreDictUnificator.getOres("plateIridium").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(86, 1), 800, 16));
                                                                tIterator = OreDictUnificator.getOres("craftingRedstoneTorch").iterator();
                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16));
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickRefinedIron"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(96, 1), 3200, 4);
                                                            } else if (name.equals("plateSteel")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), ModHandler.getRCItem("machine.beta.engine.steam.high", 1, GregTech_API.getGregTechBlock(1, 1, 34)), GT_Metitem_Component.instance.getStack(80, 1), 1600, 32);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.thinGlass, 1, 32767), GT_Metitem_Component.instance.getStack(81, 1), 1600, 32);
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(35, 1), 400, 8);
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickSteel"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(98, 1), 3200, 4);
                                                            } else if (name.equals("plateTitanium")) {
                                                                GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(36, 1), 400, 8);
                                                                for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickTitanium"))
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(99, 1), 3200, 4);
                                                            } else if (!name.equals("plateTungsten")) {
                                                                if (name.equals("plateTungstenSteel")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(38, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("reinforcedStone", 1), new ItemStack(GregTech_API.sBlockList[4], 1, 8), 400, 4);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(GregTech_API.sBlockList[0], 1, 2), new ItemStack(GregTech_API.sBlockList[4], 1, 9), 400, 4);
                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickTungstenSteel"))
                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(100, 1), 3200, 4);
                                                                } else if (name.equals("plateIridium")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("reinforcedStone", 1), new ItemStack(GregTech_API.sBlockList[0], 1, 2), 400, 4);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(GregTech_API.sBlockList[4], 1, 8), new ItemStack(GregTech_API.sBlockList[4], 1, 9), 400, 4);
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateRefinedIron").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                    tIterator = OreDictUnificator.getOres("plateAluminium").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), ore, GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("stickIridium"))
                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(tIteratedStack.copy().splitStack(4), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(101, 1), 3200, 4);
                                                                } else if (name.equals("plateAluminium")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack((Block)Block.chest, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 5, meta), new ItemStack(Block.chestTrapped, 1, 32767), new ItemStack((Block)Block.hopperBlock), 800, 2);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("pump", 1), GT_Metitem_Component.instance.getStack(6, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateIron, 1, 32767), GT_Metitem_Component.instance.getStack(11, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.pressurePlateGold, 1, 32767), GT_Metitem_Component.instance.getStack(10, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("ecMeter", 1), GT_Metitem_Component.instance.getStack(15, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), new ItemStack(Block.fenceIron, 2), GT_Metitem_Component.instance.getStack(9, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneActive, 1), GT_Metitem_Component.instance.getStack(30, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.torchRedstoneIdle, 1), GT_Metitem_Component.instance.getStack(30, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.lever, 1), GT_Metitem_Component.instance.getStack(31, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Block.workbench, 1), GT_Metitem_Component.instance.getStack(64, 1), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("energyCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(12, 1), 1600, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("lapotronCrystal", 1, 32767), GT_Metitem_Component.instance.getStack(13, 1), 3200, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, GregTech_API.getGregTechItem(37, 1, 32767), GT_Metitem_Component.instance.getStack(14, 1), 6400, 16);
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("craftingLiBattery").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(26, 1), 3200, 4));
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 2, meta), ModHandler.getIC2Item("electronicCircuit", 1), GT_Metitem_Component.instance.getStack(22, 3), 800, 16);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 6, meta), GT_Metitem_Component.instance.getStack(22, 1), GT_Metitem_Component.instance.getStack(32, 1), 400, 8);
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("generator", 1), new ItemStack(item, 4, meta), ModHandler.getIC2Item("waterMill", 2), 6400, 8);
                                                                    tIterator = OreDictUnificator.getOres("plateElectrum").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(2), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                    tIterator = OreDictUnificator.getOres("plateIridium").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(88, 1), 1600, 2));
                                                                    tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(86, 1), 800, 16));
                                                                    tIterator = OreDictUnificator.getOres("craftingRedstoneTorch").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(87, 1), 800, 16));
                                                                } else if (name.equals("plateElectrum")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("electronicCircuit", 1), new ItemStack(item, 2, meta), GT_Metitem_Component.instance.getStack(49, 1), 1600, 2);
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateRefinedIron").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 2, meta), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                    tIterator = OreDictUnificator.getOres("plateAluminium").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 2, meta), GT_Metitem_Component.instance.getStack(48, 2), 800, 1));
                                                                    tIterator = OreDictUnificator.getOres("plateSilicon").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(item, 4, meta), GT_Metitem_Component.instance.getStack(49, 2), 1600, 2));
                                                                } else if (name.equals("plateSilicon")) {
                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("plateElectrum").iterator();
                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(4), GT_Metitem_Component.instance.getStack(49, 2), 1600, 2));
                                                                } else if (name.equals("platePlatinum")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("advancedCircuit", 1), GT_Metitem_Component.instance.getStack(50, 1), 3200, 4);
                                                                } else if (name.equals("plateMagnalium")) {
                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("generator", 1), new ItemStack(item, 2, meta), ModHandler.getIC2Item("windMill", 1), 6400, 8);
                                                                }
                                                            }
                                                        if (OreDictUnificator.getFirstOre(name.replaceFirst("plate", "ingot"), 1) != null)
                                                            ModHandler.addCraftingRecipe(ore, new Object[] { "H", "I", "I", Character.valueOf('H'), "craftingToolHardHammer", Character.valueOf('I'), name.replaceFirst("plate", "ingot") });
                                                    } else if (!name.startsWith("dirtyGravel")) {
                                                        if (!name.startsWith("cleanGravel"))
                                                            if (!name.startsWith("crystalline"))
                                                                if (!name.startsWith("reduced"))
                                                                    if (name.startsWith("paper")) {
                                                                        OreDictUnificator.addAssociation(name, ore);
                                                                    } else if (name.startsWith("book")) {
                                                                        OreDictUnificator.addAssociation(name, ore);
                                                                    } else if (!name.startsWith("FZ.")) {
                                                                        if (!name.startsWith("crop"))
                                                                            if (!name.startsWith("lumar"))
                                                                                if (!name.startsWith("list"))
                                                                                    if (!name.startsWith("seed"))
                                                                                        if (!name.startsWith("sheet"))
                                                                                            if (!name.startsWith("icbm:"))
                                                                                                if (!name.startsWith("calclavia:"))
                                                                                                    if (!name.startsWith("mffs"))
                                                                                                        if (!name.startsWith("MiscPeripherals$"))
                                                                                                            if (name.equals("gasWood")) {
                                                                                                                cellCount = 16 * GtUtil.getCapsuleCellContainerCount(ore) - 16;
                                                                                                                GregTech_API.sRecipeAdder.addElectrolyzerRecipe(new ItemStack(item, 16, meta), (cellCount < 0) ? -cellCount : 0, GregTech_API.getGregTechItem(2, 4, 0), GregTech_API.getGregTechItem(2, 8, 8), GregTech_API.getGregTechItem(2, 4, 9), (cellCount > 0) ? ModHandler.getEmptyCell(cellCount) : null, 200, 100);
                                                                                                            } else if (name.equals("woodRubber") || name.equals("logRubber")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (GT_Utility.areStacksEqual(ModHandler.getIC2Item("rubberWood", 1), ore))
                                                                                                                    meta = 32767;
                                                                                                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 16, meta), 5, ModHandler.getIC2Item("resin", 8), ModHandler.getIC2Item("plantBall", 6), GregTech_API.getGregTechItem(2, 1, 9), GregTech_API.getGregTechItem(2, 4, 8), 5000);
                                                                                                                ModHandler.addSawmillRecipe(ore, ModHandler.getIC2Item("resin", 1), GT_Metitem_Dust.instance.getStack(15, 16));
                                                                                                                ModHandler.addExtractionRecipe(ore, ModHandler.getIC2Item("rubber", 1));
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                        if (tStack != null) {
                                                                                                                            ModHandler.removeRecipe(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 4 / 3)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { new ItemStack(item, 1, i) });
                                                                                                                        }
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { ore });
                                                                                                                    if (tStack != null) {
                                                                                                                        ModHandler.removeRecipe(new ItemStack[] { ore });
                                                                                                                        ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 4 / 3)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), ore });
                                                                                                                        ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { ore });
                                                                                                                    }
                                                                                                                }
                                                                                                            } else if (name.startsWith("log")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                        if (tStack != null) {
                                                                                                                            ItemStack tPlanks = tStack.copy();
                                                                                                                            tPlanks.stackSize = tPlanks.stackSize * 3 / 2;
                                                                                                                            ModHandler.addSawmillRecipe(new ItemStack(item, 1, i), tPlanks, GT_Metitem_Dust.instance.getStack(15, 1));
                                                                                                                            ModHandler.removeRecipe(new ItemStack[] { new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 5 / 4)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), new ItemStack(item, 1, i) });
                                                                                                                            ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { new ItemStack(item, 1, i) });
                                                                                                                        }
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ItemStack tStack = ModHandler.getRecipeOutput(new ItemStack[] { ore });
                                                                                                                    if (tStack != null) {
                                                                                                                        ItemStack tPlanks = tStack.copy();
                                                                                                                        tPlanks.stackSize = tPlanks.stackSize * 3 / 2;
                                                                                                                        ModHandler.addSawmillRecipe(ore, tPlanks, GT_Metitem_Dust.instance.getStack(15, 1));
                                                                                                                        ModHandler.removeRecipe(new ItemStack[] { ore });
                                                                                                                        ModHandler.addCraftingRecipe(tStack.copy().splitStack(GT_Mod.sNerfedWoodPlank ? tStack.stackSize : (tStack.stackSize * 5 / 4)), new Object[] { "S", "L", Character.valueOf('S'), "craftingToolSaw", Character.valueOf('L'), ore });
                                                                                                                        ModHandler.addShapelessCraftingRecipe(tStack.copy().splitStack(tStack.stackSize / (GT_Mod.sNerfedWoodPlank ? 2 : 1)), new Object[] { ore });
                                                                                                                    }
                                                                                                                }
                                                                                                            } else if (name.startsWith("slabWood")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sPlankStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sPlankStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addPulverisationRecipe(new ItemStack(item, 1, i), OreDictUnificator.get("dustSmallWood", 2), null, 0, false);
                                                                                                                        GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, i), ModHandler.getRCItem("liquid.creosote.cell", 1), ModHandler.getRCItem("part.tie.wood", 1), GtUtil.emptyCell, 200, 4);
                                                                                                                        GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, i), ModHandler.getRCItem("liquid.creosote.bucket", 1), ModHandler.getRCItem("part.tie.wood", 1), new ItemStack(Item.bucketEmpty, 1), 200, 4);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addPulverisationRecipe(ore, OreDictUnificator.get("dustSmallWood", 2), null, 0, false);
                                                                                                                    GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("liquid.creosote.cell", 1), ModHandler.getRCItem("part.tie.wood", 1), GtUtil.emptyCell, 200, 4);
                                                                                                                    GregTech_API.sRecipeAdder.addCannerRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("liquid.creosote.bucket", 1), ModHandler.getRCItem("part.tie.wood", 1), new ItemStack(Item.bucketEmpty, 1), 200, 4);
                                                                                                                }
                                                                                                            } else if (name.startsWith("plankWood")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sPlankStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sPlankStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addPulverisationRecipe(new ItemStack(item, 1, i), GT_Metitem_Dust.instance.getStack(15, 1), null, 0, false);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(15, 1), null, 0, false);
                                                                                                                }
                                                                                                                GregTech_API.sRecipeAdder.addLatheRecipe(ore, OreDictUnificator.get("stickWood", 2), null, 25, 8);
                                                                                                                GregTech_API.sRecipeAdder.addCNCRecipe(new ItemStack(item, 2, meta), ModHandler.mBCWoodGear, 800, 1);
                                                                                                                Iterator<ItemStack> tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 8, meta), ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.music, 1), 800, 1));
                                                                                                                tIterator = OreDictUnificator.getOres("gemDiamond").iterator();
                                                                                                                for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 8, meta), ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.jukebox, 1), 1600, 1));
                                                                                                            } else if (name.startsWith("treeSapling")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addCompressionRecipe(new ItemStack(item, 4, i), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                        ModHandler.addPulverisationRecipe(new ItemStack(item, 1, i), GT_Metitem_SmallDust.instance.getStack(15, 2), null, 0, false);
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addCompressionRecipe(new ItemStack(item, 4, meta), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                    ModHandler.addPulverisationRecipe(ore, GT_Metitem_SmallDust.instance.getStack(15, 2), null, 0, false);
                                                                                                                }
                                                                                                            } else if (name.equals("treeLeaves")) {
                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && GT_Mod.sWoodStackSize < item.getItemStackLimit())
                                                                                                                    item.setMaxStackSize(GT_Mod.sWoodStackSize);
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++) {
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                        ModHandler.addCompressionRecipe(new ItemStack(item, 8, i), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                    ModHandler.addCompressionRecipe(new ItemStack(item, 8, meta), ModHandler.getIC2Item("compressedPlantBall", 1));
                                                                                                                }
                                                                                                            } else if (name.startsWith("stick")) {
                                                                                                                if (meta == 32767) {
                                                                                                                    for (int i = 0; i < 16; i++)
                                                                                                                        OreDictUnificator.addAssociation(name, new ItemStack(item, 1, i));
                                                                                                                } else {
                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                }
                                                                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.getFirstOre(name.replaceFirst("stick", "dustSmall"), 2), null, 0, false);
                                                                                                                if (name.equals("stickWood")) {
                                                                                                                    Iterator<ItemStack> tIterator = OreDictUnificator.getOres("stoneCobble").iterator();
                                                                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.lever, 1), 400, 1));
                                                                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, new ItemStack(Item.coal, 1, 32767), new ItemStack(Block.torchWood, 4), 400, 1);
                                                                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ModHandler.getIC2Item("resin", 1), new ItemStack(Block.torchWood, 4), 400, 1);
                                                                                                                    tIterator = OreDictUnificator.getOres("dustRedstone").iterator();
                                                                                                                    for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), new ItemStack(Block.torchRedstoneActive, 1), 400, 1));
                                                                                                                } else if (name.equals("stickAluminium")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 1), 150, 10);
                                                                                                                } else if (name.equals("stickIron")) {
                                                                                                                    GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 3, meta), null, new ItemStack(Block.fenceIron, 4), 400, 1);
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 2), 150, 10);
                                                                                                                } else if (name.equals("stickRefinedIron")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 6, meta), ModHandler.getRCItem("part.rail.standard", 5), 150, 20);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateRefinedIron"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(96, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickBronze")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 2, meta), ModHandler.getRCItem("part.rail.standard", 1), 50, 20);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateBronze"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(97, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickSteel")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 4), 150, 30);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateSteel"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(98, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickTitanium")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 8), 150, 30);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateTitanium"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(99, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickTungsten")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(new ItemStack(item, 3, meta), ModHandler.getRCItem("part.rail.standard", 8), 150, 30);
                                                                                                                } else if (name.equals("stickTungstenSteel")) {
                                                                                                                    GregTech_API.sRecipeAdder.addBenderRecipe(ore, ModHandler.getRCItem("part.rail.reinforced", 4), 100, 30);
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateTungstenSteel"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(100, 1), 3200, 4);
                                                                                                                } else if (name.equals("stickIridium")) {
                                                                                                                    for (ItemStack tIteratedStack : OreDictUnificator.getOres("plateIridium"))
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(new ItemStack(item, 4, meta), tIteratedStack.copy().splitStack(4), GT_Metitem_Component.instance.getStack(101, 1), 3200, 4);
                                                                                                                }
                                                                                                            } else if (!name.startsWith("stair")) {
                                                                                                                if (!name.startsWith("slab"))
                                                                                                                    if (name.equals("itemLazurite") || name.equals("lazurite")) {
                                                                                                                        Iterator<ItemStack> tIterator = OreDictUnificator.getOres("dustGlowstone").iterator();
                                                                                                                        for (; tIterator.hasNext(); GregTech_API.sRecipeAdder.addAssemblerRecipe(ore, ((ItemStack)tIterator.next()).copy().splitStack(1), GT_Metitem_Component.instance.getStack(24, 2), 800, 2));
                                                                                                                    } else if (name.equals("itemDiamond") || name.equals("diamond")) {
                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.mBCGoldGear, new ItemStack(item, 4, meta), ModHandler.mBCDiamondGear, 1600, 2);
                                                                                                                    } else if (!name.equals("itemIridium") && !name.equals("iridium")) {
                                                                                                                        if (!name.equals("itemTear") && !name.equals("tear"))
                                                                                                                            if (!name.equals("itemClaw") && !name.equals("claw"))
                                                                                                                                if (!name.equals("itemTar") && !name.equals("tar"))
                                                                                                                                    if (!name.equals("itemSlimeball") && !name.equals("slimeball"))
                                                                                                                                        if (!name.equals("fuelCoke") && !name.equals("coke"))
                                                                                                                                            if (!name.equals("itemBeeswax") && !name.equals("beeswax"))
                                                                                                                                                if (name.equals("itemBeeComb") || name.equals("beeComb")) {
                                                                                                                                                    OreDictUnificator.addAssociation(name, ore);
                                                                                                                                                } else if (!name.equals("itemForcicium") && !name.equals("ForciciumItem")) {
                                                                                                                                                    if (!name.equals("itemForcillium"))
                                                                                                                                                        if (!name.equals("brickPeat") && !name.equals("peat"))
                                                                                                                                                            if (!name.equals("itemRoyalJelly") && !name.equals("royalJelly"))
                                                                                                                                                                if (!name.equals("itemHoneydew") && !name.equals("honeydew"))
                                                                                                                                                                    if (!name.equals("itemHoney") && !name.equals("honey"))
                                                                                                                                                                        if (!name.equals("itemPollen") && !name.equals("pollen"))
                                                                                                                                                                            if (!name.equals("itemReedTypha") && !name.equals("reedTypha"))
                                                                                                                                                                                if (!name.equals("itemSulfuricAcid") && !name.equals("sulfuricAcid"))
                                                                                                                                                                                    if (name.equals("itemRubber") || name.equals("rubber")) {
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("copperCableItem", 1), ore, ModHandler.getIC2Item("insulatedCopperCableItem", 1), 100, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("goldCableItem", 1), new ItemStack(item, 2, meta), ModHandler.getIC2Item("doubleInsulatedGoldCableItem", 1), 200, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("ironCableItem", 1), new ItemStack(item, 3, meta), ModHandler.getIC2Item("trippleInsulatedIronCableItem", 1), 300, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("insulatedGoldCableItem", 1), ore, ModHandler.getIC2Item("doubleInsulatedGoldCableItem", 1), 100, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("insulatedIronCableItem", 1), new ItemStack(item, 2, meta), ModHandler.getIC2Item("trippleInsulatedIronCableItem", 1), 200, 2);
                                                                                                                                                                                        GregTech_API.sRecipeAdder.addAssemblerRecipe(ModHandler.getIC2Item("doubleInsulatedIronCableItem", 1), ore, ModHandler.getIC2Item("trippleInsulatedIronCableItem", 1), 100, 2);
                                                                                                                                                                                    } else if (!name.equals("itemPotash") && !name.equals("potash")) {
                                                                                                                                                                                        if (!name.equals("itemCompressedCarbon") && !name.equals("compressedCarbon"))
                                                                                                                                                                                            if (name.equals("itemManganese") || name.equals("manganese")) {
                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(12, 1), null, 0, false);
                                                                                                                                                                                            } else if (name.equals("itemMagnesium") || name.equals("magnesium")) {
                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(13, 1), null, 0, false);
                                                                                                                                                                                            } else if (name.equals("itemPhosphorite") || name.equals("phosphorite") || name.equals("itemPhosphorus") || name.equals("phosphorus")) {
                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.get("dustPhosphorus", 1), null, 0, false);
                                                                                                                                                                                            } else if (!name.equals("itemBitumen") && !name.equals("bitumen")) {
                                                                                                                                                                                                if (!name.equals("itemBioFuel"))
                                                                                                                                                                                                    if (!name.equals("itemEnrichedAlloy"))
                                                                                                                                                                                                        if (name.equals("itemQuicksilver") || name.equals("quicksilver")) {
                                                                                                                                                                                                            GregTech_API.sRecipeAdder.addCannerRecipe(ore, GtUtil.emptyCell, GT_Metitem_Cell.instance.getStack(16, 1), null, 100, 1);
                                                                                                                                                                                                        } else if (!name.equals("chunkOsmium") && !name.equals("itemOsmium") && !name.equals("osmium")) {
                                                                                                                                                                                                            if (name.equals("sandOil") || name.equals("oilsandsOre")) {
                                                                                                                                                                                                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 2, meta), 1, GT_Metitem_Cell.instance.getStack(17, 1), new ItemStack(Block.sand, 1, 0), null, null, 1000);
                                                                                                                                                                                                            } else if (name.equals("itemSulfur") || name.equals("sulfur")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(8, 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("itemAluminum") || name.equals("aluminum") || name.equals("itemAluminium") || name.equals("aluminium")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(18, 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("itemSaltpeter") || name.equals("saltpeter")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, GT_Metitem_Dust.instance.getStack(9, 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("itemUranium") || name.equals("uranium")) {
                                                                                                                                                                                                                ModHandler.addPulverisationRecipe(ore, OreDictUnificator.get("dustUranium", 1), null, 0, false);
                                                                                                                                                                                                            } else if (name.equals("sandCracked")) {
                                                                                                                                                                                                                if (item.itemID < 4096)
                                                                                                                                                                                                                    GregTech_API.sRecipeAdder.addJackHammerMinableBlock(Block.blocksList[item.itemID]);
                                                                                                                                                                                                                if (item instanceof net.minecraft.item.ItemBlock && item.getItemStackLimit() > GT_Mod.sBlockStackSize)
                                                                                                                                                                                                                    item.setMaxStackSize(GT_Mod.sBlockStackSize);
                                                                                                                                                                                                                GregTech_API.sRecipeAdder.addCentrifugeRecipe(new ItemStack(item, 16, meta), -1, ModHandler.getFuelCan(25000), GT_Metitem_Dust.instance.getStack(9, 8), null, new ItemStack(Block.sand, 10), 2500);
                                                                                                                                                                                                            } else if (name.startsWith("item")) {
                                                                                                                                                                                                                System.out.println("Item Name: " + name + " !!!Unknown Item detected!!! Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information.");
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                System.out.println("Thingy Name: " + name + " !!!Unknown 'Thingy' detected!!! This Object seems to probably not follow a valid OreDictionary Convention, or I missed a Convention. Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information.");
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                            }
                                                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                    }
                                                                                                            }
                                                                    }
                                                    }
                    }
            }
        }*/
    }

    private void registerOreRecipes(ItemStack stack, String eventName, OreDictionary.OreRegisterEvent event) {
        ResourceLocation recipeGroup = new ResourceLocation(Reference.MODID, "ores");

        boolean nether = false, end = false, dense = false;
        if (eventName.startsWith("oreDense")) {
            eventName = eventName.replaceFirst("oreDense", "ore");
            dense = true;
        }
        if (eventName.startsWith("oreNether")) {
            eventName = eventName.replaceFirst("oreNether", "ore");
            nether = true;
        }
        if (eventName.startsWith("oreEnd")) {
            eventName = eventName.replaceFirst("oreEnd", "ore");
            end = true;
        }

        String dustName = eventName.replaceFirst("ore", "dust");
        ItemStack ore = OreDictUnificator.getFirstOre(dustName);
        Item item = stack.getItem();

        if (!ore.isEmpty() && item instanceof ItemBlock) {
            GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MODID, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, dustName)),
                    recipeGroup,
                    ore,
                    "T", "O", 'T', "craftingToolHardHammer", 'O', eventName);
        }

        if (item instanceof ItemBlock && stack.getMaxStackSize() > GregTechConfig.FEATURES.maxOtherBlockStackSize) item.setMaxStackSize(GregTechConfig.FEATURES.maxOtherBlockStackSize);

        /*if (eventName.equals("oreLapis")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(Item.dyePowder, (dense || nether) ? 18 : 12, 4), GT_Metitem_Dust.instance.getStack(2, 3), null, ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, new ItemStack(Item.dyePowder, 12, 4), GT_Metitem_Dust.instance.getStack(2, 1), 0, true);
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
        } else if (eventName.equals("oreSodalite")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(5, (dense || nether) ? 18 : 12), GT_Metitem_Dust.instance.getStack(18, 3), null, ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(5, 12), GT_Metitem_Dust.instance.getStack(18, 1), 0, false);
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
        } else if (eventName.equals("oreRedstone")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(Item.redstone, (dense || nether) ? 15 : 10), GT_Metitem_SmallDust.instance.getStack(250, 2), null, ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, new ItemStack(Item.redstone, 10), new ItemStack(Item.lightStoneDust, 1), 0, true);
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
        } else if (eventName.equals("oreQuartz")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(Item.netherQuartz, 1), GT_OreDictUnificator.get("dustNetherQuartz", new ItemStack(Item.netherQuartz, 3), 3), GT_OreDictUnificator.get("dustNetherrack", 1), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustNetherQuartz", new ItemStack(Item.netherQuartz, 3), 3), GT_OreDictUnificator.get("dustNetherrack", 1), 0, false);
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
        } else if (eventName.equals("oreNikolite")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.getFirstOre("dustNikolite", (dense || nether) ? 18 : 12), GT_Metitem_SmallDust.instance.getStack(36, (dense || nether) ? 2 : 1), null, ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.getFirstOre("dustNikolite", (dense || nether) ? 15 : 10), GT_Metitem_Dust.instance.getStack(36, 1), 0, false);
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
        } else if (eventName.equals("oreIron")) {
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustIron", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustNickel", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotIron", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustIron", (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(244, 1), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addBlastRecipe(stack, GT_Metitem_Cell.instance.getStack(33, 1), GT_OreDictUnificator.get("ingotRefinedIron", (dense || nether) ? 5 : 3), ModHandler.getEmptyCell(1), 100, 128, 1000);
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
        } else if (eventName.equals("oreGold")) {
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustGold", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustCopper", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotGold", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustGold", (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(243, 1), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_OreDictUnificator.get("dustGold", (dense || nether) ? 5 : 3), GT_Metitem_SmallDust.instance.getStack(243, 1), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(32, 1), GT_OreDictUnificator.get("dustGold", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustCopper", 1), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
        } else if (eventName.equals("oreSilver")) {
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustSilver", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustLead", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotSilver", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustSilver", (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(23, 2), null, ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_OreDictUnificator.get("dustSilver", (dense || nether) ? 5 : 3), GT_Metitem_SmallDust.instance.getStack(23, 2), null, ModHandler.getEmptyCell(1));
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
        } else if (eventName.equals("oreLead")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(23, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSilver", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotLead", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(23, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(246, 1), null, ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_OreDictUnificator.get("dustLead", (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(246, 1), null, ModHandler.getEmptyCell(1));
        } else if (eventName.equals("oreSilverLead") || eventName.equals("oreGalena")) {
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustGalena", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustSulfur", 1), (dense || nether) ? 100 : 50, false);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustGalena", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSulfur", (dense || nether) ? 2 : 1), null, ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_OreDictUnificator.get("dustGalena", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSulfur", (dense || nether) ? 2 : 1), GT_OreDictUnificator.get("dustSilver", (dense || nether) ? 2 : 1), ModHandler.getEmptyCell(1));
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
        } else if (eventName.equals("oreDiamond")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(Item.diamond, (dense || nether) ? 2 : 1), GT_Metitem_SmallDust.instance.getStack(36, 6), ModHandler.getIC2Item("hydratedCoalDust", 1), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(36, (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustCoal", 1), 0, true);
            ModHandler.addValuableOre(item.itemID, aMeta, 5);
        } else if (eventName.equals("oreEmerald")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(Item.emerald, (dense || nether) ? 2 : 1), GT_Metitem_SmallDust.instance.getStack(35, (dense || nether) ? 12 : 6), GT_Metitem_SmallDust.instance.getStack(37, 2), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(35, (dense || nether) ? 3 : 2), GT_Metitem_Dust.instance.getStack(37, 1), 0, true);
            ModHandler.addValuableOre(item.itemID, aMeta, 5);
        } else if (eventName.equals("oreRuby")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("gemRuby", (dense || nether) ? 2 : 1), GT_Metitem_SmallDust.instance.getStack(32, (dense || nether) ? 12 : 6), GT_Metitem_SmallDust.instance.getStack(54, 2), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(32, (dense || nether) ? 3 : 2), GT_Metitem_Dust.instance.getStack(54, 1), 0, true);
        } else if (eventName.equals("oreSapphire")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("gemSapphire", (dense || nether) ? 2 : 1), GT_Metitem_SmallDust.instance.getStack(33, (dense || nether) ? 12 : 6), GT_Metitem_SmallDust.instance.getStack(34, 2), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(33, (dense || nether) ? 3 : 2), GT_Metitem_Dust.instance.getStack(34, 1), 0, true);
        } else if (eventName.equals("oreGreenSapphire")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("gemGreenSapphire", (dense || nether) ? 2 : 1), GT_Metitem_SmallDust.instance.getStack(34, (dense || nether) ? 12 : 6), GT_Metitem_SmallDust.instance.getStack(33, 2), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(34, (dense || nether) ? 3 : 2), GT_Metitem_Dust.instance.getStack(33, 1), 0, true);
        } else if (eventName.equals("oreOlivine")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Material.instance.getStack(37, (dense || nether) ? 2 : 1), GT_Metitem_SmallDust.instance.getStack(37, (dense || nether) ? 12 : 6), GT_Metitem_SmallDust.instance.getStack(35, 2), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(37, (dense || nether) ? 3 : 2), GT_Metitem_Dust.instance.getStack(35, 1), 0, true);
        } else if (eventName.equals("oreCoal")) {
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(Item.coal, (dense || nether) ? 2 : 1), GT_OreDictUnificator.get("dustCoal", (dense || nether) ? 2 : 1), GT_OreDictUnificator.get("dustSmallThorium", 1), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustCoal", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustThorium", 1), 0, true);
            ModHandler.addValuableOre(item.itemID, aMeta, 1);
        } else if (eventName.equals("oreCopper")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustGold", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotCopper", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(242, 1), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(32, 1), GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 5 : 3), GT_Metitem_SmallDust.instance.getStack(242, 1), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustGold", 1), null, ModHandler.getEmptyCell(1));
        } else if (eventName.equals("oreTin")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustTin", (dense || nether) ? 3 : 2), GT_OreDictUnificator.get("dustIron", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotTin", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustTin", (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(241, 1), GT_Metitem_SmallDust.instance.getStack(24, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(32, 1), GT_OreDictUnificator.get("dustTin", (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(241, 1), GT_Metitem_Dust.instance.getStack(24, 1), ModHandler.getEmptyCell(1));
        } else if (eventName.equals("oreZinc")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 2);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(24, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(244, 2), null, ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(32, 1), GT_Metitem_Dust.instance.getStack(24, (dense || nether) ? 6 : 3), GT_Metitem_SmallDust.instance.getStack(244, 2), null, ModHandler.getEmptyCell(1));
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_Metitem_Material.instance.getStack(24, 1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(24, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustTin", 1), 0, false);
        } else if (eventName.equals("oreCassiterite")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustTin", (dense || nether) ? 6 : 3), GT_OreDictUnificator.get("dustTin", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotTin", 2));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustTin", (dense || nether) ? 10 : 5), null, null, ModHandler.getEmptyCell(1));
        } else if (eventName.equals("oreTetrahedrite")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustTetrahedrite", GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 4 : 2), (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustZinc", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotCopper", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(32, 1), GT_OreDictUnificator.get("dustTetrahedrite", GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 5 : 3), (dense || nether) ? 5 : 3), GT_OreDictUnificator.get("dustSmallZinc", 2), GT_OreDictUnificator.get("dustSmallAntimony", GT_OreDictUnificator.get("dustSmallIron", 2), 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustTetrahedrite", GT_OreDictUnificator.get("dustCopper", (dense || nether) ? 4 : 2), (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSmallZinc", 2), GT_OreDictUnificator.get("dustSmallAntimony", GT_OreDictUnificator.get("dustSmallIron", 2), 1), ModHandler.getEmptyCell(1));
        } else if (eventName.equals("oreAntimony")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustAntimony", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustZinc", 1), 0, false);
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_OreDictUnificator.get("ingotAntimony", 1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustAntimony", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSmallZinc", 2), GT_OreDictUnificator.get("dustSmallIron", 2), ModHandler.getEmptyCell(1));
        } else if (eventName.equals("oreIridium")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 10);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, ModHandler.getIC2Item("iridiumOre", (dense || nether) ? 3 : 2, GT_OreDictUnificator.getFirstOre("dustIridium", (dense || nether) ? 3 : 2)), GT_Metitem_SmallDust.instance.getStack(27, 2), null, ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), ModHandler.getIC2Item("iridiumOre", (dense || nether) ? 3 : 2, GT_OreDictUnificator.getFirstOre("dustIridium", (dense || nether) ? 3 : 2)), GT_Metitem_Dust.instance.getStack(27, 1), null, ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, ModHandler.getIC2Item("iridiumOre", (dense || nether) ? 3 : 2, GT_OreDictUnificator.getFirstOre("dustIridium", (dense || nether) ? 3 : 2)), GT_Metitem_Dust.instance.getStack(27, 1), 0, true);
        } else if (eventName.equals("oreCooperite") || eventName.equals("oreSheldonite")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 10);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(27, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(28, 1), GT_OreDictUnificator.get("nuggetIridium", 2), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_Metitem_Dust.instance.getStack(27, (dense || nether) ? 6 : 3), GT_Metitem_Dust.instance.getStack(28, 1), GT_OreDictUnificator.get("nuggetIridium", 2), ModHandler.getEmptyCell(1));
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_Metitem_Material.instance.getStack(27, 1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(27, (dense || nether) ? 4 : 2), ModHandler.getIC2Item("iridiumOre", 1, GT_OreDictUnificator.getFirstOre("dustIridium", 1)), 0, true);
        } else if (eventName.equals("orePlatinum")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 7);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(27, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("nuggetIridium", 2), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_Metitem_Dust.instance.getStack(27, (dense || nether) ? 6 : 3), GT_OreDictUnificator.get("nuggetIridium", 2), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_Metitem_Material.instance.getStack(27, 1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(27, (dense || nether) ? 4 : 2), ModHandler.getIC2Item("iridiumOre", 1, GT_OreDictUnificator.getFirstOre("dustIridium", 1)), 0, false);
        } else if (eventName.equals("oreNickel")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 4);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(28, (dense || nether) ? 6 : 3), GT_Metitem_SmallDust.instance.getStack(27, 1), GT_Metitem_SmallDust.instance.getStack(243, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(16, 1), GT_Metitem_Dust.instance.getStack(28, (dense || nether) ? 6 : 3), GT_Metitem_Dust.instance.getStack(27, 1), null, ModHandler.getEmptyCell(1));
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GT_Metitem_Material.instance.getStack(28, 1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(28, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustTin", 1), 0, false);
        } else if (eventName.equals("orePyrite")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 1);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(3, 5), GT_Metitem_Dust.instance.getStack(8, 2), null, ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addBlastRecipe(stack, GT_Metitem_Cell.instance.getStack(33, 1), GT_OreDictUnificator.get("ingotRefinedIron", 2), ModHandler.getEmptyCell(1), 100, 128, 1500);
            ModHandler.addInductionSmelterRecipe(stack, new ItemStack(Block.sand, 1), new ItemStack(Item.ingotIron, 1), ModHandler.getTEItem("slagRich", 1), 300, 10);
            ModHandler.addInductionSmelterRecipe(stack, ModHandler.getTEItem("slagRich", 1), new ItemStack(Item.ingotIron, 2), ModHandler.getTEItem("slag", 1), 300, 95);
            ModHandler.addOreToIngotSmeltingRecipe(stack, new ItemStack(Item.ingotIron, 1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(3, 5), GT_OreDictUnificator.get("dustIron", 1), 0, true);
        } else if (eventName.equals("oreCinnabar")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 3);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(11, 5), GT_Metitem_SmallDust.instance.getStack(249, 2), GT_Metitem_SmallDust.instance.getStack(250, 1), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(11, 3), new ItemStack(Item.redstone, 1), 0, true);
        } else if (eventName.equals("oreSphalerite")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 2);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(14, 5), GT_Metitem_Dust.instance.getStack(24, 1), GT_Metitem_SmallDust.instance.getStack(55, 1), ModHandler.getEmptyCell(1));
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, GT_Metitem_Cell.instance.getStack(32, 1), GT_Metitem_Dust.instance.getStack(14, 5), GT_Metitem_Dust.instance.getStack(24, 3), GT_Metitem_SmallDust.instance.getStack(55, 1), ModHandler.getEmptyCell(1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(14, 4), GT_Metitem_Dust.instance.getStack(24, 1), 0, true);
        } else if (eventName.equals("oreAluminium") || eventName.equals("oreAluminum")) {
            ModHandler.addValuableOre(item.itemID, aMeta, 2);
            GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(18, (dense || nether) ? 6 : 3), GT_Metitem_SmallDust.instance.getStack(19, 1), null, ModHandler.getEmptyCell(1));
            if (!dense && !nether)
                ModHandler.addOreToIngotSmeltingRecipe(stack, GregTech_API.sConfiguration.addAdvConfig("blastfurnacerequirements", "aluminium", true) ? GT_OreDictUnificator.get("dustAluminium", 1) : GT_OreDictUnificator.get("ingotAluminium", 1));
            ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(18, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(19, 1), 0, true);
        } else if (!eventName.equals("oreNaturalAluminium") && !eventName.equals("oreNaturalAluminum")) {
            if (eventName.equals("oreSteel")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustSteel", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSmallNickel", 2), null, ModHandler.getEmptyCell(1));
                if (!dense && !nether)
                    ModHandler.addOreToIngotSmeltingRecipe(stack, GregTech_API.sConfiguration.addAdvConfig("blastfurnacerequirements", "steel", true) ? GT_OreDictUnificator.get("dustSteel", 1) : GT_OreDictUnificator.get("ingotSteel", 1));
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustSteel", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustNickel", 2), 0, true);
            } else if (eventName.equals("oreTitan") || eventName.equals("oreTitanium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 5);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(19, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(18, 2), null, ModHandler.getEmptyCell(1));
                if (!dense && !nether)
                    ModHandler.addOreToIngotSmeltingRecipe(stack, GregTech_API.sConfiguration.addAdvConfig("blastfurnacerequirements", "titanium", true) ? GT_OreDictUnificator.get("dustTitanium", 1) : GT_OreDictUnificator.get("ingotTitanium", 1));
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(19, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(18, 1), 0, true);
            } else if (eventName.equals("oreChrome") || eventName.equals("oreChromium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 10);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(20, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(32, 1), null, ModHandler.getEmptyCell(1));
                if (!dense && !nether)
                    ModHandler.addOreToIngotSmeltingRecipe(stack, GregTech_API.sConfiguration.addAdvConfig("blastfurnacerequirements", "chrome", true) ? GT_OreDictUnificator.get("dustChrome", 1) : GT_OreDictUnificator.get("ingotChrome", 1));
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(20, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(32, 1), 0, true);
            } else if (eventName.equals("oreElectrum")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 5);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(21, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(242, 1), GT_Metitem_SmallDust.instance.getStack(246, 1), ModHandler.getEmptyCell(1));
                if (!dense && !nether)
                    ModHandler.addOreToIngotSmeltingRecipe(stack, GT_Metitem_Material.instance.getStack(21, 1));
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Material.instance.getStack(21, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustGold", 1), 0, false);
            } else if (eventName.equals("oreTungsten") || eventName.equals("oreTungstate")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(22, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(12, 1), 0, true);
                if (!dense && !nether)
                    ModHandler.addOreToIngotSmeltingRecipe(stack, GregTech_API.sConfiguration.addAdvConfig("blastfurnacerequirements", "tungsten", true) ? GT_OreDictUnificator.get("dustTungsten", 1) : GT_OreDictUnificator.get("ingotTungsten", 1));
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(22, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(241, 3), GT_Metitem_SmallDust.instance.getStack(12, 3), ModHandler.getEmptyCell(1));
            } else if (eventName.equals("oreBauxite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(17, (dense || nether) ? 8 : 4), GT_Metitem_Dust.instance.getStack(18, (dense || nether) ? 2 : 1), null, ModHandler.getEmptyCell(1));
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(17, (dense || nether) ? 8 : 4), GT_Metitem_Dust.instance.getStack(18, 1), 0, true);
            } else if (eventName.equals("oreApatite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 1);
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.getFirstOre("gemApatite", (dense || nether) ? 16 : 8), GT_OreDictUnificator.get("dustPhosphorus", 1), 0, false);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.getFirstOre("gemApatite", (dense || nether) ? 24 : 12), GT_OreDictUnificator.get("dustPhosphorus", 1), null, ModHandler.getEmptyCell(1));
            } else if (eventName.equals("oreSulfur")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 1);
                ModHandler.addSmeltingRecipe(stack, GT_OreDictUnificator.get("dustSulfur", 3));
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(8, (dense || nether) ? 16 : 8), GT_OreDictUnificator.get("dustSulfur", 1), 0, false);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(8, (dense || nether) ? 20 : 10), null, null, ModHandler.getEmptyCell(1));
            } else if (eventName.equals("oreSaltpeter")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
                ModHandler.addSmeltingRecipe(stack, GT_OreDictUnificator.get("dustSaltpeter", 3));
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(9, (dense || nether) ? 10 : 5), GT_OreDictUnificator.get("dustSaltpeter", 1), 0, false);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(9, (dense || nether) ? 14 : 7), null, null, ModHandler.getEmptyCell(1));
            } else if (eventName.equals("orePhosphorite")) {
                ModHandler.addSmeltingRecipe(stack, GT_OreDictUnificator.get("dustPhosphorus", 2));
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustPhosphorus", (dense || nether) ? 6 : 3), GT_OreDictUnificator.get("dustPhosphorus", 1), 0, false);
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreMagnesium")) {
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(13, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(241, 2), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(13, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(28, 1), 0, false);
            } else if (eventName.equals("oreManganese")) {
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(12, (dense || nether) ? 4 : 2), GT_Metitem_SmallDust.instance.getStack(241, 2), GT_Metitem_SmallDust.instance.getStack(28, 1), ModHandler.getEmptyCell(1));
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(12, (dense || nether) ? 4 : 2), GT_Metitem_Dust.instance.getStack(28, 1), 0, false);
            } else if (eventName.equals("oreMonazit") || eventName.equals("oreMonazite")) {
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, new ItemStack(ModHandler.mForcicium.getItem(), (dense || nether) ? 20 : 10, ModHandler.mForcicium.getItemDamage()), GT_OreDictUnificator.get("dustThorium", 2), null, ModHandler.getEmptyCell(1));
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreFortronite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreThorium")) {
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustThorium", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustUranium", 1), 0, true);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustThorium", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSmallUranium", 1), null, ModHandler.getEmptyCell(1));
                ModHandler.addValuableOre(item.itemID, aMeta, 5);
            } else if (eventName.equals("orePlutonium")) {
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.get("dustPlutonium", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustUranium", 1), 0, true);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.get("dustPlutonium", (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustUranium", 1), null, ModHandler.getEmptyCell(1));
                ModHandler.addValuableOre(item.itemID, aMeta, 15);
            } else if (eventName.equals("oreOsmium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 20);
            } else if (eventName.equals("oreEximite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreMeutoite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("orePrometheum")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreDeepIron")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreDeepIron")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreInfuscolium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreOureclase")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreAredrite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreAstral Silver")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
            } else if (eventName.equals("oreAstralSilver")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
            } else if (eventName.equals("oreCarmot")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
            } else if (eventName.equals("oreMithril")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
            } else if (eventName.equals("oreRubracium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreOrichalcum")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreAdamantine")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 5);
            } else if (eventName.equals("oreAtlarus")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreIgnatius")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreShadow Iron")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
            } else if (eventName.equals("oreShadowIron")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
            } else if (eventName.equals("oreMidasium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreVyroxeres")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreCeruclase")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreKalendrite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreVulcanite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreSanguinite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreLemurite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreAdluorite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreNaquadah")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 8);
            } else if (eventName.equals("oreBitumen")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreForce")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
            } else if (eventName.equals("oreCertusQuartz")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 4);
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.getFirstOre("dustCertusQuartz", 5), GT_OreDictUnificator.getFirstOre("crystalCertusQuartz", 1), 0, true);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.getFirstOre("crystalCertusQuartz", 2), GT_OreDictUnificator.getFirstOre("dustCertusQuartz", 3), null, ModHandler.getEmptyCell(1));
            } else if (eventName.equals("oreVinteum")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 3);
                ModHandler.addPulverisationRecipe(stack, GT_OreDictUnificator.getFirstOre("dustVinteum", 2), GT_OreDictUnificator.getFirstOre("dustVinteum", 1), 0, true);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_OreDictUnificator.getFirstOre("dustVinteum", 3), null, null, ModHandler.getEmptyCell(1));
            } else if (eventName.equals("orePotash")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreArdite")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreCobalt")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 2);
            } else if (eventName.equals("oreUranium")) {
                ModHandler.addValuableOre(item.itemID, aMeta, 5);
                ModHandler.addPulverisationRecipe(stack, GT_Metitem_Dust.instance.getStack(16, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustPlutonium", 1), 0, false);
                GregTech_API.sRecipeAdder.addGrinderRecipe(stack, -1, GT_Metitem_Dust.instance.getStack(16, (dense || nether) ? 4 : 2), GT_OreDictUnificator.get("dustSmallPlutonium", 2), GT_OreDictUnificator.get("dustThorium", 1), ModHandler.getEmptyCell(1));
            } else {
                ModHandler.addValuableOre(item.itemID, aMeta, 1);
                System.out.println("Ore Name: " + event.Name + " !!!Unknown Ore detected!!! Please report to GregTech Intergalactical for additional compatiblity. This is not an Error, it's just an Information. This Ore will still get added to the List of the IC2-Miner, but with a low Value.");
            }
        }*/
    }
}
