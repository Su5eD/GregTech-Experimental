package dev.su5ed.gregtechmod.datagen;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Dust;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import dev.su5ed.gregtechmod.util.ItemProvider;
import dev.su5ed.gregtechmod.util.loot.FortuneLootFunction;
import dev.su5ed.gregtechmod.util.loot.LocationLootItem;
import dev.su5ed.gregtechmod.util.loot.ModLoadedCondition;
import dev.su5ed.gregtechmod.util.loot.RandomOreDrops;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import one.util.streamex.EntryStream;
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
            Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK),
            Pair.of(ModifierChestLoot::new, LootContextParamSets.CHEST)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((location, lootTable) -> LootTables.validate(validationtracker, location, lootTable));
    }
    
    private static class ModBlockLoot extends BlockLoot {
        private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item()
            .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
        private static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();
        
        private final List<Block> blocks = StreamEx.<BlockItemProvider>of(ModBlock.values())
            .append(GTBlockEntity.values())
            .append(Ore.values())
            .map(BlockItemProvider::getBlock)
            .toList();
        
        @Override
        protected void addTables() {
            this.blocks.forEach(this::dropSelf);
            
            addOreDrop(Ore.GALENA, Miscellaneous.RAW_GALENA);
            addOreDrop(Ore.BAUXITE, Miscellaneous.RAW_BAUXITE);
            addOreDrop(Ore.TUNGSTATE, Miscellaneous.RAW_TUNGSTATE);
            addOreDrop(Ore.SHELDONITE, Miscellaneous.RAW_SHELDONITE);
            addOreDrop(Ore.TETRAHEDRITE, Miscellaneous.RAW_TETRAHEDRITE);
            addOreDrop(Ore.CASSITERITE, Miscellaneous.RAW_CASSITERITE);
            
            addOre(Ore.RUBY.getBlock())
                .primaryDrop(Miscellaneous.RUBY)
                .randomSecondaryDrop(Miscellaneous.RED_GARNET, 32)
                .add();
            addOre(Ore.SAPPHIRE.getBlock())
                .primaryDrop(Miscellaneous.SAPPHIRE)
                .randomSecondaryDrop(Miscellaneous.GREEN_SAPPHIRE, 64)
                .add();
            addOre(Ore.CINNABAR.getBlock())
                .primaryDrop(Dust.CINNABAR, 2)
                .randomSecondaryDrop(Items.REDSTONE, 4)
                .add();
            addOre(Ore.SPHALERITE.getBlock())
                .primaryDrop(Dust.SPHALERITE, 2)
                .randomSecondaryDrop(Dust.ZINC, 4)
                .randomSecondaryDrop(Miscellaneous.YELLOW_GARNET, 32)
                .add();
            addOre(Ore.OLIVINE.getBlock())
                .primaryDrop(Miscellaneous.OLIVINE)
                .add();
            addOre(Ore.SODALITE.getBlock())
                .primaryDrop(Dust.SODALITE, 6, 3)
                .randomSecondaryDrop(Dust.ALUMINIUM, 4)
                .add();
            addOre(Ore.PYRITE.getBlock())
                .primaryDrop(Dust.PYRITE, 2)
                .add();

            add(Ore.IRIDIUM.getBlock(), block -> {
                LootPoolEntryContainer.Builder<?> builder = EntryStream.of(ModHandler.getAllModItems("iridium_ore"))
                    .<LootPoolEntryContainer.Builder<?>>mapKeyValue((modid, item) -> applyExplosionDecay(block, LocationLootItem.lootTableItem(item)
                        .apply(FortuneLootFunction.rareOreDrop())
                        .when(ModLoadedCondition.modLoaded(modid))))
                    .reduce(LootItem.lootTableItem(block).when(HAS_SILK_TOUCH), LootPoolEntryContainer.Builder::otherwise);
                return LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(builder));
            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return this.blocks;
        }
        
        protected void addOreDrop(BlockItemProvider block, ItemProvider item) {
            add(block.getBlock(), b -> createOreDrop(b, item.asItem()));
        }
        
        protected GTLootTableFactory addOre(Block block) {
            return new GTLootTableFactory(block, this::add);
        }
        
        protected static class GTLootTableFactory {
            private final Block block;
            private final BiConsumer<Block, LootTable.Builder> finisher;

            public GTLootTableFactory(Block block, BiConsumer<Block, LootTable.Builder> finisher) {
                this.block = block;
                this.finisher = finisher;
            }
            
            public GTLootTableBuilder primaryDrop(ItemLike item) {
                return primaryDrop(item, 1);
            }
            
            public GTLootTableBuilder primaryDrop(ItemLike item, int count) {
                return primaryDrop(item, count, 1);
            }
            
            public GTLootTableBuilder primaryDrop(ItemLike item, int count, int bonusCountMultiplier) {
                LootPoolSingletonContainer.Builder<?> pool = LootItem.lootTableItem(item);
                if (count > 1) {
                    pool = pool.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
                }
                pool = pool.apply(FortuneLootFunction.bonusDrop(bonusCountMultiplier));
                LootTable.Builder tableBuilder = createSilkTouchDispatchTable(this.block, applyExplosionDecay(this.block, pool));
                return new GTLootTableBuilder(this.block, this.finisher, tableBuilder);
            }
        }
        
        private static class GTLootTableBuilder {
            private final Block block;
            private final BiConsumer<Block, LootTable.Builder> finisher;

            private LootTable.Builder builder;
            
            public GTLootTableBuilder(Block block, BiConsumer<Block, LootTable.Builder> finisher, LootTable.Builder builder) {
                this.block = block;
                this.finisher = finisher;
                this.builder = builder;
            }

            public GTLootTableBuilder randomSecondaryDrop(ItemLike item, int chance) {
                this.builder = this.builder
                    .withPool(new LootPool.Builder()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(item))
                        .when(HAS_NO_SILK_TOUCH)
                        .when(RandomOreDrops.randomDrop(chance))
                    );
                return this;
            }
            
            public void add() {
                this.finisher.accept(this.block, this.builder);
            }
        }
    }

    @Override
    public String getName() {
        return Reference.NAME + super.getName();
    }
}
