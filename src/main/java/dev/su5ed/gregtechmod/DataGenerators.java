package dev.su5ed.gregtechmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.object.ModObjects;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataGenerators {
    public static final DataGenerators INSTANCE = new DataGenerators();

    private DataGenerators() {}

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(new BlockTagsGen(generator, helper));
            generator.addProvider(new LootTableGen(generator));
        }

        if (event.includeClient()) {
            generator.addProvider(new BlockStateGen(generator, helper));
            generator.addProvider(new ItemModelGen(generator, helper));
        }
    }

    private static class BlockStateGen extends BlockStateProvider {

        public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, Reference.MODID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            Arrays.stream(ModObjects.ModBlock.values())
                .forEach(modBlock -> {
                    Block block = modBlock.getBlockInstance();
                    ResourceLocation name = block.getRegistryName();
                    String path = name.getPath();
                    String texturePath = modBlock.getName();
                    
                    ModelFile model;
                    if (modBlock.getBlockInstance() instanceof ConnectedBlock) {
                        model = models().getBuilder(path)
                            .parent(models().getExistingFile(mcLoc("cube")))
                            .customLoader((blockModelBuilder, helper) -> {
                                ResourceLocation location = ClientSetup.getLoaderLocation(texturePath);
                                return new CustomLoaderBuilder<BlockModelBuilder>(location, blockModelBuilder, helper) { };
                            })
                            .end();
                    }
                    else {
                        ResourceLocation location = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + texturePath);
                        model = models().cubeAll(path, location);
                    }
                    
                    simpleBlock(block, model);
                });
        }
    }

    private static class ItemModelGen extends ItemModelProvider {

        public ItemModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, Reference.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            StreamEx.of(ModObjects.ModBlock.values())
                .map(block -> block.getItemInstance().getRegistryName().getPath())
                .mapToEntry(name -> modLoc("block/" + name))
                .forKeyValue(this::withExistingParent);
        }
    }

    private static class BlockTagsGen extends BlockTagsProvider {

        public BlockTagsGen(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Reference.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            List<TagAppender<Block>> tags = StreamEx.of(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
                .map(this::tag)
                .toList();

            StreamEx.of(ModObjects.ModBlock.values())
                .map(ModObjects.ModBlock::getBlockInstance)
                .forEach(block -> tags.forEach(tag -> tag.add(block)));
        }
    }

    private static class LootTableGen extends LootTableProvider {
        private static final Logger LOGGER = LogManager.getLogger();
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
        private final DataGenerator generator;

        public LootTableGen(DataGenerator pGenerator) {
            super(pGenerator);

            this.generator = pGenerator;
        }
        
        private void addTables() {
            StreamEx.of(ModObjects.ModBlock.values())
                .map(ModObjects.ModBlock::getBlockInstance)
                .forEach(this::addSimpleTable);
        }

        @Override
        public void run(HashCache pCache) {
            addTables();
            Map<ResourceLocation, LootTable> tables = EntryStream.of(this.lootTables)
                .mapKeys(BlockBehaviour::getLootTable)
                .mapValues(LootTable.Builder::build)
                .toImmutableMap();
            writeTables(pCache, tables);
        }

        private void writeTables(HashCache cache, Map<ResourceLocation, LootTable> tables) {
            Path outputFolder = this.generator.getOutputFolder();
            tables.forEach((key, lootTable) -> {
                Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
                try {
                    DataProvider.save(GSON, cache, LootTables.serialize(lootTable), path);
                } catch (IOException e) {
                    LOGGER.error("Couldn't write loot table {}", path, e);
                }
            });
        }

        protected void addSimpleTable(Block block) {
            LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block));
            LootTable.Builder table = LootTable.lootTable().withPool(builder);
            
            this.lootTables.put(block, table);
        }

        @Override
        public String getName() {
            return Reference.NAME + " LootTables";
        }
    }
}
