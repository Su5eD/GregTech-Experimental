package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTab;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.block.ResourceBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class ModObjects { 
    public static final ModObjects INSTANCE = new ModObjects();
    private static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(GregTechTab.INSTANCE);

    private ModObjects() {}
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        Arrays.stream(ModBlock.values())
            .map(block -> block.getBlockInstance().setRegistryName(Reference.MODID, block.registryName))
            .forEach(registry::register);
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        
        Arrays.stream(ModBlock.values())
            .map(block -> block.getItemInstance().setRegistryName(Reference.MODID, block.registryName))
            .forEach(registry::register);
    }

    public enum ModBlock {
        ADVANCED_MACHINE_CASING(ConnectedBlock::new, 3, 30),
        ALUMINIUM(3, 30),
        BRASS(3.5F, 30),
        CHROME(10, 100),
        ELECTRUM(4, 30),
        FUSION_COIL(4, 30),
        GREEN_SAPPHIRE(4.5F, 30),
        HIGHLY_ADVANCED_MACHINE(10, 100),
        INVAR(4.5F, 30),
        IRIDIUM(3.5F, 600),
        IRIDIUM_REINFORCED_STONE(100, 300),
        IRIDIUM_REINFORCED_TUNGSTEN_STEEL(ConnectedBlock::new, 200, 400),
        LEAD(3, 60),
        LESUBLOCK(4, 30),
        NICKEL(3, 45),
        OLIVINE(4.5F, 30),
        OSMIUM(4, 900),
        PLATINUM(4, 30),
        REINFORCED_MACHINE_CASING(ConnectedBlock::new, 3, 60),
        RUBY(4.5F, 30),
        SAPPHIRE(4.5F, 30),
        SILVER(3, 30),
        STANDARD_MACHINE_CASING(ConnectedBlock::new, 3, 30),
        STEEL(3, 100),
        TITANIUM(10, 200),
        TUNGSTEN(4.5F, 100),
        TUNGSTEN_STEEL(ConnectedBlock::new, 100, 300),
        ZINC(3.5F, 30);

        private final String registryName;
        private final Lazy<Block> blockInstance;
        private final Lazy<Item> itemInstance;
        
        ModBlock(float strength, float resistance) {
            this(() -> new ResourceBlock(strength, resistance));
        }
        
        ModBlock(BiFunction<Float, Float, Block> constructor, float strength, float resistance) {
            this(() -> constructor.apply(strength, resistance));
        }

        ModBlock(Supplier<Block> constructor) {
            this.registryName = getName() + "_block";
            this.blockInstance = Lazy.of(constructor);
            this.itemInstance = Lazy.of(() -> new BlockItem(getBlockInstance(), ITEM_PROPERTIES));
        }

        public Item getItemInstance() {
            return this.itemInstance.get();
        }

        public Block getBlockInstance() {
            return this.blockInstance.get();
        }
        
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
