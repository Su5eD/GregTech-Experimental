package mods.gregtechmod.init;

import ic2.api.item.IC2Items;
import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechTEBlock;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.render.RenderBlockOre;
import mods.gregtechmod.render.RenderTeBlock;
import mods.gregtechmod.util.IBlockCustomItem;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.JsonHandler;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
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

    private static void registerModel(Item item) {
        registerModel(item, 0, item.getRegistryName());
    }

    private static void registerModel(Item item, int metadata, ResourceLocation path) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(path, "inventory"));
    }

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
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (GtUtil.getFullInvisibility(event.getEntityPlayer())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<String> tooltip = event.getToolTip();

        if (stack.isItemEqual(IC2Items.getItem("dust", "diamond"))) tooltip.add("C128");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "lead"))) tooltip.add("Pb");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "obsidian"))) tooltip.add("MgFeSi2O8");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "coal"))) tooltip.add("C2");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "iron"))) tooltip.add("Fe");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "gold"))) tooltip.add("Au");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "copper"))) tooltip.add("Cu");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "tin"))) tooltip.add("Sn");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "bronze"))) tooltip.add("SnCu3");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "silver"))) tooltip.add("Ag");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "clay"))) tooltip.add("Na2LiAl2Si2");
        else if (stack.isItemEqual(IC2Items.getItem("misc_resource", "ashes"))) tooltip.add("C");

        FluidStack fluidContained = FluidUtil.getFluidContained(stack);
        Item item = stack.getItem();
        if (TileEntityIndustrialCentrifuge.isCell(item) && fluidContained != null) {
            FluidLoader.FLUIDS.forEach(provider -> {
                if (provider.getFluid() == fluidContained.getFluid()) {
                    String description = provider.getDescription();
                    if (description != null) tooltip.add(item instanceof ItemFluidCell ? 2 : 1, description);
                }
            });
        }
    }
}
