package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTab;
import dev.su5ed.gtexperimental.block.LightSourceBlock;
import dev.su5ed.gtexperimental.compat.DamagedIC2ReactorComponentIngredient;
import dev.su5ed.gtexperimental.recipe.crafting.ToolCraftingIngredient;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import dev.su5ed.gtexperimental.recipe.type.VanillaDamagedIngredient;
import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.BlockItemProvider;
import dev.su5ed.gtexperimental.util.FluidProvider;
import dev.su5ed.gtexperimental.util.ItemProvider;
import dev.su5ed.gtexperimental.util.loot.FortuneLootFunction;
import dev.su5ed.gtexperimental.util.loot.LocationLootItem;
import dev.su5ed.gtexperimental.util.loot.ModLoadedCondition;
import dev.su5ed.gtexperimental.util.loot.RandomOreDrops;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import one.util.streamex.StreamEx;

public final class ModObjects {
    public static final Lazy<Block> LIGHT_SOURCE_BLOCK = Lazy.of(LightSourceBlock::new);

    private ModObjects() {}

    public static Item.Properties itemProperties() {
        return new Item.Properties().tab(GregTechTab.INSTANCE);
    }

    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
            StreamEx.<BlockItemProvider>of(ModBlock.values())
                .append(Ore.values())
                .append(GTBlockEntity.values())
                .mapToEntry(BlockItemProvider::getRegistryName, BlockItemProvider::getBlock)
                .forKeyValue(helper::register);

            helper.register("light_source", LIGHT_SOURCE_BLOCK.get());
        });

        event.register(ForgeRegistries.Keys.ITEMS, helper -> StreamEx.<ItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(Ingot.values())
            .append(Nugget.values())
            .append(Rod.values())
            .append(Dust.values())
            .append(Smalldust.values())
            .append(Plate.values())
            .append(TurbineRotor.values())
            .append(Component.values())
            .append(GTBlockEntity.values())
            .append(ModCoverItem.values())
            .append(Tool.values())
            .append(Upgrade.values())
            .append(Miscellaneous.values())
            .append(ColorSpray.values())
            .append(Wrench.values())
            .append(JackHammer.values())
            .append(Hammer.values())
            .append(Saw.values())
            .append(File.values())
            .append(NuclearCoolantPack.values())
            .append(NuclearFuelRod.values())
            .append(Armor.values())
            .append(ModFluid.values())
            .mapToEntry(ItemProvider::getRegistryName, ItemProvider::getItem)
            .forKeyValue(helper::register));

        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> StreamEx.<BlockEntityProvider>of(GTBlockEntity.values())
            .mapToEntry(BlockEntityProvider::getRegistryName, BlockEntityProvider::getType)
            .forKeyValue(helper::register));

        event.register(ForgeRegistries.Keys.FLUID_TYPES, helper -> StreamEx.of(ModFluid.values())
            .mapToEntry(FluidProvider::getFluidRegistryName, FluidProvider::getType)
            .forKeyValue(helper::register));
        
        event.register(ForgeRegistries.Keys.FLUIDS, helper -> StreamEx.of(ModFluid.values())
            .forEach(provider -> {
                String name = provider.getName();
                helper.register(name + "_fluid", provider.getSourceFluid());
                helper.register("flowing_" + name, provider.getFlowingFluid());
            }));
        
        event.register(Registry.LOOT_FUNCTION_REGISTRY, helper -> helper.register(FortuneLootFunction.NAME, FortuneLootFunction.TYPE));
        
        event.register(Registry.LOOT_ITEM_REGISTRY, helper -> {
            helper.register(RandomOreDrops.NAME, RandomOreDrops.TYPE);
            helper.register(ModLoadedCondition.NAME, ModLoadedCondition.TYPE);
        });
        
        event.register(Registry.LOOT_ENTRY_REGISTRY, helper -> helper.register(LocationLootItem.NAME, LocationLootItem.TYPE));
        
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
            CraftingHelper.register(SelectedProfileCondition.Serializer.INSTANCE);
            CraftingHelper.register(VanillaFluidIngredient.Serializer.NAME, VanillaFluidIngredient.Serializer.INSTANCE);
            CraftingHelper.register(DamagedIC2ReactorComponentIngredient.NAME, DamagedIC2ReactorComponentIngredient.SERIALIZER);
            CraftingHelper.register(VanillaDamagedIngredient.NAME, VanillaDamagedIngredient.SERIALIZER);
            CraftingHelper.register(ToolCraftingIngredient.Serializer.NAME, ToolCraftingIngredient.Serializer.INSTANCE);
        });
    }
}
