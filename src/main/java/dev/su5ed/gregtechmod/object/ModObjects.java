package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTab;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.cover.ModCoverType;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import one.util.streamex.StreamEx;

import java.util.function.Supplier;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class ModObjects {
    public static final ModObjects INSTANCE = new ModObjects();
    
    public static Supplier<IForgeRegistry<CoverType>> coverRegistry;

    private ModObjects() {}

    public static Item.Properties itemProperties() {
        return new Item.Properties().tab(GregTechTab.INSTANCE);
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event) {
        RegistryBuilder<CoverType> builder = new RegistryBuilder<CoverType>()
            .setName(location("covers"))
            .setType(CoverType.class)
            .setMaxID(Integer.MAX_VALUE - 1);
        coverRegistry = event.create(builder);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        StreamEx.<BlockItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(GTBlockEntity.values())
            .map(BlockItemProvider::getBlock)
            .forEach(registry::register);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        StreamEx.<ItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(Ingot.values())
            .append(Nugget.values())
            .append(Rod.values())
            .append(Dust.values())
            .append(Smalldust.values())
            .append(Plate.values())
            .append(TurbineRotor.values())
            .append(Component.values())
            .append(GTBlockEntity.values())
            .append(ModCoverItem.values())
            .append(Tool.values())
            .append(Upgrade.values())
            .map(ItemProvider::getItem)
            .forEach(registry::register);
    }

    @SubscribeEvent
    public void registerBlockEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        IForgeRegistry<BlockEntityType<?>> registry = event.getRegistry();

        StreamEx.<BlockEntityProvider>of(GTBlockEntity.values())
            .map(BlockEntityProvider::getType)
            .forEach(registry::register);
    }

    @SubscribeEvent
    public void registerCovers(RegistryEvent.Register<CoverType> event) {
        IForgeRegistry<CoverType> registry = event.getRegistry();
        StreamEx.of(ModCoverType.values())
            .map(ModCoverType::get)
            .forEach(registry::register);
    }
}
