package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTab;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import one.util.streamex.StreamEx;

public final class ModObjects {
    public static final ModObjects INSTANCE = new ModObjects();

    private ModObjects() {}

    public static Item.Properties itemProperties() {
        return new Item.Properties().tab(GregTechTab.INSTANCE);
    }

    @SubscribeEvent
    public void registerBlocks(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, helper -> StreamEx.<BlockItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(GTBlockEntity.values())
            .mapToEntry(BlockItemProvider::getRegistryName, BlockItemProvider::getBlock)
            .forKeyValue(helper::register));

        event.register(ForgeRegistries.Keys.ITEMS, helper -> StreamEx.<ItemProvider>of(ModBlock.values())
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
            .append(Miscellaneous.values())
            .append(ColorSpray.values())
            .append(Wrench.values())
            .append(JackHammer.values())
            .append(Hammer.values())
            .append(Saw.values())
            .append(File.values())
            .append(Cell.values())
            .append(NuclearCoolantPack.values())
            .append(NuclearFuelRod.values())
            .mapToEntry(ItemProvider::getRegistryName, ItemProvider::getItem)
            .forKeyValue(helper::register));

        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> StreamEx.<BlockEntityProvider>of(GTBlockEntity.values())
            .mapToEntry(BlockEntityProvider::getRegistryName, BlockEntityProvider::getType)
            .forKeyValue(helper::register));
    }
}
