package mods.gregtechmod.init;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModCompat;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.core.GregTechTEBlock;
import mods.gregtechmod.objects.blocks.tileentities.TileEntityLightSource;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@EventBusSubscriber
public class RegistryHandler {
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        GregTechMod.logger.info("Registering blocks");
        BlockItemLoader.init();
        event.getRegistry().registerAll(BlockItemLoader.BLOCKS.toArray(new Block[0]));
        
        GameRegistry.registerTileEntity(TileEntityLightSource.class, new ResourceLocation(Reference.MODID, "light_source"));
        
        BlockTileEntity blockTE = TeBlockRegistry.get(GregTechTEBlock.LOCATION);
        Map<String, ItemStack> teblocks = Arrays.stream(GregTechTEBlock.VALUES)
                .collect(Collectors.toMap(teblock -> teblock.getName().toLowerCase(Locale.ROOT), teblock -> new ItemStack(blockTE, 1, teblock.getId())));
        GtUtil.setPrivateStaticValue(GregTechObjectAPI.class, "teBlocks", teblocks);
        
        ModCompat.disableCasingFacades();
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        GregTechMod.logger.info("Registering items");
        event.getRegistry().registerAll(BlockItemLoader.ITEMS.toArray(new Item[0]));
    }

    public static void registerFluids() {
        FluidLoader.init();
        GregTechMod.logger.info("Registering fluids");
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
        String path = event.getName().getPath();
        if (event.getName().getNamespace().equals("minecraft") && path.startsWith("chests")) {
            if (GregTechMod.class.getResource("/assets/" + Reference.MODID + "/loot_tables/" + path + ".json") != null) {
                ResourceLocation name = new ResourceLocation(Reference.MODID, event.getName().getPath());
                GregTechMod.logger.info("Loading Loot Table " + name);
                
                LootTable table = event.getLootTableManager().getLootTableFromLocation(name);
                LootTable vanillaLoot = event.getTable();
                LootPool materials = table.getPool("gregtechmod_materials");
                LootPool sprays = table.getPool("gregtechmod_sprays");
                
                if (materials != null) vanillaLoot.addPool(materials);
                if (sprays != null) vanillaLoot.addPool(sprays);
            }
        }
    }
}
