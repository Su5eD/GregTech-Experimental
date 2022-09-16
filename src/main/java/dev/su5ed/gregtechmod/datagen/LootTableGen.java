package dev.su5ed.gregtechmod.datagen;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

class LootTableGen extends LootTableProvider {

    public LootTableGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(
            Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((location, lootTable) -> LootTables.validate(validationtracker, location, lootTable));
    }
    
    private static class ModBlockLoot extends BlockLoot {
        private final List<Block> blocks = StreamEx.<BlockItemProvider>of(ModBlock.values())
            .append(GTBlockEntity.values())
            .map(BlockItemProvider::getBlock)
            .toList();
        
        @Override
        protected void addTables() {
            this.blocks.forEach(this::dropSelf);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return this.blocks;
        }
    }

    @Override
    public String getName() {
        return Reference.NAME + " Loot Tables";
    }
}
