package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTab;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import one.util.streamex.StreamEx;

public final class ModObjects {
    public static final ModObjects INSTANCE = new ModObjects();
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(GregTechTab.INSTANCE);

    private ModObjects() {}

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        StreamEx.of(ModBlock.values())
            .map(ModBlock::getBlock)
            .forEach(registry::register);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        
        StreamEx.<ItemProvider>of(ModBlock.values())
            .append(Ingot.values())
            .append(Nugget.values())
            .map(ItemProvider::getItem)
            .forEach(registry::register);
    }
}
