package mods.gregtechmod.init;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.core.GregTechTEBlock;
import mods.gregtechmod.objects.blocks.tileentities.TileEntityLightSource;
import mods.gregtechmod.render.RenderBlockOre;
import mods.gregtechmod.render.RenderTeBlock;
import mods.gregtechmod.util.IBlockCustomItem;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.JsonHandler;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

@EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BlockItemLoader.init();
        event.getRegistry().registerAll(BlockItemLoader.BLOCKS.toArray(new Block[0]));
        GameRegistry.registerTileEntity(TileEntityLightSource.class, new ResourceLocation(Reference.MODID, "light_source"));
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(BlockItemLoader.ITEMS.toArray(new Item[0]));
    }

    public static void registerFluids() {
        FluidLoader.init();
        GregTechAPI.logger.info("Registering fluids");
        for (FluidLoader.IFluidProvider provider : FluidLoader.FLUIDS) {
            Fluid fluid = provider.getFluid();
            if (provider.isFallbackFluid() && FluidRegistry.isFluidRegistered(fluid.getName())) continue;
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        BlockItemLoader.BLOCKS
                .forEach(block -> {
                    Item blockItem = Item.getItemFromBlock(block);
                    if (blockItem == Items.AIR) return;
                    if (block instanceof IBlockCustomItem) registerModel(blockItem, 0, ((IBlockCustomItem)block).getItemModel());
                    else registerModel(blockItem);
                });

        BlockItemLoader.ITEMS.stream()
                .filter(item -> item instanceof IModelInfoProvider)
                .forEach(item -> {
                    ModelInformation info = ((IModelInfoProvider) item).getModelInformation();
                    registerModel(item, info.metadata, info.path);
                });
        registerBakedModels();
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item) {
        registerModel(item, 0, item.getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item, int metadata, ResourceLocation path) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(path, "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerBakedModels() {
        GregTechAPI.logger.info("Registering baked models");
        BakedModelLoader loader = new BakedModelLoader();
        for (GregTechTEBlock teBlock : GregTechTEBlock.values()) {
            try {
                if (teBlock.hasBakedModel()) {
                    String name = teBlock.getName();
                    JsonHandler json = new JsonHandler(name, "teblock");
                    loader.register("models/block/"+name, new RenderTeBlock(json.generateMapFromJSON("textures"), json.particle));
                    if (teBlock.hasActive()) {
                        json = new JsonHandler(name+"_active", "teblock");
                        loader.register("models/block/"+name+"_active", new RenderTeBlock(json.generateMapFromJSON("textures"), json.particle));
                    }
                }
            } catch (Exception e) {
                GregTechAPI.logger.error(e.getMessage());
            }
        }
        for (BlockItems.Ore ore : BlockItems.Ore.values()) {
            JsonHandler json = new JsonHandler(ore.name().toLowerCase(Locale.ROOT), "ore");
            loader.register("models/block/ore/"+ore.name().toLowerCase(Locale.ROOT), new RenderBlockOre(json.generateMapFromJSON("textures"), json.generateMapFromJSON("textures_nether"), json.generateMapFromJSON("textures_end"), json.particle));
        }
        ModelLoaderRegistry.registerLoader(loader);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerIcons(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        String path = "blocks/covers/";
        String centrifuge = "blocks/machines/centrifuge/";
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"adv_machine_vent"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"adv_machine_vent_rotating"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_top_active2"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_top_active3"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_side_active2"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_side_active3"));
        map.registerSprite(new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_screen_random")); //TODO: Remove when implemented in another machine
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"machine_vent_rotating"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"drain"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"active_detector"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"eu_meter"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"item_meter"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"liquid_meter"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"normal"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"noredstone"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"machine_controller"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"solar_panel"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"crafting"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"conveyor"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"pump"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"valve"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"energy_only"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"redstone_only"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"redstone_conductor"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"redstone_signalizer"));

        FluidLoader.FLUIDS.forEach(provider -> {
            map.registerSprite(provider.getTexture());
        });
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        String path = event.getName().getPath();
        if (event.getName().getNamespace().equals("minecraft") && path.startsWith("chests")) {
            if (GregTechMod.class.getResource("/assets/"+Reference.MODID+"/loot_tables/"+path+".json") != null) {
                LootTable table = event.getLootTableManager().getLootTableFromLocation(new ResourceLocation(Reference.MODID, event.getName().getPath()));
                LootTable vanillaLoot = event.getTable();
                LootPool materials = table.getPool("gregtechmod_materials");
                LootPool sprays = table.getPool("gregtechmod_sprays");
                if (materials != null) vanillaLoot.addPool(materials);
                if (sprays != null) vanillaLoot.addPool(sprays);
            }
        }
    }
}
