package mods.gregtechmod.init;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.cover.ICoverProvider;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModCompat;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.Cover;
import mods.gregtechmod.objects.GregTechComponent;
import mods.gregtechmod.objects.GregTechTEBlock;
import mods.gregtechmod.objects.blocks.tileentities.TileEntityLightSource;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Reference.MODID)
public class RegistryHandler {

    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        IForgeRegistry<ICoverProvider> registry = new RegistryBuilder<ICoverProvider>()
            .setName(new ResourceLocation(Reference.MODID, "covers"))
            .setType(ICoverProvider.class)
            .setMaxID(Integer.MAX_VALUE - 1)
            .create();
        JavaUtil.setStaticValue(GregTechAPI.class, "coverRegistry", registry);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        GregTechMod.LOGGER.info("Registering blocks");
        BlockItemLoader.init();
        IForgeRegistry<Block> registry = event.getRegistry();
        Map<String, Block> blocks = BlockItemLoader.getBlocks().stream()
            .peek(registry::register)
            .collect(Collectors.toMap(value -> value.getRegistryName().getPath(), Function.identity()));
        JavaUtil.setStaticValue(GregTechObjectAPI.class, "blocks", blocks);

        GameRegistry.registerTileEntity(TileEntityLightSource.class, new ResourceLocation(Reference.MODID, "light_source"));

        Map<String, ItemStack> teblocks = Arrays.stream(GregTechTEBlock.VALUES)
            .collect(Collectors.toMap(teblock -> teblock.getName().toLowerCase(Locale.ROOT), teblock -> new ItemStack(GregTechTEBlock.blockTE, 1, teblock.getId())));
        JavaUtil.setStaticValue(GregTechObjectAPI.class, "teBlocks", teblocks);

        ModCompat.disableCasingFacades();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        GregTechMod.LOGGER.info("Registering items");
        IForgeRegistry<Item> registry = event.getRegistry();
        Map<String, ItemStack> items = BlockItemLoader.getAllItems().stream()
            .peek(registry::register)
            .collect(Collectors.toMap(value -> value.getRegistryName().getPath(), ItemStack::new));
        JavaUtil.setStaticValue(GregTechObjectAPI.class, "items", items);
    }

    @SubscribeEvent
    public static void registerTEBlocks(TeBlockFinalCallEvent event) {
        GtUtil.withModContainerOverride(FMLCommonHandler.instance().findContainerFor(Reference.MODID), () -> TeBlockRegistry.addAll(GregTechTEBlock.class, GregTechTEBlock.LOCATION));
        TeBlockRegistry.addCreativeRegisterer(GregTechTEBlock.INDUSTRIAL_CENTRIFUGE, GregTechTEBlock.LOCATION);
    }

    @SubscribeEvent
    public static void registerCovers(RegistryEvent.Register<ICoverProvider> event) {
        IForgeRegistry<ICoverProvider> registry = event.getRegistry();
        Arrays.stream(Cover.values())
            .map(cover -> cover.instance.get())
            .forEach(registry::register);
    }

    public static void registerComponents() {
        Arrays.stream(GregTechComponent.values()).forEach(GregTechComponent::register);
    }

    public static void registerFluids() {
        FluidLoader.init();
        GregTechMod.LOGGER.info("Registering fluids");
        for (FluidLoader.IFluidProvider provider : FluidLoader.FLUIDS) {
            if (provider.isFallbackFluid() && FluidRegistry.isFluidRegistered(provider.getName())) continue;
            Fluid fluid = provider.getFluid();
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation location = event.getName();
        String path = location.getPath();
        if (location.getNamespace().equals("minecraft") && path.startsWith("chests")) {
            if (GregTechMod.class.getResource("/assets/" + Reference.MODID + "/loot_tables/" + path + ".json") != null) {
                ResourceLocation name = new ResourceLocation(Reference.MODID, path);
                GregTechMod.LOGGER.info("Loading Loot Table " + name);

                LootTable table = event.getLootTableManager().getLootTableFromLocation(name);
                LootTable vanillaLoot = event.getTable();
                Stream.of("gregtechmod_materials", "gregtechmod_sprays")
                    .map(table::getPool)
                    .filter(Objects::nonNull)
                    .forEach(vanillaLoot::addPool);
            }
        }
    }
}
