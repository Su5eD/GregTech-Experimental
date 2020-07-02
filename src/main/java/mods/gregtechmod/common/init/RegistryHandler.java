package mods.gregtechmod.common.init;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.objects.items.Materials;
import mods.gregtechmod.common.util.IFluidModel;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : BlockInit.BLOCKS) {
            event.getRegistry().register(block);
        }
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Materials.register();
        for (Item item : ItemInit.ITEMS.values()) {
            event.getRegistry().register(item);
        }
    }

    public static void registerFluids() {
        for (Fluid fluid : FluidInit.FLUIDS) {
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for(Block block : BlockInit.BLOCKS) {
            if(block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
            else if (block instanceof IFluidModel) ((IFluidModel)block).registerCustomMeshAndState();
        }
        for(Item item : ItemInit.ITEMS.values()) {
            if(item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
    }

    @SubscribeEvent
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
        map.registerSprite(new ResourceLocation(GregtechMod.MODID, path+"item_valve"));
    }
}
