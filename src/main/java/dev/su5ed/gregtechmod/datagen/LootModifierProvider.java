package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.util.loot.ConditionLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import one.util.streamex.StreamEx;

public class LootModifierProvider extends GlobalLootModifierProvider {

	public LootModifierProvider(DataGenerator gen) {
		super(gen, Reference.MODID);
	}

	@Override
	protected void start() {
        StreamEx.of(LootModifiers.INSTANCE.entries.keys())
			.forEach(location -> add(location.getPath(), new ConditionLootModifier(location)));
	}
}
