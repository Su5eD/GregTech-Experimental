package dev.su5ed.gregtechmod.datagen;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.object.Tool;
import dev.su5ed.gregtechmod.util.loot.LocationLootItem;
import dev.su5ed.gregtechmod.util.loot.ModLoadedCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Collection;
import java.util.Map;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class LootModifiers {
    public static final LootModifiers INSTANCE = new LootModifiers();

    public final Multimap<ResourceLocation, ModifierEntry> entries = LinkedHashMultimap.create();

    private LootModifiers() {
        add(BuiltInLootTables.ABANDONED_MINESHAFT,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(item(Ingot.SILVER, 1, 4, 12))
                    .add(item(Ingot.LEAD, 1, 4, 3))
                    .add(item(Ingot.STEEL, 1, 4, 6))
                    .add(optionalItem(ModHandler.IC2_MODID, "bronze_ingot", 1, 4, 6))
                    .add(item(Items.EMERALD, 1, 4, 2))
                    .add(item(Miscellaneous.RUBY, 1, 4, 2))
                    .add(item(Miscellaneous.SAPPHIRE, 1, 4, 2))
                    .add(item(Miscellaneous.GREEN_SAPPHIRE, 1, 4, 2))
                    .add(item(Miscellaneous.OLIVINE, 1, 4, 2))
                    .add(item(Miscellaneous.RED_GARNET, 1, 4, 4))
                    .add(item(Miscellaneous.YELLOW_GARNET, 1, 4, 4))
                    .add(EmptyLootItem.emptyItem().setWeight(45)))
                .withPool(LootPool.lootPool()
                    .name("sprays")
                    .add(LootItem.lootTableItem(Tool.ICE_SPRAY).setWeight(20))
                    .add(LootItem.lootTableItem(Tool.BUG_SPRAY).setWeight(20))
                    .add(EmptyLootItem.emptyItem().setWeight(40))));

        add(BuiltInLootTables.DESERT_PYRAMID,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(item(Ingot.SILVER, 4, 16, 12))
                    .add(item(Ingot.PLATINUM, 2, 8, 4))
                    .add(item(Miscellaneous.RUBY, 2, 8, 2))
                    .add(item(Miscellaneous.SAPPHIRE, 2, 8, 2))
                    .add(item(Miscellaneous.OLIVINE, 2, 8, 2))
                    .add(item(Miscellaneous.RED_GARNET, 2, 8, 4))
                    .add(item(Miscellaneous.YELLOW_GARNET, 2, 8, 4))
                    .add(EmptyLootItem.emptyItem().setWeight(30))));

        add(BuiltInLootTables.JUNGLE_TEMPLE,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(optionalItem(ModHandler.IC2_MODID, "bronze_ingot", 4, 16, 12))
                    .add(item(Miscellaneous.RUBY, 2, 8, 2))
                    .add(item(Miscellaneous.SAPPHIRE, 2, 8, 2))
                    .add(item(Miscellaneous.GREEN_SAPPHIRE, 2, 8, 2))
                    .add(item(Miscellaneous.OLIVINE, 2, 8, 2))
                    .add(item(Miscellaneous.RED_GARNET, 2, 8, 4))
                    .add(item(Miscellaneous.YELLOW_GARNET, 2, 8, 4))
                    .add(EmptyLootItem.emptyItem().setWeight(28))));

        add(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(item(Items.FIRE_CHARGE, 2, 8, 30))
                    .add(EmptyLootItem.emptyItem().setWeight(30))));

        add(BuiltInLootTables.SIMPLE_DUNGEON,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(item(Ingot.SILVER, 1, 6, 120))
                    .add(item(Ingot.LEAD, 1, 6, 30))
                    .add(item(Ingot.STEEL, 1, 6, 60))
                    .add(optionalItem(ModHandler.IC2_MODID, "bronze_ingot", 1, 6, 60))
                    .add(item(Items.EMERALD, 1, 6, 20))
                    .add(item(Miscellaneous.RUBY, 1, 6, 20))
                    .add(item(Miscellaneous.SAPPHIRE, 1, 6, 20))
                    .add(item(Miscellaneous.GREEN_SAPPHIRE, 1, 6, 20))
                    .add(item(Miscellaneous.OLIVINE, 1, 6, 20))
                    .add(item(Miscellaneous.RED_GARNET, 1, 6, 40))
                    .add(item(Miscellaneous.YELLOW_GARNET, 1, 6, 40))
                    .add(EmptyLootItem.emptyItem().setWeight(450)))
                .withPool(LootPool.lootPool()
                    .name("sprays")
                    .add(LootItem.lootTableItem(Tool.ICE_SPRAY).setWeight(20))
                    .add(LootItem.lootTableItem(Tool.HYDRATION_SPRAY).setWeight(20))
                    .add(EmptyLootItem.emptyItem().setWeight(40))));

        add(BuiltInLootTables.STRONGHOLD_CROSSING,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(item(Ingot.STEEL, 8, 32, 12))
                    .add(optionalItem(ModHandler.IC2_MODID, "bronze_ingot", 8, 32, 12))
                    .add(EmptyLootItem.emptyItem().setWeight(24)))
                .withPool(LootPool.lootPool()
                    .name("sprays")
                    .add(LootItem.lootTableItem(Tool.HYDRATION_SPRAY).setWeight(20))
                    .add(EmptyLootItem.emptyItem().setWeight(20))));
        
        add(BuiltInLootTables.VILLAGE_TOOLSMITH,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .name("materials")
                    .setRolls(ConstantValue.exactly(1))
                    .add(item(Ingot.STEEL, 4, 8, 12))
                    .add(item(Ingot.BRASS, 4, 8, 12))
                    .add(optionalItem(ModHandler.IC2_MODID, "bronze_ingot", 4, 8, 12))
                    .add(EmptyLootItem.emptyItem().setWeight(36))));
    }

    public Map<ResourceLocation, Collection<ModifierEntry>> getEntries() {
        return this.entries.asMap();
    }

    private LootPoolEntryContainer.Builder<?> item(ItemLike item, int minCount, int maxCount, int weight) {
        return LootItem.lootTableItem(item)
            .setWeight(weight)
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
    }

    private LootPoolEntryContainer.Builder<?> optionalItem(String modid, String name, int minCount, int maxCount, int weight) {
        return LocationLootItem.lootTableItem(new ResourceLocation(modid, name))
            .when(ModLoadedCondition.modLoaded(modid))
            .setWeight(weight)
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
    }

    private void add(ResourceLocation name, LootTable.Builder builder) {
        entries.put(name, new ModifierEntry(location(name.getPath()), builder));
    }

    public record ModifierEntry(ResourceLocation location, LootTable.Builder builder) {}
}
