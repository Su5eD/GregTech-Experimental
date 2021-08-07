package mods.gregtechmod.compat;

import buildcraft.api.facades.FacadeAPI;
import buildcraft.silicon.plug.FacadeStateManager;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.init.FluidLoader;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.GtUtil;
import mods.railcraft.api.fuel.FluidFuelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class ModCompat {
    
    public static void disableCasingFacades() {
        if (ModHandler.buildcraftSilicon) sendFakeIMC();
    }
    
    @SuppressWarnings("deprecation")
    @Optional.Method(modid = "buildcraftsilicon")
    private static void sendFakeIMC() {
        Constructor<IMCMessage> imcContructor = ObfuscationReflectionHelper.findConstructor(IMCMessage.class, String.class, Object.class);
        Field senderField = ReflectionHelper.findField(IMCMessage.class, "sender");
        
        Stream.of(BlockItems.Block.STANDARD_MACHINE_CASING, BlockItems.Block.REINFORCED_MACHINE_CASING, BlockItems.Block.ADVANCED_MACHINE_CASING, BlockItems.Block.IRIDIUM_REINFORCED_TUNGSTEN_STEEL, BlockItems.Block.TUNGSTEN_STEEL)
                .map(BlockItems.Block::getInstance)
                .map(IForgeRegistryEntry.Impl::getRegistryName)
                .forEach(name -> {
                    try {
                        IMCMessage message = imcContructor.newInstance(FacadeAPI.IMC_FACADE_DISABLE, name);
                        senderField.set(message, Reference.MODID);
                        FacadeStateManager.receiveInterModComms(message);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        GregTechMod.logger.error("Error while trying to disable casing facades", e);
                    }
                });
    }

    public static void registerTools() {
        GregTechMod.logger.info("Registering various tools to be usable on GregTech machines");
        if (ModHandler.projectredCore) {
            ItemStack screwdriver = ModHandler.getPRItem("screwdriver", OreDictionary.WILDCARD_VALUE);
            GregTechAPI.instance().registerScrewdriver(screwdriver);
        }
        if (ModHandler.railcraft) {
            Item ironCrowbar = ModHandler.getItem("railcraft", "tool_crowbar_iron");
            Item steelCrowbar = ModHandler.getItem("railcraft", "tool_crowbar_steel");
            GregTechAPI.instance().registerCrowbar(ironCrowbar);
            GregTechAPI.instance().registerCrowbar(steelCrowbar);
            GregTechAPI.instance().registerTurbineRotor(ModHandler.rcTurbineRotor, 80, 20, 2);
        }
    }

    public static void registerBoilerFuels() {
        if (ModHandler.railcraft) _registerBoilerFuels();
    }

    @Optional.Method(modid = "railcraft")
    private static void _registerBoilerFuels() {
        GregTechMod.logger.info("Adding fuels to Railcraft's boiler");
        
        FluidFuelManager.addFuel(FluidLoader.Gas.HYDROGEN.getFluid(), 2000);
        FluidFuelManager.addFuel(FluidLoader.Gas.METHANE.getFluid(), 3000);
        FluidFuelManager.addFuel(FluidLoader.Liquid.LITHIUM.getFluid(), 24000);
        if (GregTechMod.classic) FluidFuelManager.addFuel(FluidLoader.Liquid.NITRO_COALFUEL.getFluid(), 18000);
    }

    public static void addRollingMachineRecipes() {
        if (!ModHandler.railcraft) return;
        GregTechMod.logger.info("Adding Rolling Machine recipes");

        addRollingMachineRecipe("coil_kanthal", new ItemStack(BlockItems.Component.COIL_KANTHAL.getInstance(), 3), "AAA", "BCC", "BBC", 'A', GregTechMod.classic ? "ingotRefinedIron" : "ingotIron", 'B', "ingotChrome", 'C', "ingotAluminium");
        addRollingMachineRecipe("coil_nichrome", new ItemStack(BlockItems.Component.COIL_NICHROME.getInstance()), " B ", "BAB", " B ", 'A', "ingotChrome", 'B', "ingotNickel");
        addRollingMachineRecipe("coil_cupronickel", new ItemStack(BlockItems.Component.COIL_CUPRONICKEL.getInstance()), "BAB", "A A", "BAB", 'A', "ingotCopper", 'B', "ingotNickel");
        
        ItemStack railStandard = ModHandler.getRCItem("rail");
        addRollingMachineRecipe("rail_standard", StackUtil.copyWithSize(railStandard, 4), "X X", "X X", "X X", 'X', "ingotAluminium");
        addRollingMachineRecipe("rail_standard_2", StackUtil.copyWithSize(railStandard, 32), "X X", "X X", "X X", 'X', "ingotTitanium");
        addRollingMachineRecipe("rail_standard_3", railStandard, "X X", "X X", "X X", 'X', "ingotTungsten");
        
        ItemStack railReinforced = StackUtil.copyWithSize(railStandard, 32);
        railReinforced.setItemDamage(4);
        addRollingMachineRecipe("rail_reinforced", railStandard, "X X", "X X", "X X", 'X', "ingotTungstenSteel");
        
        ItemStack rebar = ModHandler.getRCItem("rebar");
        addRollingMachineRecipe("rebar", StackUtil.copyWithSize(rebar, 2), "  X", " X ", "X  ", 'X', "ingotAluminium");
        addRollingMachineRecipe("rebar_2", StackUtil.copyWithSize(rebar, 16), "  X", " X ", "X  ", 'X', "ingotTitanium");
        addRollingMachineRecipe("rebar_3", StackUtil.copyWithSize(rebar, 16), "  X", " X ", "X  ", 'X', "ingotTungsten");
        addRollingMachineRecipe("rebar_4", StackUtil.copyWithSize(rebar, 48), "  X", " X ", "X  ", 'X', "ingotTungstenSteel");
        
        ItemStack postMetal = ModHandler.getRCItem("post_metal");
        ItemStack postMetalLightBlue = StackUtil.setSize(GtUtil.copyWithMeta(postMetal, 3), 8);
        addRollingMachineRecipe("post_metal_light_blue", postMetalLightBlue, "XXX", " X ", "XXX", 'X', "ingotAluminium");
        addRollingMachineRecipe("post_metal_light_blue_2", postMetalLightBlue, "X X", "XXX", "X X", 'X', "ingotAluminium");
        
        ItemStack postMetalPurple = StackUtil.setSize(GtUtil.copyWithMeta(postMetal, 10), 64);
        addRollingMachineRecipe("post_metal_purple", postMetalPurple, "XXX", " X ", "XXX", 'X', "ingotTitanium");
        addRollingMachineRecipe("post_metal_purple_2", postMetalPurple, "X X", "XXX", "X X", 'X', "ingotTitanium");
        
        ItemStack postMetalBlack = StackUtil.setSize(GtUtil.copyWithMeta(postMetal, 15), 64);
        addRollingMachineRecipe("post_metal_black", postMetalBlack, "XXX", " X ", "XXX", 'X', "ingotTungsten");
        addRollingMachineRecipe("post_metal_black", postMetalBlack, "X X", "XXX", "X X", 'X', "ingotTungsten");
    }

    public static void addRollingMachineRecipe(String name, ItemStack output, Object... pattern) {
        if (ModHandler.railcraft) ModHandler.addRollingMachineRecipe(name, output, pattern);
        else GameRegistry.addShapedRecipe(
                new ResourceLocation(Reference.MODID, name),
                null,
                output,
                pattern
        );
    }
}
