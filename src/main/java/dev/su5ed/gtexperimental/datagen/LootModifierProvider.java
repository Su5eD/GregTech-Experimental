package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.util.loot.ConditionLootModifier;
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
