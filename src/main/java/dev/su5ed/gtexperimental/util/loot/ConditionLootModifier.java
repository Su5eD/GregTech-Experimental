package dev.su5ed.gtexperimental.util.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.su5ed.gtexperimental.api.Reference;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

/**
 * A global loot modifier used to inject the additional chest loot to the vanilla loot tables.
 * <p>
 * Source: <a href="https://github.com/ForestryMC/ForestryMC/blob/f46a19abf33b872cb2afefab2fcd5b02f992dac9/src/main/java/forestry/core/loot/ConditionLootModifier.java">Forestry</a>
 */
public class ConditionLootModifier extends LootModifier {
    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_REGISTRAR = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MODID);
    
    public static final RegistryObject<Codec<ConditionLootModifier>> CODEC = LOOT_MODIFIER_REGISTRAR.register("condition_loot_modifier", () ->
        RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ResourceLocation.CODEC.fieldOf("tableLocation").forGetter(m -> m.tableLocation))
            .apply(inst, ConditionLootModifier::new)));

    private final ResourceLocation tableLocation;
    
    /**
     * Helper field to prevent an endless method loop caused by forge in {@link LootTable#getRandomItems(LootContext)}
     * which calls this method again, since it keeps the {@link LootContext#getQueriedLootTableId()} value, which causes
     * "getRandomItems" to calling this method again, because the conditions still met even that it is another loot table.
     */
    private boolean operates = false;

    public ConditionLootModifier(ResourceLocation location) {
        super(new LootItemCondition[] { LootTableIdCondition.builder(location).build() });
        this.tableLocation = location;
    }

    private ConditionLootModifier(LootItemCondition[] conditions, ResourceLocation location) {
        super(merge(conditions, LootTableIdCondition.builder(location).build()));
        this.tableLocation = location;
    }
    
    public static void init(IEventBus bus) {
        LOOT_MODIFIER_REGISTRAR.register(bus);
    }

    private static LootItemCondition[] merge(LootItemCondition[] conditions, LootItemCondition condition) {
        LootItemCondition[] newArray = Arrays.copyOf(conditions, conditions.length + 1);
        newArray[conditions.length] = condition;
        return newArray;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (this.operates) {
            return generatedLoot;
        }
        this.operates = true;
        ResourceLocation name = location(this.tableLocation.getPath());
        LootTable table = context.getLootTable(name);
        if (table != LootTable.EMPTY) {
            generatedLoot.addAll(table.getRandomItems(context));
        }
        this.operates = false;
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
