package dev.su5ed.gregtechmod.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.ModBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

class LootTableGen extends LootTableProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private final DataGenerator generator;

    public LootTableGen(DataGenerator pGenerator) {
        super(pGenerator);

        this.generator = pGenerator;
    }

    private void addTables() {
        StreamEx.of(ModBlock.values())
            .map(ModBlock::getBlock)
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
