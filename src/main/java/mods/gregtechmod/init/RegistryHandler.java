package mods.gregtechmod.init;

import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.core.GregtechTeBlock;
import mods.gregtechmod.cover.RenderTeBlock;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.JsonHandler;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BlockItemLoader.init();
        event.getRegistry().registerAll(BlockItemLoader.BLOCKS.toArray(new Block[0]));
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(BlockItemLoader.ITEMS.toArray(new Item[0]));
    }

    public static void registerFluids() {
        FluidLoader.init();
        GregtechMod.LOGGER.info("Registering fluids");
        FluidLoader.FLUIDS.forEach(fluid -> {
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        });
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        BlockItemLoader.BLOCKS
                .forEach(block -> registerModel(Item.getItemFromBlock(block)));

        BlockItemLoader.ITEMS.stream()
                .filter(item -> item instanceof IModelInfoProvider)
                .forEach(item -> {
                    ModelInformation info = ((IModelInfoProvider) item).getModelInformation();
                    registerModel(item, info.metadata, info.path);
                });
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
        GregtechMod.LOGGER.info("Registering baked models");
        BakedModelLoader loader = new BakedModelLoader();
        for (GregtechTeBlock teBlock : GregtechTeBlock.values()) {
            try {
                if (teBlock.hasBakedModel()) {
                    String name = teBlock.getName();
                    JsonHandler json = new JsonHandler(name);
                    loader.register("models/block/"+name, new RenderTeBlock(json.textures, json.particle));
                    if (teBlock.hasActive()) {
                        json = new JsonHandler(name+"_active");
                        loader.register("models/block/"+name+"_active", new RenderTeBlock(json.textures, json.particle));
                    }
                }
            } catch (Exception e) {
                GregtechMod.LOGGER.error(e.getMessage());
            }
        }
        ModelLoaderRegistry.registerLoader(loader);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerIcons(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        String path = "blocks/covers/";
        String centrifuge = "blocks/machines/centrifuge/";
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"adv_machine_vent"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"adv_machine_vent_rotating"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, centrifuge+"centrifuge_top_active2"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, centrifuge+"centrifuge_top_active3"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, centrifuge+"centrifuge_side_active2"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, centrifuge+"centrifuge_side_active3"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, "blocks/machines/adv_machine_screen_random")); //TODO: Remove when implemented in another machine
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"machine_vent_rotating"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"drain"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"active_detector"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"eu_meter"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"item_meter"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"liquid_meter"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"normal"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"noredstone"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"machine_controller"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"solar_panel"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"crafting"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"conveyor"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"pump"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"valve"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"energy_only"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"redstone_only"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"redstone_conductor"));
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"redstone_signalizer"));

        for (FluidLoader.Liquids type : FluidLoader.Liquids.values()) {
            map.registerSprite(type.texture);
        }
        for (FluidLoader.Gases type : FluidLoader.Gases.values()) {
            map.registerSprite(type.texture);
        }
    }
}
