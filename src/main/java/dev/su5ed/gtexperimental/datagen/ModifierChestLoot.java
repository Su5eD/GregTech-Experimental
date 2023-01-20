package dev.su5ed.gtexperimental.datagen;

import net.minecraft.data.loot.ChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import one.util.streamex.StreamEx;

import java.util.function.BiConsumer;

public class ModifierChestLoot extends ChestLoot {

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		StreamEx.of(LootModifiers.INSTANCE.entries.values())
			.mapToEntry(LootModifiers.ModifierEntry::location, LootModifiers.ModifierEntry::builder)
			.forKeyValue(consumer);
    }
}
