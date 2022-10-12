package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTab;
import dev.su5ed.gregtechmod.block.LightSourceBlock;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import dev.su5ed.gregtechmod.util.FluidProvider;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import one.util.streamex.StreamEx;

public final class ModObjects {
    public static final ModObjects INSTANCE = new ModObjects();

    public static final Lazy<Block> LIGHT_SOURCE_BLOCK = Lazy.of(LightSourceBlock::new);

    private ModObjects() {}

    public static Item.Properties itemProperties() {
        return new Item.Properties().tab(GregTechTab.INSTANCE);
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
            StreamEx.<BlockItemProvider>of(ModBlock.values())
                .append(Ore.values())
                .append(GTBlockEntity.values())
                .mapToEntry(BlockItemProvider::getRegistryName, BlockItemProvider::getBlock)
                .forKeyValue(helper::register);

            helper.register("light_source", LIGHT_SOURCE_BLOCK.get());
        });

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
            .append(Armor.values())
            .append(ModFluid.values())
            .mapToEntry(ItemProvider::getRegistryName, ItemProvider::getItem)
            .forKeyValue(helper::register));

        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> StreamEx.<BlockEntityProvider>of(GTBlockEntity.values())
            .mapToEntry(BlockEntityProvider::getRegistryName, BlockEntityProvider::getType)
            .forKeyValue(helper::register));

        event.register(ForgeRegistries.Keys.FLUID_TYPES, helper -> StreamEx.of(ModFluid.values())
            .mapToEntry(FluidProvider::getFluidRegistryName, FluidProvider::getType)
            .forKeyValue(helper::register));
        
        event.register(ForgeRegistries.Keys.FLUIDS, helper -> StreamEx.of(ModFluid.values())
            .forEach(provider -> {
                String name = provider.getName();
                helper.register(name + "_fluid", provider.getSourceFluid());
                helper.register("flowing_" + name, provider.getFlowingFluid());
            }));
    }
}
